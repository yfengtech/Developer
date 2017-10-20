package cz.developer.sample

import android.app.Application
import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.Log

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo

import java.io.PrintWriter
import java.io.StringWriter

import cz.developer.library.DeveloperManager

/**
 * Created by Administrator on 2016/11/7.
 */

class App : Application(), Thread.UncaughtExceptionHandler {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(this)
        DeveloperManager.init(this){
            //配置渠道
            channel="Channel"
            //数据切换
            switch {
                item(key="log",desc="日志调试")
                item(key="network",desc="网络请求")
                item(key="data",desc="数据调试")
                onItemChecked { s, b ->  }
            }
            //网络模块
            network {
                serverUrl = arrayOf("https://www.baidu.com",
                        "https://www.hao123.com/",
                        "http://www.sina.com.cn/",
                        "http://www.jd.com/")
            }
        }

        Fresco.initialize(applicationContext)
    }

    fun display(view: SimpleDraweeView, url: String) {
        view.tag = url
        val controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setTapToRetryEnabled(true)
                .setOldController(view.controller)
                .setControllerListener(object : ControllerListener<ImageInfo> {
                    override fun onSubmit(id: String, callerContext: Any) {}

                    override fun onFinalImageSet(id: String, imageInfo: ImageInfo?, animatable: Animatable?) {
                    }

                    override fun onIntermediateImageSet(id: String, imageInfo: ImageInfo?) {}

                    override fun onIntermediateImageFailed(id: String, throwable: Throwable) {}

                    override fun onFailure(id: String, throwable: Throwable) {}

                    override fun onRelease(id: String) {}
                }).build()
        view.controller = controller
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))
        Log.e(TAG, stackTrace.toString())
    }

    companion object {
        private val TAG = "App"
    }
}
