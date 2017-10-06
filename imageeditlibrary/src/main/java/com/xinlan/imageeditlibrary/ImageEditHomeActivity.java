package com.xinlan.imageeditlibrary;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ImageEditHomeActivity extends AppBaseActivity {

    Button button;
    ImageView imageView;
    public Bitmap bitmap;
    String imgURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit_home);

        if(getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).containsKey(ConstantUtil.URL))
        {
            String imgLink = getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).getString(ConstantUtil.URL);

            if(Patterns.WEB_URL.matcher(imgLink).matches())
            {
                imgURL = (imgLink != null)?imgLink:"";
                loadImage(imgURL);
            }
            else
            {
                finish();
            }
        }
        else if(getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).containsKey(ConstantUtil.FILE))
        {
            String data = getIntent().getExtras().getBundle(ConstantUtil.EXTERNAL_BUNDLE).getString(ConstantUtil.FILE);

            File file = new Gson().fromJson(data,File.class);

            if(file!=null)
            {
                loadImage(file);
            }
            else
            {
                finish();
            }
        }
        else
        {
            finish();
        }

        button = (Button) findViewById(R.id.btnSend);
        imageView = (ImageView) findViewById(R.id.image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadImage(imgURL);
            }
        });
    }

    public void loadImage(String url) {
        Picasso.with(this).load(url).into(target);
    }

    public void loadImage(File file) {
        startImageEditing(file);
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap tempBitmap, Picasso.LoadedFrom from) {
            imageView.setImageBitmap(tempBitmap);
            bitmap = tempBitmap;
            new TedPermission(ImageEditHomeActivity.this)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("Denied")
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

    public void downloadResult(String fileName) {

        /*File folder = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File file = new File(folder.getPath() + "/" + fileName + "_" + System.currentTimeMillis() + ".png"); */

        FileOutputStream fos;
        try {

            File file=File.createTempFile("prefix","suffix",getCacheDir());

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

    public static final int ACTION_REQUEST_EDITIMAGE = 9;

    private void startImageEditing(File file) {
        File outputFile = FileUtils.genEditFile(getResources().getString(R.string.app_name));

        //EditImageActivity.start(this, file.getPath(), outputFile.getAbsolutePath(), ACTION_REQUEST_EDITIMAGE);

        if (!TextUtils.isEmpty(file.getPath())) {

            Intent it = new Intent(this, EditImageActivity.class);
            it.putExtra(EditImageActivity.FILE_PATH, file.getPath());
            it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputFile.getAbsolutePath());
            startActivityForResult(it, ACTION_REQUEST_EDITIMAGE);
        }
        else
        {
            Toast.makeText(this, R.string.no_choose, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
            }
        }
    }

    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit) {
            Toast.makeText(this, getString(com.xinlan.imageeditlibrary.R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
        } else {
            newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);
        }
        Log.i("newFilePath",newFilePath);
        Uri uri = Uri.parse(newFilePath);
        imageView.setImageURI(uri);
    }
}
