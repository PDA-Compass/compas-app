package net.afterday.compas.app.sensors.Watch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.sensors.SensorResult;
import net.afterday.compas.engine.sensors.Watch.Watch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class WatchImpl implements Watch {
    private Subject<SensorResult> resultStream = PublishSubject.create();
    private BluetoothAdapter bluetoothAdapter;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private WatchBluetoothService.ConnectedThread watchBluetoothService;


    public WatchImpl(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        else
        {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    if (deviceName.equals("Compass watch")){
                        try {
                            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                            watchBluetoothService = new WatchBluetoothService.ConnectedThread(socket, resultStream);
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        if(bluetoothAdapter != null) {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            watchBluetoothService.start();
            //socket.connect();


            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
        }

    }

    @Override
    public void stop() {

    }

    @Override
    public Observable<SensorResult> getSensorResultsStream() {
        return resultStream;
    }
}
