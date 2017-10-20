package cz.developer.library.ui.switchs

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch

import cz.developer.library.DeveloperManager
import cz.developer.library.R
import kotlinx.android.synthetic.main.fragment_debug_switch.*


/**
 * Created by cz on 15/12/1.
 * debug信息
 */
internal class DebugSwitchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_switch, container, false)
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
        //添加附加选项
        val switchInterface = DeveloperManager.developerConfig.switchItem
        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
        switchInterface?.items?.forEach {
            val switchView = Switch(context)
            val key=it.key
            switchView.text = it.desc
            switchView.setTextColor(Color.BLACK)
            switchView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
            switchView.setPadding(padding, padding, padding, padding)
            switchView.setBackgroundResource(R.drawable.developer_white_item_selector)
            if(null!=key){
                switchView.setOnCheckedChangeListener { _, isChecked ->
                    switchInterface.itemChecked?.invoke(key,isChecked)
                }
            }
            menuContainer.addView(switchView, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        }
    }
}
