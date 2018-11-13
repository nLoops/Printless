package com.nloops.students.subjects;

import android.Manifest;
import android.annotation.TargetApi;
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
import java.util.List;
import java.util.Objects;
import pub.devrel.easypermissions.EasyPermissions;


public class SubjectActivity extends AppCompatActivity implements
    EasyPermissions.PermissionCallbacks {

  @BindView(R.id.bottom_navigation)
  BottomNavigationView bottomNavigation;
  @BindView(R.id.viewpager)
  ViewPager viewPager;
  @BindView(R.id.home_toolbar)
  Toolbar mToolBar;
  @BindView(R.id.tv_home_toolbar)
  TextView mToolBarTV;

  private static final int PERMISSION_REQ_CODE = 225;

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
    // get permissions
    getPermissions();
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

    setupViewPager(viewPager);
  }

  /**
   * Helper method to add Fragments to the adapter and allow viewpager to switch between
   * them.
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

  /**
   * This Method will check if we have the required permissions to RECORD and SAVE files, if not we
   * will alert USER to get the permissions.
   */
  @TargetApi(23)
  private void getPermissions() {
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    if (!EasyPermissions.hasPermissions(SubjectActivity.this, permissions)) {
      EasyPermissions.requestPermissions(this,
          getString(R.string.permissions_required),
          PERMISSION_REQ_CODE, permissions);
    }
  }

  @TargetApi(23)
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @Override
  public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    // will implemented soon
  }

  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    // will implemented soon
  }

}
