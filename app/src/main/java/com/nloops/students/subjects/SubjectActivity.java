package com.nloops.students.subjects;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nloops.students.R;
import com.nloops.students.cloud.CloudModel;
import com.nloops.students.cloud.CloudOperations;
import com.nloops.students.data.mvp.local.LocalDataSource;
import com.nloops.students.fragments.HomeFragmentsAdapter;
import com.nloops.students.fragments.ReportsFragment;
import com.nloops.students.fragments.SettingsFragment;
import com.nloops.students.fragments.SubjectFragment;
import com.nloops.students.utils.SharedPreferenceHelper;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.views.StudentsViewPager;
import io.paperdb.Paper;
import java.util.List;
import java.util.Objects;
import pub.devrel.easypermissions.EasyPermissions;


public class SubjectActivity extends AppCompatActivity implements
    EasyPermissions.PermissionCallbacks {

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

  private static final int PERMISSION_REQ_CODE = 101;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_home);
    // link views using ButterKnife
    ButterKnife.bind(this);
    // Setup toolbar
    setSupportActionBar(mToolBar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    getPermissions();
    // onBottomNavigation item selected
    bottomNavigation.setOnNavigationItemSelectedListener(
        item -> {
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

  /**
   * This Method will check if we have the required permissions to RECORD and SAVE files, if not we
   * will alert USER to get the permissions.
   */
  @TargetApi(23)
  private void getPermissions() {
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    if (!EasyPermissions.hasPermissions(SubjectActivity.this, permissions)) {
      getPermissionsMsg(permissions);
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
    SharedPreferenceHelper.getInstance(SubjectActivity.this).setPermissionsState(true);
    CloudOperations.getInstance(getApplicationContext()).initialize();
  }

  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    Toast.makeText(SubjectActivity.this.getApplicationContext(),
        getString(R.string.dialog_deny_msg), Toast.LENGTH_LONG).show();
    finish();
  }

  private void getPermissionsMsg(String[] permissions) {
    AlertDialog builder = new Builder(this)
        .setMessage(getString(R.string.dialog_permission_msg))
        .setNegativeButton(getString(R.string.dialog_per_cancel), (dialog, which) -> {
          Toast.makeText(SubjectActivity.this.getApplicationContext(),
              getString(R.string.dialog_deny_msg), Toast.LENGTH_LONG).show();
          if (dialog != null) {
            dialog.dismiss();
          }
          finish();
        })
        .setPositiveButton(getString(R.string.dialog_per_ok), (dialog, which) -> {
          if (dialog != null) {
            dialog.dismiss();
          }
          EasyPermissions.requestPermissions(SubjectActivity.this,
              getString(R.string.permissions_required),
              PERMISSION_REQ_CODE, permissions);
        })
        .show();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      syncDataWithDatabase();
      boolean isScheduled = Paper.book().read(UtilsConstants.CLOUD_SCHEDULED, false);
      Log.d("TESA", "onResume1: ");
      if (!isScheduled) {
        CloudOperations.getInstance(this).initialize();
        Paper.book().write(UtilsConstants.CLOUD_SCHEDULED, true);
        Log.d("TESA", "onResume2: ");
      }
    }
  }

  private void syncDataWithDatabase() {
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      // get DataSource Instance
      LocalDataSource dataSource = LocalDataSource.getInstance(this);
      // get CloudPath in database and query data.
      FirebaseDatabase.getInstance().getReference()
          .child(UtilsConstants.ATTENDANCE_DATABASE_REFERENCE)
          .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
          .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {
                CloudModel model = dataSnapshot.getValue(CloudModel.class);
                dataSource.syncDataWithRoom(model);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
              Log.d("tesa", "onCancelled: ");
            }
          });
    }
  }

}
