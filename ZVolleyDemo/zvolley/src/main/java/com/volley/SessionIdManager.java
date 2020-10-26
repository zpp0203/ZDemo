package com.volley;

import android.text.TextUtils;

import com.foreasy.extend.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by Ric on 2017/3/12.
 * 会话Id 每次打开App创建一次
 */

public class SessionIdManager {

    private static String sessionId = "";

    public synchronized static String getInstallationId() {
        if (TextUtils.isEmpty(sessionId)) {
            File sessionIdFile = new File(MyApplication.getInstance().getFilesDir(), "SessionId");

            try {
                if (!sessionIdFile.exists()) {
                    writeSessionIdFile(sessionIdFile);
                }

                sessionId = readSessionIdFile(sessionIdFile);
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }
        return sessionId;
    }

    private static String readSessionIdFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeSessionIdFile(File installation) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(installation);
            //UUID是1.5中新增的一个类，在java.util下，用它可以产生一个号称全球唯一的ID。(每秒产生10000000个UUID，则可以保证（概率意义上）3240年不重复。 )
            String e = "and" + UUID.randomUUID().toString();
            out.write(e.getBytes());
            out.close();
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
}

