package com.Bridge.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {


    private TextView toPPT;
    private TextView toMY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        initView();


    }
    public void initView(){
        toPPT=findViewById(R.id.toPPT);
        toPPT.setOnClickListener(pptListener);

        toMY=findViewById(R.id.toMY);
        toMY.setOnClickListener(myListener);
    }

    public View.OnClickListener pptListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(FirstActivity.this,MainActivity.class);
            startActivity(intent);

        }
    };

    public View.OnClickListener myListener=new View.OnClickListener(){
        public void onClick(View v){

            Intent intent=new Intent(FirstActivity.this, MyActivity.class);
            startActivity(intent);
        }
    };
}
