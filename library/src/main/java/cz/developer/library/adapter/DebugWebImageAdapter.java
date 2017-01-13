package cz.developer.library.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;
import cz.developer.library.ui.ImageDisplayInterface;

/**
 * Created by cz on 1/13/17.
 */

public class DebugWebImageAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<String> items;
    private final ImageDisplayInterface imageDisplay;

    public DebugWebImageAdapter(Context context, List<String> items) {
        this.imageDisplay=DeveloperManager.getInstances().getImageDisplay();
        this.inflater=LayoutInflater.from(context);
        this.items=new ArrayList<>();
        if(null!=items){
            this.items.addAll(items);
        }
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public String getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView=inflater.inflate(R.layout.web_image_item,parent,false);
            ViewStub viewStub= (ViewStub) convertView.findViewById(R.id.view_stub);
            viewStub.inflate();
        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.debug_image_view);
        final TextView imageSize= (TextView) convertView.findViewById(R.id.tv_image_size);
        final TextView imageInfo= (TextView) convertView.findViewById(R.id.tv_image_info);
        String url = getItem(position);
        if(null!=imageDisplay){
            final View originalView=convertView;
            imageDisplay.onDisplay(imageView,url,(imageUrl,bitmap)->{
                File imageFile = imageDisplay.getImageFile(imageUrl);
                if(null!=imageFile){
                    loadBitmapInfo(originalView, imageSize, imageInfo,imageFile);
                }
            });
            convertView.setOnClickListener(v -> {
                File imageFile = imageDisplay.getImageFile(url);
                if(null!=imageFile){
                    loadBitmapInfo(v, imageSize, imageInfo,imageFile);
                }
            });
        }
        return convertView;
    }

    private void loadBitmapInfo(View v, TextView imageSize, TextView imageInfo,File imageFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        //检测文件大小
        imageSize.setText(inflater.getContext().getString(R.string.image_size_value,bitmap.getWidth(),bitmap.getHeight()));
        String fileSizeInfo = Formatter.formatFileSize(v.getContext(), imageFile.length());
        String imageSizeInfo = Formatter.formatFileSize(v.getContext(), bitmap.getByteCount());
        //检测 bitmap 大小
        imageInfo.setText(inflater.getContext().getString(R.string.image_info_value,fileSizeInfo,imageSizeInfo));
    }
}
