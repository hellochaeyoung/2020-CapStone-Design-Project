package com.Bridge.bridge;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.Bridge.bridge.Client.ClientThread;
import com.Bridge.bridge.Client.SendThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int BUFSIZE=10000;

    public static boolean isFinish=false;
    private ImageView image;
    private Button camera;
    private Button gallery;
    private Button button;

    private String filePath;

    public  ClientThread mClientThread;

    private File file;
    private Uri photoUri;
    private String imageFilePath;


    private static final int CAMERA=0;
    private static final int GALLERY=1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance("no");

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard,"capture.jpg");
        if(mClientThread==null){
            String serverIP="223.194.152.65";
            if(serverIP.length()!=0){
                mClientThread=new ClientThread(serverIP,mMainHandler);
                mClientThread.start();

            }

        }



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
        public void onClick(View v) {
            sendTakePhotoIntent();
        }

    };

    private void sendTakePhotoIntent(){
        Intent photoIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(photoIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException ex){
                System.out.println("error in camera button");
            }
            if(photoFile != null){
                photoUri = FileProvider.getUriForFile(this, getPackageName(),photoFile);
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);

                startActivityForResult(photoIntent,CAMERA);
            }
        }
        // startActivityForResult(intent,CAMERA);

    }
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
            dialogFragment.show(getSupportFragmentManager(),"dialog");
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
                   // bitmap = ImageUtils.getInstant().getCompressedBitmap()
                    image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == CAMERA) {
               // Bundle bundle = data.getExtras();
               // BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 8;
                // bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);

//                bitmap = (Bitmap) bundle.get("data");
//                image.setImageBitmap(bitmap);
               // image.setImageURI(photoUri);

                // bitmap = BitmapFactory.decodeFile(imageFilePath);
                bitmap = ImageUtils.getInstant().getCompressedBitmap(imageFilePath);

                ExifInterface exif = null;

                try {
                    exif = new ExifInterface(imageFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int exifOrientation;
                int exifDegree;

                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegrees(exifOrientation);
                } else {
                    exifDegree = 0;
                }
                image.setImageBitmap(rotate(bitmap, exifDegree));

            }

        }


    };


    public static String saveBitmapToJpeg(Context context,Bitmap bitmap, String name){
        File storage=context.getCacheDir();
        String fileName=name+".jpg";
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


    public Handler mMainHandler=new Handler(){
        public void handleMessage(Message msg){
            System.out.println("no handler??");
            switch(msg.what){
                case 1:

                    System.out.println("@@@@@@@@@@@@@@@@");
                    break;

                case 3: System.out.println("hi here");
                    dialogFragment.dismiss();
                    Intent intent = new Intent(MainActivity.this, RecvActivity.class);
                    startActivity(intent);
                    finish();

                    break;

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

    public File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" +timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;

    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        String profileImageBase64 = Base64.encodeToString(image, 0);

        return profileImageBase64;
    }
}