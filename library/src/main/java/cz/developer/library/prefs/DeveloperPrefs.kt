package cz.developer.library.prefs

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by cz on 16/3/15.
 */
object DeveloperPrefs {
    // 配置项名称
    private val DEFAULT_PREFERENCE = "developer_config"
    private val DEFAULT_INT_VALUE = -1
    private var appContext: Context? = null

    //重复检测,直接到post任务执行完毕
    val sharedPreferences: SharedPreferences
        get() {
            var appContext: Context? = null
            while (null == appContext) {
                appContext = getApplicationContext()
            }
            return appContext.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE)
        }

    private fun getApplicationContext(): Context? {
        if (appContext == null) {
            //部分5.0以下手机,在子线程内,利用反射取Context,会报空.所以这里如果是子线程,且小于5.0,直接post到子线程执行,外围通过while(判断是否取到数据
            try {
                val application = Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null) as Application
                appContext = application.applicationContext
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return appContext
    }

    val preferenceEditor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    fun remove(key: String) {
        preferenceEditor.remove(key).commit()
    }

    /**
     * 根据角标获取 默认值 -1;

     * @param key
     * *
     * @return
     */
    fun getInt(key: String): Int =sharedPreferences.getInt(key, DEFAULT_INT_VALUE)

    fun getInt(key: String, defaultValue: Int): Int {
        val value = getInt(key)
        return if (DEFAULT_INT_VALUE == value) defaultValue else value
    }


    /**
     * 根据角标获取 默认值 -1;

     * @param key
     * *
     * @return
     */
    fun getLong(key: String): Long =sharedPreferences.getLong(key, DEFAULT_INT_VALUE.toLong())

    fun getFloat(key: String): Float =sharedPreferences.getFloat(key, DEFAULT_INT_VALUE.toFloat())

    /**
     * 获得boolean值
     * @param key
     * *
     * @return
     */
    fun getBoolean(key: String): Boolean =sharedPreferences.getBoolean(key, false)

    fun setInt(key: String, value: Int) =preferenceEditor.putInt(key, value).commit()

    fun setFloat(key: String, value: Float)=preferenceEditor.putFloat(key, value).commit()

    fun setLong(key: String, value: Long) =preferenceEditor.putLong(key, value).commit()

    fun setString(key: String, value: String) =preferenceEditor.putString(key, value).commit()

    fun getString(key: String): String =sharedPreferences.getString(key, null)

    fun setBoolean(key: String, value: Boolean) =preferenceEditor.putBoolean(key, value).commit()
}
