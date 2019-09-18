package com.nichetech.smartonsite.benchmark.Common;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.nichetech.smartonsite.benchmark.Activities.StartTripActivity;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestUpdateTrip;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCommon;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.nichetech.smartonsite.benchmark.Common.Utility.adddatainfile;
import static com.nichetech.smartonsite.benchmark.Common.Utility.mContext;

/**
 * Created by android-2 on 4/22/17.
 */

public class ReceiverPositioningAlarm extends BroadcastReceiver {

    public static final String COMMAND = "SENDER";
    public static final int SENDER_ACT_DOCUMENT = 0;
    public static final int SENDER_SRV_POSITIONING = 1;
    public static final String ACTION_REFRESH_SCHEDULE_ALARM =
            "org.mabna.order.ACTION_REFRESH_SCHEDULE_ALARM";
    public Location currentLocation;
    private static Location prevLocation;
    private static Context _context;
    private String provider = LocationManager.NETWORK_PROVIDER;
    private static Intent _intent;
    private static LocationManager locationManager;

    private boolean isGPSEnabled;

    public double StartLatitude, StartLongitude, EndLatitude, EndLongitude;
    Preferences pref;


    @Override
    public void onReceive(final Context context, Intent intent) {
        _context = context;
        _intent = intent;
        _context.startService(new Intent(_context, GPSTracker.class));
        pref = new Preferences(context);

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        try {
            if (Utility.isOnline(_context)) {
                if (isGPSEnabled) {
                    gotLocation(GPSTracker.currentLocation);
                } else {
                    Intent it = new Intent();
                    PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder b = new NotificationCompat.Builder(_context);

                    //.......... Notification alert on GPS ..........//
                    b.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setTicker("SAAS - GPS not working")
                            .setContentTitle("SAAS - GPS not working")
                            .setContentText("Your GPS seems to be disabled. Please enable.")
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setContentIntent(contentIntent);

                    NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(2, b.build());
                    Utility.showToast(_context, "Your GPS seems to be disabled. Please enable.");
                }
            } else {
                String s = String.valueOf(GPSTracker.currentLocation.getLatitude()) + " , " + String.valueOf(GPSTracker.currentLocation.getLongitude());
                adddatainfile(s + "-----{No Internet}", _context);
                Intent it = new Intent();
                PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder b = new NotificationCompat.Builder(_context);

                //.......... Notification alert on INTERNET ..........//
                b.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("SAAS - Internet not working")
                        .setContentTitle("SAAS - Internet not working")
                        .setContentText("Your Internet seems to be disabled. Please enable.")
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent);


                NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(3, b.build());
                Utility.showToast(_context, "Your Internet seems to be disabled. Please enable.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void gotLocation(Location location) throws IOException {

        try {
            prevLocation = currentLocation == null ? null : new Location(
                    currentLocation);
            currentLocation = location;


            if (isLocationNew()) {
                prevLocation = currentLocation;
                location.setLatitude(GPSTracker.currentLocation.getLatitude());
                location.setLongitude(GPSTracker.currentLocation.getLongitude());
                if (SrvPositioning.isFirsttime == 0) {
                    SrvPositioning.isFirsttime = 1;
                    setlattitudeandlongotude();
                    setENDlattitudeandlongotude();
                    double v = distFrom((float) StartLatitude, (float) StartLongitude, (float) EndLatitude, (float) EndLongitude);
                    if (v > 200.00 && v < 3000) {
                        if (GPSTracker.Latitude != 0.0 && GPSTracker.Longitude != 0.0) {
                            StartTripActivity.myarray.add(v);
                            upDateTripData();
                        }
                    }
                } else {

                    if (validation()) {
                        getPrefdata();
                        setENDlattitudeandlongotude();
                        double v = distFrom((float) StartLatitude, (float) StartLongitude, (float) EndLatitude, (float) EndLongitude);
                        if (v > 200.00 && v < 3000) {
                            if (GPSTracker.Latitude != 0.0 && GPSTracker.Longitude != 0.0) {
                                StartTripActivity.myarray.add(v);
                                upDateTripData();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLocationNew() {
        if (currentLocation == null) {
            return false;
        } else if (prevLocation == null) {
            return true;
        } else return currentLocation.getTime() != prevLocation.getTime();
    }


    // listener ----------------------------------------------------
    static ArrayList<OnNewLocationListener> arrOnNewLocationListener =
            new ArrayList<OnNewLocationListener>();

    public void setOnNewLocationListener(OnNewLocationListener listener) {
        arrOnNewLocationListener.add(listener);
    }


    public void upDateTripData() {
        Preferences mPref = new Preferences(_context);

        RequestUpdateTrip requestUpdateTrip = new RequestUpdateTrip(mPref.getStrUserTripId(), mPref.getStrUserId(), String.valueOf(GPSTracker.currentLocation.getLatitude()), String.valueOf(GPSTracker.currentLocation.getLongitude()), "");

        SAASApi saasApi = Utility.saasapi(mContext);

        Call<ResponseCommon> call = saasApi.UpdateTrip(requestUpdateTrip);

        call.enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                // Log.d("Response", response.body().getError_code() + "");
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                generateResponse(t.toString());
                String s = String.valueOf(GPSTracker.currentLocation.getLatitude()) + " , " + String.valueOf(GPSTracker.currentLocation.getLongitude());
                adddatainfile(s + "-----{Response_Fail}", _context);
            }
        });

    }

    public void setlattitudeandlongotude() {

        StartLatitude = GPSTracker.Latitude;
        StartLongitude = GPSTracker.Longitude;

        Log.e("gpsTragetLatitude==>", "" + GPSTracker.Latitude);
        Log.e("gpsTragetLongitude==>", "" + GPSTracker.Longitude);


    }

    public void setPreflattitudeandlongitude() {
        pref.setLattitude(String.valueOf(GPSTracker.Latitude));
        pref.setLongitude(String.valueOf(GPSTracker.Longitude));
    }

    public void getPrefdata() {

        StartLatitude = Double.parseDouble(pref.Lattitude());
        StartLongitude = Double.parseDouble(pref.Longitude());

        Log.e("StartLatitude==>", "" + StartLatitude);
        Log.e("StartLongitude==>", "" + StartLongitude);

    }


    public void setENDlattitudeandlongotude() {

        EndLatitude = GPSTracker.Latitude;
        EndLongitude = GPSTracker.Longitude;


    }


    public boolean validation() {

        if (pref.Lattitude().equalsIgnoreCase(String.valueOf(currentLocation.getLatitude())) && pref.Longitude().equalsIgnoreCase(String.valueOf(currentLocation.getLongitude()))) {
            Log.e("prefLattitude==>", "" + pref.Lattitude());
            Log.e("cu_Lattitude", "" + currentLocation.getLatitude());
            Log.e("prefLongitude==>", "" + pref.Longitude());
            Log.e("cu_Longitude", "" + currentLocation.getLongitude());
            return false;
        } else {
            Log.e("prefLattitude1==>", "" + pref.Lattitude());
            Log.e("cu_Lattitude1", "" + currentLocation.getLatitude());
            Log.e("prefLongitude1==>", "" + pref.Longitude());
            Log.e("cu_Longitude1", "" + currentLocation.getLongitude());
            return true;
        }


    }


    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters   // for KM 3958.75
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        float sum = 0;
        setPreflattitudeandlongitude();


        return dist;
    }

    public void generateResponse(String sBody) {
        try {


            File root = new File(Environment.getExternalStorageDirectory(), "SOS");
            if (!root.exists()) {
                root.mkdir();
            }
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

            String file_name = "Response_Add_point_" + dateFormat.format(date) + ".txt";
            File gpxfile = new File(root, file_name);
            if (!gpxfile.exists()) {
                gpxfile.createNewFile();
            }

            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line + "--------------------\n");
                //text.append('\n'); // commented for one line only
            }

            FileWriter writer = new FileWriter(gpxfile);
            writer.append(text + sBody);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}