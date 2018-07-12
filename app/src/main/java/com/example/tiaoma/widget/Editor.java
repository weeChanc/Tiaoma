package com.example.tiaoma.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import com.example.tiaoma.App;
import com.example.tiaoma.R;
import com.example.tiaoma.bean.EditBoxsRecord;
import com.example.tiaoma.bean.TextProperty;
import com.example.tiaoma.db.entity.EditBoxRecord;
import com.example.tiaoma.utils.DigitUtils;
import com.example.tiaoma.widget.editBox.EditBox;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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

    private Map<String,String> typeFaceMap = new HashMap<>();


    public Editor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.editor, this);
        findView();
        setListener();
        initMap();
    }

    private void initMap(){
        typeFaceMap.put("华文行楷","xingkai.ttf");
        typeFaceMap.put("华文琥珀","hupo.ttf");
        typeFaceMap.put("方正繁宋体","songfanti.ttf");
        typeFaceMap.put("华文彩云","caiyun.ttf");
        typeFaceMap.put("方正繁楷体","kaifanti.ttf");
        typeFaceMap.put("黑体","simhei.ttf");
        typeFaceMap.put("楷体","simkai.ttf");
        typeFaceMap.put("隶书","simli.ttf");
        typeFaceMap.put("宋体","simsun.ttf");
        typeFaceMap.put("幼圆","simyou.ttf");
        typeFaceMap.put("5*8点阵","en58.ttf");
        typeFaceMap.put("微软雅黑","yahei.ttf");
        typeFaceMap.put("方正繁黑体","heifanti.ttf");
        typeFaceMap.put("16*16点阵","fangzheng16.ttf");
    }


    public void setRecord(EditBoxsRecord record) {
        this.record = record;
        Log.e("Editor", editBoxes.size() + "");
        this.record.match(editBoxes.size());
    }

    public void addEditBox(EditBox editBox) {
        if (editBoxes.size() == 0) {
            editBox.setActive(true);
        } else {
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
                            property.setContent(new SimpleDateFormat(text.toString(), Locale.CHINA).format(new Date(System.currentTimeMillis())));
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

            new MaterialDialog.Builder(getContext())
                    .title("请命名")
                    .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .input("标题", null, (dialog, input) -> {
                        EditBoxRecord record = this.record.toEditBoxRecord();
                        record.setName(input.toString());
                        App.getApp().getExecutors().diskIO().execute(() -> {
                            App.getApp().getDb().editBoxRecordDao().insert(record);
                            App.getApp().getExecutors().mainThread().execute(() -> {
                                ToastUtils.showShort("保存成功");
                            });
                        });
                    }).show();

        });

        send.setOnClickListener(v -> {
            Bitmap bitmap = getActiveEditBox().getTextBitmap();
            BitSet bitSet = new BitSet();

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
            bitSet.set(320 * (i - 1) + 320, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = bitSet.toByteArray();
            baos.write(bytes, 0, bytes.length - 1);
        });

        fontStyle.setOnClickListener(v -> {
            new MaterialDialog.Builder(getContext())
                    .title("设置文本字体")
                    .items(new String[]{"宋体", "黑体", "楷体", "5*8点阵", "16*16点阵", "微软雅黑",
                            "隶书", "幼圆", "华文彩云", "华文琥珀", "华文行楷", "方正繁黑体", "方正繁楷体",
                            "方正繁宋体"})
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            property.setTypeface(typeFaceMap.get(text.toString()));
                            return true;
                        }
                    })
                    .positiveText("确定")
                    .negativeText("取消")
                    .show();
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
        record.addWidgetPropertyesAt(activeBoxPosition, property.clone());
    }

    /**
     * 根据记录给每个box加入字体
     */
    public void refreshByRecord() {
        for (int i = 0; i < this.record.getEditBoxsCount(); i++) {
            activeBox(editBoxes.get(i));
            for (TextProperty widgetProperty : this.record.getWidgetPropertiesAt(i)) {
                activeBox.addText(widgetProperty);
            }
        }
    }
}
