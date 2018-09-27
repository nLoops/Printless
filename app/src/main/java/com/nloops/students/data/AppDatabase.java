package com.nloops.students.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;
import com.nloops.students.data.dao.SubjectDAO;
import com.nloops.students.data.tables.SubjectEntity;

/**
 * This class will map the whole database object, it will be a singleton that's mean it will be only
 * one instance of this object during the runtime and the reason is to block any conflicts.
 */
@Database(entities = {SubjectEntity.class}, version = 1, exportSchema = false)
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
        Log.d(TAG, "Creating New Database Instance");
        sInstance = Room.databaseBuilder(context.getApplicationContext(),
            AppDatabase.class, AppDatabase.DATABASE_NAME)
            .build();
      }
    }
    Log.d(TAG, "Getting the database instance");
    return sInstance;
  }

  // Table DAOs
  public abstract SubjectDAO subjectDAO();

}
