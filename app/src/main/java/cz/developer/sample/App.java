package cz.developer.sample;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.PrintWriter;
import java.io.StringWriter;

import cz.developer.library.DeveloperConfig;
import cz.developer.library.DeveloperManager;
import cz.developer.sample.impl.ImageAdapterImpl;
import cz.developer.sample.impl.NetworkAdapter;
import cz.developer.sample.impl.SwitchImpl;

/**
 * Created by Administrator on 2016/11/7.
 */

public class App extends Application implements Thread.UncaughtExceptionHandler {
    private static final String TAG="App";
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
        DeveloperConfig.Builder builder=new DeveloperConfig.Builder();
        builder.setChannel("Channel");
        builder.setSwitchInterface(new SwitchImpl());//开关
        builder.setNetworkAdapter(new NetworkAdapter());//网络
        builder.setImageAdapter(new ImageAdapterImpl());//图片调试
        DeveloperManager.getInstances().init(this,builder.build());

        Fresco.initialize(getApplicationContext());

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        Log.e(TAG,stackTrace.toString());
    }
}
