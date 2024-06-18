package com.enioka.scanner.sdk.honeywelloss.spp;

import android.graphics.Bitmap;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class PairingBarcodeHoneywellOssSppTest {
    // Wrapper for the test to easily compare the reference barcode with the generated barcode
    public void compareBarcode(int[] ref, Bitmap result) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        Assert.assertEquals("Barcode length mismatch", ref.length, byteArray.length);
        for (int i = 0; i < ref.length; i++) {
            Assert.assertEquals(ref[i], byteArray[i]);
        }
    }

    @Test
    public void testHoneywellDefaultBarcodePairing() {
        int[] refBarcode = new int[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 3, 32, 0, 0, 0, -96, 8, 6, 0, 0, 0, -12, 40, 8, 45, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 115, 66, 73, 84, 8, 8, 8, 8, 124, 8, 100, -120, 0, 0, 3, -23, 73, 68, 65, 84, 120, -100, -19, -41, 65, 110, -62, 48, 20, 64, -63, -90, -9, -65, 115, -70, 101, -127, 37, 71, -74, 31, 72, -99, 89, 34, 39, -7, 56, 64, 120, -41, 125, -33, -9, 15, 91, 92, -41, -11, -10, -11, -41, 45, 126, 93, 51, -38, -6, -103, 53, 51, 51, -52, 28, 59, 90, 63, -13, 94, 102, -50, 57, 58, 118, -76, 102, -27, 90, 51, -5, -4, 116, -1, -97, -50, -77, 50, -37, -52, 121, -98, -50, 115, -6, -2, 126, -37, -4, -93, -13, -97, 56, -49, -116, -107, 61, 95, -7, -84, -82, -52, -80, 50, -25, -82, -11, -27, 12, -89, -17, -17, -52, -6, -45, 123, -69, 50, -61, -54, 60, -93, 99, 79, 124, 55, 103, -82, 59, 58, 118, -41, 115, 115, -41, 12, -93, 53, 51, 51, -84, -20, -37, -82, -33, -100, -47, -75, 78, 63, 127, 79, 63, 23, -4, 93, -34, -21, -9, -45, 3, 0, 0, 0, -1, -121, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, -4, 1, 125, -101, -77, 60, 49, -78, -13, -55, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        final HoneywellOssSppScannerProvider provider = new HoneywellOssSppScannerProvider();
        Bitmap result = provider.getPairingBarcode();

        compareBarcode(refBarcode, result);
    }

    @Test
    public void testHoneywellActivateBluetoothBarcodePairing() {
        int[] refBarcode = new int[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 3, 32, 0, 0, 0, -96, 8, 6, 0, 0, 0, -12, 40, 8, 45, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 115, 66, 73, 84, 8, 8, 8, 8, 124, 8, 100, -120, 0, 0, 3, -70, 73, 68, 65, 84, 120, -100, -19, -41, -63, 9, -61, 48, 16, 0, -63, 56, -3, -9, -20, 84, -96, -57, -127, -75, -74, -55, -52, 59, 72, 70, 57, 9, -10, 56, -49, -13, -4, -4, -79, -29, 56, 70, -65, 95, 29, -41, 106, -99, -23, -15, 94, -75, -50, -44, -18, 125, -89, -21, 95, -11, -65, 76, -19, -98, -121, -87, -89, -99, -1, 93, -13, -65, -14, -76, 123, -15, -76, -71, 125, -38, -4, 76, -67, -3, 94, -17, -66, 47, 43, 119, -83, -65, -78, -5, 29, -104, -18, 123, -41, 123, -75, -78, -5, 30, 93, -75, -17, 91, -34, -67, -87, -73, -97, -61, 91, 125, -17, -2, 0, 0, 0, -32, 127, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -116, 0, 1, 0, 0, 50, 2, 4, 0, 0, -56, 8, 16, 0, 0, 32, 35, 64, 0, 0, -128, -52, 15, -40, 34, 98, 60, 79, -5, -52, -48, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        final HoneywellOssSppScannerProvider provider = new HoneywellOssSppScannerProvider();
        Bitmap result = provider.activateBluetoothBarcode();

        compareBarcode(refBarcode, result);
    }
}