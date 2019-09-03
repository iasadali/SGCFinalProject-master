package com.asad.sgcfinal;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Generator
{
    String fileLoc,chunk,base64String,fileName,gifloc,password;
    ArrayList<Bitmap> sgcodes;
    BarcodeEncoder sgcodeEncoder;
    int chunksCounter,loop;
    final String TAG="Generator";

    public Generator(String _fileLoc,String _fileName)
    {
        MakeDirectory();
        fileLoc=_fileLoc;
        fileName=_fileName;
    }

    public Boolean GenerateSGC(String val) throws WriterException, IOException
    {
        Log.d(TAG,"IN CLASS METHOD");
        base64String=getBase64FromPath();
        sgcodes=new ArrayList<Bitmap>();
        sgcodeEncoder = new BarcodeEncoder();
        chunksCounter=0;
        loop=0;
        chunk="";
        while(loop<base64String.length()-2112)
        {
            chunk=base64String.substring(loop,loop+2112);
            chunk=chunk+"SCANGETCODE"+String.valueOf(chunksCounter);
            sgcodes.add(ChunkToQR(chunk));
            chunksCounter++;
            loop+=2112;
            Log.d(TAG,chunk);
        }
        chunk=base64String.substring(loop,loop+(base64String.length()-loop));
        chunk=chunk+"SCANGETCODE"+String.valueOf(chunksCounter);
        sgcodes.add(ChunkToQR(chunk));
        Log.d(TAG,chunk);
        chunk="START:"+String.valueOf(chunksCounter)+":"+fileName+":"+val;
        sgcodes.add(ChunkToQR(chunk));
        Log.d(TAG,chunk);
        GenerateGIF(sgcodes);
        return true;
    }

    public String GetGifLocation()
    {
        return gifloc;
    }

    public void MakeDirectory()
    {
        File folder = new File("/storage/emulated/0/ScanGetCodes");
        if (!folder.exists())
        {
            folder.mkdirs();
        }
    }



    public byte[] GenerateGIF(ArrayList<Bitmap> arr) throws IOException
    {
        ArrayList<Bitmap> bitmaps = arr;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setFrameRate(2);
        encoder.start(bos);
        for (Bitmap bitmap : bitmaps) {
            encoder.addFrame(bitmap);
        }
        encoder.finish();
        gifloc="/storage/emulated/0/ScanGetCodes/"+fileName+".gif";
        FileOutputStream os = new FileOutputStream(gifloc);
        os.write(bos.toByteArray());
        os.close();


        return bos.toByteArray();
    }


    public Bitmap ChunkToQR(String content) throws WriterException
    {
        return sgcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
    }

    public  String getBase64FromPath() throws IOException
    {
        String base64 = "";
        try {
            File file = new File(fileLoc);
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length,
                    Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("<><>",base64);
        return base64;
    }

}
