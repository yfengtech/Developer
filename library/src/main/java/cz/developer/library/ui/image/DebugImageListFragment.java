package cz.developer.library.ui.image;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.quant.titlebar.TitleBarFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cz.developer.library.DeveloperManager;
import cz.developer.library.R;
import cz.developer.library.model.ImageItem;

/**
 * Created by cz on 11/9/16.
 */

public class DebugImageListFragment extends TitleBarFragment {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private View imageMenu;
    private ArrayList<ImageItem> items;

    public static Fragment newInstance(ArrayList<ImageItem> items){
        Fragment fragment=new DebugImageListFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList("items",items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getArguments()){
            items=getArguments().getParcelableArrayList("items");
        }
    }

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_image_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.list_view);
        drawerLayout= (DrawerLayout) view.findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                imageMenu.animate().translationX(imageMenu.getWidth()/3*slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        final View container=view.findViewById(R.id.ll_container);
        Switch switchView= (Switch) view.findViewById(R.id.st_translation);
        switchView.setChecked(true);
        switchView.setOnCheckedChangeListener((compoundButton, b) ->{
            container.setBackgroundColor(b? Color.TRANSPARENT:Color.WHITE);
            drawerLayout.postInvalidate();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitleMenu();
        ImageAdapter imageAdapter = DeveloperManager.getInstances().getDeveloperConfig().imageAdapter;
        if(null!=imageAdapter){
            final DebugImageAdapter adapter = new DebugImageAdapter(getContext(), imageAdapter, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((adapterView, view, i, l) -> {
            });
            listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                ImageItem item = adapter.getItem(i);
                if(null!=item&&null!=item.obj){
                    ListView listView1 =new ListView(getContext(),null,android.R.attr.listViewStyle);
                    final List<String> items1 =new ArrayList<>();
                    final List<String> valueItems=new ArrayList<>();
                    Class<? extends Parcelable> clazz = item.obj.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    String value=null;
                    for(int k=0;k<fields.length;k++){
                        if(!fields[k].isAccessible()){
                            fields[k].setAccessible(true);
                        }
                        value = fields[k].getName();
                        if(!TextUtils.isEmpty(value)){
                            try {
                                Object obj = fields[k].get(item.obj);
                                if(null!=obj){
                                    String valueItem=(null==obj?"null":obj.toString());
                                    items1.add(value+" = "+valueItem);
                                    valueItems.add(valueItem);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    listView1.setAdapter(new ArrayAdapter<>(getContext(),R.layout.simple_text_item, items1));
                    listView1.setOnItemLongClickListener((adapterView1, view1, i12, l1) -> {
                        ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setText(valueItems.get(i12));
                        Toast.makeText(getContext(), "Copy Text:"+valueItems.get(i12), Toast.LENGTH_SHORT).show();
                        return true;
                    });
                    new AlertDialog.Builder(getContext()).
                            setTitle(item.info).
                            setView(listView1).
                            setPositiveButton(android.R.string.cancel,(dialogInterface, i1) -> dialogInterface.dismiss()).show();
                }
                return true;
            });
        }
    }

    private void initTitleMenu() {
        setTitleText(R.string.debug_image_list);
        setOnBackClickListener(v->getFragmentManager().popBackStack());
        addImageMenuItem(R.drawable.ic_menu_white);
        ViewGroup menuContainer= (ViewGroup) titleBar.findViewById(R.id.menu_layout);
        imageMenu = menuContainer.getChildAt(0);
        setOnMenuItemClickListener((v, index) -> {
            if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                v.animate().translationX(0);
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                v.animate().translationX(v.getWidth()/3);
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }
}
