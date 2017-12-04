package cz.developer.library.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.developer.library.R
import cz.developer.library.ui.view.model.ViewHierarchyItem
import cz.developer.library.view.adapter.DeveloperFragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_debug_view_hierarchy.*

/**
 * Created by cz on 2017/9/7.
 */
internal class DebugViewHierarchyFragment :Fragment(){
    var hierarchyItem: ViewHierarchyItem?=null
    companion object {
        fun newInstance(item: ViewHierarchyItem?):Fragment= DebugViewHierarchyFragment().apply {
            hierarchyItem=item
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_view_hierarchy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.setTitle(R.string.view_extras)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        viewPager.adapter= DeveloperFragmentPagerAdapter(childFragmentManager,
                arrayOf(HierarchyPage1Fragment.newInstance(hierarchyItem),HierarchyPage2Fragment.newInstance(hierarchyItem)))
    }
}