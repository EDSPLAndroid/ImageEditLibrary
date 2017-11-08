package com.xinlan.imageeditlibrary.editimage.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.xinlan.imageeditlibrary.FileUtils;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.AddImageFragment;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;
import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;
import com.xinlan.imageeditlibrary.editimage.utils.FetchFileFromDeviceTask;
import com.xinlan.imageeditlibrary.editimage.utils.ImageLoaderHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class ImageSourceTypeAdapter extends RecyclerView.Adapter<ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback, PreferenceManager.OnActivityResultListener {

    private AddImageFragment addImageFragment;
    List<ImageEditingData.StickerDataBean.StickerCategoryDataBean> stickerCategoryDataBeanArrayList;

    public static String ADD_IMAGE_FROM_CAMERA = "Camera";
    public static String ADD_IMAGE_FROM_GALLERY_CATEGORY = "Gallery_Category";
    public static String ADD_IMAGE_FROM_GALLERY = "Gallery";

    public ImageSourceTypeAdapter(AddImageFragment fragment) {
        super();
        this.addImageFragment = fragment;
        initImageSourceTypeList();
    }

    public void initImageSourceTypeList() {
        stickerCategoryDataBeanArrayList = new ArrayList<>();

        ImageEditingData.StickerDataBean.StickerCategoryDataBean stickerCategoryDataBean = new ImageEditingData.StickerDataBean.StickerCategoryDataBean();
        stickerCategoryDataBean.setStickerCategoryName(ADD_IMAGE_FROM_CAMERA);
        stickerCategoryDataBean.setStickerCategoryURL("https://cdn1.iconfinder.com/data/icons/camera-13/100/Artboard_62-512.png");

        stickerCategoryDataBeanArrayList.add(stickerCategoryDataBean);

        ImageEditingData.StickerDataBean.StickerCategoryDataBean stickerCategoryDataBean2 = new ImageEditingData.StickerDataBean.StickerCategoryDataBean();
        stickerCategoryDataBean2.setStickerCategoryName(ADD_IMAGE_FROM_GALLERY_CATEGORY);
        stickerCategoryDataBean2.setStickerCategoryURL("https://cdn1.iconfinder.com/data/icons/camera-13/100/Artboard_62-512.png");

        stickerCategoryDataBeanArrayList.add(stickerCategoryDataBean2);

        /*stickerCategoryDataBean = new ImageEditingData.StickerDataBean.StickerCategoryDataBean();
        stickerCategoryDataBean.setStickerCategoryName(ADD_IMAGE_FROM_GALLERY);
        stickerCategoryDataBeanArrayList.add(stickerCategoryDataBean);*/

        selectFromAblum();

    }

    public class ImageHolder extends ViewHolder {
        public ImageView icon;
        public ViewGroup myView;

        public ImageHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.icon) != null)
                this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.myView = (ViewGroup) itemView.findViewById(R.id.rootView);
        }
    }// end inner class

    @Override
    public int getItemCount() {
        return stickerCategoryDataBeanArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_image_add_item_row, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    /**
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ImageHolder imageHoler = (ImageHolder) holder;
        String name = null;
        try {
            name = stickerCategoryDataBeanArrayList.get(position).getStickerCategoryName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO

        try {

            //Picasso.with(imageHoler.icon.getContext()).load(PrefserUtil.getCurrentImageEditingData(addImageFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getStickerCategoryURL()).into(imageHoler.icon);

            if(name.equals(ADD_IMAGE_FROM_GALLERY))
            {
                Picasso.with(imageHoler.icon.getContext()).load(new File(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL())).resize(100,100).into(imageHoler.icon);
                //ImageLoaderHelper.loadTransform(imageHoler.icon,stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL(),imageHoler.icon.getContext().getResources().getDrawable(R.drawable.sticker_normal),imageHoler.icon.getContext().getResources().getString(R.string.imageTransformation_StarMaskTransformation),ConstantUtil.FILE);
            }
            else if(name.equals(ADD_IMAGE_FROM_CAMERA))
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(96,96);
                imageHoler.icon.setLayoutParams(params);
                imageHoler.icon.setImageDrawable(imageHoler.icon.getContext().getResources().getDrawable(R.drawable.camera_add));
                //Picasso.with(imageHoler.icon.getContext()).load(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL()).resize(150,150).into(imageHoler.icon);
                //ImageLoaderHelper.loadTransform(imageHoler.icon,stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL(),imageHoler.icon.getContext().getResources().getDrawable(R.drawable.sticker_normal),imageHoler.icon.getContext().getResources().getString(R.string.imageTransformation_StarMaskTransformation), ConstantUtil.URL);
            }
            else if(name.equals(ADD_IMAGE_FROM_GALLERY_CATEGORY))
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(96,96);
                imageHoler.icon.setLayoutParams(params);
                imageHoler.icon.setImageDrawable(imageHoler.icon.getContext().getResources().getDrawable(R.drawable.add_camera_photo));
                //Picasso.with(imageHoler.icon.getContext()).load(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL()).resize(150,150).into(imageHoler.icon);
                //ImageLoaderHelper.loadTransform(imageHoler.icon,stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL(),imageHoler.icon.getContext().getResources().getDrawable(R.drawable.sticker_normal),imageHoler.icon.getContext().getResources().getString(R.string.imageTransformation_StarMaskTransformation), ConstantUtil.URL);
            }

            imageHoler.myView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //addImageFragment.swipToStickerDetails(PrefserUtil.getCurrentImageEditingData(addImageFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getChildren());

                    if (stickerCategoryDataBeanArrayList.get(position).getStickerCategoryName().equals(ADD_IMAGE_FROM_GALLERY)) {
                        //addImageFragment.selectedStickerItem(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL());

                        //selectFromAblum();

                        addImageFragment.swipToStickerDetails(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL(),ConstantUtil.FILE);
                        //addImageFragment.swipToStickerDetails(PrefserUtil.getCurrentImageEditingData(addImageFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getChildren());
                    }
                    else if (stickerCategoryDataBeanArrayList.get(position).getStickerCategoryName().equals(ADD_IMAGE_FROM_CAMERA)) {
                        //addImageFragment.swipToStickerDetails(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL(),ConstantUtil.URL);
                        takePhotoClick();
                        //addImageFragment.takePhotoClick();
                        //addImageFragment.swipToStickerDetails(PrefserUtil.getCurrentImageEditingData(addImageFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getChildren());
                    }
                    else if (stickerCategoryDataBeanArrayList.get(position).getStickerCategoryName().equals(ADD_IMAGE_FROM_GALLERY_CATEGORY)) {
                        //addImageFragment.swipToStickerDetails(stickerCategoryDataBeanArrayList.get(position).getStickerCategoryURL(),ConstantUtil.URL);
                        selectFromGalleryAblum();
                        //addImageFragment.takePhotoClick();
                        //addImageFragment.swipToStickerDetails(PrefserUtil.getCurrentImageEditingData(addImageFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getChildren());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectFromAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            readImageFromGallery();
        }//end if
    }

    private void selectFromGalleryAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionsForAlbum();
        } else {
            readImageFromGalleryAlbum();
        }//end if
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(addImageFragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addImageFragment.getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_STORAGE);
            return;
        }
        readImageFromGallery();
    }

    private void checkPermissionsForAlbum() {
        if (ActivityCompat.checkSelfPermission(addImageFragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addImageFragment.getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_STORAGE_ALBUM);
            return;
        }
        readImageFromGalleryAlbum();
    }

    private void readImageFromGallery() {
        FetchFileFromDeviceTask fetchFileFromDeviceTaskImages = new FetchFileFromDeviceTask(addImageFragment.getActivity(), onGetAllFileList, FetchFileFromDeviceTask.FILE_TYPE_IMAGES);
        fetchFileFromDeviceTaskImages.execute();
    }

    private void readImageFromGalleryAlbum() {
        addImageFragment.imageFromGalleryAlbum();
    }

    FetchFileFromDeviceTask.OnGetAllFileList onGetAllFileList = new FetchFileFromDeviceTask.OnGetAllFileList() {
        @Override
        public void onGetFileListSuccess(Object object) {
            ArrayList<String> imagesList = (ArrayList<String>) object;
            Log.i("adapter list size", imagesList.size() + "");

            List<ImageEditingData.StickerDataBean.StickerCategoryDataBean.ChildrenBean> childrenBeen = new ArrayList<>();

            for (int i = 0; i < imagesList.size(); i++) {
                ImageEditingData.StickerDataBean.StickerCategoryDataBean.ChildrenBean childrenBean = new ImageEditingData.StickerDataBean.StickerCategoryDataBean.ChildrenBean();
                childrenBean.setStickerImageURL(imagesList.get(i));
                childrenBeen.add(childrenBean);

                ImageEditingData.StickerDataBean.StickerCategoryDataBean stickerCategoryDataBean = new ImageEditingData.StickerDataBean.StickerCategoryDataBean();
                stickerCategoryDataBean.setStickerCategoryName(ADD_IMAGE_FROM_GALLERY);
                stickerCategoryDataBean.setStickerCategoryURL(imagesList.get(i));
                stickerCategoryDataBeanArrayList.add(stickerCategoryDataBean);
                notifyDataSetChanged();
            }
            //addImageFragment.swipToStickerDetails(childrenBeen);
        }

        @Override
        public void onFail() {
            Toast.makeText(addImageFragment.getActivity(), "No Images Available", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSON_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readImageFromGallery();
            return;
        }//end if

        if (requestCode == REQUEST_PERMISSON_STORAGE_ALBUM
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readImageFromGalleryAlbum();
            return;
        }//end if

        if (requestCode == REQUEST_PERMISSON_CAMERA
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doTakePhoto();
            return;
        }//end if
    }

    private void doTakePhoto() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(addImageFragment.getActivity().getPackageManager()) != null) {
            File photoFile = FileUtils.genEditFile(addImageFragment.getActivity().getResources().getString(R.string.app_name));
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                addImageFragment.getActivity().startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
            }
        }*/

        addImageFragment.takePhotoClick();

    }

    private void handleTakePhoto(Intent data) {
        if (photoURI != null) {//拍摄成功
            String path = photoURI.getPath();
            Log.i("Path", "PATH = " + path);
            //startLoadTask();
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == addImageFragment.getActivity().RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {

                case TAKE_PHOTO_CODE:
                    handleTakePhoto(data);
                    break;
            }// end switch
        }
        return true;
    }

    protected void takePhotoClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestTakePhotoPermissions();
        } else {
            doTakePhoto();
        }//end if
    }

    /**
     * 请求拍照权限
     */
    private void requestTakePhotoPermissions() {

        new TedPermission(addImageFragment.getActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage(R.string.strPermissionMsg)
                .setPermissions(Manifest.permission.CAMERA)
                .check();

        /*if (ActivityCompat.checkSelfPermission(addImageFragment.getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addImageFragment.getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSON_CAMERA);
            return;
        }
        doTakePhoto();*/
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            doTakePhoto();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i("Permission Denied", "" + deniedPermissions.toString());
        }
    };

    public static final int REQUEST_PERMISSON_STORAGE = 1;
    public static final int REQUEST_PERMISSON_STORAGE_ALBUM = 3;
    public static final int REQUEST_PERMISSON_CAMERA = 2;
    public static final int TAKE_PHOTO_CODE = 8;
    private Uri photoURI = null;

}// end class