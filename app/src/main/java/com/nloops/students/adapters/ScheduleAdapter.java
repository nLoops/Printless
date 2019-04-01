package com.nloops.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.nloops.students.R;
import com.nloops.students.utils.ScheduleObject;
import java.util.ArrayList;

public class ScheduleAdapter extends ArrayAdapter<ScheduleObject> {

  private ArrayList<ScheduleObject> scheduleObjects;

  public ScheduleAdapter(
      @NonNull Context context, @NonNull ArrayList<ScheduleObject> scheduleObjects) {
    super(context, 0, scheduleObjects);
    this.scheduleObjects = scheduleObjects;
  }


  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(
          R.layout.list_timer_picker_item, parent, false);
    }

    ScheduleObject scheduleObject = getItem(position);

    TextView dateTV = convertView.findViewById(R.id.tv_sche_days);
    TextView timeTV = convertView.findViewById(R.id.tv_sche_time);

    assert scheduleObject != null;
    dateTV.setText(scheduleObject.getmDateString());
    timeTV.setText(scheduleObject.getmTimeString());
    return convertView;
  }

  public void removeSchedule(int position) {
    scheduleObjects.remove(position);
    notifyDataSetChanged();
  }

  public ArrayList<ScheduleObject> getScheduleObjects() {
    return scheduleObjects;
  }
}
