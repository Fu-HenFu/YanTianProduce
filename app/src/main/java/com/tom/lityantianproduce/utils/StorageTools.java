package com.tom.lityantianproduce.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StorageTools {

    public static void StoreInfoFile(Context mConext, String mFile, Map<String, String> DataList) {

        if (mFile == null || mConext == null)
            return;

        int allSize = DataList.size();
        if (allSize == 0)
            return;

        try {
            File phoneInfoFile = mConext.getExternalFilesDir("Litphone");
            Log.i("File path", mFile);
            if (!phoneInfoFile.exists()) {
                Log.i("File Exist", "Exist");
                phoneInfoFile.mkdir();
            }

            File file = new File(phoneInfoFile.getAbsolutePath() + File.separator +
                    "Litphone.dat");
            Log.i("File Path", file.getAbsolutePath());
            FileOutputStream mFileOutputStream =  new FileOutputStream(file);//mConext.openFileOutput(mFile, MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE );
            int index = 0;
            // StringBuilder mStringBuilder
            Iterator<Map.Entry<String, String>> iterator = DataList.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                StringBuffer phoneInfoBuffer = new StringBuffer();
                phoneInfoBuffer.append(entry.getKey());
                phoneInfoBuffer.append("=").append(entry.getValue());

//            for (index = 0; index < allSize; index++) {

//                StringBuilder mStringBuilder = new StringBuilder(String.valueOf(((String) DataList.get(index)).toString()));
                try {
                    mFileOutputStream.write(phoneInfoBuffer.toString()
                            .getBytes());
                    mFileOutputStream.write("\n".getBytes());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            /*add by liuzhenting 20170801 begin*/
            try {
                mFileOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /*add by liuzhenting 20170801 end*/

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(mConext.getFilesDir(), "Litphone.dat");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// 判断当前目录是否存在，存在返回true,否则返回false
//		else {

        try {
            InputStream instream = new FileInputStream(file);
            String content = "";
            if (instream != null) {
                InputStreamReader inputReader = new InputStreamReader(instream, "UTF-8");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line = "";
                //分行读取
                while ((line = buffReader.readLine()) != null) {
                    content += line + "\n";
                }
                instream.close();//关闭输入流
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void StoreResultFile(Context mConext, String result) {
        if (mConext == null || result == null)
            return;

        ArrayList<String> myArrayList = new ArrayList<String>();
        myArrayList.add(result);
        Log.i("StoreResultFile", "result= "+result);
//        StoreInfoFile(mConext, "LitFactoryResult.dat", myArrayList);
    }
}
