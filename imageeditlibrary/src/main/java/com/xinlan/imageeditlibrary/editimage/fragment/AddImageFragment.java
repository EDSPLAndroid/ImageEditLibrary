package com.xinlan.imageeditlibrary.editimage.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xinlan.imageeditlibrary.FileUtils;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.adapter.ImageListAdapter;
import com.xinlan.imageeditlibrary.editimage.adapter.ImageSourceTypeAdapter;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;
import com.xinlan.imageeditlibrary.editimage.utils.ImageLoaderHelper;
import com.xinlan.imageeditlibrary.editimage.view.StickerItem;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * Created by INTEL I5 on 30-09-2017.
 */

public class AddImageFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_ADDIMAGE;

    public static final String TAG = AddImageFragment.class.getName();

    private View mainView;
    private ViewFlipper flipper;
    private View backToMenu;
    private RecyclerView typeList;
    private RecyclerView imageList;
    private View backToType;
    private StickerView mStickerView;
    private ImageListAdapter imageListAdapter;

    private SaveStickersTask mSaveTask;

    public static AddImageFragment newInstance() {
        AddImageFragment fragment = new AddImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.fragment_edit_image_sticker_type,
                null);

        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mStickerView = activity.mStickerView;
        flipper = (ViewFlipper) mainView.findViewById(R.id.flipper);
        flipper.setInAnimation(activity, R.anim.in_bottom_to_top);
        flipper.setOutAnimation(activity, R.anim.out_bottom_to_top);

        //
        backToMenu = mainView.findViewById(R.id.back_to_main);
        typeList = (RecyclerView) mainView
                .findViewById(R.id.stickers_type_list);
        typeList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        typeList.setLayoutManager(mLayoutManager);
        typeList.setAdapter(new ImageSourceTypeAdapter(this));
        backToType = mainView.findViewById(R.id.back_to_type);// back按钮

        imageList = (RecyclerView) mainView.findViewById(R.id.stickers_list);
        // imageList.setHasFixedSize(true);
        LinearLayoutManager stickerListLayoutManager = new LinearLayoutManager(
                activity);
        stickerListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageList.setLayoutManager(stickerListLayoutManager);
        imageListAdapter= new ImageListAdapter(this);
        imageList.setAdapter(imageListAdapter);

        backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单
        backToType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 返回上一级列表
                flipper.showPrevious();
            }
        });
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_ADD_IMAGE;
        activity.mAddImageFragment.getmStickerView().setVisibility(
                View.VISIBLE);
        activity.bannerFlipper.showNext();
    }

    /**
     * 跳转至贴图详情列表
     *
     * @param imagePath
     * @param imageType
     */
    public void swipToStickerDetails(String imagePath,String imageType) {

        imageListAdapter.addStickerImages(imagePath,imageType);
        flipper.showNext();
    }

    /**
     * 从Assert文件夹中读取位图数据
     *
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 选择贴图加入到页面中
     *
     * @param path
     */
    public void selectedStickerItem(String path,String shapeType,String imageType) {
        //Picasso.with(getActivity()).load(new File(path)).into(target);
        //ImageLoaderHelper.loadTransform(imageHoler.image,path,getResources().getDrawable(R.drawable.sticker_normal),shapeType, ConstantUtil.FILE);
        ImageLoaderHelper.loadTransformStickerView(mStickerView,null,path,getResources().getDrawable(R.drawable.sticker_normal),shapeType,-1,-1,false,imageType);
    }

    Target target = new Target() {
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

    public StickerView getmStickerView() {
        return mStickerView;
    }

    private Uri fileUri=null;

    public void takePhotoClick() {

        fileUri = Uri.fromFile(FileUtils.genEditFile(getResources().getString(R.string.app_name)));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, PICK_CAMERA_IMAGE_REQUEST);

    }

    /**
     * 返回主菜单页面
     *
     * @author panyi
     */
    private final class BackToMenuClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end inner class

    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(0);
        mStickerView.setVisibility(View.GONE);
        activity.bannerFlipper.showPrevious();
    }

    /**
     * 保存贴图任务
     *
     * @author panyi
     */
    private final class SaveStickersTask extends StickerTask {
        public SaveStickersTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            LinkedHashMap<Integer, StickerItem> addItems = mStickerView.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);// 乘以底部图片变化矩阵
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }// end for
        }

        @Override
        public void onPostResult(Bitmap result) {
            mStickerView.clear();
            activity.changeMainBitmap(result);
        }
    }// end inner class

    /**
     * 保存贴图层 合成一张图片
     */
    public void applyStickers() {
        // System.out.println("保存 合成图片");
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        mSaveTask = new SaveStickersTask((EditImageActivity) getActivity());
        mSaveTask.execute(activity.mainBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            swipToStickerDetails(fileUri.getPath(),ConstantUtil.FILE);
        }
    }

    private int PICK_CAMERA_IMAGE_REQUEST = 2;


}// end class