package cz.developer.library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

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

    public static final DeveloperManager instances=new DeveloperManager();
    private DeveloperConfig config;

    public static DeveloperManager getInstances(){
        return instances;
    }

    public static void toDeveloper(FragmentActivity activity){
        toFragment(activity, DeveloperFragment.newInstance());
    }

    public static void toFragment(FragmentActivity activity,Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        View contentView = getContentView(activity.getWindow().getDecorView());
        contentView.setId(R.id.activity_container);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out);
        fragmentTransaction.addToBackStack(fragment.getClass().getName()).add(R.id.activity_container,fragment).commit();
    }

    private static View getContentView(View  view) {
        View contentView = null;
        if (view instanceof ViewGroup) {
            ViewGroup childViewGroup = (ViewGroup) view;
            int childCount = childViewGroup.getChildCount();
            if(2==childCount&&(childViewGroup.getChildAt(0) instanceof ViewStub)&&(childViewGroup.getChildAt(1) instanceof FrameLayout)){
                contentView=childViewGroup.getChildAt(1);
            } else {
                for(int i=0;i<childCount;i++){
                    return getContentView(childViewGroup.getChildAt(i));
                }
            }
        }
        return contentView;
    }

    public void setDeveloperConfig(DeveloperConfig config){
        this.config=config;
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
