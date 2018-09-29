package com.nloops.students.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.SubjectAdapter;
import com.nloops.students.data.AppDatabase;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

  private static final String TAG = SubjectActivity.class.getSimpleName();

  // declare global variables
  @BindView(R.id.rv_subject_activity)
  RecyclerView mSubjectRV;
  @BindView(R.id.subject_rv_empty_state)
  RelativeLayout mRecyclerEmptyState;
  @BindView(R.id.subject_fab)
  FloatingActionButton mAddSubjectFAB;

  // object from SubjectAdapter to feed our RecyclerView with data
  private SubjectAdapter mAdapter;

  // object of AppDatabase to help in our operations
  private AppDatabase mDB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subject);
    // link views using ButterKnife
    ButterKnife.bind(this);

    // Prepare RecyclerView
    mSubjectRV.setLayoutManager(new LinearLayoutManager(this));
    mSubjectRV.setHasFixedSize(true);
    // Prepare Adapter
    mAdapter = new SubjectAdapter(null);
    mSubjectRV.setAdapter(mAdapter);
    // get DB instance
    mDB = AppDatabase.getInstance(getApplicationContext());
    // call load data function
    loadData();

    mAddSubjectFAB.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(SubjectActivity.this, SubjectAddEdit.class));
      }
    });
  }


  /**
   * This helper void will getData from DB using LiveData this operation should done in the
   * background thread not on the main thread LiveData automatically run on the background
   */
  private void loadData() {
    Log.d(TAG, "Loading Subject data from DB");
    LiveData<List<SubjectEntity>> subjects = mDB.subjectDAO().loadAllSubjects();
    subjects.observe(this, new Observer<List<SubjectEntity>>() {
      @Override
      public void onChanged(@Nullable List<SubjectEntity> subjectEntities) {
        Log.d(TAG, "Receiving database update from LiveData");
        if (subjectEntities == null || subjectEntities.isEmpty()) {
          mRecyclerEmptyState.setVisibility(View.VISIBLE);
        } else {
          // set the data from DB to Adapter.
          mAdapter.setSubjectAdapterData(subjectEntities);
        }
      }
    });
  }

}
