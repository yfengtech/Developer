package cz.developer.library.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import cz.developer.library.Constants
import cz.developer.library.DeveloperActivityManager
import cz.developer.library.R
import cz.developer.library.ui.view.adapter.DebugViewAdapter
import cz.developer.library.prefs.DeveloperPrefs
import cz.developer.library.setViewDebug
import kotlinx.android.synthetic.main.fragment_debug_view.*

/**
 * Created by cz on 1/11/17.
 */

internal class DebugViewFragment : Fragment() {
    private var adapter: DebugViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            toolBar.subtitle=arguments?.getString("desc")
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        switchView.isChecked=DeveloperPrefs.debugList
        switchView.setOnCheckedChangeListener { _, isChecked ->
            DeveloperPrefs.debugList=isChecked
            //设置所有控件状态
            DeveloperActivityManager.forEach{ it.setViewDebug(isChecked) }
        }
        //AbsListView/RecyclerView/ImageView
        listView.adapter = DebugViewAdapter(context, items)
    }


    companion object {
        class Item(var title:String?=null,var desc:String?=null)
        val items= mutableListOf<Item>()
        fun item(action:Item.()->Unit){
            items.add(Item().apply(action))
        }

        init {
            item {
                title = "控件视图"
                desc = "控件所处层级,以及基本视图,可见所有属性等"
            }
            item {
                title = "控件属性集"
                desc = "控件常规属性信息"
            }
            item {
                title = "控件Tag信息"
                desc = "可用于查看一些关键信息"
            }
            item {
                title = "其他"
                desc = "未来考虑支持WebView,以及其他定制信息"
            }
        }
    }

}
