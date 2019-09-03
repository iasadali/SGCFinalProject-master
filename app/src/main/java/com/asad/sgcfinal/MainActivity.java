package com.asad.sgcfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    Handler splasher;
    Intent next;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS =
            {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        splasher=new Handler();
        splasher.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                CheckPermissions();
            }
        }, 1500);
    }



    protected  void CheckPermissions()
    {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        ||(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            StartPermissionActivity();
        }
        else
        {
            StartHomeActivity();
        }
    }


    protected  void StartPermissionActivity()
    {
        next= new Intent(this,PermissionActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(next);
        finish();
    }

    protected  void StartHomeActivity()
    {
        next= new Intent(this,HomeActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(next);
        finish();
    }
}
