package com.nloops.students.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.nloops.students.R;
import java.util.Objects;

public class LoadingDialog extends DialogFragment {


  public LoadingDialog() {
    // required by system.
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.loding_dialog_layout, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    SpinKitView mSpinKitView = Objects.requireNonNull(getView())
        .findViewById(R.id.loading_spin_kit);
    CubeGrid cubeGrid = new CubeGrid();
    mSpinKitView.setIndeterminateDrawable(cubeGrid);
  }
}
