package com.nloops.students.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.nloops.students.R;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import io.paperdb.Paper;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class UtilsMethods {

  // private constructor prevent instantiations
  private UtilsMethods() {
  }


  /**
   * This method will returns menu to activity
   *
   * @param items {@link PowerMenuItem}
   * @param listener {@link OnMenuItemClickListener}
   * @param context {@link Context}
   * @return {@link PowerMenu}
   */
  public static PowerMenu getPowerMenu(List<PowerMenuItem> items,
      OnMenuItemClickListener<PowerMenuItem> listener,
      Context context) {
    return new PowerMenu.Builder(context)
        .addItemList(items)
        .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | RIGHT)
        .setMenuRadius(10f)
        .setMenuShadow(10f)
        .setTextColor(context.getResources().getColor(R.color.colorTextView))
        .setSelectedTextColor(Color.WHITE)
        .setMenuColor(Color.WHITE)
        .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
        .setOnMenuItemClickListener(listener)
        .build();
  }

  /**
   * This Helper method helps to force keyboard to appears
   *
   * @param activity {@link Activity}
   */
  public static void showKeyboard(Activity activity) {
    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  /**
   * Helper Method to add a beautiful {@link Animation} to View
   *
   * @param view passed {@link View}
   * @param context passed {@link Context}
   */
  public static void slideInFromTop(View view, Context context) {
    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top);
    view.setAnimation(animation);
    view.animate();
    animation.start();
  }

  /**
   * Helper Method to pass DayName and Return it's number from Calendar
   *
   * @param dayName Name Of Week Day
   * @return Calendar.WeekDay number.
   */
  public static int getDayNumber(String dayName) {
    switch (dayName) {
      case UtilsConstants.DAY_SUNDAY:
        return Calendar.SUNDAY;
      case UtilsConstants.DAY_MONDAY:
        return Calendar.MONDAY;
      case UtilsConstants.DAY_TUESDAY:
        return Calendar.TUESDAY;
      case UtilsConstants.DAY_WEDNESDAY:
        return Calendar.WEDNESDAY;
      case UtilsConstants.DAY_THURDAY:
        return Calendar.THURSDAY;
      case UtilsConstants.DAY_FRIDAY:
        return Calendar.FRIDAY;
      case UtilsConstants.DAY_SATURDAY:
        return Calendar.SATURDAY;
      default:
        return -1;
    }
  }

  /**
   * Return selected time by passing selected string.
   *
   * @param hourDay {@link String}
   * @return {@link Integer} to schedule subject attendance.
   */
  public static int getHourOfDay(String hourDay) {
    switch (hourDay) {
      case UtilsConstants.HOUR_ONE:
        return 13;
      case UtilsConstants.HOUR_THREE:
        return 15;
      case UtilsConstants.HOUR_SEVEN:
        return 19;
      case UtilsConstants.HOUR_TEN:
        return 10;
      default:
        return 9;
    }
  }

  public static void setCursorToEnd(TextInputEditText editText) {
    editText.setSelection
        (Objects.requireNonNull(editText.getText()).length());
  }

  public static String getUserUID() {
    String uid = "";
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    } else {
      Paper.book().read(UtilsConstants.LAST_FIREBASE_UID, "");
    }
    return uid;
  }

}
