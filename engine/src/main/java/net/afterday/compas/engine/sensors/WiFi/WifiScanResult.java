package net.afterday.compas.engine.sensors.WiFi;

public interface WifiScanResult {
    String getName();
    int getStrength();
    long getScanTime();

    String getSsid();
    String getBssid();
    int getLevel();
}
