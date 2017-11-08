package com.xinlan.imageeditandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xinlan.imageeditlibrary.Splash;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;
import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashDemo extends Splash {

/*    @Override
    protected Bundle getMainClassParam() {
        Bundle bundle = new Bundle();

        //For Image fetch from URL
        bundle.putString(ConstantUtil.URL,"https://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg");

        //For Image fetch from FILE

        *//*File file = new File("/storage/emulated/0/Pictures/xinlanedit/ImageEdit_1502868302331.jpg");
        //File file = new File("/storage/emulated/0/Pictures/xinlanedit/ImageEdit_1502956859656.jpg");
        //File file = new File("/storage/emulated/0/Pictures/xinlanedit/ImageEdit_1502957644243.jpg");
        bundle.putString(ConstantUtil.FILE,new Gson().toJson(file));*//*
        return bundle;
    }

    @Override
    protected Class<?> getMainClass() {
        return ImageEditHomeActivity.class;
    }*/

    //String imgUrl="https://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg";
    String imgUrl="http://www.planwallpaper.com/static/images/city_of_love-wallpaper-1366x768.jpg";
    //String imgDataUrl="http://174.66.76.164/manageapp/File/ImageEditingJSONData.txt";
    String imgDataUrl="http://174.66.76.164/manageapp/File/greetingsapp.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Bundle bundle = new Bundle();

        //For Image fetch from URL
        bundle.putString(ConstantUtil.URL,imgUrl);
        bundle.putString(ConstantUtil.IMG_DATA_OBJ_URL,imgDataUrl);

        this.startActivityForResult(new Intent(this, EditImageActivity.class).putExtra(ConstantUtil.EXTERNAL_BUNDLE,bundle),REQUEST_CODE_IMG_EDIT);*/

        DataTask dataTask = new DataTask();
        dataTask.execute(imgDataUrl);

    }
    public static final int REQUEST_CODE_IMG_EDIT = 999;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMG_EDIT) {
            if (resultCode == RESULT_OK) {
                String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
            } else
                finish();
        }
        else
            finish();
    }

    public class DataTask extends AsyncTask<String, Integer, Void> {
        String text = "";

        public DataTask() {

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    text += line;
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            if (!text.equals("")) {

                Gson gson = new Gson();
                try {

                    ImageEditingData imageEditingData = gson.fromJson(text, ImageEditingData.class);

                    if (imageEditingData != null && !imageEditingData.equals("")) {

                        //PrefserUtil.setCurrentImageEditingData(SplashDemo.this, imageEditingData);

                        Bundle bundle = new Bundle();
                        //For Image fetch from URL
                        bundle.putString(ConstantUtil.URL,imgUrl);
                        bundle.putString(ConstantUtil.IMG_DATA_OBJ_URL,new Gson().toJson(imageEditingData));
                        startActivityForResult(new Intent(SplashDemo.this, EditImageActivity.class).putExtra(ConstantUtil.EXTERNAL_BUNDLE,bundle),REQUEST_CODE_IMG_EDIT);


                    } else {
                        Toast.makeText(SplashDemo.this, "We Currently Working on it.Please Try again after sometime", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}