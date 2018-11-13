package com.nloops.students.attendance;

import android.support.annotation.NonNull;
import com.nloops.students.data.mvp.StructureDataSource.LoadAbsenteeCallBack;
import com.nloops.students.data.mvp.StructureDataSource.LoadStudentsCallBack;
import com.nloops.students.data.mvp.StructureDataSource.LoadingAbsenteeByDateCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;
import timber.log.Timber;

public class AttendancePresenter implements AttendanceContract.Presenter {

  private final LocalDataSource mDataSource;
  private final AttendanceContract.View mView;
  private final int mClassID;

  public AttendancePresenter(@NonNull LocalDataSource dataSource,
      @NonNull AttendanceContract.View view,
      int classID) {
    this.mDataSource = dataSource;
    this.mView = view;
    this.mClassID = classID;
    mView.setPresenter(this);
  }

  @Override
  public void loadStudents() {
    mDataSource.getStudents(mClassID, new LoadStudentsCallBack() {
      @Override
      public void onStudentsLoaded(List<StudentEntity> data) {
        mView.showStudentsList(data);
      }

      @Override
      public void onStudentsDataNotAvailable() {
        Timber.tag("Data");
        Timber.i("Data is not Available");
      }
    });
  }

  @Override
  public void insertAttendance(AbsenteeEntity entity) {
    mDataSource.insertAbsentee(entity);
  }

  @Override
  public void updateAttendance(AbsenteeEntity entity) {
    mDataSource.updateAbsentee(entity);
  }


  @Override
  public void loadAbsenteeByDate(long dateValue, int classID) {
    mDataSource.getAbsenteeByDate(dateValue, classID, new LoadingAbsenteeByDateCallBack() {
      @Override
      public void onAbsenteeLoaded(AbsenteeEntity entity) {
        mView.showStudentsList(entity.getStudentsList());
        mView.showAbsenteeList(entity);
      }

      @Override
      public void onAbsenteeDataNotAvailable() {
        mView.clearAdapter();
      }
    });
  }

  @Override
  public void loadAllAbsentee(int classID) {
    mDataSource.getAllAbsenteeByClass(classID, new LoadAbsenteeCallBack() {
      @Override
      public void onAbsenteeLoaded(List<AbsenteeEntity> data) {
        mView.showAllAbsentee(data);
      }

      @Override
      public void onAbsenteeDataNotAvailable() {
        Timber.tag("Data");
        Timber.i("Data is not Available");
      }
    });
  }


  @Override
  public void start() {
    loadStudents();
  }
}
