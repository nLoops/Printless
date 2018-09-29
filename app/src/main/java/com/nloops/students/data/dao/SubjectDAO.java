package com.nloops.students.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

/**
 * This interface will holds {@link SubjectEntity} Database operations like query, insert, update,
 * delete data.
 */

@Dao
public interface SubjectDAO {

  @Query("SELECT * from subjects")
  LiveData<List<SubjectEntity>> loadAllSubjects();

  @Insert
  void insertSubject(SubjectEntity subjectEntity);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void updateSubject(SubjectEntity subjectEntity);

  @Delete
  void deleteSubject(SubjectEntity subjectEntity);


}
