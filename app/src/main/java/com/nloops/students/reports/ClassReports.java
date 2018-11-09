package com.nloops.students.reports;

import android.content.Intent;
import android.os.Bundle;
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
import com.nloops.students.adapters.ClassReportAdapter;
import com.nloops.students.adapters.ClassReportAdapter.OnClassReportClick;
import com.nloops.students.data.mvp.StructureDataSource.LoadClassesCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.subjects.SubjectActivity;
import com.nloops.students.utils.UtilsConstants;
import java.util.List;
import java.util.Objects;

public class ClassReports extends AppCompatActivity implements OnClassReportClick {

  // bind views to class
  @BindView(R.id.rv_class_report_activity)
  RecyclerView mReportsRV;
  @BindView(R.id.reports_rv_empty_state)
  RelativeLayout mEmptyState;
  @BindView(R.id.tv_class_report_toolbar)
  TextView mToolBarTV;
  @BindView(R.id.class_report_toolbar)
  Toolbar mToolbar;

  // ref of Adapter
  private ClassReportAdapter mAdapter;
  // ref of passed subjectID
  private int passedSubjectID = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_class_reports);
    // bind views
    ButterKnife.bind(this);
    // Setup toolbar
    setSupportActionBar(mToolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    // get passed SubjectID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_TO_CLASS_REPORT)) {
      passedSubjectID = getIntent().getIntExtra(UtilsConstants.EXTRA_SUBJECT_TO_CLASS_REPORT, -1);
    }
    // get subject name
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_NAME_TO_CLASS_REPORT)) {
      String subjectName = getIntent()
          .getStringExtra(UtilsConstants.EXTRA_SUBJECT_NAME_TO_CLASS_REPORT);
      mToolBarTV.setText(subjectName);
    }

  }

  @Override
  protected void onResume() {
    super.onResume();
    displayData(passedSubjectID);
  }

  private void displayData(int subjectID) {
    LocalDataSource.getInstance(this).getClasses(subjectID, new LoadClassesCallBack() {
      @Override
      public void onClassesLoaded(List<ClassEntity> data) {
        mEmptyState.setVisibility(View.INVISIBLE);
        mReportsRV.setVisibility(View.VISIBLE);
        mReportsRV.setLayoutManager(new LinearLayoutManager(ClassReports.this));
        mReportsRV.setHasFixedSize(true);
        mAdapter = new ClassReportAdapter(data, ClassReports.this, ClassReports.this);
        mReportsRV.setAdapter(mAdapter);
      }

      @Override
      public void onClassesDataNotAvailable() {
        mEmptyState.setVisibility(View.VISIBLE);
        mReportsRV.setVisibility(View.INVISIBLE);
      }
    });
  }

  @Override
  public void onClick(int classID, String className) {
    Intent intent = new Intent(ClassReports.this, StudentsReports.class);
    intent.putExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_REPORT, classID);
    intent.putExtra(UtilsConstants.EXTRA_CLASS_NAME_TO_STUDENT_REPORT, className);
    intent.putExtra(UtilsConstants.EXTRA_SUBJECT_TO_CLASS_REPORT, passedSubjectID);
    startActivity(intent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        Intent subjectIntent = new Intent(ClassReports.this, SubjectActivity.class);
        subjectIntent.setFlags
            (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(subjectIntent);

        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
