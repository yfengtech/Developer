package cz.developer.library.ui.operation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.io.IOException

import cz.developer.library.R
import kotlinx.android.synthetic.main.fragment_debug_other.*

/**
 * Created by cz on 15/11/30.
 * debug其他设置界面
 */
class DebugOperationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_other, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearCache.setOnClickListener { _ ->
            val appPackage = context.packageName
            val cmd = "pm clear $appPackage HERE"
            val runtime = Runtime.getRuntime()
            try {
                runtime.exec(cmd)
            } catch (e: IOException) {
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            toolBar.subtitle=arguments?.getString("desc")
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
    }
}
