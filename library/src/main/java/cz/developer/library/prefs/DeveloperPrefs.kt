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
    val HIERARCHY_KEY="hierarchy"//视图调试
    val URL="url"//视图调试
    val DEBUG_LIST="debug_list"//视图调试
    val INIT_CONFIG="init_config"//视图调试
    val NET_ITEMS="net_items"//视图调试

    private fun getSharedPrefs(context:Context)=context.getSharedPreferences("developer", Context.MODE_PRIVATE)

    fun setSet(context:Context,key:Any,item:Set<String>){
        val sharedPrefs=getSharedPrefs(context)
        sharedPrefs.edit().putStringSet(key.toString(),item).commit()
    }

    fun getSet(context:Context,key:Any):MutableSet<String>?{
        var stringSet:MutableSet<String>?=null
        val sharedPrefs=getSharedPrefs(context)
        if(null!= sharedPrefs){
            stringSet = sharedPrefs.getStringSet(key.toString(), mutableSetOf())
        }
        return stringSet
    }

    fun setString(context:Context,key: Any, value: String) {
        val sharedPrefs = getSharedPrefs(context)
        sharedPrefs.edit().putString(key.toString(), value).commit()
    }

    fun getString(context:Context,key: Any): String?{
        val sharedPrefs = getSharedPrefs(context)
        return sharedPrefs?.getString(key.toString(), null)
    }

    fun setBoolean(context:Context,key: Any, value: Boolean){
        val sharedPrefs = getSharedPrefs(context)
        sharedPrefs?.edit()?.putBoolean(key.toString(), value)?.commit()
    }

    fun getBoolean(context:Context,key: Any): Boolean {
        val sharedPrefs = getSharedPrefs(context)
        return sharedPrefs.getBoolean(key.toString(), false)
    }

}