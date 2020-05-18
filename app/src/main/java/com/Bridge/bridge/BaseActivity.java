package com.Bridge.bridge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {


    /**
     * Permission Setting
     * @param permissions
     */
    protected void ensurePermissions(String... permissions) {
        ArrayList<String> deniedPermissionList = new ArrayList<>();

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission))
                deniedPermissionList.add(permission);
        }

        if (!deniedPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, deniedPermissionList.toArray(new String[0]), 0);
        } else {
            onGrantedPermissions();
        }
    }

    protected void onGrantedPermissions() {

    }

    protected void showAlertDialog(final String message, final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("권한 설정 후 이용");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                showAlertDialog("Requested permission is not granted.", true);
                onGrantedPermissions();
            }
        }
    }
}
