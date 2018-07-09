package com.example.tiaoma.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.example.tiaoma.R;
import com.example.tiaoma.db.RecordModel;
import com.example.tiaoma.db.TextProperty;
import com.example.tiaoma.db.EditBoxsRecord;
import com.example.tiaoma.utils.DigitUtils;
import com.example.tiaoma.widget.editBox.EditBox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 产生Ruler插入所需的字符串
 */
public class Editor extends FrameLayout {

    private ImageView save;
    private ImageView send;
    private ImageView fontH;
    private ImageView fontW;
    private ImageView fontGap;
    private ImageView fontStyle;
    private ImageView time;
    private ImageView undo;
    private ImageView addText;
    private ImageView addPicture;

    private Button first;
    private Button gb;
    private Button e0;
    private Button e1;
    private Button e2;
    private Button produceTime;
    private Button validateTime;
    private Button batchNumber;

    private TextProperty property = new TextProperty();
    private EditBox activeBox;
    private int activeBoxPosition;
    private List<EditBox> editBoxes = new ArrayList<>(4);
    private EditBoxsRecord record;




    public Editor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.editor, this);
        findView();
        setListener();
    }

    /**
     * 旋转屏幕恢复设置
     * 要在ONCREATE后调用
     */
    public void init() {

        if (getContext() instanceof FragmentActivity)
            record = ViewModelProviders.of((FragmentActivity) getContext()).get(RecordModel.class).getRecord();

        for (int i = 0; i < record.getEditBoxsCount(); i++) {
            activeBox(editBoxes.get(i));
            for (TextProperty widgetProperty : record.getWidgetPropertiesAt(i)) {
                activeBox.addText(widgetProperty);
            }
        }
    }

    public void addEditBox(EditBox editBox) {

        if (editBoxes.size() == 0) {
            editBox.setActive(true);
        }else{
            //其他box要取消
            editBox.disableMarker();
        }

        editBoxes.add(editBox);
        editBox.setOnClickListener(v -> {
            activeBox(editBox);
        });

        editBox.setLongClickListener((x, y) -> {
            editBox.setMarkerPos((int) x);
        });

    }

    private void findView() {
        save = findViewById(R.id.save);
        send = findViewById(R.id.send);
        fontH = findViewById(R.id.fontH);
        fontW = findViewById(R.id.fontW);
        fontGap = findViewById(R.id.fontGap);
        fontStyle = findViewById(R.id.fontStyle);
        time = findViewById(R.id.time);
        undo = findViewById(R.id.undo);
        addText = findViewById(R.id.addText);
        addPicture = findViewById(R.id.addPicture);
        first = findViewById(R.id.first);
        gb = findViewById(R.id.gb);
        e0 = findViewById(R.id.e0);
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        produceTime = findViewById(R.id.produceTime);
        validateTime = findViewById(R.id.validateTime);
        batchNumber = findViewById(R.id.batchNumber);

    }

    private void setListener() {

        fontW.setOnClickListener((v) -> {
            new MaterialDialog.Builder(getContext())
                    .title("请输入字体拉伸倍数(可带小数)")
                    .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .input(null, property.getFontScaleRatio() + "", (dialog, input) -> {
                        if (DigitUtils.isDigit(input)) {
                            property.setFontScaleRatio(Float.parseFloat(input.toString()));
                        } else {
                            Toast.makeText(getContext(), "输入有误,重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        });

        fontGap.setOnClickListener((v) -> {
            new MaterialDialog.Builder(getContext())
                    .title("请输入字体间隙")
                    .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .input(null, property.getLetterSpacing() + "", (dialog, input) -> {
                        if (DigitUtils.isDigit(input)) {
                            property.setLetterSpacing(Float.parseFloat(input.toString()));
                        } else {
                            Toast.makeText(getContext(), "输入有误,重新输入", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .positiveText("确定")
                    .negativeText("取消")
                    .show();
        });

        addText.setOnClickListener((v) -> {
            new MaterialDialog.Builder(getContext())
                    .title("请输入文字")
                    .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .input("正文内容", null, (dialog, input) -> {
                        property.setContent(input.toString());
                        addText(property);
                    }).show();
        });

        time.setOnClickListener((v) -> {
            new MaterialDialog.Builder(getContext())
                    .title("选择插入时间变量的格式")
                    .items(new String[]{"yyyy.mm.dd", "hh:mm:ss", "yyyy/mm/dd hh:mm:ss", "yyyy.mm.dd hh:mm", "hh:mm"})
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            property.setContent(new SimpleDateFormat(text.toString(),Locale.CHINA).format(new Date(System.currentTimeMillis())));
                            addText(property);
                            return true;
                        }
                    })
                    .positiveText("确定")
                    .negativeText("取消")
                    .show();
        });


        first.setOnClickListener((v) -> {
            property.setContent("甲醛释放量符合国家标注");
            addText(property);
        });

        gb.setOnClickListener(v -> {
            property.setContent("GB/T 9846-2015 (普通胶合板国家标准)");
            addText(property);
        });

        e0.setOnClickListener(v -> {
            property.setContent("EO≤0.5mg/L(可直接用于室内)");
            addText(property);
        });
        e1.setOnClickListener(v -> {
            property.setContent("E1≤1.5mg/L(可直接用于室内)");
            addText(property);
        });
        e2.setOnClickListener(v -> {
            property.setContent("E2≤5.0mg/L(须饰面处理后可允许用于室内)");
            addText(property);
        });

        produceTime.setOnClickListener(v -> {
            property.setContent("生产日期: ");
            addText(property);
        });

        validateTime.setOnClickListener(v -> {
            property.setContent("有效期至: ");
            addText(property);
        });

        batchNumber.setOnClickListener(v -> {
            property.setContent("批  号: ");
            addText(property);
        });

        undo.setOnClickListener(v -> {

            record.popWidgetPropertyesAt(activeBoxPosition);
            getActiveEditBox().undo();

        });

        save.setOnClickListener(v -> {
            try {
                new File(
                        Environment
                                .getExternalStorageDirectory()
                                .getPath() + "/output.png").createNewFile();


                getActiveEditBox().getTextBitmap().compress(Bitmap.CompressFormat.PNG, 100,
                        new FileOutputStream(new File(
                                Environment
                                        .getExternalStorageDirectory()
                                        .getPath() + "/output.png")));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        send.setOnClickListener(v -> {
            Bitmap bitmap = getActiveEditBox().getTextBitmap();
            for (int i = 0; i < bitmap.getRowBytes(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {
                    Log.e("Editor",i+ " " + j );
                }
            }
        });

    }

    private EditBox getActiveEditBox() {

        if (activeBox == null) {
            for (int i = 0; i < editBoxes.size(); i++) {
                if (editBoxes.get(i).isActive()) {
                    activeBox = editBoxes.get(i);
                    activeBoxPosition = i;
                    return activeBox;
                }
            }
        }

        return activeBox;
    }

    private void activeBox(EditBox box) {
        getActiveEditBox().disableMarker();
        getActiveEditBox().setActive(false);
        box.setActive(true);
        box.enableMarker();
        for (int i = 0; i < editBoxes.size(); i++) {
            if (editBoxes.get(i).isActive()) {
                activeBox = editBoxes.get(i);
                activeBoxPosition = i;
            }
        }

    }

    private void addText(TextProperty property) {
        EditBox box = getActiveEditBox();

        if (box == null) {
            ToastUtils.showShort("请选择输入框");
            return;
        }

        property.setX(box.getMarkerPos());
        box.addText(property);

        while (record.getEditBoxsCount() < editBoxes.size()) {
            //初始化记录数量
            record.addEditBox(new ArrayList<>());
        }
        record.addWidgetPropertyesAt(activeBoxPosition, property.clone());


    }

}
