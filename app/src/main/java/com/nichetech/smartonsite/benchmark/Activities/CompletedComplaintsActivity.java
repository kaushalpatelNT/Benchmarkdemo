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
import com.nichetech.smartonsite.benchmark.Adapter.CompletedComplaintAdapter;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.Data.CompletedComplaintItem;
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

public class CompletedComplaintsActivity extends AppCompatActivity {


    private String TAG = "Completed Complaint List";
    private Preferences mPref;
    private Toolbar toolbar;
    private Utility utility;
    private Context mContext;

    private ImageView ivHeader;
    private TextView tvName, tvCompName, tvId, tvDate, tvHeader;
    private RecyclerView rlCompletedList;
    CompletedComplaintAdapter madapter;
    List<CompletedComplaintItem> mCompletedList;
    private List<ResponseAssignedList.ComplaintData> complaintDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_complaints);

        utility = new Utility(this);
        mPref = new Preferences(CompletedComplaintsActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }

        tvName = (TextView) findViewById(R.id.tv_completed_name);
        tvCompName = (TextView) findViewById(R.id.tv_completed_company_name);
        tvId = (TextView) findViewById(R.id.tv_completed_id);
        tvDate = (TextView) findViewById(R.id.tv_completed_date);
        ivHeader = (ImageView) findViewById(R.id.iv_back);
        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.completedTitle);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(CompletedComplaintsActivity.this);
        tvHeader.setTypeface(raleway_semibold);


        mCompletedList = new ArrayList<CompletedComplaintItem>();
        rlCompletedList = (RecyclerView) findViewById(R.id.rl_completed_compalints);
        rlCompletedList.setHasFixedSize(true);
        rlCompletedList.setLayoutManager(new GridLayoutManager(this, 1));


        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });


    }

    private void getCompletedList() {


        final ProgressDialog progressDialog = new ProgressDialog(CompletedComplaintsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        String company_id = mPref.getStrUserOrgId();
        String user_id = mPref.getStrUserId();


        Call<ResponseAssignedList> call = saasapi.GetComplaintList(new RequestComplaintList(100, 1, Constant.ComplaintType.Closed));
        call.enqueue(new Callback<ResponseAssignedList>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResponseAssignedList> call, Response<ResponseAssignedList> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        complaintDatas = response.body().getData();

                        rlCompletedList.setAdapter(new CompletedComplaintAdapter(CompletedComplaintsActivity.this, complaintDatas));


                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    } else {
                        Utility.showAlert(CompletedComplaintsActivity.this, response.body().error_message);
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(CompletedComplaintsActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseAssignedList> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(CompletedComplaintsActivity.this, getString(R.string.msgServerNotConnect));

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isOnline(CompletedComplaintsActivity.this)) {
            getCompletedList();
        } else {
            Utility.showToast(this, R.string.msgNoInternet);
        }
    }
}
