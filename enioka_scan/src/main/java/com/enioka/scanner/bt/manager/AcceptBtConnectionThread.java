package com.enioka.scanner.bt.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * A SPP service listener thread on a bluetooth adapter. Used by master BT devices to connect to the phone. Closes after the first connection (OK or not). No timeout.
 */
class AcceptBtConnectionThread extends Thread {
    private static final String LOG_TAG = "BtSppSdk";

    private final BluetoothServerSocket serverSocket;
    private final ConnectToBtDeviceThread.OnConnectedCallback onConnectedCallback;
    private final BtSppScannerProvider parentProvider;

    private boolean done = false;

    AcceptBtConnectionThread(BluetoothAdapter bluetoothAdapter, ConnectToBtDeviceThread.OnConnectedCallback callback, BtSppScannerProvider parentProvider) {
        this.onConnectedCallback = callback;
        this.parentProvider = parentProvider;

        BluetoothServerSocket serverSocket = null;
        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("SPP", ConnectToBtDeviceThread.SERVER_BT_SERVICE_UUID);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Socket's create() method failed", e);
        }
        this.serverSocket = serverSocket;
    }

    public void run() {
        BluetoothSocket clientSocket;
        Log.i(LOG_TAG, "Starting bluetooth slave server and waiting for incoming connections");

        while (true) {
            try {
                // Connect to the remote device through the socket. This call blocks until it succeeds or throws an exception.
                clientSocket = this.serverSocket.accept();
                Log.i(LOG_TAG, "BT master connection received!");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                Log.e(LOG_TAG, "Could not accept device connection. " + connectException.getMessage());
                break;
            }

            if (clientSocket != null) {
                if (this.onConnectedCallback != null) {
                    this.onConnectedCallback.connected(new BtSppScanner(parentProvider, clientSocket));
                    Log.i(LOG_TAG, "Live socket opened for incoming SPP BT device.");
                }

                this.cancel();
                break;
            }
        }

        Log.d(LOG_TAG, "BT slave server stops listening for incoming connections");
        this.done = true;
    }

    // Closes the client socket and causes the thread to finish.
    void cancel() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not close the client socket", e);
        }
    }

    boolean isDone() {
        return this.done;
    }
}
