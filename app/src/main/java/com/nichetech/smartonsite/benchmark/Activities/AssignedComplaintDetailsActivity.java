package com.nichetech.smartonsite.benchmark.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.nichetech.smartonsite.benchmark.Common.*;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintDetails;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintReject;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintStatus;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseComplaintStatus;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponsecomplaintDetails;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.*;

public class AssignedComplaintDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "Accept";
    private Utility utility;
    private Preferences mPref;

    private Toolbar toolbar;
    private ImageView ivBack;

    private TextView lblCustomerCode, lblCustomerName, lblCustomerAddress, lblCustomerPhone, lblProductName, lblComplaintType, lblPriority, lblComplaintDetails,
            lblCustomerContact, lblComplaintDate;

    private TextView tvCustomerCode, tvProductName, tvCustomerName, tvCustomerAddress, tvCustomerPhone, tvComplaintType, tvPriority, tvCustomerRemark, tvComplaintDate;

    private Button btnAccepted, btnRejected;
    private AlertDialog.Builder alertDialogBuilder;

    private String strComplaintNo, strCusName, strCusAddress, strCusPhone, strProductName, strComplaintType, strPriority,
            strCustomerRemark, strCusContactNo, strCusComplaintDate, strReason = "", strStatus = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_complaint_details);

        utility = new Utility(this);
        mPref = new Preferences(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);

        }

        if (getIntent().getStringExtra(Constant.ACTIVITY_NAME).equalsIgnoreCase(Constant.ActivityTitle.ASSIGNED_LIST)) {

            strComplaintNo = getIntent().getStringExtra(Constant.Complaint.ComplainId);
            strCusName = getIntent().getStringExtra(Constant.Complaint.CustomerName);
            strCusAddress = getIntent().getStringExtra(Constant.Complaint.CustomerAddress);
            strCusPhone = getIntent().getStringExtra(Constant.Complaint.CustomerPhone);
            strProductName = getIntent().getStringExtra(Constant.Complaint.ProductName);
            strComplaintType = getIntent().getStringExtra(Constant.Complaint.ComplaintType);
            strPriority = getIntent().getStringExtra(Constant.Complaint.Priority);
            strCustomerRemark = getIntent().getStringExtra(Constant.Complaint.CustomerRemark);
            strCusContactNo = getIntent().getStringExtra(Constant.Complaint.CustomerAlternatePhone);
            strCusComplaintDate = getIntent().getStringExtra(Constant.Complaint.ComplaintDate);

        } else {
            strComplaintNo = getIntent().getStringExtra(Constant.Complaint.ComplainId);
            GetComplaintDetails(strComplaintNo);
        }


        lblCustomerCode = (TextView) findViewById(R.id.customerCode);
        lblCustomerName = (TextView) findViewById(R.id.customerName);
        lblCustomerAddress = (TextView) findViewById(R.id.customerAddress);
        lblCustomerPhone = (TextView) findViewById(R.id.customerPhone);
        lblProductName = (TextView) findViewById(R.id.productName);
        lblComplaintType = (TextView) findViewById(R.id.complaintType);
        lblPriority = (TextView) findViewById(R.id.complaintPriority);
        lblComplaintDetails = (TextView) findViewById(R.id.customerRemark);
        lblComplaintDate = (TextView) findViewById(R.id.complaintDate);


        tvCustomerCode = (TextView) findViewById(R.id.tv_customerCode);
        tvCustomerName = (TextView) findViewById(R.id.tv_customerName);
        tvCustomerAddress = (TextView) findViewById(R.id.tv_customerAddress);
        tvCustomerPhone = (TextView) findViewById(R.id.tv_customerPhone);
        tvProductName = (TextView) findViewById(R.id.tv_productName);
        tvComplaintType = (TextView) findViewById(R.id.tv_complaintType);
        tvPriority = (TextView) findViewById(R.id.tv_complaintPriority);
        tvCustomerRemark = (TextView) findViewById(R.id.tv_customerRemark);
        tvComplaintDate = (TextView) findViewById(R.id.tv_complaintDate);

        ivBack = (ImageView) findViewById(R.id.ivBack);

        String date = DateTimeUtils.changeDateTimeFormat(strCusComplaintDate,
                DateTimeUtils.SERVER_FORMAT_DATE_TIME);

        if (Utility.isNotNull(strComplaintNo)) {
            tvCustomerCode.setText(strComplaintNo);
        } else {
            tvCustomerCode.setText("-");
        }
        if (Utility.isNotNull(strCusName)) {
            tvCustomerName.setText(strCusName);
        } else {
            tvCustomerName.setText("-");
        }
        if (Utility.isNotNull(strCusAddress)) {
            tvCustomerAddress.setText(strCusAddress);
        } else {
            tvCustomerAddress.setText("-");
        }
        if (Utility.isNotNull(strCusPhone)) {
            tvCustomerPhone.setText(strCusPhone);
        } else {
            tvCustomerPhone.setText("-");
        }
        if (Utility.isNotNull(strProductName)) {
            tvProductName.setText(strProductName);
        } else {
            tvProductName.setText("-");
        }
        if (Utility.isNotNull(strComplaintType)) {
            tvComplaintType.setText(strComplaintType);
        } else {
            tvComplaintType.setText("-");
        }
        if (Utility.isNotNull(strPriority)) {
            tvPriority.setText(strPriority);
        } else {
            tvPriority.setText("-");
        }
        if (Utility.isNotNull(strCustomerRemark)) {
            tvCustomerRemark.setText(strCustomerRemark);
        } else {
            tvCustomerRemark.setText("-");
        }
        if (Utility.isNotNull(date)) {
            tvComplaintDate.setText(date);
        } else {
            tvComplaintDate.setText("-");
        }

        btnAccepted = (Button) findViewById(R.id.btn_accept);
        btnRejected = (Button) findViewById(R.id.btn_reject);

        lblCustomerCode.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblCustomerName.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblCustomerAddress.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblCustomerPhone.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblComplaintType.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblPriority.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblComplaintDetails.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));
        lblComplaintDate.setTypeface(TypefaceUtils.RalewayMedium(AssignedComplaintDetailsActivity.this));


        tvCustomerCode.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvCustomerName.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvCustomerAddress.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvCustomerPhone.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvComplaintType.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvPriority.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvCustomerRemark.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));
        tvComplaintDate.setTypeface(TypefaceUtils.RalewaySemiBold(AssignedComplaintDetailsActivity.this));

        btnAccepted.setOnClickListener(this);
        btnRejected.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ivBack:

                onBackPressed();

                break;

            case R.id.btn_accept:

                strStatus = "Pending";
                if (Utility.isOnline(AssignedComplaintDetailsActivity.this)) {
                    ChangeComplaintStatus(strStatus, strReason);
                } else {
                    Utility.showToast(AssignedComplaintDetailsActivity.this, R.string.msgNoInternet);
                }
                break;

            case R.id.btn_reject:

                if (Utility.isOnline(AssignedComplaintDetailsActivity.this)) {
                    LayoutInflater minflater = LayoutInflater.from(getApplicationContext());
                    View promptsView = minflater.inflate(R.layout.row_reject_request, null);


                    AlertDialog.Builder sayWindows = new AlertDialog.Builder(
                            AssignedComplaintDetailsActivity.this);
                    sayWindows.setMessage("Reason");
                    sayWindows.setPositiveButton("ok", null);
                    sayWindows.setNegativeButton("cancel", null);
                    sayWindows.setView(promptsView);

                    final EditText userInput = promptsView
                            .findViewById(R.id.reason_text);

                    final AlertDialog mAlertDialog = sayWindows.create();
                    mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                        @Override
                        public void onShow(final DialogInterface dialog) {

                            Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            b.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    // TODO Do something
                                    if (Utility.isNotNull(userInput.getText().toString().trim())) {
                                        strStatus = "Rejected";
                                        strReason = userInput.getText().toString().trim();
                                        dialog.dismiss();
                                        if (Utility.isOnline(AssignedComplaintDetailsActivity.this)) {
                                            ComplaintReject(strStatus, strReason);
                                        } else {
                                            Utility.showToast(AssignedComplaintDetailsActivity.this, R.string.msgNoInternet);
                                        }
                                    } else {
                                        Utility.showToast(AssignedComplaintDetailsActivity.this, getString(R.string.msgInvalidReason));
                                    }


                                }
                            });
                        }
                    });
                    mAlertDialog.show();
                } else {

                    Utility.showToast(AssignedComplaintDetailsActivity.this, getString(R.string.msgNoInternet));

                }


                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void ChangeComplaintStatus(String strStatus, String strReason) {


        final ProgressDialog progressDialog = new ProgressDialog(AssignedComplaintDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());


        Call<ResponsecomplaintDetails> call = saasapi.AcceptComplaint(new RequestComplaintDetails(strComplaintNo));
        call.enqueue(new Callback<ResponsecomplaintDetails>() {
            @Override
            public void onResponse(Call<ResponsecomplaintDetails> call, Response<ResponsecomplaintDetails> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssignedComplaintDetailsActivity.this)
                                .setMessage(response.body().getError_message())
                                .setPositiveButton(getString(R.string.strOk), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(AssignedComplaintDetailsActivity.this, DashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });

                        builder.show();


                    } else {
                        Utility.showAlert(AssignedComplaintDetailsActivity.this, response.body().getError_message());
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(AssignedComplaintDetailsActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponsecomplaintDetails> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(AssignedComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });
    }

    public void ComplaintReject(String strStatus, String strReason) {


        final ProgressDialog progressDialog = new ProgressDialog(AssignedComplaintDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());


        Call<ResponsecomplaintDetails> call = saasapi.RejectComplaint(new RequestComplaintReject(strComplaintNo,strReason));
        call.enqueue(new Callback<ResponsecomplaintDetails>() {
            @Override
            public void onResponse(Call<ResponsecomplaintDetails> call, Response<ResponsecomplaintDetails> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssignedComplaintDetailsActivity.this)
                                .setMessage(response.body().getError_message())
                                .setPositiveButton(getString(R.string.strOk), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(AssignedComplaintDetailsActivity.this, DashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });

                        builder.show();


                    } else {
                        Utility.showAlert(AssignedComplaintDetailsActivity.this, response.body().getError_message());
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(AssignedComplaintDetailsActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponsecomplaintDetails> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(AssignedComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });
    }

    private void GetComplaintDetails(String complaintId) {

        final ProgressDialog progressDialog = new ProgressDialog(AssignedComplaintDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();


        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        String strUserId = mPref.getStrUserId();
        String strUserOrgId = mPref.getStrUserOrgId();

        Call<ResponsecomplaintDetails> call = saasapi.GetComplaintDetails(new RequestComplaintDetails(complaintId));
        call.enqueue(new Callback<ResponsecomplaintDetails>() {
            @Override
            public void onResponse(Call<ResponsecomplaintDetails> call, Response<ResponsecomplaintDetails> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {


                        if (Utility.isNotNull(response.body().getData().getComplainId())) {
                            tvCustomerCode.setText(response.body().getData().getComplainId());
                        } else {
                            tvCustomerCode.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getCustomerName())) {
                            tvCustomerName.setText(response.body().getData().getCustomerName());
                        } else {
                            tvCustomerName.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getCustomerAddress())) {
                            tvCustomerAddress.setText(response.body().getData().getCustomerAddress());
                        } else {
                            tvCustomerAddress.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getCustomerPhone())) {
                            tvCustomerPhone.setText(response.body().getData().getCustomerPhone());
                        } else {
                            tvCustomerPhone.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getProductName())) {
                            tvProductName.setText(response.body().getData().getProductName());
                        } else {
                            tvProductName.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getComplaintType())) {
                            tvComplaintType.setText(response.body().getData().getComplaintType());
                        } else {
                            tvComplaintType.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getPriority())) {
                            tvPriority.setText(response.body().getData().getPriority());
                        } else {
                            tvPriority.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getCustomerRemark())) {
                            tvCustomerRemark.setText(response.body().getData().getCustomerRemark());
                        } else {
                            tvCustomerRemark.setText("-");
                        }
                        if (Utility.isNotNull(response.body().getData().getComplaintDate())) {
                            tvComplaintDate.setText(Utility.convertDate(response.body().getData().getComplaintDate()));
                        } else {
                            tvComplaintDate.setText("-");
                        }


                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    } else {
                        Utility.showAlert(AssignedComplaintDetailsActivity.this, response.body().error_message);
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(AssignedComplaintDetailsActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponsecomplaintDetails> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(AssignedComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });

    }
}
