package net.afterday.compas.app.sensors.Watch;

import android.bluetooth.BluetoothSocket;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.sensors.SensorResult;

import java.io.IOException;
import java.io.InputStream;

public class WatchBluetoothService extends Thread {

    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private byte[] mmBuffer;
        private Subject<SensorResult> outStream;

        private String acum = "";

        ConnectedThread(BluetoothSocket socket, Subject<SensorResult> stream) {
            mmSocket = socket;
            InputStream tmpIn = null;
            //OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                //Log.e(TAG, "Error occurred when creating input stream", e);
            }
            /*
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }*/

            mmInStream = tmpIn;
            outStream = stream;
        }

        private void parse(String value) {
            if (acum.length() == 0){
                acum = value;
            }
            else
            {
                acum += value;
            }

            String[] parts = acum.split("\\[");
            char last = acum.charAt(acum.length() - 1);
            int count = 0;
            if (last == '[') {
                acum = "";
                count = parts.length;
            }
            else
            {
                if (parts.length == 0 || parts.length == 1) {
                    return;
                }
                acum = parts[parts.length - 1];
                count = parts.length - 1;
            }

            long now = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                String line = parts[i];
                String[] lines = line.split("\\|");
                if (lines.length == 3) {
                    outStream.onNext(
                            new SensorResult("w"+lines[0], lines[1], Integer.parseInt(lines[2]), now)
                    );
                }
            }
        }

        @Override
        public void run() {
            try {
                mmSocket.connect();
            } catch (IOException e) {
                return;
                //e.printStackTrace();
            }
            mmBuffer = new byte[256];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            try {
                while (true) {
                    try {
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        if (numBytes > 0) {
                            String text = new String(mmBuffer, 0, numBytes);
                            parse(text);
                        }
                        // Send the obtained bytes to the UI activity.
                        //h.obtainMessage(RECEIVE_MESSAGE, numBytes, -1, mmBuffer).sendToTarget();
                        //Log.i("asdf", "printing");

                    } catch (IOException e) {
                        //Log.d(TAG, "Input stream was disconnected", e);
                        break;
                    } catch (NullPointerException e) {
                        //Log.i("asdf", "Null pointer exception");
                        break;
                    }
                }
            } finally {
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
