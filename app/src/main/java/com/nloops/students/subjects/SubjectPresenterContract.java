package com.nloops.students.subjects;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

public interface SubjectPresenterContract {

  interface View extends BaseView<Presenter> {

    void showSubjectsList(List<SubjectEntity> data);

    void showEmptyState();

    void showAddNewSubject();

    void showEditSubject(int subjectID);

    void showResultMessage(String message);

    void showDeletedMessage();

    void setupPopupMenu();

    void showClassesActivity(int subjectID, String subjectName);
  }


  interface Presenter extends BasePresenter {

    void loadSubjects(boolean forceUpdate);

    void deleteSubject(SubjectEntity subject);
  }

}
