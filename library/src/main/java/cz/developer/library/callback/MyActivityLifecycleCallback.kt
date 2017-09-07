package cz.developer.library.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


import cz.developer.library.DeveloperActivityManager
import cz.developer.library.DeveloperManager
import cz.developer.library.widget.DeveloperLayout


/**
 * Created by cz on 9/7/16.
 */
class MyActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    init {
        DeveloperActivityManager.clear()
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (null != activity) {
            DeveloperActivityManager.add(activity)
            injectLayout(activity)
        }
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
                //回调长按监听
                layout.onItemLongClickListener{ DeveloperManager.onItemLongClick(activity,it) }
                layout.addView(childView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                decorView.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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
