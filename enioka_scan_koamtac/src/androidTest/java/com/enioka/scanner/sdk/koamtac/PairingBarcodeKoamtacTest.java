package com.enioka.scanner.sdk.koamtac;

import android.graphics.Bitmap;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class PairingBarcodeKoamtacTest {
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
    public void testKoamtacBarcodePairing() {
        int[] refBarcode = new int[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 3, 32, 0, 0, 0, -96, 8, 6, 0, 0, 0, -12, 40, 8, 45, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 115, 66, 73, 84, 8, 8, 8, 8, 124, 8, 100, -120, 0, 0, 3, -77, 73, 68, 65, 84, 120, -100, -19, -41, 65, 14, -125, 32, 20, 64, -63, -46, -5, -33, -103, -34, -128, 5, 9, -81, -46, -50, -20, 85, -32, -85, -55, 27, 115, -50, -7, -70, -48, 24, 99, -21, -70, -43, 118, 87, -9, -36, 61, -90, 19, -9, -36, -75, -69, -106, -35, -77, -34, 117, -30, 92, 78, -20, -67, 62, -77, 91, 102, -12, -92, -17, -24, -60, 127, -30, -124, 122, -99, -11, -116, 118, -43, -77, 125, -46, 28, 110, 121, -34, 47, -20, -31, -60, -13, 86, -22, -1, -25, -54, 47, -68, 19, 43, -73, -52, -17, 73, 103, 86, 121, 127, 123, 1, 0, 0, -64, -1, 16, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, -103, 15, -32, -84, 89, 60, 103, 16, -59, -83, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        final KoamtacScannerProvider provider = new KoamtacScannerProvider();
        Bitmap result = provider.getPairingBarcode();

        compareBarcode(refBarcode, result);
    }

    @Test
    public void testKoamtacActivateBluetooth() {
        int[] refBarcode = new int[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 3, 32, 0, 0, 0, -96, 8, 6, 0, 0, 0, -12, 40, 8, 45, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 115, 66, 73, 84, 8, 8, 8, 8, 124, 8, 100, -120, 0, 0, 3, -84, 73, 68, 65, 84, 120, -100, -19, -41, 49, 18, -125, 48, 12, 0, -63, -112, -1, -1, -39, 121, -126, 11, 13, 103, -104, -20, -10, 8, -112, -35, -36, -75, -42, 90, -97, 7, -69, -82, 107, -12, -4, -18, -9, 118, -13, -89, -21, -71, 123, -2, -44, -35, -5, -67, -5, -3, 59, 111, 63, -1, -45, -9, -25, -12, -3, -40, 57, 125, 126, 83, -45, -13, -97, -50, -33, -79, -33, -39, -4, 29, -5, -99, -51, -33, 57, -3, -2, -87, -45, -9, -29, -23, -25, -69, -13, -12, -5, -1, -10, -3, 78, 125, 79, 127, 0, 0, 0, -16, 63, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, 70, -128, 0, 0, 0, 25, 1, 2, 0, 0, 100, 4, 8, 0, 0, -112, 17, 32, 0, 0, 64, -26, 7, 61, 118, 80, 60, -74, 23, -15, -32, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
        final KoamtacScannerProvider provider = new KoamtacScannerProvider();
        Bitmap result = provider.activateBluetoothBarcode();

        compareBarcode(refBarcode, result);
    }
}