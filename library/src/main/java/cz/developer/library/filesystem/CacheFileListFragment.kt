package cz.developer.library.ui.filesystem

import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.R
import cz.developer.library.ui.filesystem.adapter.CacheFileAdapter
import cz.developer.library.ui.filesystem.adapter.FileSystemAdapter
import cz.developer.library.ui.filesystem.model.FileItem
import kotlinx.android.synthetic.main.fragment_cache_file_list.*
import java.io.File
import android.content.Intent
import android.net.Uri


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
        val adapter=FileSystemAdapter(context,file.listFiles()?.toList())
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
        startActivity(when(end){
            in arrayOf("m4a","mp3","mid","xmf","ogg","wav")->getAudioFileIntent(filePath)
            "3gp","mp4"->getAudioFileIntent(filePath)
            "jpg","gif","png","jpeg", "bmp"->getImageFileIntent(filePath)
            "apk"-> getApkFileIntent(filePath)
            "ppt"->getPptFileIntent(filePath)
            "xls"->getExcelFileIntent(filePath)
            "doc"->getWordFileIntent(filePath)
            "pdf"->getPdfFileIntent(filePath)
            "chm"->getChmFileIntent(filePath)
            "txt"->getTextFileIntent(filePath, false)
            else->getAllIntent(filePath)
        })
    }

    //Android获取一个用于打开APK文件的intent
    fun getAllIntent(param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = android.content.Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "*/*")
        return intent
    }

    //Android获取一个用于打开APK文件的intent
    fun getApkFileIntent(param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = android.content.Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        return intent
    }

    //Android获取一个用于打开VIDEO文件的intent
    fun getVideoFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "video/*")
        return intent
    }

    //Android获取一个用于打开AUDIO文件的intent
    fun getAudioFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "audio/*")
        return intent
    }

    //Android获取一个用于打开Html文件的intent
    fun getHtmlFileIntent(param: String): Intent {

        val uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, "text/html")
        return intent
    }

    //Android获取一个用于打开图片文件的intent
    fun getImageFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "image/*")
        return intent
    }

    //Android获取一个用于打开PPT文件的intent
    fun getPptFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        return intent
    }

    //Android获取一个用于打开Excel文件的intent
    fun getExcelFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        return intent
    }

    //Android获取一个用于打开Word文件的intent
    fun getWordFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/msword")
        return intent
    }

    //Android获取一个用于打开CHM文件的intent
    fun getChmFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/x-chm")
        return intent
    }

    //Android获取一个用于打开文本文件的intent
    fun getTextFileIntent(param: String, paramBoolean: Boolean): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (paramBoolean) {
            val uri1 = Uri.parse(param)
            intent.setDataAndType(uri1, "text/plain")
        } else {
            val uri2 = Uri.fromFile(File(param))
            intent.setDataAndType(uri2, "text/plain")
        }
        return intent
    }

    //Android获取一个用于打开PDF文件的intent
    fun getPdfFileIntent(param: String): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/pdf")
        return intent
    }
}