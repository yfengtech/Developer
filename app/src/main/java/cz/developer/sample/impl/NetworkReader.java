package cz.developer.sample.impl;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.NetItem;
import xyqb.library.XmlAttribute;
import xyqb.library.XmlElement;
import xyqb.library.config.Config;
import xyqb.library.config.XmlReaderBase;

/**
 * Created by cz on 11/8/16.
 */
@Config("net_config.xml")
public class NetworkReader extends XmlReaderBase<List<NetItem>> {
    @Override
    public List<NetItem> readXmlConfig(XmlElement rootElement) {
        List<NetItem> requestItems=new ArrayList<>();
        if(null!=rootElement){
            ArrayList<XmlElement> children = rootElement.getChildren();
            for(XmlElement element:children){
                NetItem item=new NetItem();
                ArrayList<XmlAttribute> attributes = element.getAttributes();
                for(int i=0;i<attributes.size();i++){
                    XmlAttribute xmlAttribute = attributes.get(i);
                    if("action".equals(xmlAttribute.name)){
                        item.action=xmlAttribute.value;
                    } else if("info".equals(xmlAttribute.name)){
                        item.info=xmlAttribute.value;
                    } else if("url".equals(xmlAttribute.name)){
                        item.url=xmlAttribute.value;
                    }
                }
                requestItems.add(item);
            }
        }
        return requestItems;
    }
}
