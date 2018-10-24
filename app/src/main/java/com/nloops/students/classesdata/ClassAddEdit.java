package com.nloops.students.classesdata;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.nloops.students.classesdata.ClassEditContract.Presenter;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.utils.UtilsConstants;

public class ClassAddEdit extends AppCompatActivity implements ClassEditContract.View {

  // Classes Activity Views
  @BindView(R.id.ed_class_name)
  TextInputEditText mClassNameED;
  @BindView(R.id.btn_add_class)
  Button mAddClassBT;
  @BindView(R.id.btn_cancel_class)
  Button mCancelClassBT;
  @BindView(R.id.class_add_edit_container)
  ConstraintLayout mLayoutContainer;

  // ref of class presenter
  private ClassEditPresenter mPresenter;

  // flag to check if we are in editmode
  private boolean isEditMode = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_class_add_edit);
    // bind views
    ButterKnife.bind(this);

    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_ID_INTENT)) {
      int classID = getIntent().
          getIntExtra(UtilsConstants.EXTRA_CLASS_ID_INTENT, -1);

      if (classID > -1) {
        isEditMode = true;
      }

      mPresenter = new ClassEditPresenter(LocalDataSource.getInstance(this), this,
          classID);
    }

    mAddClassBT.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_TO_EDIT_SUBJECT_ID)) {
          int parentSubjectID = getIntent().getIntExtra
              (UtilsConstants.EXTRA_CLASS_TO_EDIT_SUBJECT_ID, -1);
          // Create new Class Entity.
          ClassEntity classEntity = new ClassEntity(mClassNameED.getText().toString(),
              parentSubjectID);
          if (isEditMode) {
            mPresenter.updateClass(classEntity);
          } else {
            mPresenter.insertClass(classEntity);
          }
        }
      }
    });

    mCancelClassBT.setOnClickListener(new OnClickListener() {
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
      mAddClassBT.setBackground(fullBackground);
      mCancelClassBT.setBackground(strokeBackground);
      mAddClassBT.setText(getString(R.string.button_update_current));
      mCancelClassBT.setTextColor(getResources().getColor(R.color.colorYellow));
    }
  }

  @Override
  public void showErrorMessage() {
    Snackbar.make(mLayoutContainer, getString(R.string.subject_add_edit_error_msg),
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  public boolean isMissingData() {
    return mClassNameED.length() <= 0;
  }

  @Override
  public void setClassName(String name) {
    mClassNameED.setText(name);
  }

  @Override
  public void setPresenter(Presenter presenter) {
    //
  }
}
