package com.nloops.students.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.nloops.students.data.dao.SubjectDAO;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentAbsJoin;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import timber.log.Timber;

/**
 * This class will map the whole database object, it will be a singleton that's mean it will be only
 * one instance of this object during the runtime and the reason is to block any conflicts.
 */
@Database(entities = {SubjectEntity.class, ClassEntity.class, StudentEntity.class,
    AbsenteeEntity.class,
    StudentAbsJoin.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

  // Declare local variables
  private static final String TAG = AppDatabase.class.getSimpleName();
  private static final Object LOCK = new Object();
  private static final String DATABASE_NAME = "studens_tracker";
  private static AppDatabase sInstance;

  // getInstance method : if this first call it will return new instance
  // instead it will return the current one.
  public static AppDatabase getInstance(Context context) {
    if (sInstance == null) {
      synchronized (LOCK) {
        Timber.d("Creating New Database Instance");
        sInstance = Room.databaseBuilder(context.getApplicationContext(),
            AppDatabase.class, AppDatabase.DATABASE_NAME)
            .build();
      }
    }
    Timber.d("Getting the database instance");
    return sInstance;
  }

  // Table DAOs
  public abstract SubjectDAO subjectDAO();

}
