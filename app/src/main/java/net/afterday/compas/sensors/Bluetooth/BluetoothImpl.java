package net.afterday.compas.sensors.Bluetooth;

import static com.crashlytics.android.Crashlytics.log;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import net.afterday.compas.core.player.Player;
import net.afterday.compas.engine.events.PlayerEventBus;
import net.afterday.compas.logging.Logger;
import net.afterday.compas.sensors.SensorResult;
import net.afterday.compas.util.BleAdvertisedData;
import net.afterday.compas.util.BleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@TargetApi(19)
public class BluetoothImpl implements Bluetooth {
    private static final String TAG = "BluetoothImpl";
    private Context context;
    private IntentFilter intentFilter;
    private Observable<List<SensorResult>> resultStream = PublishSubject.create();
    private Observable<Double> detectorStream = PublishSubject.create();
    private BluetoothAdapter bla;
    private Integer level;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private AtomicBoolean isBleRunning = new AtomicBoolean(false);
    private BluetoothReceiver br = new BluetoothReceiver();
    private BluetoothAdapter.LeScanCallback callback;
    private HashMap<String, String> registeredMacs = new HashMap<>();
    private Set<String> artefactMacs = new HashSet<>();
    private Observable tick;
    private HashMap<String, SensorResult> accum = new HashMap<>();
    private Boolean isDarken = false;
    private Disposable resetter;
    private Disposable disposableTick;
    private Disposable disposableStatus;

    public BluetoothImpl(Context context)
    {
        this.context = context;
        setup();
        br = new BluetoothReceiver();
        bla = BluetoothAdapter.getDefaultAdapter();
        //BluetoothAdapter.ACTION_DISCOVERY_FINISHED
        intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        callback = new LeScanCallback();
        context.registerReceiver(br, intentFilter);
    }

    private void setup()
    {
        registeredMacs.put("24:0A:C4:82:E7", "R100");
        registeredMacs.put("24:0A:C4:82:E8", "M100");
        registeredMacs.put("24:0A:C4:82:E9", "H100");
        registeredMacs.put("24:0A:C4:82:A0", "C100");
        registeredMacs.put("24:0A:C4:82:A1", "B100");
        registeredMacs.put("24:0A:C4:82:A2", "C200");
        registeredMacs.put("24:0A:C4:82:A3", "B200");
        registeredMacs.put("24:0A:C4:82:A4", "M200");
        registeredMacs.put("24:0A:C4:82:A5", "R900");

        artefactMacs.add("24:0A:C4:82:B0");
    }

    @Override
    public void start()
    {
        //context.registerReceiver(br, intentFilter);
        if (!isRunning.get()) {
            isRunning.set(true);
            if (bla != null) {
                Boolean result = bla.startLeScan(callback);
                if (!result) {
                    Logger.e("Bluetooth not started");
                }
            }

            tick = Observable.interval(0, 12000, TimeUnit.MILLISECONDS);
            disposableTick = tick.subscribe((i) ->
            {
                List<SensorResult> result = new ArrayList<>();
                for (SensorResult s : accum.values()) {
                    result.add(s);
                }
                accum.clear();
                ((Subject) resultStream).onNext(result);
            });

            disposableStatus = Observable.combineLatest(
                PlayerEventBus.instance().getPlayerStateStream(),
                PlayerEventBus.instance().getPlayerFractionStream(),
                    Pair::new)
                    .subscribe( (i)->
            {
                if (i.second.equals(Player.FRACTION.DARKEN) && i.first.equals(Player.STATE.ALIVE)) {
                    isDarken = true;
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if(bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON){
                        bluetoothAdapter.setName("CO_R100_"+ (int)Math.floor(Math.random()*(100)));
                        startBle();
                    }
                } else {
                    isDarken = false;
                    stopBle();
                    // restore name
                }
            });
        }
    }

