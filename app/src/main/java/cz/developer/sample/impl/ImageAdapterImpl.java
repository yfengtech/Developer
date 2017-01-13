package cz.developer.sample.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.ImageItem;
import cz.developer.library.ui.image.ImageAdapter;
import cz.developer.sample.R;

/**
 * Created by cz on 11/9/16.
 //    1.3:1    实际尺寸   115*87   小图
 //    自由图：以宽度为基准，下面自由适配
 */
public class ImageAdapterImpl implements ImageAdapter<ImageAdapterImpl.Item> {
    float ASPECT_RATIO_SMALL_GROUP=1.321f;//1:4.6 --345*75

    private static final int ITEM_SMALL_GROUP=0;
    private static final int ITEM_COUNT=1;

    @Override
    public View getView(Context context, ViewGroup parent,ImageItem<Item> item) {
        View view=null;
        if(ITEM_SMALL_GROUP==item.itemType){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view=layoutInflater.inflate(R.layout.image_group_item,parent,false);
            LinearLayout layout= (LinearLayout) view.findViewById(R.id.ll_container);
            TextView textView= (TextView) view.findViewById(R.id.tv_item_name);
            view.setPadding(item.horizontalPadding,item.verticalPadding,item.horizontalPadding,item.verticalPadding);
            Item objItem= item.obj;
            textView.setText(objItem.value);
            for(String url:item.imageItems){
                View childView=layoutInflater.inflate(R.layout.debug_image_item,(ViewGroup) view,false);
                childView.setPadding((int)item.itemPadding,0,(int)item.itemPadding,0);
                SimpleDraweeView imageView= (SimpleDraweeView) childView.findViewById(R.id.wv_image);
                imageView.setAspectRatio(item.aspectRatio);
                imageView.setImageURI(Uri.parse(url));
                layout.addView(childView,new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
            }
        }
        return view;
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public List<ImageItem<Item>> getImageItems() {
        List<ImageItem<Item>> imageItems=new ArrayList<>();
        for(int i=0;i<5;i++){
            ImageItem<Item> imageItem=new ImageItem(Item.class);
            imageItem.info="SMALL_GROUP";
            imageItem.itemType=ITEM_SMALL_GROUP;
            imageItem.aspectRatio=ASPECT_RATIO_SMALL_GROUP;
            imageItem.horizontalPadding =24;
            imageItem.verticalPadding=24;
            imageItem.itemPadding=24;
            imageItem.obj=new Item("key","value");
            ArrayList<Item> items=new ArrayList<>();
            for(int k=0;k<5;k++){
                items.add(new Item("i:"+i+" key:"+k,"value:"+k));
            }
            imageItem.objItems=items;
            imageItem.imageItems.add("http://file.weixinkd.com/article_201611_10_102_5823db35248e8.jpg/180x135");
            imageItem.imageItems.add("http://file.weixinkd.com/article_201611_10_103_5823db35d149c.jpg/180x135");
            imageItem.imageItems.add("http://file.weixinkd.com/article_201611_10_10a_5823db3660450.jpg/180x135");
            imageItems.add(imageItem);
        }
        return imageItems;
    }

    public static class Item implements Parcelable {
        public final String key;
        public final String value;

        public Item(String key, String value) {
            this.key = key;
            this.value = value;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.key);
            dest.writeString(this.value);
        }

        protected Item(Parcel in) {
            this.key = in.readString();
            this.value = in.readString();
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}
