package com.asad.sgcfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class fragmentGenerate extends Fragment
{

    public Boolean isGenerated;
    Button browse,generate;
    Switch encrypt;
    EditText loc,pass;
    Boolean sw;

    static final int REQUEST_FILE_BROWSE = 90;
    Uri selectedFile;
    String filePath,fileName;
    Generator generator;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.activity_fragment_generate, null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
HomeActivity.Waiter(false);

        browse=getView().findViewById(R.id.btn_browse);
        generate=getView().findViewById(R.id.btn_generate);
        loc=getView().findViewById(R.id.tb_loc);
        pass=getView().findViewById(R.id.tb_password);
        pass.setEnabled(false);
        sw=false;
        encrypt=getView().findViewById(R.id.encrypt_switch);
        encrypt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(sw==false)
                {
                    sw=true;
                    pass.setText("");
                    pass.setEnabled(true);
                }
                else
                {
                    sw=false;
                    pass.setText("");
                    pass.setEnabled(false);
                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BrowseDialogue(REQUEST_FILE_BROWSE);
            }
        });

        generate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if(loc.getText().length()<1)
                {
                    Toast.makeText(getActivity(),"Please Select a File!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HomeActivity.Waiter(true);
                    Handler work=new Handler();
                    work.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {

                                isGenerated=false;
                                Log.d("FRAG","CALLING METHOD");
                                isGenerated=GenerateSGC();
                                CheckStatus();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }, 200);
                }

            }
        });
    }


    protected  void StartViewer(String fileLoc)
    {
        Intent viewer= new Intent(getActivity(),CodeViewer.class);
        viewer.putExtra("img",fileLoc);
        getActivity().startActivity(viewer);
    }


    public void CheckStatus()
    {
        do {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                public void run()
                {
                    HomeActivity.Waiter(false);
                    Toast.makeText(getActivity(),"Generated",Toast.LENGTH_SHORT).show();
                    StartViewer(generator.GetGifLocation());
                }
            }, 1000);
        }
        while(isGenerated==false);
    }

    public  Boolean GenerateSGC() throws IOException, WriterException
    {
        if(loc.getText().length()<1)
        {
            Toast.makeText(getActivity(),"Select a file!",Toast.LENGTH_SHORT).show();
            Log.d("STARTSTARTSTART",pass.getText().toString());
            return false;
        }
        else
        {


            generator=new Generator(filePath,fileName);
            return generator.GenerateSGC(pass.getText().toString());
        }
    }

    public  void BrowseDialogue(int requestCode)
    {
        loc.setText("");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"),requestCode);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_FILE_BROWSE && resultCode == RESULT_OK)
        {
            selectedFile = intent.getData();
            try
            {
                filePath=PathUtil.getPath(getContext(),selectedFile);
                String[] arr=filePath.split("/");
                loc.setText(arr[arr.length-1]);
                fileName=loc.getText().toString();
            }
            catch (Exception e)
            {

            }
        }
    }





}