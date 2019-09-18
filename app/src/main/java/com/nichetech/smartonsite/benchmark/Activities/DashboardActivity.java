package com.nichetech.smartonsite.benchmark.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.Firebase.MyFirebaseMessagingService;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintCount;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestLogout;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseComplaintCount;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseLogout;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Preferences mPref;
    private Utility mUtility;

    private Toolbar toolbar;

    private TextView tvTitle, tvTaskAssign, tvTaskPending, tvTaskComplete, tvTaskReject, tvTaskLogout, tvTaskChangePassword,
            tvCountAssign, tvCountPending, tvCountComplete, tvCountReject, tvCountLogout,
            tvCompanyName, tvCompId, tvCompDetail, tvVersion;
    private ImageView iv_navigation;
    private LinearLayout llStartrTrip, llTripList, llAssigned, llPending, llCompleted, llRejected, llChangePassword, llLogout;
    private String strAssigned, strPending, strRejected, strCompleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mPref = new Preferences(this);
        mUtility=new Utility(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);

        }

        tvTitle = (TextView) findViewById(R.id.toolbar_title);

        tvTitle.setText(getString(R.string.dashboardTitle));

        tvVersion = (TextView) findViewById(R.id.tv_version);
        try {
            PackageInfo packageManager = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText("version " + packageManager.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Utility.stopPerformanceTrace();

        llStartrTrip = (LinearLayout) findViewById(R.id.ll_start_trip);
        llTripList = (LinearLayout) findViewById(R.id.ll_trip_list);
        llAssigned = (LinearLayout) findViewById(R.id.ll_assigned);
        llPending = (LinearLayout) findViewById(R.id.ll_pending);
        llCompleted = (LinearLayout) findViewById(R.id.ll_completed);
        llRejected = (LinearLayout) findViewById(R.id.ll_rejected);
        llChangePassword = (LinearLayout) findViewById(R.id.ll_changepassword);
        llLogout = (LinearLayout) findViewById(R.id.ll_logout);

        tvTaskAssign = (TextView) findViewById(R.id.tv_task_assign);
        tvTaskPending = (TextView) findViewById(R.id.tv_task_pending);
        tvTaskComplete = (TextView) findViewById(R.id.tv_task_completed);
        tvTaskReject = (TextView) findViewById(R.id.tv_task_rejected);
        tvTaskLogout = (TextView) findViewById(R.id.tv_task_logout);
        tvTaskChangePassword = (TextView) findViewById(R.id.tv_changepassword);

        tvCountAssign = (TextView) findViewById(R.id.tv_count_assign);
        tvCountPending = (TextView) findViewById(R.id.tv_count_pending);
        tvCountComplete = (TextView) findViewById(R.id.tv_count_completed);
        tvCountReject = (TextView) findViewById(R.id.tv_count_rejected);
        tvCountLogout = (TextView) findViewById(R.id.tv_count_logout);

        /*iv_navigation = (ImageView) findViewById(R.id.iv_navigation_view);
        tvCompanyName = (TextView) findViewById(R.id.tv_navigation_companyName);
        tvCompId = (TextView) findViewById(R.id.tv_navigation_companyId);
        tvCompDetail = (TextView) findViewById(R.id.tv_navigation_companyDetail);
*/
        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(DashboardActivity.this);
        Typeface raleway_medium = TypefaceUtils.RalewayMedium(DashboardActivity.this);
        Typeface myriadpro = TypefaceUtils.MyriadProRegular(DashboardActivity.this);

        tvTaskAssign.setTypeface(raleway_medium);
        tvTaskPending.setTypeface(raleway_medium);
        tvTaskComplete.setTypeface(raleway_medium);
        tvTaskReject.setTypeface(raleway_medium);
        tvTaskLogout.setTypeface(raleway_medium);
        tvTaskChangePassword.setTypeface(raleway_medium);
        tvCountAssign.setTypeface(myriadpro);
        tvCountPending.setTypeface(myriadpro);
        tvCountComplete.setTypeface(myriadpro);
        tvCountReject.setTypeface(myriadpro);
        tvCountLogout.setTypeface(myriadpro);


        tvCountLogout.setText(mPref.getStrUserName());

        llStartrTrip.setOnClickListener(this);
        llTripList.setOnClickListener(this);
        llAssigned.setOnClickListener(this);
        llCompleted.setOnClickListener(this);
        llPending.setOnClickListener(this);
        llRejected.setOnClickListener(this);
        llChangePassword.setOnClickListener(this);
        llLogout.setOnClickListener(this);


        if (Utility.isNotNull(MyFirebaseMessagingService.getSessionPushKey(DashboardActivity.this))) {

            Log.d("Firebase Key", MyFirebaseMessagingService.getSessionPushKey(DashboardActivity.this));
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_start_trip:
                Intent it_start_trip = new Intent(DashboardActivity.this, StartTripActivity.class);
                startActivity(it_start_trip);
                break;

            case R.id.ll_trip_list:
                Intent it_trip_list = new Intent(DashboardActivity.this, TripListActivity.class);
                startActivity(it_trip_list);
                break;

            case R.id.ll_assigned:
                Intent it_assign = new Intent(DashboardActivity.this, AssignedComplaintActivity.class);
                startActivity(it_assign);
                break;

            case R.id.ll_pending:
                Intent it_pending = new Intent(DashboardActivity.this, PendingComplaintsActivity.class);
                startActivity(it_pending);
//                Utility.showAlert(DashboardActivity.this, "Coming Soon...");
                break;

            case R.id.ll_completed:

                Intent it_completed = new Intent(DashboardActivity.this, CompletedComplaintsActivity.class);
                startActivity(it_completed);
//                Utility.showAlert(DashboardActivity.this, "Coming Soon...");
                break;

            case R.id.ll_rejected:
                Intent it_rejected = new Intent(DashboardActivity.this, RejectedComplaintActivity.class);
                startActivity(it_rejected);
//                Utility.showAlert(DashboardActivity.this, "Coming Soon...");
                break;

            case R.id.ll_changepassword:
                Intent it_changepass = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
                startActivity(it_changepass);
                break;
            case R.id.ll_logout:

                mPref.logout();
                Intent inLogin = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(inLogin);
                finish();

               /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.msgLogout));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage(getString(R.string.msgPleasewait));
                        progressDialog.show();

                        RequestLogout requestLogout = new RequestLogout();
                        requestLogout.user_id = mPref.getStrUserId();

                        SAASApi saasapi = Utility.saasapi(getApplicationContext());


                        Call<ResponseLogout> call = saasapi.getLogout(requestLogout);
                        call.enqueue(new Callback<ResponseLogout>() {
                            @Override
                            public void onResponse(Call<ResponseLogout> call, Response<ResponseLogout> response) {

                                if (response.isSuccessful()) {
                                    if (progressDialog.isShowing()) progressDialog.dismiss();
                                    if (response.body().getError_code() == 1) {
                                        Utility.showToast(DashboardActivity.this, response.body().getError_message());
                                        mPref.logout();
                                        Intent inLogin = new Intent(DashboardActivity.this, LoginActivity.class);
                                        startActivity(inLogin);
                                        finish();
                                    } else {
                                        Utility.showAlert(DashboardActivity.this, response.body().getError_message());
                                    }

                                } else {
                                    if (progressDialog.isShowing()) progressDialog.dismiss();
                                    Utility.showAlert(DashboardActivity.this, response.body().getError_message());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseLogout> call, Throwable t) {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                t.printStackTrace();

                            }
                        });

//                        mPref.logout();


                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();*/


                break;

        }

    }

    public void getComplaintCount() {


        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        String company_id = mPref.getStrUserOrgId();
        String user_id = mPref.getStrUserId();


        Call<ResponseComplaintCount> call = saasapi.GetComplaintCount();
        call.enqueue(new Callback<ResponseComplaintCount>() {
            @Override
            public void onResponse(Call<ResponseComplaintCount> call, Response<ResponseComplaintCount> response) {


                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    if (response.body().getError_code() == 1) {

                        tvCountAssign.setText(String.valueOf(response.body().getData().getAssigned()));
                        tvCountPending.setText(String.valueOf(response.body().getData().getAccepted()));
                        tvCountComplete.setText(String.valueOf(response.body().getData().getResolved()));
                        tvCountReject.setText(String.valueOf(response.body().getData().getRejected()));

                    } else {
                        tvCountAssign.setText("0");
                        tvCountPending.setText("0");
                        tvCountComplete.setText("0");
                        tvCountReject.setText("0");
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    tvCountAssign.setText("0");
                    tvCountPending.setText("0");
                    tvCountComplete.setText("0");
                    tvCountReject.setText("0");
                }
            }

            @Override
            public void onFailure(Call<ResponseComplaintCount> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();

            }
        });


    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("")
                .setCancelable(false)
                .setMessage(getString(R.string.msgStartTripleave))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    @Override
    protected void onResume() {
        super.onResume();

        getComplaintCount();
    }
}

