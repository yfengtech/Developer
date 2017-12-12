package cz.developer.library

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.support.v4.app.Fragment
import cz.developer.library.callback.MyActivityLifecycleCallback
import cz.developer.library.exception.MyUncaughtExceptionHandler
import cz.developer.library.prefs.DeveloperPrefs
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

fun Application.developer(application: Application, config: DeveloperConfig.()->Unit) {
    //配置
    DeveloperManager.developerConfig = DeveloperConfig().apply(config)
    //首次初始化
    if(!DeveloperPrefs.initConfig){
        DeveloperPrefs.initConfig=true
        //设置初始调试视图
        DeveloperPrefs.setBoolean(DeveloperPrefs.HIERARCHY_KEY, DeveloperManager.developerConfig.hierarchy)
    }
    //先记录
    Developer.applicationContext=application.applicationContext
    //初始化sharedPrefs
    DeveloperPrefs.sharedPrefs= application.applicationContext.getSharedPreferences("developer", Context.MODE_PRIVATE)
    //注册
    application.registerActivityLifecycleCallbacks(MyActivityLifecycleCallback())
    //处理异常,直接包装
    val uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler(MyUncaughtExceptionHandler(application.cacheDir,uncaughtExceptionHandler))
}

internal object Developer{
    var applicationContext: Context?=null
        get() {
            //部分5.0以下手机,在子线程内,利用反射取Context,会报空.所以这里如果是子线程,且小于5.0
            if(null==field){
                val application = Class.forName("android.app.ActivityThread")?.getMethod("currentApplication")?.invoke(null) as? Application
                field=application?.applicationContext
            }
            return field
        }

    val packageName:String?
        get() {
            var appContext: Context? = null
            if (null == appContext) {
                appContext = applicationContext
            }
            return appContext?.packageName
        }

}