package com.nloops.students.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.nloops.students.adapters.SubjectAdapter.SubjectViewHolder;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.subjects.SubjectActivity;
import java.util.List;

/**
 * This Adapter will handle data to display on {@link SubjectActivity} RecyclerView
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectViewHolder> {

  public interface OnSubjectClickListener {

    void onSubjectClicked(int subjectID, View view, int adapterPosition);
  }

  // ref for List of Subjects.
  private List<SubjectEntity> subjectEntityList;

  // ref of Adapter clickListener
  private OnSubjectClickListener mClickListener;


  public SubjectAdapter(List<SubjectEntity> dataList, OnSubjectClickListener listener) {
    this.subjectEntityList = dataList;
    this.mClickListener = listener;
  }

  @NonNull
  @Override
  public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    // link the adapter with the xml file we created and pass it to our view-holder
    // to link views then we can assign values to.
    int layoutID = R.layout.subject_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View returnView = inflater.inflate(layoutID, viewGroup, false);
    return new SubjectViewHolder(returnView);
  }

  @Override
  public void onBindViewHolder(@NonNull SubjectViewHolder holder, int i) {

    // get Current Subject on the loop position
    SubjectEntity currentSubject = subjectEntityList.get(i);
    // assign UI views Values
    holder.mSubjectTV.setText(currentSubject.getSubjectName());
    holder.mClassTV.setText(currentSubject.getSchoolName());
    holder.mSchoolTV.setText("Classes (9)");
  }

  @Override
  public int getItemCount() {
    return subjectEntityList == null ? 0 : subjectEntityList.size();
  }

  /**
   * Helper method to return Subject item in specific position
   *
   * @param position the subject pos in the array-list
   * @return {@link SubjectEntity}
   */
  public SubjectEntity getSubject(int position) {
    return subjectEntityList.get(position);
  }

  /**
   * This helper method will takes holder position and retrieve {@link SubjectEntity} in this
   * position to get it's ID to allow us pass it to get Subject Details.
   *
   * @param holder {@link SubjectViewHolder}
   */
  private void performClickAction(SubjectViewHolder holder, View view) {
    SubjectEntity entity = getSubject(holder.getAdapterPosition());
    mClickListener.onSubjectClicked(entity.getSubjectID(), view, holder.getAdapterPosition());
  }

  /**
   * This void will help to replace data-set from outside adapter class
   *
   * @param data {@link List<SubjectEntity>}
   */
  public void setSubjectAdapterData(List<SubjectEntity> data) {
    subjectEntityList = null;
    subjectEntityList = data;
    notifyDataSetChanged();

  }

  /**
   * This class will holds our xml file views we can access and assign values through it.
   */
  class SubjectViewHolder extends ViewHolder implements OnClickListener {

    @BindView(R.id.tv_subject_name)
    TextView mSubjectTV;
    @BindView(R.id.tv_school_name)
    TextView mSchoolTV;
    @BindView(R.id.tv_class_count)
    TextView mClassTV;
    @BindView(R.id.ib_overflow)
    ImageButton mOverflowIB;

    public SubjectViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mOverflowIB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      performClickAction(this, v);
    }
  }

}
