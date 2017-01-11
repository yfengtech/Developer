package cz.developer.library.xml;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.DebugViewItem;
import xyqb.library.XmlElement;
import xyqb.library.config.Config;
import xyqb.library.config.XmlReaderBase;


/**
 * Created by cz on 9/20/16.
 * app_config.xml解析
 */
@Config("config/view_config.xml")
public class ViewConfigReader extends XmlReaderBase<List<DebugViewItem>> {

    @Override
    public List<DebugViewItem> readXmlConfig(XmlElement rootElement) {
        List<DebugViewItem> items=new ArrayList<>();
        ArrayList<XmlElement> children = rootElement.getChildren();
        for(int i=0;i<children.size();i++){
            XmlElement child = children.get(i);
            DebugViewItem item=new DebugViewItem();
            item.viewName=child.getAttributeValue("view");
            item.viewInfo=child.getAttributeValue("info");
            String clazzName = child.getAttributeValue("clazz");
            if(!TextUtils.isEmpty(clazzName)){
                try {
                    item.clazz = Class.forName(clazzName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            items.add(item);
        }
        return items;
    }
}
