package com.asad.sgcfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class PermissionActivity extends AppCompatActivity
{

    Handler handler;
    int PERMISSION_ALL = 1;
    Intent homeIntent;

    Button granted;

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
        setContentView(R.layout.activity_permission);
        granted=findViewById(R.id.btn_continue);
        granted.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CheckPermissions();
            }
        });
        handler=new Handler();
        handler.postDelayed(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        CheckPermissions();
                    }
                }, 200);
    }

    protected  void CheckPermissions()
    {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                ||(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
        {
            handler=new Handler();
            handler.postDelayed(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            GetPermissions();
                        }
                    }, 200);
        }
        else
        {
            StartHomeActivity();

        }
    }



    public  void GetPermissions()
    {
        if (!hasPermissions(this, PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public  boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    protected  void StartHomeActivity()
    {
        homeIntent= new Intent(this,HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(homeIntent);
        finish();
    }



}
