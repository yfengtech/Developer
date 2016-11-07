package cz.developer.sample.impl;

import java.util.HashMap;
import java.util.Map;

import cz.developer.library.ui.switchs.ISwitchInterface;

/**
 * Created by Administrator on 2016/11/7.
 */

public class SwitchImpl implements ISwitchInterface {
    @Override
    public Map<String, String> getExtrasItems() {
        HashMap<String,String> items=new HashMap<>();
        items.put("网络调试","net");
        items.put("异常调试","error1");
        items.put("崩溃调试","error2");
        return items;
    }

    @Override
    public boolean itemIsChecked(String key) {
        return true;
    }

    @Override
    public void onSwitchItemCheckedChanged(String key, boolean isChecked) {

    }
}
