package cz.developer.library.ui.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cz on 2017/9/13.
 */
data class ExceptionItem(var name:String?=null,
                    var threadName:String?=null,
                    var className:String?=null,
                    var methodName:String?=null,
                    var desc:StringBuilder=StringBuilder(),
                    var lastModified:Long=0) : Parcelable {


    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ExceptionItem> = object : Parcelable.Creator<ExceptionItem> {
            override fun createFromParcel(source: Parcel): ExceptionItem = ExceptionItem(source)
            override fun newArray(size: Int): Array<ExceptionItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(String::class.java.classLoader) as String?,
    source.readValue(String::class.java.classLoader) as String?,
    source.readValue(String::class.java.classLoader) as String?,
    source.readValue(String::class.java.classLoader) as String?,
    source.readSerializable() as StringBuilder,
    source.readLong())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(name)
        dest.writeValue(threadName)
        dest.writeValue(className)
        dest.writeValue(methodName)
        dest.writeSerializable(desc)
        dest.writeLong(lastModified)
    }


}