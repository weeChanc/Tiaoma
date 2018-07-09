package com.example.tiaoma.db;

import java.util.ArrayList;
import java.util.List;

/**
 * EditBoxs以及其内部属性集合记录
 */
public class EditBoxsRecord {
    private int count;
    private List<List<TextProperty>> properties = new ArrayList<>();

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

    public void addEditBox(List<TextProperty> properties) {
        count ++;
        this.properties.add(properties);
    }
}
