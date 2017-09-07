package cz.developer.library.ui.appinfo

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import cz.developer.library.DeveloperConfig
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import kotlinx.android.synthetic.main.fragment_debug_app_info.*


/**
 * Created by cz on 15/12/1.
 */
class DebugAppInfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_app_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        //软件信息
        setText(view, R.id.tv_channel, R.string.channel_value, DeveloperManager.developerConfig.channel)
        setText(view, R.id.tv_version, R.string.app_version_value, appVersionName)
        setText(view, R.id.tv_version_code, R.string.app_code_value, appVersionCode)

        //机器信息
        setText(view, R.id.tv_os_version, R.string.os_version_value, Build.DISPLAY)
        setText(view, R.id.tv_os_api, R.string.os_api_value, Build.VERSION.SDK_INT)
        setText(view, R.id.tv_device_model, R.string.device_model_value, Build.MODEL)
        setText(view, R.id.tv_device_brand, R.string.device_brand_value, Build.BRAND)
        setText(view, R.id.tv_imei, R.string.imei_value, imeiValue)
        setText(view, R.id.tv_android_id, R.string.android_id_value, androidId)
    }

    fun setText(view: View, @IdRes id: Int, @StringRes res: Int, vararg params: Any?) {
        val textView = view.findViewById(id) as TextView
        textView.text = getString(res, *params)
    }

    /**
     * 获得软件版本

     * @return
     */
    private val appVersionName: String?
        get() {
            var appVersion: String? = null
            try {
                val context = context
                appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
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
                appCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
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
                value = Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
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
                    value = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
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
            val pm = context.packageManager
            result = PackageManager.PERMISSION_GRANTED == pm.checkPermission(permissionName, context.packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

}
