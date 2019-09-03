package com.asad.sgcfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import pl.droidsonroids.gif.GifImageView;

public class CodeViewer extends AppCompatActivity
{

    ImageButton btnShare;
    GifImageView imgV;
    String imageLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_viewer);
        btnShare=findViewById(R.id.btnShare);
        imgV=findViewById(R.id.imgV);
        Bundle bundle = getIntent().getExtras();
        imageLoc=bundle.getString("img");
        imgV.setImageURI(Uri.fromFile(new File(imageLoc)));

        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                shareImage();
            }
        });
    }

    private void shareImage()
    {
       try
       {
           StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
           StrictMode.setVmPolicy(builder.build());
           Intent shareIntent = new Intent();
           shareIntent.setAction(Intent.ACTION_SEND);
           shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imageLoc)));
           shareIntent.setType("*/*");
           startActivity(Intent.createChooser(shareIntent,"CodeViewer" ));
       }
       catch (Exception ex)
       {
           Log.d("errorrrrrrrrrr",ex.toString());
       }
    }

}
