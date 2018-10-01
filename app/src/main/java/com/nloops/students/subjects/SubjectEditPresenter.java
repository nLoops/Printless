package com.nloops.students.subjects;

import android.support.annotation.NonNull;
import com.nloops.students.data.mvp.StructureDataSource.LoadSingleSubjectCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.SubjectEntity;

public class SubjectEditPresenter implements SubjectEditContract.Presenter {

  private final LocalDataSource dataSource;

  private final SubjectEditContract.View mView;

  private int subjectID;

  public SubjectEditPresenter(@NonNull LocalDataSource localDataSource,
      @NonNull SubjectEditContract.View view, int id) {
    // assign local vars with passed values
    this.dataSource = localDataSource;

    this.mView = view;

    this.subjectID = id;
    // link this presenter to the view.
    mView.setPresenter(this);
  }

  @Override
  public void insertSubject(SubjectEntity subject) {
    if (mView.isMissingData()) {
      mView.showMissingDataMessage();
    } else {
      dataSource.insertSubject(subject);
      mView.showInsertMessage();
    }
  }

  @Override
  public void updateSubject(SubjectEntity subject) {
    if (mView.isMissingData()) {
      mView.showMissingDataMessage();
    } else {
      // SET ID to update the exist subject
      subject.setSubjectID(subjectID);
      // perform update subject on the background
      dataSource.updateSubject(subject);
      // notify the user that update success.
      mView.showUpdateMessage();
    }
  }

  @Override
  public void populateData() {

    if (isNewSubject()) {
      mView.handleViewsColors();
    } else {
      mView.handleViewsColors();
      dataSource.getSubject(subjectID, new LoadSingleSubjectCallBack() {
        @Override
        public void onSubjectLoaded(SubjectEntity subject) {
          mView.setSubjectName(subject.getSubjectName());
          mView.setSubjectSchool(subject.getSchoolName());
        }

        @Override
        public void onSubjectDataNotAvailable() {
          mView.showErrorMessage();
        }
      });
    }
  }

  @Override
  public void start() {
    populateData();
  }

  // if subjectID == -1 which is means we have new task return true.
  private boolean isNewSubject() {
    return subjectID == -1;
  }
}
