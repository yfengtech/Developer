package cz.developer.library.model;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewItem {
    public String viewName;
    public String viewInfo;
    public Class<?> clazz;

    public DebugViewItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        boolean result=false;
        if (o != null&& o instanceof DebugViewItem){
            DebugViewItem that = (DebugViewItem) o;
            result= equals(viewName, that.viewName);
        }
        return result;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
