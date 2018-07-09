package com.example.tiaoma.widget.editBox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.example.tiaoma.R;
import com.example.tiaoma.db.TextProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 包含刻度尺和TextViewGroup
 */
public class EditBox extends HorizontalScrollView {

    //即是否获取了焦点
    private boolean active;
    private Scale scale;
    private TextGroup textGroup;
    private float touchX;
    private float touchY;

    public EditBox(Context context) {
        super(context);
    }

    public EditBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ruler, this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scale = findViewById(R.id.scale);
        textGroup = findViewById(R.id.textGroup);
        textGroup.combineScrollView(this);

        setOnTouchListener((v, e) -> {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                touchX = e.getX();
                touchY = e.getY();
                performClick();
            }

            return detector.onTouchEvent(e);

        });
    }

    public void enableMarker() {
        textGroup.enableMarker();
    }

    public void disableMarker() {
        textGroup.disableMarker();
    }

    public Bitmap getTextBitmap() {

        disableMarker();
        if (textGroup.getMeasuredHeight() > 0) {
            textGroup.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(textGroup.getWidth(), textGroup.getHeight(),
                    Bitmap.Config.ALPHA_8);
            Canvas c = new Canvas(b);
            textGroup.layout(0, 0, textGroup.getWidth(), textGroup.getWidth());
            textGroup.draw(c);
            requestLayout();

            float s = 300.0f/b.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(s,s);
            return Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true);
        }


        return null;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }


    com.example.tiaoma.widget.OnLongClickListener listener;

    public void setLongClickListener(com.example.tiaoma.widget.OnLongClickListener listener) {
        this.listener = listener;
    }

    public void setMarkerPos(int pos) {
        textGroup.setMarkerPos(pos);
    }

    public int getMarkerPos() {
        return textGroup.getMarkerPosition();
    }

    public void addText(TextProperty property) {
        textGroup.addText(property);
    }

    public void undo() {
        textGroup.removeView();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    GestureDetector detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (listener != null) {
                listener.onLongClick(e.getX() + getScrollX(), e.getY());
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });


}
