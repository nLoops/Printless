package com.nloops.students.subjects;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.SubjectEntity;

public interface SubjectEditContract {

  interface View extends BaseView<Presenter> {

    void showInsertMessage();

    void showUpdateMessage();

    void showMissingDataMessage();

    void handleViewsColors();

    void showErrorMessage();

    boolean isMissingData();

    void setSubjectName(String name);

    void setSubjectSchool(String school);
  }


  interface Presenter extends BasePresenter {

    void insertSubject(SubjectEntity subject);

    void updateSubject(SubjectEntity subject);

    void populateData();
  }

}
