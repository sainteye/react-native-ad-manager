package com.matejdr.admanager;
import android.util.Log;

import com.google.android.gms.ads.nativead.NativeAdView;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class AdViewManager extends ViewGroupManager<NativeAdView> {
  private static final String REACT_CLASS = "AdView";

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected NativeAdView createViewInstance(ThemedReactContext reactContext) {
    Log.w("ifoodie", "init!! AdView");
    return new NativeAdView(reactContext);
  }
}
