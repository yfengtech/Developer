package cz.developer.library.network.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onExpandItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.log.FilePrefs
import cz.developer.library.network.adapter.NetworkRequestFileAdapter
import kotlinx.android.synthetic.main.fragment_debug_request_list.*
import java.io.File

/**
 * Created by cz on 2017/10/24.
 */
internal class DebugRequestListFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_request_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity?:return
        if (activity is AppCompatActivity) {
            toolBar.title = arguments?.getString("title")
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener { fragmentManager?.popBackStack() }
        }

        recyclerView.layoutManager=LinearLayoutManager(context)
        val items=LinkedHashMap<File,List<File>>()
        val networkFolder=FilePrefs.networkFolder
        networkFolder.listFiles()?.reversed()?.forEach {
            items.put(it,it.listFiles().toList())
        }
        val adapter= NetworkRequestFileAdapter(activity,items,true)
        recyclerView.adapter=adapter
        recyclerView.onExpandItemClick { _, group, position ->
            val item=adapter.getChild(group,position)
            DeveloperManager.toDeveloperFragment(activity,DebugRequestContentFragment.newInstance(item))
        }
    }
}