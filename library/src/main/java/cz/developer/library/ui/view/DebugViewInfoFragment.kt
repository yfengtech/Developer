package cz.developer.library.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.developer.library.R
import cz.developer.library.ui.view.adapter.ViewAttributeAdapter
import cz.developer.library.ui.view.model.ViewAttribute
import kotlinx.android.synthetic.main.fragment_debug_view_info.*

/**
 * Created by cz on 2017/9/7.
 */
internal class DebugViewInfoFragment:Fragment(){
    var viewAttribute:ViewAttribute?=null
    companion object {
        fun newInstance(attribute:ViewAttribute):Fragment=DebugViewInfoFragment().apply {
            viewAttribute=attribute
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_view_info, container, false)
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

        recyclerView.layoutManager=LinearLayoutManager(context)
        val items=viewAttribute?.getAttributes()
        recyclerView.adapter=ViewAttributeAdapter(context,items)
    }
}