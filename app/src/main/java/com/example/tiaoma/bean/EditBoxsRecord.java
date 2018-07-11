package com.example.tiaoma.bean;

import android.arch.persistence.room.Entity;

import com.example.tiaoma.db.entity.EditBoxRecord;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * EditBoxs以及其内部属性集合记录
 */

public class EditBoxsRecord {
    private int count;
    private List<List<TextProperty>> properties = new ArrayList<>();

    public EditBoxsRecord(int count, List<List<TextProperty>> properties) {
        this.count = count;
        this.properties = properties;
    }

    public EditBoxsRecord(int count) {
        this.count = count;
        match(count);
    }

    public void match(int count){
        this.count = count;
        for(int i = properties.size() ; i < count ; i++){
            properties.add(new ArrayList<>());
        }
    }

    public int getEditBoxsCount() {
        return count;
    }

    public List<TextProperty> getWidgetPropertiesAt(int position) {
        return properties.get(position);
    }

    public boolean addWidgetPropertyesAt(int position,TextProperty property){
        return properties.get(position).add(property);
    }

    public TextProperty popWidgetPropertyesAt(int position){
        List<TextProperty> properties = getWidgetPropertiesAt(position);
        if(properties.size() > 1){
            return  properties.remove(properties.size()-1);
        }
        return null;
    }


    public EditBoxRecord toEditBoxRecord(){
        EditBoxRecord record = new EditBoxRecord();
        record.setContent(new Gson().toJson(properties));
        record.setCount(count);
        return record;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<List<TextProperty>> getProperties() {
        return properties;
    }

    public void setProperties(List<List<TextProperty>> properties) {
        this.properties = properties;
    }
}
