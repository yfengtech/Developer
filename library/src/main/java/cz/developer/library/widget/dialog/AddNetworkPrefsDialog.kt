package cz.developer.library.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import cz.developer.library.DeveloperManager

import cz.developer.library.R
import cz.developer.library.prefs.DeveloperPrefs


/**
 * 带编辑框的弹出对话框
 * @author cz
 * *
 * @date 2015/8/3
 */
internal class AddNetworkPrefsDialog : DialogFragment() {
    private var listener: OnSubmitListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.changed_net_url)
        builder.setMessage(R.string.add_server_url_message)
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.dialog_add_net_edit, null)
        builder.setView(view)
        val editText = view.findViewById(R.id.et_editor) as EditText
        val text="http://"
        editText.setText(text)
        editText.setSelection(text.length)
        editText.post {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        builder.setPositiveButton(R.string.add_net_url) { _: DialogInterface, _: Int ->
            if (null != listener) {
                var text = editText.text.toString()
                // /结尾
                if(!text.endsWith("/")){
                    text+="/"
                }
                if (TextUtils.isEmpty(text) || !"https?://[^/]+/".toRegex().matches(text)) {
                    Toast.makeText(context,"输入服务器地址不合法",Toast.LENGTH_SHORT).show()
                } else {
                    listener?.onSubmit(text.trim { it <= ' ' })
                }
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        return builder.create()
    }

    /**
     * 设置编辑提交监听

     * @param listener
     */
    fun setOnSubmitListener(listener: OnSubmitListener): AddNetworkPrefsDialog {
        this.listener = listener
        return this
    }


    /**
     * 提交文字监听
     */
    interface OnSubmitListener {
        fun onSubmit(edit: String)
    }

}
