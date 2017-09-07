package cz.developer.library.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup

import java.util.ArrayList

import cz.developer.library.R


/**
 * 带编辑框的弹出对话框
 * @author cz
 * *
 * @date 2015/8/3
 */
class EditDialog : DialogFragment() {
    private var listener: OnSubmitListener? = null
    private var items: ArrayList<String>? = null
    private var selectIndex: Int = 0
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null != arguments) {
            selectIndex = -1
            items = arguments.getStringArrayList("items")
            url = arguments.getString("url")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.changed_net_url)
        builder.setMessage(R.string.server_url_message)
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.dialog_net_edit, null)
        builder.setView(view)
        val layout = view.findViewById(R.id.rg_layout) as RadioGroup
        if (null != items) {
            for (i in items!!.indices) {
                val button = RadioButton(context)
                button.text = items!![i]
                layout.addView(button)
            }
        }
        layout.setOnCheckedChangeListener { radioGroup, id -> selectIndex = layout.indexOfChild(layout.findViewById(id)) }
        val editText = view.findViewById(R.id.et_editor) as EditText
        editText.setHint(R.string.input_url_hint)
        if (!TextUtils.isEmpty(url)) {
            editText.setText(url)
            editText.setSelection(url!!.length)
        }
        editText.post {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        builder.setPositiveButton(R.string.changed) { dialog: DialogInterface, which: Int ->
            if (null != listener) {
                val text = editText.text
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.toString().trim { it <= ' ' }) && text.toString() != url) {
                    listener!!.onSubmit(text.toString().trim { it <= ' ' })
                } else if (-1 != selectIndex) {
                    listener!!.onSubmit(items!![selectIndex])
                }
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        return builder.create()
    }

    /**
     * 设置编辑提交监听

     * @param listener
     */
    fun setOnSubmitListener(listener: OnSubmitListener): EditDialog {
        this.listener = listener
        return this
    }


    /**
     * 提交文字监听
     */
    interface OnSubmitListener {
        fun onSubmit(edit: String)
    }

    companion object {
        /**
         * 获得一个编辑对话框对像
         * @return
         */
        fun newInstance(items: ArrayList<String>, url: String): EditDialog {
            val editDialog = EditDialog()
            val args = Bundle()
            args.putStringArrayList("items", items)
            args.putString("url", url)
            editDialog.arguments = args
            return editDialog
        }
    }
}
