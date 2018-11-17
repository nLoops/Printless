package com.nloops.students.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nloops.students.R;
import com.nloops.students.subjects.SubjectActivity;
import com.nloops.students.utils.LocalUser;
import com.nloops.students.utils.UtilsConstants;
import com.nloops.students.views.LoadingDialog;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends Fragment {

  // first bind views
  @BindView(R.id.reg_edit_holder)
  TextInputLayout mEmailHolder;
  @BindView(R.id.reg_password_holder)
  TextInputLayout mPasswordHolder;
  @BindView(R.id.reg_retype_password_holder)
  TextInputLayout mRePasswordHolder;
  @BindView(R.id.reg_email_ed)
  TextInputEditText mEmailED;
  @BindView(R.id.reg_password_ed)
  TextInputEditText mPasswordED;
  @BindView(R.id.reg_repassword_ed)
  TextInputEditText mRePasswordED;


  // this pattern will check the user input.
  private final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  LoadingDialog loadingDialog;
  // ref of Firebase Objects to Auth User.
  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;
  private FirebaseUser user;


  public RegisterActivity() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initFirebase();
  }

  @Override
  public void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_register, container, false);
    // bind views to class
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

    mRePasswordED.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mRePasswordHolder.isErrorEnabled()) {
          mRePasswordHolder.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });
  }

  @OnClick(R.id.reg_btn_action)
  public void confirmRegister(Button button) {
    registerUser();
  }

  private void registerUser() {
    if (isMissingData()) {
      checkErrorMessage();
    } else {
      String userEmail = Objects.requireNonNull(mEmailED.getText()).toString();
      String userPassword = Objects.requireNonNull(mPasswordED.getText()).toString();
      String userRePassword = Objects.requireNonNull(mRePasswordED.getText()).toString();

      if (validate(userEmail, userPassword, userRePassword)) {
        regUserInServer(userEmail, userPassword);
      } else {
        Snackbar.make(Objects.requireNonNull(getView()),
            getString(R.string.str_msg_email_exist), Snackbar.LENGTH_LONG).show();
      }
    }
  }

  /**
   * Validate email, pass == re_pass
   *
   * @param emailStr passed Email.
   * @param password passed Password.
   * @return boolean if the input Data Validated or not.
   */
  private boolean validate(String emailStr, String password, String repeatPassword) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
    return password.length() > 0 && repeatPassword.equals(password) && matcher.find();
  }

  private void checkErrorMessage() {
    if (mEmailED.length() <= 0 && mPasswordED.length() > 0 && mRePasswordED.length() > 0) {
      mEmailHolder.setErrorEnabled(true);
      mEmailHolder.setError(getText(R.string.missing_data_field_warn));
    } else if (mPasswordED.length() <= 0 && mEmailED.length() > 0 && mRePasswordED.length() > 0) {
      mPasswordHolder.setErrorEnabled(true);
      mPasswordHolder.setError(getText(R.string.missing_data_field_warn));
    } else if (mRePasswordED.length() <= 0 && mEmailED.length() > 0 && mPasswordED.length() > 0) {
      mRePasswordHolder.setErrorEnabled(true);
      mRePasswordHolder.setError(getText(R.string.missing_data_field_warn));
    } else {
      mEmailHolder.setErrorEnabled(true);
      mEmailHolder.setError(getText(R.string.missing_data_field_warn));
      mPasswordHolder.setErrorEnabled(true);
      mPasswordHolder.setError(getText(R.string.missing_data_field_warn));
      mRePasswordHolder.setErrorEnabled(true);
      mRePasswordHolder.setError(getText(R.string.missing_data_field_warn));
    }
  }

  private boolean isMissingData() {
    return mEmailED.length() <= 0 || mPasswordED.length() <= 0 || mRePasswordED.length() <= 0;
  }

  void regUserInServer(String email, String password) {
    loadingDialog.showNow(Objects.requireNonNull(getActivity())
        .getSupportFragmentManager(), "reg_loading");

    mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(Objects.requireNonNull(getActivity()),
            new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                loadingDialog.dismiss();
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                  Snackbar.make(Objects.requireNonNull(getView()),
                      getString(R.string.login_failed_msg), Snackbar.LENGTH_LONG).show();
                } else {
                  // if true we upload user data to server.
                  initNewUserInfo();
                  Snackbar.make(Objects.requireNonNull(getView()),
                      getString(R.string.str_login_sucess), Snackbar.LENGTH_LONG).show();
                  startActivity(new Intent(getContext(), SubjectActivity.class));
                  getActivity().finish();
                }
              }
            })
        .addOnFailureListener(e -> loadingDialog.dismiss());

  }

  /**
   * Push new User Data to {@link FirebaseDatabase} under node /users/user-uid
   */
  void initNewUserInfo() {
    String separator = "@";
    LocalUser newLocalUser = new LocalUser();
    newLocalUser.email = user.getEmail();
    newLocalUser.name = Objects.requireNonNull(user.getEmail())
        .substring(0, user.getEmail().indexOf(separator));
    FirebaseDatabase.getInstance().getReference().child(UtilsConstants.USERS_DATABASE_REFERENCE)
        .child(user.getUid())
        .setValue(newLocalUser);
  }

  /**
   * Helper Method to init {@link #mAuth} to operate the User functions with the backend, using the
   * {@link #mAuthListener} to detect when and what action to operate.
   */
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
        startActivity(new Intent(getContext(), SubjectActivity.class));
        Objects.requireNonNull(getActivity()).finish();
      }
    };

    loadingDialog = new LoadingDialog();
  }

}
