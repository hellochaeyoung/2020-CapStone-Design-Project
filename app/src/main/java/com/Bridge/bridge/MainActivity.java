package com.Bridge.bridge;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    private ImageView image;
    private Button camera;
    private Button gallery;
    private Button button;
    private File file;

    private static final int CAMERA=0;
    private static final int GALLERY=1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);
        init();



    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //권한부


    private void init(){
        image=findViewById(R.id.image);
        camera=findViewById(R.id.camera);
        gallery=findViewById(R.id.gallery);
        button=findViewById(R.id.send);

        camera.setOnClickListener(cameraListener);
        gallery.setOnClickListener(galleryListener);
        button.setOnClickListener(buttonListener);

    }


    View.OnClickListener cameraListener=new View.OnClickListener(){
        public void onClick(View v){
            openCamera();
        }
    };

    View.OnClickListener galleryListener=new View.OnClickListener(){
        public void onClick(View v){
            openGallery();
        }
    };
    View.OnClickListener buttonListener=new View.OnClickListener(){
        public void onClick(View v){
            SaveBitmaptoFile(bitmap,"pic");
        }
    };



    private void openCamera(){
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA);
    }

    private void openGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GALLERY);
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == CAMERA) {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                image.setImageBitmap(bitmap);
            }

        }


    }


    //bitmap to image --> goto SDcard storage
    //저장소:stroage/emulated/0/MyDir
    public static void SaveBitmaptoFile(Bitmap bitmap,String name){
        String path= Environment.getExternalStorageDirectory().getAbsolutePath();
        path+="/MyDir/";
        File file=new File(path);
        System.out.println(path);
        if(!file.exists())
            file.mkdirs();

        OutputStream os=null;

        try{
            file.createNewFile();
            os=new FileOutputStream(path+"/pic.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.close();
            ///path+="/pic.jpeg";
            //   file=new File(path);
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}

