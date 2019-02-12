package cz.developer.library.ui.appinfo

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.appinfo.DebugPropItemAdapter
import cz.developer.library.appinfo.BuildProp
import kotlinx.android.synthetic.main.fragment_debug_app_info.*
import java.io.FileNotFoundException


/**
 * Created by cz on 15/12/1.
 */
internal class DebugAppInfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_app_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context =context?:return
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            toolBar.subtitle=arguments?.getString("desc")
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager?.popBackStack() }
        }
        //软件信息
        val propItems= mutableMapOf<String,MutableList<PropItem>>()
        var items=propItems.getOrPut(getString(R.string.app_info)){ mutableListOf<PropItem>()}
        items.add(PropItem(getString(R.string.channel_value,DeveloperManager.developerConfig.channel)))
        items.add(PropItem(getString(R.string.app_version_value,appVersionName)))
        items.add(PropItem(getString(R.string.app_code_value,appVersionCode)))

        //机器信息
        items=propItems.getOrPut(getString(R.string.model_info)){ mutableListOf<PropItem>()}
        items.add(PropItem(getString(R.string.os_version_value,Build.DISPLAY)))
        items.add(PropItem(getString(R.string.os_api_value,Build.VERSION.SDK_INT)))
        items.add(PropItem(getString(R.string.device_model_value,Build.MODEL)))
        items.add(PropItem(getString(R.string.device_brand_value,Build.BRAND)))
        items.add(PropItem(getString(R.string.imei_value,imeiValue)))
        items.add(PropItem(getString(R.string.android_id_value,androidId)))

//        items=propItems.getOrPut("BuildProp"){ mutableListOf()}
//        try {
//            BuildProp.buildProperties?.forEach { items.add(PropItem("${it.key} = ${it.value}")) }
//        } catch (e: Exception){
//            e.printStackTrace()
//        }
        //重置分类
        val adapterItems=propItems.flatMap { (key,item)-> item.onEach { it.group=key } }.toList()
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter= DebugPropItemAdapter(context, adapterItems)
    }

    /**
     * 获得软件版本

     * @return
     */
    private val appVersionName: String?
        get() {
            var appVersion: String? = null
            try {
                appVersion = context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return appVersion
        }

    /**
     * 获得软件code

     * @return
     */
    private val appVersionCode: Int
        get() {
            var appCode=0
            try {
                val context = context
                appCode = context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionCode?:-1
            } catch (e: PackageManager.NameNotFoundException) {
                appCode = -1
            }

            return appCode
        }



    /**
     * 获得android设备id

     * @return
     */
    private val androidId: String?
        get() {
            var value: String?
            try {
                val appContext = context
                value = Settings.Secure.getString(appContext?.contentResolver, Settings.Secure.ANDROID_ID)
            } catch (e: Exception) {
                e.printStackTrace()
                value = "Android Id 获取失败!"
            }
            return value
        }

    /**
     * @return
     */
    val imeiValue: String
        get() {
            var value: String
            if (hasPermission("android.permission.READ_PHONE_STATE")) {
                try {
                    value = (context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                } catch (e: Exception) {
                    value = "获取imei码失败了~"
                }
            } else {
                value = "Need READ_PHONE_STATE Permission"
            }
            return value
        }

    /**
     * 判断某个权限是否授权

     * @param permissionName 权限名称，比如：android.permission.READ_PHONE_STATE
     * *
     * @return
     */
    private fun hasPermission(permissionName: String): Boolean {
        var result = false
        try {
            val context = context
            val pm = context?.packageManager
            result = PackageManager.PERMISSION_GRANTED == pm?.checkPermission(permissionName, context?.packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

}
