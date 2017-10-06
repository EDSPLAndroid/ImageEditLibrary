package com.xinlan.imageeditlibrary.editimage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kimeeo.kAndroid.core.utils.NetworkUtilities;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xinlan.imageeditlibrary.AppBaseActivity;
import com.xinlan.imageeditlibrary.FileUtils;
import com.xinlan.imageeditlibrary.InternetErrorActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.AddImageFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.AddTextFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.BeautyFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.CropFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.FliterListFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.MainMenuFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.PaintFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.RotateFragment;
import com.xinlan.imageeditlibrary.editimage.fragment.StirckerFragment;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;
import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;
import com.xinlan.imageeditlibrary.editimage.utils.FileUtil;
import com.xinlan.imageeditlibrary.editimage.utils.PrefserUtil;
import com.xinlan.imageeditlibrary.editimage.view.CropImageView;
import com.xinlan.imageeditlibrary.editimage.view.CustomPaintView;
import com.xinlan.imageeditlibrary.editimage.view.CustomViewPager;
import com.xinlan.imageeditlibrary.editimage.view.RotateImageView;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;
import com.xinlan.imageeditlibrary.editimage.view.TextStickerView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * 一个幽灵
 * 共产主义的幽灵
 * 在欧洲徘徊
 * 旧欧洲的一切势力，
 * 教皇和沙皇、
 * 梅特涅和基佐、
 * 法国的激进党人和德国的警察，
 * 都为驱除这个幽灵而结成了神圣同盟
 * -----《共产党宣言》
 * <p>
 * 图片编辑 主页面
 *
 * @author panyi
 *         <p>
 *         包含 1.贴图 2.滤镜 3.剪裁 4.底图旋转 功能
 */
public class EditImageActivity extends AppBaseActivity {
    public static final String FILE_PATH = "file_path";
    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String SAVE_FILE_PATH = "save_file_path";

    public static final String IMAGE_IS_EDIT = "image_is_edit";

    public static final int MODE_NONE = 0;
    public static final int MODE_STICKERS = 1;// 贴图模式
    public static final int MODE_FILTER = 2;// 滤镜模式
    public static final int MODE_CROP = 3;// 剪裁模式
    public static final int MODE_ROTATE = 4;// 旋转模式
    public static final int MODE_TEXT = 5;// 文字模式
    public static final int MODE_PAINT = 6;//绘制模式
    public static final int MODE_BEAUTY = 7;//美颜模式
    public static final int MODE_ADD_IMAGE = 8;

    public String filePath;// 需要编辑图片路径
    public String saveFilePath;// 生成的新图片路径
    private int imageWidth, imageHeight;// 展示图片控件 宽 高
    private LoadImageTask mLoadImageTask;

    public int mode = MODE_NONE;// 当前操作模式

    protected int mOpTimes = 0;
    protected boolean isBeenSaved = false;

    private EditImageActivity mContext;
    public Bitmap mainBitmap;// 底层显示Bitmap
    public ImageViewTouch mainImage;
    private View backBtn;

    public ViewFlipper bannerFlipper;
    private View applyBtn;// 应用按钮
    private View saveBtn;// 保存按钮

    public StickerView mStickerView;// 贴图层View
    public CropImageView mCropPanel;// 剪切操作控件
    public RotateImageView mRotatePanel;// 旋转操作控件
    public TextStickerView mTextStickerView;//文本贴图显示View
    public CustomPaintView mPaintView;//涂鸦模式画板

    public CustomViewPager bottomGallery;// 底部gallery
    private BottomGalleryAdapter mBottomGalleryAdapter;// 底部gallery
    private MainMenuFragment mMainMenuFragment;// Menu
    public StirckerFragment mStirckerFragment;// 贴图Fragment
    public FliterListFragment mFliterListFragment;// 滤镜FliterListFragment
    public CropFragment mCropFragment;// 图片剪裁Fragment
    public RotateFragment mRotateFragment;// 图片旋转Fragment
    public AddTextFragment mAddTextFragment;//图片添加文字
    public PaintFragment mPaintFragment;//绘制模式Fragment
    public BeautyFragment mBeautyFragment;//美颜模式Fragment
    public AddImageFragment mAddImageFragment;

    private SaveImageTask mSaveImageTask;
    String editImgURL;
    String dataType = "";
    File receivedFile;

