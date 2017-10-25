package cz.developer.library.log

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import cz.developer.library.Developer
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

/**
 * Created by cz on 2017/10/20.
 * 日志文件记录
 */
internal object FilePrefs {
    private val developerFolder=File(Environment.getDataDirectory(),"/data/${Developer.packageName}/developer")
    internal val networkFolder=File(developerFolder,"network")
    internal var exceptionFolder=File(developerFolder,"error")
    internal var tmpFile:File=File(developerFolder,"tmp.txt")
    private var errorFile:File=File(developerFolder,"error.txt")
    private var requestFolder:File?=null
    private var file:File?=null


    init {
        ensureFolder(developerFolder,networkFolder,exceptionFolder)
    }

    fun ensureFolder(vararg files: File){
        files.forEach {
            if(!it.exists()){
                it.mkdirs()
            }
        }
    }

    /**
     * 创建新的记录文件
     */
    fun newRecordFile(){
        ensureFolder(developerFolder,networkFolder,exceptionFolder)
        //删除限定个数内的log文件
        developerFolder.listFiles()?.
                filter { it.name.endsWith("txt") }?.
                sortedBy { -it.lastModified() }?.
                filterIndexed { index, _ -> 1<index }?.forEach{it.delete()}
        //删除限定个数内的网络目录文件
        networkFolder.listFiles()?.
                sortedBy { -it.lastModified() }?.
                filterIndexed { index, _ -> 1<index }?.forEach{it.deleteRecursively()}
        //删除异常目录内,超过5个的旧数据
        exceptionFolder.listFiles()?.
                sortedBy { -it.lastModified() }?.
                filterIndexed { index, _ -> 4<index }?.forEach{it.deleteRecursively()}
        //本次打开日志文件
        file=File(developerFolder,SimpleDateFormat("yy-MM-dd-HH:mm:ss").format(Date())+".txt")
        //本次请求目录
        requestFolder=File(networkFolder,SimpleDateFormat("MM_dd_HH.mm.ss").format(Date()))

        //创建文件
        file?.createNewFile()
        requestFolder?.mkdirs()
    }

    /**
     * 创建新的请求记录文件
     */
    fun newRequestFile(info:String?=null):File{
        return File(requestFolder?: networkFolder,(info?:"")+SimpleDateFormat("yy-MM-dd-HH:mm:ss").format(Date())+".txt")
    }


    /**
     * 记录事件日志
     */
    fun eventLog(content:String){
        //如果file对象为空则使用errorFile
        log(file ?: errorFile,"<#$content#>\n")
    }

    /**
     * 保存文件日志
     * @param content
     */
    fun log(file:File,content: String) {
        var writer: BufferedWriter? = null
        try {
            writer = BufferedWriter(FileWriter(file, true))
            writer.write(content)
        } catch (e: IOException) {
        } finally {
            writer?.close()
        }
    }
}