package cz.developer.library.ui.hierarchy

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.developer.library.R
import cz.developer.library.closeDeveloperLayout
import cz.developer.library.openDeveloperLayout
import cz.developer.library.widget.hierarchy.HierarchyNode
import cz.developer.library.widget.hierarchy.adapter.SimpleHierarchyAdapter
import kotlinx.android.synthetic.main.fragment_hierarchy_view.*

/**
 * Created by cz on 2017/9/7.
 */
internal class HierarchyFragment: Fragment() {
    private lateinit var node: HierarchyNode
    companion object {
        fun newInstance(node: HierarchyNode):Fragment{
            val fragment= HierarchyFragment()
            fragment.node=node
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hierarchy_view,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity?:return
        if(activity is AppCompatActivity){
            toolBar.title = getString(R.string.activity_hierarchy)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager?.popBackStack() }
        }
        hierarchyView.setAdapter(SimpleHierarchyAdapter(activity,node))
    }

    override fun onAttach(context: Context?) {
        closeDeveloperLayout()//隐藏调试视图
        super.onAttach(context)
    }

    override fun onDetach() {
        openDeveloperLayout()//打开调试视图
        super.onDetach()
    }

}