package cz.developer.library.ui.filesystem

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.developer.library.R
import kotlinx.android.synthetic.main.fragment_cache_file_system.*
import java.io.File

/**
 * Created by cz on 2017/10/19.
 */
internal class CacheFileSystemFragment: Fragment(){
    companion object {
        fun newInstance(file:File):Fragment{
            return CacheFileSystemFragment().apply {
                arguments=Bundle().apply { putSerializable("file",file) }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cache_file_system,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        val file=arguments.getSerializable("file") as File
        childFragmentManager.
                beginTransaction().
                addToBackStack(null).
                add(R.id.fileFragmentContainer, CacheFileListFragment.newInstance(file)).commit()

    }
}