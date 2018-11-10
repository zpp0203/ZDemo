package com.zpp.demo.tools;


import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TCPNioClient extends Thread{
    private final String TAG = "SocketService";
    // 信道选择器
    private Selector selector;
    // 与服务器通信的信道
    SocketChannel socketChannel;
    // 要连接的服务器Ip地址
    private String hostIp= "218.244.138.13";

    // 要连接的远程服务器在监听的端口
    private int hostListenningPort= 10623;

    private static final long HEART_BEAT_RATE = 3 * 1000;
    private long sendTime = 0L;

    @Override
    public void run() {
        initialize();
    }

    /**
     * 初始化
     *
     * @throws IOException
     */
    private void initialize(){
        try {
            // 获得一个Socket通道
            socketChannel = SocketChannel.open();
            // 设置通道为非阻塞
            socketChannel.configureBlocking(false);

            // 获得一个通道管理器
            this.selector = Selector.open();
            // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
            //用channel.finishConnect();才能完成连接

            socketChannel.connect(new InetSocketAddress(hostIp, hostListenningPort));
            //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            // 启动读取线程
            new TCPClientReadThread(selector);
            //初始化成功后，就准备发送心跳包
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
            Log.e(TAG,"初始化Socket");
        } catch (Exception e) {
            Log.e(TAG, "客户端无法连接服务器"+e.getMessage());
        }
    }

    /**
     * 发送字符串到服务器
     *
     * @param message
     * @throws IOException
     */
    public boolean sendMsg(String message) {
        try {
            ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
            socketChannel.write(writeBuffer);
            sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            Log.d(TAG,"发送："+message);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // For heart Beat
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(socketChannel.isConnected()) {
                            boolean isSuccess = sendMsg("heart");//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                            if (!isSuccess) {
                                mHandler.removeCallbacks(heartBeatRunnable);
                                closeSocket();
                                initialize();
                            } else {

                            }
                        }
                    }
                }).start();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };
    private void closeSocket() {
        try {
            if(socketChannel!=null && socketChannel.isConnected()){
                socketChannel.finishConnect();
                socketChannel.close();
                socketChannel=null;
            }
            if (null != selector) {
                selector.close();
                selector = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

