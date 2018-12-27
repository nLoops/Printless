package com.nloops.students.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.nloops.students.R;
import com.nloops.students.subjects.SubjectActivity;

public class LaunchActivity extends AppCompatActivity {

  @BindView(R.id.pager)
  ViewPager mPager;
  @BindView(R.id.tablayout)
  TabLayout mTablayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launch);
    // bind views
    ButterKnife.bind(this);
    checkUserState();

  }

  private void checkUserState() {
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      // User is signed in
      // start subject home activity if we have registered account singed
      startActivity(new Intent(this, SubjectActivity.class));
      finish();
    } else {
      LaunchFragmentAdapter adapter = new LaunchFragmentAdapter(getSupportFragmentManager());
      mPager.setAdapter(adapter);
      mTablayout.setupWithViewPager(mPager);
    }
  }
}
