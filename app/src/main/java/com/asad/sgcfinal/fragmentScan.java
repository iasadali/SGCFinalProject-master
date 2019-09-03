package com.asad.sgcfinal;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class fragmentScan extends Fragment
{

    Button btn_scan;
    EditText pass;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        return inflater.inflate(R.layout.activity_fragment_scan, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        btn_scan=getView().findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //StartScan();
Scanner();
            }
        });

        pass=getView().findViewById(R.id.passss);



    }

    public void StartScan()
    {
        /*IntentIntegrator integrator = new IntentIntegrator(super.getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();*/


    }

    protected  void Scanner()
    {
        Intent next= new Intent(getActivity(),ContinuousCaptureActivity.class);
        next.putExtra("password",pass.getText().toString());
        this.startActivity(next);
    }








}