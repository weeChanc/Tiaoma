package com.example.tiaoma.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.tiaoma.db.dao.EditBoxRecordDao;
import com.example.tiaoma.db.entity.EditBoxRecord;

@Database(entities = {EditBoxRecord.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EditBoxRecordDao editBoxRecordDao();
}
