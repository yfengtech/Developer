package cz.developer.library.ui.image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cz.developer.library.model.ImageItem;

/**
 * Created by cz on 11/9/16.
 * 图片数据获取
 * //获取图片url,外边距,内边距,比例数据,以及
 *
 */
public interface ImageAdapter {

    View getView(Context context, ViewGroup parent,ImageItem item);

    int getItemCount();

    List<ImageItem> getImageItems();

}
