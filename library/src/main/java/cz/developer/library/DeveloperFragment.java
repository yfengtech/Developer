package cz.developer.library;

import android.content.Context;
import android.media.JetPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quant.titlebar.TitleBarFragment;

import java.util.List;

import cz.developer.library.adapter.DebugListAdapter;
import cz.developer.library.mode.DebugItem;
import cz.developer.library.xml.ListConfigReader;
import xyqb.library.config.PrefsManager;

public class DeveloperFragment extends TitleBarFragment {
    private static final String TAG = "DeveloperFragment";
    private ListView listView;
    public static Fragment newInstance(){
        return new DeveloperFragment();
    }

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.list);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.debug_list);
        List<DebugItem> debugItems = PrefsManager.readConfig(ListConfigReader.class);
        listView.setAdapter(new DebugListAdapter(getContext(),debugItems));

    }
}
