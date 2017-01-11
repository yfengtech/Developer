package cz.developer.library.ui.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.quant.titlebar.TitleBarFragment;

import cz.developer.library.R;
import cz.developer.library.prefs.DeveloperPrefs;

/**
 * Created by cz on 11/9/16.
 * 网络设置页
 */
public class NetworkSettingFragment extends TitleBarFragment {
    private RadioGroup selectLayout;
    private EditText editor;
    private String[] actionItems;
    private String[] selectItems;

    public static Fragment newInstance(String[] actionItems,String[] selectItems,String serverUrl){
        Fragment fragment=new NetworkSettingFragment();
        Bundle args=new Bundle();
        if(null!=actionItems){
            args.putStringArray("actionItems",actionItems);
        }
        String[] newItems=null;
        if(null!=selectItems&&!TextUtils.isEmpty(serverUrl)){
            newItems=new String[selectItems.length+1];
            newItems[0]=serverUrl;
            System.arraycopy(selectItems,0,newItems,1,selectItems.length);
        } else if(null!=selectItems){
            newItems= selectItems;
        } else if(!TextUtils.isEmpty(serverUrl)){
            newItems=new String[]{serverUrl};
        }
        args.putStringArray("selectItems",newItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getArguments()){
            actionItems=getArguments().getStringArray("actionItems");
            selectItems=getArguments().getStringArray("selectItems");
        }
    }

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_network_setting,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectLayout= (RadioGroup) view.findViewById(R.id.rg_layout);
        editor= (EditText) view.findViewById(R.id.et_editor);
        view.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            if(null!=actionItems){
                SharedPreferences.Editor preferenceEditor = DeveloperPrefs.getPreferenceEditor();
                Editable text = editor.getText();
                if(!TextUtils.isEmpty(text)){
                    //应用设置
                    for(int i=0;i<actionItems.length;i++){
                        preferenceEditor.putString(actionItems[i],text.toString());
                    }
                    Toast.makeText(getContext(), R.string.changed_complete, Toast.LENGTH_SHORT).show();
                }
                preferenceEditor.commit();
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.changed_net_url);
        setOnBackClickListener(v->getFragmentManager().popBackStack());
        if(null!=selectItems){
            for(int i=0;i<selectItems.length;i++){
                RadioButton button=new RadioButton(getContext());
                button.setText(selectItems[i]);
                if(0==i){
                    button.setTextColor(Color.GREEN);
                    editor.setText(selectItems[i]);
                    editor.setSelection(selectItems[i].length());
                }
                selectLayout.addView(button);
            }
        }
        selectLayout.setOnCheckedChangeListener((radioGroup, id) -> {
            int i = radioGroup.indexOfChild(radioGroup.findViewById(id));
            editor.setText(selectItems[i]);
            editor.setSelection(selectItems[i].length());
        });
    }
}
