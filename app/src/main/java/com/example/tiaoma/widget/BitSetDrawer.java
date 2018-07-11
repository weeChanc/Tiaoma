package com.example.tiaoma.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.BitSet;

public class BitSetDrawer extends View {

    private BitSet mBitSet;
    private Paint mPaint;

    public BitSetDrawer(Context context, BitSet bitSet) {
        super(context);
        this.mBitSet = bitSet;
    }

    public BitSetDrawer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getBitSet(BitSet bitSet){
        this.mBitSet = bitSet;
        EventBus.getDefault().removeStickyEvent(bitSet);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitSet == null) return;

        for (int i = 0 ; i < (mBitSet.length()-1)/320 ; i++){
            for(int j = 0 ; j < 320 ; j++){
                if(mBitSet.get(i*320+j)){
                    canvas.drawPoint((float)i,(float)j,mPaint);
                }
            }

        }

    }

}
