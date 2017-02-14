package cz.developer.sample;

import android.view.View;

import cz.developer.library.DebugViewHelper;
import cz.developer.library.DeveloperManager;

/**
 * Created by czz on 2017/2/14.
 */

public class DeveloperWrapper {

    public static void setViewTag(View view, Object tag){
        DebugViewHelper.setViewTag(view,tag);
    }
}
