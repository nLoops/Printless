package com.nloops.students.utils;

import android.content.Context;
import android.graphics.Color;
import com.nloops.students.R;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import java.util.List;

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

}
