package com.matejdr.admanager;

import android.util.Log;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;
import com.google.android.gms.ads.nativead.MediaView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ReactModule(name = "CTKAdManageNativeManager")
public class RNAdManageNativeManager extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "CTKAdManageNativeManager";
    /**
     * @{Map} with all registered managers
     **/
    private Map<String, AdsManagerProperties> propertiesMap = new HashMap<>();

    public static class AdsManagerProperties {
        String[] testDevices;
        String adUnitID;

        public String[] getTestDevices() {
            return testDevices;
        }

        public String getAdUnitID() {
            return adUnitID;
        }

        public void setTestDevices(String[] testDevices) {
            this.testDevices = testDevices;
        }

        public void setAdUnitID(String adUnitID) {
            this.adUnitID = adUnitID;
        }
    }

    public RNAdManageNativeManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /**
     * Initialises native ad manager for a given placement id and ads to request.
     * This method is run on the UI thread
     *
     * @param adUnitID
     */
    @ReactMethod
    public void init(final String adUnitID, ReadableArray testDevices) {
        final AdsManagerProperties adsManagerProperties = new AdsManagerProperties();
        adsManagerProperties.setAdUnitID(adUnitID);

        ReadableNativeArray nativeArray = (ReadableNativeArray) testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        adsManagerProperties.setTestDevices(list.toArray(new String[list.size()]));

        propertiesMap.put(adUnitID, adsManagerProperties);
    }

    /**
     * Returns AdsManagerProperties for a given ad unit id
     *
     * @param adUnitID
     * @return
     */
    public AdsManagerProperties getAdsManagerProperties(String adUnitID) {
        return propertiesMap.get(adUnitID);
    }

    @ReactMethod
    public void registerViewsForInteraction(final int adTag,
                                            final int adViewTag,
                                            final int mediaViewTag,
                                            final ReadableArray clickableViewsTags,
                                            final Promise promise) {
        getReactApplicationContext().getNativeModule(UIManagerModule.class).addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                try {
                    NativeAdViewContainer nativeAdViewContainer = null;

                    if (adTag != -1) {
                        nativeAdViewContainer = (NativeAdViewContainer) nativeViewHierarchyManager.resolveView(adTag);
                    }

                    MediaView mediaView = null;
                    if (mediaViewTag != -1) {
                        mediaView = (MediaView) nativeViewHierarchyManager.resolveView(mediaViewTag);
                    }

                    NativeAdView adView = null;
                    if (adViewTag != -1) {
                        adView = (NativeAdView) nativeViewHierarchyManager.resolveView(adViewTag);
                    }

                    List<View> clickableViews = new ArrayList<>();

                    for (int i = 0; i < clickableViewsTags.size(); ++i) {
                        View view = nativeViewHierarchyManager.resolveView(clickableViewsTags.getInt(i));
                        clickableViews.add(view);
                    }

                    nativeAdViewContainer.registerViewsForInteraction(clickableViews, adView, mediaView);
                    Log.w("ifoodie", "regist!!" + mediaView.getClass().getSimpleName());
                    promise.resolve(null);

                } catch (ClassCastException e) {
                    promise.reject("E_CANNOT_CAST", e);
                } catch (IllegalViewOperationException e) {
                    promise.reject("E_INVALID_TAG_ERROR", e);
                } catch (NullPointerException e) {
                    promise.reject("E_NO_NATIVE_AD_VIEW", e);
                } catch (Exception e) {
                    promise.reject("E_AD_REGISTER_ERROR", e);
                }
            }
        });
    }
}
