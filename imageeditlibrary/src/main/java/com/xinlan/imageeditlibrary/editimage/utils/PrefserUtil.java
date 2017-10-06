package com.xinlan.imageeditlibrary.editimage.utils;

import android.content.Context;

import com.github.pwittchen.prefser.library.Prefser;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;

import java.util.List;

/**
 * Created by User on 08-08-2017.
 */

public class PrefserUtil {

    public static ImageEditingData getCurrentImageEditingData(Context context) {
        return new Prefser(context).get(PREF_IMG_DATA_OBJ,ImageEditingData.class,new ImageEditingData());
    }

    public static void setCurrentImageEditingData(Context context,ImageEditingData currentImageEditingData) {
        new Prefser(context).put(PREF_IMG_DATA_OBJ,currentImageEditingData);
    }

    static public final String PREF_IMG_DATA_OBJ="IMG_DATA_OBJ";

    public List<ImageEditingData.StickerDataBean.StickerCategoryDataBean> getStickerData(Context context) {

        if(getCurrentImageEditingData(context)!=null && getCurrentImageEditingData(context).getStickerData().getStickerCategoryData()!=null)
        {
            return getCurrentImageEditingData(context).getStickerData().getStickerCategoryData();
        }
        return null;
    }

}