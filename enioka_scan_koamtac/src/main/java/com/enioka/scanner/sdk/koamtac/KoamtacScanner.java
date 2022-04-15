package com.enioka.scanner.sdk.koamtac;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.enioka.scanner.api.ScannerLedColor;
import com.enioka.scanner.api.Scanner;
import com.enioka.scanner.api.callbacks.ScannerStatusCallback;
import com.enioka.scanner.api.proxies.ScannerDataCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerInitCallbackProxy;
import com.enioka.scanner.api.proxies.ScannerStatusCallbackProxy;
import com.enioka.scanner.data.Barcode;

import java.util.ArrayList;
import java.util.List;

import koamtac.kdc.sdk.KDCBarcodeDataReceivedListener;
import koamtac.kdc.sdk.KDCConnectionListener;
import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCDevice;
import koamtac.kdc.sdk.KDCErrorListener;
import koamtac.kdc.sdk.KDCReader;
import koamtac.kdc.sdk.KDCSymbology;

class KoamtacScanner implements Scanner, Scanner.WithBeepSupport, Scanner.WithLedSupport, KDCBarcodeDataReceivedListener, KDCConnectionListener, KDCErrorListener {
    private static final String LOG_TAG = "KoamtacScanner";

    private KDCReader scanner;
    private BluetoothDevice btDevice;
    private ScannerDataCallbackProxy dataCallback;
    private ScannerStatusCallbackProxy statusCallback;
    private ScannerInitCallbackProxy initCallback;
    private Context ctx;

    KoamtacScanner(BluetoothDevice device) {
        this.btDevice = device;
    }

    @Override
    public void initialize(final Context applicationContext, final ScannerInitCallbackProxy initCallback, final ScannerDataCallbackProxy dataCallback, final ScannerStatusCallbackProxy statusCallback, final Mode mode) {
        this.ctx = applicationContext;
        this.dataCallback = dataCallback;
        this.statusCallback = statusCallback;
        this.initCallback = initCallback;

        Log.i(LOG_TAG, "Start of initialization of Koamtac scanner " + btDevice.getName() + ". SDK version " + KDCReader.GetKDCReaderVersion());

        scanner = new KDCReader(null, this, null, null, null, this, false);

        scanner.EnableAutoConnectionMode(false);
        scanner.SetContext(applicationContext); // Compulsory for BLE.
        scanner.SetKDCErrorListener(this);

        scanner.SetConnectionMode(KDCConstants.ConnectionMode.BLUETOOTH_SMART);

        /*scanner.EnableAttachType(true);
        scanner.EnableAttachSerialNumber(true);
        scanner.EnableAttachTimestamp(true);
        scanner.EnableAttachLocation(true);
        scanner.SetDataDelimiter(KDCConstants.DataDelimiter.SEMICOLON);
        scanner.SetRecordDelimiter(KDCConstants.RecordDelimiter.LF);*/

        if (scanner.IsConnected()) {
            scanner.Disconnect();
        }
        Log.d(LOG_TAG, "Calling connect");
        scanner.Connect(this.btDevice);  // callbacks are called in ConnectionChanged method.
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // KOAMTAC CALLBACKS & METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void BarcodeDataReceived(KDCData kdcData) {
        if (dataCallback != null) {
            Barcode barcode = new Barcode(kdcData.GetData(), KoamtacDataTranslator.sdk2Api(kdcData.GetBarcodeSymbology()));

            final List<Barcode> res = new ArrayList<>(1);
            res.add(barcode);

            // Use a handler from the main message loop to run on the UI thread, as this method is called by another thread.
            dataCallback.onData(KoamtacScanner.this, res);
        }
    }

    @Override
    public void ConnectionChanged(BluetoothDevice bluetoothDevice, int i) {
        final ScannerStatusCallback.Status status;
        switch (i) {
            case KDCConstants.CONNECTION_STATE_CONNECTED:
                status = ScannerStatusCallback.Status.CONNECTED;

                configureScanner();
                if (initCallback != null) {
                    initCallback.onConnectionSuccessful(KoamtacScanner.this);
                }

                break;
            case KDCConstants.CONNECTION_STATE_CONNECTING:
                status = ScannerStatusCallback.Status.CONNECTING;
                break;
            case KDCConstants.CONNECTION_STATE_FAILED:
                status = ScannerStatusCallback.Status.FAILURE;
                break;
            case KDCConstants.CONNECTION_STATE_INITIALIZING:
                status = ScannerStatusCallback.Status.INITIALIZING;
                break;
            case KDCConstants.CONNECTION_STATE_INITIALIZING_FAILED:
                status = ScannerStatusCallback.Status.FAILURE;
                if (initCallback != null) {
                    initCallback.onConnectionFailure(KoamtacScanner.this);
                }
                break;
            case KDCConstants.CONNECTION_STATE_LISTEN:
                status = ScannerStatusCallback.Status.WAITING;
                break;
            case KDCConstants.CONNECTION_STATE_LOST:
                status = ScannerStatusCallback.Status.DISCONNECTED;
                break;
            case KDCConstants.CONNECTION_STATE_NONE:
            default:
                // Not doing anything.
                status = ScannerStatusCallback.Status.UNKNOWN;
                break;
        }

        if (status == ScannerStatusCallback.Status.UNKNOWN) {
            return;
        }

        if (this.statusCallback != null) {
            statusCallback.onStatusChanged(KoamtacScanner.this, status);
        }
    }

    private void configureScanner() {
        this.scanner.DisableAllSymbologies();
        KDCSymbology s = this.scanner.GetSymbology();
        s.Enable(KDCConstants.Symbology.CODE128, true);
        s.Enable(KDCConstants.Symbology.I2OF5, true);
        s.Enable(KDCConstants.Symbology.CODE35, true);
        this.scanner.SetSymbology(s, scanner.GetKDCDeviceInfo());
    }

    @Override
    public void ErrorReceived(KDCDevice<?> kdcDevice, int i) {
        Log.e(LOG_TAG, "ERROR in KDC SCANNER: " + i);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFE CYCLE
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setDataCallBack(ScannerDataCallbackProxy cb) {
        this.dataCallback = cb;
    }

    @Override
    public void disconnect() {
        if (this.scanner != null && this.scanner.IsConnected()) {
            this.scanner.Disconnect();
        }
        if (this.scanner != null) {
            this.scanner.Dispose();
        }
    }

    @Override
    public void pause() {
        this.scanner.LockButtons(KDCConstants.ButtonLockType.SCAN_BUTTON_ONLY);
    }

    @Override
    public void resume() {
        this.scanner.UnlockButtons(KDCConstants.ButtonLockType.SCAN_BUTTON_ONLY);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BEEPS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void beepScanSuccessful() {
        this.scanner.SetSuccessAlertBeep();
    }

    @Override
    public void beepScanFailure() {
        this.scanner.SetFailureAlertBeep();
    }

    @Override
    public void beepPairingCompleted() {
        this.scanner.SetSuccessAlertBeep();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LED
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void ledColorOn(ScannerLedColor color) {
        this.scanner.SetDisplayMessage(color.toString());
    }

    @Override
    public void ledColorOff(ScannerLedColor color) {
        this.scanner.SetDisplayMessage("");
    }

    @Override
    public String getProviderKey() {
        return KoamtacScannerProvider.PROVIDER_KEY;
    }
}
