package com.nloops.students.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nloops.students.R;
import com.nloops.students.utils.KeyboardUtil;
import java.util.Objects;

public class ChangePasswordDialog extends BottomSheetDialogFragment {

  @BindView(R.id.change_password_ed)
  TextInputEditText mCurrentPassword;
  @BindView(R.id.new_password_ed)
  TextInputEditText mNewPassword;
  @BindView(R.id.change_password_holder)
  TextInputLayout mCurrentHolder;
  @BindView(R.id.new_password_holder)
  TextInputLayout mNewHolder;

  private static ChangePasswordDialog INSTANCE;

  public static ChangePasswordDialog newInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ChangePasswordDialog();
    }
    return INSTANCE;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_change_password_layout, container, false);
    //set to adjust screen height automatically, when soft keyboard appears on screen
    //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    new KeyboardUtil(getActivity(), view);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mCurrentPassword.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mCurrentHolder.isErrorEnabled()) {
          mCurrentHolder.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });

    mNewPassword.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mNewHolder.isErrorEnabled()) {
          mNewHolder.setErrorEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        //
      }
    });
  }

  @OnClick(R.id.confirm_password_bt)
  public void confirmPassword(Button btn) {
    if (isMissingData()) {
      missingDataMsg();
    } else {
      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      String newPassword = mNewPassword.getText().toString();
      assert user != null;
      user.updatePassword(newPassword)
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                  "Password updated successfully", Toast.LENGTH_SHORT).show();
              dismiss();
            } else {
              Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                  "an error occurred, try again later", Toast.LENGTH_SHORT).show();
            }
          });
    }
  }

  private void getKeyboardFocus() {
    mCurrentPassword.requestFocus();
    InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).
        getSystemService(Context.INPUT_METHOD_SERVICE);
    assert imm != null;
    imm.showSoftInput(mCurrentPassword, InputMethodManager.SHOW_IMPLICIT);
  }

  private void missingDataMsg() {
    if (mCurrentPassword.length() <= 0 && mNewPassword.length() > 0) {
      mCurrentHolder.setErrorEnabled(true);
      mCurrentHolder.setError(getText(R.string.missing_data_field_warn));
    } else if (mNewPassword.length() <= 0 && mCurrentPassword.length() > 0) {
      mNewHolder.setErrorEnabled(true);
      mNewHolder.setError(getText(R.string.missing_data_field_warn));
    } else {
      mCurrentHolder.setErrorEnabled(true);
      mNewHolder.setErrorEnabled(true);
      mCurrentHolder.setError(getText(R.string.missing_data_field_warn));
      mNewHolder.setError(getText(R.string.missing_data_field_warn));
    }
  }

  private boolean isMissingData() {
    return mCurrentPassword.length() <= 0 || mNewPassword.length() <= 0;
  }

}
