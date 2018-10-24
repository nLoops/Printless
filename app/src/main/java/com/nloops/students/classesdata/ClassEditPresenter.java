package com.nloops.students.classesdata;

import android.support.annotation.NonNull;
import com.nloops.students.data.mvp.StructureDataSource.LoadSingleClassCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;

public class ClassEditPresenter implements ClassEditContract.Presenter {

  private final LocalDataSource mDataSource;

  private final ClassEditContract.View mView;

  private final int mClassID;


  public ClassEditPresenter(@NonNull LocalDataSource dataSource,
      @NonNull ClassEditContract.View view,
      @NonNull int classID) {
    this.mDataSource = dataSource;
    this.mView = view;
    this.mClassID = classID;

    mView.setPresenter(this);
  }


  @Override
  public void insertClass(ClassEntity classEntity) {
    if (mView.isMissingData()) {
      mView.showMissingDataMessage();
    } else {
      mDataSource.insertClass(classEntity);
      mView.showInsertMessage();
    }
  }

  @Override
  public void updateClass(ClassEntity classEntity) {
    if (mView.isMissingData()) {
      mView.showMissingDataMessage();
    } else {
      // SET ID to update the exist class
      classEntity.setClassID(mClassID);
      // perform update class on the background
      mDataSource.updateClass(classEntity);
      // notify the user that update success.
      mView.showUpdateMessage();
    }
  }

  @Override
  public void populateData() {
    if (isNewClass()) {
      mView.handleViewsColors();
    } else {
      mView.handleViewsColors();
      mDataSource.getClass(mClassID, new LoadSingleClassCallBack() {
        @Override
        public void onClassDataLoaded(ClassEntity classEntity) {
          mView.setClassName(classEntity.getClassName());
        }

        @Override
        public void onClassDataNotAvailable() {
          mView.showErrorMessage();
        }
      });
    }
  }

  @Override
  public void start() {
    populateData();
  }

  // if classID == -1 which is means we have new task return true.
  private boolean isNewClass() {
    return mClassID == -1;
  }
}
