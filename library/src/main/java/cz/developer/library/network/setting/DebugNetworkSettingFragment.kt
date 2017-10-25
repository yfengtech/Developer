package cz.developer.okhttp3.ui
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import cz.developer.library.DeveloperManager

import cz.developer.library.R
import cz.developer.library.prefs.DeveloperPrefs
import kotlinx.android.synthetic.main.fragment_network_setting.*

/**
 * Created by cz on 11/9/16.
 * 网络设置页
 */
internal class DebugNetworkSettingFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network_setting, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = arguments?.getString("title")
            toolBar.subtitle=arguments?.getString("desc")
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        val networkAdapter = DeveloperManager.developerConfig.network
        if(null==networkAdapter){
            Snackbar.make(settingContainer,"未配置网络数据适配器",Snackbar.LENGTH_SHORT).show()
        } else {
            val serverUrl=DeveloperPrefs.url
            val selectItems=networkAdapter.serverUrl
            if (null != selectItems) {
                for (i in selectItems.indices) {
                    val button = RadioButton(context)
                    button.id=i
                    button.text = selectItems[i]
                    if (0 == i) {
                        button.setTextColor(Color.GREEN)
                        editor.setText(selectItems[i])
                        editor.setSelection(selectItems[i].length)
                    }
                    radioLayout.addView(button)
                    //选中己配置的
                    if(serverUrl==selectItems[i]){
                        radioLayout.check(i)
                    }
                }
                radioLayout.setOnCheckedChangeListener { radioGroup, id ->
                    val i = radioGroup.indexOfChild(radioGroup.findViewById(id))
                    editor.setText(selectItems[i])
                    editor.setSelection(selectItems[i].length)
                }
            }
            applyButton.setOnClickListener { _ ->
                if (null != selectItems) {
                    val text = editor.text
                    if (TextUtils.isEmpty(text)) {
                        Snackbar.make(settingContainer,"修改失败,配置服务器地址不能空!",Snackbar.LENGTH_SHORT).show()
                    } else {
                        //应用设置
                        DeveloperPrefs.url=text.toString()
                        Toast.makeText(context, R.string.changed_complete, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_network_setting,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.ab_setting){
            DeveloperManager.toDeveloperFragment(activity, DebugNetworkFragment.newInstance(arguments))
        }
        return super.onOptionsItemSelected(item)
    }
}
