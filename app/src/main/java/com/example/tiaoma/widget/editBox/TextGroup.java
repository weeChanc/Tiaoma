package com.example.tiaoma.widget.editBox;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tiaoma.bean.TextProperty;
import com.example.tiaoma.utils.DensityUtil;

/**
 * 插入TextView控件
 */
public class TextGroup extends FrameLayout {

    private Marker marker;
    //    private Scale scale;

    private HorizontalScrollView scrollView;

    public TextGroup(Context context) {
        super(context);
    }

    public TextGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        marker = new Marker(context);
        MarginLayoutParams lp = new MarginLayoutParams(3, DensityUtil.sp2px(getContext(), 22));
        marker.setLayoutParams(lp);
        addView(marker);
    }

    public void enableMarker() {
        addView(marker, 0);
    }

    public void disableMarker() {
        removeView(marker);
    }


    public void addText(TextProperty property) {

        if (scrollView != null)
            scrollView.smoothScrollTo(marker.getPosition(), 0);

        SpannableStringBuilder builder = new SpannableStringBuilder(property.getContent());
        TextView tv = new TextView(getContext());

        builder.setSpan(new ScaleXSpan(property.getFontScaleRatio()), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setLetterSpacing(property.getLetterSpacing());
        tv.setTextSize(property.getTextSize());
        tv.setText(builder);
        tv.setTextColor(Color.BLACK);

        if (property.getTypeface() != null)
            tv.setTypeface(Typeface.createFromAsset(getContext().getAssets(), property.getTypeface()));
//        tv.setIncludeFontPadding(false);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = property.getX();
        tv.setLayoutParams(lp);
        //获取属性,添加View
        addView(tv);

        //测量View指针后移
        tv.measure(0, 0);
        setMarkerPos(marker.getPosition() + tv.getMeasuredWidth());
    }

    public void removeView() {
        if (getChildCount() > 1)
            removeViewAt(getChildCount() - 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int wmax = 0;
        int hmax = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int w = child.getMeasuredWidth() + ((MarginLayoutParams) child.getLayoutParams()).leftMargin;
            int h = child.getMeasuredHeight() + ((MarginLayoutParams) child.getLayoutParams()).topMargin;
            wmax = wmax > w ? wmax : w;
            hmax = hmax > h ? hmax : h;
        }

        if (heightMode == MeasureSpec.AT_MOST && widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wmax, hmax);
            return;
        }

        if (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wmax, height);
            return;
        }

        if (heightMode == MeasureSpec.AT_MOST && widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, hmax);
            return;
        }

        if (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, height);
            return;
        }

        setMeasuredDimension(wmax, hmax);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            getChildAt(i).layout(layoutParams.leftMargin, layoutParams.topMargin
                    , layoutParams.leftMargin + child.getMeasuredWidth(), child.getMeasuredHeight() + layoutParams.topMargin);
        }
    }

    public void setMarkerPos(int markerPos) {
        MarginLayoutParams lp = (MarginLayoutParams) marker.getLayoutParams();
        lp.leftMargin = markerPos;
        marker.setLayoutParams(lp);
        marker.setPosition(markerPos);
    }

    public void combineScrollView(HorizontalScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public int getMarkerPosition() {
        return marker.getPosition();
    }


}




