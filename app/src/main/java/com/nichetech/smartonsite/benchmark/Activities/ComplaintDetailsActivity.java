package com.nichetech.smartonsite.benchmark.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Adapter.AssignedAdapter;
import com.nichetech.smartonsite.benchmark.Adapter.ComplaintDetailAdapter;
import com.nichetech.smartonsite.benchmark.Adapter.DealerListAdapter;
import com.nichetech.smartonsite.benchmark.Adapter.ProductListAdapter;
import com.nichetech.smartonsite.benchmark.Common.*;
import com.nichetech.smartonsite.benchmark.Data.Dealer;
import com.nichetech.smartonsite.benchmark.Data.Product;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.*;
import com.nichetech.smartonsite.benchmark.ResponseClass.*;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ComplaintDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ComplaintDetails";

    private TextView tvComplaintNo, tvCustomerName, tvComplaintType, tvPriority, tvCustomerRemark, tvProductName, tvBillNo, tvDealerName, tvComplaintDate, tvHeader, tvCustomerAddress, tvCustomerPhone, tvCompanyRemark;
    private RecyclerView rlComplaintDetail;
    private Preferences mPref;
    private ResponseCustomFormDetail.Data complaintDatas;
    private List<ResponseCustomFormDetail.Action> actionDetails = new ArrayList<>();
    private ComplaintDetailAdapter complaintDetailAdapter;
    private String strCompanyId;
    private String strComplaintCode;
    private String strComplaintId;
    private String strStatus, strOrgFormId;
    private Button btnProceed;
    private ImageView ivBack, ivBillNo, ivProductName, ivDealerName;
    private LinearLayout llLabel;
    private ArrayList<Dealer> dealerList = new ArrayList<>();
    private ArrayList<Product> productList = new ArrayList<>();


    private String strComplaintNo, strCusName, strCusAddress, strCusPhone, strProductName, strBillNo, strDealerName, strComplaintType, strPriority, strCustomerRemark, strCompanyRemark, strCusContactNo, strCusComplaintDate;

    private String updateDealerName,updateBillNo="",updateProductName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        mPref = new Preferences(this);


        if (getIntent().hasExtra(Constant.ACTIVITY_NAME)) {
            strStatus = getIntent().getStringExtra(Constant.ACTIVITY_NAME);
            strComplaintNo = getIntent().getStringExtra(Constant.Complaint.ComplainId);
            strCusName = getIntent().getStringExtra(Constant.Complaint.CustomerName);
            strCusAddress = getIntent().getStringExtra(Constant.Complaint.CustomerAddress);
            strCusPhone = getIntent().getStringExtra(Constant.Complaint.CustomerPhone);
            strProductName = getIntent().getStringExtra(Constant.Complaint.ProductName);
            strBillNo = getIntent().getStringExtra(Constant.Complaint.BillNo);
            strDealerName = getIntent().getStringExtra(Constant.Complaint.DealerName);
            strComplaintType = getIntent().getStringExtra(Constant.Complaint.ComplaintType);
            strPriority = getIntent().getStringExtra(Constant.Complaint.Priority);
            strCustomerRemark = getIntent().getStringExtra(Constant.Complaint.CustomerRemark);
            strCompanyRemark = getIntent().getStringExtra(Constant.Complaint.CompanyRemark);
            strCusContactNo = getIntent().getStringExtra(Constant.Complaint.CustomerAlternatePhone);
            strCusComplaintDate = getIntent().getStringExtra(Constant.Complaint.ComplaintDate);

        }

        tvComplaintNo = (TextView) findViewById(R.id.tv_complaintNo);
        tvCustomerName = (TextView) findViewById(R.id.tv_customerName);
        tvCustomerAddress = (TextView) findViewById(R.id.tv_customerAddress);
        tvCustomerPhone = (TextView) findViewById(R.id.tv_customerPhone);
        tvComplaintType = (TextView) findViewById(R.id.tv_complaintType);
        tvPriority = (TextView) findViewById(R.id.tv_complaintPriority);
        tvCustomerRemark = (TextView) findViewById(R.id.tv_customerRemark);
        tvCompanyRemark = (TextView) findViewById(R.id.tv_company_remark);
        tvProductName = (TextView) findViewById(R.id.tv_productName);
        tvBillNo = (TextView) findViewById(R.id.tv_bill_no);
        tvDealerName = (TextView) findViewById(R.id.tv_delear_name);
        tvComplaintDate = (TextView) findViewById(R.id.tv_date);
        llLabel = (LinearLayout) findViewById(R.id.ll_label);
        btnProceed = (Button) findViewById(R.id.btn_proceed);


        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.Complaint_detail_title);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this);
        tvHeader.setTypeface(raleway_semibold);

        ivBillNo = findViewById(R.id.ivBillNo);
        ivProductName = findViewById(R.id.ivProductName);
        ivDealerName = findViewById(R.id.ivDealerName);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ivBillNo.setOnClickListener(this);
        ivProductName.setOnClickListener(this);
        ivDealerName.setOnClickListener(this);

        rlComplaintDetail = (RecyclerView) findViewById(R.id.rl_action);
        rlComplaintDetail.setHasFixedSize(true);
        rlComplaintDetail.setLayoutManager(new GridLayoutManager(this, 1));
        rlComplaintDetail.addItemDecoration(new DividerItemDecoration(this));
        rlComplaintDetail.setNestedScrollingEnabled(false);


        if (strStatus.matches("Pending")) {
            llLabel.setVisibility(View.VISIBLE);
            rlComplaintDetail.setVisibility(View.VISIBLE);
            btnProceed.setVisibility(View.VISIBLE);
        } else if (strStatus.matches("Rejected")) {
            llLabel.setVisibility(View.GONE);
            rlComplaintDetail.setVisibility(View.GONE);
            btnProceed.setVisibility(View.GONE);
        } else {
            llLabel.setVisibility(View.VISIBLE);
            rlComplaintDetail.setVisibility(View.VISIBLE);
            btnProceed.setVisibility(View.GONE);
        }

        String date = DateTimeUtils.changeDateTimeFormat(strCusComplaintDate,
                DateTimeUtils.SERVER_FORMAT_DATE_TIME);

        if (Utility.isNotNull(strComplaintNo)) {
            tvComplaintNo.setText(strComplaintNo);
        } else {
            tvComplaintNo.setText("-");
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
        if (Utility.isNotNull(strBillNo)) {
            tvBillNo.setText(strBillNo);
        } else {
            tvBillNo.setText("-");
        }
        if (Utility.isNotNull(strDealerName)) {
            tvDealerName.setText(strDealerName);
        } else {
            tvDealerName.setText("-");
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
        if (Utility.isNotNull(strCompanyRemark)) {
            tvCompanyRemark.setText(strCompanyRemark);
        } else {
            tvCompanyRemark.setText("-");
        }
        if (Utility.isNotNull(date)) {
            tvComplaintDate.setText(date);
        } else {
            tvComplaintDate.setText("-");
        }

        //--------- TYPEFACE  ------//
        ((TextView) findViewById(R.id.tv_label_complaint_no)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_customerName)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_customerAddress)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_customerPhone)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_complaintType)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_complaintPriority)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_complaintDescription)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_date)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));
        ((TextView) findViewById(R.id.tv_label_action_list)).setTypeface(TypefaceUtils.RalewayMedium(ComplaintDetailsActivity.this));

        tvComplaintNo.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvCustomerName.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvCustomerAddress.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvCustomerPhone.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvComplaintType.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvPriority.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvCustomerRemark.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));
        tvComplaintDate.setTypeface(TypefaceUtils.RalewaySemiBold(ComplaintDetailsActivity.this));

        RequestCustomFormDetail requestComplaintList = new RequestCustomFormDetail();
        requestComplaintList.company_id = strCompanyId;
        requestComplaintList.complaint_code = strComplaintCode;
        requestComplaintList.complaint_id = strComplaintId;
        requestComplaintList.user_id = mPref.getStrUserId();
        requestComplaintList.form_id = getIntent().getStringExtra("org_form_id");

        if (Utility.isOnline(this)) {
            getDealerName();
            getProductList();
        } else {
            Utility.showAlert(getString(R.string.msgNoInternet), new Utility.OnOkClickListener() {
                @Override
                public void onOkClick() {
                    onBackPressed();
                }
            });
        }

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itFillup = new Intent(ComplaintDetailsActivity.this, ComplaintFillupActivity.class);
                itFillup.putExtra("complaint_id", strComplaintNo);
                startActivity(itFillup);
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBillNo:
                showBillNoDialog();
                break;
            case R.id.ivProductName:
                showProductNameDialog();
                break;
            case R.id.ivDealerName:
                showDealerNameDialog();
                break;
        }

    }

    private void showBillNoDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_billno, viewGroup, false);

        final EditText edBillNo = dialogView
                .findViewById(R.id.ed_bill_no);

        final Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btnCancel = dialogView.findViewById(R.id.btn_cancel);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBillNo=edBillNo.getText().toString().trim();
                if(Utility.isNotNull(updateBillNo)){
                    alertDialog.dismiss();
                    updateComplaint();
                }else {
                    Utility.showToast(ComplaintDetailsActivity.this,"Please enter bill no.");
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBillNo="";
                alertDialog.dismiss();
            }
        });
    }

    private void showProductNameDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_productname, viewGroup, false);

        final AutoCompleteTextView edProductName = dialogView
                .findViewById(R.id.atProductName);

        edProductName.setThreshold(1);

        edProductName.setAdapter(new ProductListAdapter(this, R.layout.item_spinner, productList));

        final Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        edProductName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateProductName=productList.get(position).ProductId;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isNotNull(updateProductName)){
                    alertDialog.dismiss();
                    updateComplaint();
                }else {
                    Utility.showToast(ComplaintDetailsActivity.this,"Please Select Product Name");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductName="";
                alertDialog.dismiss();
            }
        });
    }


    private void showDealerNameDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_dealername, viewGroup, false);

        final AutoCompleteTextView edDealerName = dialogView
                .findViewById(R.id.atDealerName);
        edDealerName.setThreshold(1);
         DealerListAdapter adapter = new DealerListAdapter(this, R.layout.item_spinner, dealerList);
        edDealerName.setAdapter(adapter);


        edDealerName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateDealerName=dealerList.get(position).DealerId;
            }
        });

        final Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btnCancel = dialogView.findViewById(R.id.btn_cancel);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isNotNull(updateDealerName)){
                    alertDialog.dismiss();
                    updateComplaint();
                }else {
                    updateProductName="";
                    Utility.showToast(ComplaintDetailsActivity.this,"Please Select Dealer Name");
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDealerName="";
                alertDialog.dismiss();
            }
        });
    }


    public void getDealerName() {


        final ProgressDialog progressDialog = new ProgressDialog(ComplaintDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseDealer> call = saasapi.getDealerList();
        call.enqueue(new Callback<ResponseDealer>() {
            @Override
            public void onResponse(Call<ResponseDealer> call, Response<ResponseDealer> response) {

                Log.d(TAG, response.body() + "");
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {
                        dealerList = response.body().Data;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDealer> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(ComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });


    }

    public void getProductList() {


        final ProgressDialog progressDialog = new ProgressDialog(ComplaintDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseProduct> call = saasapi.getProductList(new RequestGetProduct(strComplaintNo));
        call.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {

                Log.d(TAG, response.body() + "");
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {
                        productList = response.body().Data;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(ComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });


    }

    public void updateComplaint() {


        final ProgressDialog progressDialog = new ProgressDialog(ComplaintDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseCommon> call = saasapi.updateComplaint(new RequestUpdateComplaint(strComplaintNo,updateBillNo,updateDealerName,updateProductName));
        call.enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {

                Log.d(TAG, response.body() + "");
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {
                    updateBillNo="";
                    updateDealerName="";
                    updateProductName="";
                    GetComplaintDetails(strComplaintNo);
                    Utility.showAlert(ComplaintDetailsActivity.this,response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(ComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });


    }

    private void GetComplaintDetails(String complaintId) {

        final ProgressDialog progressDialog = new ProgressDialog(ComplaintDetailsActivity.this);
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
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        strProductName = response.body().getData().getProductName();
                        strBillNo = response.body().getData().getBillNumber();
                        strDealerName = response.body().getData().getDealerName();

                        if (Utility.isNotNull(strProductName)) {
                            tvProductName.setText(strProductName);
                        } else {
                            tvProductName.setText("-");
                        }
                        if (Utility.isNotNull(strBillNo)) {
                            tvBillNo.setText(strBillNo);
                        } else {
                            tvBillNo.setText("-");
                        }
                        if (Utility.isNotNull(strDealerName)) {
                            tvDealerName.setText(strDealerName);
                        } else {
                            tvDealerName.setText("-");
                        }


                    } else {
                        // Utility.showAlert(ComplaintDetailsActivity.this, response.body().error_message);
                    }

                } else {
                    /*if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(ComplaintDetailsActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }


            }

            @Override
            public void onFailure(Call<ResponsecomplaintDetails> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(ComplaintDetailsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