    /**
     * @param context
     * @param editImagePath
     * @param outputPath
     * @param requestCode
     */
    public static void start(Activity context, final String editImagePath, final String outputPath, final int requestCode) {
        if (TextUtils.isEmpty(editImagePath)) {
            Toast.makeText(context, R.string.no_choose, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(context, EditImageActivity.class);
        it.putExtra(EditImageActivity.FILE_PATH, editImagePath);
        it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputPath);
        context.startActivityForResult(it, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInitImageLoader();
        setContentView(R.layout.activity_image_edit);

        checkInternetConnection();
    }

    public void checkInternetConnection() {
        if (isConnected())
            configImageEditing();
        else
            showConnectionErorr();
    }

    private void configImageEditing() {

        if (getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).containsKey(ConstantUtil.URL)) {
            String imgLink = getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).getString(ConstantUtil.URL);

            if (Patterns.WEB_URL.matcher(imgLink).matches()) {
                editImgURL = (imgLink != null) ? imgLink : "";
                dataType = ConstantUtil.URL;
                //loadImage(editImgURL);
            } else {
                finish();
            }
        } else if (getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).containsKey(ConstantUtil.FILE)) {
            String data = getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).getString(ConstantUtil.FILE);

            receivedFile = new Gson().fromJson(data, File.class);

