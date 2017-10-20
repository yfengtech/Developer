package cz.developer.library.network.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cz on 11/8/16.
 */

data class NetItem(var info: String?, var url: String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<NetItem> = object : Parcelable.Creator<NetItem> {
            override fun createFromParcel(source: Parcel): NetItem = NetItem(source)
            override fun newArray(size: Int): Array<NetItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
        source.readValue(String::class.java.classLoader) as String?,
        source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(info)
        dest.writeString(url)
    }


}
