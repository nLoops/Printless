package com.nloops.students.reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.StudentsReportAdapter;
import com.nloops.students.data.mvp.StructureDataSource.LoadStudentsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.utils.UtilsConstants;
import java.util.List;
import java.util.Objects;

public class StudentsReports extends AppCompatActivity {

  // bind views to class
  @BindView(R.id.rv_student_report_activity)
  RecyclerView mReportsRV;
  @BindView(R.id.reports_rv_empty_state)
  RelativeLayout mEmptyState;
  @BindView(R.id.tv_student_report_toolbar)
  TextView mToolBarTV;
  @BindView(R.id.student_report_toolbar)
  Toolbar mToolbar;

  // ref of data adapter
  private StudentsReportAdapter mAdapter;
  // ref of passed ClassID
  private int passedClassID = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_students_reports);
    // bind views
    ButterKnife.bind(this);
    // setup toolbar
    setSupportActionBar(mToolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    // get passed class ID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_REPORT)) {
      passedClassID = getIntent().getIntExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_REPORT, -1);
    }
    // get class name
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_NAME_TO_STUDENT_REPORT)) {
      String className = getIntent()
          .getStringExtra(UtilsConstants.EXTRA_CLASS_NAME_TO_STUDENT_REPORT);
      mToolBarTV.setText(className);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    displayData(passedClassID);
  }

  private void displayData(int classID) {
    LocalDataSource.getInstance(StudentsReports.this).getStudents(classID,
        new LoadStudentsCallBack() {
          @Override
          public void onStudentsLoaded(List<StudentEntity> data) {
            mEmptyState.setVisibility(View.INVISIBLE);
            mReportsRV.setVisibility(View.VISIBLE);
            mReportsRV.setLayoutManager(new LinearLayoutManager(StudentsReports.this));
            mReportsRV.setHasFixedSize(true);
            mAdapter = new StudentsReportAdapter(data, StudentsReports.this, passedClassID);
            mReportsRV.setAdapter(mAdapter);
          }

          @Override
          public void onStudentsDataNotAvailable() {
            mEmptyState.setVisibility(View.VISIBLE);
            mReportsRV.setVisibility(View.INVISIBLE);
          }
        });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_TO_CLASS_REPORT)) {
          int passedSubject = getIntent()
              .getIntExtra(UtilsConstants.EXTRA_SUBJECT_TO_CLASS_REPORT, -1);
          Intent classReport = new Intent(StudentsReports.this, ClassReports.class);
          classReport.putExtra(UtilsConstants.EXTRA_SUBJECT_TO_CLASS_REPORT, passedSubject);
          classReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(classReport);
        } else {
          NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
