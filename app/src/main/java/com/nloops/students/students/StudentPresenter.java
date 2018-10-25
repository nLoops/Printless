package com.nloops.students.students;

import android.support.annotation.NonNull;
import com.nloops.students.data.mvp.StructureDataSource.LoadStudentsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;

public class StudentPresenter implements StudentContract.Presenter {

  private final LocalDataSource mDataSource;

  private final StudentContract.View mView;

  private final int mClassID;


  public StudentPresenter(@NonNull LocalDataSource dataSource,
      @NonNull StudentContract.View view,
      @NonNull int classID) {
    this.mDataSource = dataSource;
    this.mView = view;
    this.mClassID = classID;
    // link presenter with view.
    mView.setPresenter(this);
  }


  @Override
  public void loadStudents() {
    mDataSource.getStudents(mClassID, new LoadStudentsCallBack() {
      @Override
      public void onStudentsLoaded(List<StudentEntity> data) {
        mView.showStudentList(data);
      }

      @Override
      public void onStudentsDataNotAvailable() {
        mView.showEmptyState();
      }
    });
  }

  @Override
  public void deleteStudent(StudentEntity studentEntity) {
    mDataSource.deleteStudent(studentEntity);
    loadStudents();
    mView.showDeletedMessage();
  }

  @Override
  public void start() {
    loadStudents();
    mView.setupPopupMenu();
  }
}
