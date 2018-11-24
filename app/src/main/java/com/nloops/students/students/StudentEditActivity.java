package com.nloops.students.students;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.students.StudentEditContract.Presenter;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.utils.UtilsMethods;
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
  @BindView(R.id.tl_add_student_name)
  TextInputLayout mStudentNameTL;
  @BindView(R.id.tl_add_student_uid)
  TextInputLayout mStudentUidTL;
  @BindView(R.id.general_toolbar)
  Toolbar mToolBar;
  @BindView(R.id.tv_general_toolbar)
  TextView mToolBarTV;

  // ref of presenter to handle data
  private StudentEditPresenter mPresenter;

  // flag to detect which mode we are in
  // is new Student, or Edit Mode.
  private boolean isEditMode = false;

  private StudentEntity entity;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_edit);
    // link views to class
    ButterKnife.bind(this);
    // Setup toolbar
    setSupportActionBar(mToolBar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    // setup presenter
    setupPresenter();
    // force keyboard to appear
    UtilsMethods.showKeyboard(this);
    // hide error if enabled
    mStudentName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mStudentNameTL.isErrorEnabled()) {
          mStudentNameTL.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });
    mStudentUID.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mStudentUidTL.isErrorEnabled()) {
          mStudentUidTL.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });
  }

  private void setupPresenter() {
    if (getIntent().hasExtra(UtilsConstants.EXTRA_STUDENT_ID_INTENT)) {
      // get passed id
      int studentID = getIntent().
          getIntExtra(UtilsConstants.EXTRA_STUDENT_ID_INTENT, -1);
      if (studentID > -1) {
        isEditMode = true;
        // set toolbar title
        mToolBarTV.setText(getString(R.string.toolbar_edit_student));
      } else {
        // set toolbar title
        mToolBarTV.setText(getString(R.string.toolbar_new_student));
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
    if (!isMissingData()) {
      mPresenter.searchForUID(Objects.requireNonNull(mStudentUID.getText()).toString());
    } else {
      showMissingDataMessage();
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
    if (mStudentName.length() <= 0 && mStudentUID.length() > 0) {
      mStudentNameTL.setErrorEnabled(true);
      mStudentNameTL.setError(getText(R.string.missing_data_field_warn));
    } else if (mStudentUID.length() <= 0 && mStudentName.length() > 0) {
      mStudentUidTL.setErrorEnabled(true);
      mStudentUidTL.setError(getText(R.string.missing_data_field_warn));
    } else {
      mStudentNameTL.setErrorEnabled(true);
      mStudentNameTL.setError(getText(R.string.missing_data_field_warn));
      mStudentUidTL.setErrorEnabled(true);
      mStudentUidTL.setError(getText(R.string.missing_data_field_warn));
    }
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
    if (!isEditMode) {
      return mStudentUID.length() <= 0 || mStudentName.length() <= 0;
    } else {
      return mStudentName.length() <= 0;
    }
  }

  @Override
  public void setStudentName(String name) {
    mStudentName.setText(name);
    UtilsMethods.setCursorToEnd(mStudentName);
  }

  @Override
  public void setStudentUID(String studentUID) {
    mStudentUID.setText(studentUID);
    UtilsMethods.setCursorToEnd(mStudentUID);
  }

  @Override
  public void setStudentEntity(StudentEntity entity) {
    this.entity = entity;
  }

  @Override
  public void showStudentNameMsg(String studentName) {
    AlertDialog builder = new Builder(StudentEditActivity.this)
        .setMessage(getString(R.string.duplicate_students_msg, studentName))
        .setPositiveButton(getString(R.string.duplicate_msg_okay),
            (dialog, which) -> actionStudent())
        .setNegativeButton(getString(R.string.duplicate_msg_cancel), (dialog, which) -> {
          if (dialog != null) {
            dialog.dismiss();
          }
        })
        .show();

  }

  @Override
  public void proceedStudentAction() {
    actionStudent();
  }

  @Override
  public void setPresenter(Presenter presenter) {
    mPresenter = (StudentEditPresenter) presenter;
  }

  private void actionStudent() {
    int classID = -1;
    int subjectID = -1;
    // get passed ClassID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID)) {
      classID = getIntent().getIntExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID, -1);
    }
    // get passed SubjectID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_ID_CLASS_TO_STUDENT)) {
      subjectID = getIntent().getIntExtra(UtilsConstants.EXTRA_SUBJECT_ID_CLASS_TO_STUDENT, -1);
    }
    // check if we are in edit mode then we will call update method, instead we will call
    // insert new student method.
    if (isEditMode) {
      if (entity != null) {
        entity.setStudentName(Objects.requireNonNull(mStudentName.getText()).toString());
        entity.setStudentUniID(Objects.requireNonNull(mStudentUID.getText()).toString());
        mPresenter.updateStudent(entity);
      }
    } else {
      StudentEntity studentEntity = new StudentEntity(
          Objects.requireNonNull(mStudentName.getText()).toString(),
          Objects.requireNonNull(mStudentUID.getText()).toString(),
          classID, UtilsConstants.STUDENT_ABSENTEE_NO, subjectID);
      mPresenter.insertStudent(studentEntity);
    }
  }
}
