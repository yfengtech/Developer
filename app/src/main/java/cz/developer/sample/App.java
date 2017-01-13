package cz.developer.sample;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import cz.developer.library.DeveloperConfig;
import cz.developer.library.DeveloperManager;
import cz.developer.library.callback.OnDisplayComplete;
import cz.developer.library.ui.ImageDisplayInterface;
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
        builder.setImageDisplay(new ImageDisplayInterface() {
            @Override
            public void onDisplay(ImageView imageView, String url, OnDisplayComplete displayComplete) {
                display((SimpleDraweeView) imageView,url,displayComplete);
            }

            @Override
            public File getImageFile(String url) {
                return getCacheFile(url);
            }
        });
        DeveloperManager.getInstances().init(this,builder.build());

        Fresco.initialize(getApplicationContext());

    }

    public void display(final SimpleDraweeView view, final String url, final OnDisplayComplete listener) {
        view.setTag(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setTapToRetryEnabled(true)
                .setOldController(view.getController()).build();
        view.setController(controller);
    }

    public File getCacheFile(String url){
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(new SimpleCacheKey(url));
        return resource.getFile();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        Log.e(TAG,stackTrace.toString());
    }
}
