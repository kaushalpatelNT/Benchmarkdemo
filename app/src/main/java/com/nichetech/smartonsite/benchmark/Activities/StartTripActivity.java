package com.nichetech.smartonsite.benchmark.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nichetech.smartonsite.benchmark.Common.*;
import com.nichetech.smartonsite.benchmark.Data.ContactData;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestEndTrip;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestStartTrip;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCommon;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseStartTrip;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseUploadImage;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseUploadTextFile;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;

public class StartTripActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private Utility utility;
    private Toolbar toolbar;
    private Preferences mPref;
    private LocationManager manager;
    private GPSTracker gpsTracker;
    private ProgressDialog progressDialog;
    private LinearLayout llStartTrip, llEndTrip;
    private TextView tvHeader, tvEndNamelabel, tvEndName, tvEndNoLabel, tvEndNo, tvImageHint;

    private EditText etTripDescription;
    private PackageInfo packageManager;
    private ArrayList<ContactData> contactList = new ArrayList<>();
    private ArrayAdapter<String> contact_adapter1;
    private ArrayAdapter<String> name_adapter1;

    private AutoCompleteTextView edClientName;
    private EditText edDescription, edClientNumber;
    private ImageView ivStartTrip, ivEndTrip, ivHeader, ivShowImage;


    private String strClientName, strContactNo, startTripTime, endTripTime, strDescription, strAddress = "", path;
    private String user_trip_id = "0";
    private String pathfordb = "";
    private Uri fileUri;
    private ArrayList<Bitmap> images = new ArrayList<>();
    private List<String> name = new ArrayList<>();
    public NotificationManager notificationManager;
    public static boolean isGoingOn = false;

    public static List<Double> myarray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_trip);


        utility = new Utility(this);
        mPref = new Preferences(this);
        startService(new Intent(StartTripActivity.this, GPSTracker.class));
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        notificationManager = (NotificationManager) StartTripActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        progressDialog = new ProgressDialog(StartTripActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));


        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(this);
        Typeface raleway_medium = TypefaceUtils.RalewayMedium(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);

        }
        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.start_trip);
        ivHeader = (ImageView) findViewById(R.id.iv_back);

        edClientName = (AutoCompleteTextView) findViewById(R.id.ed_client_name);
        edClientNumber = (EditText) findViewById(R.id.ed_client_number);
        edDescription = (EditText) findViewById(R.id.et_trip_Description);
        edDescription.setOnTouchListener(this);

        gpsTracker = new GPSTracker();
        //new GPSTracker(StartTripActivity.this);*/

        tvEndNamelabel = (TextView) findViewById(R.id.tv_end_name_label);
        tvEndNoLabel = (TextView) findViewById(R.id.tv_end_no_label);
        tvEndName = (TextView) findViewById(R.id.tv_end_name);
        tvEndNo = (TextView) findViewById(R.id.tv_end_no);
        tvImageHint = (TextView) findViewById(R.id.imagehint);

        etTripDescription = (EditText) findViewById(R.id.et_trip_Description);

        tvEndNamelabel.setTypeface(raleway_semibold);
        tvEndNoLabel.setTypeface(raleway_semibold);
        tvEndName.setTypeface(raleway_medium);
        tvEndNo.setTypeface(raleway_medium);
        tvImageHint.setTypeface(raleway_medium);
        etTripDescription.setTypeface(raleway_medium);

        llStartTrip = (LinearLayout) findViewById(R.id.ll_start_details);
        llEndTrip = (LinearLayout) findViewById(R.id.ll_end_details);

        ivStartTrip = (ImageView) findViewById(R.id.iv_start_trip);
        ivEndTrip = (ImageView) findViewById(R.id.iv_end_trip);
        ivShowImage = (ImageView) findViewById(R.id.iv_images);

        if (Utility.isNotNull(mPref.getStrUserTripId())) {


            llStartTrip.setVisibility(View.GONE);
            llEndTrip.setVisibility(View.VISIBLE);
            tvHeader.setText(R.string.end_trip);
            tvEndName.setText(mPref.getStrUserClientName());
            tvEndNo.setText(mPref.getStrUserClientNumber());

            ivStartTrip.setVisibility(View.GONE);
            ivEndTrip.setVisibility(View.VISIBLE);
        }

        ivStartTrip.setOnClickListener(this);
        tvImageHint.setOnClickListener(this);

        ivEndTrip.setOnClickListener(this);
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                ) {
                                                /*strAddress = Utility.getAddress();*/
                                                /*Log.d("Current Latitude", GPSTracker.currentLocation.getLatitude() + "");
                                                Log.d("Current Longtiude", GPSTracker.currentLocation.getLongitude() + "");*/

            readContactData1();

        } else {
            ActivityCompat.requestPermissions(StartTripActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }


        edClientName.setFilters(new InputFilter[]{Utility.filter, new InputFilter.LengthFilter(50)});
        edClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (Utility.isNotNull(s.toString())) {
                        //setOthers();
                    } else {
//                        edClientName.setText("");
                        edClientNumber.setText("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            v.getParent().requestDisallowInterceptTouchEvent(true);

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            v.getParent().requestDisallowInterceptTouchEvent(false);
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(StartTripActivity.this);
    }

    public void submitTripData() {
        final ProgressDialog progressDialog = new ProgressDialog(StartTripActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        Log.d("Location", GPSTracker.Latitude + "");

        // Get System TELEPHONY service reference
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        assert tManager != null;
        String carrierName = tManager.getNetworkOperatorName();
        Field[] fields = Build.VERSION_CODES.class.getFields();
        String osName = fields[Build.VERSION.SDK_INT].getName();
        String version = Build.VERSION.RELEASE;

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        try {
            packageManager = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String DeviceDetail = "Android_" + packageManager.versionName + "_" + osName + "_" + version + "_" + carrierName + "_" + manufacturer + "_" + model;

        Log.e("DeviceDetail==>", "" + DeviceDetail);


        //RequestStartTrip.trip_fromlocation trip_fromloaction = new RequestStartTrip.trip_fromlocation(String.valueOf(GPSTracker.Latitude), String.valueOf(GPSTracker.Longitude));
        String trip_fromloaction = String.valueOf(GPSTracker.Latitude)+","+String.valueOf(GPSTracker.Longitude);
        RequestStartTrip.trip_tolocation trip_toloaction = new RequestStartTrip.trip_tolocation("", "");

        final RequestStartTrip requestStartTrip = new RequestStartTrip(mPref.getStrUserId(), edClientName.getText().toString(), edClientNumber.getText().toString(),strAddress,trip_fromloaction, "Android", DeviceDetail);


        SAASApi saasApi = Utility.saasapi(getApplicationContext());

        Call<ResponseStartTrip> call = saasApi.AddStartTrip(requestStartTrip);

        call.enqueue(new Callback<ResponseStartTrip>() {
            @Override
            public void onResponse(Call<ResponseStartTrip> call, Response<ResponseStartTrip> response) {


                if (response.body().getError_code() == 1) {
                    utility.generateNoteOnSD(StartTripActivity.this, response.body().getData());
                    isGoingOn = true;
                    Intent intent = new Intent(StartTripActivity.this, SplashActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(StartTripActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                            .setContentIntent(contentIntent)
                            .setAutoCancel(false)
                            .setContentTitle("Your Trip is goingOn")
                            .setSmallIcon(R.mipmap.ic_launcher) //add a 24x24 image to the drawable folder
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setOngoing(true);


                    notificationManager.notify(1, builder.build());


                    llStartTrip.setVisibility(View.GONE);
                    llEndTrip.setVisibility(View.VISIBLE);
                    tvHeader.setText(R.string.end_trip);
                    tvEndName.setText(strClientName);
                    tvEndNo.setText(strContactNo);
                    ivStartTrip.setVisibility(View.GONE);
                    ivEndTrip.setVisibility(View.VISIBLE);

                    user_trip_id = response.body().getData();

                    mPref.setStrUserTripId(response.body().getData());
                    mPref.setStrUserClientName(strClientName);
                    mPref.setStrUserClientNumber(strContactNo);

                    Log.d("User Trip Data", mPref.getStrUserTripId());

//                    new GPSTracker(StartTripActivity.this, user_trip_id);
                    startService(new Intent(StartTripActivity.this, SrvPositioning.class));
                  /*  if (ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ) {

                        startService(new Intent(StartTripActivity.this, SrvPositioning.class));
                        } else {
                        ActivityCompat.requestPermissions(StartTripActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }*/
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    //ActivityCompat.requestPermissions(StartTripActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);

                }

            }

            @Override

            public void onFailure(Call<ResponseStartTrip> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(StartTripActivity.this, getString(R.string.msgServerNotConnect));
            }
        });

    }


    public void endTripData() {
        final ProgressDialog progressDialog = new ProgressDialog(StartTripActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();
       // RequestEndTrip.trip_tolocation trip_toloaction = new RequestEndTrip.trip_tolocation(valueOf(GPSTracker.Latitude), valueOf(GPSTracker.Longitude));
        String trip_toloaction = valueOf(GPSTracker.Latitude)+","+valueOf(GPSTracker.Longitude);

        String imageName="";
        if(name.size()>0){
            for (int i=0;i<name.size();i++){
                if(i != name.size()-1){
                    imageName+=name.get(i)+"";
                }else {
                    imageName+=name.get(i);
                }
            }
        }

        RequestEndTrip requestEndTrip = new RequestEndTrip(mPref.getStrUserId(), mPref.getStrUserTripId(), strDescription, strAddress, "", trip_toloaction, imageName);


        SAASApi saasApi = Utility.saasapi(getApplicationContext());

        Call<ResponseCommon> call = saasApi.EndTrip(requestEndTrip);

        call.enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {


                if (response.body().getError_code() == 1) {
                    isGoingOn = false;
                    notificationManager.cancel(1);
                    mPref.setStrUserTripId("");
                    mPref.setFlUserTripDistance(0.0f);
                    stopService(new Intent(StartTripActivity.this, ReceiverPositioningAlarm.class));
                    stopAlarm();
                    stopService(new Intent(StartTripActivity.this, SrvPositioning.class));
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    UploadFile();


                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                }


            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                generateResponse(t.toString());
                Utility.showToast(StartTripActivity.this, getString(R.string.msgServerNotConnect));
            }
        });
    }

    public void UploadFile() {
        final ProgressDialog progressDialog = new ProgressDialog(StartTripActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();
        mPref = new Preferences(this);
        Log.e("Filename", "" + mPref.getFilename());

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "SOS" + File.separator + mPref.getFilename());

        // MultipartBody.Part is used to send also the actual file name
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file_image", mPref.getFilename(), requestBody);

        SAASApi saasApi = Utility.saasapi(getApplicationContext());

        Call<ResponseUploadTextFile> call = saasApi.uploadTripTextFile(body);
        call.enqueue(new Callback<ResponseUploadTextFile>() {
            @Override
            public void onResponse(Call<ResponseUploadTextFile> call, Response<ResponseUploadTextFile> response) {
                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Intent inTripList = new Intent(StartTripActivity.this, TripListActivity.class);
                    startActivity(inTripList);
                    finish();
                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Intent inTripList = new Intent(StartTripActivity.this, TripListActivity.class);
                    startActivity(inTripList);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ResponseUploadTextFile> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Intent inTripList = new Intent(StartTripActivity.this, TripListActivity.class);
                startActivity(inTripList);
                finish();
            }
        });

    }

    public boolean checkValidation() {

        strClientName = edClientName.getText().toString();
        strContactNo = edClientNumber.getText().toString();

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;


        startTripTime = calendar.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        Log.d("StartTime", startTripTime);


        if (!Utility.isNotNull(strClientName)) {
            Utility.showAlert(this, getString(R.string.msgClientNameNull));
            return false;
        } else if (!Utility.isNotNull(strContactNo)) {
            Utility.showAlert(this, getString(R.string.msgClientNoNull));
            return false;
        } else if (strContactNo.length() != 10) {
            Utility.showAlert(this, getString(R.string.msgClientNoNotMatches));
            return false;

        } else {

            return true;
        }
    }

    public boolean checkEndTripValidation() {

        strDescription = edDescription.getText().toString();

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        endTripTime = calendar.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        Log.d("EndTime", endTripTime);


        if (!Utility.isNotNull(strDescription)) {
            Utility.showAlert(this, getString(R.string.msgClientDescription));
            return false;
        } else {

            return true;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGoingOn) {
            Intent intent = new Intent(StartTripActivity.this, SplashActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(StartTripActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(contentIntent)
                    .setAutoCancel(false)
                    .setContentTitle("Your Trip is goingOn")
                    .setSmallIcon(R.mipmap.ic_launcher) //add a 24x24 image to the drawable folder
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setOngoing(true);


            notificationManager.notify(1, builder.build());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isGoingOn) {
            Intent intent = new Intent(StartTripActivity.this, SplashActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(StartTripActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(contentIntent)
                    .setAutoCancel(false)
                    .setContentTitle("Your Trip is goingOn")
                    .setSmallIcon(R.mipmap.ic_launcher) //add a 24x24 image to the drawable folder
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setOngoing(true);


            notificationManager.notify(1, builder.build());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_start_trip:
                Utility.hideKeyboard(StartTripActivity.this);
                if (checkValidation()) {

                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (Utility.isOnline(StartTripActivity.this)) {
                            new AlertDialog.Builder(StartTripActivity.this)
                                    .setTitle("")
                                    .setMessage(getString(R.string.msgStartTrip) + "\n" + getString(R.string.msgStartTripInternet))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            if (ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                                    ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                                    ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                                    ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                                    ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                                    ) {
                                                if (GPSTracker.Latitude != 0.0 && GPSTracker.Longitude != 0.0) {
                                                    if (Utility.isOnline(StartTripActivity.this)) {
                                                        submitTripData();
                                                    } else {
                                                        Utility.showToast(StartTripActivity.this, R.string.msgNoInternet);
                                                    }

                                                } else {
                                                    startService(new Intent(StartTripActivity.this, GPSTracker.class));
                                                    Utility.showAlert(StartTripActivity.this, getString(R.string.msgNotGetGps));
                                                }


                                            } else {
                                                ActivityCompat.requestPermissions(StartTripActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                                            }

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            Utility.showAlert(StartTripActivity.this, getString(R.string.msgNoInternet));
                        }
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(StartTripActivity.this);
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
                }
                break;

            case R.id.imagehint:
                try {
                    if (ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(StartTripActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            ) {

                        if (images.size() < 1) {
                            OpenImagePicker();
                        } else {
                            Utility.showToast(StartTripActivity.this, getString(R.string.msgMaxTripImageupload));
                        }

                    } else {
                        ActivityCompat.requestPermissions(StartTripActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.iv_end_trip:
                Utility.hideKeyboard(StartTripActivity.this);

                if (checkEndTripValidation()) {


                    if (images.size() > 0) {
                        new uploadTripImage().execute();
                    } else {
                        if (GPSTracker.Latitude != null && GPSTracker.Longitude != null) {
                            if (Utility.isOnline(StartTripActivity.this)) {
                                endTripData();
                            } else {
                                Utility.showToast(StartTripActivity.this, R.string.msgNoInternet);
                            }

                        } else {
                            startService(new Intent(StartTripActivity.this, GPSTracker.class));
                            Utility.showAlert(StartTripActivity.this, getString(R.string.msgNotGetGps));
                        }
                    }

                }
                break;
        }

    }

    public void OpenImagePicker() {
        if (Utility.isOnline(StartTripActivity.this)) {
            String file_name = "saas" + ".jpg";

            ContentValues value = new ContentValues();
            value.put(MediaStore.Images.Media.TITLE, file_name);

            Uri imageurl = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);

            path = valueOf(imageurl) + ", " + path;

            fileUri = Utility.getOutputMediaFileUri("SAAS");
            assert fileUri != null;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Image Source");
            builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //GET IMAGE FROM GALLERY.
                                    openGallery();
                                    break;

                                case 1:
                                    //GET IMAGE FROM CAMERA.
                                    openCamera();
                                    break;

                                default:
                                    break;
                            }
                        }
                    });

            builder.show();

        } else {
//                    Utility.showToast(ComplaintFillupActivity.this,R.string.msgNoInternet);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class uploadTripImage extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            String[] listoff = pathfordb.split(",");

            for (int i = 0; i <= listoff.length; i++) {
                if (i < listoff.length) {


                    File file = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + listoff[i]);

                    // MultipartBody.Part is used to send also the actual file name
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file_image", listoff[i], requestBody);


                    try {
                        SAASApi ftsInterface = Utility.saasapi(getApplicationContext());
                        Call<ResponseUploadImage> call = ftsInterface.uploadTripImage(body);

                        Response<ResponseUploadImage> responseBody = call.execute();
                        if (responseBody.isSuccessful()) {
                            name.add(responseBody.body().getData());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            strAddress = Utility.getAddress();
            if (GPSTracker.Latitude != 0.0 && GPSTracker.Longitude != 0.0) {
                if (Utility.isOnline(StartTripActivity.this)) {
                    endTripData();
                } else {
                    Utility.showToast(StartTripActivity.this, R.string.msgNoInternet);
                }
            } else {
                startService(new Intent(StartTripActivity.this, GPSTracker.class));
                Utility.showAlert(StartTripActivity.this, getString(R.string.msgNotGetGps));
            }

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
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED
                    && grantResults[5] == PackageManager.PERMISSION_GRANTED) {

                //OpenImagePicker();
                readContactData1();

            } else {
                Utility.showAlert(StartTripActivity.this, getString(R.string.msgPermission));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        super.onStop();
    }

    //OPEN CAMERA
    private void openCamera() {
        String fileName = "conect" + DateTimeUtils.getCurrentDateTimeMix() + ".jpg";
        // create parameters for Intent with filename
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by Camera on CONECT");
        fileUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, Constant.REQUEST_FOR_CAMERA);
    }

    //OPEN GALLERY
    public void openGallery() {

        Intent gintent = new Intent();

        gintent.setType("image/*");
        gintent.putExtra("outputX", 120);
        gintent.putExtra("outputY", 120);
        gintent.putExtra("aspectX", 1);
        gintent.putExtra("aspectY", 1);
        gintent.putExtra("scale", true);

        gintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gintent, Constant.REQUEST_FOR_GALLERY);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constant.REQUEST_FOR_CAMERA:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(fileUri);
                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "SAAS" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".jpeg";

                        File sourceFile = new File(this.getFilesDir().getAbsolutePath(), "SAAA_IMG.jpeg");
                        if (!sourceFile.exists())
                            sourceFile.createNewFile();
                        File f = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + filename);

                        try {
                            FileUtils.copyFile(sourceFile, f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!f.exists())
                            f.createNewFile();

                        // write the bytes in file
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);

                        String filePath = f.getPath();

                        String img_name = filePath.substring(filePath.lastIndexOf("/") + 1);


                        images.add(bitmap);
                        ivShowImage.setVisibility(View.VISIBLE);

                        if (Utility.isNotNull(pathfordb))
                            pathfordb = pathfordb + "," + img_name;
                        else
                            pathfordb = img_name;
                        tvImageHint.setVisibility(View.GONE);
                        Glide.with(this)
                                .load(filePath)
                                .apply(RequestOptions.centerCropTransform())
                                .into(ivShowImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    tvImageHint.setVisibility(View.VISIBLE);
                }
                break;

            case Constant.REQUEST_FOR_GALLERY:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "SAAS" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".jpeg";

                        File sourceFile = new File(this.getFilesDir().getAbsolutePath(), "SAAA_IMG.jpeg");
                        if (!sourceFile.exists())
                            sourceFile.createNewFile();
                        File f = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + filename);

                        try {
                            FileUtils.copyFile(sourceFile, f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!f.exists())
                            f.createNewFile();

                        // write the bytes in file
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);

                        String filePath = f.getPath();

                        String img_name = filePath.substring(filePath.lastIndexOf("/") + 1);

                        images.add(bitmap);
                        ivShowImage.setVisibility(View.VISIBLE);

                        if (Utility.isNotNull(pathfordb))
                            pathfordb = pathfordb + "," + img_name;
                        else
                            pathfordb = img_name;
                        tvImageHint.setVisibility(View.GONE);
                        Glide.with(this)
                                .load(filePath)
                                .apply(RequestOptions.centerCropTransform())
                                .into(ivShowImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    tvImageHint.setVisibility(View.VISIBLE);
                }
                break;

        }
    }


    public void stopAlarm() {

        Intent intent = new Intent(StartTripActivity.this, ReceiverPositioningAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }

    }

    public void generateResponse(String sBody) {
        try {


            File root = new File(Environment.getExternalStorageDirectory(), "SOS");
            if (!root.exists()) {
                root.mkdir();
            }
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

            String file_name = "Response_EndTrip_" + dateFormat.format(date) + ".txt";
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

    //READ CONTACT FASTER
    private void readContactData1() {

        ArrayList<String> phoneValueArr = new ArrayList<>();
        ArrayList<String> nameValueArr = new ArrayList<>();

        String[] projection = {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE,
                ContactsContract.PhoneLookup._ID
        };

        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";

        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
        };

        Uri uri = ContactsContract.Data.CONTENT_URI;

        Cursor cursor = StartTripActivity.this.getContentResolver().query(uri, projection, selection, selectionArgs, null);


        final int mimeTypeIdx = cursor != null ? cursor.getColumnIndex(ContactsContract.Data.MIMETYPE) : 0;
        final int idIdx = cursor != null ? cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID) : 0;
        final int nameIdx = cursor != null ? cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) : 0;
        final int dataIdx = cursor != null ? cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA) : 0;

        assert cursor != null;
        while (cursor.moveToNext()) {

            ContactData inwardContact = new ContactData();

            String id;
            String phoneNumber = "";
            String displayName;

            String data = cursor.getString(dataIdx);


            String mimeType = cursor.getString(mimeTypeIdx);

            id = cursor.getString(idIdx);

            if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
            } else {
                phoneValueArr.add(data);
                phoneNumber = data;
            }
            displayName = cursor.getString(nameIdx);
            nameValueArr.add(displayName);

            inwardContact.setId(id);
            inwardContact.setName(displayName);
            inwardContact.setPhone(phoneNumber);
            contactList.add(inwardContact);
        }

        name_adapter1 = new ArrayAdapter<>(StartTripActivity.this, R.layout.custom_textview, nameValueArr);
        contact_adapter1 = new ArrayAdapter<>(StartTripActivity.this, R.layout.custom_textview, phoneValueArr);

        edClientName.setAdapter(name_adapter1);
//        edClientNumber.setAdapter(contact_adapter1);
        cursor.close();
    }

    //SET NUMBER
   /* private void setOthers() {

        for (int i = 0; i < contactList.size(); i++) {

            if (Utility.isNotNull(edClientName.getText().toString()) &&
                    contactList.get(i).getName().equalsIgnoreCase(edClientName.getText().toString())) {

                if (Utility.isNotNull(contactList.get(i).getPhone())) {
                    boolean isValidNo = true;
                    String number = contactList.get(i).getPhone();
                    PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
                    try {
                        long number333 = numberUtil.parse(number, "IN").getNationalNumber();
                        number = "" + number333;
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                        isValidNo = false;
                    }
                    if (isValidNo) {
                        edClientNumber.setFilters(new InputFilter[]{Utility.filter, new InputFilter.LengthFilter(10)});
                        edClientNumber.setText(number);
                    } else {
                        edClientNumber.setText("");
                        Utility.showAlert(StartTripActivity.this, "Invalid contact No. Please select valid contact No.");
                    }

                } else {
                    edClientNumber.setText("");
                }
            }
        }
    }*/

}
