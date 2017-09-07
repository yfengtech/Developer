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
import kotlinx.android.synthetic.main.fragment_debug_view.*

/**
 * Created by cz on 1/11/17.
 */

class DebugViewFragment : Fragment() {
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
        switchView.setOnCheckedChangeListener { _, isChecked ->
            DeveloperPrefs.setBoolean(Constants.DEBUG_LIST, isChecked)
            //设置所有控件状态
            DeveloperActivityManager.forEach{ activity ->
                val decorView = activity.window.decorView
                if (null != decorView) {
                    val contentView = decorView.findViewById(android.R.id.content)
                    if (null != contentView && contentView is ViewGroup) {

                    }
                }
            }
        }
        //AbsListView/RecyclerView/ImageView
        listView.adapter = DebugViewAdapter(context, items)
        listView.setOnItemClickListener { _, _, position, _ -> adapter!!.selectItem(position) }
    }


    companion object {
        class Item(var title:String?=null,var desc:String?=null)
        val items= mutableListOf<Item>()
        fun item(action:Item.()->Unit){
            items.add(Item().apply(action))
        }

        init {
            item{
                title="View常规信息"
                desc="包括信息类型,可见所有属性等"
            }
            item{
                title="AbsListView"
                desc="长按查看Item所有信息"
            }
            item{
                title="RecyclerView"
                desc="长按查看Item所有信息"
            }
            item{
                title="ImageView"
                desc="查看Tag以及ScaleType,和一些其他信息"
            }
            item{
                title="WebView"
                desc="查看一些其他信息"
            }
            item{
                title="View"
                desc="查看Tab以及其他信息"
            }
        }
    }

}
