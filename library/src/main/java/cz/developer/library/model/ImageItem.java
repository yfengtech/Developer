package cz.developer.library.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cz on 11/9/16.
 */

public class ImageItem implements Parcelable {
    public static final int BANNER_ITEM=0;
    public static final int LIST_ITEM=1;
    public static final String BANNER_VALUE="BANNER";
    public static final String LIST_VALUE="LIST";
    public final ArrayList<String> imageItems;
    public float aspectRatio;
    public int horizontalPadding;
    public int verticalPadding;
    public float itemPadding;
    public String info;
    public int itemType;
    public int imageType;
    public Parcelable obj;

    public ImageItem() {
        imageType=LIST_ITEM;
        imageItems=new ArrayList<>();
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
        dest.writeParcelable(this.obj, 0);
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
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
