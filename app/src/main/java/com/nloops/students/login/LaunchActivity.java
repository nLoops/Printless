package com.nloops.students.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nloops.students.R;

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
    // pager adapter
    LaunchFragmentAdapter adapter = new LaunchFragmentAdapter(getSupportFragmentManager());
    mPager.setAdapter(adapter);
    mTablayout.setupWithViewPager(mPager);
  }
}
