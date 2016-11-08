package cz.developer.library.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import cz.developer.library.R;


/**
 * 带编辑框的弹出对话框
 *
 * @author cz
 * @date 2015/8/3
 */
public class EditDialog extends DialogFragment {
    private OnSubmitListener listener;
    private ArrayList<String> items;
    private int selectIndex;
    private String url;
    /**
     * 获得一个编辑对话框对像
     *
     * @return
     */
    public static EditDialog newInstance(ArrayList<String> items, String url) {
        EditDialog editDialog = new EditDialog();
        Bundle args=new Bundle();
        args.putStringArrayList("items",items);
        args.putString("url",url);
        editDialog.setArguments(args);
        return editDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getArguments()){
            selectIndex=-1;
            items = getArguments().getStringArrayList("items");
            url=getArguments().getString("url");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.changed_net_url);
        builder.setMessage(R.string.server_url_message);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_net_edit, null);
        builder.setView(view);
        final RadioGroup layout = (RadioGroup) view.findViewById(R.id.rg_layout);
        if(null!=items){
            for(int i=0;i<items.size();i++){
                RadioButton button=new RadioButton(getContext());
                button.setText(items.get(i));
                layout.addView(button);
            }
        }
        layout.setOnCheckedChangeListener((radioGroup, id) ->
                selectIndex = layout.indexOfChild(layout.findViewById(id)));
        final EditText editText = (EditText) view.findViewById(R.id.et_editor);
        editText.setHint(R.string.input_url_hint);
        if(!TextUtils.isEmpty(url)){
            editText.setText(url);
            editText.setSelection(url.length());
        }
        editText.post(() -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
        builder.setPositiveButton(R.string.changed, (DialogInterface dialog, int which) -> {
            if (null != listener) {
                Editable text = editText.getText();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.toString().trim())&&!text.toString().equals(url)) {
                    listener.onSubmit(text.toString().trim());
                }else if(-1!=selectIndex){
                    listener.onSubmit(items.get(selectIndex));
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (DialogInterface dialog, int which) -> dialog.dismiss());
        return builder.create();
    }

    /**
     * 设置编辑提交监听
     *
     * @param listener
     */
    public EditDialog setOnSubmitListener(OnSubmitListener listener) {
        this.listener = listener;
        return this;
    }


    /**
     * 提交文字监听
     */
    public interface OnSubmitListener {
        void onSubmit(String edit);
    }
}
