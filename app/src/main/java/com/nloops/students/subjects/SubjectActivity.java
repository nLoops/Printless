package com.nloops.students.subjects;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;
import com.nloops.students.fragments.HomeFragmentsAdapter;
import com.nloops.students.fragments.ReportsFragment;
import com.nloops.students.fragments.SettingsFragment;
import com.nloops.students.fragments.SubjectFragment;
import com.nloops.students.views.StudentsViewPager;
import java.util.Objects;


public class SubjectActivity extends AppCompatActivity {

  @BindView(R.id.bottom_navigation)
  BottomNavigationView bottomNavigation;
  @BindView(R.id.viewpager)
  StudentsViewPager viewPager;
  @BindView(R.id.general_toolbar)
  Toolbar mToolBar;
  @BindView(R.id.tv_general_toolbar)
  TextView mToolBarTV;

  private MenuItem prevMenuItem;

  HomeFragmentsAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_home);
    // link views using ButterKnife
    ButterKnife.bind(this);
    // Setup toolbar
    setSupportActionBar(mToolBar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    // onBottomNavigation item selected
    bottomNavigation.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_home:
                viewPager.setCurrentItem(0);
                break;
              case R.id.action_report:
                viewPager.setCurrentItem(1);
                break;
              case R.id.action_settings:
                viewPager.setCurrentItem(2);
                break;
            }
            return false;
          }
        });

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
      }

      @Override
      public void onPageSelected(int position) {
        if (position == 0) {
          mToolBarTV.setText(getString(R.string.toolbar_subjects));
        } else if (position == 1) {
          mToolBarTV.setText(getString(R.string.toolbar_reports));
          adapter.getItem(position).onResume();
        } else if (position == 2) {
          mToolBarTV.setText(getString(R.string.toolbar_settings));
        }

        if (prevMenuItem != null) {
          prevMenuItem.setChecked(false);
        } else {
          bottomNavigation.getMenu().getItem(0).setChecked(false);
        }
        bottomNavigation.getMenu().getItem(position).setChecked(true);
        prevMenuItem = bottomNavigation.getMenu().getItem(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        //
      }
    });

    viewPager.setPagingEnabled(false);

    setupViewPager(viewPager);
  }

  /**
   * Helper method to add Fragments to the adapter and allow viewpager to switch between them.
   *
   * @param viewPager {@link ViewPager}
   */
  private void setupViewPager(ViewPager viewPager) {
    adapter = new HomeFragmentsAdapter(getSupportFragmentManager());
    SubjectFragment subjectFragment = new SubjectFragment();
    ReportsFragment reportsFragment = new ReportsFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    adapter.addFragment(subjectFragment);
    adapter.addFragment(reportsFragment);
    adapter.addFragment(settingsFragment);
    viewPager.setAdapter(adapter);
  }


}
