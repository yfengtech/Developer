package cz.developer.library.ui.network;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.R;
import cz.developer.library.model.NetItem;
import cz.developer.library.prefs.DeveloperPrefs;

/**
 * Created by cz on 11/8/16.
 */

public class NetworkItemAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<NetItem> items;
    private final SparseArray<String> urlItems;
    private CharSequence filterText;
    private ItemFilter filter;
    private String serverUrl;
    private int textColor;

    public NetworkItemAdapter(Context context,List<NetItem> items,String url) {
        TypedValue typedValue = new TypedValue();
        this.context=context;
        this.textColor=typedValue.data;
        this.inflater=LayoutInflater.from(context);
        this.items=new ArrayList<>();
        this.serverUrl=url;
        this.urlItems=new SparseArray<>();
        if(null!=items){
            this.items.addAll(items);
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NetItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(null==view){
            view = inflater.inflate(R.layout.network_item, parent, false);
            ViewHolder holder=new ViewHolder();
            holder.actionView= (TextView) view.findViewById(R.id.tv_name);
            holder.pathView= (TextView) view.findViewById(R.id.tv_path);
            holder.urlView= (TextView) view.findViewById(R.id.tv_url);
            view.setTag(holder);
        }
        ViewHolder holder= (ViewHolder) view.getTag();
        NetItem item = getItem(i);
        holder.actionView.setText(getString(R.string.network_desc_value, item.getInfo()));
        holder.pathView.setText(getString(R.string.path_value, item.getUrl()));
        String url = urlItems.get(i);
        if(null==url){
            String dynamicUrl = DeveloperPrefs.INSTANCE.getString(item.getAction());
            url=TextUtils.isEmpty(dynamicUrl)?"":dynamicUrl;
            urlItems.put(i,url);
        }
        holder.urlView.setTextColor(TextUtils.isEmpty(url)?textColor:Color.RED);
        holder.urlView.setText(getString(R.string.url_value,TextUtils.isEmpty(url)?serverUrl:url));
        setColorSpan(holder.actionView, Color.GREEN,filterText);
        setColorSpan(holder.pathView, Color.GREEN,filterText);
        return view;
    }

    public void removeUrlItem(int i){
        urlItems.remove(i);
    }

    public void clearUrlItems(){
        urlItems.clear();
    }

    public String getString(@StringRes int res,Object... params){
        return context.getString(res,params);
    }

    public void setColorSpan(TextView textView, int color, Object... words) {
        CharSequence text = textView.getText();
        SpannableStringBuilder span = new SpannableStringBuilder(text);
        if (!TextUtils.isEmpty(text) && null != words) {
            for (int i = 0; i < words.length; i++) {
                if (null != words[i]) {
                    int index=0,start =0;
                    while (-1 != start) {
                        start = text.toString().indexOf(words[i].toString(), index);
                        if (-1 != start) {
                            span.setSpan(new ForegroundColorSpan(color), start, start + words[i].toString().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            index=start+1;
                        }
                    }
                }
            }
        }
        textView.setText(span);
    }

    public void swapItems(List<NetItem> items){
        if(null!=items){
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if(null==filter){
            filter=new ItemFilter(items);
        }
        return filter;
    }

    public void filter(CharSequence text){
        this.filterText=text;
        getFilter().filter(text);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl=serverUrl;
    }

    private class ItemFilter extends Filter{
        private final ArrayList<NetItem> finalItems;
        public ItemFilter(List<NetItem> items){
            finalItems=new ArrayList<>();
            if(null!=items){
                finalItems.addAll(items);
            }
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults=new FilterResults();
            List<NetItem> filterItems=new ArrayList<>();
            for(int i=0;i<finalItems.size();i++){
                NetItem entity = finalItems.get(i);
                if((!TextUtils.isEmpty(entity.getInfo())&& entity.getInfo().contains(constraint))||
                        (!TextUtils.isEmpty(entity.getUrl())&& entity.getUrl().contains(constraint))){
                    filterItems.add(entity);
                }
            }
            filterResults.values=filterItems;
            filterResults.count=filterItems.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            if (results.count > 0) {
                items.addAll((ArrayList<NetItem>) results.values);
            } else if(TextUtils.isEmpty(constraint)){
                items.addAll(finalItems);
            }
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder{
        public TextView actionView;
        public TextView pathView;
        public TextView urlView;
    }
}
