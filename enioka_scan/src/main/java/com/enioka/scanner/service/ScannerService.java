package com.enioka.scanner.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.enioka.scanner.LaserScanner;
import com.enioka.scanner.api.Color;
import com.enioka.scanner.api.Scanner;
import com.enioka.scanner.api.ScannerBackground;
import com.enioka.scanner.api.callbacks.ScannerConnectionHandler;
import com.enioka.scanner.api.ScannerForeground;
import com.enioka.scanner.api.ScannerSearchOptions;
import com.enioka.scanner.api.callbacks.ScannerDataCallback;
import com.enioka.scanner.api.callbacks.ScannerInitCallback;
import com.enioka.scanner.api.callbacks.ScannerStatusCallback;
import com.enioka.scanner.api.proxies.ScannerConnectionHandlerProxy;
import com.enioka.scanner.api.proxies.ScannerDataCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerInitCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerStatusCallbackProxy;
import com.enioka.scanner.data.Barcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A bound service handling all the different scanner life cycles. Should usually be bound to the app itself.<br><br>
 * Note that the public API of this service is described in {@link ScannerServiceApi}, which the type obtained by binding to this service.
 * The other methods of this class should not be accessed by clients.
 */
public class ScannerService extends Service implements ScannerConnectionHandler, ScannerInitCallback, ScannerDataCallback, ScannerStatusCallback, ScannerServiceApi {

    protected final static String LOG_TAG = "ScannerService";

    private boolean firstBind = true;

    /**
     * Scanner instances. They should never leak outside of this service.
     */
    protected final List<Scanner> scanners = new ArrayList<>(10);

    /**
     * A subset of {@link #scanners} with only foreground scanners. These are only initialized if the service has a foreground client.
     */
    protected final Set<ScannerForeground> foregroundScanners = new HashSet<>(2);

    /**
     * The registered clients (both background and foreground).
     */
    protected final Set<BackgroundScannerClient> clients = new HashSet<>(1);

    /**
     * A helper count of scanners currently being initialized. When it reaches 0, we are ready.
     */
    private final AtomicInteger initializingScannersCount = new AtomicInteger(0);

    /**
     * True when all background scanners (which are only initialized once in the lifetime of the service) are OK.
     */
    private boolean backgroundScannersInitialized = false;

    /**
     * Callbacks which are run when there are no more scanners in the initializing state.
     */
    protected final List<EndOfInitCallback> endOfInitCallbacks = new ArrayList<>();

    /**
     * The options (which can be adapted from intent extra data) with which we look for scanners on this device.
     */
    private final ScannerSearchOptions scannerSearchOptions = ScannerSearchOptions.defaultOptions().getAllAvailableScanners();

    private interface EndOfInitCallback {
        void run();
    }


    ////////////////////////////////////////////////////////////////////////////
    // SERVICE BIND
    ////////////////////////////////////////////////////////////////////////////

    /**
     * We only allow app-local bind.
     */
    public class LocalBinder extends Binder {
        public ScannerServiceApi getService() {
            // Return this instance of ScannerService so clients can call public methods
            return ScannerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        scannerSearchOptions.fromIntentExtras(intent);

        if (firstBind) {
            this.initLaserScannerSearch();
        }
        firstBind = false;
        return new LocalBinder();
    }


    ////////////////////////////////////////////////////////////////////////////
    // SERVICE LIFECYCLE
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Starting scanner service");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "Destroying scanner service");
        this.disconnect();
        super.onDestroy();
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER PROVIDER CONNECTION HANDLERS
    ////////////////////////////////////////////////////////////////////////////

    public void restartScannerDiscovery() {
        this.disconnect();
        this.initLaserScannerSearch();
    }

    protected synchronized void initLaserScannerSearch() {
        LaserScanner.getLaserScanner(this.getApplicationContext(), new ScannerConnectionHandlerProxy(this), scannerSearchOptions);
    }

    @Override
    public void scannerConnectionProgress(String providerKey, String scannerKey, String message) {
        onStatusChanged(null, Status.CONNECTING);
    }

