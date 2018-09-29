package com.nloops.students.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.activities.SubjectActivity;
import com.nloops.students.adapters.SubjectAdapter.SubjectViewHolder;
import com.nloops.students.data.tables.SubjectEntity;
import java.util.List;

/**
 * This Adapter will handle data to display on {@link SubjectActivity} RecyclerView
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectViewHolder> {

  // ref for List of Subjects.
  private List<SubjectEntity> subjectEntityList;


  public SubjectAdapter(List<SubjectEntity> dataList) {
    this.subjectEntityList = dataList;
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
  class SubjectViewHolder extends ViewHolder {

    @BindView(R.id.tv_subject_name)
    TextView mSubjectTV;
    @BindView(R.id.tv_school_name)
    TextView mSchoolTV;
    @BindView(R.id.tv_class_count)
    TextView mClassTV;

    public SubjectViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
