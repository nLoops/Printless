package com.nloops.students.students;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.students.StudentEditContract.Presenter;
import com.nloops.students.utils.UtilsConstants;
import java.util.Objects;

public class StudentEditActivity extends AppCompatActivity implements
    StudentEditContract.View {

  // bind views to class
  @BindView(R.id.ed_student_uid)
  TextInputEditText mStudentUID;
  @BindView(R.id.ed_student_name)
  TextInputEditText mStudentName;
  @BindView(R.id.student_edit_container)
  ConstraintLayout mLayoutContainer;
  @BindView(R.id.btn_add_student)
  Button mAddStudentBT;
  @BindView(R.id.btn_cancel_student)
  Button mCancelStudentBT;

  // ref of presenter to handle data
  private StudentEditPresenter mPresenter;

  // flag to detect which mode we are in
  // is new Student, or Edit Mode.
  private boolean isEditMode = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_edit);
    // link views to class
    ButterKnife.bind(this);
    if (getIntent().hasExtra(UtilsConstants.EXTRA_STUDENT_ID_INTENT)) {
      // get passed id
      int studentID = getIntent().
          getIntExtra(UtilsConstants.EXTRA_STUDENT_ID_INTENT, -1);
      if (studentID > -1) {
        isEditMode = true;
      }
      mPresenter = new StudentEditPresenter(LocalDataSource.getInstance(this),
          this, studentID);
    }

  }


  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @OnClick(R.id.btn_add_student)
  public void insertStudent(Button button) {
    int classID = -1;
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID)) {
      classID = getIntent().getIntExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID, -1);
    }
    StudentEntity entity = new StudentEntity(
        Objects.requireNonNull(mStudentName.getText()).toString(),
        mStudentUID.getText().toString(),
        classID);
    if (isEditMode) {
      mPresenter.updateStudent(entity);
    } else {
      mPresenter.insertStudent(entity);
    }
  }

  @OnClick(R.id.btn_cancel_student)
  public void cancelStudentOperation(Button button) {
    finish();
  }


  @Override
  public void showInsertMessage() {
    setResult(UtilsConstants.RESULT_ADD_ITEM);
    finish();
  }

  @Override
  public void showUpdateMessage() {
    setResult(UtilsConstants.RESULT_EDIT_ITEM);
    finish();
  }

  @Override
  public void showMissingDataMessage() {
    Snackbar.make(mLayoutContainer, getString(R.string.subject_add_edit_missing_date),
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void handleViewsColors() {
    if (isEditMode) {
      Drawable fullBackground =
          ContextCompat.getDrawable(this, R.drawable.add_btn_background_yellow);
      Drawable strokeBackground =
          ContextCompat.getDrawable(this, R.drawable.add_btn_background_stroke_yellow);
      mAddStudentBT.setBackground(fullBackground);
      mAddStudentBT.setText(getString(R.string.button_update_current));
      mCancelStudentBT.setBackground(strokeBackground);
      mCancelStudentBT.setTextColor(getResources().getColor(R.color.colorYellow));
    }
  }

  @Override
  public void showErrorMessage() {
    Snackbar.make(mLayoutContainer, getString(R.string.subject_add_edit_error_msg),
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  public boolean isMissingData() {
    return mStudentUID.length() <= 0 && mStudentName.length() <= 0;
  }

  @Override
  public void setStudentName(String name) {
    mStudentName.setText(name);
  }

  @Override
  public void setPresenter(Presenter presenter) {
    //
  }
}
