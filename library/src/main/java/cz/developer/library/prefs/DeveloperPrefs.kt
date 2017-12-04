package cz.developer.library.prefs

import android.content.Context
import android.content.SharedPreferences
import cz.developer.library.Developer

/**
 * Created by cz on 2017/6/26.
 * 实现功能:
 * 1:透过代理,操作sharedPrefrece 值对象如操作对象一般
 * 2:增加配置与对象处理,如首页tab配置,需要json转为对象,简化主类工作
 */
object DeveloperPrefs {

    val DEFAULT_STRING =""
    val HIERARCHY_KEY="hierarchy"//视图调试
    val sharedPrefs: SharedPreferences= Developer.applicationContext.getSharedPreferences("developer", Context.MODE_PRIVATE)

    var url : String by DeveloperPreference(sharedPrefs,"url",DEFAULT_STRING)

    /**
     * 调试控件信息
     */
    var debugView: Boolean by DeveloperPreference(sharedPrefs,"debug_list",false)

    var initConfig: Boolean by DeveloperPreference(sharedPrefs,"init_config",false)

    var prefsItems: HashSet<String> by DeveloperPreference(sharedPrefs,"net_items",HashSet<String>())

    fun setString(key: Any, value: String) =sharedPrefs.edit().putString(key.toString(), value).commit()

    fun getString(key: Any): String? =sharedPrefs.getString(key.toString(), null)

    fun setBoolean(key: Any, value: Boolean) =sharedPrefs.edit().putBoolean(key.toString(), value).commit()

    fun getBoolean(key: Any): Boolean =sharedPrefs.getBoolean(key.toString(), false)
}