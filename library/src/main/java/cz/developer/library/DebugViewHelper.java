package cz.developer.library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.developer.library.adapter.DebugItemInfoAdapter;
import cz.developer.library.adapter.DebugWebImageAdapter;
import cz.developer.library.adapter.IAdapterItem;

import static android.content.ContentValues.TAG;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewHelper {

    public static void initLayout(ViewGroup root,ViewGroup layout, boolean select,boolean force){
        if(force||DeveloperManager.config.debugList){
            int childCount = layout.getChildCount();
            for(int i=0;i<childCount;i++){
                View childView=layout.getChildAt(i);
                if(childView instanceof AbsListView){
                    initAbsListView((AbsListView)childView,select);
                } else if(childView instanceof RecyclerView){
                    initRecyclerView((RecyclerView) childView,select);
                } else if(childView instanceof ImageView){
                    initImageView((ImageView) childView,select);
                } else if(childView instanceof WebView){
                    initWebView(root,(WebView)childView,select);
                } else if(childView instanceof ViewGroup){
                    initLayout(root,(ViewGroup) childView,select,force);
                } else if(childView.isClickable()&&!isViewLongClickable(childView)&&null!=childView.getTag()){
                    initView(childView,select);
                }
            }
        }
    }

    private static boolean isViewLongClickable(View view){
        boolean result=false;
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Object o = field.get(view);
            field=o.getClass().getDeclaredField("mOnLongClickListener");
            field.setAccessible(true);
            result=null!=field.get(o);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
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

    private static void initWebView(ViewGroup root,WebView webView, boolean select) {
        Context context = webView.getContext();
        View debugView = webView.findViewById(R.id.debug_btn);
        if(select&&null==debugView){
            View findView=root.findViewById(android.R.id.content);
            if(null!=findView){
                FrameLayout contentView= (FrameLayout)findView;
                ImageView imageView=new ImageView(webView.getContext());
                imageView.setImageResource(R.drawable.ic_bug_report_white);
                imageView.setBackgroundResource(R.drawable.primary_oval_shape);
                int padding = applyDimension(context.getResources().getDisplayMetrics(), 12);
                imageView.setPadding(padding,padding,padding,padding);
                imageView.setId(R.id.debug_btn);
                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity= Gravity.RIGHT|Gravity.BOTTOM;
                layoutParams.rightMargin=padding;
                layoutParams.bottomMargin=padding;
                contentView.addView(imageView,layoutParams);
                imageView.setOnClickListener(v -> {
                    webView.getSettings().setJavaScriptEnabled(true);
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
                        //4.4以上,展示网页内所有图片
                        WebView.setWebContentsDebuggingEnabled(true);
                        webView.evaluateJavascript("javascript:(function(){"+
                                "var imgNodes = document.getElementsByTagName(\"img\");" +
                                "    var imgUrls=[];" +
                                "        for(var i=0;i<imgNodes.length;i++){" +
                                "            imgUrls[i] = imgNodes[i].getAttribute('src');" +
                                "        }" +
                                "        return imgUrls"+
                                "})()",
                                value -> {
                                    List<String> urlItems=new ArrayList<>();
                                    Matcher matcher = Pattern.compile("(\"([^\"]+)\")+").matcher(value);
                                    while(matcher.find()){
                                        urlItems.add(matcher.group(2));
                                    }
                                    //超过80
                                    if(80>webView.getProgress()){
                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(context.getString(R.string.web_view_loading));
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        webView.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(80<webView.getProgress()&&Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                                                    progressDialog.dismiss();
                                                    showWebViewDetailDialog(webView, urlItems);
                                                } else {
                                                    webView.postDelayed(this,100);
                                                }
                                            }
                                        }, 1000);
                                    } else {
                                        showWebViewDetailDialog(webView, urlItems);
                                    }
                                });
                    } else {
                        new AlertDialog.Builder(v.getContext()).
                                setTitle(R.string.debug_url).
                                setMessage(webView.getUrl()).
                                setNegativeButton(R.string.go_website,(dialog, which) ->
                                    v.getContext().startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl())))).
                                            setPositiveButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss()).show();
                    }
                });
            }
        } else if(!select&&null!=debugView){
            webView.removeView(debugView);
        }
    }

    private static void showWebViewDetailDialog(WebView webView,  List<String> urlItems) {
        final Context context = webView.getContext();
        new AlertDialog.Builder(context).
                setTitle(R.string.debug_web).
                setMessage(webView.getUrl()).
                setNeutralButton(R.string.go_website_image, (dialog, which) -> {
                    //查看图片列表
                    ListView listView = new ListView(context);
                    listView.setAdapter(new DebugWebImageAdapter(context, urlItems));
                    listView.setOnItemClickListener((parent, view, position, id) ->
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlItems.get(position)))));
                    new AlertDialog.Builder(context).
                            setTitle(R.string.debug_image).
                            setMessage(webView.getUrl()).
                            setView(listView).show();
                }).setNegativeButton(R.string.go_website, (dialog, which) -> {
            Uri uri = Uri.parse(webView.getUrl());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        }).setPositiveButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
    }

    private static int applyDimension(DisplayMetrics metrics,int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
    }

    private static void initRecyclerView(RecyclerView recyclerView,final boolean select) {
        int childCount = recyclerView.getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = recyclerView.getChildAt(i);
            childView.setOnLongClickListener(!select?null:v -> {
                setRecyclerChildViewDebugListener(childView, recyclerView);
                return true;
            });
        }
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                view.setOnLongClickListener(!select?null:v -> {
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
