package cz.developer.library.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.DebugViewHelper;
import cz.developer.library.DeveloperActivityManager;
import cz.developer.library.DeveloperManager;
import cz.developer.library.model.DebugViewItem;
import cz.developer.library.xml.ViewConfigReader;
import xyqb.library.config.PrefsManager;


/**
 * Created by cz on 9/7/16.
 */
public class MyActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "MyActivityLifecycleCallback";
    private final List<DebugViewItem> viewItems;
    public MyActivityLifecycleCallback() {
        List<DebugViewItem> viewItems= PrefsManager.readConfig(ViewConfigReader.class);
        this.viewItems=new ArrayList<>();
        if(null!=viewItems){
            this.viewItems.addAll(viewItems);
        }
        DeveloperActivityManager.get().clear();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if(null!=activity){
            DeveloperActivityManager.get().add(activity);
        }
        //仅开启时,初始化当前activity
        if(null!=activity&& DeveloperManager.config.debugList){
            View decorView =  activity.getWindow().getDecorView();
            ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
            DebugViewHelper.initLayout(contentView,true,false);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        View decorView =  activity.getWindow().getDecorView();
        ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
        contentView.getChildCount();
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        DeveloperActivityManager.get().remove(activity);
    }
}
