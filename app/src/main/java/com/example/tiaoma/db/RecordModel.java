package com.example.tiaoma.db;

import android.arch.lifecycle.ViewModel;

/**
 * ViewModel防丢
 */
public class RecordModel extends ViewModel {

    private EditBoxsRecord record = new EditBoxsRecord();

    public EditBoxsRecord getRecord() {
        return record;
    }

    public void setWidgets(EditBoxsRecord record) {
        this.record = record;
    }
}
