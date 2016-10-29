package cz.developer.library;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.quant.titlebar.TitleBarFragment;


/**
 * Created by cz on 15/12/1.
 * debug信息
 */
public class DebugInfoFragment extends TitleBarFragment {
//    @Id(R.id.sv_debug_mode)
    private Switch mDebugMode;
//    @Id(R.id.sv_cache_mode)
    private Switch mCacheMode;
//    @Id(R.id.sv_run_info)
    private Switch mRunInfo;
//    @Id(R.id.sv_error_info)
    private Switch mErrorInfo;
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
        return inflater.inflate(R.layout.fragment_debug_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleText(title);
//        view.findViewById(R.id.dl_debug).setOnClickListener(v -> mDebugMode.toggle());
//        view.findViewById(R.id.dl_cache).setOnClickListener(v -> mCacheMode.toggle());
//        view.findViewById(R.id.dl_run_info).setOnClickListener(v -> mRunInfo.toggle());
//        view.findViewById(R.id.dl_error_url).setOnClickListener(v -> mErrorInfo.toggle());
//        mDebugMode.setChecked(PrefernceUtils.getBoolean(ConfigName.DEBUG), false);
//        mCacheMode.setChecked(PrefernceUtils.getRvsBoolean(ConfigName.OPEN_CACHE), false);
//        mRunInfo.setChecked(PrefernceUtils.getBoolean(ConfigName.APP_RUN_INFO), false);
//        mErrorInfo.setChecked(PrefernceUtils.getRvsBoolean(ConfigName.DEBUG_ERROR_URL), false);
//        mDebugMode.setOnCheckedChangeListener((SwitchView switchView, boolean isChecked) -> {
//            PrefernceUtils.setBoolean(ConfigName.DEBUG, isChecked);
//            App.setDebugMode(isChecked);
//        });
//        mCacheMode.setOnCheckedChangeListener((SwitchView switchView, boolean isChecked) ->
//                PrefernceUtils.setBoolean(ConfigName.OPEN_CACHE, !isChecked));
//        mErrorInfo.setOnCheckedChangeListener((switchView, isChecked) ->
//                PrefernceUtils.setBoolean(ConfigName.DEBUG_ERROR_URL, isChecked));
//        mRunInfo.setOnCheckedChangeListener((switchView, isChecked) -> {
//            App.setShowDebugInfo(isChecked);
//            PrefernceUtils.setBoolean(ConfigName.APP_RUN_INFO, isChecked);
//            Loger.e("isCheck:" + isChecked);
//            if (isChecked) {
//                //小米厂商另计
//                if ("xiaomi".equals(Build.BRAND.toLowerCase())) {
//                    new AlertDialog.Builder(getActivity()).setMessage(R.string.xiaomi_info).setPositiveButton(R.string.to, (dialog, which) -> {
//                        PackageUtils.showInstalledAppDetails(getActivity());
//                        getActivity().startService(new Intent(getActivity(), FxService.class));
//                    }).setNegativeButton(R.string.cancel, (dialog, which) -> {
//                        dialog.dismiss();
//                    }).show();
//                } else {
//                    getActivity().startService(new Intent(getActivity(), FxService.class));
//                }
//            } else {
//                getActivity().stopService(new Intent(getActivity(), FxService.class));
//            }
//        });
    }
}
