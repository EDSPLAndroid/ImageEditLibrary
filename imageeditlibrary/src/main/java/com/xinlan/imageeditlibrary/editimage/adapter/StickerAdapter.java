package com.xinlan.imageeditlibrary.editimage.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.StirckerFragment;
import com.xinlan.imageeditlibrary.editimage.model.ImageEditingData;

import java.util.ArrayList;
import java.util.List;


/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class StickerAdapter extends RecyclerView.Adapter<ViewHolder> {
    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();// 下载图片显示

    private StirckerFragment mStirckerFragment;
    private List<String> pathList = new ArrayList<String>();// 图片路径列表

    public StickerAdapter(StirckerFragment fragment) {
        super();
        this.mStirckerFragment = fragment;
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
        return pathList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        final String path = pathList.get(position);

       // Picasso.with(((ImageHolder) holder).image.getContext()).load(path).into(imageHoler.image);
        Picasso.with(((ImageHolder) holder).image.getContext()).load(path).into(imageHoler.image);
        imageHoler.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("data---->" + data);
                mStirckerFragment.selectedStickerItem(path);
            }
        });
    }

    public void addStickerImages(List<ImageEditingData.StickerDataBean.StickerCategoryDataBean.ChildrenBean> childrenBeen) {
        pathList.clear();
        try {

            for (ImageEditingData.StickerDataBean.StickerCategoryDataBean.ChildrenBean bean : childrenBeen) {
                pathList.add(bean.getStickerImageURL());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* try {
            String[] files = mStirckerFragment.getActivity().getAssets()
                    .list(folderPath);
            for (String name : files) {
                pathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        this.notifyDataSetChanged();
    }

}// end class
