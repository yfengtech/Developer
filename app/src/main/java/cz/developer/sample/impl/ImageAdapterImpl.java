package cz.developer.sample.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cz.developer.library.model.ImageItem;
import cz.developer.library.ui.image.ImageAdapter;

/**
 * Created by cz on 11/9/16.
 * //    Banner        1.8:1       实际尺寸 750*410
 //    今日秒杀     1.4:1      实际尺寸   115*80
 //    新品首发      2.1:1        实际尺寸   170*80
 //    手机通讯     3.8:1     实际尺寸    345*90  大图
 //    1.3:1    实际尺寸   115*87   小图
 //    自由图：以宽度为基准，下面自由适配
 */
public class ImageAdapterImpl implements ImageAdapter {
    float ASPECT_RATIO_BANNER=0.546f;//15:8  --750*410
    float ASPECT_RATIO_SECKILL=1.38f;//1:1.5 --114*76      345*240
    float ASPECT_RATIO_NEW=2.125f;//1:2   --170*85
    float ASPECT_RATIO_LARGE_GROUP=3.87f;//1:4.6 --345*75
    float ASPECT_RATIO_SMALL_GROUP=1.321f;//1:4.6 --345*75

    @Override
    public View getView(Context context, ViewGroup parent,ImageItem item) {
        ImageView imageView=new ImageView(context);
        imageView.setBackgroundColor(Color.GRAY);
        return imageView;
    }

