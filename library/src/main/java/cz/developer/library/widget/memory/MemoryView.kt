package cz.developer.library.widget.memory

import android.app.ActivityManager
import android.content.Context
import android.util.AttributeSet
import java.util.*

/**
 * Created by cz on 2017/9/11.
 */
class MemoryView(context: Context,attrs: AttributeSet? = null) : CurveChartView(context, attrs){
    private val DURATION: Long = 600
    private var timer: Timer=Timer()

    fun start() {
        var timerTask=HeapTimerTask()
        timer.scheduleAtFixedRate(timerTask, 0, DURATION)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }


    inner class PssTimerTask : MemoryTimerTask() {
        override val value: Float
            get() {
                val pid = android.os.Process.myPid()
                val pssInfo = getApplicationPssInfo(pid)
                return pssInfo.totalPss as Float / 1024
            }
    }

    inner class HeapTimerTask : MemoryTimerTask() {

        override val value: Float
            get() {
                val dalvikHeapMem = getApplicationDalvikHeapMem()
                return dalvikHeapMem.allocated *1f / 1024
            }
    }

    inner abstract class MemoryTimerTask : TimerTask() {

        abstract val value: Float

        override fun run() {
            addData(value)
//            post{ memoryView.setText(value) }
        }
    }


    fun stop() {
        timer.cancel()
    }

    /**
     * 获取应用实际占用内存

     * @param context
     * *
     * @param pid
     * *
     * @return 应用pss信息KB
     */
    fun getApplicationPssInfo(pid: Int): PssInfo {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = am.getProcessMemoryInfo(intArrayOf(pid))[0]
        val pssInfo = PssInfo()
        pssInfo.totalPss = memoryInfo.totalPss
        pssInfo.dalvikPss = memoryInfo.dalvikPss
        pssInfo.nativePss = memoryInfo.nativePss
        pssInfo.otherPss = memoryInfo.otherPss
        return pssInfo
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

    /**
     * 应用实际占用内存（共享按比例分配）
     */
    class PssInfo {
        var totalPss: Int = 0
        var dalvikPss: Int = 0
        var nativePss: Int = 0
        var otherPss: Int = 0
    }
}