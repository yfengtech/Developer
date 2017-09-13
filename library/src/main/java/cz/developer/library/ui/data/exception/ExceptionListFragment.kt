package cz.developer.library.ui.data.exception

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cz.recyclerlibrary.onItemClick
import cz.developer.library.DeveloperManager
import cz.developer.library.R
import cz.developer.library.ui.data.adapter.ExceptionAdapter
import cz.developer.library.ui.data.model.ExceptionItem
import kotlinx.android.synthetic.main.fragment_exception_list.*
import java.io.File

/**
 * Created by cz on 2017/9/13.
 */
class ExceptionListFragment:Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exception_list,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.setTitle(R.string.exception_list)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }

        val dir= File(context.cacheDir,"error")
        val items= mutableListOf<ExceptionItem>()
        dir.listFiles().forEach {
            var index=0
            val item=ExceptionItem()
            item.lastModified=it.lastModified()
            items.add(item)
            it.forEachLine {
                when(index){
                    0->item.name=it
                    1->item.threadName=it
                    2->item.className=it
                    3->item.methodName
                    else->item.desc.append("$it\n")
                }
                index++
            }
        }
        recyclerView.layoutManager=LinearLayoutManager(context)
        val adapter=ExceptionAdapter(context,items)
        recyclerView.adapter=adapter
        recyclerView.onItemClick { _, _, adapterPosition ->
            DeveloperManager.toDeveloperFragment(activity,ExceptionContentFragment.newInstance(adapter[adapterPosition]))
        }
    }
}