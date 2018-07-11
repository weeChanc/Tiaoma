package com.example.tiaoma.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.tiaoma.bean.EditBoxsRecord;
import com.example.tiaoma.bean.TextProperty;
import com.example.tiaoma.db.dao.EditBoxRecordDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "editbox_record")
public class EditBoxRecord {

    public static final int TYPE_COMBINE = 1;
    public static final int TYPE_SINGLE = 0;

    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String name;
    private int count;
    //List<List<EditBoxsRecord>>转json存数据库
    private String content;

    public EditBoxRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EditBoxsRecord toEditBoxsRecord(){
        Type typeToken = new TypeToken<List<List<TextProperty>>>(){}.getType();
        if(content!=null){
            return new EditBoxsRecord(count,new Gson().fromJson(content,typeToken));
        }

        return new EditBoxsRecord(count);
    }

    @Override
    public String toString() {
        return "EditBoxRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", content='" + content + '\'' +
                '}';
    }
}
