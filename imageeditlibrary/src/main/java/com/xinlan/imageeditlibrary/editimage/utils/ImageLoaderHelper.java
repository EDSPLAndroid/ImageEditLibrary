package com.xinlan.imageeditlibrary.editimage.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

//import com.squareup.picasso.Target;

/**
 * Created by Administrator on 9/21/2016.
 */

public class ImageLoaderHelper {

    public static void loadTransformThumbnail(final ImageView imageView, final View progressBar, final String url, String thumbnail, final Drawable errorView, final String transformationType, final String imageType) {
        if(thumbnail!=null)
        {
              RequestCreator requestCreator = Picasso.with(imageView.getContext()).load(thumbnail);
                requestCreator.networkPolicy(NetworkPolicy.OFFLINE);
                requestCreator.into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        loadTransform(imageView, progressBar, url, null, transformationType, -1, -1, false,imageType);
                    }

                    @Override
                    public void onError() {
                        loadTransform(imageView, progressBar, url, null, transformationType, -1, -1, false,imageType);
                    }
                });
        }
        else
            loadTransform(imageView,progressBar,url,errorView,transformationType,-1,-1,false,imageType);
    }

    public static void loadTransform(ImageView view, String url, Drawable errorView, String transformationType,String imageType,int width, int height, boolean resize) {
        loadTransform(view,null,url,errorView,transformationType,width,height,resize,imageType);
    }

    public static void loadTransform(ImageView view, String url, Drawable errorView, String transformationType,String imageType) {
        loadTransform(view,null,url,errorView,transformationType,-1,-1,false,imageType);
    }

    static int corner=-1;
    public static void loadTransform(ImageView view, final View progressBar, String url, Drawable errorView, String transformationTypeList, int width, int height, boolean resize, String imageType) {

        if (url != null && !url.equals("")) {

            if(corner==-1) {
                corner = (int) view.getContext().getResources().getDimension(R.dimen.margin2x);
                corner = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, corner, view.getResources().getDisplayMetrics()));
            }

            int fillColor=0;
            int maskDrable=0;

            String[] transformationList=transformationTypeList.split(",");
            String[] transformationListFinal=new String[transformationList.length];
            for (int i = 0; i < transformationList.length; i++) {
                String transformation= transformationList[i];
                if(transformation.equals(view.getContext().getString(R.string.imageTransformation_ColorPrimaryFilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.colorPrimary;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_ColorPrimaryDarkFilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.colorPrimaryDark;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_ColorAccentFilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.colorAccent;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_1_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor1;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_2_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor2;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_3_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor3;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_4_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor4;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_5_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor5;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_StarMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_star;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_FlagMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_flag;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_BagMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_bag;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_BellMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_bell;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_CloudMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_cloud;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_FloristMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_florist;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_HeartMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_heart;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_PetsMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_pets;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_PuzzleMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_puzzle;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_TicketMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_ticket;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_AppleMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_apple;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_AppleMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_big_heart;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Boy_1_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_boy_1;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Boy_2_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_boy_2;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Boy_3_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_boy_3;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_1_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_1;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_2_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_2;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_3_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_3;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_4_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_4;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Hexagon_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_hexagon;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Macro_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_macro;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Lotus_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_lotus;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_CatMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_cat_in_black;
                }
                else
                    transformationListFinal[i]=transformation;

            }


            List<Transformation> transformation=getTransformation(view.getContext(),transformationListFinal,corner,fillColor,maskDrable);
            if(transformation!=null)
            {
                RequestCreator requestCreator=null;

                if(imageType.equals(ConstantUtil.URL))
                    requestCreator = Picasso.with(view.getContext()).load(url).transform(transformation);
                else if(imageType.equals(ConstantUtil.FILE))
                    requestCreator = Picasso.with(view.getContext()).load(new File(url)).transform(transformation);

                if(errorView!=null)
                    requestCreator.error(errorView);
                if(resize)
                    requestCreator.resize(width,height).centerCrop();
                requestCreator.into(view);
            }
            else
            {

                RequestCreator requestCreator=null;

                if(imageType.equals(ConstantUtil.URL))
                    requestCreator = Picasso.with(view.getContext()).load(url).transform(transformation);
                else if(imageType.equals(ConstantUtil.FILE))
                    requestCreator = Picasso.with(view.getContext()).load(new File(url)).transform(transformation);

                if(errorView!=null)
                    requestCreator.error(errorView);
                if(resize)
                    requestCreator.resize(width,height).centerCrop();
                requestCreator.into(view);
            }
        }
        else
        {
            RequestCreator requestCreator = Picasso.with(view.getContext()).load("fakePath").transform(new CropCircleTransformation()).transform(new CropCircleTransformation());
            if(errorView!=null)
                requestCreator.error(errorView);
            requestCreator.into(view);
        }

    }

    static StickerView mStickerView;

    public static void loadTransformStickerView(StickerView view, final View progressBar, String url, Drawable errorView, String transformationTypeList, int width, int height, boolean resize, String imageType) {

        mStickerView = view;

        if (url != null && !url.equals("")) {

            if(corner==-1) {
                corner = (int) view.getContext().getResources().getDimension(R.dimen.margin2x);
                corner = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, corner, view.getResources().getDisplayMetrics()));
            }

            int fillColor=0;
            int maskDrable=0;

            String[] transformationList=transformationTypeList.split(",");
            String[] transformationListFinal=new String[transformationList.length];
            for (int i = 0; i < transformationList.length; i++) {
                String transformation= transformationList[i];
                if(transformation.equals(view.getContext().getString(R.string.imageTransformation_ColorPrimaryFilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.colorPrimary;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_ColorPrimaryDarkFilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.colorPrimaryDark;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_ColorAccentFilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.colorAccent;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_1_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor1;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_2_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor2;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_3_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor3;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_4_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor4;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Color_5_FilterTransformation))) {
                    transformationListFinal[i] = view.getContext().getString(R.string.imageTransformation_ColorFilterTransformation);
                    fillColor=R.color.imageColor5;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_StarMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_star;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_FlagMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_flag;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_BagMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_bag;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_BellMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_bell;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_CloudMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_cloud;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_FloristMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_florist;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_HeartMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_heart;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_PetsMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_pets;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_PuzzleMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_puzzle;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_TicketMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_ticket;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_AppleMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_apple;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_AppleMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_big_heart;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Boy_1_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_boy_1;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Boy_2_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_boy_2;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Boy_3_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_boy_3;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_1_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_1;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_2_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_2;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_3_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_3;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Girl_4_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_girl_4;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Hexagon_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_hexagon;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Macro_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_macro;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_Lotus_MaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_lotus;
                }
                else if(transformation.equals(view.getContext().getString(R.string.imageTransformation_CatMaskTransformation)))
                {
                    transformationListFinal[i]=view.getContext().getString(R.string.imageTransformation_MaskTransformation);
                    maskDrable=R.drawable.mask_cat_in_black;
                }
                else
                    transformationListFinal[i]=transformation;

            }


            List<Transformation> transformation=getTransformation(view.getContext(),transformationListFinal,corner,fillColor,maskDrable);
            if(transformation!=null)
            {
                RequestCreator requestCreator=null;

                if(imageType.equals(ConstantUtil.URL))
                    requestCreator = Picasso.with(view.getContext()).load(url).transform(transformation);
                else if(imageType.equals(ConstantUtil.FILE))
                    requestCreator = Picasso.with(view.getContext()).load(new File(url)).transform(transformation);

                if(errorView!=null)
                    requestCreator.error(errorView);
                if(resize)
                    requestCreator.resize(width,height).centerCrop();
                requestCreator.into(targetStickerView);
            }
            else
            {

                RequestCreator requestCreator=null;

                if(imageType.equals(ConstantUtil.URL))
                    requestCreator = Picasso.with(view.getContext()).load(url).transform(transformation);
                else if(imageType.equals(ConstantUtil.FILE))
                    requestCreator = Picasso.with(view.getContext()).load(new File(url)).transform(transformation);

                if(errorView!=null)
                    requestCreator.error(errorView);
                if(resize)
                    requestCreator.resize(width,height).centerCrop();
                requestCreator.into(targetStickerView);
            }
        }
        else
        {
            RequestCreator requestCreator = Picasso.with(view.getContext()).load("fakePath").transform(new CropCircleTransformation()).transform(new CropCircleTransformation());
            if(errorView!=null)
                requestCreator.error(errorView);
            requestCreator.into(targetStickerView);
        }
    }

    static Target targetStickerView = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mStickerView.addBitImage(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public static void setCopyright(View view, String name) {
        if(name != null)
        {
            try {

            URI uri = new URI(name);
            String domain = uri.getHost();

            TextView tv = (TextView) view;
            tv.setText(domain.startsWith("www.") ? domain.substring(4) : domain);
            }
            catch (Exception e)
            {

            }
        }
    }

    private static List<Transformation> getTransformation(Context context,String[] transformationTypes,int corners,@ColorRes int colorRes,@DrawableRes int maskRes) {

        if(transformationTypes!=null) {
            List<Transformation> list = new ArrayList<>();
            for (int i = 0; i < transformationTypes.length; i++) {
                String transformationType = transformationTypes[i];
                if(transformationType!=null) {
                    if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_CropCircleTransformation)))
                        list.add(new CropCircleTransformation());
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_CropSquareTransformation)))
                        list.add(new CropSquareTransformation());
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_RoundedCornersTransformation))) {
                        list.add(new RoundedCornersTransform(corners, 0));
                    } else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_ColorFilterTransformation)) && colorRes != 0)
                        list.add(new ColorFilterTransformation(context.getResources().getColor(colorRes), PorterDuff.Mode.LIGHTEN));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_GrayscaleTransformation)))
                        list.add(new GrayscaleTransformation());
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_BlurTransformation)))
                        list.add(new BlurTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_MaskTransformation)) && maskRes != -1)
                        list.add(new MaskTransformation(context, maskRes));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_SepiaFilterTransformation)))
                        list.add(new SepiaFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_ContrastFilterTransformation)))
                        list.add(new ContrastFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_InvertFilterTransformation)))
                        list.add(new InvertFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_PixelationFilterTransformation)))
                        list.add(new PixelationFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_SketchFilterTransformation)))
                        list.add(new SketchFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_SwirlFilterTransformation)))
                        list.add(new SwirlFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_BrightnessFilterTransformation)))
                        list.add(new BrightnessFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_ToonFilterTransformation)))
                        list.add(new ToonFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_KuwaharaFilterTransformation)))
                        list.add(new KuwaharaFilterTransformation(context));
                    else if (transformationType.equals(context.getResources().getString(R.string.imageTransformation_VignetteFilterTransformation)))
                        list.add(new VignetteFilterTransformation(context));
                }
            }
            return list;
        }
        return null;
    }
}