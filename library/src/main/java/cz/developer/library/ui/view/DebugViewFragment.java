package cz.developer.library.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;

import com.quant.titlebar.TitleBarFragment;

import java.util.List;

import cz.developer.library.DebugViewHelper;
import cz.developer.library.DeveloperActivityManager;
import cz.developer.library.R;
import cz.developer.library.adapter.DebugViewAdapter;
import cz.developer.library.model.DebugViewItem;
import cz.developer.library.xml.ViewConfigReader;
import xyqb.library.config.PrefsManager;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewFragment extends TitleBarFragment {
    private DebugViewAdapter adapter;
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
    public View onCreateView(Context context, LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleText(title);
        setOnBackClickListener(v->getFragmentManager().popBackStack());
        Switch switchView= (Switch) view.findViewById(R.id.switch_view);
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //设置所有控件状态
            DeveloperActivityManager.get().forActivities(activity->{
                View decorView = activity.getWindow().getDecorView();
                DebugViewHelper.initLayout((ViewGroup) decorView,isChecked);
            });
        });
        List<DebugViewItem> viewItems= PrefsManager.readConfig(ViewConfigReader.class);
        //AbsListView/RecyclerView/ImageView
        ListView listView= (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter = new DebugViewAdapter(getContext(), viewItems));
        listView.setOnItemClickListener((parent, view1, position, id) -> adapter.selectItem(position));
    }

}
