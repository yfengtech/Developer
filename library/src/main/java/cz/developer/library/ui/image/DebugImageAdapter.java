package cz.developer.library.ui.image;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.R;
import cz.developer.library.model.ImageItem;
import cz.developer.library.widget.ThumbView;

/**
 * Created by cz on 11/9/16.
 */

public class DebugImageAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<ImageItem> items;
    private final int thumbColor;

    public DebugImageAdapter(Context context,ImageAdapter imageAdapter) {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.items=new ArrayList<>();
        this.thumbColor= ContextCompat.getColor(context,R.color.alphaGray);
        if(null!=items){
            List<ImageItem> imageItems = imageAdapter.getImageItems();
            if(null!=imageItems){
                this.items.addAll(imageItems);
            }
        }
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public ImageItem getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null==view){
            view = inflater.inflate(R.layout.debug_image_item, viewGroup, false);
            ViewHolder holder=new ViewHolder();
            holder.textView= (TextView) view.findViewById(R.id.tv_info);
            holder.itemCount= (TextView) view.findViewById(R.id.tv_item_count);
            holder.aspectRatio= (TextView) view.findViewById(R.id.tv_aspect_ratio);
            holder.paddingView= (TextView) view.findViewById(R.id.tv_padding);
            holder.itemType= (TextView) view.findViewById(R.id.tv_type);
            holder.thumbView= (ThumbView) view.findViewById(R.id.tv_thumb);
            view.setTag(holder);
        }
        ViewHolder holder= (ViewHolder) view.getTag();
        ImageItem item = getItem(i);
        holder.textView.setText(getString(R.string.info_value,item.info));
        holder.itemCount.setText(getString(R.string.item_count_value,item.imageItems.size()));
        holder.aspectRatio.setText(getString(R.string.aspect_ratio_value,item.aspectRatio));
        holder.paddingView.setText(getString(R.string.padding_value,item.horizontalPadding,item.verticalPadding,item.itemPadding));
        holder.itemType.setText(getString(R.string.image_type_value,ImageItem.BANNER_ITEM==item.type?ImageItem.BANNER_VALUE:ImageItem.LIST_VALUE));

        holder.thumbView.setItemPadding((int) item.itemPadding);
        holder.thumbView.setPadding(item.horizontalPadding,item.verticalPadding,item.horizontalPadding,item.verticalPadding);
        holder.thumbView.setAspectRatio(item.aspectRatio);
        holder.thumbView.setItemCount(ImageItem.BANNER_ITEM!=item.type?item.imageItems.size():1);
        holder.thumbView.setColor(thumbColor);
        return view;
    }

    private String getString(@StringRes int res,Object...params){
        return context.getResources().getString(res,params);
    }

    public void setItem(int index, ImageItem item) {
        this.items.set(index,item);
        notifyDataSetChanged();
    }

    public static class ViewHolder{
        public TextView textView;
        public TextView itemCount;
        public TextView aspectRatio;
        public TextView paddingView;
        public TextView itemType;
        public ThumbView thumbView;
    }
}
