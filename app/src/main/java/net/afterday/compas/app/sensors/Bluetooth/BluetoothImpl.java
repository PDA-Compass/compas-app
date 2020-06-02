package net.afterday.compas.app.sensors.Bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.sensors.Bluetooth.Bluetooth;
import net.afterday.compas.engine.sensors.SensorResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@TargetApi(19)
public class BluetoothImpl implements Bluetooth {
    private static final String TAG = "BluetoothImpl";
    private Context context;
    private IntentFilter intentFilter;
    private Subject<SensorResult> resultStream = PublishSubject.create();
    private BluetoothAdapter bla;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private BluetoothReceiver br = new BluetoothReceiver();
    private BluetoothAdapter.LeScanCallback callback;
    private List<String> registeredMacs = new ArrayList<>();
    private Disposable resetter;

    public BluetoothImpl(Context context)
    {
        this.context = context;
        setup();
        br = new BluetoothReceiver();
        bla = BluetoothAdapter.getDefaultAdapter();
        //BluetoothAdapter.ACTION_DISCOVERY_FINISHED
        intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        callback = new LeScanCallback();
    }

    private void setup()
    {
        //Afterday Project
        registeredMacs.add("24:0A:C4:82:E7:16");
        registeredMacs.add("FF:FF:3D:F9:15:7E");
        registeredMacs.add("FF:FF:3C:FA:C6:B3");
        registeredMacs.add("FF:FF:3B:FE:9F:38");
        registeredMacs.add("FF:FF:3D:F9:35:58");
        registeredMacs.add("FF:FF:3F:F4:32:BB");
        registeredMacs.add("FF:FF:3E:F3:96:5A");
        registeredMacs.add("FF:FF:3E:F4:03:7B");
        registeredMacs.add("FF:FF:3E:F3:F1:0F");
        registeredMacs.add("FF:FF:3E:F4:0A:5E");
        registeredMacs.add("FF:FF:3B:FE:9C:90");
        registeredMacs.add("FF:FF:3E:F3:F4:EC");
        registeredMacs.add("FF:FF:3C:FA:D9:E8");
        registeredMacs.add("FF:FF:3E:F3:F5:14");
        registeredMacs.add("FF:FF:3F:F3:DC:BF");
        registeredMacs.add("FF:FF:3F:F4:27:A6");
        registeredMacs.add("FF:FF:3E:F4:13:90");
        registeredMacs.add("FF:FF:3E:F3:97:32");
        registeredMacs.add("FF:FF:39:F3:95:0E");
        registeredMacs.add("FF:FF:3E:F3:F4:EC");
        //
    }

    @Override
    public void start()
    {
        //context.registerReceiver(br, intentFilter);
        isRunning.set(true);
        if (bla != null) {
            bla.startLeScan(callback);
        }
    }

    @Override
    public void stop()
    {
        isRunning.set(false);
    }

    @Override
    public Observable<SensorResult> getSensorResultsStream()
    {
        return resultStream;
    }

    private class LeScanCallback implements BluetoothAdapter.LeScanCallback
    {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes)
        {
            //TODO:  filter bluetooth Device
            long now = System.currentTimeMillis();
            if(registeredMacs.contains(bluetoothDevice.getAddress()))
            {
                if(resetter != null && !resetter.isDisposed())
                {
                    resetter.dispose();
                }
                resultStream.onNext(new SensorResult(
                        bluetoothDevice.getAddress(),
                        bluetoothDevice.getName(),
                        i,
                        now));

                //TODO: понять что это? //resetter = Observable.timer(3, TimeUnit.SECONDS).subscribe((x) -> ((Subject<Double>)resultStream).onNext(0d));
                //((Subject<BluetoothScanResult>)resultStream).onNext(new BluetoothScanResultImpl(bluetoothDevice.getAddress(), i, now));
            }
            else
            {
                resultStream.onNext(new SensorResult(
                        bluetoothDevice.getAddress(),
                        bluetoothDevice.getName(),
                        i,
                        now));
            }
            if(!isRunning.get())
            {
                bla.stopLeScan(callback);
            }
            Log.d(TAG, "SCANNED!!!!!!" + bluetoothDevice.getAddress() + " " + bluetoothDevice.getName() + " " + i + " " + bytes);
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