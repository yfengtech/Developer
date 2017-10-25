package cz.developer.library.exception

import cz.developer.library.log.FilePrefs
import java.io.File
import java.io.FilePermission
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by cz on 2017/9/5.
 */
class MyUncaughtExceptionHandler(val cacheDir: File, val handler:Thread.UncaughtExceptionHandler?):Thread.UncaughtExceptionHandler{
    override fun uncaughtException(t: Thread, e: Throwable) {
        handler?.uncaughtException(t,e)
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))
        val exceptionStackTrace = e.stackTrace
        val stackTraceElement = exceptionStackTrace[0]

        val out=StringBuilder()
        val exceptionName=e::class.java.name
        val threadName=t.name
        val className = stackTraceElement.className
        val methodName = stackTraceElement.methodName
        val lineNumber = stackTraceElement.lineNumber

        out.append("$exceptionName\n")
        out.append("$threadName\n")
        out.append("$className\n")
        out.append("$methodName($lineNumber)\n")
        out.append("$stackTrace\n")

        val file=File(FilePrefs.exceptionFolder, SimpleDateFormat("yy-MM-dd-HH:mm:ss").format(Date())+".txt")
        file.outputStream().write(out.toString().toByteArray())
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}