package cz.developer.library

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.support.v4.app.Fragment
import cz.developer.library.widget.DeveloperLayout

/**
 * Created by cz on 2017/10/24.
 */
/**
 * 开启调试模块
 */
internal fun Fragment.openDeveloperLayout(){
    activity?.openDeveloperLayout()
}

internal fun Activity.openDeveloperLayout(){
    val layout=findViewById(R.id.developerContainer)
    if(null!=layout&&layout is DeveloperLayout){
        layout.openDeveloperLayout()
    }
}

/**
 * 关闭调试模块
 */
internal fun Fragment.closeDeveloperLayout(){
    activity?.closeDeveloperLayout()
}

internal fun Activity.closeDeveloperLayout(){
    val layout=findViewById(R.id.developerContainer)
    if(null!=layout&&layout is DeveloperLayout){
        layout.closeDeveloperLayout()
    }
}

internal fun Activity.setViewDebug(debug:Boolean){
    val layout=findViewById(R.id.developerContainer)
    if(null!=layout&&layout is DeveloperLayout){
        layout.setViewDebug(debug)
    }
}

internal object Developer{
    val applicationContext: Context
        get() {
            //部分5.0以下手机,在子线程内,利用反射取Context,会报空.所以这里如果是子线程,且小于5.0,直接post到子线程执行,外围通过while(判断是否取到数据
            val application = Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null) as Application
            return application.applicationContext
        }

    val resources: Resources
        get() {
            var appContext: Context? = null
            while (null == appContext) {
                appContext = applicationContext
            }
            return appContext.resources
        }

    val packageName:String
        get() {
            var appContext: Context? = null
            while (null == appContext) {
                appContext = applicationContext
            }
            return appContext.packageName
        }

}