package com.zpp.demo.view.system;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.zandroid.tools.StringUtils;
import com.zandroid.widget.CustomDialog;
import com.zpp.demo.BuildConfig;
import com.zpp.demo.R;
import com.zpp.demo.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.OnClick;

public class CropImgActivity extends BaseActivity {

    @Bind(R.id.crop_im)
    ImageView imageView;

    //拍照后照片的Uri
    //private Uri imageUri;
    private String imagePath;
    //裁剪图片存放地址的Uri
    private Uri cropImageUri;
    private String cropImagePath;
    /** 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_crop_img;
    }



    @OnClick({R.id.crop_im})
    public void onClick(View v) {
        setHeadDialog();
    }

    CustomDialog dialog;
    /**
     * 显示选择对话框
     */
    private void setHeadDialog() {
        //权限检测
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CropImgActivity.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openDialog();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // 授权被允许
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("-------->", "访问相册请求被允许");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openDialog();
                } else {
                    Log.v("-------->", "访问相册请求被拒绝");
                }
                break;
            }
        }
    }
    private void openDialog(){
        CustomDialog.Builder builder = CustomDialog.Builder.getIntance(this);
        builder.cancelTouchout(false)
                .view(R.layout.dialog_head_layout)
                .gravity(Gravity.BOTTOM)
                .height(ViewGroup.LayoutParams.MATCH_PARENT)
                .width(ViewGroup.LayoutParams.MATCH_PARENT)
                .style(R.style.custom_dialog)
                .addViewOnclick(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                })
                .addViewOnclick(R.id.open_media, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        openMedia();
                    }
                })
                .addViewOnclick(R.id.openAlbum, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        openAlbum();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }
    //打开相机
    private void openMedia(){
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
                    imageUri= FileProvider.getUriForFile(mContext,BuildConfig.APPLICATION_ID+".fileProvider",outputImage);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                }else{
                    imageUri= Uri.fromFile(outputImage);
                }
                //启动相机程序
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加权限
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(mContext, "没有找到储存目录",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(mContext, "没有储存卡",Toast.LENGTH_LONG).show();
        }
    }
    //打开相册
    private void openAlbum(){
        Intent intent = new Intent((Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)?Intent.ACTION_OPEN_DOCUMENT:Intent.ACTION_GET_CONTENT, null);
        //intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    if(resultCode==RESULT_OK&&data!=null){
                        //判断手机系统版本号
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                            //4.4及以上系统使用这个方法处理图片
                            handlerImageOnKitKat(data);
                        }else{
                            //4.4以下系统使用这个方法处理图片
                            handlerImageBeforeKitKat(data);
                        }
                    }
                    break;
                case CAMERA_REQUEST_CODE://相机后
                    //进行裁剪
                    startPhotoZoom(imagePath);
                    //在手机相册中显示刚拍摄的图片
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(imagePath);
                    Uri uri = Uri.fromFile(file);// parse(pathUri);
                    mediaScanIntent.setData(uri);//imageUri
                    sendBroadcast(mediaScanIntent);
                    break;
                case RESULT_REQUEST_CODE: // 图片缩放完成后
                    if (data != null) {
                        getImageToView();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传数据
     *
     */
    private void getImageToView() {
        Log.e("--", "上传图片");
        try {
            final Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropImageUri));
            if (photo != null) {
                imageView.setImageBitmap(photo);
                File img = new File(new URI(cropImageUri.toString()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void startPhotoZoom(String imagePath) {
        cropImagePath=getExternalCacheDir()+"/crop_image.jpg";
        File cropPhoto= new File(cropImagePath);//new File(getExternalCacheDir(),"crop_image.jpg")
        try{
            if(cropPhoto.exists()){
                cropPhoto.delete();
            }
            cropPhoto.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        cropImageUri=Uri.fromFile(cropPhoto);

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

        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // 没有人脸检测
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //4.4以上
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handlerImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的URI，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath=uri.getPath();
        }
        Log.e(getLocalClassName(),"imagePath:"+imagePath);
        startPhotoZoom(imagePath);
    }

    private void handlerImageBeforeKitKat(Intent data){
        Uri cropUri=data.getData();
        startPhotoZoom(getImagePath(cropUri,null));
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
