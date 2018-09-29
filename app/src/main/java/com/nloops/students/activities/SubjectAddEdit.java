package com.nloops.students.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.AppExecutors;
import com.nloops.students.data.tables.SubjectEntity;

public class SubjectAddEdit extends AppCompatActivity {

  private static final String TAG = SubjectAddEdit.class.getSimpleName();

  // get Views ref
  @BindView(R.id.ed_subject_name)
  TextInputEditText mSubjectNameED;
  @BindView(R.id.ed_school_name)
  TextInputEditText mSchoolNameED;
  @BindView(R.id.btn_add_subject)
  Button mInsertSubjectBT;
  @BindView(R.id.btn_cancel_subject)
  Button mCancelSubjectBT;

  // ref of Database
  AppDatabase mDB;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_edit_subject);
    // Link views to the activity
    ButterKnife.bind(this);
    // link insert new subject to Add button.
    mInsertSubjectBT.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        insertSubject();
      }
    });

    mCancelSubjectBT.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    // define Database object.
    mDB = AppDatabase.getInstance(getApplicationContext());

  }

  /**
   * This helper method will use background Executor to insert data new object
   */
  private void insertSubject() {
    if (mSchoolNameED.length() <= 0 && mSubjectNameED.length() <= 0) {
      return;
    }
    // declare object to hold the inserted one.
    final SubjectEntity entity = new SubjectEntity(mSubjectNameED.getText().toString()
        , mSchoolNameED.getText().toString());
    // run the operation on the background.
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mDB.subjectDAO().insertSubject(entity);
      }
    });

    // close activity and return to subject activity
    finish();

  }

}
