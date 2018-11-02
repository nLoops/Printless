package com.nloops.students.adapters;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.AttendanceAdapter.AttendanceViewHolder;
import com.nloops.students.data.tables.StudentEntity;
import com.nloops.students.utils.UtilsConstants;
import java.util.List;

public class AttendanceAdapter extends Adapter<AttendanceViewHolder> {

  private List<StudentEntity> mStudents;

  public AttendanceAdapter(List<StudentEntity> models) {
    this.mStudents = models;
  }

  private void checkBoxClicked(AttendanceViewHolder holder) {
    StudentEntity entity = getStudent(holder.getAdapterPosition());
    int state = UtilsConstants.STUDENT_ABSENTEE_NO;
    if (holder.mStudentActionCB.isChecked()) {
      state = UtilsConstants.STUDENT_ABSENTEE_OKAY;
    }
    entity.setAttendanceState(state);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutID = R.layout.attendance_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View retView = inflater.inflate(layoutID, viewGroup, false);
    return new AttendanceViewHolder(retView);
  }

  @Override
  public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int i) {

    // get current student in loop
    StudentEntity student = getStudent(i);
    holder.mStudentName.setText(student.getStudentName());
    holder.mStudentUID.setText(student.getStudentUniID());
    holder.mStudentActionCB.setChecked(student.isAttendanceOkay());
    if (student.isAttendanceOkay()) {
      holder.mStudentName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    } else {
      holder.mStudentName.setPaintFlags(0);
    }
  }

  @Override
  public int getItemCount() {
    return mStudents == null ? 0 : mStudents.size();
  }

  public StudentEntity getStudent(int pos) {
    return mStudents.get(pos);
  }

  public void setStudentData(List<StudentEntity> data) {
    mStudents = null;
    mStudents = data;
    notifyDataSetChanged();
  }

  public List<StudentEntity> getStudentEntity() {
    return mStudents;
  }

  class AttendanceViewHolder extends ViewHolder implements OnClickListener {

    // ref of views
    @BindView(R.id.tv_att_student_name)
    TextView mStudentName;
    @BindView(R.id.tv_att_student_uid)
    TextView mStudentUID;
    @BindView(R.id.cb_att_student)
    CheckBox mStudentActionCB;

    public AttendanceViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mStudentActionCB.setOnClickListener(this);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == mStudentActionCB) {
        checkBoxClicked(this);
      } else if (v == itemView) {
        mStudentActionCB.setChecked(!mStudentActionCB.isChecked());
        checkBoxClicked(this);
      }
    }
  }

}
