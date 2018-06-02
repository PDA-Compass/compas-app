package net.afterday.compass.fragment;

import android.app.DialogFragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import net.afterday.compass.R;
import net.afterday.compass.engine.events.ItemEventsBus;

import java.util.List;

/**
 * Created by Justas Spakauskas on 3/24/2018.
 */

public class ScannerFragment extends DialogFragment
{
    private View v;
    private DecoratedBarcodeView dbw;
    private static final String TAG = "ScannerFragment";

    public static ScannerFragment newInstance() {
        ScannerFragment f = new ScannerFragment();
        //Log.d(TAG, "ScannerFragment: newInstance");
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.scanner_fragment, container, false);

        // Assign close button
        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup(v);
            }
        });
        scanQr();
        //scanQr(savedInstanceState);


        //Log.d("SCANNER", "onCreateView");

        return v;
    }

    public void closePopup(View view) {
        dismiss();
    }

    private void scanQr()
    {
//        //Log.d("SCANNER", "scanQr");
//        IntentIntegrator intent = IntentIntegrator.forFragment(this);
//
//        intent.setPrompt("Scan item QR code");
//        intent.setOrientationLocked(true);
//
//        int cameraId = 0;
//        int cameraCount = 0;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        cameraCount = Camera.getNumberOfCameras();
//        for (int camIdx = 0; camIdx < cameraCount; camIdx++)
//        {
//            Camera.getCameraInfo(camIdx, cameraInfo);
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) //Use back camera
//            {
//                cameraId = camIdx;
//                break;
//            }
//        }
//        intent.setCameraId(cameraId);
//        intent.initiateScan();
        CameraSettings cs = new CameraSettings();
        int cameraId = 0;
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++)
        {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) //TODO: Padaryti kameru perjungima
            {
                cameraId = camIdx;
                break;
            }
        }
        cs.setRequestedCameraId(cameraId);
        dbw = (DecoratedBarcodeView) v.findViewById(R.id.zxing_barcode_scanner);
        dbw.setStatusText(null);
        dbw.getBarcodeView().setCameraSettings(cs);
        dbw.getBarcodeView().decodeSingle(new BarcodeCallback()
        {
            @Override
            public void barcodeResult(BarcodeResult result)
            {
                Log.d(TAG, "ItemScanned: " + result.getText());
                ItemEventsBus.instance().addItem(result.getText());
                //net.afterday.compass.logging.Log.d("Barcode scanned: " + result.getText());
                dbw.pause();
                getActivity().onBackPressed();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints)
            {
                //Log.e(TAG, "Possible result points: " + resultPoints);
            }
        });
//        CaptureManager capture = new CaptureManager(getActivity(), mDecoratedBarcodeView);
//        capture.initializeFromIntent(getActivity().getIntent(), savedInstanceState);
//        capture.decode();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        dbw.getBarcodeView().resume();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        dbw.getBarcodeView().pause();
    }
}
