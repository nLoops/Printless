package com.nloops.students.reports;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.adapters.StudentsReportAdapter;
import com.nloops.students.data.mvp.StructureDataSource.LoadStudentsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.utils.StudentReportModel;
import com.nloops.students.utils.UtilsConstants;
import com.shidian.excel.ExcelUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import pub.devrel.easypermissions.EasyPermissions;

public class StudentsReports extends AppCompatActivity implements
    EasyPermissions.PermissionCallbacks {

  // bind views to class
  @BindView(R.id.rv_student_report_activity)
  RecyclerView mReportsRV;
  @BindView(R.id.reports_rv_empty_state)
  RelativeLayout mEmptyState;
  @BindView(R.id.tv_student_report_toolbar)
  TextView mToolBarTV;
  @BindView(R.id.student_report_toolbar)
  Toolbar mToolbar;

  private static final int PERMISSION_REQ_CODE = 225;

  // ref of data adapter
  private StudentsReportAdapter mAdapter;
  // ref of passed ClassID
  private int passedClassID = -1;

  private ArrayList<ArrayList<String>> recordList;
  private static String[] title = {"StudentName", "StudentUID", "Total Lectures", "AbsenteeCount",
      "Absentee Percentage"};
  private File file;
  private String fileName;

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
    // get permissions
    getPermissions();
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

  @OnClick(R.id.export_excel_bt)
  public void createExcel(Button button) {
    exportExcel();
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

  private String getSDPath() {
    File sdDir = null;
    boolean sdCardExist = Environment.getExternalStorageState().equals(
        android.os.Environment.MEDIA_MOUNTED);
    if (sdCardExist) {
      sdDir = Environment.getExternalStorageDirectory();
    }
    String dir = null;
    if (sdDir != null) {
      dir = sdDir.toString();
    }
    return dir;
  }

  public void makeDir(File dir) {
    if (!dir.getParentFile().exists()) {
      makeDir(dir.getParentFile());
    }
    dir.mkdir();
  }

  private void exportExcel() {
    getPermissions();
    file = new File(getSDPath() + "/studentapp");
    makeDir(file);
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        Locale.getDefault()).format(new Date());
    String filePath = "/Report_" + timeStamp + ".xls";
    ExcelUtils.initExcel(file.toString() + filePath, title);
    fileName = getSDPath() + "/studentapp" + filePath;
    ExcelUtils.writeObjListToExcel(getRecordData(), fileName, this);
    Uri mailUri = Uri.parse("file:///" + fileName);
    String[] sendTo = new String[]{"a.elgammal112@gmail.com"};
    composeEmail(sendTo, "Students Report", mailUri);
  }

  private ArrayList<ArrayList<String>> getRecordData() {
    recordList = new ArrayList<>();
    for (int i = 0; i < mAdapter.getmStudentModels().size(); i++) {
      StudentReportModel student = mAdapter.getmStudentModels().get(i);
      ArrayList<String> beanList = new ArrayList<String>();
      beanList.add(student.getStudentName());
      beanList.add(student.getStudentUID());
      beanList.add(student.getTotalLectures());
      beanList.add(student.getAbsenteeCount());
      beanList.add(student.getAbsenteePercantge());
      recordList.add(beanList);
    }
    return recordList;
  }


  public void composeEmail(String[] addresses, String subject, Uri attachment) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("application/excel");
    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    intent.putExtra(Intent.EXTRA_STREAM, attachment);
    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    }
  }

  /**
   * This Method will check if we have the required permissions to RECORD and SAVE files, if not we
   * will alert USER to get the permissions.
   */
  @TargetApi(23)
  private void getPermissions() {
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    if (!EasyPermissions.hasPermissions(StudentsReports.this, permissions)) {
      EasyPermissions.requestPermissions(this,
          getString(R.string.permissions_required),
          PERMISSION_REQ_CODE, permissions);
    }
  }

  @TargetApi(23)
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @Override
  public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    // will implemented soon
  }

  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    // TODO: 16/11/2018 implement the logic if user denied the perimissions
  }

}
