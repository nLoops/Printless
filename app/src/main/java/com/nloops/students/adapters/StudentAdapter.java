package com.nloops.students.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.StudentAdapter.StudentViewHolder;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;

public class StudentAdapter extends Adapter<StudentViewHolder> {

  public interface OnStudentClickListener {

    void onOverFlowClicked(int studentID, View view, int adapterPos);

    void onStudentClick(int studentID);
  }

  // list of data ref
  private List<StudentEntity> mStudentEntities;
  // ref of click listener
  private OnStudentClickListener mListener;

  public StudentAdapter(List<StudentEntity> data, OnStudentClickListener listener) {
    this.mStudentEntities = data;
    this.mListener = listener;
  }

  private void performClickAction(StudentViewHolder holder, View view) {
    StudentEntity entity = getStudentEntity(holder.getAdapterPosition());
    mListener.onOverFlowClicked(entity.getStudentID(), view, holder.getAdapterPosition());
  }


  private void performClickItem(StudentViewHolder holder) {
    StudentEntity entity = getStudentEntity(holder.getAdapterPosition());
    mListener.onStudentClick(entity.getStudentID());
  }

  @NonNull
  @Override
  public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutID = R.layout.student_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View item = inflater.inflate(layoutID, viewGroup, false);
    return new StudentViewHolder(item);
  }

  @Override
  public void onBindViewHolder(@NonNull StudentViewHolder holder, int i) {
    StudentEntity entity = getStudentEntity(i);
    holder.mStudentName.setText(entity.getStudentName());
    holder.mStudentUID.setText(entity.getStudentUniID());
  }

  @Override
  public int getItemCount() {
    return mStudentEntities == null ? 0 : mStudentEntities.size();
  }

  public StudentEntity getStudentEntity(int position) {
    return mStudentEntities.get(position);
  }

  public void setStudentAdapterData(List<StudentEntity> data) {
    mStudentEntities = null;
    mStudentEntities = data;
    notifyDataSetChanged();

  }

  class StudentViewHolder extends ViewHolder implements OnClickListener {

    // ViewHolder views
    @BindView(R.id.tv_student_name)
    TextView mStudentName;
    @BindView(R.id.tv_student_uid)
    TextView mStudentUID;
    @BindView(R.id.ib_student_overflow)
    ImageButton mOverFlow;

    public StudentViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mOverFlow.setOnClickListener(this);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == mOverFlow) {
        performClickAction(this, v);
      } else if (v == itemView) {
        performClickItem(this);
      }
    }
  }

}
