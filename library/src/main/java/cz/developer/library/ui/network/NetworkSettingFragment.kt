package cz.developer.library.ui.network

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

import cz.developer.library.R
import cz.developer.library.prefs.DeveloperPrefs
import kotlinx.android.synthetic.main.fragment_network_setting.*

/**
 * Created by cz on 11/9/16.
 * 网络设置页
 */
class NetworkSettingFragment : Fragment() {
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
        val actionItems = arguments?.getStringArray("actionItems")
        val selectItems = arguments?.getStringArray("selectItems")
        applyButton.setOnClickListener { _ ->
            if (null != actionItems) {
                val preferenceEditor = DeveloperPrefs.preferenceEditor
                val text = editor.text
                if (!TextUtils.isEmpty(text)) {
                    //应用设置
                    for (i in actionItems.indices) {
                        preferenceEditor.putString(actionItems[i], text.toString())
                    }
                    Toast.makeText(context, R.string.changed_complete, Toast.LENGTH_SHORT).show()
                }
                preferenceEditor.commit()
                fragmentManager.popBackStack()
            }
        }
        if (null != selectItems) {
            for (i in selectItems.indices) {
                val button = RadioButton(getContext())
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

    companion object {

        fun newInstance(actionItems: Array<String>?, selectItems: Array<String?>?, serverUrl: String): Fragment {
            val fragment = NetworkSettingFragment()
            val args = Bundle()
            if (null != actionItems) {
                args.putStringArray("actionItems", actionItems)
            }
            var newItems: Array<String?>? = null
            if (null != selectItems && !TextUtils.isEmpty(serverUrl)) {
                newItems = arrayOfNulls(selectItems.size + 1)
                newItems[0] = serverUrl
                System.arraycopy(selectItems, 0, newItems, 1, selectItems.size)
            } else if (null != selectItems) {
                newItems = selectItems
            } else if (!TextUtils.isEmpty(serverUrl)) {
                newItems = arrayOf(serverUrl)
            }
            args.putStringArray("selectItems", newItems)
            fragment.arguments = args
            return fragment
        }
    }
}
