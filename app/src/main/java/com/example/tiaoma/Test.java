package com.example.tiaoma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.tiaoma.utils.DensityUtil;
import com.example.tiaoma.utils.DigitUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        FrameLayout f =  findViewById(R.id.frame);
//
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.leftMargin = DensityUtil.dp2px(this,100);
//        TextView tv = new TextView(this);
//        tv.setText("1234556");
//        tv.setTextSize(DensityUtil.dp2px(this,22));
//        tv.setLayoutParams(layoutParams);
//        f.addView(tv);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(Environment.getExternalStorageDirectory().getPath()+"/output.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitSet bitSet = new BitSet();

        long startTime = System.currentTimeMillis();

        Log.e("Editor","bitmap row = " + bitmap.getWidth() + " bitmap height " + bitmap.getHeight());

        int i = 0;
        for (; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < 320; j++) {

                if (j >= 300) {
                    bitSet.set(j + 320 * i, false);
                    continue;
                }

                if (Color.alpha(bitmap.getPixel(i, j)) == 0) {
                    bitSet.set(j + 320 * i, false);
                } else {
                    bitSet.set(j + 320 * i, true);
                }
            }
        }
        bitSet.set(320 * (i-1) + 320 , true);

        EventBus.getDefault().postSticky(bitSet);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = bitSet.toByteArray();
        Log.e("Test",bytes.length+"");
        try {
            baos.write(bytes,0,bytes.length-1);
            baos.writeTo(new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/output")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
