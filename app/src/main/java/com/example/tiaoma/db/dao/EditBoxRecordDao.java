package com.example.tiaoma.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.tiaoma.bean.EditBoxsRecord;
import com.example.tiaoma.db.entity.EditBoxRecord;

import java.util.List;

@Dao
public interface EditBoxRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(EditBoxRecord record);

    @Delete
    public void delete(EditBoxRecord record);

    @Query("SELECT * FROM editbox_record")
    public List<EditBoxRecord> queryAll();


}
