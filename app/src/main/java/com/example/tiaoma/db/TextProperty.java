package com.example.tiaoma.db;

public class TextProperty implements Cloneable{

    //内容
    private String content;
    //缩放比例
    private float fontScaleRatio = 1.0f;
    //字间距
    private float letterSpacing = 0.0f;
    //字体大小
    private float textSize = 22;
    //leftMargin - 偏移值
    private int x;

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getFontScaleRatio() {
        return fontScaleRatio;
    }

    public void setFontScaleRatio(float fontScaleRatio) {
        this.fontScaleRatio = fontScaleRatio;
    }

    public float getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "偏移值="+x+", 内容="+content;
    }

    @Override
    public TextProperty clone() {
        try{
            return (TextProperty) super.clone();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
