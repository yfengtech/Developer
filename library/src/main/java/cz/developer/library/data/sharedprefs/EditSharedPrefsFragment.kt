package cz.developer.library.ui.data.sp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import cz.developer.library.R
import cz.developer.library.data.model.PrefsType
import cz.developer.library.ui.data.model.SharedPrefsItem
import kotlinx.android.synthetic.main.edit_layout.*
import kotlinx.android.synthetic.main.fragment_edit_shared_prefs.*
import kotlinx.android.synthetic.main.shared_prefs_bool_item.*
import java.util.ArrayList

/**
 * Created by cz on 2017/9/11.
 */
internal class EditSharedPrefsFragment: Fragment(){
    companion object {
        fun newInstance(item: SharedPrefsItem)=EditSharedPrefsFragment().apply {
            prefsItem=item
        }
    }
    private var callback:((SharedPrefsItem)->Unit)?=null
    lateinit var prefsItem:SharedPrefsItem
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_shared_prefs,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = getString(R.string.edit_shared_prefs_value,prefsItem.name)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager.popBackStack() }
        }
        addSharedPrefsFieldItem(getLayoutInflater(savedInstanceState), prefsItem)
        applySharedPrefsChanged()
    }

    private fun addSharedPrefsFieldItem(layoutInflater: LayoutInflater, item:SharedPrefsItem) {
        when (item.type) {
            PrefsType.BOOLEAN -> boolViewStub?.inflate()
            PrefsType.SET -> {
                val valueArray = item.value as Set<String>
                valueArray.forEach {
                    val editLayout = layoutInflater.inflate(R.layout.edit_layout, layout, false) as LinearLayout
                    val titleView = editLayout.findViewById(R.id.tv_title) as TextView
                    val editor = editLayout.findViewById(R.id.et_editor) as EditText
                    titleView.text = it
                    editor.setText(it)
                    editor.setSelection(it.length)
                    layout.addView(editLayout)
                }
            }
            else -> {
                val editLayout = layoutInflater.inflate(R.layout.edit_layout, layout, false) as LinearLayout
                val titleView = editLayout.findViewById(R.id.tv_title) as TextView
                val editor = editLayout.findViewById(R.id.et_editor) as EditText
                //设置输入规则,避免数据化异常
                if (PrefsType.INTEGER.equals(item.type) || PrefsType.LONG.equals(item.type)) {
                    editor.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                } else if (PrefsType.FLOAT === item.type) {
                    editor.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                } else {
                    editor.inputType = InputType.TYPE_CLASS_TEXT
                }
                titleView.text = item.name
                editor.setText(item.value.toString())
                editor.setSelection(item.value.toString().length)
                layout.addView(editLayout)
            }
        }
    }


    private fun applySharedPrefsChanged() {
        applyButton.setOnClickListener({ _ ->
            val newItem = getChangedItem()
            if(newItem != prefsItem){
                //发生变化
                callback?.invoke(newItem)
                fragmentManager.popBackStack()
            }
        })
    }

    /**
     * 获得更变之后的配置项
     */
    private fun getChangedItem():SharedPrefsItem{
        val newItem=SharedPrefsItem(type=prefsItem.type,name = prefsItem.name)
        newItem.value=when (newItem.type) {
            PrefsType.SET -> {
                (1..layout.childCount - 1).map {
                    val childView=layout.getChildAt(it)
                    val editor = childView.findViewById(R.id.et_editor) as EditText
                    editor.text.toString()
                }.toSet()
            }
            PrefsType.BOOLEAN -> R.id.boolTrue==boolLayout.checkedRadioButtonId
            else -> {
                val editor=editLayout.findViewById(R.id.et_editor) as EditText
                editor.text
            }
        }
        return newItem
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onChangedSharedPrefs(action:(SharedPrefsItem)->Unit){
        this.callback=action
    }
}