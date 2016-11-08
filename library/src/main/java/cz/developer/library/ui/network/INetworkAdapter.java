package cz.developer.library.ui.network;

import java.util.List;

import cz.developer.library.model.NetItem;

/**
 * Created by cz on 11/8/16.
 */

public interface INetworkAdapter {
    //获取请求url
    String getServerUrl();
    //获取备选url集
    List<String> getSelectUrl();
    //获取当前使用接口信息集
    List<NetItem> getNetworkItems();
}
