package com.nloops.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.adapters.ClassesAdapter.ClassesViewHolder;
import com.nloops.students.data.mvp.StructureDataSource.LoadStudentsCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.ClassEntity;
import com.nloops.students.data.tables.StudentEntity;
import java.util.List;

public class ClassesAdapter extends Adapter<ClassesViewHolder> {

  public interface OnClassClickListener {

    void onOverFlowClicked(int classID, View view, int adapterPosition, boolean isZeroStudents);

    void onClassClicked(int classID, String className);
  }

  // ref of list of classes data
  private List<ClassEntity> mClassEntities;
  // ref of listener
  private OnClassClickListener mListener;
  // ref of Context
  private Context mContext;


  public ClassesAdapter(List<ClassEntity> data, OnClassClickListener listener, Context context) {
    this.mClassEntities = data;
    this.mListener = listener;
    this.mContext = context;
  }

  @NonNull
  @Override
  public ClassesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutID = R.layout.classes_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View item = inflater.inflate(layoutID, viewGroup, false);
    return new ClassesViewHolder(item);
  }

  private void performClickAction(ClassesViewHolder holder, View view) {
    ClassEntity entity = getClassEntity(holder.getAdapterPosition());
    boolean isEmpty = holder.mStudentsCount.getText()
        .equals(mContext.getString(R.string.zero_students));
    mListener.onOverFlowClicked(entity.getClassID(), view, holder.getAdapterPosition(), isEmpty);
  }


  private void performClickItem(ClassesViewHolder holder) {
    ClassEntity entity = getClassEntity(holder.getAdapterPosition());
    mListener.onClassClicked(entity.getClassID(), entity.getClassName());
  }

  @Override
  public void onBindViewHolder(@NonNull final ClassesViewHolder holder, int i) {

    ClassEntity entity = getClassEntity(i);
    holder.mClassName.setText(entity.getClassName());
    // get Count of Class students
    LocalDataSource.getInstance(mContext).getStudents(entity.getClassID(),
        new LoadStudentsCallBack() {
          @Override
          public void onStudentsLoaded(List<StudentEntity> data) {
            String count = "Students " + "(" + data.size() + ")";
            holder.mStudentsCount.setText(count);
          }

          @Override
          public void onStudentsDataNotAvailable() {
            holder.mStudentsCount.setText(mContext.getString(R.string.zero_students));
          }
        });

  }

  @Override
  public int getItemCount() {
    return mClassEntities == null ? 0 : mClassEntities.size();
  }


  public ClassEntity getClassEntity(int position) {
    return mClassEntities.get(position);
  }

  public void setClassesAdapterData(List<ClassEntity> data) {
    mClassEntities = null;
    mClassEntities = data;
    notifyDataSetChanged();

  }

  class ClassesViewHolder extends ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_class_name)
    TextView mClassName;

    @BindView(R.id.tv_students_count)
    TextView mStudentsCount;

    @BindView(R.id.ib_class_overflow)
    ImageButton mOverflow;

    public ClassesViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mOverflow.setOnClickListener(this);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == mOverflow) {
        performClickAction(this, v);
      } else if (v == itemView) {
        performClickItem(this);
      }
    }
  }

}
