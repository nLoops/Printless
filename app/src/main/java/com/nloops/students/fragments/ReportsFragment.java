package com.nloops.students.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.ReportSubjectAdapter;
import com.nloops.students.data.mvp.StructureDataSource.LoadSubjectsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;
import java.util.Objects;


public class ReportsFragment extends Fragment {

  // bind views to class
  @BindView(R.id.rv_report_activity)
  RecyclerView mReportsRV;
  @BindView(R.id.reports_rv_empty_state)
  RelativeLayout mEmptyState;
  // ref of context
  private Context mContext;
  // ref of data adapter
  private ReportSubjectAdapter mAdapter;

  public ReportsFragment() {
    // required by system
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
    View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    displayDate();
  }

  private void displayDate() {
    LocalDataSource.getInstance(mContext).getSubjects(new LoadSubjectsCallBack() {
      @Override
      public void onSubjectsLoaded(List<SubjectEntity> subjects) {
        mEmptyState.setVisibility(View.INVISIBLE);
        mReportsRV.setVisibility(View.VISIBLE);
        mReportsRV.setLayoutManager(new LinearLayoutManager(mContext));
        mReportsRV.setHasFixedSize(true);
        mAdapter = new ReportSubjectAdapter(subjects, mContext);
        mReportsRV.setAdapter(mAdapter);
      }

      @Override
      public void onSubjectDataNotAvailable() {
        mEmptyState.setVisibility(View.VISIBLE);
        mReportsRV.setVisibility(View.INVISIBLE);
      }
    });
  }


}
