package cz.developer.library.prefs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by cz on 16/3/15.
 */
public class DeveloperPrefs {
    // 配置项名称
    private static final String DEFAULT_PREFERENCE = "developer_config";
    private static final String DEFAULT_KEY = "config";
    private static final int DEFAULT_INT_VALUE = -1;
    private static Handler handler;
    private static Context appContext;

    static {
        handler=new Handler(Looper.getMainLooper());
    }

    public static SharedPreferences getSharedPreferences() {
        Context appContext = null;
        //重复检测,直接到post任务执行完毕
        while(null==appContext){
            appContext=getAppContext();
        }
        return appContext.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static Context getAppContext() {
        if (appContext == null) {
            //部分5.0以下手机,在子线程内,利用反射取Context,会报空.所以这里如果是子线程,且小于5.0,直接post到子线程执行,外围通过while(判断是否取到数据
            if(Looper.getMainLooper()==Looper.myLooper()|| Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                try {
                    Application application=(Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
                    appContext = application.getApplicationContext();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        appContext=getAppContext();
                    }
                });
            }
        }
        return appContext;
    }

    public static SharedPreferences.Editor getPreferenceEditor() {
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
