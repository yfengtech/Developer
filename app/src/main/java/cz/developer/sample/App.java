package cz.developer.sample;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
        builder.setImageDisplay((imageView, url, displayComplete) -> display((SimpleDraweeView) imageView,url,displayComplete));//加载图片
        DeveloperManager.getInstances().init(this,builder.build());

        Fresco.initialize(getApplicationContext());
    }

    public void display(final SimpleDraweeView view, final String url, final OnDisplayComplete listener) {
        view.setTag(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setTapToRetryEnabled(true)
                .setOldController(view.getController())
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        ImageRequest imageRequest = ImageRequestBuilder
                                .newBuilderWithSource( Uri.parse(url))
                                .setProgressiveRenderingEnabled(true)
                                .build();
                        ImagePipeline imagePipeline = Fresco.getImagePipeline();
                        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest,getApplicationContext());
                        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                            @Override
                            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                if(null!=listener){
                                    listener.onComplete(url,getCacheFile(url),bitmap);
                                }
                            }
                            @Override
                            public void onFailureImpl(DataSource dataSource) {
                            }
                        }, CallerThreadExecutor.getInstance());

                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                    }

                    @Override
                    public void onRelease(String id) {
                    }
                }).build();
        view.setController(controller);
    }



    public File getCacheFile(String url){
        File file=null;
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(new SimpleCacheKey(url));
        if(null!=resource){
            file=resource.getFile();
        }
        return file;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        Log.e(TAG,stackTrace.toString());
    }
}
