package cz.developer.library.appinfo

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.util.*


/**
 * Created by cz on 2017/9/13.
 */
object BuildProp {
    private val properties: Properties = Properties()

    init {
        val inputStream = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
        properties.load(inputStream)
        inputStream.close()
    }

    val buildProperties:MutableSet<MutableMap.MutableEntry<Any,Any>>?
        get() = properties.entries

    fun getProperty(name: String, defaultValue: String): String {
        return properties.getProperty(name, defaultValue)
    }

}