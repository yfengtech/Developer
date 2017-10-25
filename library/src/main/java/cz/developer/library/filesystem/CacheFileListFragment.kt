package cz.developer.library.ui.filesystem

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.R
import cz.developer.library.ui.filesystem.adapter.FileSystemAdapter
import kotlinx.android.synthetic.main.fragment_cache_file_list.*
import java.io.File
import android.content.Intent
import android.net.Uri
import com.cz.recyclerlibrary.callback.OnItemLongClickListener
import cz.developer.library.DeveloperManager


/**
 * Created by cz on 2017/10/19.
 */
internal class CacheFileListFragment: Fragment(){
    companion object {
        fun newInstance(file:File):Fragment{
            return CacheFileListFragment().apply {
                arguments=Bundle().apply { putSerializable("file",file) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cache_file_list,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val file=arguments.getSerializable("file") as File
        recyclerView.layoutManager=LinearLayoutManager(context)
        val adapter=FileSystemAdapter(context,file.listFiles()?.reversed())
        recyclerView.adapter=adapter
        recyclerView.onItemClick { _, _, position ->
            val activity=activity
            if(activity is FragmentActivity){
                val file=adapter.getItem(position)
                if(file.isDirectory){
                    if(0<(file.listFiles()?.size?:0)){
                        activity.supportFragmentManager.
                                beginTransaction().
                                addToBackStack(null).
                                add(R.id.fileFragmentContainer, CacheFileListFragment.newInstance(file)).commit()
                    } else {
                        Snackbar.make(recyclerView,R.string.no_file_found,Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    //打开文件
                    openFile(file)
                }
            }
        }

    }

    fun openFile(file:File) {
        val filePath=file.absolutePath
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length).toLowerCase()
        when(end){
            in arrayOf("m4a","mp3","mid","xmf","ogg","wav")-> startAudioFileIntent(filePath)
            "3gp","mp4"-> startAudioFileIntent(filePath)
            "jpg","gif","png","jpeg", "bmp"-> startImageFileIntent(filePath)
            "apk"-> startApkFileIntent(filePath)
            "ppt"-> startPptFileIntent(filePath)
            "xls"-> startExcelFileIntent(filePath)
            "doc"-> startWordFileIntent(filePath)
            "pdf"-> startPdfFileIntent(filePath)
            "chm"-> startChmFileIntent(filePath)
            "xml","txt"-> startTextFileIntent(filePath)
            else-> startAllIntent(filePath)
        }
    }

    //Android获取一个用于打开APK文件的intent
    fun startAllIntent(param: String) {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = android.content.Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "*/*")
        startActivity(intent)
    }

    //Android获取一个用于打开APK文件的intent
    fun startApkFileIntent(param: String) {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = android.content.Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(intent)
    }

    //Android获取一个用于打开VIDEO文件的intent
    fun startVideoFileIntent(param: String) {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "video/*")
        startActivity(intent)
    }

    //Android获取一个用于打开AUDIO文件的intent
    fun startAudioFileIntent(param: String){
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "audio/*")
        startActivity(intent)
    }

    //Android获取一个用于打开Html文件的intent
    fun startHtmlFileIntent(param: String) {
        val uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, "text/html")
        startActivity(intent)
    }

    //Android获取一个用于打开图片文件的intent
    fun startImageFileIntent(param: String){
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "image/*")
        startActivity(intent)
    }

    //Android获取一个用于打开PPT文件的intent
    fun startPptFileIntent(param: String) {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        startActivity(intent)
    }

    //Android获取一个用于打开Excel文件的intent
    fun startExcelFileIntent(param: String) {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        startActivity(intent)
    }

    //Android获取一个用于打开Word文件的intent
    fun startWordFileIntent(param: String) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/msword")
        startActivity(intent)
    }

    //Android获取一个用于打开CHM文件的intent
    fun startChmFileIntent(param: String) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/x-chm")
        startActivity(intent)
    }

    //Android获取一个用于打开文本文件的intent
    fun startTextFileIntent(param: String) {
//        val intent = Intent()
//        intent.action = android.content.Intent.ACTION_VIEW
//        intent.setDataAndType(Uri.fromFile(File(param)), "text/plain")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
        DeveloperManager.toDeveloperFragment(activity,CacheFileContentFragment.newInstance(File(param)))
    }

    //Android获取一个用于打开PDF文件的intent
    fun startPdfFileIntent(param: String) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/pdf")
        startActivity(intent)
    }
}