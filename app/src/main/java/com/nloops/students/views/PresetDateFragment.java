package com.nloops.students.views;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils.TruncateAt;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nloops.students.R;
import com.nloops.students.databinding.GeneralPresetLayoutBinding;
import com.nloops.students.databinding.PresetDateLayoutBinding;
import com.nloops.students.databinding.PresetTimeLayoutBinding;
import com.nloops.students.reminder.AlarmScheduler;
import com.nloops.students.utils.SharedPreferenceHelper;
import com.nloops.students.utils.SubjectModel;
import com.nloops.students.utils.UtilsMethods;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class PresetDateFragment extends BottomSheetDialogFragment {

  public static final String ID_COLOR_SUFFIX = "img_color";
  public static final String ID_SIZE_SUFFIX = "txt_size";
  public static final String ID_DATE_SUFFIX = "txt_date";
  public static final String ID_TIME_SUFFIX = "txt_time";
  private static final String ARG_PRODUCT = "arg_product";
  private static final String SEPERATOR = " , ";

  private List<View> clonedViews = new ArrayList<>();

  private List<String> selectedDates = new ArrayList();

  private List<String> selectedTime = new ArrayList();

  private OrderSelection orderSelection;

  private Transition selectedViewTransition;

  private GeneralPresetLayoutBinding binding;

  public static class OrderSelection {

    public int size = 0;
    public int color = 0;
    public String date = "";
    public String time = "";
  }

  public static PresetDateFragment newInstance(SubjectModel model) {
    final Bundle args = new Bundle();
    args.putSerializable(ARG_PRODUCT, model);

    final PresetDateFragment presetDateFragment = new PresetDateFragment();
    presetDateFragment.setArguments(args);
    return presetDateFragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    binding = GeneralPresetLayoutBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    orderSelection = new OrderSelection();

    selectedViewTransition = TransitionInflater.from(getContext())
        .inflateTransition(R.transition.transition_selected_view);

    binding.setSubjectmodel(getSubjectModel());

    initOnDateSelected(binding.dateHolder, binding.timeHolder);

    binding.btnConfirm.setOnClickListener(v -> showFinishIV());
  }

  private SubjectModel getSubjectModel() {
    assert getArguments() != null;
    return (SubjectModel) getArguments().getSerializable(ARG_PRODUCT);
  }

  public interface ScheduleDateListener {

    void onDateSelected(View v);
  }

  private void transitionSelectedView(View v) {
    // Create the cloned view from the selected view at the same position
    final View selectionView = createSelectionView(v);

    // Add the cloned view to the parent constraint layout
    binding.mainContainer.addView(selectionView);
    clonedViews.add(selectionView);

    // Perform the transition by changing constraint's layout params
    startCloneAnimation(selectionView, getTargetView(v));
  }

  private void startCloneAnimation(View clonedView, View targetView) {
    clonedView.post(() -> {
      TransitionManager.beginDelayedTransition(
          (ViewGroup) binding.getRoot(), selectedViewTransition);

      // Fires the transition
      clonedView.setLayoutParams(SelectedParamsFactory
          .endParams(clonedView, targetView));
    });
  }

  private View getTargetView(View v) {
    final String resourceName = getResources().getResourceEntryName(v.getId());

    if (resourceName.startsWith(ID_SIZE_SUFFIX) ||
        resourceName.startsWith(ID_DATE_SUFFIX)) {
      v.setId(R.id.first_position);
      return binding.txtDateLabel;

    } else if (resourceName.startsWith(ID_COLOR_SUFFIX) ||
        resourceName.startsWith(ID_TIME_SUFFIX)) {
      v.setId(R.id.second_position);
      return binding.txtTimeLabel;
    }

    throw new IllegalStateException();
  }

  private View createSelectionView(View v) {
    final String resourceName = getResources().getResourceEntryName(v.getId());

    return createSelectedTextView(v);
  }

  private View createSelectedTextView(View v) {
    final TextView fakeSelectedTextView = new TextView(
        getContext(), null, R.attr.selectedTextStyle);

    fakeSelectedTextView.setEllipsize(TruncateAt.END);
    fakeSelectedTextView.setMaxLines(1);
    fakeSelectedTextView.setWidth(400);

    final String resourceName = getResources().getResourceEntryName(v.getId());

    if (resourceName.startsWith(ID_DATE_SUFFIX)) {
      fakeSelectedTextView.setText(orderSelection.date);
    } else if (resourceName.startsWith(ID_TIME_SUFFIX)) {
      fakeSelectedTextView.setText(orderSelection.time);
    }

    fakeSelectedTextView.setLayoutParams(SelectedParamsFactory.startTextParams(v));
    return fakeSelectedTextView;
  }

  private static class SelectedParamsFactory {

    private static ConstraintLayout.LayoutParams startColorParams(View selectedView) {
      final int colorSize = selectedView.getContext().getResources()
          .getDimensionPixelOffset(R.dimen.product_color_size);

      final ConstraintLayout.LayoutParams layoutParams =
          new ConstraintLayout.LayoutParams(colorSize, colorSize);

      setStartState(selectedView, layoutParams);
      return layoutParams;
    }

    private static ConstraintLayout.LayoutParams startTextParams(View selectedView) {
      final ConstraintLayout.LayoutParams layoutParams =
          new ConstraintLayout.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);

      setStartState(selectedView, layoutParams);
      return layoutParams;
    }

    private static void setStartState(View selectedView,
        ConstraintLayout.LayoutParams layoutParams) {
      layoutParams.topToTop = ((ViewGroup) selectedView.getParent().getParent()).getId();
      layoutParams.leftToLeft = ((ViewGroup) selectedView.getParent().getParent()).getId();
      layoutParams.setMargins((int) selectedView.getX(), (int) selectedView.getY(), 0, 0);
    }

    private static ConstraintLayout.LayoutParams endParams(View v, View targetView) {
      final ConstraintLayout.LayoutParams layoutParams =
          (ConstraintLayout.LayoutParams) v.getLayoutParams();

      final int marginLeft = v.getContext().getResources()
          .getDimensionPixelOffset(R.dimen.dimen_eight_dp);

      layoutParams.setMargins(marginLeft, 0, 0, 0);
      layoutParams.topToTop = targetView.getId();
      layoutParams.startToEnd = targetView.getId();
      layoutParams.bottomToBottom = targetView.getId();

      return layoutParams;
    }
  }

  private String getCleanedText(View v) {
    return ((TextView) v).getText().toString().replace("\n", " ");
  }

  private void initOnDateSelected(PresetDateLayoutBinding layoutBinding
      , PresetTimeLayoutBinding presetTimeLayoutBinding) {

    layoutBinding.setListener(v -> {
      if (v.isSelected()) {
        v.setSelected(false);
        selectedDates.remove(getCleanedText(v));
        orderSelection.date.replace(getCleanedText(v), "");
      } else {
        selectedDates.add(getCleanedText(v));
        if (orderSelection.date.isEmpty()) {
          orderSelection.date = getCleanedText(v);
          v.setSelected(true);
          transitionSelectedView(v);
        } else {
          orderSelection.date += SEPERATOR + getCleanedText(v);
          v.setSelected(true);
          transitionSelectedView(v);
        }
      }
    });

    presetTimeLayoutBinding.setListener(v -> {
      if (v.isSelected()) {
        v.setSelected(false);
        selectedTime.remove(getCleanedText(v));
      } else {
        selectedTime.add(getCleanedText(v));
        if (orderSelection.time.isEmpty()) {
          orderSelection.time = getCleanedText(v);
          v.setSelected(true);
          transitionSelectedView(v);
        } else {
          orderSelection.time += SEPERATOR + getCleanedText(v);
          v.setSelected(true);
          transitionSelectedView(v);
        }
      }
    });
  }

  private void showFinishIV() {
    binding.viewSwitcher.setDisplayedChild(1);
    presetSubject();
    Handler handler = new Handler();
    handler.postDelayed(() -> {
      Drawable drawable = binding.ivPresetFinish.getDrawable();
      if (drawable instanceof Animatable) {
        binding.ivPresetFinish.setVisibility(View.VISIBLE);
        ((Animatable) drawable).start();
      }
    }, 300);

    Handler mHandler = new Handler();
    mHandler.postDelayed(() -> getDialog().dismiss(), 900);
  }

  private void presetSubject() {
    ArrayList<Long> arrayList = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();

    for (String string : selectedDates) {
      for (String time : selectedTime) {
        AlarmScheduler
            .scheduleAlarm(UtilsMethods.getDayNumber(string), UtilsMethods.getHourOfDay(time),
                Objects.requireNonNull(getActivity()).getApplicationContext(), getSubjectModel());
        calendar.set(Calendar.DAY_OF_WEEK, UtilsMethods.getDayNumber(string));
        calendar.set(Calendar.HOUR_OF_DAY, UtilsMethods.getHourOfDay(time));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        arrayList.add(calendar.getTimeInMillis());
      }
    }
    // save selected dates to schedule them in case of device restart.
    SharedPreferenceHelper.getInstance(getContext()).saveScheduled(arrayList,
        String.valueOf(getSubjectModel().subjectID));
  }


}
