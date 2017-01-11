package cz.developer.library;

import cz.developer.library.prefs.DeveloperPrefs;

/**
 * Created by cz on 1/11/17.
 */

public class PrefsConfig {
    public boolean debugList;

    public PrefsConfig() {
        debugList=DeveloperPrefs.getBoolean(Constants.DEBUG_LIST);
    }
}
