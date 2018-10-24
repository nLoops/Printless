package com.nloops.students.classesdata;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.ClassEntity;
import java.util.List;

public interface ClassDataContract {

  interface ClassView extends BaseView<ClassPresenter> {

    void showClassesList(List<ClassEntity> data);

    void showEmptyState();

    void showAddNewClass();

    void showEditClass(int classID);

    void showResultMessage(String message);

    void showDeletedMessage();

    void setupPopupMenu();

  }


  interface ClassPresenter extends BasePresenter {

    void loadClasses();

    void deleteClass(ClassEntity classEntity);


  }

}
