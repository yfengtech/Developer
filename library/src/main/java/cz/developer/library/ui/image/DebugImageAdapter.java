package cz.developer.library.ui.image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.ImageItem;

/**
 * Created by cz on 11/10/16.
 */

public class DebugImageAdapter extends BaseAdapter {
    private final Context context;
    private final List<ImageItem> items;
    private final ImageAdapter imageAdapter;

    public DebugImageAdapter(Context context,ImageAdapter imageAdapter,ArrayList<ImageItem> items) {
        this.context=context;
        this.items=new ArrayList<>();
        this.imageAdapter=imageAdapter;
        if(null!=items){
            if(null!=items){
                this.items.addAll(items);
            }
        }
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public ImageItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).itemType;
    }

    @Override
    public int getViewTypeCount() {
        return imageAdapter.getItemCount();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageItem item = getItem(i);
        if(null==view&&null!=imageAdapter){
            view=imageAdapter.getView(context,viewGroup,item);
        }
        return view;
    }
}
