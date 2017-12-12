package cz.developer.library.ui.filesystem

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.os.EnvironmentCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.filesystem.adapter.CacheFileAdapter
import cz.developer.library.ui.filesystem.model.FileItem
import kotlinx.android.synthetic.main.fragment_cache_file_data.*
import java.io.File

/**
 * Created by cz on 2017/10/19.
 */
internal class CacheFileDataFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cache_file_data,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fileItems= mutableListOf<FileItem>()
        fileItems.add(FileItem(File(Environment.getDataDirectory(),"/data/${context.packageName}/"),"Data目录","/data/data/app_package_name目录"))
        fileItems.add(FileItem(context.cacheDir,"内存缓存","/data/data/app_package_name/cache目录"))
        fileItems.add(FileItem(context.filesDir,"内存文件缓存","/data/data/app_package_name/files目录,"))
        fileItems.add(FileItem(context.externalCacheDir,"外部内存卡缓存目录","需要外部读写权限"))
        fileItems.add(FileItem(context.obbDir,"应用程序的OBB文件目录","应用程序的OBB文件目录,如果没有任何OBB文件，这个目录是不存在的"))
        recyclerView.layoutManager=LinearLayoutManager(context)
        val adapter=CacheFileAdapter(context,fileItems)
        recyclerView.adapter=adapter

        recyclerView.onItemClick { _, _, position ->
            val item=adapter.getItem(position)
            if(0<(item.file.listFiles()?.size?:0)){
                activity.supportFragmentManager.
                        beginTransaction().
                        addToBackStack(null).
                        add(R.id.fileFragmentContainer, CacheFileListFragment.newInstance(item.file)).commit()
            } else {
                Snackbar.make(recyclerView,R.string.no_file_found, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}