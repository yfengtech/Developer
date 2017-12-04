package cz.developer.library.callback

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cz.developer.library.*


import cz.developer.library.log.FilePrefs
import cz.developer.library.ui.hierarchy.HierarchyFragment
import cz.developer.library.widget.DeveloperLayout
import cz.developer.library.widget.dialog.DeveloperDialog
import cz.developer.library.widget.hierarchy.HierarchyNode


/**
 * Created by cz on 9/7/16.
 */
class MyActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    init {
        DeveloperActivityManager.clear()
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (null != activity) {
            //代表应用启动
            val activityName=activity::class.java.name
            val launchActivityName = getLaunchActivityName(activity)
            if (activityName == launchActivityName) {
                //初始化记录文件
                FilePrefs.newRecordFile()
            }
            //添加并操作activity
            DeveloperActivityManager.add(activity)
            //屏蔽掉调试界面,检测activity是否使用了appCompat style
            val a = activity.obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme)
            if(activityName!=DeveloperActivity::class.java.name&&
                    a.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTheme_windowActionBar)){
                a.recycle()
                injectLayout(activity)
            }
        }
    }

    fun getLaunchActivityName(context: Context): String {
        val packageManager = context.packageManager
        val launchIntent = packageManager.getLaunchIntentForPackage(context.packageName)
        return launchIntent.component.className
    }

    /**
     * 注入布局
     */
    private fun injectLayout(activity: Activity) {
        val decorView = activity.window.decorView
        if (null != decorView && decorView is ViewGroup) {
            if (0 < decorView.childCount) {
                //加入统计浮层
                val childView = decorView.getChildAt(0)
                decorView.removeView(childView)
                val layout = DeveloperLayout(activity)
                layout.addView(childView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                decorView.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                val developerConfig=DeveloperManager.developerConfig
                //初始化调试信息
                View.inflate(activity,R.layout.developer_menu_layout,layout)
                //回调长按监听
                layout.onItemLongClickListener{ DeveloperManager.onItemLongClick(activity,it) }
//                //打开界面视图
                layout.findViewById(R.id.developerMenu).setOnClickListener {
                    //将当前界面所有控件节点信息扫描出来
                    if(activity is FragmentActivity){
                        DeveloperDialog().show(activity.supportFragmentManager,null)
                    }
                }
                //关闭布局
                if(null==developerConfig||!developerConfig.hierarchy){
                    activity.closeDeveloperLayout()
                }
            }
        }
    }


    override fun onActivityStarted(activity: Activity?)=Unit

    override fun onActivityResumed(activity: Activity?)=Unit

    override fun onActivityPaused(activity: Activity?)=Unit

    override fun onActivityStopped(activity: Activity?)=Unit

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?)=Unit

    override fun onActivityDestroyed(activity: Activity?) {
        DeveloperActivityManager.remove(activity)
    }
}
