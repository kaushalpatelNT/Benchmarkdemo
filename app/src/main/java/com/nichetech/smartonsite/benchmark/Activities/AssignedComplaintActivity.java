package com.nichetech.smartonsite.benchmark.Activities;

import android.app.ProgressDialog;
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
import com.nichetech.smartonsite.benchmark.Adapter.AssignedAdapter;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintList;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseAssignedList;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class AssignedComplaintActivity extends AppCompatActivity {

    private String TAG = "Assigned Complaint List";

    private Preferences mPref;
    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvHeader;

    private RecyclerView rlvAssignedList;
    private ArrayList<ResponseAssignedList.ComplaintData> complaintDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_complaint);

        mPref = new Preferences(AssignedComplaintActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);

        }
        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.assignedTitle);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(AssignedComplaintActivity.this);
        tvHeader.setTypeface(raleway_semibold);

        ivBack = (ImageView) findViewById(R.id.iv_back);


        rlvAssignedList = (RecyclerView) findViewById(R.id.rl_assigned_complaints);
        rlvAssignedList.setHasFixedSize(true);
        rlvAssignedList.setLayoutManager(new GridLayoutManager(this, 1));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void getAssignedList() {


        final ProgressDialog progressDialog = new ProgressDialog(AssignedComplaintActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseAssignedList> call = saasapi.GetComplaintList(new RequestComplaintList(100, 1, Constant.ComplaintType.Allocated));
        call.enqueue(new Callback<ResponseAssignedList>() {
            @Override
            public void onResponse(Call<ResponseAssignedList> call, Response<ResponseAssignedList> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        complaintDatas = response.body().getData();

                        rlvAssignedList.setAdapter(new AssignedAdapter(AssignedComplaintActivity.this, complaintDatas));


                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    } else {
                        Utility.showAlert(AssignedComplaintActivity.this, response.body().error_message, new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                onBackPressed();
                            }
                        });
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(AssignedComplaintActivity.this, error_message, new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                onBackPressed();
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
                Utility.showToast(AssignedComplaintActivity.this, getString(R.string.msgServerNotConnect));

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isOnline(AssignedComplaintActivity.this)) {
            getAssignedList();
        } else {
            Utility.showToast(AssignedComplaintActivity.this, R.string.msgNoInternet);
        }
        getAssignedList();
    }
}
