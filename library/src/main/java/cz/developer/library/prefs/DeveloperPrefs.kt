package cz.developer.library.prefs

import android.content.Context
import android.content.SharedPreferences
import com.financial.quantgroup.v2.preference.DeveloperPreference
import cz.developer.library.Developer

/**
 * Created by cz on 2017/6/26.
 * 实现功能:
 * 1:透过代理,操作sharedPrefrece 值对象如操作对象一般
 * 2:增加配置与对象处理,如首页tab配置,需要json转为对象,简化主类工作
 */
object DeveloperPrefs {

    val DEFAULT_STRING =""
    val sharedPrefs: SharedPreferences= Developer.applicationContext.getSharedPreferences("developer", Context.MODE_PRIVATE)

    var url : String by DeveloperPreference(sharedPrefs,"url",DEFAULT_STRING)

    /**
     * 调试控件信息
     */
    var debugView: Boolean by DeveloperPreference(sharedPrefs,"debug_list",false)

    fun setString(key: Any, value: String) =sharedPrefs.edit().putString(key.toString(), value).commit()

    fun getString(key: Any): String? =sharedPrefs.getString(key.toString(), null)
}