package com.example.tiaoma.model;

import android.arch.lifecycle.ViewModel;

import com.example.tiaoma.bean.EditBoxsRecord;

/**
 * ViewModel防丢
 */
public class RecordModel extends ViewModel {

    private EditBoxsRecord record = new EditBoxsRecord(0);

    public EditBoxsRecord getRecord() {
        return record;
    }

    public void setRecord(EditBoxsRecord record) {
        this.record = record;
    }
}
