package com.nichetech.smartonsite.benchmark.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nichetech.smartonsite.benchmark.Common.GPSTracker;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.Firebase.MyFirebaseMessagingService;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestLogin;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseLogin;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "LoginActivity";

    private Utility utility;
    private Preferences mPref;
    protected GPSTracker gpsTracker;

    private EditText etCompanyId, etUserId, etPassword;
    private Button btnLogin;
    private String strCompanyId, strUserId, strPassword, strUserName;
    private TextView tvForgotPassword;
    private boolean gpsEnable = false;
    private LocationManager manager;

    private FirebaseAnalytics analytics;
    private String fcmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        utility = new Utility(this);
        startService(new Intent(LoginActivity.this, GPSTracker.class));
        mPref = new Preferences(this);

        etCompanyId = (EditText) findViewById(R.id.etCompanyId);
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        etCompanyId.setTypeface(TypefaceUtils.MyriadProRegular(this));
        etUserId.setTypeface(TypefaceUtils.MyriadProRegular(this));
        tvForgotPassword.setTypeface(TypefaceUtils.MyriadProRegular(this));
        btnLogin.setTypeface(TypefaceUtils.MyriadProRegular(this));

        tvForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        analytics = FirebaseAnalytics.getInstance(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();
                fcmId=token;
                // Log and toast
              //  Toast.makeText(LoginActivity.this, fcmId, Toast.LENGTH_SHORT).show();
            }
        });


        if (Utility.isNotNull(MyFirebaseMessagingService.getSessionPushKey(LoginActivity.this))) {

            Log.d("Firebase Key", MyFirebaseMessagingService.getSessionPushKey(LoginActivity.this));
            fcmId = MyFirebaseMessagingService.getSessionPushKey(this);
        }


        requestPermissions();

    }

    private void requestFocus(View view) {

        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                Utility.hideKeyboard(LoginActivity.this);
                if (Utility.isOnline(LoginActivity.this)) {

                    if (checkValidation()) {


                        if (Utility.isNotNull(MyFirebaseMessagingService.getSessionPushKey(LoginActivity.this))) {

                            Log.d("Firebase Key", MyFirebaseMessagingService.getSessionPushKey(LoginActivity.this));
                            fcmId = MyFirebaseMessagingService.getSessionPushKey(this);
                        }
                        //Utility.startPerformanceTrace("Login Event");
                        checkLogin();

                    }

                } else {
                    Utility.showAlert(LoginActivity.this, getString(R.string.msgNoInternet));
                }
                break;

            case R.id.tvForgotPassword:
                Utility.hideKeyboard(LoginActivity.this);
                if (Utility.isOnline(LoginActivity.this)) {

                    Intent intForgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intForgotPassword);

                } else {
                    Utility.showAlert(LoginActivity.this, getString(R.string.msgNoInternet));
                }


                break;
        }

    }

    private boolean checkValidation() {

      //  strCompanyId = etCompanyId.getText().toString().toUpperCase().trim();
        strUserId = etUserId.getText().toString().toUpperCase().trim();
        strPassword = etPassword.getText().toString().trim();

   /*     if (!Utility.isNotNull(strCompanyId)) {
            Utility.showToast(LoginActivity.this, getString(R.string.msgCompanyIdNull));
            requestFocus(etCompanyId);
            return false;
        } else if (Utility.validateCompanyId(strCompanyId)) {
            Utility.showToast(LoginActivity.this, getString(R.string.msgCompanyIdInvalid));
            requestFocus(etCompanyId);
            return false;
        } else */
   if (!Utility.isNotNull(strUserId)) {
            Utility.showToast(LoginActivity.this, getString(R.string.msgUserIdNull));
            requestFocus(etUserId);
            return false;
        }/* else if (Utility.validateUserId(strUserId)) {
            Utility.showToast(LoginActivity.this, getString(R.string.msgUserIdInvalid));
            requestFocus(etUserId);
            return false;
        }*/ else if (!Utility.isNotNull(strPassword)) {
            Utility.showToast(LoginActivity.this, getString(R.string.msgPasswordNull));
            requestFocus(etPassword);
            return false;
        }/* else if (!(strPassword.length() >= 5 && strPassword.length() <= 15)) {
            Utility.showToast(LoginActivity.this, getString(R.string.msgPasswordInvalid));
            requestFocus(etPassword);
            return false;
        }*/

        return true;
    }

    private void checkLogin() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();


        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseLogin> call = saasapi.checkLogin(new RequestLogin(strUserId, strPassword, "Android",Utility.getDeviceId(this) ,fcmId));
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if (progressDialog.isShowing()) progressDialog.dismiss();
                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {


                        mPref.setStrUserName(response.body().Data.Username);
                        mPref.setStrUserId(response.body().Data.UserId);
                        mPref.setStrToken(response.body().Data.Token);

                        Intent it_Dash = new Intent(LoginActivity.this, DashboardActivity.class);
                        it_Dash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it_Dash);
                        finish();

                    } else {
                        Utility.showAlert(LoginActivity.this, response.body().error_message);
                    }

                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(LoginActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(LoginActivity.this, getString(R.string.msgServerNotConnect));

            }
        });

    }

    public void requestPermissions() {
        try {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Utility.showAlert(LoginActivity.this, getString(R.string.msgGPS));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            finish();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        super.onResume();
    }
}
