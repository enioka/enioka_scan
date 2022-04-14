package com.enioka.scanner.api;

import android.content.Context;

import com.enioka.scanner.api.callbacks.ScannerDataCallback;
import com.enioka.scanner.api.callbacks.ScannerInitCallback;
import com.enioka.scanner.api.callbacks.ScannerStatusCallback;
import com.enioka.scanner.api.proxies.ScannerDataCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerInitCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerStatusCallbackProxy;

import java.util.Map;

/**
 * The interface to implement by a laser scanner provider.
 */
public interface Scanner {
    enum Mode {
        /**
         * The scanner stops after one successful read. It must be rearmed.
         */
        SINGLE_SCAN,
        /**
         * The scanner waits for result post-treatment and auto rearms.
         */
        CONTINUOUS_SCAN,
        /**
         * The scanner is always ready to scan, not waiting for any result analysis. Results may be sent in batch.
         */
        BATCH
    }

    public static final String SCANNER_STATUS_SCANNER_SN = "SCANNER_STATUS_SCANNER_SN";
    public static final String SCANNER_STATUS_SCANNER_MODEL = "SCANNER_STATUS_SCANNER_MODEL";
    public static final String SCANNER_STATUS_BATTERY_SN = "SCANNER_STATUS_BATTERY_SN";
    public static final String SCANNER_STATUS_BATTERY_MODEL = "SCANNER_STATUS_BATTERY_MODEL";
    public static final String SCANNER_STATUS_BATTERY_WEAR = "SCANNER_STATUS_BATTERY_WEAR";
    public static final String SCANNER_STATUS_BATTERY_CHARGE = "SCANNER_STATUS_BATTERY_CHARGE";
    public static final String SCANNER_STATUS_FIRMWARE = "SCANNER_STATUS_FIRMWARE";
    public static final String SCANNER_STATUS_BT_MAC = "SCANNER_STATUS_BT_MAC";


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SOFTWARE TRIGGERS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Simulates a press on a hardware-trigger, firing the beam that will read barcodes.
     */
    default void pressScanTrigger() {
        throw new UnsupportedOperationException();
    }

    /**
     * Ends the effect of {@link #pressScanTrigger()}.
     */
    default void releaseScanTrigger() {
        throw new UnsupportedOperationException();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFE CYCLE
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Called once per application launch.
     */
    void initialize(final Context applicationContext, final ScannerInitCallbackProxy initCallback, final ScannerDataCallbackProxy dataCallback, final ScannerStatusCallbackProxy statusCallback, final Mode mode);

    /**
     * Called once per application launch, implicit wrapping with callback proxies.
     */
    default void initialize(final Context applicationContext, final ScannerInitCallback initCallback, final ScannerDataCallback dataCallback, final ScannerStatusCallback statusCallback, final Mode mode) {
        initialize(applicationContext,
                new ScannerInitCallbackProxy(initCallback),
                new ScannerDataCallbackProxy(dataCallback),
                new ScannerStatusCallbackProxy(statusCallback),
                mode);
    }

    /**
     * Change ScannerDataCallback
     *
     * @param cb a callback to call when data is read.
     */
    void setDataCallBack(ScannerDataCallbackProxy cb);

    /**
     * Disconnect scanner from the App (the app does not need the scanner anymore)
     */
    void disconnect();

    /**
     * The app keeps the scanner for itself but does not need it immediately. It may free whatever resources it has, or ignore this call.
     */
    void pause();

    /**
     * Reverse the effects of {@link #pause()}. The scanner is once again ready to scan after this call. Status callback should be called if needed. Idempotent.
     */
    void resume();


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BEEPS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Short high beep to indicate successful scan
     */
    void beepScanSuccessful();

    /**
     * Long low beep to indicate unsuccessful scan
     */
    void beepScanFailure();

    /**
     * Different beep to indicate a completed barcode pairing
     */
    void beepPairingCompleted();


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ILLUMINATION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * If the device used has a way to illuminate the target, enable it. Idempotent.
     */
    void enableIllumination();

    /**
     * Reverse of {@link #enableIllumination()}
     */
    void disableIllumination();

    /**
     * See {@link #enableIllumination()}
     */
    void toggleIllumination();

    /**
     * True if the illumination method is activated.
     */
    boolean isIlluminationOn();


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LED
    ////////////////////////////////////////////////////////////////////////////////////////////////

    void ledColorOn(Color color);

    void ledColorOff(Color color);


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // FUNCTION SUPPORT
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * True if at least one method of illumination (torch, laser...) is supported).
     */
    boolean supportsIllumination();

    /**
     * For logging and sorting purpose, this is the key of the SDK behing this scanner (same as {@link ScannerProvider#getKey()}.
     */
    String getProviderKey();


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INVENTORY
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get an inventory/status value. For example battery serial number, device MAC, etc. Keys are usually constants exported by drivers. A list can be obtained with {@link #getStatus()}. Data returned may come from a local cache.
     *
     * @param key requested key
     * @return corresponding value or null if key is not supported by this scanner.
     */
    String getStatus(String key);

    /**
     * Get an inventory/status value. For example battery serial number, device MAC, etc. Keys are usually constants exported by drivers. A list can be obtained with {@link #getStatus()}.
     *
     * @param key        requested key
     * @param allowCache if false the driver is not allowed to use a cache and MUST fetch fresh data from the device.
     * @return corresponding value or null if key is not supported by this scanner.
     */
    String getStatus(String key, boolean allowCache);

    /**
     * @return all inventory/status data known by the scanner. May be empty but not null.
     */
    Map<String, String> getStatus();
}
