package com.nloops.students.students;

import com.nloops.students.data.mvp.BasePresenter;
import com.nloops.students.data.mvp.BaseView;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;

public interface StudentContract {

  interface View extends BaseView<Presenter> {

    void showStudentList(List<StudentEntity> data);

    void showEmptyState();

    void showAddNewStudent();

    void showEditStudent(int studentID);

    void showResultMessage(String message);

    void showDeletedMessage();

    void setupPopupMenu();
  }


  interface Presenter extends BasePresenter {

    void loadStudents();

    void deleteStudent(StudentEntity studentEntity);
  }

}
