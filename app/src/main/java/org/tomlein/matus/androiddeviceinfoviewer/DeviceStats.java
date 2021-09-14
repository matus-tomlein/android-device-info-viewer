package org.tomlein.matus.androiddeviceinfoviewer;

import android.content.Context;
import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import androidx.annotation.NonNull;

public class DeviceStats {
    private Context context;

    public DeviceStats(Context context) {
        this.context = context;
    }

    public double getAvailableMemoryInMB() {
        MemoryInfo mi = getMemoryInfo();
        return toMB(mi.availMem);
    }

    public double getTotalMemoryInMB() {
        MemoryInfo mi = getMemoryInfo();
        return toMB(mi.totalMem);
    }

    public boolean getLowMemory() {
        MemoryInfo mi = getMemoryInfo();
        return mi.lowMemory;
    }

    public double getRuntimeTotalMemoryInMB() {
        return toMB(Runtime.getRuntime().totalMemory());
    }

    public double getRuntimeFreeMemoryInMB() {
        return toMB(Runtime.getRuntime().freeMemory());
    }

    public boolean isCharging() {
        BatteryManager batteryManager = getBatteryManager();
        if (batteryManager == null) { return false; }
        return batteryManager.isCharging();
    }

    public int getBatteryLevel() {
        BatteryManager batteryManager = getBatteryManager();
        if (batteryManager == null) { return 0; }
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public double getTotalStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return toMB(statFs.getTotalBytes());
    }

    public double getFreeStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return toMB(statFs.getFreeBytes());
    }

    public String getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager == null) { return null; }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) { return "no network"; }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                return "no network";
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ? "wifi" : "data";
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (!networkInfo.isConnected()) { return "no network"; }
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI ? "wifi" : "data";
        }
    }

    private BatteryManager getBatteryManager() {
        return (BatteryManager) context.getSystemService(BATTERY_SERVICE);
    }

    @NonNull
    private MemoryInfo getMemoryInfo() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        activityManager.getMemoryInfo(mi);
        return mi;
    }

    private double toMB(long bytes) {
        return bytes / 0x100000L;
    }

}
