package cz.developer.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cz.developer.library.adapter.DebugItemInfoAdapter;
import cz.developer.library.adapter.IAdapterItem;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewHelper {

    public static void initLayout(ViewGroup parent,  boolean select){
        int childCount = parent.getChildCount();
        for(int i=0;i<childCount;i++){
            View childView=parent.getChildAt(i);
            if(childView instanceof AbsListView){
                initAbsListView((AbsListView)childView,select);
            } else if(childView instanceof RecyclerView){
                initRecyclerView((RecyclerView) childView);
            } else if(childView instanceof ImageView){
                initImageView((ImageView) childView,select);
            } else if(childView instanceof ViewGroup){
                initLayout((ViewGroup) childView,select);
            } else if(childView.isClickable()&&null!=childView.getTag()){
                initView(childView,select);
            }
        }
    }

    public static void initAbsListView(AbsListView listView, boolean select) {
        ListAdapter adapter = listView.getAdapter();
        //初始化己加载条目
        if(null!=adapter){
            listView.setOnItemLongClickListener(!select?null:(parent, view, position, id) -> {
                Object item = adapter.getItem(position);
                try{
                    List<String> fieldItems = getItemFieldItems(item);
                    ListView debugList=new ListView(listView.getContext());
                    debugList.setAdapter(new DebugItemInfoAdapter(listView.getContext(), fieldItems));
                    new AlertDialog.Builder(listView.getContext()).setTitle(item.getClass().getSimpleName()).setView(debugList).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            });
        }

    }

    private static void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                view.setOnLongClickListener(v -> {
                    setRecyclerChildViewDebugListener(view, recyclerView);
                    return true;
                });
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
            }
        });
    }

    private static void setRecyclerChildViewDebugListener(View view, RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(null!=adapter&&adapter instanceof IAdapterItem){
            IAdapterItem adapterItem= (IAdapterItem) adapter;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int position = layoutManager.getPosition(view);
            Object item = adapterItem.getItem(position);
            try{
                List<String> fieldItems = getItemFieldItems(item);
                ListView debugList=new ListView(view.getContext());
                debugList.setAdapter(new DebugItemInfoAdapter(debugList.getContext(), fieldItems));
                new AlertDialog.Builder(debugList.getContext()).setTitle(item.getClass().getSimpleName()).setView(debugList).show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void initView(View view,boolean select) {
        view.setOnLongClickListener(!select?null:v -> {
            Object item = v.getTag();
            if(null!=item){
                List<String> fieldItems = getItemFieldItems(item);
                ListView debugList=new ListView(view.getContext());
                debugList.setAdapter(new DebugItemInfoAdapter(debugList.getContext(), fieldItems));
                new AlertDialog.Builder(debugList.getContext()).setTitle(item.getClass().getSimpleName()).setView(debugList).show();
            }
            return true;
        });
    }

    private static void initImageView(ImageView imageView,boolean select) {
        imageView.setOnLongClickListener(!select?null:v -> {
            Object tag = v.getTag();
            if(null!=tag){
                new AlertDialog.Builder(v.getContext()).setTitle(R.string.look_image).setMessage(tag.toString()).
                        setNegativeButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss()).
                        setPositiveButton(android.R.string.ok,(dialog, which) -> {
                            Context context = v.getContext();
                            try{
                                Uri uri = Uri.parse(tag.toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                context.startActivity(intent);
                            } catch (Exception e){
                                Toast.makeText(context, "Open website error!", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
            return true;
        });
    }

    private static List<String> getItemFieldItems(Object item){
        List<String> fieldItems=new ArrayList<>();
        if(null!=item){
            if(item instanceof String){
                fieldItems.add("String:"+item);
            } else {
                Class<?> clazz = item.getClass();
                Field[] declaredFields = clazz.getDeclaredFields();
                if(null!=declaredFields){
                    for(int i=0;i<declaredFields.length;i++){
                        if(!declaredFields[i].isAccessible()){
                            declaredFields[i].setAccessible(true);
                        }
                        try {
                            Object o = declaredFields[i].get(item);
                            fieldItems.add(declaredFields[i].getName()+":"+o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return fieldItems;
    }
}
