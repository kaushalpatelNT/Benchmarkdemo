package com.nichetech.smartonsite.benchmark.Common;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.nichetech.smartonsite.benchmark.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by Kaushal on 14-04-2017.
 */

public class GPSTracker_old extends Service implements LocationListener {

    private String TAG="GPS Tracker";
    private final Context mContext;
    private Preferences mPref;

    private String user_trip_id = "0";

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    public double latitude, currentLatitude = 0, lastLatitude = 0; // latitude
    public double longitude, currentLongitude = 0, lastLongitude = 0; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker_old(Context context, String user_trip_id) {
        this.mContext = context;
        mPref = new Preferences(mContext);
        this.user_trip_id = user_trip_id;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                     /*if (isNetworkEnabled) {

                         locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }*/

                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
                else {
                   Utility.showAlert(mContext,getString(R.string.msg_GPS_Not_Getting));
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

        lastLatitude = currentLatitude;
        lastLongitude = currentLongitude;

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        /*Log.d("Location Changed", location.getProvider());
        Log.d("Location Latitude", location.getLatitude() + "");
        Log.d("Location Longitude", location.getLongitude() + "");*/
//        Log.d("Location Longitude", getLatitude()+"");
//        Log.d("Location Longitude", getLongitude()+"");


//        Toast.makeText(mContext,"Location Changes-->"+location.getLatitude()+"\n"+location.getLongitude(),Toast.LENGTH_LONG).show();

        String strAddress = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            /*Log.d("ADDress", addresses.toString());
            Log.d("Address", addresses.get(0).getAddressLine(0));
            Log.d("City", addresses.get(0).getLocality());
            Log.d("State", addresses.get(0).getSubLocality());
            Log.d("Country", addresses.get(0).getCountryName());*/

//            strAddress = addresses.get(0).getSubLocality() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName();

//            Toast.makeText(mContext, strAddress, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

       // Log.d("PRADIP::Distance",lastLatitude+","+lastLongitude+","+currentLatitude+","+currentLongitude);

        double Distance = distance(lastLatitude, lastLongitude, currentLatitude, currentLongitude) * 0.000621371;


//        Toast.makeText(mContext, "Distance--->" + Distance, Toast.LENGTH_LONG).show();


       /* Log.d("PRADIP::GET::DISTANCE", Distance + "");
        Log.d("PRADIP::PREF::DISTANCE", mPref.getFlUserTripDistance() + "");*/

        float totalDistance = (float) (mPref.getFlUserTripDistance() + Distance);


//        mPref.setFlUserTripDistance(totalDistance);

//        Log.e("PRADIP::TOTAL::DISTANCE", "" + totalDistance);

        if (!mPref.getStrUserTripId().equalsIgnoreCase("0")) {

//            upDateTripData();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
