package com.xinlan.imageeditlibrary.editimage.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.StirckerFragment;
import com.xinlan.imageeditlibrary.editimage.utils.PrefserUtil;

import java.util.ArrayList;


/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class StickerTypeAdapter extends RecyclerView.Adapter<ViewHolder> {
   /* public static final int[] typeIcon = {R.drawable.stickers_type_animal,
            R.drawable.stickers_type_motion, R.drawable.stickers_type_cos,
            R.drawable.stickers_type_mark, R.drawable.stickers_type_decoration};*/
    //public static final String[] stickerPath = {"stickers/type1", "stickers/type2", "stickers/type3", "stickers/type4", "stickers/type5", "stickers/type6"};
    //public static final String[] stickerPathName = {"Diwali", "Diwali 2", "Holi", "表情4", "表情5", "表情6"};

    /*abstract public ArrayList<String> getStickerPath();

    abstract public ArrayList<String> getStickerPathName();*/

    private StirckerFragment mStirckerFragment;

    public StickerTypeAdapter(StirckerFragment fragment) {
        super();
        this.mStirckerFragment = fragment;
    }

    public class ImageHolder extends ViewHolder {
        public ImageView icon;
        //public TextView text;
        public ViewGroup myView;

        public ImageHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.icon)!=null)
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            //this.text = (TextView) itemView.findViewById(R.id.text);
            this.myView = (ViewGroup)itemView.findViewById(R.id.rootView);
        }
    }// end inner class

    @Override
    public int getItemCount() {
        return PrefserUtil.getCurrentImageEditingData(mStirckerFragment.getActivity()).getStickerData().getStickerCategoryData().size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_type_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    /**
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        /*String name = null;
        try {
            name = PrefserUtil.getCurrentImageEditingData(mStirckerFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getStickerCategoryName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageHoler.text.setText(name);*/
        // TODO

        try {

            Picasso.with(imageHoler.icon.getContext()).load(PrefserUtil.getCurrentImageEditingData(mStirckerFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getStickerCategoryURL()).into(imageHoler.icon);

            imageHoler.myView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStirckerFragment.swipToStickerDetails(PrefserUtil.getCurrentImageEditingData(mStirckerFragment.getActivity()).getStickerData().getStickerCategoryData().get(position).getChildren());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}// end class