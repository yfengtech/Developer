package cz.developer.library.ui.switchs;

import android.widget.Switch;

import java.util.List;
import java.util.Map;

/**
 * Created by czz on 2016/11/7.
 *
 */
public interface ISwitchInterface {
    //获取附加集
    Map<String,String> getExtrasItems();
    //条目是否选中
    boolean itemIsChecked(String key);
    //选中变化
    void onSwitchItemCheckedChanged(String key,boolean isChecked);
}
