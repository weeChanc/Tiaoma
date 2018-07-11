package com.example.tiaoma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.tiaoma.MainActivity;
import com.example.tiaoma.R;
import com.example.tiaoma.bean.EditBoxsRecord;
import com.example.tiaoma.db.entity.EditBoxRecord;
import com.example.tiaoma.widget.editBox.EditBox;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Entrance extends AppCompatActivity {

    private List<EditBoxRecord> records;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        Button button = findViewById(R.id.button);

        ImageView single = findViewById(R.id.single);
        ImageView two_single = findViewById(R.id.two_single);
        ImageView three_single = findViewById(R.id.three_single);

        single.setOnClickListener(v ->{
            navigate(1);
        });

        two_single.setOnClickListener(v -> {
            navigate(2);
        });

        three_single.setOnClickListener(v ->{
            navigate(3);
        });


        button.setOnClickListener(v -> {
            App.getApp().getExecutors().diskIO().execute(() -> {
                records = App.getApp().getDb().editBoxRecordDao().queryAll();
                names = new ArrayList<>();
                for (int i = 0; i < records.size(); i++) {
                    names.add(records.get(i).getName());
                }

                App.getApp().getExecutors().mainThread().execute(() -> {
                    new MaterialDialog.Builder(this)
                            .title("记录列表")
                            .items(names)
                            .itemsCallback((dialog, view, which, text) -> {
                                Log.e("Entrance",records.get(which).toString());
                                EventBus.getDefault().postSticky(records.get(which).toEditBoxsRecord());
                                Intent i = new Intent(this, MainActivity.class);
                                startActivity(i);
                            })
                            .show();
                });
            });
        });
    }

    private void navigate(int count){
        Intent i = new Intent(this, MainActivity.class);
        EditBoxsRecord record = new EditBoxsRecord(count);
        EventBus.getDefault().postSticky(record);
        startActivity(i);
    }
}