    private void startBle(){
        if (!isBleRunning.get()) {
            isBleRunning.set(true);
            Logger.d("Вы радиоактивны");
            BluetoothLeAdvertiser blea = bla.getBluetoothLeAdvertiser();

            AdvertiseData data = (new AdvertiseData.Builder()).setIncludeDeviceName(true).build();
            AdvertiseSettings settings = new AdvertiseSettings.Builder()
                    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                    .setConnectable(false)
                    .setTimeout(0)
                    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                    .build();

            blea.startAdvertising(settings, data, advertiseCallback);
        }
    }

    private void stopBle(){

        if (isBleRunning.get()) {
            isBleRunning.set(false);
            Logger.d("Вы не радиоактивны");
            bla.getBluetoothLeAdvertiser().stopAdvertising(advertiseCallback);
        }
    }

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
        }

        @Override
        public void onStartFailure(int errorCode) {
            Logger.e("LE Advertise Failed: " + errorCode);
        }
    };

    @Override
    public void stop()
    {
        if (disposableTick != null) {
            disposableTick.dispose();
            disposableTick = null;
        }

        if (disposableStatus != null) {
            disposableStatus.dispose();
            disposableStatus = null;
        }

        isRunning.set(false);
    }

    @Override
    public Observable<List<SensorResult>> getSensorResultsStream()
    {
        return resultStream;
    }

    @Override
    public Observable<Double> getDetectorStream(){
        return detectorStream;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    private class LeScanCallback implements BluetoothAdapter.LeScanCallback
    {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes)
        {
            //TODO:  filter bluetooth Device
            long now = System.currentTimeMillis();
            String key = bluetoothDevice.getAddress().substring(0, 14);
            if(registeredMacs.containsKey(key))
            {
                SensorResult s = new  SensorResult();
                s.id = bluetoothDevice.getAddress();
                s.name =  registeredMacs.get(key);
                s.value = i;
                s.time = now;
                accum.put(s.id, s);
                //Log.d(TAG, "SCANNED!!!!!!" + bluetoothDevice.getAddress() + " " + bluetoothDevice.getName() + " " + i + " " + Arrays.toString(bytes));
            }
            else
            {
                String name = bluetoothDevice.getName();
                if (name == null) {
                    try {
                        final BleAdvertisedData badata = BleUtil.parseAdertisedData(bytes);
                        name = badata.getName();
                    }
                    catch (Exception ex){
                        Log.e(TAG, ex.getMessage());
                    }
                }
                //Log.d(TAG, "FIND " + bluetoothDevice.getAddress() + " " + name + " " + i + " " + Arrays.toString(bytes));
                if (!isDarken && name != null && name.startsWith("CO_")){
                    SensorResult s = new  SensorResult();
                    s.id = bluetoothDevice.getAddress();
                    s.name =  "R30";
                    s.value = i;
                    s.time = now;
                    accum.put(s.id, s);
                    //Log.d(TAG, "FIND DARKER" + bluetoothDevice.getAddress() + " " + bluetoothDevice.getName() + " " + i + " " + Arrays.toString(bytes));
                }
                else {
                    if (artefactMacs.contains(key)){
                        if (level > 5) {
                            if (resetter != null && !resetter.isDisposed()) {
                                resetter.dispose();
                            }
                            ((Subject<Double>) detectorStream).onNext(new Double(i + 100));
                            resetter = Observable.timer(3, TimeUnit.SECONDS).subscribe((x) -> ((Subject<Double>) detectorStream).onNext(0d));
                        }

                        if (bluetoothDevice.getAddress().endsWith("0")) {
                            SensorResult s = new SensorResult();
                            s.id = bluetoothDevice.getAddress();
                            s.name = "R200";
                            s.value = i;
                            s.time = now;
                            accum.put(s.id, s);
                        }
                    }
                }
            }
            if(!isRunning.get())
            {
                bla.stopLeScan(callback);
            }
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