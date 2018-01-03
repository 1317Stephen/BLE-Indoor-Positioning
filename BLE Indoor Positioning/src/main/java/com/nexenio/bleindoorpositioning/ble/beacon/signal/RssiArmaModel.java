package com.nexenio.bleindoorpositioning.ble.beacon.signal;

/**
 * Created by leon on 20.12.17.
 *
 * taken from https://github.com/AltBeacon/android-beacon-library/blob/master/src/main/java/org/altbeacon/beacon/service/ArmaRssiFilter.java
 *
 * This filter calculates its rssi on base of an auto regressive moving average (ARMA) It needs only
 * the current value to do this; the general formula is  n(t) = n(t-1) - c * (n(t-1) - n(t)) where c
 * is a coefficient, that denotes the smoothness - the lower the value, the smoother the average
 * Note: a smoother average needs longer to "settle down" Note: For signals, that change rather
 * frequently (say, 1Hz or faster) and tend to vary more a recommended value would be 0,1 (that
 * means the actual value is changed by 10% of the difference between the actual measurement and the
 * actual average) For signals at lower rates (10Hz) a value of 0.25 to 0.5 would be appropriate.
 *
 * <a href="https://en.wikipedia.org/wiki/Autoregressive–moving-average_model">Autoregressive Moving
 * Average Model</a>
 */

public class RssiArmaModel {

    // How likely is it that the RSSI value changes?
    // Note: the more unlikely, the higher can that value be also, the lower the (expected) sending frequency,
    // the higher should that value be
    private static float DEFAULT_ARMA_FACTOR = 1f;
    private static float armaFactor;
    private float armaRssi;
    private boolean isInitialized = false;

    public RssiArmaModel() {
        this.armaFactor = DEFAULT_ARMA_FACTOR;
    }

    public void addMeasurement(int rssi, float packetFrequency) {
        //use first measurement as initialization
        if (!isInitialized) {
            armaRssi = rssi;
            isInitialized = true;
        }
        armaRssi = armaRssi - getArmaFactor(packetFrequency) * (armaRssi - rssi);
    }

    public float getFilteredRssi() {
        return armaRssi;
    }

    public static float getArmaFactor(float packetFrequency) {
        if (packetFrequency > 7) {
            armaFactor = 0.25f;
        } else if (packetFrequency > 6) {
            armaFactor = 0.5f;
        } else if (packetFrequency > 5) {
            armaFactor = 0.75f;
        }
        return armaFactor;
    }

}