    @Override
    public List<ImageItem> getImageItems() {
        List<ImageItem> imageItems=new ArrayList<>();
        //banner
        ImageItem imageItem=new ImageItem();
        imageItem.info="BANNER";
        imageItem.type=ImageItem.BANNER_ITEM;
        imageItem.aspectRatio=ASPECT_RATIO_BANNER;
        imageItem.horizontalPadding =24;
        imageItem.verticalPadding=24;
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%E7%BE%8E%E5%A5%B3&step_word=&hs=0&pn=11&spn=0&di=0&pi=0&rn=1&tn=baiduimagedetail&is=&istype=2&ie=utf-8&oe=utf-8&in=24401&cl=2&lm=-1&st=-1&cs=1722713499%2C3854970375&os=1088910703%2C1748467773&simid=&adpicid=0&ln=1998&fr=ala&fmq=1402900904181_R&fm=&ic=0&s=&se=1&sme=&tab=&width=&height=&face=0&ist=&jit=&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fpic.4j4j.cn%2Fupload%2Fpic%2F20130909%2F681ebf9d64.jpg&fromurl=http%3A%2F%2Fwww.4j4j.cn%2Fzmbz%2F9790.html&gsm=");
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%E7%BE%8E%E5%A5%B3&step_word=&hs=0&pn=13&spn=0&di=0&pi=20905405197&rn=1&tn=baiduimagedetail&is=&istype=2&ie=utf-8&oe=utf-8&in=24401&cl=2&lm=-1&st=-1&cs=539019579%2C1503243384&os=&simid=&adpicid=0&ln=1998&fr=ala&fmq=1402900904181_R&fm=&ic=0&s=&se=1&sme=&tab=&width=&height=&face=0&ist=&jit=&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8014c086e061d9507500dd67ff40ad163d9cacd.jpg&fromurl=http%3A%2F%2Fwww.mzitu.com%2F9853&gsm=");
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%E7%BE%8E%E5%A5%B3&step_word=&hs=0&pn=13&spn=1&di=0&pi=20905405197&rn=1&tn=baiduimagedetail&is=&istype=2&ie=utf-8&oe=utf-8&in=24401&cl=2&lm=-1&st=-1&cs=539019579%2C1503243384&os=&simid=&adpicid=0&ln=1998&fr=ala&fmq=1402900904181_R&fm=&ic=0&s=&se=1&sme=&tab=&width=&height=&face=0&ist=&jit=&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fe850352ac65c10381d59f42cb6119313b07e893c.jpg&fromurl=http%3A%2F%2Fwww.mzitu.com%2F9853&gsm=0");
        imageItems.add(imageItem);

        imageItem=new ImageItem();
        imageItem.info="SEC_KILL";
        imageItem.aspectRatio=ASPECT_RATIO_SECKILL;
        imageItem.horizontalPadding =24;
        imageItem.verticalPadding=24;
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%E7%BE%8E%E5%A5%B3&step_word=&hs=0&pn=14&spn=1&di=0&pi=21865400764&rn=1&tn=baiduimagedetail&is=&istype=2&ie=utf-8&oe=utf-8&in=24401&cl=2&lm=-1&st=-1&cs=1184802412%2C2862370394&os=&simid=&adpicid=0&ln=1998&fr=ala&fmq=1402900904181_R&fm=&ic=0&s=&se=1&sme=&tab=&width=&height=&face=0&ist=&jit=&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F472309f79052982280874be4d2ca7bcb0a46d465.jpg&fromurl=http%3A%2F%2Fwww.mmonly.cc%2Fmmtp%2Fqcmn%2F423.html&gsm=0");
        imageItems.add(imageItem);

        imageItem=new ImageItem();
        imageItem.info="New";
        imageItem.aspectRatio=ASPECT_RATIO_NEW;
        imageItem.horizontalPadding =24;
        imageItem.verticalPadding=24;
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%20%E7%BE%8E%E5%A5%B3&hs=0&pn=85&di=0&pi=9640829587&rn=1&tn=baiduimagedetail&is=0%2C42515&istype=&ie=utf-8&oe=utf-8&in=3354&cl=2&lm=-1&st=&cs=505762785%2C4034437740&os=0&adpicid=undefined&ln=33986&fr=&fmq=1378374347070_R&fm=&ic=0&s=0&se=&sme=&tab=&face=&ist=&jit=&statnum=girl&cg=girl&oriquery=&objurl=http%3A%2F%2Fd.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F54fbb2fb43166d22dc28839a442309f79052d265.jpg&gsm=");
        imageItems.add(imageItem);

        imageItem=new ImageItem();
        imageItem.info="LARGE_GROUP";
        imageItem.aspectRatio=ASPECT_RATIO_LARGE_GROUP;
        imageItem.horizontalPadding =24;
        imageItem.verticalPadding=24;
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%20%E7%BE%8E%E5%A5%B3&hs=0&pn=85&spn=2&di=0&pi=9640829587&rn=1&tn=baiduimagedetail&is=0%2C42515&istype=&ie=utf-8&oe=utf-8&in=3354&cl=2&lm=-1&st=&cs=505762785%2C4034437740&os=&simid=&adpicid=0&ln=33986&fr=&fmq=1378374347070_R&fm=&ic=0&s=0&se=&sme=&tab=&face=&ist=&jit=&statnum=girl&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9213b07eca806538b244391d95dda144ad348216.jpg&fromurl=http%3A%2F%2Fwww.mm369.net%2Fxiaohuahui%2F674.html&gsm=3c000000003c");
        imageItems.add(imageItem);

        imageItem=new ImageItem();
        imageItem.info="SMALL_GROUP";
        imageItem.aspectRatio=ASPECT_RATIO_SMALL_GROUP;
        imageItem.horizontalPadding =24;
        imageItem.verticalPadding=24;
        imageItem.itemPadding=24;
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%20%E7%BE%8E%E5%A5%B3&hs=0&pn=87&spn=1&di=0&pi=20845035020&rn=1&tn=baiduimagedetail&is=0%2C42515&istype=&ie=utf-8&oe=utf-8&in=3354&cl=2&lm=-1&st=&cs=2349694954%2C3368654805&os=&simid=&adpicid=0&ln=33986&fr=&fmq=1378374347070_R&fm=&ic=0&s=0&se=&sme=&tab=&face=&ist=&jit=&statnum=girl&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F43a7d933c895d143df33a4c977f082025baf0796.jpg&fromurl=http%3A%2F%2Fm.7160.com%2Fmeinv%2F19940%2F&gsm=3c000000003c");
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%20%E7%BE%8E%E5%A5%B3&hs=0&pn=90&spn=1&di=0&pi=23702313639&rn=1&tn=baiduimagedetail&is=0%2C42515&istype=&ie=utf-8&oe=utf-8&in=3354&cl=2&lm=-1&st=&cs=1827325703%2C2784741581&os=&simid=&adpicid=0&ln=33986&fr=&fmq=1378374347070_R&fm=&ic=0&s=0&se=&sme=&tab=&face=&ist=&jit=&statnum=girl&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb03533fa828ba61e4067399b4434970a314e59fd.jpg&fromurl=http%3A%2F%2Fwww.iyi8.com%2F2015%2Fmm_0510%2F2507.html&gsm=3c000000003c");
        imageItem.imageItems.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%9B%BE%E7%89%87%20%20%E7%BE%8E%E5%A5%B3&hs=0&pn=94&spn=2&di=0&pi=20884280445&rn=1&tn=baiduimagedetail&is=0%2C42515&istype=&ie=utf-8&oe=utf-8&in=3354&cl=2&lm=-1&st=&cs=2464707593%2C3169119076&os=&simid=&adpicid=0&ln=33986&fr=&fmq=1378374347070_R&fm=&ic=0&s=0&se=&sme=&tab=&face=&ist=&jit=&statnum=girl&cg=girl&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd833c895d143ad4bcddabf2c86025aafa50f0661.jpg&fromurl=http%3A%2F%2Fwww.mzitu.com%2F22949&gsm=3c000000003c");
        imageItems.add(imageItem);
        return imageItems;
    }
}
