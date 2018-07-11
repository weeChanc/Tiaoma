package com.example.tiaoma;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiaoma.bean.EditBoxsRecord;
import com.example.tiaoma.db.entity.EditBoxRecord;
import com.example.tiaoma.model.RecordModel;
import com.example.tiaoma.widget.Editor;
import com.example.tiaoma.widget.editBox.EditBox;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.widget.RelativeLayout.BELOW;

public class MainActivity extends AppCompatActivity {

    private PercentRelativeLayout container;
    private Editor editor;
    private RecordModel recordModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        recordModel = ViewModelProviders.of(this).get(RecordModel.class);

        container = findViewById(R.id.container);
        editor = findViewById(R.id.editor);
        show(recordModel.getRecord());
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void show(EditBoxsRecord record) {

        EventBus.getDefault().removeStickyEvent(record);

        if (record != recordModel.getRecord()) {
            recordModel.setRecord(record);
        }

        if (record.getCount() != 0) {

            int preId = View.generateViewId();
            EditBox preBox = new EditBox(this);
            preBox.setId(preId);
            container.addView(preBox);
            PercentRelativeLayout.LayoutParams preParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            PercentLayoutHelper.PercentLayoutInfo preInfo = preParams.getPercentLayoutInfo();
            preInfo.heightPercent = 0.2f;
            preParams.addRule(BELOW, R.id.editor);
            preBox.setLayoutParams(preParams);
            editor.addEditBox(preBox);

            for (int i = 1; i < record.getEditBoxsCount(); i++) {
                EditBox newBox = new EditBox(this);
                PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                PercentLayoutHelper.PercentLayoutInfo newInfo = params.getPercentLayoutInfo();
                newInfo.heightPercent = 0.2f;
                params.addRule(BELOW, preId);

                preId = View.generateViewId();
                newBox.setId(preId);
                newBox.setLayoutParams(params);
                editor.addEditBox(newBox);
                container.addView(newBox);
                preBox = newBox;
            }

        }

        editor.setRecord(record);
        editor.reFreshByRecord();
        container.requestLayout();
    }
}



