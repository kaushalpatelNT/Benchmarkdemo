/*
 * Copyright (c) 2017. Suthar Rohit
 * Developed by Suthar Rohit for NicheTech Computer Solutions Pvt. Ltd. use only.
 * <a href="http://RohitSuthar.com/">Suthar Rohit</a>
 *
 * @author Suthar Rohit
 */

package com.nichetech.smartonsite.benchmark.Common;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.nichetech.smartonsite.benchmark.Common.Utility.adddatainfile;


public class GPSTracker extends Service implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static Double Latitude = 0d, Longitude = 0d;
    public float Distance = 0f;
    String accu;

    private String TAG = GPSTracker.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static Location currentLocation;
    public Context mContext;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 60;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final long MEDIUM_INTERVAL = 1000 * 30 * 60;

    @Override
    public void onCreate() {
        super.onCreate();
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            startLocationUpdates();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.v(TAG, "Location update started ..............: ");
        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && LocationServices.FusedLocationApi != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.v(TAG, "Location update stopped .......................");
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();

        String s = String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude());
        if (currentLocation == null) currentLocation = location;
        //isbetter
        if (location.hasAccuracy()) {
            accu = "{" + location.getAccuracy() + "}";
            if (currentLocation != null) Distance = currentLocation.distanceTo(location);
            if (location.getAccuracy() <= 50) {
                currentLocation = location;
            } else if (Distance > 2000 && (Distance - location.getAccuracy()) > 0) {
                currentLocation = location;
            }
            /*if (isBetterLocation(currentLocation, location)) {
                currentLocation = location;
            }*/
        } else {
            currentLocation = location;
        }

        Preferences mPref = new Preferences(GPSTracker.this);

        if (Utility.isNotNull(mPref.getStrUserTripId())) {
            adddatainfile(s + " " + accu + "-----{" + Distance + "}", GPSTracker.this);
//            Toast.makeText(GPSTracker.this,"Location --> "+currentLocation.getLatitude()+","+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
        }

        Log.e("Latitude == " + Latitude, "Longitude == " + Longitude);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        Log.v(TAG, "Service Stopped!");
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }


        // Check whether the new location fix is more or less accurate
        double accuracyDelta = location.getAccuracy() - currentBestLocation.getAccuracy();
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;


        Log.e("accuracyDelta==>", "" + accuracyDelta);
        Log.e("isLessAccurate==>", "" + isLessAccurate);
        Log.e("isMoreAccurate==>", "" + isMoreAccurate);
        Log.e("isSignificantlyLess==>", "" + isSignificantlyLessAccurate);

        return !isSignificantlyLessAccurate;
    }
}
