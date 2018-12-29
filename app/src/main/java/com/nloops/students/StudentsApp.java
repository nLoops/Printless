package com.nloops.students;

import android.app.Application;
import android.os.StrictMode;
import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class StudentsApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code...

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    }
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());

    // init Paper DB
    Paper.init(this);

  }
}
