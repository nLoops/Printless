package com.nloops.students.data.mvp;


import android.support.annotation.NonNull;
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

  void getSubjects(@NonNull LoadSubjectsCallBack callBack);

  void getSubject(@NonNull int subjectID, @NonNull LoadSingleSubjectCallBack callBack);

  void insertSubject(@NonNull SubjectEntity subject);

  void updateSubject(@NonNull SubjectEntity subject);

  void deleteSubject(@NonNull SubjectEntity subject);

}
