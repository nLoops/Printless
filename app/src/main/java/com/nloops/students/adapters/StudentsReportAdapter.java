package com.nloops.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.StudentsReportAdapter.StudentReportVH;
import com.nloops.students.data.mvp.StructureDataSource.LoadAbsenteeCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;

public class StudentsReportAdapter extends Adapter<StudentReportVH> {

  // ref of list data
  private List<StudentEntity> mStudentsData;
  // ref of context
  private Context mContext;
  // ref of class
  private int passedClass;

  public StudentsReportAdapter(List<StudentEntity> data, Context context, int classID) {
    this.mStudentsData = data;
    this.mContext = context;
    this.passedClass = classID;
  }

  @NonNull
  @Override
  public StudentReportVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutID = R.layout.students_report_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View rootView = inflater.inflate(layoutID, viewGroup, false);
    return new StudentReportVH(rootView);
  }

  @Override
  public void onBindViewHolder(@NonNull final StudentReportVH holder, int position) {
    final StudentEntity entity = getStudentEntity(position);
    holder.mStudentName.setText(entity.getStudentName());
    holder.mStudentUID.setText(entity.getStudentUniID());

    LocalDataSource.getInstance(mContext).getAllAbsenteeByClass(passedClass,
        new LoadAbsenteeCallBack() {
          @Override
          public void onAbsenteeLoaded(List<AbsenteeEntity> data) {
            int counter = 0;
            for (int i = 0; i < data.size(); i++) {
              List<StudentEntity> entities = data.get(i).getStudentsList();
              for (StudentEntity studentEntity : entities) {
                if (studentEntity.getStudentID() == entity.getStudentID()
                    && studentEntity.isAttendanceOkay()) {
                  counter++;
                }
              }
            }
            // calculate percentage
            int percentage = (int) (counter * 100.0f) / data.size();
            if (percentage >= 50) {
              holder.mAbsenteeCount.setTextColor
                  (mContext.getResources().getColor(R.color.colorRed));
              holder.mAbsenteePercentage.setTextColor
                  (mContext.getResources().getColor(R.color.colorRed));
            }
            holder.mAbsenteeCount.setText
                (mContext.getString(R.string.report_absentee_count, counter));
            String strPercentage = mContext.getString
                (R.string.report_absentee_percentage, percentage) + "%)";
            holder.mAbsenteePercentage.setText(strPercentage);
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
    return mStudentsData == null ? 0 : mStudentsData.size();
  }

  public StudentEntity getStudentEntity(int position) {
    return mStudentsData.get(position);
  }

  public void setStudentsData(List<StudentEntity> data) {
    mStudentsData = null;
    mStudentsData = data;
    notifyDataSetChanged();

  }

  class StudentReportVH extends ViewHolder {

    @BindView(R.id.report_list_student_name)
    TextView mStudentName;
    @BindView(R.id.report_list_student_uid)
    TextView mStudentUID;
    @BindView(R.id.report_list_student_absentee_count)
    TextView mAbsenteeCount;
    @BindView(R.id.report_list_student_absentee_percentage)
    TextView mAbsenteePercentage;

    public StudentReportVH(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
