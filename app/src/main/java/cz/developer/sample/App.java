package cz.developer.sample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import cz.developer.library.DeveloperConfig;
import cz.developer.library.DeveloperManager;
import cz.developer.sample.impl.ImageAdapterImpl;
import cz.developer.sample.impl.NetworkAdapter;
import cz.developer.sample.impl.SwitchImpl;

/**
 * Created by Administrator on 2016/11/7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DeveloperConfig.Builder builder=new DeveloperConfig.Builder();
        builder.setChannel("Channel");
        builder.setSwitchInterface(new SwitchImpl());//开关
        builder.setNetworkAdapter(new NetworkAdapter());//网络
        builder.setImageAdapter(new ImageAdapterImpl());//图片调试
        DeveloperManager.getInstances().setDeveloperConfig(builder.build());

        Fresco.initialize(getApplicationContext());

    }

}
