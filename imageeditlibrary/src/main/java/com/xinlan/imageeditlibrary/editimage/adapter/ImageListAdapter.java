package com.xinlan.imageeditlibrary.editimage.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.AddImageFragment;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;
import com.xinlan.imageeditlibrary.editimage.utils.ConstantUtil;
import com.xinlan.imageeditlibrary.editimage.utils.ImageLoaderHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class ImageListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private AddImageFragment addImageFragment;
    private List<String> shapeList = new ArrayList<String>();// 图片路径列表

    public ImageListAdapter(AddImageFragment fragment) {
        super();
        this.addImageFragment = fragment;


        shapeList.add(fragment.getString(R.string.imageTransformation_CropSquareTransformation));
        shapeList.add(fragment.getString(R.string.imageTransformation_StarMaskTransformation));
        shapeList.add(fragment.getString(R.string.imageTransformation_HeartMaskTransformation));
        shapeList.add(fragment.getString(R.string.imageTransformation_CropCircleTransformation));
        shapeList.add(fragment.getString(R.string.imageTransformation_Hexagon_MaskTransformation));
    }

    public class ImageHolder extends ViewHolder {
        public ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.img);
        }
    }// end inner class

    @Override
    public int getItemCount() {
        return shapeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_shape_image_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ImageHolder imageHoler = (ImageHolder) holder;
        final String shapeType = shapeList.get(position);

        //Picasso.with(((ImageHolder) holder).image.getContext()).load(path).into(imageHoler.image);

        ImageLoaderHelper.loadTransform(imageHoler.image,imagePath,null,shapeType,imageType,100,100,true);

        /*if(imageType.equals(ConstantUtil.URL)) {
            Picasso.with(((ImageHolder) holder).image.getContext()).load(new File(imagePath)).resize(50, 50).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.i("onBitmapLoaded", "OK");
                    imageHoler.image.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.i("onBitmapFailed", "OK");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.i("onPrepareLoad", "OK");
                }
            });
        }*/
        imageHoler.image.setTag(shapeType);
        imageHoler.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("data---->" + data);
                addImageFragment.selectedStickerItem(imagePath,(String) view.getTag(),imageType);
            }
        });
    }

    String imagePath,imageType;

    public void addStickerImages(String imagePath,String imageType) {
        //shapeList.clear();
        try {
            Log.i("addStickerImages","OK");
            this.imagePath=imagePath;
            this.imageType=imageType;
            /*for (ImageEditingData.StickerDataBean.StickerCategoryDataBean.ChildrenBean bean : childrenBeen) {
                shapeList.add(bean.getStickerImageURL());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }
}// end class