package com.waracle.androidtest.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.waracle.androidtest.PlaceholderFragment;
import com.waracle.androidtest.R;

public final class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment())
          .commit();
    }
  }
}