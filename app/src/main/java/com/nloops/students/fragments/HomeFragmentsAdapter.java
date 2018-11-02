package com.nloops.students.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragmentsAdapter extends FragmentPagerAdapter {

  private List<Fragment> mFragmentsList = new ArrayList<>();

  public HomeFragmentsAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int pos) {
    return mFragmentsList.get(pos);
  }

  @Override
  public int getCount() {
    return mFragmentsList.size();
  }

  public void addFragment(Fragment fragment) {
    mFragmentsList.add(fragment);
  }
}
