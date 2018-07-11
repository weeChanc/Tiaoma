package com.example.tiaoma.widget.editBox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Marker extends View {

    private Paint mPaint = new Paint();
    private int position = 0;


    public Marker(Context context) {
        super(context);
        init();
    }

    public Marker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(Color.BLACK);

        new Runnable() {
            @Override
            public void run() {

                if (mPaint.getColor() == Color.TRANSPARENT) {
                    mPaint.setColor(Color.BLACK);
                } else {
                    mPaint.setColor(Color.TRANSPARENT);
                }

                invalidate();
                postDelayed(this, 800);
            }
        }.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}