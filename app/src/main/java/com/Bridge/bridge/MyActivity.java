package com.Bridge.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.myactionbar_action,menu);
        return true;
    }
    public boolean onOptionsItemSelected( MenuItem item){
        Intent intent=new Intent(MyActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
        return super.onOptionsItemSelected(item);

    }
}

