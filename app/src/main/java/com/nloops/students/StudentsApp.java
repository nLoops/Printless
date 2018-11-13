package com.nloops.students;

import android.app.Application;
import android.os.StrictMode;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class StudentsApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    }
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());

  }
}
