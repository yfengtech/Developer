package cz.developer.library.log

import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 2017/10/20.
 * 日志文件记录
 */
internal object FilePrefs {
    private val developerFolder=File(Environment.getExternalStorageDirectory(),"appDeveloper")
    private val networkFolder=File(developerFolder,"network")
    private var errorFile:File=File(developerFolder,"error.log")
    private var requestFolder:File?=null
    private var file:File?=null


    init {
        //创建根目录
        ensureFolder(developerFolder,networkFolder)
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
        //本次打开日志文件
        file=File(developerFolder,SimpleDateFormat("yy-MM-dd-HH:mm:ss").format(Date())+".txt")
        //本次请求目录
        requestFolder=File(networkFolder,SimpleDateFormat("MM_dd_HH.mm.ss").format(Date()))
    }

    /**
     * 创建新的请求记录文件
     */
    fun newRequestFile():File{
        return File(requestFolder?: networkFolder,SimpleDateFormat("yy-MM-dd-HH:mm:ss").format(Date())+".txt")
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