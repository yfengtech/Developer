package cz.developer.library.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by cz on 11/9/16.
 */

public class ImageItem<T extends Parcelable> implements Parcelable {
    public static final int BANNER_ITEM=0;
    public static final int LIST_ITEM=1;
    public static final String BANNER_VALUE="BANNER";
    public static final String LIST_VALUE="LIST";
    public ArrayList<String> imageItems;
    private Class<T> itemClazz;
    public float aspectRatio;
    public int horizontalPadding;
    public int verticalPadding;
    public float itemPadding;
    public String info;
    public int itemType;
    public int imageType;
    public T obj;
    public ArrayList<T> objItems;

    public ImageItem() {
        imageType=LIST_ITEM;
        imageItems=new ArrayList<>();
    }

    public ImageItem(Class<T> clazz){
        imageType=LIST_ITEM;
        imageItems=new ArrayList<>();
        this.itemClazz=clazz;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.imageItems);
        dest.writeFloat(this.aspectRatio);
        dest.writeInt(this.horizontalPadding);
        dest.writeInt(this.verticalPadding);
        dest.writeFloat(this.itemPadding);
        dest.writeString(this.info);
        dest.writeInt(this.itemType);
        dest.writeInt(this.imageType);
        dest.writeParcelable(this.obj, flags);
        dest.writeTypedList(this.objItems);
    }

    protected ImageItem(Parcel in) {
        this.imageItems = in.createStringArrayList();
        this.aspectRatio = in.readFloat();
        this.horizontalPadding = in.readInt();
        this.verticalPadding = in.readInt();
        this.itemPadding = in.readFloat();
        this.info = in.readString();
        this.itemType = in.readInt();
        this.imageType = in.readInt();
        this.obj = in.readParcelable(Parcelable.class.getClassLoader());
        if(null!=itemClazz){
            try {
                Field field = itemClazz.getDeclaredField("CREATOR");
                Parcelable.Creator<T> creator= (Creator<T>) field.get(null);
                this.objItems = in.createTypedArrayList(creator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
