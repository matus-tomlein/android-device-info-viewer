package org.tomlein.matus.androiddeviceinfoviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeviceStats deviceStats = new DeviceStats(this);

        double availableMemory = deviceStats.getAvailableMemoryInMB();
        setTextForTextView(R.id.availableMemoryTextView, String.format("%.2f MB available system memory", availableMemory));

        double totalMemory = deviceStats.getTotalMemoryInMB();
        setTextForTextView(R.id.totalMemoryTextView, String.format("%.2f MB total system memory", totalMemory));

        setTextForTextView(R.id.lowMemoryTextView, deviceStats.getLowMemory() ? "Low memory" : "Not low memory");

        double runtimeFreeMemory = deviceStats.getRuntimeFreeMemoryInMB();
        setTextForTextView(R.id.runtimeFreeMemoryTextView, String.format("%.2f MB free memory in VM (from Runtime)", runtimeFreeMemory));

        double runtimeTotalMemory = deviceStats.getRuntimeTotalMemoryInMB();
        setTextForTextView(R.id.runtimeTotalMemoryTextView, String.format("%.2f MB total memory in VM (from Runtime)", runtimeTotalMemory));

        setTextForTextView(R.id.isChargingTextView, deviceStats.isCharging() ? "Charing": "Not charging");

        int batteryLevel = deviceStats.getBatteryLevel();
        setTextForTextView(R.id.batteryLevelTextView, String.format("%d%% battery level", batteryLevel));

        double freeStorage = deviceStats.getFreeStorage();
        setTextForTextView(R.id.freeStorageTextView, String.format("%.2f MB free storage", freeStorage));

        double totalStorage = deviceStats.getTotalStorage();
        setTextForTextView(R.id.totalStorageTextView, String.format("%.2f MB total storage", totalStorage));

        setTextForTextView(R.id.networkTypeTextView, deviceStats.getNetworkType());
    }

    private void setTextForTextView(int viewId, String text) {
        TextView totalMemoryTextView = (TextView) findViewById(viewId);
        totalMemoryTextView.setText(text);
    }
}