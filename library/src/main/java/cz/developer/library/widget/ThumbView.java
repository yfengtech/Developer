package cz.developer.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cz on 11/9/16.
 */

public class ThumbView extends View {
    private final Paint paint;
    private float aspectRatio;
    private int itemPadding;
    private int itemCount;

    public ThumbView(Context context) {
        this(context,null,0);
    }

    public ThumbView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemCount=-1;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setAspectRatio(float ratio){
        this.aspectRatio=ratio;
        requestLayout();
    }

    public void setItemPadding(int padding){
        this.itemPadding=padding;
        invalidate();
    }

    public void setItemCount(int itemCount){
        this.itemCount=itemCount;
        requestLayout();
    }

    public void setColor(int color){
        this.paint.setColor(color);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int itemWidth = (measuredWidth - getPaddingLeft() - getPaddingRight() - (itemCount - 1) * itemPadding) / itemCount;
        int height= (int) (itemWidth*aspectRatio)+getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(measuredWidth,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int itemWidth = (width - getPaddingLeft() - getPaddingRight() - (itemCount - 1) * itemPadding) / itemCount;

        int left=getPaddingLeft();
        for(int i=0;i<itemCount;i++){
            canvas.drawRect(left,paddingTop,left+itemWidth,height-paddingBottom,paint);
            left+=itemWidth+itemPadding;
        }
    }
}
