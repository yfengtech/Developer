package cz.developer.library.ui.network
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast

import cz.developer.library.R
import kotlinx.android.synthetic.main.fragment_network_setting.*

/**
 * Created by cz on 11/9/16.
 * 网络设置页
 */
internal class NetworkSettingFragment : Fragment() {

    companion object {
        fun newInstance(selectItems: Array<String>?): Fragment {
            val fragment = NetworkSettingFragment()
            val args = Bundle()
            if (null != selectItems) {
                args.putStringArray("selectItems", selectItems)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network_setting, container, false)
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
        val selectItems = arguments?.getStringArray("selectItems")
        applyButton.setOnClickListener { _ ->
            if (null != selectItems) {
                val text = editor.text
                if (TextUtils.isEmpty(text)) {
                    Snackbar.make(settingContainer,"修改失败,配置服务器地址不能空!",Snackbar.LENGTH_SHORT).show()
                } else {
                    //应用设置
                    Toast.makeText(context, R.string.changed_complete, Toast.LENGTH_SHORT).show()
                    fragmentManager.popBackStack()
                }
            }
        }
        if (null != selectItems) {
            for (i in selectItems.indices) {
                val button = RadioButton(context)
                button.text = selectItems[i]
                if (0 == i) {
                    button.setTextColor(Color.GREEN)
                    editor.setText(selectItems[i])
                    editor.setSelection(selectItems[i].length)
                }
                radioLayout.addView(button)
            }
            radioLayout.setOnCheckedChangeListener { radioGroup, id ->
                val i = radioGroup.indexOfChild(radioGroup.findViewById(id))
                editor.setText(selectItems[i])
                editor.setSelection(selectItems[i].length)
            }
        }
    }
}
