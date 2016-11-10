package cz.developer.library.ui.appinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quant.titlebar.TitleBarFragment;

import cz.developer.library.DeveloperConfig;
import cz.developer.library.DeveloperManager;
import cz.developer.library.R;


/**
 * Created by cz on 15/12/1.
 */
public class DebugAppInfoFragment extends TitleBarFragment {
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            title = arguments.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_app_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleText(title);
        setOnBackClickListener(v->getFragmentManager().popBackStack());
        DeveloperConfig developerConfig = DeveloperManager.getInstances().getDeveloperConfig();
        //软件信息
        setText(view,R.id.tv_channel,R.string.channel_value, TextUtils.isEmpty(developerConfig.channel)?"":developerConfig.channel);
        setText(view,R.id.tv_version,R.string.app_version_value, getAppVersionName());
        setText(view,R.id.tv_version_code,R.string.app_code_value, getAppVersionCode());

        //机器信息
        setText(view,R.id.tv_os_version,R.string.os_version_value, Build.DISPLAY);
        setText(view,R.id.tv_os_api,R.string.os_api_value, Build.VERSION.SDK_INT);
        setText(view,R.id.tv_device_model,R.string.device_model_value, Build.MODEL);
        setText(view,R.id.tv_device_brand,R.string.device_brand_value, Build.BRAND);
        setText(view,R.id.tv_imei,R.string.imei_value, getImeiValue());
        setText(view,R.id.tv_android_id,R.string.android_id_value, getAndroidId());
    }

    public void setText(View view,@IdRes int id, @StringRes int res,Object...params){
        TextView textView= (TextView) view.findViewById(id);
        textView.setText(getString(res,params));
    }

    /**
     * 获得软件版本
     *
     * @return
     */
    private String getAppVersionName() {
        String appVersion = null;
        try {
            Context context = getContext();
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * 获得软件code
     *
     * @return
     */
    private int getAppVersionCode() {
        int appCode = -1;
        try {
            Context context = getContext();
            appCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            appCode = -1;
        }
        return appCode;
    }



    public ApplicationInfo getAppInfo() {
        ApplicationInfo appInfo = null;
        try {
            Context context = getContext();
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }


    /**
     * 获得android设备id
     *
     * @return
     */
    private String getAndroidId() {
        String value = null;
        try {
            Context appContext = getContext();
            value = Settings.Secure.getString(appContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            value="Android Id 获取失败!";
        }
        return value;
    }

    /**
     *
     * @return
     */
    public String getImeiValue() {
        String value = null;
        if (hasPermission("android.permission.READ_PHONE_STATE")) {
            try {
                value = ((TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            } catch (Exception e) {
                value="获取imei码失败了~";
            }
        } else {
            value="Need READ_PHONE_STATE Permission";
        }
        return value;
    }

    /**
     * 判断某个权限是否授权
     *
     * @param permissionName 权限名称，比如：android.permission.READ_PHONE_STATE
     * @return
     */
    private boolean hasPermission(String permissionName) {
        boolean result=false;
        try {
            Context context = getContext();
            PackageManager pm = context.getPackageManager();
            result=(PackageManager.PERMISSION_GRANTED == pm.checkPermission(permissionName, context.getPackageName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
