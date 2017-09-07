package cz.developer.library.exception

/**
 * Created by cz on 2017/9/5.
 */
class MyUncaughtExceptionHandler(val handler:Thread.UncaughtExceptionHandler?):Thread.UncaughtExceptionHandler{
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        handler?.uncaughtException(t,e)
        //todo
    }
}