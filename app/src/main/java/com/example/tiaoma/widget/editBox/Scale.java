package com.example.tiaoma.widget.editBox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Scale extends View {

    private Paint mPaint;
    private float scale = 10;
    private float longLineLength = 30f;
    private float shortLineLength = longLineLength * 4 / 9;
    private int length = 10000;
    private float[] longLines;
    private float[] shortLines;
    private float gap = 20f;

    public Scale(Context context) {
        super(context);
        init();
    }

    public Scale(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setTextAlign(Paint.Align.CENTER);

    }

    private void initLines() {

        List<Float> longLines = new ArrayList<>();
        List<Float> shortLines = new ArrayList<>();

        for (float i = 0; i <= length / scale / 10; i++) {
            longLines.add(i * scale * 10);
            longLines.add(0f);
            longLines.add(i * scale * 10);
            longLines.add(longLineLength);
        }

        for (float i = 0; i <= length / scale; i++) {
            shortLines.add(i * scale);
            shortLines.add(0f);
            shortLines.add(i * scale);
            shortLines.add(shortLineLength);
        }

        this.longLines = new float[longLines.size()];
        this.shortLines = new float[shortLines.size()];

        for (int i = 0; i < longLines.size(); i++) {
            this.longLines[i] = longLines.get(i);
        }

        for (int i = 0; i < shortLines.size(); i++) {
            this.shortLines[i] = shortLines.get(i);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(3);
        canvas.drawLines(longLines, mPaint);
        mPaint.setStrokeWidth(2);
        canvas.drawLines(shortLines, mPaint);

        for (int i = 0; i < longLines.length; i += 4) {
            canvas.drawText("" + longLines[i], longLines[i], longLineLength + gap, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        initLines();


        if (heightMode == MeasureSpec.AT_MOST && widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(length, (int)(longLineLength+mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().ascent + gap)  );
            return;
        }

        if (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(length, height);
            return;
        }

        if (heightMode == MeasureSpec.AT_MOST && widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, (int)(longLineLength+mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().ascent + gap));
            return;
        }

        if (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, height);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


}
