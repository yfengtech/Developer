package cz.developer.library.ui.image;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quant.titlebar.TitleBarFragment;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;
import cz.developer.library.bus.DeveloperBus;
import cz.developer.library.event.OnChangedImageItemEvent;

/**
 * Created by cz on 11/9/16.
 */
public class DebugImageFragment extends TitleBarFragment {
    private ListView listView;
    private DebugImageItemAdapter adapter;

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list_view);
        view.findViewById(R.id.iv_start).setOnClickListener(v -> DeveloperManager.toFragment(getActivity(),DebugImageListFragment.newInstance(adapter.getItems())));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.debug_image);
        setOnBackClickListener(v -> getFragmentManager().popBackStack());
        listView.postDelayed(()->{
            listView.setAdapter(adapter = new DebugImageItemAdapter(getContext(), DeveloperManager.getInstances().getDeveloperConfig().imageAdapter));
            listView.setOnItemClickListener((adapterView, view, i, l) ->
                    DeveloperManager.toFragment(getActivity(), DebugImageEditFragment.newInstance(adapter.getItem(i), i)));
        },300);
        DeveloperBus.subscribe(OnChangedImageItemEvent.class, item -> {
            if (null != adapter) {
                adapter.setItem(item.index, item.item);
            }
        });
    }

    @Override
    public void onDestroyView() {
        DeveloperBus.unSubscribeItems(this);
        super.onDestroyView();
    }
}