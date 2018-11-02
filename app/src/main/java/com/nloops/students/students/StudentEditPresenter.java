package com.nloops.students.students;

import android.support.annotation.NonNull;
import com.nloops.students.data.mvp.StructureDataSource.LoadSingleStudentCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;

public class StudentEditPresenter implements StudentEditContract.Presenter {

  private final LocalDataSource mDataSource;

  private final StudentEditContract.View mView;

  private final int mStudentID;

  /**
   * Public Constructor of the {@link StudentEditPresenter}
   *
   * @param dataSource {@link LocalDataSource}
   * @param view {@link StudentEditContract.View}
   * @param studentID passed student id.
   */
  public StudentEditPresenter(@NonNull LocalDataSource dataSource,
      @NonNull StudentEditContract.View view,
      int studentID) {
    this.mDataSource = dataSource;
    this.mView = view;
    this.mStudentID = studentID;
    mView.setPresenter(this);
  }


  @Override
  public void insertStudent(StudentEntity studentEntity) {
    if (mView.isMissingData()) {
      mView.showMissingDataMessage();
    } else {
      mDataSource.insertStudent(studentEntity);
      mView.showInsertMessage();
    }
  }

  @Override
  public void updateStudent(String name) {
    if (mView.isMissingData()) {
      mView.showMissingDataMessage();
    } else {
      mDataSource.updateStudentName(name, mStudentID);
      // notify the user that update success.
      mView.showUpdateMessage();
    }
  }

  @Override
  public void populateData() {
    if (isNewStudent()) {
      mView.handleViewsColors();
    } else {
      mView.handleViewsColors();
      mDataSource.getStudent(mStudentID, new LoadSingleStudentCallBack() {
        @Override
        public void onStudentsLoaded(StudentEntity studentEntity) {
          mView.setStudentName(studentEntity.getStudentName());
        }

        @Override
        public void onStudentsDataNotAvailable() {
          mView.showErrorMessage();
        }
      });
    }
  }

  @Override
  public void start() {
    populateData();
  }

  // if studentID == -1 which is means we have new task return true.
  private boolean isNewStudent() {
    return mStudentID == -1;
  }
}
