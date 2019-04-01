package com.nloops.students.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nloops.students.R;
import com.nloops.students.adapters.ScheduleAdapter;
import com.nloops.students.reminder.AlarmScheduler;
import com.nloops.students.utils.ScheduleObject;
import com.nloops.students.utils.SharedPreferenceHelper;
import com.nloops.students.utils.SubjectModel;
import com.nloops.students.utils.UtilsMethods;
import io.paperdb.Paper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class PresetClassDate extends BottomSheetDialogFragment {

  @BindView(R.id.pre_class_view_switcher)
  ViewSwitcher switcher;
  @BindView(R.id.pre_class_action_button)
  Button actionButton;
  @BindView(R.id.time_sche_list_view)
  ListView mListView;
  @BindView(R.id.tv_empty_list_view)
  TextView mEmptyListView;
  @BindView(R.id.date_timer_picker)
  TimePicker mTimerPicker;
  @BindView(R.id.btn_date_back)
  ImageButton mBackButton;


  private ArrayList<ScheduleObject> mScheduleDatesObjects;
  private ScheduleAdapter mAdapter;
  private ArrayList<String> mSelectedDate = new ArrayList<>();
  private static final String SEPARATOR = " , ";
  private static final String ARG_PRODUCT = "arg_product";
  private static final String CLASS_ID = "classID";
  private String dateToDisplayString = "";


  public static PresetClassDate newInstance(SubjectModel model, int classID) {
    final Bundle args = new Bundle();
    args.putSerializable(ARG_PRODUCT, model);
    args.putInt(CLASS_ID, classID);

    final PresetClassDate presetClassDate = new PresetClassDate();
    presetClassDate.setArguments(args);
    return presetClassDate;
  }

  private SubjectModel getSubjectModel() {
    assert getArguments() != null;
    return (SubjectModel) getArguments().getSerializable(ARG_PRODUCT);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.preset_class_schedule, container, false);
    ButterKnife.bind(this, view);
    assert getArguments() != null;
    mScheduleDatesObjects = Paper.book().read(String.valueOf(getArguments().getInt(CLASS_ID)),
        new ArrayList<>());
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAdapter = new ScheduleAdapter(Objects.requireNonNull(getContext()), mScheduleDatesObjects);
    mListView.setAdapter(mAdapter);
    mListView.setEmptyView(mEmptyListView);
    mListView.setOnItemLongClickListener((parent, view1, position, id) -> {
      mAdapter.removeSchedule(position);
      AlarmScheduler.cancelAlarm(getContext(), getSubjectModel());
      Paper.book()
          .write(String.valueOf(getArguments().getInt(CLASS_ID)), mAdapter.getScheduleObjects());
      return true;
    });
  }

  @OnClick(R.id.pre_class_action_button)
  public void actionButtonMethod(Button button) {
    // if we're at page (1) of view switcher let's move to the second one
    if (switcher.getDisplayedChild() == 0) {
      switcher.setDisplayedChild(1);
      // change the action button to tell the user you can save new date schedule
      actionButton.setText(getString(R.string.date_action_btn_save));
      // make button back visible
      mBackButton.setVisibility(View.VISIBLE);
    } else {
      // if we're at page (2) which is means the user maybe scheduled a new date
      // change action button text back
      actionButton.setText(getString(R.string.date_action_btn_schedule));
      // make button back invisible
      mBackButton.setVisibility(View.GONE);
      // create object of date and time to
      ScheduleObject object = new ScheduleObject(dateToDisplayString, getTime());
      // append the adapter list of new data
      mScheduleDatesObjects.add(object);
      // notify the adapter to display it
      mAdapter.notifyDataSetChanged();
      // go back to page (0)
      switcher.setDisplayedChild(0);
      // time to clear and schedule dates
      scheduleDate();
      clearVars();
    }
  }

  @OnClick({R.id.txt_date1, R.id.txt_date2, R.id.txt_date3, R.id.txt_date4,
      R.id.txt_date5, R.id.txt_date6, R.id.txt_date7})
  public void changeState(TextView textView) {
    textView.setSelected(!textView.isSelected());
    if (textView.isSelected()) {
      mSelectedDate.add(textView.getText().toString());
      if (mSelectedDate.size() > 1) {
        dateToDisplayString += SEPARATOR + textView.getText().toString();
      } else {
        dateToDisplayString = textView.getText().toString();
      }
    } else {
      if (mSelectedDate.size() > 1) {
        String tv = textView.getText().toString();
        int i = mSelectedDate.indexOf(tv);
        if (i == 0) {
          String removeAt = tv + SEPARATOR;
          dateToDisplayString = dateToDisplayString.replace(removeAt, "");
        } else {
          String partToRemove = SEPARATOR + tv;
          dateToDisplayString = dateToDisplayString.replace(partToRemove, "");
        }
      } else {
        dateToDisplayString =
            dateToDisplayString.replace(textView.getText().toString(), " ");
      }
      mSelectedDate.remove(textView.getText().toString());
    }
  }

  @OnClick(R.id.btn_date_back)
  public void onButtonBackClicked(ImageButton button) {
    if (switcher.getDisplayedChild() == 1) {
      // make button back invisible
      mBackButton.setVisibility(View.GONE);
      switcher.setDisplayedChild(0);
      clearVars();
    }
  }

  private void scheduleDate() {
    int hour = mTimerPicker.getCurrentHour();
    int minute = mTimerPicker.getCurrentMinute();
    Calendar calendar = Calendar.getInstance();
    ArrayList<Long> savedDates = new ArrayList<>();

    for (String day : mSelectedDate) {
      int dayOfWeek = UtilsMethods.getDayNumber(day);
      AlarmScheduler.scheduleAlarm(dayOfWeek, hour, minute,
          Objects.requireNonNull(getActivity()).getApplicationContext(),
          getSubjectModel());
      // save selected dates to retrieve it if the device restarted
      calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
      calendar.set(Calendar.HOUR_OF_DAY, hour);
      calendar.set(Calendar.MINUTE, minute);
      calendar.set(Calendar.SECOND, 0);
      savedDates.add(calendar.getTimeInMillis());
    }

    // save scheduled dates.
    assert getArguments() != null;
    SharedPreferenceHelper.getInstance(getContext())
        .saveScheduled(savedDates, String.valueOf(getArguments().getInt(CLASS_ID)));

    // save list of objects to display on the list view
    Paper.book().write(String.valueOf(getArguments().getInt(CLASS_ID)), mScheduleDatesObjects);

  }

  private void clearSelected() {
    ConstraintLayout layout =
        Objects.requireNonNull(getView()).findViewById(R.id.pre_class_week_days);
    for (int i = 0; i < layout.getChildCount(); i++) {
      View v = layout.getChildAt(i);
      if (v instanceof TextView) {
        v.setSelected(false);
      }
    }
  }

  private void clearVars() {
    dateToDisplayString = "";
    mSelectedDate.clear();
    clearSelected();
  }

  private String getTime() {
    return mTimerPicker.getCurrentHour() + " : " + mTimerPicker.getCurrentMinute();
  }
}
