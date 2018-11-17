package com.nloops.students.login;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LaunchFragmentAdapter extends FragmentStatePagerAdapter {

  public LaunchFragmentAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    switch (i) {
      case 0:
        return new RegisterActivity();
      case 1:
        return new LoginFragment();
    }
    return null;
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "NEW";
      case 1:
        return "LOG IN";
      default:
        return null;
    }
  }
}
