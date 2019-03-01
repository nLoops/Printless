package com.nloops.students.data.mvp.local;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.nloops.students.cloud.CloudModel;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.AppExecutors;
import com.nloops.students.data.dao.SubjectDAO;
import com.nloops.students.data.mvp.StructureDataSource;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.utils.UtilsMethods;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    Runnable runnable = () -> {
      final List<SubjectEntity> subjects = mDB.subjectDAO()
          .loadAllSubjects(UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (subjects.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onSubjectDataNotAvailable();
        } else {
          callBack.onSubjectsLoaded(subjects);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }


  @Override
  public void getSubject(final int subjectID,
      @NonNull final LoadSingleSubjectCallBack callBack) {

    Runnable runnable = () -> {
      final SubjectEntity subject = mDB.subjectDAO().loadSingleSubject(subjectID);
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (subject != null) {
          callBack.onSubjectLoaded(subject);
        } else {
          callBack.onSubjectDataNotAvailable();
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void insertSubject(@NonNull final SubjectEntity subject) {
    AppExecutors.getInstance().diskIO().execute(() -> mDB.subjectDAO().insertSubject(subject));
  }

  @Override
  public void updateSubject(@NonNull final SubjectEntity subject) {
    AppExecutors.getInstance().diskIO().execute(() -> mDB.subjectDAO().updateSubject(subject));
  }

  @Override
  public void deleteSubject(@NonNull final SubjectEntity subject) {
    AppExecutors.getInstance().diskIO().execute(() -> mDB.subjectDAO().deleteSubject(subject));
  }

  // Logic for ClassEntity.

  @Override
  public void getClasses(final int subjectID,
      @NonNull final LoadClassesCallBack callBack) {
    Runnable runnable = () -> {
      final List<ClassEntity> classesData = mDB.subjectDAO()
          .loadAllClasses(subjectID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (classesData.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onClassesDataNotAvailable();
        } else {
          callBack.onClassesLoaded(classesData);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getClass(final int classID,
      @NonNull final LoadSingleClassCallBack callBack) {

    Runnable runnable = () -> {
      final ClassEntity classEntity = mDB.subjectDAO().loadSingleClass(classID);
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (classEntity != null) {
          callBack.onClassDataLoaded(classEntity);
        } else {
          callBack.onClassDataNotAvailable();
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void insertClass(@NonNull final ClassEntity classEntity) {
    AppExecutors.getInstance().diskIO().execute(() -> mDB.subjectDAO().insertClass(classEntity));
  }

  @Override
  public void updateClass(@NonNull final ClassEntity classEntity) {
    AppExecutors.getInstance().diskIO().execute(() -> mDB.subjectDAO().updateClass(classEntity));
  }

  @Override
  public void deleteClass(@NonNull final ClassEntity classEntity) {

    AppExecutors.getInstance().diskIO().execute(() -> mDB.subjectDAO().deleteClass(classEntity));
  }

  // Logic for ClassEntity.

  @Override
  public void getStudents(final int classID, @NonNull final LoadStudentsCallBack callBack) {
    Runnable runnable = () -> {
      final List<StudentEntity> studentsData = mDB.subjectDAO()
          .loadAllStudents(classID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (studentsData.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onStudentsDataNotAvailable();
        } else {
          callBack.onStudentsLoaded(studentsData);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void checkStudentsUID(String searchKeyword, @NonNull LoadStudentsCallBack callBack) {
    Runnable runnable = () -> {
      final List<StudentEntity> studentsData = mDB.subjectDAO().checkStudentUID(searchKeyword);
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (studentsData.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onStudentsDataNotAvailable();
        } else {
          callBack.onStudentsLoaded(studentsData);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getStudentsBySubject(final int subjectID,
      @NonNull final LoadStudentsCallBack callBack) {
    Runnable runnable = () -> {
      final List<StudentEntity> studentsData = mDB.subjectDAO()
          .loadAllStudentsBySubject(subjectID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (studentsData.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onStudentsDataNotAvailable();
        } else {
          callBack.onStudentsLoaded(studentsData);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getStudent(final int studentID, @NonNull final LoadSingleStudentCallBack callBack) {
    Runnable runnable = () -> {
      final StudentEntity studentEntity = mDB.subjectDAO().loadSingleStudent(studentID);
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (studentEntity != null) {
          callBack.onStudentsLoaded(studentEntity);
        } else {
          callBack.onStudentsDataNotAvailable();
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void insertStudent(@NonNull final StudentEntity studentEntity) {
    AppExecutors.getInstance().diskIO()
        .execute(() -> mDB.subjectDAO().insertStudent(studentEntity));
  }

  @Override
  public void updateStudent(@NonNull final StudentEntity studentEntity) {
    AppExecutors.getInstance().diskIO()
        .execute(() -> mDB.subjectDAO().updateStudent(studentEntity));
  }

  @Override
  public void deleteStudent(@NonNull final StudentEntity studentEntity) {
    AppExecutors.getInstance().diskIO()
        .execute(() -> mDB.subjectDAO().deleteStudent(studentEntity));
  }

  @Override
  public int updateStudentName(final String name, final int id) {
    final int[] updated = new int[1];
    AppExecutors.getInstance().diskIO().execute(
        () -> updated[0] = mDB.subjectDAO().updateStudentName(name, id));
    return updated[0];
  }

  // Logic for Absentee Entity.

  @Override
  public void getAllAbsentee(@NonNull final LoadAbsenteeCallBack callBack) {
    Runnable runnable = () -> {
      final List<AbsenteeEntity> absenteeEntities = mDB.subjectDAO()
          .loadAllAbsentee(UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (absenteeEntities.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onAbsenteeDataNotAvailable();
        } else {
          callBack.onAbsenteeLoaded(absenteeEntities);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getAllAbsenteeBySubject(final int subjectID,
      @NonNull final LoadAbsenteeCallBack callBack) {
    Runnable runnable = () -> {
      final List<AbsenteeEntity> absenteeEntities =
          mDB.subjectDAO().loadAllAbsenteeBySubject(subjectID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (absenteeEntities.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onAbsenteeDataNotAvailable();
        } else {
          callBack.onAbsenteeLoaded(absenteeEntities);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getAllAbsenteeByClass(final int classID,
      @NonNull final LoadAbsenteeCallBack callBack) {
    Runnable runnable = () -> {
      final List<AbsenteeEntity> absenteeEntities =
          mDB.subjectDAO().loadAllAbsenteeByClass(classID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (absenteeEntities.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onAbsenteeDataNotAvailable();
        } else {
          callBack.onAbsenteeLoaded(absenteeEntities);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getAbsentee(final int absenteeID, final int classID,
      @NonNull final LoadSingleAbsenteeCallBack callBack) {
    Runnable runnable = () -> {
      final AbsenteeEntity absenteeEntity = mDB.subjectDAO()
          .loadSingleAbsentee(absenteeID, classID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (absenteeEntity != null) {
          callBack.onAbsenteeLoaded(absenteeEntity);
        } else {
          callBack.onAbsenteeDataNotAvailable();
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void getAbsenteeByDate(final long dateValue, final int classID,
      @NonNull final LoadingAbsenteeByDateCallBack callBack) {
    Runnable runnable = () -> {
      final AbsenteeEntity absenteeEntity = mDB.subjectDAO()
          .getAbsenteeByDate(dateValue, classID, UtilsMethods.getUserUID());
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (absenteeEntity != null) {
          callBack.onAbsenteeLoaded(absenteeEntity);
        } else {
          callBack.onAbsenteeDataNotAvailable();
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  @Override
  public void insertAbsentee(@NonNull final AbsenteeEntity absenteeEntity) {
    AppExecutors.getInstance().diskIO().execute(
        () -> mDB.subjectDAO().insertAbsentee(absenteeEntity));
  }

  @Override
  public void updateAbsentee(@NonNull final AbsenteeEntity absenteeEntity) {
    AppExecutors.getInstance().diskIO().execute(
        () -> mDB.subjectDAO().updateAbsentee(absenteeEntity));
  }

  @Override
  public void deleteAbsentee(@NonNull final AbsenteeEntity absenteeEntity) {
    AppExecutors.getInstance().diskIO().execute(
        () -> mDB.subjectDAO().deleteAbsentee(absenteeEntity));
  }

  @Override
  public void getAbsenteeForStudent(final int studentID,
      @NonNull final LoadAbsenteeCallBack callBack) {
    Runnable runnable = () -> {
      final List<AbsenteeEntity> absenteeEntities =
          mDB.subjectDAO().getAbsenteeForStudent(studentID);
      AppExecutors.getInstance().mainThread().execute(() -> {
        if (absenteeEntities.isEmpty()) {
          // This will be called if the table is new or just empty.
          callBack.onAbsenteeDataNotAvailable();
        } else {
          callBack.onAbsenteeLoaded(absenteeEntities);
        }
      });
    };

    AppExecutors.getInstance().diskIO().execute(runnable);
  }

  /**
   * Async Loading Students using UID, help us in Attendance Situations.
   *
   * @return List of {@link StudentEntity}
   */
  public StudentEntity getStudentByUID(Integer studentUID)
      throws ExecutionException, InterruptedException {
    return new StudentsDataLoader(mDB.subjectDAO()).execute(studentUID).get();
  }

  private static class StudentsDataLoader extends AsyncTask<Integer, Void, StudentEntity> {

    private SubjectDAO mAsyncDao;

    StudentsDataLoader(SubjectDAO dao) {
      this.mAsyncDao = dao;
    }


    @Override
    protected StudentEntity doInBackground(Integer... integers) {
      return mAsyncDao.loadSingleStudent(integers[0]);
    }
  }

  public void syncDataWithRoom(CloudModel model) {
    if (model.getSubjectEntities() != null) {
      for (SubjectEntity entity : model.getSubjectEntities()) {
        insertSubject(entity);
      }
    }

    if (model.getClassEntities() != null) {
      for (ClassEntity classEntity : model.getClassEntities()) {
        insertClass(classEntity);
      }
    }

    if (model.getAbsenteeEntities() != null) {
      for (AbsenteeEntity absenteeEntity : model.getAbsenteeEntities()) {
        insertAbsentee(absenteeEntity);
      }
    }

    if (model.getStudentEntities() != null) {
      for (StudentEntity studentEntity : model.getStudentEntities()) {
        insertStudent(studentEntity);
      }
    }
  }

  public boolean isSyncNeeded() throws ExecutionException, InterruptedException {
    return checkSubjects().size() <= 0;
  }

  private List<SubjectEntity> checkSubjects() throws ExecutionException, InterruptedException {
    return new SubjectDataLoader(mDB.subjectDAO()).execute().get();
  }

  private static class SubjectDataLoader extends AsyncTask<Void, Void, List<SubjectEntity>> {

    private SubjectDAO mAsyncDao;

    SubjectDataLoader(SubjectDAO dao) {
      this.mAsyncDao = dao;
    }

    @Override
    protected List<SubjectEntity> doInBackground(Void... voids) {
      return mAsyncDao.loadAllSubjects(UtilsMethods.getUserUID());
    }
  }


}
