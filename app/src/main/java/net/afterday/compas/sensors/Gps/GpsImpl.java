package net.afterday.compas.sensors.Gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public class GpsImpl implements Gps
{
    private static final String TAG = "GpsImpl";
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Disposable d;
    private Subject<Integer> satelitesCount = BehaviorSubject.createDefault(0);
    public GpsImpl(Context context)
    {
        this.context = context;
        locationListener = new LocationListenerImpl();
    }

    @Override
    public void start()
    {
        if(locationManager == null)
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            d = Observable.timer(0, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread()).subscribe((t) -> locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener));
        }
    }

    @Override
    public void stop()
    {
        if(d != null && !d.isDisposed())
        {
            d.dispose();
        }
        if(locationManager != null)
        {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
        satelitesCount.onNext(0);
    }

    @Override
    public Observable<Integer> getSensorResultsStream()
    {
        return satelitesCount;
    }

    private class LocationListenerImpl implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            //android.util.Log.e(TAG, "onLocationChanged: " + location);
            Bundle b = location.getExtras();
            int satellites = b.getInt("satellites");
            android.util.Log.e(TAG, "onLocationChanged: " + satellites);
            satelitesCount.onNext(satellites);
//            android.util.Log.e(TAG, "Satellites:" + b.getInt("satellites"));
//            android.util.Log.e(TAG, "----------------------------------------");
//            for (String key : b.keySet())
//            {
//                android.util.Log.e(TAG, key + " Extra: " + b.get(key));
//            }
            //location.getExtras().get
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle)
        {
            android.util.Log.e(TAG, "onStatusChanged: " + s + " Bundle: " + bundle);
        }

        @Override
        public void onProviderEnabled(String s)
        {

        }

        @Override
        public void onProviderDisabled(String s)
        {

        }
    }
}
