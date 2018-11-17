package com.nloops.students.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.nloops.students.R;
import com.nloops.students.subjects.SubjectActivity;
import com.nloops.students.views.LoadingDialog;
import java.util.Objects;


public class LoginFragment extends Fragment {


  // first bind views
  @BindView(R.id.log_edit_holder)
  TextInputLayout mEmailHolder;
  @BindView(R.id.log_password_holder)
  TextInputLayout mPasswordHolder;
  @BindView(R.id.log_email_ed)
  TextInputEditText mEmailED;
  @BindView(R.id.log_password_ed)
  TextInputEditText mPasswordED;
  @BindView(R.id.log_btn_action)
  Button mLoginButton;

  private FirebaseAuth mAuth;

  LoadingDialog loadingDialog;

  private boolean isReset = false;

  public LoginFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_login, container, false);
    // bind views to class
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mEmailED.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mEmailHolder.isErrorEnabled()) {
          mEmailHolder.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });

    mPasswordED.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mPasswordHolder.isErrorEnabled()) {
          mPasswordHolder.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Define Firebase Auth.
    mAuth = FirebaseAuth.getInstance();
    loadingDialog = new LoadingDialog();
  }

  @OnClick(R.id.log_btn_action)
  public void login(Button button) {
    if (isReset) {
      resetPassword(Objects.requireNonNull(mEmailED.getText()).toString());
    } else {
      if (isMissingData()) {
        checkErrorMessage();
      } else {
        String email = Objects.requireNonNull(mEmailED.getText()).toString();
        String password = Objects.requireNonNull(mPasswordED.getText()).toString();
        signIn(email, password);
      }
    }
  }

  @OnClick(R.id.tv_login_forgot)
  public void resetPassword(TextView textView) {
    // first hide password edit text with beautiful animation
    mPasswordHolder.animate()
        .translationX(mPasswordHolder.getWidth())
        .alpha(0.0f)
        .setDuration(300)
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mPasswordHolder.setVisibility(View.GONE);
          }
        });

    // change button text
    mLoginButton.setText(getString(R.string.str_login_reset));

    isReset = true;

  }

  /**
   * Action Login
   */
  void signIn(String email, String password) {
    FragmentManager fm =
        Objects.requireNonNull(getActivity()).getSupportFragmentManager();
    loadingDialog.showNow(fm, "loading");
    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(Objects.requireNonNull(getActivity()),
            task -> {
              // If sign in fails, display a message to the user. If sign in succeeds
              // the auth state listener will be notified and logic to handle the
              // signed in user can be handled in the listener.
              loadingDialog.dismiss();
              if (!task.isSuccessful()) {
                Snackbar.make(Objects.requireNonNull(getView()),
                    getString(R.string.login_failed_msg), Snackbar.LENGTH_LONG).show();
              } else {
                Snackbar.make(Objects.requireNonNull(getView()),
                    getString(R.string.login_success_msg), Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), SubjectActivity.class);
                startActivity(intent);
                getActivity().finish();
              }
            }).addOnFailureListener(e -> {
      loadingDialog.dismiss();
      Snackbar.make(Objects.requireNonNull(getView()),
          getString(R.string.login_failed_operation_msg), Snackbar.LENGTH_LONG).show();
    })
    ;
  }

  private void checkErrorMessage() {
    if (mEmailED.length() <= 0 && mPasswordED.length() > 0) {
      mEmailHolder.setErrorEnabled(true);
      mEmailHolder.setError(getText(R.string.missing_data_field_warn));
    } else if (mPasswordED.length() <= 0 && mEmailED.length() > 0) {
      mPasswordHolder.setErrorEnabled(true);
      mPasswordHolder.setError(getText(R.string.missing_data_field_warn));
    } else {
      mEmailHolder.setErrorEnabled(true);
      mEmailHolder.setError(getText(R.string.missing_data_field_warn));
      mPasswordHolder.setErrorEnabled(true);
      mPasswordHolder.setError(getText(R.string.missing_data_field_warn));
    }
  }

  private boolean isMissingData() {
    return mEmailED.length() <= 0 || mPasswordED.length() <= 0;
  }

  /**
   * Action reset password
   */
  void resetPassword(final String email) {
    loadingDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager()
        , "loading");
    mAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener(task -> {
          loadingDialog.dismiss();
          isReset = false;
          Snackbar.make(Objects.requireNonNull(getView()),
              getString(R.string.login_reset_success_msg), Snackbar.LENGTH_LONG).show();
          mPasswordHolder.setVisibility(View.VISIBLE);
          mPasswordHolder.setAlpha(0.0f);

          mPasswordHolder.animate()
              .translationX(0)
              .alpha(1.0f)
              .setListener(null);

          // change button text
          mLoginButton.setText(getString(R.string.log_button_acion));
        })
        .addOnFailureListener(e -> {
          loadingDialog.dismiss();
          Snackbar.make(Objects.requireNonNull(getView()),
              getString(R.string.login_reset_failed_msg), Snackbar.LENGTH_LONG).show();
        });

  }

}
