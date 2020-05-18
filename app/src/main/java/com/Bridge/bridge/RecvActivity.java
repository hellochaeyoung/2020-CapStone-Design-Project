package com.Bridge.bridge;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class RecvActivity extends BaseActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    Button sharebtn;
    Button openbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initButton();
    }

    public void initButton(){
        sharebtn = findViewById(R.id.Share);
        openbtn = findViewById(R.id.App);

        sharebtn.setOnClickListener(shareListener);
        openbtn.setOnClickListener(openListener);

    }

    public View.OnClickListener openListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File file= new File("/sdcard/Download/test.pptx");
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");
            try{
                startActivity(intent);

            }catch(ActivityNotFoundException e){
                Toast.makeText(RecvActivity.this, "파일을 열 수 있는 PowerPoint 어플리케이션이 없습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    public View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File saveFile = new File("/sdcard/Download/test.pptx");
            shareFile(saveFile);
        }
    };

    public void shareFile(File shareFile) {

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);           // 단일파일 보내기
        intent.setType("application/excel");    // 엑셀파일 공유 시
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", shareFile);
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        // intent.setType("*/*");        // 모든 공유 형태 전달
        //intent.putExtra(Intent.EXTRA_SUBJECT, "공유 제목");  // 제목
        // intent.putExtra(Intent.EXTRA_TEXT, "공유 내용");     // 내용

        Log.i(TAG, "test.file.getpath="+shareFile.getPath());

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);     // 공유 앱에 권한 주기
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);    // 공유 앱에 권한 주기
        startActivity(Intent.createChooser(intent, "공유 타이틀"));
    }





    @Override
    protected void onResume() {
        super.onResume();
        ensurePermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}