package cz.developer.library;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import cz.developer.library.callback.MyActivityLifecycleCallback;
import cz.developer.library.ui.ImageDisplayInterface;
import cz.developer.library.ui.network.INetworkAdapter;
import cz.developer.library.ui.switchs.ISwitchInterface;

/**
 * Created by czz on 2016/10/29.
 * 1:提供单独设定域名方法
 * 2:提供自定义应用内展示信息模块(app info)
 * 3:
 */
public class DeveloperManager {
    private static final MyActivityLifecycleCallback callback;
    public static final int VIEW_TAG=DeveloperManager.class.hashCode();
    public static final DeveloperManager instances=new DeveloperManager();
    public static final PrefsConfig config;
    private DeveloperConfig developerConfig;

    public static DeveloperManager getInstances(){
        return instances;
    }

    static {
        config=new PrefsConfig();
        callback=new MyActivityLifecycleCallback();
    }

    public static void setViewTag(View view, Object tag){
        if(null!=view&&null!=tag){
            view.setTag(VIEW_TAG,tag);
            view.setLongClickable(true);
        }
    }

    public static Object getViewTag(View view){
        Object tag=null;
        if(null!=view){
            tag=view.getTag(VIEW_TAG);
        }
        return tag;
    }

    public static void toFragment(FragmentActivity activity,Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out);
        fragmentTransaction.addToBackStack(fragment.getClass().getName()).add(R.id.container,fragment).commit();
    }


    public void init(Application application, DeveloperConfig config){
        this.developerConfig =config;
        if(null!=callback){
            application.unregisterActivityLifecycleCallbacks(callback);
        }
        application.registerActivityLifecycleCallbacks(callback);
    }

    public ISwitchInterface getSwitchInterface(){
        return this.developerConfig.switchConfig;
    }

    public DeveloperConfig getDeveloperConfig(){
        return this.developerConfig;
    }

    public INetworkAdapter getNetworkAdapter(){
        return this.developerConfig.networkAdapter;
    }

    public ImageDisplayInterface getImageDisplay() {
        return this.developerConfig.imageDisplay;
    }
}
