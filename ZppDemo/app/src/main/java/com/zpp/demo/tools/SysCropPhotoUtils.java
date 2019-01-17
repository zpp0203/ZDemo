package com.zpp.demo.tools;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.zpp.demo.BuildConfig;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * 系统调用相册相机裁剪图片功能，集合成方法
 */

public class SysCropPhotoUtils {
    public static final String TAG = SysCropPhotoUtils.class.getSimpleName();

    private static SysCropPhotoUtils sInstance = null;

    //拍照后照片的Uri
    private String imagePath;
    //裁剪图片存放地址的Uri
    private String cropImagePath;
    /** 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;


    public SysCropPhotoUtils() {
        super();
    }

    public synchronized static SysCropPhotoUtils getInstance() {
        if (sInstance == null) {
            sInstance = new SysCropPhotoUtils();
        }
        return sInstance;
    }


    /* 调用系统相机拍照取图 */
    public void getPhotoFromCamera(Activity context){
        //先验证手机是否有sdcard
        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){
            //创建File对象，用于存储拍照后的照片
            imagePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/out_image.jpg";
            File outputImage=new File(imagePath);//SD卡的应用关联缓存目录
            outputImage.getParentFile().mkdirs();
            try {
                if(outputImage.exists()){
                    outputImage.delete();
                }
                outputImage.createNewFile();
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri=null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri= FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".fileProvider",outputImage);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                }else{
                    imageUri= Uri.fromFile(outputImage);
                }
                //启动相机程序
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加权限
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                context.startActivityForResult(intent,CAMERA_REQUEST_CODE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(context, "没有找到储存目录",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "没有储存卡",Toast.LENGTH_LONG).show();
        }
    }

    //打开相册
    public void getPhotoFromAlbum(Context context){
        Intent intent = new Intent((Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)?Intent.ACTION_OPEN_DOCUMENT:Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }

    /**
     * 对返回图片进行处理
     *
     * @param context
     * @param data
     * @param requestCode
     * @param resultCode
     * @return
     */
    public String handlePhotoResult(Activity context, Intent data, int requestCode, int resultCode) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    if(resultCode==RESULT_OK&&data!=null){
                        //判断手机系统版本号
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                            //4.4及以上系统使用这个方法处理图片
                            handlerImageOnKitKat(context,data);
                        }else{
                            //4.4以下系统使用这个方法处理图片
                            handlerImageBeforeKitKat(context,data);
                        }
                    }
                    break;
                case CAMERA_REQUEST_CODE://相机后
                    //进行裁剪
                    startPhotoZoom(context,imagePath);
                    //在手机相册中显示刚拍摄的图片
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(imagePath);
                    Uri uri = Uri.fromFile(file);// parse(pathUri);
                    mediaScanIntent.setData(uri);//imageUri
                    context.sendBroadcast(mediaScanIntent);
                    break;
                case RESULT_REQUEST_CODE: // 图片缩放完成后
                    if (data != null) {
                        return cropImagePath;
                    }
                    break;
            }
        }
        return null;
    }
    //裁剪图片
    public void startPhotoZoom(Activity activity,String imagePath) {
        cropImagePath=activity.getExternalCacheDir()+"/"+System.currentTimeMillis()+"crop_image.jpg";
        File cropPhoto= new File(cropImagePath);
        try{
            if(cropPhoto.exists()){
                cropPhoto.delete();
            }
            cropPhoto.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        Uri cropImageUri=Uri.fromFile(cropPhoto);

        Intent intent = new Intent("com.android.camera.action.CROP");
        File file = new File(imagePath);
        Uri uri = Uri.fromFile(file);// parse(pathUri);
        intent.setDataAndType(uri, "image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        int width=activity.getWindow().getAttributes().width;
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", width);//600

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // 没有人脸检测
        activity.startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //4.4以上
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handlerImageOnKitKat(Activity activity,Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(activity,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(activity,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(activity,contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的URI，则使用普通方式处理
            imagePath=getImagePath(activity,uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath=uri.getPath();
        }
        this.imagePath=imagePath;
        Log.e(TAG,"imagePath:"+imagePath);
        startPhotoZoom(activity,imagePath);
    }

    private void handlerImageBeforeKitKat(Activity activity,Intent data){
        Uri cropUri=data.getData();
        this.imagePath=getImagePath(activity,cropUri,null);
        startPhotoZoom(activity,imagePath);
    }

    private String getImagePath(Activity activity,Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
