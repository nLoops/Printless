package com.nloops.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.ReportSubjectAdapter.ReportSubjectVH;
import com.nloops.students.data.mvp.StructureDataSource.LoadAbsenteeCallBack;
import com.nloops.students.data.mvp.StructureDataSource.LoadStudentsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

public class ReportSubjectAdapter extends Adapter<ReportSubjectVH> {

  public interface OnSubjectReportClickListener {

    void onSubjectClicked(int subjectID, String subjectName);
  }
  // ref of List that holds subject data, will passed in constructor
  private List<SubjectEntity> mSubjectsList;
  // ref of context
  private Context mContext;
  // ref of listener
  private OnSubjectReportClickListener mListener;

  public ReportSubjectAdapter(List<SubjectEntity> data, Context context,
      OnSubjectReportClickListener listener) {
    this.mSubjectsList = data;
    this.mContext = context;
    this.mListener = listener;
  }

  @NonNull
  @Override
  public ReportSubjectVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutID = R.layout.report_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View rootView = inflater.inflate(layoutID, viewGroup, false);
    return new ReportSubjectVH(rootView);
  }

  @Override
  public void onBindViewHolder(@NonNull final ReportSubjectVH holder, int position) {
    SubjectEntity entity = getSubject(position);
    holder.mSubjectName.setText(entity.getSubjectName());
    LocalDataSource.getInstance(mContext)
        .getStudentsBySubject(entity.getSubjectID(), new LoadStudentsCallBack() {
          @Override
          public void onStudentsLoaded(List<StudentEntity> data) {

            holder.mStudentCount
                .setText(mContext.getString(R.string.report_student_count, data.size()));
          }

          @Override
          public void onStudentsDataNotAvailable() {
            holder.mStudentCount.setText(mContext.getString(R.string.report_student_count, 0));
          }
        });

    LocalDataSource.getInstance(mContext)
        .getAllAbsenteeBySubject(entity.getSubjectID(), new LoadAbsenteeCallBack() {
          @Override
          public void onAbsenteeLoaded(List<AbsenteeEntity> data) {
            holder.mAbsenteeCount.setText
                (mContext.getString(R.string.report_absentee_count, data.size()));
          }

          @Override
          public void onAbsenteeDataNotAvailable() {
            holder.mAbsenteeCount.setText
                (mContext.getString(R.string.report_absentee_count, 0));
          }
        });

  }

  @Override
  public int getItemCount() {
    return mSubjectsList == null ? 0 : mSubjectsList.size();
  }

  /**
   * Helper method to return Subject item in specific position
   *
   * @param position the subject pos in the array-list
   * @return {@link SubjectEntity}
   */
  public SubjectEntity getSubject(int position) {
    return mSubjectsList.get(position);
  }

  /**
   * This void will help to replace data-set from outside adapter class
   *
   * @param data {@link List<SubjectEntity>}
   */
  public void setSubjectAdapterData(List<SubjectEntity> data) {
    mSubjectsList = null;
    mSubjectsList = data;
    notifyDataSetChanged();

  }

  private void performClicked(ReportSubjectVH holder) {
    SubjectEntity entity = getSubject(holder.getAdapterPosition());
    mListener.onSubjectClicked(entity.getSubjectID(), entity.getSubjectName());
  }

  class ReportSubjectVH extends ViewHolder implements OnClickListener {

    // bind views to class
    @BindView(R.id.report_list_subject_name)
    TextView mSubjectName;
    @BindView(R.id.report_list_student_count)
    TextView mStudentCount;
    @BindView(R.id.report_list_absentee_count)
    TextView mAbsenteeCount;

    public ReportSubjectVH(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == itemView) {
        performClicked(this);
      }
    }
  }

}
