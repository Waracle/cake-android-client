package com.waracle.androidtest.presentation.fragment;

import com.waracle.androidtest.core.GetCakes;
import com.waracle.androidtest.data.remote.RemoteCakeSource;

final class CakesFragmentComponent {

  private CakesFragmentComponent() {
    throw new AssertionError("No instances.");
  }

  static GetCakes inject() {
    RemoteCakeSource remoteCakeSource = new RemoteCakeSource();
    return new GetCakes(remoteCakeSource);
  }
}
