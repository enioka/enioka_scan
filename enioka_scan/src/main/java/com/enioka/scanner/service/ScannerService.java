package com.enioka.scanner.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.enioka.scanner.LaserScanner;
import com.enioka.scanner.api.Scanner;
import com.enioka.scanner.api.callbacks.ScannerConnectionHandler;
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
import java.util.HashSet;
import java.util.List;
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
     * The registered clients (both background and foreground).
     */
    protected final Set<ScannerClient> clients = new HashSet<>(1);

    /**
     * A helper count of scanners currently being initialized. When it reaches 0, we are ready.
     */
    private final AtomicInteger initializingScannersCount = new AtomicInteger(0);

    /**
     * True when all scanners (which are only initialized once in the lifetime of the service) are OK.
     */
    private boolean allScannersInitialized = false;

    /**
     * Callbacks which are run when there are no more scanners in the initializing state.
     */
    protected final List<EndOfInitCallback> endOfInitCallbacks = new ArrayList<>();

    /**
     * The options (which can be adapted from intent extra data) with which we look for scanners on this device.
     */
    private ScannerSearchOptions scannerSearchOptions = ScannerSearchOptions.defaultOptions();

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

        this.initProviderDiscovery(() -> finishFirstBind(intent.getExtras()));
        return new LocalBinder();
    }

    private void finishFirstBind(final Bundle extras) {
        if (firstBind && (extras == null || extras.getBoolean(ScannerServiceApi.EXTRA_START_SEARCH_ON_SERVICE_BIND, true))) {
            this.initLaserScannerSearch();
        }
        firstBind = false;
        Log.d(LOG_TAG, "Service has finished its post-init processes");
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
    // SCANNER SERVICE API IMPL
    ////////////////////////////////////////////////////////////////////////////

    protected synchronized void initLaserScannerSearch() {
        Log.i(LOG_TAG, "(re)starting scanner search!");
        LaserScanner.getLaserScanner(this.getApplicationContext(), new ScannerConnectionHandlerProxy(this), scannerSearchOptions);
    }

    protected synchronized void initProviderDiscovery(final LaserScanner.OnProvidersDiscovered endOfDiscoveryCallback) {
        Log.i(LOG_TAG, "(re)starting provider discovery!");
        LaserScanner.discoverProviders(this.getApplicationContext(), () -> {
            onStatusChanged(null, Status.SERVICE_PROVIDER_SEARCH_OVER); // TODO - 2022/03/02: ScannerStatusCallback may not be the appropriate way to communicate Service status
            for (final ScannerClient client : clients) {
                client.onProviderDiscoveryEnded();
            }
            endOfDiscoveryCallback.onDiscoveryDone();
        });
    }

    @Override
    public void registerClient(ScannerClient client) {
        Log.d(LOG_TAG, "Registering new client: " + client.toString());
        this.clients.add(client);

        if (!firstBind) {
            Log.d(LOG_TAG, "Notifying late clients that providers are already discovered");
            client.onProviderDiscoveryEnded();
        }

        if (allScannersInitialized) {
            Log.d(LOG_TAG, "Notifying late clients that scanners are already connected");
            client.onScannerInitEnded(scanners.size());
        }
    }

    @Override
    public void unregisterClient(ScannerClient client) {
        this.clients.remove(client);
    }

    @Override
    public void restartScannerDiscovery() {
        this.disconnect();
        this.initLaserScannerSearch();
    }

    @Override
    public void restartProviderDiscovery(final LaserScanner.OnProvidersDiscovered endOfDiscoveryCallback) {
        this.initProviderDiscovery(endOfDiscoveryCallback);
    }

    @Override
    public List<String> getAvailableProviders() {
        return LaserScanner.getProviderCache();
    }

    @Override
    public void updateScannerSearchOptions(ScannerSearchOptions newOptions) {
        this.scannerSearchOptions = newOptions;
    }

    @Override
    public void resume() {
        for (Scanner s : this.scanners) {
            s.resume();
        }
    }

    @Override
    public void pause() {
        for (Scanner s : this.scanners) {
            s.pause();
        }
    }

    @Override
    public void disconnect() {
        for (Scanner s : this.scanners) {
            s.disconnect();
        }
    }

    @Override
    public List<Scanner> getConnectedScanners() {
        return scanners;
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER CONNECTION HANDLER IMPL
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void scannerConnectionProgress(String providerKey, String scannerKey, String message) {
        onStatusChanged(null, Status.CONNECTING);
        Log.d(LOG_TAG, "Progress on connection of scanner " + scannerKey + " from " + providerKey + ": " + message);
    }

    @Override
    public void scannerCreated(String providerKey, String scannerKey, Scanner s) {
        Log.d(LOG_TAG, "Service has received a new scanner from provider " + providerKey + " and will initialize it. Its key is " + scannerKey);

        initializingScannersCount.incrementAndGet();
        Log.d(LOG_TAG, "New scanner initializing, total initializing: " + initializingScannersCount.get());
        s.initialize(this.getApplicationContext(),
                new ScannerInitCallbackProxy(this),
                new ScannerDataCallbackProxy(this),
                new ScannerStatusCallbackProxy(this),
                Scanner.Mode.BATCH);
    }

    @Override
    public void noScannerAvailable() {
        onStatusChanged(null, Status.SERVICE_SDK_SEARCH_NOCOMPATIBLE); // TODO - 2022/03/02: ScannerStatusCallback may not be the appropriate way to communicate Service status
        checkInitializationEnd();
    }

    @Override
    public void endOfScannerSearch() {
        Log.i(LOG_TAG, (scanners.size() + initializingScannersCount.get()) + " scanners from the different SDKs have reported for duty. Waiting for the initialization of " + initializingScannersCount.get() + " scanners.");
        checkInitializationEnd();
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER INIT CALLBACK IMPL
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnectionSuccessful(Scanner s) {
        Log.i(LOG_TAG, "A scanner has successfully initialized from provider " + s.getProviderKey());
        this.scanners.add(s);
        onStatusChanged(s, Status.CONNECTED);
        initializingScannersCount.decrementAndGet();
        Log.d(LOG_TAG, "New scanner initialized (success), total initializing: " + initializingScannersCount.get());
        checkInitializationEnd();
    }

    @Override
    public void onConnectionFailure(Scanner s) {
        Log.i(LOG_TAG, "A scanner has failed to initialize from provider " + s.getProviderKey());
        onStatusChanged(s, Status.FAILURE);
        initializingScannersCount.decrementAndGet();
        Log.d(LOG_TAG, "New scanner initialized (failure), total initializing: " + initializingScannersCount.get());
        checkInitializationEnd();
    }

    private void checkInitializationEnd() {
        synchronized (scanners) {
            if (initializingScannersCount.get() != 0) {
                // We wait for all scanners
                return;
            }

            // If here, laser init has ended.
            allScannersInitialized = true;
            Log.i(LOG_TAG, "All found scanners have now ended their initialization (or failed to do so)");

            // Run callbacks
            List<EndOfInitCallback> cbs = new ArrayList<>(endOfInitCallbacks);
            endOfInitCallbacks.clear();
            for (EndOfInitCallback callback : cbs) {
                callback.run();
            }

            // Notify
            onStatusChanged(null, Status.SERVICE_SDK_SEARCH_OVER); // TODO - 2022/03/02: ScannerStatusCallback may not be the appropriate way to communicate Service status

            for (final ScannerClient client : clients) {
                client.onScannerInitEnded(scanners.size());
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER DATA CALLBACK IMPL
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onData(Scanner s, List<Barcode> data) {
        for (final ScannerClient client : clients) {
            client.onData(data);
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // SCANNER STATUS CALLBACK IMPL
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStatusChanged(final Scanner scanner, final ScannerStatusCallback.Status newStatus) {
        Log.d(LOG_TAG, "Status changed: " + newStatus.name() + " --- " + newStatus.getLocalizedMessage(this));
        for (ScannerClient client : ScannerService.this.clients) {
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
    }
}
