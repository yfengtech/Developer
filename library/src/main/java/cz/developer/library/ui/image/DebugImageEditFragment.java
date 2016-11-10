package cz.developer.library.ui.image;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.quant.titlebar.TitleBarFragment;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.R;
import cz.developer.library.bus.DeveloperBus;
import cz.developer.library.event.OnChangedImageItemEvent;
import cz.developer.library.model.ImageItem;
import cz.developer.library.widget.ThumbView;

/**
 * Created by cz on 11/9/16.
 */

public class DebugImageEditFragment extends TitleBarFragment {
    private DrawerLayout drawerLayout;
    private ThumbView thumbView;
    private ImageItem item;
    private int index;
    public static Fragment newInstance(ImageItem item,int i){
        Fragment fragment=new DebugImageEditFragment();
        Bundle args=new Bundle();
        args.putParcelable("item",item);
        args.putInt("index",i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null!=getArguments()){
            item=getArguments().getParcelable("item");
            index=getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(Context context, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_edit,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        thumbView= (ThumbView) view.findViewById(R.id.tv_thumb);
        thumbView.setItemPadding((int) item.itemPadding);
        thumbView.setPadding(item.horizontalPadding,item.verticalPadding,item.horizontalPadding,item.verticalPadding);
        thumbView.setAspectRatio(item.aspectRatio);
        thumbView.setItemCount(item.imageItems.size());
        thumbView.setItemType(item.imageType);
        thumbView.setColor(ContextCompat.getColor(getContext(),R.color.alphaGray));


        setText(view,R.id.tv_info,R.string.info_value,item.info);
        setText(view,R.id.tv_item_count,R.string.item_count_value,item.imageItems.size());

        final TextView aspectRatioInfo= (TextView) view.findViewById(R.id.tv_aspect_ratio);
        aspectRatioInfo.setText(getString(R.string.aspect_ratio_value,item.aspectRatio));
        SeekBar aspectRatioSeekBar= (SeekBar) view.findViewById(R.id.sb_aspect_ratio);
        aspectRatioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                item.aspectRatio=3.0f*(i*1.0f/seekBar.getMax());
                aspectRatioInfo.setText(getString(R.string.aspect_ratio_value,item.aspectRatio));
                thumbView.setAspectRatio(item.aspectRatio);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView horizontalPaddingView= (TextView) view.findViewById(R.id.tv_horizontal_padding);
        horizontalPaddingView.setText(getString(R.string.horizontal_padding_value,item.horizontalPadding, convertPixelToDp(item.horizontalPadding)));
        SeekBar horizontalPaddingSeekBar= (SeekBar) view.findViewById(R.id.sb_horizontal_padding);
        horizontalPaddingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                item.horizontalPadding=i;
                horizontalPaddingView.setText(getString(R.string.horizontal_padding_value,item.horizontalPadding, convertPixelToDp(item.horizontalPadding)));
                thumbView.setPadding(i,item.verticalPadding,i,item.verticalPadding);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView verticalPaddingView= (TextView) view.findViewById(R.id.tv_vertical_padding);
        verticalPaddingView.setText(getString(R.string.vertical_padding_value,item.verticalPadding, convertPixelToDp(item.verticalPadding)));
        SeekBar verticalPaddingSeekBar= (SeekBar) view.findViewById(R.id.sb_vertical_padding);
        verticalPaddingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                item.verticalPadding=i;
                verticalPaddingView.setText(getString(R.string.vertical_padding_value,item.verticalPadding, convertPixelToDp(i)));
                thumbView.setPadding(item.horizontalPadding,i,item.horizontalPadding,i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView itemPaddingView= (TextView) view.findViewById(R.id.tv_item_padding);
        itemPaddingView.setText(getString(R.string.item_padding_value,item.itemPadding, convertPixelToDp(item.itemPadding)));
        SeekBar itemPaddingSeekBar= (SeekBar) view.findViewById(R.id.sb_item_padding);
        itemPaddingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                item.itemPadding=i;
                itemPaddingView.setText(getString(R.string.item_padding_value,item.itemPadding, convertPixelToDp(i)));
                thumbView.setItemPadding(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        RadioButton button= (RadioButton) view.findViewById(ImageItem.BANNER_ITEM==item.imageType?R.id.rb_banner:R.id.rb_list);
        button.setChecked(true);

        final View container=view.findViewById(R.id.ll_container);
        drawerLayout= (DrawerLayout) view.findViewById(R.id.drawer_layout);
        Switch switchView= (Switch) view.findViewById(R.id.st_translation);
        switchView.setChecked(true);
        switchView.setOnCheckedChangeListener((compoundButton, b) ->{
            container.setBackgroundColor(b?Color.TRANSPARENT:Color.WHITE);
            drawerLayout.postInvalidate();
        });

        final List<String> finaItems=new ArrayList<>(item.imageItems);
        TextView valueView= (TextView) view.findViewById(R.id.tv_value);
        valueView.setText(String.valueOf(finaItems.size()));
        view.findViewById(R.id.iv_remove_value).setOnClickListener(v->{
            if(1<item.imageItems.size()){
                item.imageItems.remove(item.imageItems.size()-1);
                valueView.setText(String.valueOf(item.imageItems.size()));
                thumbView.setItemCount(item.imageItems.size());
            }
        });
        view.findViewById(R.id.iv_add_value).setOnClickListener(v->{
            if(item.imageItems.size()<finaItems.size()){
                item.imageItems.add(finaItems.get(item.imageItems.size()));
                valueView.setText(String.valueOf(item.imageItems.size()));
                thumbView.setItemCount(item.imageItems.size());
            }
        });
    }

    private float convertPixelToDp(float px){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public void setText(View view,@IdRes int id, @StringRes int res,Object...params){
        TextView textView= (TextView) view.findViewById(id);
        textView.setText(getString(res,params));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleText(R.string.edit_image_format);
        setOnBackClickListener(v->getFragmentManager().popBackStack());
        addImageMenuItem(R.drawable.ic_menu_white);
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

    @Override
    public void onDestroyView() {
        DeveloperBus.post(new OnChangedImageItemEvent(item,index));
        super.onDestroyView();
    }
}
