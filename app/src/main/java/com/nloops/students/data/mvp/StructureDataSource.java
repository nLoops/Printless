package com.nloops.students.data.mvp;


import android.support.annotation.NonNull;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

/**
 * This interface will be the main source for accessing data Local Data + Network data if we need to
 * this is the first build block of our App Architecture (M-V-P)
 */
public interface StructureDataSource {

  // first define two callbacks to handle loading of Subject data
  // one for the whole subject data which we can use in recyclerView
  // second for the single subject which we can use in delete, update operations.
  interface LoadSubjectsCallBack {

    void onSubjectsLoaded(List<SubjectEntity> subjects);

    void onSubjectDataNotAvailable();
  }

  interface LoadSingleSubjectCallBack {

    void onSubjectLoaded(SubjectEntity subject);

    void onSubjectDataNotAvailable();
  }

  // The Same logic for ClassEntity
  interface LoadClassesCallBack {

    void onClassesLoaded(List<ClassEntity> data);

    void onClassesDataNotAvailable();
  }

  interface LoadSingleClassCallBack {

    void onClassDataLoaded(ClassEntity classEntity);

    void onClassDataNotAvailable();
  }

  // The Same logic for Students Entity
  interface LoadStudentsCallBack {

    void onStudentsLoaded(List<StudentEntity> data);

    void onStudentsDataNotAvailable();
  }

  interface LoadSingleStudentCallBack {

    void onStudentsLoaded(StudentEntity studentEntity);

    void onStudentsDataNotAvailable();
  }

  // The Same logic for Absentee Entity
  interface LoadAbsenteeCallBack {

    void onAbsenteeLoaded(List<AbsenteeEntity> data);

    void onAbsenteeDataNotAvailable();
  }

  interface LoadSingleAbsenteeCallBack {

    void onAbsenteeLoaded(AbsenteeEntity absenteeEntity);

    void onAbsenteeDataNotAvailable();
  }

  interface LoadingAbsenteeByDateCallBack {

    void onAbsenteeLoaded(AbsenteeEntity entity);

    void onAbsenteeDataNotAvailable();
  }

  void getSubjects(@NonNull LoadSubjectsCallBack callBack);

  void getSubject(int subjectID, @NonNull LoadSingleSubjectCallBack callBack);

  void insertSubject(@NonNull SubjectEntity subject);

  void updateSubject(@NonNull SubjectEntity subject);

  void deleteSubject(@NonNull SubjectEntity subject);

  // for ClassEntity

  void getClasses(int subjectID, @NonNull LoadClassesCallBack callBack);

  void getClass(int classID, @NonNull LoadSingleClassCallBack callBack);

  void insertClass(@NonNull ClassEntity classEntity);

  void updateClass(@NonNull ClassEntity classEntity);

  void deleteClass(@NonNull ClassEntity classEntity);

  // for StudentEntity

  void getStudents(int classID, @NonNull LoadStudentsCallBack callBack);

  void getStudentsBySubject(int subjectID, @NonNull LoadStudentsCallBack callBack);

  void getStudent(int studentID, @NonNull LoadSingleStudentCallBack callBack);

  void insertStudent(@NonNull StudentEntity studentEntity);

  void updateStudent(@NonNull StudentEntity studentEntity);

  void deleteStudent(@NonNull StudentEntity studentEntity);

  int updateStudentName(String name, int id);

  // for Absentee Entity

  void getAllAbsentee(@NonNull LoadAbsenteeCallBack callBack);

  void getAllAbsenteeBySubject(int subjectID, @NonNull LoadAbsenteeCallBack callBack);

  void getAllAbsenteeByClass(int classID, @NonNull LoadAbsenteeCallBack callBack);

  void getAbsentee(int absenteeID, int classID, @NonNull LoadSingleAbsenteeCallBack callBack);

  void getAbsenteeByDate(long dateValue, int classID,
      @NonNull LoadingAbsenteeByDateCallBack callBack);

  void insertAbsentee(@NonNull AbsenteeEntity absenteeEntity);

  void updateAbsentee(@NonNull AbsenteeEntity absenteeEntity);

  void deleteAbsentee(@NonNull AbsenteeEntity absenteeEntity);

  void getAbsenteeForStudent(int studentID, @NonNull LoadAbsenteeCallBack callBack);

}
