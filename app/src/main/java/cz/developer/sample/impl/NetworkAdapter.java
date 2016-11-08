package cz.developer.sample.impl;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.NetItem;
import cz.developer.library.ui.network.INetworkAdapter;
import xyqb.library.config.PrefsManager;

/**
 * Created by cz on 11/8/16.
 */

public class NetworkAdapter implements INetworkAdapter {
    @Override
    public String getServerUrl() {
        return "https://www.baidu.com";
    }

    @Override
    public List<String> getSelectUrl() {
        List<String> items=new ArrayList<>();
        items.add("https://www.hao123.com/");
        items.add("http://www.sina.com.cn/");
        items.add("http://www.jd.com/");
        return items;
    }

    @Override
    public List<NetItem> getNetworkItems() {
        return PrefsManager.readConfig(NetworkReader.class);
    }
}
