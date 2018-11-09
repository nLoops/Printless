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
import com.nloops.students.adapters.ClassReportAdapter.ClassReportVH;
import com.nloops.students.data.mvp.StructureDataSource.LoadAbsenteeCallBack;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.data.tables.AbsenteeEntity;
import com.nloops.students.data.tables.ClassEntity;
import java.util.List;

public class ClassReportAdapter extends Adapter<ClassReportVH> {

  public interface OnClassReportClick {

    void onClick(int classID, String className);
  }

  // ref of list of data
  private List<ClassEntity> mClassesData;
  // ref of context
  private Context mContext;
  // ref of listener
  private OnClassReportClick mListener;

  public ClassReportAdapter(List<ClassEntity> data, Context context, OnClassReportClick listener) {
    this.mClassesData = data;
    this.mContext = context;
    this.mListener = listener;
  }

  @NonNull
  @Override
  public ClassReportVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutID = R.layout.class_report_list_item;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View rootView = inflater.inflate(layoutID, viewGroup, false);
    return new ClassReportVH(rootView);
  }

  @Override
  public void onBindViewHolder(@NonNull final ClassReportVH holder, int position) {
    ClassEntity entity = getClass(position);
    holder.mClassName.setText(entity.getClassName());
    LocalDataSource.getInstance(mContext).getAllAbsenteeByClass(entity.getClassID(),
        new LoadAbsenteeCallBack() {
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
    return mClassesData == null ? 0 : mClassesData.size();
  }

  /**
   * Helper method to return Class item in specific position
   *
   * @param position the class pos in the array-list
   * @return {@link ClassEntity}
   */
  public ClassEntity getClass(int position) {
    return mClassesData.get(position);
  }

  /**
   * This void will help to replace data-set from outside adapter class
   *
   * @param data {@link List<ClassEntity>}
   */
  public void setClassDataList(List<ClassEntity> data) {
    mClassesData = null;
    mClassesData = data;
    notifyDataSetChanged();

  }

  private void performOnClick(ClassReportVH holder) {
    ClassEntity entity = getClass(holder.getAdapterPosition());
    mListener.onClick(entity.getClassID(), entity.getClassName());
  }

  class ClassReportVH extends ViewHolder implements OnClickListener {

    @BindView(R.id.report_list_class_name)
    TextView mClassName;
    @BindView(R.id.report_list_lectures_count)
    TextView mLecturesCount;
    @BindView(R.id.report_list_class_absentee_count)
    TextView mAbsenteeCount;

    public ClassReportVH(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == itemView) {
        performOnClick(this);
      }
    }
  }

}
