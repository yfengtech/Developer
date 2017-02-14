package cz.developer.library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Patterns;
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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.developer.library.adapter.DebugItemInfoAdapter;
import cz.developer.library.adapter.DebugWebImageAdapter;
import cz.developer.library.callback.HierarchyTreeChangeListener;

/**
 * Created by cz on 1/11/17.
 */

public class DebugViewHelper {

    public static void setViewHierarchyChangeListener(ViewGroup layout){
        layout.setOnHierarchyChangeListener(HierarchyTreeChangeListener.wrap(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View childView) {
                if(!(parent instanceof AbsListView)){
                    initViewInfo(childView, DeveloperManager.config.debugList);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        }));
    }

    /**
     * 初始化控件信息
     * @param childView
     * @param select
     */
    private static void initViewInfo(View childView, boolean select) {
        if(childView instanceof AbsListView){
            initAbsListView((AbsListView)childView,select);
        } else if(childView instanceof RecyclerView){
            initRecyclerView((RecyclerView) childView,select);
        } else if(childView instanceof ImageView){
            initImageView((ImageView) childView,select);
        } else if(childView instanceof WebView){
            initWebView((WebView)childView,select);
        } else if(!isViewLongClickable(childView)){
            initView(childView,select);
        }
    }

    /**
     * 初始化控件信息
     * @param layout
     * @param select
     */
    public static void initLayoutInfo(ViewGroup layout, boolean select){
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
                initWebView((WebView)childView,select);
            } else if(childView instanceof ViewGroup){
                initLayoutInfo((ViewGroup) childView,select);
            } else if(!isViewLongClickable(childView)){
                initView(childView,select);
            }
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
        if(!select){
            //部分手机上,longClickable设置后,也会拦截点击事件,如小米...
            listView.setLongClickable(select);
        } else {
            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                ListAdapter adapter = listView.getAdapter();
                //初始化己加载条目
                if(null!=adapter){
                    itemClick(view.getContext(),adapter.getItem(position));
                }
                return true;
            });
        }
    }

    private static void itemClick(Context context,Object item){
        try{
            if(null!=context&&null!=item){
                Map<String,String> fieldItems = getItemFieldItems(item);
                List<String> items=new ArrayList<>(fieldItems.size());
                List<String> itemValue=new ArrayList<>(fieldItems.size());
                for(Map.Entry<String,String> entry:fieldItems.entrySet()){
                    itemValue.add(entry.getValue());
                    items.add(entry.getKey()+":"+entry.getValue());
                }
                ListView debugList=new ListView(context);
                debugList.setAdapter(new DebugItemInfoAdapter(context, items));
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle(R.string.debug_info).setView(debugList).show();
                debugList.setOnItemClickListener((parent1, view1, position1, id1) -> {
                    dialog.dismiss();
                    itemClickListener(parent1.getContext(),itemValue.get(position1));
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 条目点击
     * @param value
     */
    private static void itemClickListener(Context context, String value) {
        if(null!=context){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.item_info).setMessage(value);

            //是否为网页,为网页,加上跳转
            Matcher matcher = Patterns.WEB_URL.matcher(value);
            if(matcher.matches()){
                builder.setNegativeButton(R.string.go_website,(dialog, which) ->
                        context.startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(value))));
            }
            //加入copy
            builder.setNeutralButton(R.string.copy,(dialog, which) -> {
                // 得到剪贴板管理器
                ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText("item field value",value));
                Toast.makeText(context, R.string.copy_complete, Toast.LENGTH_SHORT).show();
            });
            //加入分享
            builder.setPositiveButton(R.string.share,(dialog, which) -> shareMessage(context,context.getString(R.string.content_share_to),value)).show();
        }
    }


    /**
     * 分享功能
     * @param context 上下文
     * @param title 消息标题
     * @param message 消息内容
     */
    public static void shareMessage(Context context, String title, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
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
            if(!select){
                view.setLongClickable(select);
            } else {
                view.setOnLongClickListener(v -> {
                    Object tag = v.getTag();
                    if(null!=tag){
                        itemClick(view.getContext(), tag);
                    }
                    return null!=tag;
                });
            }
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
        } else if(!isViewLongClickable(view)){
            if(!select){
                view.setLongClickable(select);
            } else {
                view.setOnLongClickListener(v -> {
                    Object tag = view.getTag();
                    if(null!=tag){
                        itemClick(view.getContext(), tag);
                    }
                    return null!=tag;
                });
            }
        }
    }



    private static void initView(View view,boolean select) {
        if(!select){
            view.setLongClickable(select);
        } else {
            view.setOnLongClickListener(v -> {
                Object item = view.getTag();
                if(null!=item){
                    itemClick(v.getContext(),item);
                }
                return null!=item;
            });
        }
    }

    private static void initImageView(ImageView imageView,boolean select) {
        if(!select){
            imageView.setLongClickable(select);
        } else if(!isViewLongClickable(imageView)){
            imageView.setOnLongClickListener(v -> {
                Object tag = imageView.getTag();
                if(null!=tag) {
                    Context context = v.getContext();
                    new AlertDialog.Builder(context).setTitle(R.string.look_image).setMessage(tag.toString()).
                            setNeutralButton(android.R.string.cancel,(dialog, which) -> dialog.dismiss()).
                            setNegativeButton(R.string.share,(dialog, which) ->
                                    shareMessage(context,context.getString(R.string.content_share_to),tag.toString())).
                            setPositiveButton(R.string.go_website,(dialog, which) -> {
                                try{
                                    Uri uri = Uri.parse(tag.toString());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    context.startActivity(intent);
                                } catch (Exception e){
                                    Toast.makeText(context, "Open website error!", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
                return null!=tag;
            });
        }
    }

    private static Map<String,String> getItemFieldItems(Object item){
        Map<String,String> fieldItems=new HashMap<>();
        if(null!=item){
            if(item instanceof String){
                fieldItems.put("String",item.toString());
            } else {
                Class<?> clazz = item.getClass();
                Field[] declaredFields = clazz.getDeclaredFields();
                if(null!=declaredFields){
                    for(int i=0;i<declaredFields.length;i++){
                        if(!declaredFields[i].isAccessible()){
                            declaredFields[i].setAccessible(true);
                        }
                        try {
                            //非静态字静加入
                            if(!Modifier.isStatic(declaredFields[i].getModifiers())){
                                Object o = declaredFields[i].get(item);
                                fieldItems.put(declaredFields[i].getName(),null==o?"Non":o.toString());
                            }
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
