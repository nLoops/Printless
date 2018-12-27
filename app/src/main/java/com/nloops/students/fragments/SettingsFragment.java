package com.nloops.students.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nloops.students.R;

public class SettingsFragment extends Fragment {


  public SettingsFragment() {
    // required by system
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
    //ButterKnife.bind(this, rootView);
    return rootView;
  }
/*
  @OnClick(R.id.tv_settings_logout)
  public void logOut(TextView textView) {
    FirebaseAuth.getInstance().signOut();
    Intent intent = new Intent(getContext(), LaunchActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    Objects.requireNonNull(getActivity()).finish();
  }

  @OnClick(R.id.tv_settings_absentee)
  public void syncData(TextView textView) {
    CloudOperations.getInstance
        (Objects.requireNonNull(getActivity()).getApplicationContext(),
            getActivity().getSupportFragmentManager()).syncData();
  }
*/


}
