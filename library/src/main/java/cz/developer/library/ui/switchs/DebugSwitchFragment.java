package cz.developer.library.ui.switchs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.quant.titlebar.TitleBarFragment;

import java.util.Map;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;


/**
 * Created by cz on 15/12/1.
 * debug信息
 */
public class DebugSwitchFragment extends TitleBarFragment {
    private LinearLayout container;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            title = arguments.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_switch, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleText(title);
        setOnBackClickListener(v->getFragmentManager().popBackStack());

        container= (LinearLayout) view.findViewById(R.id.ll_container);
        //添加附加选项
        ISwitchInterface switchInterface = DeveloperManager.getInstances().getSwitchInterface();
        if(null!=switchInterface){
            Map<String, String> extrasItems = switchInterface.getExtrasItems();
            if(null!=extrasItems){
                int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,12,getResources().getDisplayMetrics());
                for(Map.Entry<String,String> entry:extrasItems.entrySet()){
                    Switch switchView=new Switch(getContext());
                    switchView.setText(entry.getKey());
                    switchView.setPadding(padding,padding,padding,padding);
                    switchView.setChecked(switchInterface.itemIsChecked(entry.getValue()));
                    switchView.setBackgroundResource(R.drawable.white_item_selector);
                    switchView.setOnCheckedChangeListener((buttonView, isChecked) ->switchInterface.onSwitchItemCheckedChanged(entry.getValue(),isChecked));
                    container.addView(switchView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            }
        }
    }
}
