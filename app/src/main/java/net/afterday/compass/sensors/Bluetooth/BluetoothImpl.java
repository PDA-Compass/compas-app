package net.afterday.compass.sensors.Bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 4/2/2018.
 */
@TargetApi(18)
public class BluetoothImpl implements Bluetooth
{
    private static final String TAG = "BluetoothImpl";
    private Context context;
    private IntentFilter intentFilter;
    private Observable<BluetoothScanResult> resultStream = PublishSubject.create();
    private BluetoothAdapter bla;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private BluetoothReceiver br = new BluetoothReceiver();
    private BluetoothAdapter.LeScanCallback callback;

    public BluetoothImpl(Context context)
    {
        this.context = context;
        br = new BluetoothReceiver();
        bla = BluetoothAdapter.getDefaultAdapter();
        //BluetoothAdapter.ACTION_DISCOVERY_FINISHED
        intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        callback = new LeScanCallback();
    }

    @Override
    public void start()
    {
        //context.registerReceiver(br, intentFilter);
        isRunning.set(true);
        bla.startLeScan(callback);
    }

    @Override
    public void stop()
    {
        isRunning.set(false);
    }

    @Override
    public Observable<BluetoothScanResult> getSensorResultsStream()
    {
        return resultStream;
    }

    private class LeScanCallback implements BluetoothAdapter.LeScanCallback
    {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes)
        {
            long now = System.currentTimeMillis();
            if(bluetoothDevice.getName() != null)
            {
                ((Subject<BluetoothScanResult>)resultStream).onNext(new BluetoothScanResultImpl(bluetoothDevice, i, now));
            }
            if(!isRunning.get())
            {
                bla.stopLeScan(callback);
            }
            //Log.e(TAG, "SCANNED!!!!!!" + bluetoothDevice.getAddress() + " " + bluetoothDevice.getName() + " " + i + " " + bytes);
        }
    }

    private class BluetoothReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction()))
            {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("BLUETOOTH RECEIVED!", "" + intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
            }
            if(isRunning.get())
            {
                Log.e(TAG,"START DISCOVERY");
                bla.startDiscovery();
            }
        }
    }
}