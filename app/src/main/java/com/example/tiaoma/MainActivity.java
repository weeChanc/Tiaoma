package com.example.tiaoma;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tiaoma.widget.Editor;
import com.example.tiaoma.widget.editBox.EditBox;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Editor editor = findViewById(R.id.editor);
        EditBox editBox = findViewById(R.id.editbox);
        EditBox editBox2 = findViewById(R.id.editbox2);
        EditBox editBox3 = findViewById(R.id.editbox3);

        editor.addEditBox(editBox);
        editor.addEditBox(editBox2);
        editor.addEditBox(editBox3);
        editor.init();

//
//        editor.setOnTextProduceListener(property -> {
//            ruler.addText(property);
//        });

    }
}



