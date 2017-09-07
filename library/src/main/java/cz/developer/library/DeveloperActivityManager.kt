package cz.developer.library

import android.app.Activity

import java.util.ArrayList

/**
 * Created by cz on 1/11/17.
 */

object DeveloperActivityManager {
    private val linkActivities = ArrayList<Activity>()

    /**
     * 清除所有activity对角
     */
    fun clear() {
        linkActivities.clear()
    }


    /**
     * 添加activity

     * @param activity
     */
    fun add(activity: Activity?) {
        if (null != activity) {
            linkActivities.add(activity)
        }
    }

    /**
     * 移除指定activity

     * @param activity
     */
    fun remove(activity: Activity?) {
        if (null != activity) {
            linkActivities.remove(activity)
        }
    }

    fun forEach(action:(Activity)->Unit){
        linkActivities.forEach(action)
    }


    fun size(): Int {
        return linkActivities.size
    }

}
