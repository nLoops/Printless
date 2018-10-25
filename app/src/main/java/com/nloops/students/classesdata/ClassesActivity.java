package com.nloops.students.classesdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.ClassesAdapter;
import com.nloops.students.adapters.ClassesAdapter.OnClassClickListener;
import com.nloops.students.classesdata.ClassDataContract.ClassPresenter;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.students.StudentActivity;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.utils.UtilsMethods;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import java.util.ArrayList;
import java.util.List;

public class ClassesActivity extends AppCompatActivity implements ClassDataContract.ClassView,
    OnClassClickListener {

  // ref of layout views
  @BindView(R.id.rv_classes_activity)
  RecyclerView mRecyclerView;
  @BindView(R.id.subject_rv_empty_state)
  RelativeLayout mRecyclerEmptyState;
  @BindView(R.id.classes_fab)
  FloatingActionButton mClassFAB;
  @BindView(R.id.classes_layout_container)
  CoordinatorLayout mLayoutContainer;

  // ref of data adapter
  private ClassesAdapter mAdapter;

  // ref of Presenter
  private ClassDataPresenter mPresenter;

  // ref of passed subjectID
  private int passedSubjectID = -1;

  // constants for activity request
  private static final int ACTIVITY_REQUEST_CODE = 201;

  // ref of PowerMenu
  PowerMenu powerMenu;

  // ref of classID and ClassPosition
  private int classID = -1;

  private int classPos = -1;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_classes);
    // bind all views to the code
    ButterKnife.bind(this);
    // get passed subjectID
    if (getIntent().hasExtra(UtilsConstants.EXTRA_SUBJECT_ID_TO_CLASSES)) {
      passedSubjectID = getIntent().getIntExtra(UtilsConstants.EXTRA_SUBJECT_ID_TO_CLASSES, -1);
    }

    // setup presenter
    mPresenter = new ClassDataPresenter(LocalDataSource.getInstance(this),
        this, passedSubjectID);
    // setup RecyclerView
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setHasFixedSize(true);
    // prepare Adapter
    mAdapter = new ClassesAdapter(null, this, this);
    mRecyclerView.setAdapter(mAdapter);

    mClassFAB.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        showAddNewClass();
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @Override
  public void showClassesList(List<ClassEntity> data) {
    mRecyclerView.setVisibility(View.VISIBLE);
    mRecyclerEmptyState.setVisibility(View.INVISIBLE);
    mAdapter.setClassesAdapterData(data);
  }

  @Override
  public void showEmptyState() {
    mRecyclerView.setVisibility(View.INVISIBLE);
    mRecyclerEmptyState.setVisibility(View.VISIBLE);
  }

  @Override
  public void showAddNewClass() {
    Intent classIntent = new Intent(ClassesActivity.this, ClassAddEdit.class);
    classIntent.putExtra(UtilsConstants.EXTRA_CLASS_ID_INTENT, -1);
    classIntent.putExtra(UtilsConstants.EXTRA_CLASS_TO_EDIT_SUBJECT_ID, passedSubjectID);
    startActivityForResult(classIntent, ACTIVITY_REQUEST_CODE);
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
  public void showEditClass(int classID) {
    Intent classIntent = new Intent(ClassesActivity.this, ClassAddEdit.class);
    classIntent.putExtra(UtilsConstants.EXTRA_CLASS_ID_INTENT, classID);
    classIntent.putExtra(UtilsConstants.EXTRA_CLASS_TO_EDIT_SUBJECT_ID, passedSubjectID);
    startActivityForResult(classIntent, ACTIVITY_REQUEST_CODE);
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
    items.add(new PowerMenuItem(getString(R.string.pop_menu_new_att), false));
    items.add(new PowerMenuItem(getString(R.string.pop_menu_edit_att), false));
    items.add(new PowerMenuItem(getString(R.string.pop_menu_delete), false));
    items.add(new PowerMenuItem(getString(R.string.pop_menu_rename), false));
    // get power menu
    powerMenu = UtilsMethods
        .getPowerMenu(items, onMenuItemClickListener, ClassesActivity.this);


  }

  @Override
  public void showStudentsActivity(int classID) {
    Intent studentIntent = new Intent(ClassesActivity.this, StudentActivity.class);
    studentIntent.putExtra(UtilsConstants.EXTRA_CLASS_TO_STUDENT_ID, classID);
    startActivity(studentIntent);
  }

  private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
    @Override
    public void onItemClick(int position, PowerMenuItem item) {

      if (item.getTitle().equals(getString(R.string.pop_menu_rename))) {
        if (classID != -1) {
          // hide menu
          handlePopupVisibility();
          // open edit page
          showEditClass(classID);
        }
      } else if (item.getTitle().equals(getString(R.string.pop_menu_delete))) {
        // hide menu
        handlePopupVisibility();
        // get the clicked item to delete it
        ClassEntity entity = mAdapter.getClassEntity(classPos);
        // perform delete
        mPresenter.deleteClass(entity);
      }
    }
  };

  @Override
  public void setPresenter(ClassPresenter presenter) {
    //
  }

  @Override
  public void onOverFlowClicked(int classID, View view, int adapterPosition) {
    this.classID = classID;
    this.classPos = adapterPosition;
    // if menu is visible we hide it, if not we show it.
    handlePopupVisibility();
    // menu size and position.
    powerMenu.showAsDropDown(view, -370, 0);
    // make custom width of menu.
    int width = (int) (powerMenu.getContentViewWidth() * 1.5);
    powerMenu.setWidth(width);
  }

  @Override
  public void onClassClicked(int classID) {
    showStudentsActivity(classID);
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
}
