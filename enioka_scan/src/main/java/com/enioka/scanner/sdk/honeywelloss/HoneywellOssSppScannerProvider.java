package com.enioka.scanner.sdk.honeywelloss;

import com.enioka.scanner.bt.api.BluetoothScanner;
import com.enioka.scanner.bt.api.BtSppScannerProvider;
import com.enioka.scanner.bt.api.DataSubscriptionCallback;
import com.enioka.scanner.bt.api.ScannerDataParser;
import com.enioka.scanner.sdk.honeywelloss.commands.Cleanup;
import com.enioka.scanner.sdk.honeywelloss.commands.GetFirmware;
import com.enioka.scanner.sdk.honeywelloss.data.FirmwareVersion;
import com.enioka.scanner.sdk.honeywelloss.parsers.HoneywellOssParser;

public class HoneywellOssSppScannerProvider implements BtSppScannerProvider {
    public static final String PROVIDER_KEY = "BT_HoneywellOssProvider";

    private final ScannerDataParser inputHandler = new HoneywellOssParser();

    @Override
    public void canManageDevice(final BluetoothScanner device, final ManagementCallback callback) {
        device.runCommand(new Cleanup(), null);
        testFirmwareCommand(device, callback);
    }

    @Override
    public String getKey() {
        return PROVIDER_KEY;
    }

    private void testFirmwareCommand(final BluetoothScanner device, final ManagementCallback callback) {
        device.runCommand(new GetFirmware(), new DataSubscriptionCallback<FirmwareVersion>() {
            @Override
            public void onSuccess(FirmwareVersion data) {
                callback.canManage(new HoneywellOssScanner(device));
            }

            @Override
            public void onFailure() {
                callback.cannotManage();
            }

            @Override
            public void onTimeout() {
                callback.cannotManage();
            }
        });
    }

    @Override
    public ScannerDataParser getInputHandler() {
        return inputHandler;
    }

}
