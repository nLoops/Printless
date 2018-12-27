package com.nloops.students.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nloops.students.R;
import com.nloops.students.subjects.SubjectActivity;

public class LaunchActivity extends AppCompatActivity {

  @BindView(R.id.pager)
  ViewPager mPager;
  @BindView(R.id.tablayout)
  TabLayout mTablayout;

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;
  private FirebaseUser user;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launch);
    // bind views
    ButterKnife.bind(this);
    initFirebase();

  }


  private void initFirebase() {
    //Define Firebase Auth.
    mAuth = FirebaseAuth.getInstance();
    // set the listener to Auth User.
    mAuthListener = firebaseAuth -> {
      user = firebaseAuth.getCurrentUser();
      // if current user != null means (exist or signing in)
      if (user != null) {
        // User is signed in
        // start subject home activity if we have registered account singed
        startActivity(new Intent(this, SubjectActivity.class));
        finish();
      } // pager adapter
      LaunchFragmentAdapter adapter = new LaunchFragmentAdapter(getSupportFragmentManager());
      mPager.setAdapter(adapter);
      mTablayout.setupWithViewPager(mPager);
    };

  }

  @Override
  public void onResume() {
    super.onResume();
    mAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  public void onPause() {
    super.onPause();
    mAuth.removeAuthStateListener(mAuthListener);
  }
}
