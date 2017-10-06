package com.xinlan.imageeditlibrary.editimage.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by INTEL I5 on 30-09-2017.
 */

public class FetchFileFromDeviceTask extends AsyncTask
{
    public static int FILE_TYPE_IMAGES=1;
    int fileType;

    Context context;
    OnGetAllFileList onGetAllFileList;

    public FetchFileFromDeviceTask(Context context, OnGetAllFileList onGetAllFileList,int fileType) {
        this.context=context;
        this.onGetAllFileList=onGetAllFileList;
        this.fileType=fileType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        if(fileType==FILE_TYPE_IMAGES)
            return ImagePathProvider.getAllImagesPath(context);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(o instanceof ArrayList)
            onGetAllFileList.onGetFileListSuccess(o);
        else
            onGetAllFileList.onFail();
    }

    public interface OnGetAllFileList
    {
        void onGetFileListSuccess(Object object);
        void onFail();
    }
}