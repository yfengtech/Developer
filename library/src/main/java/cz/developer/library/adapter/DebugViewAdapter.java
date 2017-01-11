package cz.developer.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.R;
import cz.developer.library.model.DebugViewItem;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewAdapter extends BaseAdapter{
    private final LayoutInflater layoutInflater;
    private final List<DebugViewItem> viewItems;
    private final List<DebugViewItem> selectItems;

    public DebugViewAdapter(Context context, List<DebugViewItem> viewItems) {
        this.layoutInflater=LayoutInflater.from(context);
        this.selectItems =new ArrayList<>();
        this.viewItems = new ArrayList<>();
        if(null!=viewItems){
            this.viewItems.addAll(viewItems);
            this.selectItems.addAll(viewItems);
        }
    }

    @Override
    public int getCount() {
        return viewItems.size();
    }

    @Override
    public DebugViewItem getItem(int position) {
        return viewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<DebugViewItem> getSelectItems(){
        return selectItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView=layoutInflater.inflate(R.layout.debug_view_item,parent,false);
        }
        DebugViewItem item = getItem(position);
        TextView viewName= (TextView) convertView.findViewById(R.id.tv_view_name);
        TextView viewDesc= (TextView) convertView.findViewById(R.id.tv_view_desc);
        CheckBox checkBox= (CheckBox) convertView.findViewById(R.id.check_box);
        viewName.setText(item.viewName);
        viewDesc.setText(item.viewInfo);
        checkBox.setChecked(selectItems.contains(item));
        return convertView;
    }

    public void selectItem(int position) {
        DebugViewItem item = getItem(position);
        if(selectItems.contains(item)){
            selectItems.remove(item);
        } else {
            selectItems.add(item);
        }
        notifyDataSetChanged();
    }
}
