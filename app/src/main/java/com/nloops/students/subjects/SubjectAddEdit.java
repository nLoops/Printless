package com.nloops.students.subjects;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.subjects.SubjectEditContract.Presenter;
import com.nloops.students.utils.UtilsConstants;

/**
 * This Activity Will handle Add, Edit Subjects it will cooperation with {@link SubjectActivity}
 * using {@link SubjectEditPresenter} and {@link SubjectEditContract} to implement MVP Architecture
 */
public class SubjectAddEdit extends AppCompatActivity implements
    SubjectEditContract.View {

  // get Views ref
  @BindView(R.id.ed_subject_name)
  TextInputEditText mSubjectNameED;
  @BindView(R.id.ed_school_name)
  TextInputEditText mSchoolNameED;
  @BindView(R.id.btn_add_subject)
  Button mInsertSubjectBT;
  @BindView(R.id.btn_cancel_subject)
  Button mCancelSubjectBT;
  @BindView(R.id.subject_add_edit_container)
  ConstraintLayout layoutContainer;

  // ref of presenter
  private SubjectEditPresenter mPresenter;

  // flag to detect which mode we are to handle colors and theme
  private boolean isEditMode = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_edit_subject);
    // Link views to the activity
    ButterKnife.bind(this);
    // preparing presenter for new subject or edit mode
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_ID_INTENT)) {
      // getting passed value
      int subjectID = getIntent()
          .getIntExtra(UtilsConstants.EXTRA_SUBJECT_ID_INTENT, -1);
      // update flag to be true we are in Edit_MODE
      if (subjectID > -1) {
        isEditMode = true;
      }
      // if passed value > -1 that's mean we have exist subject and we should active EDIT_MODE
      mPresenter = new SubjectEditPresenter(LocalDataSource.
          getInstance(this), this, subjectID);
    }

    // link insert new subject to Add button.
    mInsertSubjectBT.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        SubjectEntity entity = new SubjectEntity(mSubjectNameED.getText().toString()
            , mSchoolNameED.getText().toString());
        if (isEditMode) {
          mPresenter.updateSubject(entity);
        } else {
          mPresenter.insertSubject(entity);
        }
      }
    });

    mCancelSubjectBT.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


  }


  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.start();
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
    Snackbar.make(layoutContainer, getString(R.string.subject_add_edit_missing_date),
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void handleViewsColors() {
    if (isEditMode) {
      Drawable fullBackground =
          ContextCompat.getDrawable(this, R.drawable.add_btn_background_yellow);
      Drawable strokeBackground =
          ContextCompat.getDrawable(this, R.drawable.add_btn_background_stroke_yellow);
      mInsertSubjectBT.setBackground(fullBackground);
      mCancelSubjectBT.setBackground(strokeBackground);
      mInsertSubjectBT.setText(getString(R.string.button_update_current));
      mCancelSubjectBT.setTextColor(getResources().getColor(R.color.colorYellow));
    }
  }

  @Override
  public void showErrorMessage() {
    Snackbar.make(layoutContainer, getString(R.string.subject_add_edit_error_msg),
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  public boolean isMissingData() {
    return mSchoolNameED.length() <= 0 && mSubjectNameED.length() <= 0;
  }

  @Override
  public void setSubjectName(String name) {
    mSubjectNameED.setText(name);
  }

  @Override
  public void setSubjectSchool(String school) {
    mSchoolNameED.setText(school);
  }

  @Override
  public void setPresenter(Presenter presenter) {
    //
  }
}