            if (receivedFile != null) {
                dataType = ConstantUtil.FILE;
                //loadImage(receivedfile);
            } else {
                finish();
            }
        } else {
            finish();
        }

        if (getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).containsKey(ConstantUtil.IMG_DATA_OBJ_URL)) {
            String dataLink = getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).getString(ConstantUtil.IMG_DATA_OBJ_URL);

            if (!dataLink.equals("")) {

                Gson gson = new Gson();
                try {

                    ImageEditingData imageEditingData = gson.fromJson(dataLink, ImageEditingData.class);
                    if (imageEditingData != null && !imageEditingData.equals("")) {

                        PrefserUtil.setCurrentImageEditingData(EditImageActivity.this, imageEditingData);
                        setData();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
            else
                finish();

            /*if (Patterns.WEB_URL.matcher(dataLink).matches()) {
                String dataURL = (dataLink != null) ? dataLink : "";

                if (dataURL != null && !dataURL.equals("")) {
                    DataTask dataTask = new DataTask();
                    dataTask.execute(dataURL);
                } else {
                    finish();
                }
            } else {
                finish();
            }*/
        }
        //initView();
        //getData();
    }

    private void getData() {
        filePath = getIntent().getStringExtra(FILE_PATH);
        saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径
        loadImageToScreen(filePath);
    }

    private void initView() {
        mContext = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 2;
        imageHeight = metrics.heightPixels / 2;

        bannerFlipper = (ViewFlipper) findViewById(R.id.banner_flipper);
        bannerFlipper.setInAnimation(this, R.anim.in_bottom_to_top);
        bannerFlipper.setOutAnimation(this, R.anim.out_bottom_to_top);
        applyBtn = findViewById(R.id.apply);
        applyBtn.setOnClickListener(new ApplyBtnClick());
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new SaveBtnClick());

        mainImage = (ImageViewTouch) findViewById(R.id.main_image);
        backBtn = findViewById(R.id.back_btn);// 退出按钮
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mStickerView = (StickerView) findViewById(R.id.sticker_panel);
        mCropPanel = (CropImageView) findViewById(R.id.crop_panel);
        mRotatePanel = (RotateImageView) findViewById(R.id.rotate_panel);
        mTextStickerView = (TextStickerView) findViewById(R.id.text_sticker_panel);
        mPaintView = (CustomPaintView) findViewById(R.id.custom_paint_view);

        // 底部gallery
        bottomGallery = (CustomViewPager) findViewById(R.id.bottom_gallery);
        //bottomGallery.setOffscreenPageLimit(7);
        mMainMenuFragment = MainMenuFragment.newInstance();
        mBottomGalleryAdapter = new BottomGalleryAdapter(
                this.getSupportFragmentManager());
        mStirckerFragment = StirckerFragment.newInstance();
        mFliterListFragment = FliterListFragment.newInstance();
        mCropFragment = CropFragment.newInstance();
        mRotateFragment = RotateFragment.newInstance();
        mAddTextFragment = AddTextFragment.newInstance();
        mPaintFragment = PaintFragment.newInstance();
        mBeautyFragment = BeautyFragment.newInstance();
        mAddImageFragment = AddImageFragment.newInstance();

        bottomGallery.setAdapter(mBottomGalleryAdapter);


        mainImage.setFlingListener(new ImageViewTouch.OnImageFlingListener() {
            @Override
            public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //System.out.println(e1.getAction() + " " + e2.getAction() + " " + velocityX + "  " + velocityY);
                if (velocityY > 1) {
                    closeInputMethod();
                }
            }
        });

        loadImageToScreen(filePath);
    }

    /**
     * 关闭输入法
     */
    private void closeInputMethod() {
        if (mAddTextFragment.isAdded()) {
            mAddTextFragment.hideInput();
        }
    }

    /**
     * @author panyi
     */
    private final class BottomGalleryAdapter extends FragmentPagerAdapter {
        public BottomGalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            // System.out.println("createFragment-->"+index);
            switch (index) {
                case MainMenuFragment.INDEX:// 主菜单
                    return mMainMenuFragment;
                case StirckerFragment.INDEX:// 贴图
                    return mStirckerFragment;
                case FliterListFragment.INDEX:// 滤镜
                    return mFliterListFragment;
                case CropFragment.INDEX://剪裁
                    return mCropFragment;
                case RotateFragment.INDEX://旋转
                    return mRotateFragment;
                case AddTextFragment.INDEX://添加文字
                    return mAddTextFragment;
                case PaintFragment.INDEX:
                    return mPaintFragment;//绘制
                case BeautyFragment.INDEX://美颜
                    return mBeautyFragment;
                case AddImageFragment.INDEX:
                    return mAddImageFragment;
            }//end switch
            return MainMenuFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 9;
        }
    }// end inner class

    /**
     * 异步载入编辑图片
     *
     * @param filepath
     */
    public void loadImageToScreen(String filepath) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(filepath);
    }

    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialogWithTitle(EditImageActivity.this, true);

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            return BitmapUtils.getSampledBitmap(params[0], imageWidth,
                    imageHeight);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            showProgressDialogWithTitle(EditImageActivity.this, false);
            if (mainBitmap != null) {
                mainBitmap.recycle();
                mainBitmap = null;
                System.gc();
            }
            mainBitmap = result;
            mainImage.setImageBitmap(result);
            mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            // mainImage.setDisplayType(DisplayType.FIT_TO_SCREEN);
        }
    }// end inner class

    @Override
    public void onBackPressed() {
        switch (mode) {
            case MODE_STICKERS:
                mStirckerFragment.backToMain();
                return;
            case MODE_FILTER:// 滤镜编辑状态
                mFliterListFragment.backToMain();// 保存滤镜贴图
                return;
            case MODE_CROP:// 剪切图片保存
                mCropFragment.backToMain();
                return;
            case MODE_ROTATE:// 旋转图片保存
                mRotateFragment.backToMain();
                return;
            case MODE_TEXT:
                mAddTextFragment.backToMain();
                return;
            case MODE_PAINT:
                mPaintFragment.backToMain();
                return;
            case MODE_BEAUTY://从美颜模式中返回
                mBeautyFragment.backToMain();
                return;
            case MODE_ADD_IMAGE://从美颜模式中返回
                mAddImageFragment.backToMain();
                return;
        }// end switch

        if (canAutoExit()) {
            onSaveTaskDone();
        } else {//图片还未被保存    弹出提示框确认
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.exit_without_save)
                    .setCancelable(false).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    if(saveFilePath!=null)
                    {
                        File file = new File(saveFilePath);
                        file.delete();
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(saveFilePath))));
                    }

                    mContext.finish();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    /**
     * 应用按钮点击
     *
     * @author panyi
     */
    private final class ApplyBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (mode) {
                case MODE_STICKERS:
                    mStirckerFragment.applyStickers();// 保存贴图
                    break;
                case MODE_FILTER:// 滤镜编辑状态
                    mFliterListFragment.applyFilterImage();// 保存滤镜贴图
                    break;
                case MODE_CROP:// 剪切图片保存
                    mCropFragment.applyCropImage();
                    break;
                case MODE_ROTATE:// 旋转图片保存
                    mRotateFragment.applyRotateImage();
                    break;
                case MODE_TEXT://文字贴图 图片保存
                    mAddTextFragment.applyTextImage();
                    break;
                case MODE_PAINT://保存涂鸦
                    mPaintFragment.savePaintImage();
                    break;
                case MODE_BEAUTY://保存美颜后的图片
                    mBeautyFragment.applyBeauty();
                    break;
                case MODE_ADD_IMAGE:
                    mAddImageFragment.applyStickers();
                    break;
                default:
                    break;
            }// end switch
        }
    }// end inner class

    /**
     * 保存按钮 点击退出
     *
     * @author panyi
     */
    private final class SaveBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mOpTimes == 0) {//并未修改图片
                onSaveTaskDone();
            } else {
                doSaveImage();
            }
        }
    }// end inner class

    protected void doSaveImage() {
        if (mOpTimes <= 0)
            return;

        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }

        mSaveImageTask = new SaveImageTask();
        mSaveImageTask.execute(mainBitmap);
    }

    /**
     * 切换底图Bitmap
     *
     * @param newBit
     */
    public void changeMainBitmap(Bitmap newBit) {
        if (newBit == null)
            return;

        if (mainBitmap != null) {
            if (!mainBitmap.isRecycled()) {// 回收
                mainBitmap.recycle();
            }
        }
        mainBitmap = newBit;
        mainImage.setImageBitmap(mainBitmap);
        mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        increaseOpTimes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }

        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }
    }

    public void increaseOpTimes() {
        mOpTimes++;
        isBeenSaved = false;
    }

    public void resetOpTimes() {
        isBeenSaved = true;
    }

    public boolean canAutoExit() {
        return isBeenSaved || mOpTimes == 0;
    }

    protected void onSaveTaskDone() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FILE_PATH, filePath);
        returnIntent.putExtra(EXTRA_OUTPUT, saveFilePath);
        returnIntent.putExtra(IMAGE_IS_EDIT, mOpTimes > 0);

        if(!isBeenSaved)
        {
            if(saveFilePath!=null)
            {
                File file = new File(saveFilePath);
                file.delete();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(saveFilePath))));
            }
        }
        FileUtil.ablumUpdate(this, saveFilePath);

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /**
     * 保存图像
     * 完成后退出
     */
    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            if (TextUtils.isEmpty(saveFilePath))
                return false;

            return BitmapUtils.saveBitmap(params[0], saveFilePath);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = EditImageActivity.getLoadingDialog(mContext, R.string.saving_image, false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result) {
                resetOpTimes();
                onSaveTaskDone();
            } else {
                Toast.makeText(mContext, R.string.save_error, Toast.LENGTH_SHORT).show();
            }
        }
    }//end inner class

    ProgressDialog progressDialog;

    public void showProgressDialogWithTitle(Context context, boolean needDialogShow) {
        if (needDialogShow) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            //progressDialog.setTitle("Please Wait..");
            progressDialog.setMessage(getString(R.string.strLoadingMsg));
            progressDialog.show();
        } else {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    public class DataTask extends AsyncTask<String, Integer, Void> {
        String text = "";

        public DataTask() {

        }

        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialogWithTitle(EditImageActivity.this, true);
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

                        PrefserUtil.setCurrentImageEditingData(EditImageActivity.this, imageEditingData);
                        setData();

                    } else {
                        Toast.makeText(mContext, "We Currently Working on it.Please Try again after sometime", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                showProgressDialogWithTitle(EditImageActivity.this, false);
            } else {
                showProgressDialogWithTitle(EditImageActivity.this, false);
            }
        }
    }

    protected void showConnectionErorr() {
        Intent intent = new Intent(this, InternetErrorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INTERNET_ERROR);
    }

    public static final int REQUEST_CODE_INTERNET_ERROR = 21;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_INTERNET_ERROR && resultCode == RESULT_OK)
            checkInternetConnection();
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    protected void setData() {

        if (dataType.equals(ConstantUtil.URL))
            loadImage(editImgURL);
        else if (dataType.equals(ConstantUtil.FILE))
            loadImage(receivedFile);

    }

    public boolean isConnected() {
        try {
            return NetworkUtilities.isConnected(this);
        } catch (Exception var2) {
            return false;
        }
    }

    public void loadImage(File file) {
        startImageEditing(file);
    }

    private void startImageEditing(File file) {

        if (file != null) {
            File outputFile = FileUtils.genEditFile(getResources().getString(R.string.app_name));

            //EditImageActivity.start(this, file.getPath(), outputFile.getAbsolutePath(), ACTION_REQUEST_EDITIMAGE);

            if (!TextUtils.isEmpty(file.getPath())) {
                filePath = file.getPath();
                saveFilePath = outputFile.getAbsolutePath();
                initView();
            } else {
                Toast.makeText(this, R.string.no_choose, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else
            finish();
    }

    public void loadImage(String url) {
        Picasso.with(this).load(url).into(target);
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap tempBitmap, Picasso.LoadedFrom from) {
            bitmap = tempBitmap;
            new TedPermission(EditImageActivity.this)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage(R.string.strPermissionMsg)
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            downloadResult(getResources().getString(R.string.app_name) + "" + new Random().nextInt());
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i("Permission Denied", "" + deniedPermissions.toString());
        }
    };
    public Bitmap bitmap;

    public void downloadResult(String fileName) {

        /*File folder = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File file = new File(folder.getPath() + "/" + fileName + "_" + System.currentTimeMillis() + ".png"); */

        FileOutputStream fos;
        try {

            File file = File.createTempFile("prefix", "suffix", getCacheDir());

            fos = new FileOutputStream(file);

            if (fileName.endsWith(".png"))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            else if (fileName.endsWith(".jpg"))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            else if (fileName.endsWith(".web"))
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
            else
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            file.setReadable(true, false);
            startImageEditing(file);

        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);

        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
}// end class