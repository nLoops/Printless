package com.nloops.students.adapters;

import android.content.Context;
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
import com.nloops.students.data.mvp.StructureDataSource.LoadClassesCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.SubjectEntity;
import com.nloops.students.subjects.SubjectActivity;
import java.util.List;

/**
 * This Adapter will handle data to display on {@link SubjectActivity} RecyclerView
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectViewHolder> {

  public interface OnSubjectClickListener {

    void onSubjectClicked(int subjectID, View view, int adapterPosition);

    void onItemClicked(int subjectID, String subjectName);
  }

  // ref for List of Subjects.
  private List<SubjectEntity> subjectEntityList;

  // ref of Adapter clickListener
  private OnSubjectClickListener mClickListener;

  // ref of Context
  private Context mContext;

  public SubjectAdapter(List<SubjectEntity> dataList, OnSubjectClickListener listener,
      Context context) {
    this.subjectEntityList = dataList;
    this.mClickListener = listener;
    this.mContext = context;
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
  public void onBindViewHolder(@NonNull final SubjectViewHolder holder, int i) {

    // get Current Subject on the loop position
    SubjectEntity currentSubject = subjectEntityList.get(i);
    // assign UI views Values
    holder.mSubjectTV.setText(currentSubject.getSubjectName());
    holder.mSchoolTV.setText(currentSubject.getSchoolName());
    // calculate total of classes under this subject
    LocalDataSource.getInstance(mContext).getClasses(currentSubject.getSubjectID(),
        new LoadClassesCallBack() {
          @Override
          public void onClassesLoaded(List<ClassEntity> data) {
            String count = "Classes " + "(" + data.size() + ")";
            holder.mClassTV.setText(count);
          }

          @Override
          public void onClassesDataNotAvailable() {
            holder.mClassTV.setText(mContext.getString(R.string.zero_classes));
          }
        });

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

  public void addItem(SubjectEntity entity) {
    subjectEntityList.add(entity);
    notifyDataSetChanged();
  }

  public void deleteItem(SubjectEntity entity) {
    subjectEntityList.remove(entity);
    notifyDataSetChanged();
  }

  public boolean isLastItem(int position) {
    return getSubject(position) == subjectEntityList.get(subjectEntityList.size() - 1);
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


  private void performClickItem(SubjectViewHolder holder) {
    SubjectEntity entity = getSubject(holder.getAdapterPosition());
    mClickListener.onItemClicked(entity.getSubjectID(), entity.getSubjectName());
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
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == mOverflowIB) {
        performClickAction(this, v);
      } else if (v == itemView) {
        performClickItem(this);
      }
    }
  }

}
