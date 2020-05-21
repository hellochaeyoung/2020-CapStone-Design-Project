package com.Bridge.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {


    private TextView toPPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ActionBar action= getSupportActionBar();
        action.hide();
        initView();


    }
    public void initView(){
        toPPT=findViewById(R.id.toPPT);
        toPPT.setOnClickListener(pptListener);


    }

    public View.OnClickListener pptListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(FirstActivity.this,MainActivity.class);
            startActivity(intent);

        }
    };


}
