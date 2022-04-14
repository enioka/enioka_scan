package com.enioka.scanner.api;

import android.app.Activity;

import com.enioka.scanner.api.callbacks.ScannerDataCallback;
import com.enioka.scanner.api.callbacks.ScannerInitCallback;
import com.enioka.scanner.api.callbacks.ScannerStatusCallback;
import com.enioka.scanner.api.proxies.ScannerDataCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerInitCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerStatusCallbackProxy;

/**
 * A Scanner which can only work in relation with an Activity.
 */
public interface ScannerForeground extends Scanner {
    /**
     * Called once per application launch.
     *
     * @param activity The activity used by the foreground scanner.
     */
    void initialize(final Activity activity, final ScannerInitCallbackProxy initCallback, final ScannerDataCallbackProxy dataCallback, final ScannerStatusCallbackProxy statusCallback, final Mode mode);

    /**
     * Called once per application launch.
     *
     * @param activity The activity used by the foreground scanner.
     */
    default void initialize(final Activity activity, final ScannerInitCallback initCallback, final ScannerDataCallback dataCallback, final ScannerStatusCallback statusCallback, final Mode mode) {
        initialize(activity,
                new ScannerInitCallbackProxy(initCallback),
                new ScannerDataCallbackProxy(dataCallback),
                new ScannerStatusCallbackProxy(statusCallback),
                mode);
    }
}
