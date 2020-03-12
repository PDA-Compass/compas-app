package net.afterday.compas.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import net.afterday.compas.R;
import net.afterday.compas.engine.events.CodeInputEventBus;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.util.OnSwipeTouchListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Justas Spakauskas on 3/24/2018.
 */

public class ScannerFragment extends DialogFragment
{
    private View v;
    private DecoratedBarcodeView dbw;
    private static final String TAG = "ScannerFragment";
    private int currentCam;
    private boolean flashOn;
    public static ScannerFragment newInstance() {
        ScannerFragment f = new ScannerFragment();
        //Log.d(TAG, "ScannerFragment: newInstance");
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Observable.timer(300, TimeUnit.SECONDS).take(1).subscribe((t) -> closePopup(v));
        v = inflater.inflate(R.layout.scanner_fragment, container, false);
        v.setOnTouchListener(new OnSwipeTouchListener(this.getActivity()){
            @Override
            public void onSwipeLeft()
            {
                changeCamera();
            }

            @Override
            public void onSwipeRight()
            {
                changeCamera();
            }

            @Override
            public void onSwipeUp()
            {
                toggleFlashLight();
            }

            @Override
            public void onSwipeDown()
            {
                toggleFlashLight();
            }
        });
        // Assign close button
        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    closePopup(v);
                }catch (Exception e)
                {}

            }
        });
        scanQr();
        //scanQr(savedInstanceState);


        //Log.d("SCANNER", "onCreateView");

        return v;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        v.setOnTouchListener(null);
    }

    public void closePopup(View view) {
        dismiss();
    }

    private void toggleFlashLight()
    {
        if(flashOn)
        {
            dbw.setTorchOff();
            flashOn = false;
        }else
        {
            dbw.setTorchOn();
            flashOn = true;
        }
//        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
//        {
//            if(cam == null)
//            {
//                cam = Camera.open();
//                Camera.Parameters p = cam.getParameters();
//                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                cam.setParameters(p);
//                cam.startPreview();
//            }else
//            {
//                cam.stopPreview();
//                cam.release();
//                cam = null;
//            }
//        }
    }

    private void changeCamera()
    {
        if(currentCam == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {

            currentCam = Camera.CameraInfo.CAMERA_FACING_BACK;
        }else
        {
            currentCam = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        dbw.pause();
        dbw.getBarcodeView().setCameraSettings(getCameraSettings(currentCam));
        dbw.resume();
    }

    private void scanQr()
    {
        dbw = (DecoratedBarcodeView) v.findViewById(R.id.zxing_barcode_scanner);
        dbw.setStatusText(null);
        currentCam = Camera.CameraInfo.CAMERA_FACING_FRONT;
        dbw.getBarcodeView().setCameraSettings(getCameraSettings(currentCam));
        dbw.getBarcodeView().decodeSingle(new BarcodeCallback()
        {
            @Override
            public void barcodeResult(BarcodeResult result)
            {
                Log.d(TAG, "ItemScanned: " + result.getText());
                CodeInputEventBus.codeScanned(result.getText());
                //ItemEventsBus.instance().addItem(result.getText());
                //net.afterday.compas.logging.Logger.d("Barcode scanned: " + result.getText());
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

    private CameraSettings getCameraSettings(int cameraType)
    {
        CameraSettings cs = new CameraSettings();
        int cameraId = 0;
        int defaultId = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++)
        {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                defaultId = camIdx;
            }
            if (cameraInfo.facing == cameraType)
            {
                cameraId = camIdx;
                break;
            }
        }
        cs.setRequestedCameraId(cameraId > 0 ? cameraId : defaultId);
        return cs;
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
