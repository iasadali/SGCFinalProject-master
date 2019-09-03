package com.asad.sgcfinal;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    ArrayList<String> arr,code;
    Boolean process,found;
    int count;
    String temp;
    String fileName,data;
    String[] splt;
    String password;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result){

            temp=result.getText();
            if(result.getText() == null || result.getText().equals(lastText) || arr.contains(result.getText()))
            {
                // Prevent duplicate scans
                return;
            }
            if(temp.split("START",0).length>1 || arr.contains(result.getText()))
            {
                splt=temp.split(":",0);
                count=Integer.valueOf(splt[1]);
                fileName=splt[2];
                Log.d("SIZEOFSTART: ",String.valueOf(splt.length));
                if(splt.length>3)
                {
                    Log.d("ANSWER","("+splt[3]+") - ("+password+")");
                    String a=splt[3];
                    String b=password;


                    if((a.equals(b))==false)
                    {
                        pause(null);
                        Toast.makeText(getApplicationContext(),"ENCRYPT",Toast.LENGTH_LONG);
                        finish();
                    }
                }
                //Toast.makeText(getApplicationContext(),splt[3],Toast.LENGTH_LONG);
                process=true;
                barcodeView.setStatusText("File: "+fileName+"\n"+" TOTAL: "+String.valueOf(count));
                found=true;
            }
            else
            {

            }

            arr.add(temp);
            if(found)
            {
                barcodeView.setStatusText("ARR: "+String.valueOf(arr.size()-1)+" COUNT: "+String.valueOf(count));
               // String  per= String.valueOf((Double.valueOf(code.size()-1)/Double.valueOf(count))*100);
               // barcodeView.setStatusText(per+"%");
            }
            else
            {
                barcodeView.setStatusText("ARRAY: "+String.valueOf(arr.size()));
            }



            if(process==true)
            {
                if(arr.size()==count+2)
                {
                    pause(null);

                    String[] fin=new String[arr.size()];
                    String[] tmp;
                    for(int i=0; i<arr.size(); i++)
                    {
                        try
                        {

                            if(arr.get(i).contains("START")==false)
                            {
                                tmp=arr.get(i).split("SCANGETCODE",0);
                                fin[Integer.valueOf(tmp[1])]=tmp[0];
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.d("EXCP",arr.get(i));
                        }
                    }

                    String baseSixFour="";
                    for(int y=0; y<fin.length-1;y++)
                    {
                        baseSixFour+=fin[y];
                    }



                    data=baseSixFour;
                    Log.d("CODDDDDDEEE >>>>>>>>>>>>>>>>.",data+">>>>>>>");
                    try
                    {
                        SaveFile();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    } catch (WriterException e)
                    {
                        e.printStackTrace();
                    }



                }
            }





            lastText = result.getText();
           // barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();
            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };




    public  void SaveFile() throws IOException, WriterException
    {
        String fp="/storage/emulated/0/Download/"+fileName;
        FileOutputStream os = new FileOutputStream(fp);
        Log.d("FINAL BASE 64",data);
        os.write(Base64.decode(data, Base64.NO_WRAP));
        os.close();
        Toast.makeText(this, "File Saved", Toast.LENGTH_LONG).show();
        this.finish();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        password=getIntent().getExtras().getString("password");
        Log.d("PASSWORD",password);
        setContentView(R.layout.continuous_scan);
        arr=new ArrayList<String>();
        process=false;
        found=false;
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    public void onBackPressed()
    {
        Intent t = new Intent(ContinuousCaptureActivity.this, fragmentScan.class);
        startActivity(t);
        finish();
    }
}