package com.nichetech.smartonsite.benchmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;



public class SplashActivity extends AppCompatActivity {

    private Preferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);


        try {

            mPref = new Preferences(SplashActivity.this);

            FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();

            new CountDownTimer(5000, 1000) {

                @Override
                public void onFinish() {

                    if (Utility.isNotNull(mPref.getStrUserId())) {

                        if (Utility.isOnline(SplashActivity.this)){

                            if(Utility.isNotNull(mPref.getStrUserTripId())){
                                Intent intDashboard = new Intent(SplashActivity.this, StartTripActivity.class);
                                startActivity(intDashboard);
                                finish();
                            }else {
                                Intent intDashboard = new Intent(SplashActivity.this, DashboardActivity.class);
                                startActivity(intDashboard);
                                finish();
                            }


                        }else {
                            Utility.showAlert(SplashActivity.this, getString(R.string.msgNoInternet), new Utility.OnOkClickListener() {
                                @Override
                                public void onOkClick() {
                                    finish();
                                }
                            });
                        }


                    } else {
                        if (Utility.isOnline(SplashActivity.this)){
                            Intent it_main = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(it_main);
                        }else {
                            Utility.showAlert(SplashActivity.this, getString(R.string.msgNoInternet), new Utility.OnOkClickListener() {
                                @Override
                                public void onOkClick() {
                                    finish();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onTick(long l) {

                }

            }.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
