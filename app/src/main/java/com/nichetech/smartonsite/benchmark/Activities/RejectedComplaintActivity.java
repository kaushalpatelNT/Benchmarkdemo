package com.nichetech.smartonsite.benchmark.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Adapter.RejectedComplaintAdapter;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.Data.RejectedComplaintItem;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintList;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseAssignedList;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class RejectedComplaintActivity extends AppCompatActivity {

    private String TAG = "Rejected Complaint List";
    private Preferences mPref;
    private Toolbar toolbar;
    private Utility utility;
    private Context mContext;

    private ImageView ivHeader;
    private TextView tvName, tvCompName, tvId, tvDate, tvHeader;
    private RecyclerView rlRejectedList;
    RejectedComplaintAdapter madapter;
    List<RejectedComplaintItem> mRejectedList;
    private List<ResponseAssignedList.ComplaintData> complaintDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_complaint);

        utility = new Utility(this);
        mPref = new Preferences(RejectedComplaintActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }

        tvName = (TextView) findViewById(R.id.tv_rejected_name);
        tvCompName = (TextView) findViewById(R.id.tv_rejected_company_name);
        tvId = (TextView) findViewById(R.id.tv_rejected_id);
        tvDate = (TextView) findViewById(R.id.tv_rejected_date);
        tvHeader = (TextView) findViewById(R.id.tv_title);
        ivHeader = (ImageView) findViewById(R.id.iv_back);

        tvHeader.setText(R.string.rejectedTitle);
        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(RejectedComplaintActivity.this);
        tvHeader.setTypeface(raleway_semibold);


        rlRejectedList = (RecyclerView) findViewById(R.id.rl_rejected_compalints);
        rlRejectedList.setHasFixedSize(true);
        rlRejectedList.setLayoutManager(new GridLayoutManager(this, 1));


        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });


    }

    private void getRejectedList() {


        final ProgressDialog progressDialog = new ProgressDialog(RejectedComplaintActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        String company_id = mPref.getStrUserOrgId();
        String user_id = mPref.getStrUserId();


        Call<ResponseAssignedList> call = saasapi.GetComplaintList(new RequestComplaintList(100, 1, Constant.ComplaintType.Rejected));
        call.enqueue(new Callback<ResponseAssignedList>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseAssignedList> call, Response<ResponseAssignedList> response) {

                Log.d(TAG, response.code() + "");
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        complaintDatas = response.body().getData();

                        rlRejectedList.setAdapter(new RejectedComplaintAdapter(RejectedComplaintActivity.this, complaintDatas));



                    } else {
                        Utility.showAlert(RejectedComplaintActivity.this, response.body().error_message);
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(RejectedComplaintActivity.this, error_message,new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                finish();
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
                Utility.showToast(RejectedComplaintActivity.this, getString(R.string.msgServerNotConnect));

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isOnline(RejectedComplaintActivity.this)) {
            getRejectedList();
        } else {
            Utility.showToast(this, R.string.msgNoInternet);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
