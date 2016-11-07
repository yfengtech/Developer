package cz.developer.library.prefs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cz.library.util.Utils;

import java.lang.reflect.Method;

/**
 * Created by cz on 16/3/15.
 */
public class DeveloperPrefs {
    // 配置项名称
    private static final String DEFAULT_PREFERENCE = "config";
    private static final String DEFAULT_KEY = "config";
    private static final int DEFAULT_INT_VALUE = -1;
    private static Context appContext;

    static {
        appContext = getAppContext();
    }

    private static SharedPreferences getSharedPreferences() {
        Context appContext = getAppContext();
        return appContext.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static Context getAppContext() {
        if (appContext == null) {
            try {
                final Class<?> activityThreadClass = Utils.class.getClassLoader().loadClass("android.app.ActivityThread");
                final Method currentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread");
                final Object activityThread = currentActivityThread.invoke(null);
                final Method getApplication = activityThreadClass.getDeclaredMethod("getApplication");
                final Application application = (Application) getApplication.invoke(activityThread);
                appContext = application.getApplicationContext();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return appContext;
    }

    private static SharedPreferences.Editor getPreferenceEditor() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.edit();
    }


    public static void remove(String key) {
        SharedPreferences.Editor editor = getPreferenceEditor();
        editor.remove(String.valueOf(key));
        editor.commit();
    }


    public static String getDefaultValue() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(DEFAULT_KEY, null);
    }


    /**
     * 根据角标获取 默认值 -1;
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(key, DEFAULT_INT_VALUE);
    }

    public static int getInt(String key, int defaultValue) {
        int value = getInt(key);
        return DEFAULT_INT_VALUE == value ? defaultValue : value;
    }


    /**
     * 根据角标获取 默认值 -1;
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getLong(key, DEFAULT_INT_VALUE);
    }

    public static float getFloat(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getFloat(key, DEFAULT_INT_VALUE);
    }


    /**
     * 获得boolean值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return 1 == getInt(key);
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = getPreferenceEditor();
        editor.putInt(key, value).commit();
    }

    public static void setFloat(String key, float value) {
        SharedPreferences.Editor editor = getPreferenceEditor();
        editor.putFloat(key, value).commit();
    }

    public static void setLong(String key, long value) {
        SharedPreferences.Editor editor = getPreferenceEditor();
        editor.putLong(key, value).commit();
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = getPreferenceEditor();
        editor.putString(key, value).commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, null);
    }


    public static void setBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = getPreferenceEditor();
        editor.putInt(key, value ? 1 : 0).commit();
    }
}
