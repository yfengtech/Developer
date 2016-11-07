package cz.developer.sample;

import android.app.Application;

import cz.developer.library.DeveloperConfig;
import cz.developer.library.DeveloperManager;
import cz.developer.sample.impl.SwitchImpl;

/**
 * Created by Administrator on 2016/11/7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DeveloperConfig.Builder builder=new DeveloperConfig.Builder();
        builder.setSwitchInterface(new SwitchImpl());
        DeveloperManager.getInstances().setDeveloperConfig(builder.build());
    }
}
