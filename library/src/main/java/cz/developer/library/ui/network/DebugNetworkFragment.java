package cz.developer.library.ui.network;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quant.titlebar.TitleBarFragment;
import com.quant.titlebar.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;
import cz.developer.library.model.NetItem;
import cz.developer.library.prefs.DeveloperPrefs;
import cz.developer.library.ui.dialog.EditDialog;

/**
 * Created by czz on 2016/10/29.
 */

public class DebugNetworkFragment extends TitleBarFragment {
    private ListView listView;
    private NetworkItemAdapter adapter;
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getArguments()){
            title=getArguments().getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(Context context, LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitleBar();
        initAdapter();

    }

    private void initTitleBar() {
        setTitleText(title);
        setOnBackClickListener(v -> getFragmentManager().popBackStack());
        SearchView searchView=new SearchView(getContext());
        searchView.setSearchDeleteDrawable(ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_clear_mtrl_alpha));
        searchView.setHintDrawable(ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_search_white));
        searchView.setEditTextColor(Color.WHITE);
        searchView.setOnSubmitListener(item -> adapter.filter(null));
        searchView.setOnTextChangeListener((newItem, oldItem, count) -> {
            if (TextUtils.isEmpty(newItem)) {
                adapter.filter(null);
            } else {
                adapter.filter(newItem.toString().toLowerCase());
            }
        });
        addImageMenuItem(R.drawable.abc_ic_search_white,searchView);
    }

    private void initAdapter() {
        INetworkAdapter networkAdapter = DeveloperManager.getInstances().getNetworkAdapter();
        String serverUrl=null;
        List<NetItem> networkItems=null;
        final ArrayList<String> selectItems=new ArrayList<>();
        if(null!=networkAdapter){
            serverUrl = networkAdapter.getServerUrl();
            networkItems = networkAdapter.getNetworkItems();
            List<String> items = networkAdapter.getSelectUrl();
            if(null!=items){
                selectItems.addAll(items);
            }
        }
        final String finalUrl=serverUrl;
        listView.setAdapter(adapter=new NetworkItemAdapter(getContext(),networkItems,serverUrl));
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            NetItem item = adapter.getItem(i);
            final String url = DeveloperPrefs.getString(item.action);
            EditDialog editDialog = EditDialog.newInstance(selectItems, !TextUtils.isEmpty(url)?url:finalUrl);
            editDialog.setOnSubmitListener(text->{
                if(!text.equals(url)){
                    DeveloperPrefs.setString(item.action,text);
                    adapter.removeUrlItem(i);
                    adapter.notifyDataSetChanged();
                }
            });
            editDialog.show(getFragmentManager(),null);
            return true;
        });
    }
}
