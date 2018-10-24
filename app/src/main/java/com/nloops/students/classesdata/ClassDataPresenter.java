package com.nloops.students.classesdata;

import android.support.annotation.NonNull;
import com.nloops.students.classesdata.ClassDataContract.ClassPresenter;
import com.nloops.students.classesdata.ClassDataContract.ClassView;
import com.nloops.students.data.mvp.StructureDataSource.LoadClassesCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;
import java.util.List;

public class ClassDataPresenter implements ClassPresenter {

  private final LocalDataSource mLocalDataSource;

  private final ClassView mClassView;

  private final int mSubjectID;

  public ClassDataPresenter(@NonNull LocalDataSource dataSource,
      @NonNull ClassView classView,
      @NonNull int subjectID) {

    this.mLocalDataSource = dataSource;

    this.mClassView = classView;

    this.mSubjectID = subjectID;

    mClassView.setPresenter(this);

  }


  @Override
  public void loadClasses() {
    mLocalDataSource.getClasses(mSubjectID, new LoadClassesCallBack() {
      @Override
      public void onClassesLoaded(List<ClassEntity> data) {
        mClassView.showClassesList(data);
      }

      @Override
      public void onClassesDataNotAvailable() {
        mClassView.showEmptyState();
      }
    });
  }

  @Override
  public void deleteClass(ClassEntity classEntity) {
    mLocalDataSource.deleteClass(classEntity);
    loadClasses();
    mClassView.showDeletedMessage();
  }


  @Override
  public void start() {
    loadClasses();
    mClassView.setupPopupMenu();
  }
}
