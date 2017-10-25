package cz.developer.library.widget.memory

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by cz on 2017/9/11.
 */
class MemoryView(context: Context,attrs: AttributeSet? = null) : CurveChartView(context, attrs){
    private val DURATION: Long = 800
    private var action= object :Runnable {
        override fun run() {
            val dalvikHeapMem = getApplicationDalvikHeapMem()
            addData(dalvikHeapMem.allocated *1f / 1024)
            postDelayed(this,DURATION)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(View.VISIBLE==visibility){
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if(View.VISIBLE==visibility){
            start()
        } else {
            stop()
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if(hasWindowFocus&&View.VISIBLE==visibility){
            start()
        } else if(!hasWindowFocus){
            stop()
        }
    }

    fun start() {
        removeCallbacks(action)
        post(action)
    }

    fun stop() {
        removeCallbacks(action)
    }

    /**
     * 获取应用dalvik内存信息
     * @return dalvik堆内存KB
     */
    fun getApplicationDalvikHeapMem(): DalvikHeapMem {
        val runtime = Runtime.getRuntime()
        val dalvikHeapMem = DalvikHeapMem()
        dalvikHeapMem.freeMem = runtime.freeMemory() / 1024
        dalvikHeapMem.maxMem = Runtime.getRuntime().maxMemory() / 1024
        dalvikHeapMem.allocated = (Runtime.getRuntime().totalMemory() - runtime.freeMemory()) / 1024
        return dalvikHeapMem
    }

    /**
     * Dalvik堆内存，只要App用到的内存都算（包括共享内存）
     */
    class DalvikHeapMem {
        var freeMem: Long = 0
        var maxMem: Long = 0
        var allocated: Long = 0
    }
}