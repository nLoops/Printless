package com.nloops.students.students;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.adapters.StudentAdapter;
import com.nloops.students.adapters.StudentAdapter.OnStudentClickListener;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.students.StudentContract.Presenter;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.utils.UtilsMethods;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity implements
    StudentContract.View, OnStudentClickListener {

  // ref of activity views
  @BindView(R.id.rv_student_activity)
  RecyclerView mStudentRV;
  @BindView(R.id.subject_rv_empty_state)
  RelativeLayout mRecyclerViewEmptyView;
  @BindView(R.id.student_layout_container)
  CoordinatorLayout mLayoutContainer;
  @BindView(R.id.general_toolbar)
  Toolbar mToolBar;
  @BindView(R.id.tv_general_toolbar)
  TextView mToolBarTV;

  // ref of StudentAdapter
  private StudentAdapter mAdapter;

  // ref of Presenter
  private StudentPresenter mPresenter;

  // ref of passed class id
  private int passedClassID = -1;

  // subject ID
  private int passedSubjectID = -1;

  // constants for activity request
  private static final int ACTIVITY_REQUEST_CODE = 301;

  // ref of PowerMenu
  PowerMenu powerMenu;

  // ref of studentID and StudentPosition
  private int studentID = -1;

  private int studentPos = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student);
    // bind all Views
    ButterKnife.bind(this);
    // Setup toolbar
    setSupportActionBar(mToolBar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_NAME_TO_STUDENT)) {
      mToolBarTV.setText(getIntent().getStringExtra(UtilsConstants.EXTRA_CLASS_NAME_TO_STUDENT));
    }
    // setup presenter
    setupPresenter();
  }

  private void setupPresenter() {
    // get passed classID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID)) {
      passedClassID = getIntent().getIntExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID, -1);
    }
    // get passed subject ID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_ID_CLASS_TO_STUDENT)) {
      passedSubjectID = getIntent()
          .getIntExtra(UtilsConstants.EXTRA_SUBJECT_ID_CLASS_TO_STUDENT, -1);
    }
    // Define Presenter
    mPresenter = new StudentPresenter(LocalDataSource.getInstance(this), this, passedClassID);
    // setup RecyclerView
    mStudentRV.setLayoutManager(new LinearLayoutManager(this));
    mStudentRV.setHasFixedSize(true);
    // prepare adapter
    mAdapter = new StudentAdapter(null, this);
    mStudentRV.setAdapter(mAdapter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @OnClick(R.id.student_fab)
  public void showEditStudent(FloatingActionButton fab) {
    showAddNewStudent();
  }

  @Override
  public void showStudentList(List<StudentEntity> data) {
    mStudentRV.setVisibility(View.VISIBLE);
    mRecyclerViewEmptyView.setVisibility(View.INVISIBLE);
    mAdapter.setStudentAdapterData(data);
  }

  @Override
  public void showEmptyState() {
    mStudentRV.setVisibility(View.INVISIBLE);
    mRecyclerViewEmptyView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showAddNewStudent() {
    Intent studentIntent = new Intent(StudentActivity.this, StudentEditActivity.class);
    studentIntent.putExtra(UtilsConstants.EXTRA_STUDENT_ID_INTENT, -1);
    studentIntent.putExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID, passedClassID);
    studentIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_ID_CLASS_TO_STUDENT, passedSubjectID);
    startActivityForResult(studentIntent, ACTIVITY_REQUEST_CODE);
  }

  @Override
  public void showEditStudent(int studentID) {
    Intent studentIntent = new Intent(StudentActivity.this, StudentEditActivity.class);
    studentIntent.putExtra(UtilsConstants.EXTRA_STUDENT_ID_INTENT, studentID);
    studentIntent.putExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID, passedClassID);
    startActivityForResult(studentIntent, ACTIVITY_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ACTIVITY_REQUEST_CODE) {
      if (resultCode == UtilsConstants.RESULT_ADD_ITEM) {
        showResultMessage(getString(R.string.snack_added_success));

      } else if (resultCode == UtilsConstants.RESULT_EDIT_ITEM) {
        showResultMessage(getString(R.string.snack_edited_success));

      }
    }
  }

  @Override
  public void showResultMessage(String message) {
    Snackbar.make(mLayoutContainer, message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void showDeletedMessage() {
    showResultMessage(getString(R.string.snack_deleted_success));
  }

  @Override
  public void setupPopupMenu() {
    List<PowerMenuItem> items = new ArrayList<>();
    items.add(new PowerMenuItem(getString(R.string.pop_menu_rename), false));
    items.add(new PowerMenuItem(getString(R.string.pop_menu_delete), false));
    // get power menu
    powerMenu = UtilsMethods
        .getPowerMenu(items, onMenuItemClickListener, StudentActivity.this);

  }

  private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
    @Override
    public void onItemClick(int position, PowerMenuItem item) {

      if (item.getTitle().equals(getString(R.string.pop_menu_rename))) {
        if (studentID != -1) {
          // hide menu
          handlePopupVisibility();
          // open edit page
          showEditStudent(studentID);
        }
      } else if (item.getTitle().equals(getString(R.string.pop_menu_delete))) {
        // hide menu
        handlePopupVisibility();
        // get the clicked item to delete it
        StudentEntity entity = mAdapter.getStudentEntity(studentPos);
        // perform delete
        mPresenter.deleteStudent(entity);
      }
    }
  };

  @Override
  public void setPresenter(Presenter presenter) {
    // for future implementation.
  }

  /**
   * Helper method to change popup menu visibility states.
   */
  private void handlePopupVisibility() {
    if (powerMenu.isShowing()) {
      powerMenu.dismiss();
      return;
    }
  }


  @Override
  public void onOverFlowClicked(int studentID, View view, int adapterPos) {
    this.studentID = studentID;
    this.studentPos = adapterPos;
    // if menu is visible we hide it, if not we show it.
    handlePopupVisibility();
    // menu size and position.
    powerMenu.showAsDropDown(view, -370, 0);
  }

  @Override
  public void onStudentClick(int studentID) {
    // for future implementation.
  }
}
