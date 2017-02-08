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
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.developer.library.adapter.DebugItemInfoAdapter;
import cz.developer.library.adapter.DebugWebImageAdapter;
import cz.developer.library.adapter.IAdapterItem;
import cz.developer.library.callback.HierarchyTreeChangeListener;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewHelper {

    private static final String TAG = "DebugViewHelper";

    public static void initLayout(ViewGroup layout,  boolean select, boolean force){
        if(force||DeveloperManager.config.debugList){
            layout.setOnHierarchyChangeListener(HierarchyTreeChangeListener.wrap(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View childView) {
                    if(childView instanceof AbsListView){
                        initAbsListView((AbsListView)childView,select);
                    } else if(childView instanceof RecyclerView){
                        initRecyclerView((RecyclerView) childView,select);
                    } else if(childView instanceof ImageView){
                        initImageView((ImageView) childView,select);
                    } else if(childView instanceof WebView){
                        initWebView((WebView)childView,select);
                    } else if(childView.isClickable()&&!isViewLongClickable(childView)&&null!=childView.getTag()){
                        initView(childView,select);
                    }
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                    Log.e(TAG,"onChildViewRemoved:"+child);
                }
            }));
        }
    }



    private static boolean isViewLongClickable(View view){
        boolean result=false;
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Object o = field.get(view);
            if(null!=o){
                field= o.getClass().getDeclaredField("mOnLongClickListener");
                field.setAccessible(true);
                result=null!=field.get(o);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static boolean isViewClickable(View view){
        boolean result=false;
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Object o = field.get(view);
            if(null!=o){
                field= o.getClass().getDeclaredField("mOnClickListener");
                field.setAccessible(true);
                result=null!=field.get(o);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void initAbsListView(AbsListView listView, boolean select) {
        listView.setOnItemLongClickListener(!select?null:(parent, view, position, id) -> {
            ListAdapter adapter = listView.getAdapter();
            //初始化己加载条目
            if(null!=adapter){
                Object item = adapter.getItem(position);
                try{
                    List<String> fieldItems = getItemFieldItems(item);
                    ListView debugList=new ListView(listView.getContext());
                    debugList.setAdapter(new DebugItemInfoAdapter(listView.getContext(), fieldItems));
                    new AlertDialog.Builder(listView.getContext()).setTitle(item.getClass().getSimpleName()).setView(debugList).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        });
    }

    private static void initWebView(WebView webView, boolean select) {
        Context context = webView.getContext();
        View debugView = webView.findViewById(R.id.debug_btn);
        if(select&&null==debugView){
            ImageView imageView=new ImageView(webView.getContext());
            imageView.setImageResource(R.drawable.ic_bug_report);
//                imageView.setBackgroundResource(R.drawable.primary_oval_shape);
            int padding = applyDimension(context.getResources().getDisplayMetrics(), 12);
            imageView.setPadding(padding,padding,padding,padding);
            imageView.setId(R.id.debug_btn);
            webView.addView(imageView,new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,0,0));
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
            imageView.post(() -> {
                AbsoluteLayout.LayoutParams layoutParams= (AbsoluteLayout.LayoutParams) imageView.getLayoutParams();
                int margin = applyDimension(context.getResources().getDisplayMetrics(), 4);
                layoutParams.x=webView.getWidth()-imageView.getWidth()-margin;
                layoutParams.y=webView.getHeight()-imageView.getHeight()-margin;
                imageView.requestLayout();
            });
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
            initRecyclerView(recyclerView.getChildAt(i), select, recyclerView);
        }

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                initRecyclerView(view, select, recyclerView);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
            }
        });
    }

    private static void initRecyclerView(View view, boolean select, RecyclerView recyclerView) {
        if(isViewClickable(view)&&!isViewLongClickable(view)){
            view.setOnLongClickListener(!select?null:v -> {
                setRecyclerAdapterItemClicked(view, recyclerView);
                return true;
            });
        } else {
            //子控件事件
            setRecyclerChildViewClicked(view,recyclerView,select);
        }
    }

    private static void setRecyclerChildViewClicked(View view,RecyclerView recyclerView,boolean select) {
        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            for(int i=0;i<viewGroup.getChildCount();i++){
                setRecyclerChildViewClicked(viewGroup.getChildAt(i),recyclerView,select);
            }
        } else if(!isViewLongClickable(view)&&null!=view.getTag()){
            view.setOnLongClickListener(!select?null:v -> {
                setRecyclerAdapterItemClicked(view, recyclerView);
                return true;
            });
        }
    }

    private static void setRecyclerAdapterItemClicked(View view, RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(null!=adapter){
            if(adapter instanceof IAdapterItem){
                IAdapterItem adapterItem= (IAdapterItem) adapter;
                recyclerViewItemClicked(view, recyclerView, adapterItem);
            } else {
                //处理装饰模式内对象
                recyclerViewItemClicked(view, recyclerView, null);
            }
        }
    }

    private static void recyclerViewItemClicked(View view, RecyclerView recyclerView, IAdapterItem adapterItem) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        Object item;
        if(null!=adapterItem){
            int position = layoutManager.getPosition(view);
            item = adapterItem.getItem(position);
        } else {
            item=view.getTag();
        }
        try{
            if(null!=item){
                List<String> fieldItems = getItemFieldItems(item);
                ListView debugList=new ListView(view.getContext());
                debugList.setAdapter(new DebugItemInfoAdapter(debugList.getContext(), fieldItems));
                new AlertDialog.Builder(debugList.getContext()).setTitle(item.getClass().getSimpleName()).setView(debugList).show();
            }
        } catch (Exception e){
            e.printStackTrace();
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
