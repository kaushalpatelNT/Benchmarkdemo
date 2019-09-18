package com.nichetech.smartonsite.benchmark.Common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestUpdateTrip;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCommon;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kaushal on 18-04-2017.
 */

public class TrackingService extends Service {

    private Context mContext;
//    private GPSTracker gpsTracker;
    private Preferences mPref;
    private String user_trip_id,user_id;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        gpsTracker=new GPSTracker(getApplicationContext());

        user_trip_id=intent.getStringExtra("user_trip_id");
        user_id=intent.getStringExtra("user_id");

        Log.d("User_trip_id",user_trip_id);
        Log.d("Service start","Service Startin...");

        new CountDownTimer(1000,1000){
            @Override
            public void onFinish() {
//                upDateTripData();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("Service start","Service destroy.");
        super.onDestroy();
    }

    public void upDateTripData() {

        RequestUpdateTrip requestUpdateTrip = new RequestUpdateTrip(user_trip_id,user_id,String.valueOf(GPSTracker.currentLocation.getLatitude()),String.valueOf(GPSTracker.currentLocation.getLongitude()),"Nehrunagar,Ahmedabad");


        SAASApi saasApi = Utility.saasapi(getApplicationContext());

        Call<ResponseCommon> call = saasApi.UpdateTrip(requestUpdateTrip);

        call.enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {

                Log.d("Response",response.body().getError_code()+"");

            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {

            }
        });

    }
}