    @Override
    public void scannerCreated(String providerKey, String scannerKey, Scanner s) {
        Log.d(LOG_TAG, "Service has received a new scanner from provider " + providerKey + " and will initialize it. Its key is " + scannerKey);

        if (s instanceof ScannerBackground) {
            initializingScannersCount.incrementAndGet();
            ((ScannerBackground) s).initialize(this.getApplicationContext(),
                    new ScannerInitCallbackProxy(this),
                    new ScannerDataCallbackProxy(this),
                    new ScannerStatusCallbackProxy(this),
                    Scanner.Mode.BATCH);
        } else {
            Log.i(LOG_TAG, "This is a foreground scanner");
            // If foreground, only init it when we have an active activity.
            synchronized (foregroundScanners) {
                foregroundScanners.add((ScannerForeground) s);
            }
        }
    }

    @Override
    public void noScannerAvailable() {
        onStatusChanged(null, Status.SERVICE_SDK_SEARCH_NOCOMPATIBLE); // TODO - 2022/03/02: ScannerStatusCallback may not be the appropriate way to communicate Service status
        checkInitializationEnd();
    }

    @Override
    public void endOfScannerSearch() {
        Log.i(LOG_TAG, (scanners.size() + initializingScannersCount.get()) + " scanners from the different SDKs have reported for duty. Waiting for the initialization of the " + (scanners.size() + initializingScannersCount.get() - foregroundScanners.size()) + " background scanners.");
        checkInitializationEnd();
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER INIT HANDLERS
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnectionSuccessful(Scanner s) {
        Log.i(LOG_TAG, "A scanner has successfully initialized from provider " + s.getProviderKey());
        this.scanners.add(s);
        onStatusChanged(s, Status.CONNECTED);
        initializingScannersCount.decrementAndGet();
        checkInitializationEnd();
    }

    @Override
    public void onConnectionFailure(Scanner s) {
        Log.i(LOG_TAG, "A scanner has failed to initialize from provider " + s.getProviderKey());
        onStatusChanged(s, Status.FAILURE);
        initializingScannersCount.decrementAndGet();
        checkInitializationEnd();
    }

    private void checkInitializationEnd() {
        synchronized (scanners) {
            if (initializingScannersCount.get() != 0) {
                // We wait for all scanners
                return;
            }

            // If here, laser init has ended.
            backgroundScannersInitialized = true;
            Log.i(LOG_TAG, "All found scanners have now ended their initialization (or failed to do so)");

            // Run callbacks
            List<EndOfInitCallback> cbs = new ArrayList<>(endOfInitCallbacks);
            endOfInitCallbacks.clear();
            for (EndOfInitCallback callback : cbs) {
                callback.run();
            }

            // Notify
            onStatusChanged(null, Status.SERVICE_SDK_SEARCH_OVER); // TODO - 2022/03/02: ScannerStatusCallback may not be the appropriate way to communicate Service status
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER DATA HANDLERS
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onData(Scanner s, List<Barcode> data) {
        for (BackgroundScannerClient client : this.clients) {
            client.onData(data);
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // SERVICE API
    ////////////////////////////////////////////////////////////////////////////

    public void takeForegroundControl(final Activity activity, final ForegroundScannerClient client) {
        this.registerClient(client);

        Log.i(LOG_TAG, "Registering a new foreground activity");
        synchronized (scanners) {
            if (backgroundScannersInitialized && foregroundScanners.isEmpty()) {
                client.onForegroundScannerInitEnded(0, scanners.size());
                return;
            }

            if (backgroundScannersInitialized) {
                Log.i(LOG_TAG, "Reinitializing all foreground scanners");
                this.endOfInitCallbacks.add(() -> client.onForegroundScannerInitEnded(foregroundScanners.size(), scanners.size() - foregroundScanners.size()));
                for (ScannerForeground sf : foregroundScanners) {
                    ScannerService.this.initializingScannersCount.addAndGet(1);
                    sf.initialize(activity,
                            new ScannerInitCallbackProxy(ScannerService.this),
                            new ScannerDataCallbackProxy(ScannerService.this),
                            new ScannerStatusCallbackProxy(ScannerService.this),
                            Scanner.Mode.SINGLE_SCAN);
                }
            } else {
                this.endOfInitCallbacks.add(() -> { // FIXME - 2022/04/01: why two layers of callbacks ?
                    Log.i(LOG_TAG, "Initializing all foreground scanners");
                    ScannerService.this.endOfInitCallbacks.add(() -> client.onForegroundScannerInitEnded(foregroundScanners.size(), scanners.size() - foregroundScanners.size()));
                    for (ScannerForeground sf : foregroundScanners) {
                        ScannerService.this.initializingScannersCount.addAndGet(1);
                        sf.initialize(activity,
                                new ScannerInitCallbackProxy(ScannerService.this),
                                new ScannerDataCallbackProxy(ScannerService.this),
                                new ScannerStatusCallbackProxy(ScannerService.this),
                                Scanner.Mode.SINGLE_SCAN);
                    }
                });
            }
        }
    }

    public void registerClient(BackgroundScannerClient client) {
        this.clients.add(client);
    }

    public void unregisterClient(BackgroundScannerClient client) {
        this.clients.remove(client);
    }

    public void pressScanTrigger() {
        for (Scanner s : this.scanners) {
            s.pressScanTrigger();
        }
    }

    public void releaseScanTrigger() {
        for (Scanner s : this.scanners) {
            s.releaseScanTrigger();
        }
    }

    public boolean anyScannerSupportsIllumination() {
        for (Scanner s : this.scanners) {
            if (s.supportsIllumination()) {
                return true;
            }
        }
        return false;
    }

    public boolean anyScannerHasIlluminationOn() {
        for (Scanner s : this.scanners) {
            if (s.isIlluminationOn()) {
                return true;
            }
        }
        return false;
    }

    public void toggleIllumination() {
        for (Scanner s : this.scanners) {
            s.toggleIllumination();
        }
    }

    public void resume() {
        for (Scanner s : this.scanners) {
            s.resume();
        }
    }

    public void pause() {
        for (Scanner s : this.scanners) {
            s.pause();
        }
    }

    public void disconnect() {
        for (Scanner s : this.scanners) {
            s.disconnect();
        }
    }

    public void beep() {
        for (Scanner s : this.scanners) {
            s.beepScanSuccessful();
        }
    }

    public void ledColorOn(Color color) {
        for (Scanner s : this.scanners) {
            s.ledColorOn(color);
        }
    }

    public void ledColorOff(Color color) {
        for (Scanner s : this.scanners) {
            s.ledColorOff(color);
        }
    }

    // On ajoute quand même ces apis pour le ScannerServiceAPI pour plus de control sur le contenu et on expose tous les autres scanners pour donner une liberter pour les clients de la librairie

    @Override
    public Map<String, String> getFirstScannerStatus() {
        if (!this.scanners.isEmpty()) {
            return this.scanners.get(0).getStatus();
        }
        return new HashMap<>();
    }

    @Override
    public String getFirstScannerStatus(String key) {
        if (!this.scanners.isEmpty()) {
            return this.scanners.get(0).getStatus(key);
        }
        return null;
    }

    @Override
    public String getFirstScannerStatus(String key, boolean allowCache) {
        if (!this.scanners.isEmpty()) {
            return this.scanners.get(0).getStatus(key, allowCache);
        }
        return null;
    }

    @Override
    public List<Scanner> getConnectedScanners() {
        return scanners;
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER STATUS CALLBACK
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStatusChanged(final Scanner scanner, final ScannerStatusCallback.Status newStatus) {
        Log.d(LOG_TAG, "Status changed: " + newStatus.name() + " --- " + newStatus);
        for (BackgroundScannerClient client : ScannerService.this.clients) {
            client.onStatusChanged(scanner, newStatus);
        }

        switch (newStatus) {
            case DISCONNECTED:
                onScannerDisconnected(scanner);
                break;
            default:
                //ignore
        }
    }

    private void onScannerDisconnected(final Scanner scanner) {
        scanners.remove(scanner);
        if (scanner instanceof ScannerForeground) {
            foregroundScanners.remove(scanner);
        }
    }
}
