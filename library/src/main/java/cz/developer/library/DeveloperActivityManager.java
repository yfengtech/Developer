package cz.developer.library;

import android.app.Activity;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by cz on 1/11/17.
 */

public class DeveloperActivityManager {
    private static final DeveloperActivityManager instance = new DeveloperActivityManager();
    private static final ArrayList<Activity> linkActivities = new ArrayList<>();

    private DeveloperActivityManager() {
    }

    public static DeveloperActivityManager get() {
        return instance;
    }


    /**
     * 清除所有activity对角
     */
    public void clear() {
        linkActivities.clear();
    }


    /**
     * 添加activity
     *
     * @param activity
     */
    public void add(Activity activity) {
        if(null!=activity){
            linkActivities.add(activity);
        }
    }

    /**
     * 移除指定activity
     *
     * @param activity
     */
    public void remove(Activity activity) {
        if(null!=activity){
            linkActivities.remove(activity);
        }
    }


    public int size() {
        return linkActivities.size();
    }


    /**
     * 遍历当前activity列
     *
     * @param action
     */
    public void forActivities(Action1<Activity> action) {
        int size = linkActivities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = linkActivities.get(i);
            if (null != activity && null != action) {
                action.call(activity);
            }
        }
    }
}
