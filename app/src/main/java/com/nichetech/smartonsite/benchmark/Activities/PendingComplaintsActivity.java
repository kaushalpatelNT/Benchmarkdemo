package com.nichetech.smartonsite.benchmark.Activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Adapter.PendingComplaintAdapter;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.Data.PendingComplaintItem;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintList;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseAssignedList;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingComplaintsActivity extends AppCompatActivity {

    private String TAG = "Pending Complaint List";

    private Preferences mPref;
    private Toolbar toolbar;
    private Utility utility;
    private Context mContext;

    private ImageView ivBack;
    private TextView tvName, tvCompName, tvId, tvDate, tvHeader;
    private RecyclerView rlPendingList;
    PendingComplaintAdapter madapter;
    List<PendingComplaintItem> mPendingList;
    private List<ResponseAssignedList.ComplaintData> complaintDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaints);

        tvName = (TextView) findViewById(R.id.tv_pending_name);
        tvCompName = (TextView) findViewById(R.id.tv_pending_company_name);
        tvId = (TextView) findViewById(R.id.tv_pending_id);
        tvDate = (TextView) findViewById(R.id.tv_pending_date);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        utility = new Utility(this);
        mPref = new Preferences(PendingComplaintsActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }

        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.pendingTitle);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(PendingComplaintsActivity.this);
        tvHeader.setTypeface(raleway_semibold);

        rlPendingList = (RecyclerView) findViewById(R.id.rl_pending_compalints);
        rlPendingList.setHasFixedSize(true);
        rlPendingList.setLayoutManager(new GridLayoutManager(this, 1));


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });


    }

    private void getPendingList() {


        final ProgressDialog progressDialog = new ProgressDialog(PendingComplaintsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        String company_id = mPref.getStrUserOrgId();
        String user_id = mPref.getStrUserId();


        Call<ResponseAssignedList> call = saasapi.GetComplaintList(new RequestComplaintList(100, 1, Constant.ComplaintType.Accepted));
        call.enqueue(new Callback<ResponseAssignedList>() {
            @Override
            public void onResponse(Call<ResponseAssignedList> call, Response<ResponseAssignedList> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        complaintDatas = response.body().getData();

                        rlPendingList.setAdapter(new PendingComplaintAdapter(PendingComplaintsActivity.this, complaintDatas));


                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    } else {
                        Utility.showAlert(PendingComplaintsActivity.this, response.body().error_message, new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                ivBack.callOnClick();
                            }
                        });
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(PendingComplaintsActivity.this, error_message, new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                ivBack.callOnClick();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseAssignedList> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(PendingComplaintsActivity.this, String.valueOf(R.string.msgServerNotConnect));

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isOnline(PendingComplaintsActivity.this)) {
            getPendingList();
        } else {
            Utility.showToast(this, R.string.msgNoInternet);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
