package cz.developer.library;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cz.developer.library.callback.MyActivityLifecycleCallback;
import cz.developer.library.ui.DeveloperFragment;
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
    public static final DeveloperManager instances=new DeveloperManager();
    private DeveloperConfig config;

    public static DeveloperManager getInstances(){
        return instances;
    }

    static {
        callback=new MyActivityLifecycleCallback();
    }


    public static void toDeveloper(FragmentActivity activity){
        toFragment(activity, DeveloperFragment.newInstance());
    }

    public static void toFragment(FragmentActivity activity,Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out);
        fragmentTransaction.addToBackStack(fragment.getClass().getName()).add(android.R.id.content,fragment).commit();
    }


    public void init(Application application, DeveloperConfig config){
        this.config=config;
        if(null!=callback){
            application.unregisterActivityLifecycleCallbacks(callback);
        }
        application.registerActivityLifecycleCallbacks(callback);
    }

    public ISwitchInterface getSwitchInterface(){
        return this.config.switchConfig;
    }

    public DeveloperConfig getDeveloperConfig(){
        return this.config;
    }

    public INetworkAdapter getNetworkAdapter(){
        return this.config.networkAdapter;
    }
}
