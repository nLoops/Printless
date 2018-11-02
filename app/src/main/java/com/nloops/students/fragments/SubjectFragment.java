package com.nloops.students.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.adapters.SubjectAdapter;
import com.nloops.students.adapters.SubjectAdapter.OnSubjectClickListener;
import com.nloops.students.classesdata.ClassesActivity;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.subjects.SubjectAddEdit;
import com.nloops.students.subjects.SubjectPresenter;
import com.nloops.students.subjects.SubjectPresenterContract;
import com.nloops.students.subjects.SubjectPresenterContract.Presenter;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.utils.UtilsMethods;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This Activity will Display List of Subjects stored into Database we can add, edit, delete subject
 * from this list will using {@link SubjectPresenter} and {@link SubjectPresenterContract.View} to
 * handle actions in this Activity and apply the MVP Architecture
 */
public class SubjectFragment extends Fragment implements
    SubjectPresenterContract.View, OnSubjectClickListener {

  // declare global variables
  @BindView(R.id.rv_subject_activity)
  RecyclerView mSubjectRV;
  @BindView(R.id.subject_rv_empty_state)
  RelativeLayout mRecyclerEmptyState;
  @BindView(R.id.subject_layout_container)
  CoordinatorLayout layoutContainer;

  // ref of context
  private Context mContext;
  // object from SubjectAdapter to feed our RecyclerView with data
  private SubjectAdapter mAdapter;

  // object of SubjectPresenter
  private SubjectPresenter mPresenter;

  // ref of constant activity ref to get info back from the others
  private static final int ACTIVITY_REQUEST_CODE = 101;

  // ref of popup menu
  private PowerMenu powerMenu;

  // ref of subject ID
  private int mSubjectID = -1;

  // ref of Subject Adapter ArrayList position.
  private int mSubjectPosition = -1;


  public SubjectFragment() {
    // empty constructor required by system.
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = Objects.requireNonNull(getContext()).getApplicationContext();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.activity_subject, container, false);
    ButterKnife.bind(this, rootView);
    setupPresenter();
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.start();
  }

  private void setupPresenter() {
    mPresenter = new SubjectPresenter(LocalDataSource.getInstance
        (mContext)
        , this);
    // Prepare RecyclerView
    mSubjectRV.setLayoutManager(new LinearLayoutManager(mContext));
    mSubjectRV.setHasFixedSize(true);
    // Prepare Adapter
    mAdapter = new SubjectAdapter(null, this, mContext);
    mSubjectRV.setAdapter(mAdapter);
  }

  @OnClick(R.id.subject_fab)
  public void showAddSubject(FloatingActionButton fab) {
    showAddNewSubject();
  }

  @Override
  public void onSubjectClicked(int subjectID, View view, int adapterPosition) {
    // first we're passing clicked item subjectID in case we need to launch SubjectDetails Activity
    mSubjectID = subjectID;
    // pass subject position it's useful into delete method
    mSubjectPosition = adapterPosition;
    // if menu is visible we hide it, if not we show it.
    handlePopupVisibility();
    // menu size and position.
    powerMenu.showAsDropDown(view, -370, 0);
  }

  @Override
  public void onItemClicked(int subjectID) {
    showClassesActivity(subjectID);
  }

  @Override
  public void showSubjectsList(List<SubjectEntity> data) {
    mSubjectRV.setVisibility(View.VISIBLE);
    mRecyclerEmptyState.setVisibility(View.INVISIBLE);
    mAdapter.setSubjectAdapterData(data);
  }

  @Override
  public void showEmptyState() {
    mSubjectRV.setVisibility(View.INVISIBLE);
    mRecyclerEmptyState.setVisibility(View.VISIBLE);
  }

  @Override
  public void showAddNewSubject() {
    Intent subjectIntent = new Intent(mContext, SubjectAddEdit.class);
    subjectIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_ID_INTENT, -1);
    startActivityForResult(subjectIntent, ACTIVITY_REQUEST_CODE);
  }

  @Override
  public void showEditSubject(int subjectID) {
    Intent subjectIntent = new Intent(mContext, SubjectAddEdit.class);
    subjectIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_ID_INTENT, subjectID);
    startActivityForResult(subjectIntent, ACTIVITY_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    Snackbar.make(layoutContainer, message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void showDeletedMessage() {
    showResultMessage(getString(R.string.snack_deleted_success));
  }

  @Override
  public void setupPopupMenu() {
    List<PowerMenuItem> items = new ArrayList();
    items.add(new PowerMenuItem(getString(R.string.pop_menu_preset), false));
    items.add(new PowerMenuItem(getString(R.string.pop_menu_delete), false));
    items.add(new PowerMenuItem(getString(R.string.pop_menu_rename), false));
    powerMenu = UtilsMethods.getPowerMenu
        (items, onMenuItemClickListener, mContext);
  }

  @Override
  public void showClassesActivity(int subjectID) {
    Intent classIntent = new Intent(mContext, ClassesActivity.class);
    classIntent.putExtra(UtilsConstants.EXTRA_SUBJECT_ID_TO_CLASSES, subjectID);
    startActivity(classIntent);
  }

  @Override
  public void setPresenter(Presenter presenter) {
    mPresenter = (SubjectPresenter) presenter;
  }

  private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
    @Override
    public void onItemClick(int position, PowerMenuItem item) {
      if (item.getTitle().equals(getString(R.string.pop_menu_rename))) {
        if (mSubjectID != -1) {
          // 1- hide menu.
          handlePopupVisibility();
          // 2- launch SubjectAddEdit intent.
          showEditSubject(mSubjectID);
        }
      } else if (item.getTitle().equals(getString(R.string.pop_menu_delete))) {
        // 1- hide menu
        handlePopupVisibility();
        // 2- get Subject object using it's position into adapter
        SubjectEntity subject = mAdapter.getSubject(mSubjectPosition);
        // 3- delete from DB operation
        mPresenter.deleteSubject(subject);
      }
    }
  };

  /**
   * Helper method to change popup menu visibility states.
   */
  private void handlePopupVisibility() {
    if (powerMenu.isShowing()) {
      powerMenu.dismiss();
    }
  }
}
