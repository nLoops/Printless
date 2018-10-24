package com.nloops.students.data.mvp.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.AppExecutors;
import com.nloops.students.data.mvp.StructureDataSource;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

/**
 * This Class will implement {@link StructureDataSource} to feed functions with local operations
 * that required from local DB This class will be a singleton to prevent multi constructing
 */
public class LocalDataSource implements StructureDataSource {

  // this object will holds one single call of this class
  private static volatile LocalDataSource INSTANCE;

  // ref of AppDatabase
  private AppDatabase mDB;


  private LocalDataSource(@NonNull Context context) {
    mDB = AppDatabase.getInstance(context.getApplicationContext());
  }

  public static LocalDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      synchronized (LocalDataSource.class) {
        if (INSTANCE == null) {
          INSTANCE = new LocalDataSource(context);
        }
      }
    }
    return INSTANCE;
  }

  @Override
  public void getSubjects(@NonNull final LoadSubjectsCallBack callBack) {

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        final List<SubjectEntity> subjects = mDB.subjectDAO().loadAllSubjects();
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
          @Override
          public void run() {
            if (subjects.isEmpty()) {
              // This will be called if the table is new or just empty.
              callBack.onSubjectDataNotAvailable();
            } else {
              callBack.onSubjectsLoaded(subjects);
            }
          }
        });
      }
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }


  @Override
  public void getSubject(@NonNull final int subjectID,
      @NonNull final LoadSingleSubjectCallBack callBack) {

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        final SubjectEntity subject = mDB.subjectDAO().loadSingleSubject(subjectID);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
          @Override
          public void run() {
            if (subject != null) {
              callBack.onSubjectLoaded(subject);
            } else {
              callBack.onSubjectDataNotAvailable();
            }
          }
        });
      }
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void insertSubject(@NonNull final SubjectEntity subject) {
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().insertSubject(subject);
      }
    });
  }

  @Override
  public void updateSubject(@NonNull final SubjectEntity subject) {
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().updateSubject(subject);
      }
    });
  }

  @Override
  public void deleteSubject(@NonNull final SubjectEntity subject) {
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().deleteSubject(subject);
      }
    });
  }

  // Logic for ClassEntity.

  @Override
  public void getClasses(@NonNull final int subjectID,
      @NonNull final LoadClassesCallBack callBack) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        final List<ClassEntity> classesData = mDB.subjectDAO().loadAllClasses(subjectID);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
          @Override
          public void run() {
            if (classesData.isEmpty()) {
              // This will be called if the table is new or just empty.
              callBack.onClassesDataNotAvailable();
            } else {
              callBack.onClassesLoaded(classesData);
            }
          }
        });
      }
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getClass(@NonNull final int classID,
      @NonNull final LoadSingleClassCallBack callBack) {

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        final ClassEntity classEntity = mDB.subjectDAO().loadSingleClass(classID);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
          @Override
          public void run() {
            if (classEntity != null) {
              callBack.onClassDataLoaded(classEntity);
            } else {
              callBack.onClassDataNotAvailable();
            }
          }
        });
      }
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void insertClass(@NonNull final ClassEntity classEntity) {
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().insertClass(classEntity);
      }
    });
  }

  @Override
  public void updateClass(@NonNull final ClassEntity classEntity) {
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().updateClass(classEntity);
      }
    });
  }

  @Override
  public void deleteClass(@NonNull final ClassEntity classEntity) {

    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().deleteClass(classEntity);
      }
    });
  }

}
