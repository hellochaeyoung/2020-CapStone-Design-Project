package com.Bridge.bridge;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Bridge.bridge.Client.ClientThread;
import com.Bridge.bridge.Client.SendThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final int BUFSIZE=10000;

    public static boolean isFinish=false;
    private ImageView image;
    private Button camera;
    private Button gallery;
    private Button button;

    private String filePath;

    public ClientThread mClientThread;


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

        if(mClientThread==null){
            String serverIP="172.16.100.35";
            if(serverIP.length()!=0){
                mClientThread=new ClientThread(serverIP,mMainHandler);
                mClientThread.start();

            }

        }

        verifyStoragePermissions(MainActivity.this);
        init();

        while(true){
            if(isFinish==true){
                Intent intent = new Intent(MainActivity.this, RecvActivity.class);
                startActivity(intent);
                break;
            }
        }


    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actionbar_action,menu);
        return true;
    }
    public boolean onOptionsItemSelected( MenuItem item){
        Intent intent=new Intent(MainActivity.this, MyActivity.class);
        startActivity(intent);

        finish();
        return super.onOptionsItemSelected(item);

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

        byte[] b=new byte[1000];
        b="}".getBytes();
        for(int i=0;i<b.length;i++){
            System.out.println(b[i]+" ");
        }

    }


    View.OnClickListener cameraListener=new View.OnClickListener(){
        public void onClick(View v){
            Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA);        }
    };

    View.OnClickListener galleryListener=new View.OnClickListener(){
        public void onClick(View v){
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent,GALLERY);        }
    };


    View.OnClickListener buttonListener=new View.OnClickListener(){
        public void onClick(View v){
            //SaveBitmaptoFile(bitmap,"pic");
            filePath=saveBitmapToJpeg(getApplicationContext(),bitmap,"pic");
            SocketConnect();

        }
    };




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


    };


    public static String saveBitmapToJpeg(Context context,Bitmap bitmap, String name){
        File storage=context.getCacheDir();
        String fileName=name+".jpeg";
        //draw.jpeg;
        File tempFile=new File(storage,fileName);

        try{
            tempFile.createNewFile();
            FileOutputStream out=new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.close();
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();
    }


    public void SocketConnect(){

        if(SendThread.mHandler!=null){
            System.out.println("here!!!!!!!!");
            Message msg=Message.obtain();
            File file=new File(filePath);
           // byte[] sendData=bitmapToByteArray(bitmap);
            String sendData=bitmapToString(bitmap);
            sendData+="}";
            msg.what=1;
            msg.obj=sendData;
            System.out.println(sendData+"}");
            //data
            SendThread.mHandler.sendMessage(msg);
            System.out.println("hello");
        }
    }


    private Handler mMainHandler=new Handler(){
        public void handlerMessage(Message msg){
            switch(msg.what){
                case 1:
                    //data print
            }
        }
    };




    public byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray =stream.toByteArray();
        return byteArray;
    }



    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        String profileImageBase64 = Base64.encodeToString(image, 0);

        return profileImageBase64;
    }
}