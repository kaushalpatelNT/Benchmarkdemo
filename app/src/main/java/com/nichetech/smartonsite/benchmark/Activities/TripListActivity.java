package com.nichetech.smartonsite.benchmark.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Adapter.TripListAdapter;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestTripList;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseTripList;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripListActivity extends AppCompatActivity {

    private String TAG = "Trip List";

    private Preferences mPref;
    private Utility utility;
    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvHeader;
    private TripListAdapter tripListAdapter;
    private ArrayList<ResponseTripList.TripListData> tripListdata = new ArrayList<>();

    private RecyclerView rlTripList;
    private int start = 0;
    private int limit = 25;
    private int totalCount = 0;
    private boolean isProgressPremium = false;
    private boolean isFirstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        mPref = new Preferences(this);
        utility = new Utility(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }
        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.tripListTitle);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(this);
        tvHeader.setTypeface(raleway_semibold);

        ivBack = (ImageView) findViewById(R.id.iv_back);


        rlTripList = (RecyclerView) findViewById(R.id.rl_trip_list);
        rlTripList.setHasFixedSize(true);
        rlTripList.setLayoutManager(new GridLayoutManager(this, 1));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tripListAdapter = new TripListAdapter(TripListActivity.this, tripListdata, rlTripList);
        tripListAdapter.setOnLoadListener(new TripListAdapter.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Utility.isOnline(TripListActivity.this)) {

                        if (totalCount > start && totalCount != 0) {
                            tripListdata.add(null);
                            tripListAdapter.notifyItemInserted(tripListdata.size());
                            isProgressPremium = true;
                            getTripList(start, limit);
                        } else if (tripListAdapter != null) {
                            tripListAdapter.setLoaded();

                        } else {
                            isProgressPremium = false;
                        }

                    } else {
                        if (tripListAdapter != null) {
                            removeLoader();
                        }
                        isProgressPremium = false;
                        Utility.showToast(TripListActivity.this, getString(R.string.msgNoInternet));
                    }

                } catch (Exception e) {
                    if (tripListAdapter != null) {
                        removeLoader();
                    }
                    isProgressPremium = false;
                    e.printStackTrace();
                }
            }
        });
        rlTripList.setAdapter(tripListAdapter);
        getTripList(start, limit);

    }

    public void getTripList(int s, int l) {

        final ProgressDialog progressDialog = new ProgressDialog(TripListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        if (isFirstTime) progressDialog.show();
        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        String org_id = mPref.getStrUserOrgId();
        String user_id = mPref.getStrUserId();

        RequestTripList requestTripList = new RequestTripList();
        requestTripList.Page = "1";

        Call<ResponseTripList> call = saasapi.TripList(requestTripList);
        call.enqueue(new Callback<ResponseTripList>() {
            @Override
            public void onResponse(Call<ResponseTripList> call, Response<ResponseTripList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError_code() == 1) {
                        isFirstTime = false;
                        if (isProgressPremium) {
                            removeLoader();
                        }
                        List<ResponseTripList.TripListData> result = response.body().getData();
                        totalCount = response.body().getTrip_count();
                        tripListdata.addAll(result);
                        tripListAdapter.notifyDataSetChanged();
                        tripListAdapter.setLoaded();
                        start = tripListdata.size();

                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    } else {
                        Utility.showAlert(TripListActivity.this, response.body().error_message, new Utility.OnOkClickListener() {
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
                        if (isProgressPremium) {
                            removeLoader();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTripList> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(TripListActivity.this, getString(R.string.msgServerNotConnect));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inDashboard = new Intent(TripListActivity.this, DashboardActivity.class);
        inDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inDashboard);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    public void removeLoader() {
        int oldSize = tripListdata.size();
        tripListdata.removeAll(Collections.singleton(null));
        int newSize = tripListdata.size();
        tripListAdapter.notifyItemRangeRemoved(newSize, oldSize - newSize);
        tripListAdapter.setLoaded();
        isProgressPremium = false;
    }
}
