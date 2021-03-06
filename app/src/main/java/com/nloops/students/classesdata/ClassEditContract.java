package com.nloops.students.classesdata;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.ClassEntity;

public interface ClassEditContract {

  interface View extends BaseView<Presenter> {

    void showInsertMessage();

    void showUpdateMessage();

    void showMissingDataMessage();

    void handleViewsColors();

    void showErrorMessage();

    boolean isMissingData();

    void setClassName(String name);

  }


  interface Presenter extends BasePresenter {

    void insertClass(ClassEntity classEntity);

    void updateClass(ClassEntity classEntity);

    void populateData();
  }

}
