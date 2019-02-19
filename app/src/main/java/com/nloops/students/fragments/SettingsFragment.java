package com.nloops.students.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nloops.students.R;
import com.nloops.students.SettingsActivity;
import com.nloops.students.cloud.CloudOperations;
import com.nloops.students.login.LaunchActivity;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.views.ChangePasswordDialog;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class SettingsFragment extends Fragment {

  @BindView(R.id.tv_sett_name)
  TextView mNameTV;

  public SettingsFragment() {
    // required by system
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
    ButterKnife.bind(this, rootView);
    mNameTV.setText(getUsername());
    return rootView;
  }

  private String getUsername() {
    String separator = "@";
    String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
        .getEmail();
    assert userEmail != null;
    return userEmail.substring(0, userEmail.indexOf(separator));
  }

  @OnClick(R.id.tv_sett_activity)
  public void launchSettings(TextView tv) {
    assert getActivity() != null;
    startActivity(new Intent(getContext(), SettingsActivity.class));
    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
  }

  @OnClick(R.id.tv_sett_signout)
  public void signOutEmail(TextView tv) {
    assert getActivity() != null;
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      try {
        FirebaseDatabase.getInstance().getReference()
            .child(UtilsConstants.ATTENDANCE_DATABASE_REFERENCE)
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .setValue(CloudOperations.loadModelsData())
            .addOnSuccessListener(aVoid -> {
              FirebaseAuth.getInstance().signOut();
              startActivity(new Intent(getContext(), LaunchActivity.class));
              getActivity().finish();
            })
            .addOnFailureListener(e -> Log.d("tesa", "onFailure: "));
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  @OnClick(R.id.tv_sett_password)
  public void changePassword(TextView tv) {
    assert getActivity() != null;
    ChangePasswordDialog.newInstance()
        .show(getActivity().getSupportFragmentManager(), "change_password");
  }

  @OnClick(R.id.profile_iv)
  public void onImageClicked(ImageView iv) {
    assert getActivity() != null;
    Toast.makeText(getActivity().getApplicationContext(),
        "Adding profile upcoming for the next update", Toast.LENGTH_SHORT).show();
  }

}
