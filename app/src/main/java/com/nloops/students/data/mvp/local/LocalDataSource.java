package com.nloops.students.data.mvp.local;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.AppExecutors;
import com.nloops.students.data.mvp.StructureDataSource;
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

  // ref of Context
  private Activity mActivity;

  private LocalDataSource(@NonNull Context context, @NonNull Activity activity) {
    this.mActivity = activity;
    mDB = AppDatabase.getInstance(context.getApplicationContext());
  }

  public static LocalDataSource getInstance(@NonNull Context context, @NonNull Activity activity) {
    if (INSTANCE == null) {
      synchronized (LocalDataSource.class) {
        if (INSTANCE == null) {
          INSTANCE = new LocalDataSource(context, activity);
        }
      }
    }
    return INSTANCE;
  }

  @Override
  public void getSubjects(@NonNull final LoadSubjectsCallBack callBack) {

    LiveData<List<SubjectEntity>> subjects = mDB.subjectDAO().loadAllSubjects();
    subjects.observe((LifecycleOwner) mActivity, new Observer<List<SubjectEntity>>() {
      @Override
      public void onChanged(@Nullable List<SubjectEntity> subjectEntities) {
        if (subjectEntities.isEmpty()) {
          callBack.onSubjectDataNotAvailable();
        } else {
          callBack.onSubjectsLoaded(subjectEntities);
        }
      }
    });

  }

  @Override
  public void getSubject(@NonNull int subjectID,
      @NonNull final LoadSingleSubjectCallBack callBack) {
    LiveData<SubjectEntity> subejct = mDB.subjectDAO().loadSinglSubject(subjectID);
    subejct.observe((LifecycleOwner) mActivity, new Observer<SubjectEntity>() {
      @Override
      public void onChanged(@Nullable SubjectEntity subjectEntity) {
        if (subjectEntity == null) {
          callBack.onSubjectDataNotAvailable();
        } else {
          callBack.onSubjectLoaded(subjectEntity);
        }
      }
    });
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
}
