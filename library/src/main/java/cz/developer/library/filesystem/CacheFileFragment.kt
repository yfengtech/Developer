package cz.developer.library.ui.filesystem

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_cache_file.*
import java.io.File

/**
 * Created by cz on 2017/10/19.
 */
internal class CacheFileFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cache_file,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity?:return
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager?.popBackStack() }
        }
        activity.supportFragmentManager.
                beginTransaction().
                add(R.id.fileFragmentContainer, CacheFileDataFragment()).commit()
    }
}