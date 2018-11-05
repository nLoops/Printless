package com.nloops.students.attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.adapters.AttendanceAdapter;
import com.nloops.students.attendance.AttendanceContract.Presenter;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.utils.UtilsMethods;
import com.nloops.students.views.FinishDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity implements
    AttendanceContract.View, OnDateSelectedListener {

  @BindView(R.id.attendance_rv)
  RecyclerView mAttRV;
  @BindView(R.id.cv_attendance)
  MaterialCalendarView mCalendarView;
  @BindView(R.id.tv_att_date_today)
  TextView mSelectedDateTV;

  // ref of data adapter
  private AttendanceAdapter mAdapter;
  // ref of presenter
  private AttendancePresenter mPresenter;
  /*FORMATTER to display Selected Date on ToolBar Title*/
  private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
  // ref to hold date
  private long mAttendanceLong = Long.MAX_VALUE;
  // class id
  int classID;
  // subject id
  int passedSubjectID;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_attendance);
    // bind views
    ButterKnife.bind(this);
    // prepare presenter
    preparePresenter();
    // setup calendar
    /*Calendar View Setup*/
    mCalendarView.setOnDateChangedListener(this);
    mCalendarView.setSelectedDate(Calendar.getInstance().getTime());
    CalendarDay day = CalendarDay.from(Calendar.getInstance().getTime());
    mSelectedDateTV.setText(FORMATTER.format(day.getDate()));
    mAttendanceLong = day.getDate().getTime();

  }

  private void preparePresenter() {
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_ID_TO_ATTENDANCE)) {
      classID = getIntent().getIntExtra(UtilsConstants.EXTRA_CLASS_ID_TO_ATTENDANCE, -1);
      passedSubjectID = getIntent()
          .getIntExtra(UtilsConstants.EXTRA_SUBJECT_ID_CLASS_TO_ATTENDANCE, -1);
      mPresenter = new AttendancePresenter(LocalDataSource.getInstance(this),
          this, classID);
      mAttRV.setLayoutManager(new LinearLayoutManager(this));
      mAttRV.setHasFixedSize(true);
      mAdapter = new AttendanceAdapter(null);
      mAttRV.setAdapter(mAdapter);
    }
  }

  @OnClick(R.id.attendance_finish_bt)
  public void saveAttendance(Button button) {
    AbsenteeEntity entity = new AbsenteeEntity(getAttendanceDate(), mAdapter.getStudentEntity(),
        classID, passedSubjectID);
    mPresenter.insertAttendance(entity);
    FragmentManager fm = getSupportFragmentManager();
    FinishDialog finishDialog = new FinishDialog();
    finishDialog.show(fm, "finished");
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!getIntent().hasExtra(UtilsConstants.EXTRA_SET_ATTENDANCE_EDIT_MODE)) {
      mPresenter.loadStudents();
    } else {
      mPresenter.loadAbsenteeByDate(mAttendanceLong, classID);
    }
  }


  @Override
  public void setPresenter(Presenter presenter) {
    //
  }

  @Override
  public void showStudentsList(List<StudentEntity> data) {
    List<StudentEntity> studentEntities = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      StudentEntity entity = new StudentEntity(data.get(i).getStudentID(),
          data.get(i).getStudentName(), data.get(i).getStudentUniID(),
          data.get(i).getForeignClassID(),
          data.get(i).getAttendanceState(),
          data.get(i).getForeignSubjectID());
      studentEntities.add(entity);
    }
    mAdapter.setStudentData(studentEntities);
  }

  @Override
  public void showAbsenteeList(List<AbsenteeEntity> data) {

  }

  @Override
  public void clearAdapter() {
    mAdapter.setStudentData(null);
  }

  @Override
  public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
      @NonNull CalendarDay calendarDay, boolean b) {
    mSelectedDateTV.setText(b ? FORMATTER.format(calendarDay.getDate()) : "No Selection");
    UtilsMethods.slideInFromTop(mSelectedDateTV, AttendanceActivity.this);
    mAttendanceLong = calendarDay.getDate().getTime();
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SET_ATTENDANCE_EDIT_MODE)) {
      mPresenter.loadAbsenteeByDate(mAttendanceLong, classID);
    }

  }


  private long getAttendanceDate() {
    return mAttendanceLong;
  }
}
