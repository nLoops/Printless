package com.nloops.students.subjects;

import android.support.annotation.NonNull;
import com.nloops.students.data.mvp.StructureDataSource.LoadSubjectsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.subjects.SubjectPresenterContract.Presenter;
import com.nloops.students.subjects.SubjectPresenterContract.View;
import java.util.List;
import timber.log.Timber;

public class SubjectPresenter implements Presenter {

  private final LocalDataSource mLocalDataSource;

  private final View mView;

  private boolean refresh = true;


  public SubjectPresenter(@NonNull LocalDataSource dataSource,
      @NonNull View view) {
    mLocalDataSource = dataSource;
    mView = view;

    // set view presenter
    mView.setPresenter(this);
  }

  @Override
  public void loadSubjects(boolean forceUpdate) {
    Timber.d("force update value is %s", forceUpdate);
    mLocalDataSource.getSubjects(new LoadSubjectsCallBack() {
      @Override
      public void onSubjectsLoaded(List<SubjectEntity> subjects) {
        mView.showSubjectsList(subjects);
        Timber.d("SubjectsLoaded");
      }

      @Override
      public void onSubjectDataNotAvailable() {
        mView.showEmptyState();
        Timber.d("SubjectNotAvailable");
      }
    });

  }


  @Override
  public void deleteSubject(SubjectEntity subject) {
    mLocalDataSource.deleteSubject(subject);
    loadSubjects(refresh);
    mView.showDeletedMessage();
  }

  @Override
  public void start() {
    loadSubjects(refresh);
    mView.setupPopupMenu();
  }
}
