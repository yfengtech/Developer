package cz.developer.library.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cz on 11/8/16.
 */

public class NetItem implements Parcelable {
    public String action;
    public String info;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.info);
        dest.writeString(this.url);
    }

    public NetItem() {
    }

    protected NetItem(Parcel in) {
        this.action = in.readString();
        this.info = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<NetItem> CREATOR = new Parcelable.Creator<NetItem>() {
        public NetItem createFromParcel(Parcel source) {
            return new NetItem(source);
        }

        public NetItem[] newArray(int size) {
            return new NetItem[size];
        }
    };

}
