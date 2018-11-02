package com.nloops.students.students;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.StudentEntity;

public interface StudentEditContract {

  interface View extends BaseView<StudentEditContract.Presenter> {

    void showInsertMessage();

    void showUpdateMessage();

    void showMissingDataMessage();

    void handleViewsColors();

    void showErrorMessage();

    boolean isMissingData();

    void setStudentName(String name);

  }


  interface Presenter extends BasePresenter {

    void insertStudent(StudentEntity studentEntity);

    void updateStudent(String name);

    void populateData();
  }

}
