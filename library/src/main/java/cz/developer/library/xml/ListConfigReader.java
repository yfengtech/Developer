package cz.developer.library.xml;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.DebugItem;
import xyqb.library.XmlElement;
import xyqb.library.config.Config;
import xyqb.library.config.XmlReaderBase;


/**
 * Created by cz on 9/20/16.
 * app_config.xml解析
 */
@Config("config/app_config.xml")
public class ListConfigReader extends XmlReaderBase<List<DebugItem>> {

    @Override
    public List<DebugItem> readXmlConfig(XmlElement rootElement) {
        List<DebugItem> items=new ArrayList<>();
        ArrayList<XmlElement> children = rootElement.getChildren();
        for(int i=0;i<children.size();i++){
            XmlElement child = children.get(i);
            DebugItem item=new DebugItem();
            item.title=child.getAttributeValue("title");
            item.subInfo=child.getAttributeValue("info");
            item.clazz=child.getAttributeValue("clazz");
            items.add(item);
        }
        return items;
    }
}
