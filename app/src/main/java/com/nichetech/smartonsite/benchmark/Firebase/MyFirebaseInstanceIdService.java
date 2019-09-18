package com.nichetech.smartonsite.benchmark.Firebase;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by kaushal on 9/12/16.
 */

public class MyFirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase_Key", "Refreshed token: " + refreshedToken);
        MyFirebaseMessagingService.setSessionPushKey(getApplicationContext(),refreshedToken);
        sendRegistrationToServer(refreshedToken);

    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        MyFirebaseMessagingService.setSessionPushKey(getApplicationContext(),token);
    }
}
