package com.nichetech.smartonsite.benchmark.Activities;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.target.Target;

import com.bumptech.glide.Glide;
import com.nichetech.smartonsite.benchmark.Adapter.RoutePointAdapter;
import com.nichetech.smartonsite.benchmark.Common.DateTimeUtils;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseTripList;

import java.util.ArrayList;

public class TripDetailActivity extends AppCompatActivity {

    private String TAG = "Trip List";

    private Preferences mPref;
    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvHeader, tvTripDetailNameLabel, tvTripDetailName, tvTripDetailNoLabel, tvTripDetailNo, tvTripDetailDateLabel,
            tvTripDetailDate, tvTripDetaiStartTimeLabel, tvTripDetailStartTime, tvTripDetaiEndTimeLabel, tvTripDetailEndTime,
            tvTripDetailDistanceLabel, tvTripDetailDistance, tvRouteName, tvRoute, tvRouteDate, tvRouteStartTripLabel, tvRouteStartTrip, tvRouteRndTripLabel, tvRouteRndTrip, tvTripDetailDiscriptionLabel, tvTripDetailDiscription;

    private String strTripDetailName, strTripDetailNo, strTripDetailDate, strTripDetailStartTime, strTripDetailEndTime,
            strRouteStartTrip, strRouteEndTrip, strLocationPoint, strDistance, strDiscription, strTripImage;

    private int Position;

    private RecyclerView rvRoutePoint;

    private ArrayList<ResponseTripList.TripListData.Location> locations = new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<ResponseTripList.TripListData> imageList = new ArrayList<>();
    private String imageListUrl = "";
    private String listImage[];

    private LinearLayout llRoute, llRouteDetail, llImageView;
    private ImageView ivTripSelect, ivTripDescription;
    public int click = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        mPref = new Preferences(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }
        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.tripDetailTitle);

        rvRoutePoint = (RecyclerView) findViewById(R.id.lv_routePoint);
        rvRoutePoint.setHasFixedSize(false);
        rvRoutePoint.setLayoutManager(new GridLayoutManager(this, 1));
        rvRoutePoint.setNestedScrollingEnabled(false);


        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(this);
        Typeface raleway_medium = TypefaceUtils.RalewayMedium(this);
        tvHeader.setTypeface(raleway_semibold);

        tvTripDetailNameLabel = (TextView) findViewById(R.id.tv_trip_detail_name_label);
        tvTripDetailNoLabel = (TextView) findViewById(R.id.tv_trip_detail_no_label);
        tvTripDetailDateLabel = (TextView) findViewById(R.id.tv_trip_detail_date_label);
        tvTripDetaiStartTimeLabel = (TextView) findViewById(R.id.tv_trip_detail_start_time_label);
        tvTripDetaiEndTimeLabel = (TextView) findViewById(R.id.tv_trip_detail_end_time_label);
        tvTripDetailDistanceLabel = (TextView) findViewById(R.id.tv_trip_detail_distance_label);
        tvTripDetailDiscriptionLabel = (TextView) findViewById(R.id.tv_trip_detail_discription_label);
        tvTripDetailName = (TextView) findViewById(R.id.tv_trip_detail_name);
        tvTripDetailNo = (TextView) findViewById(R.id.tv_trip_detail_no);
        tvTripDetailDate = (TextView) findViewById(R.id.tv_trip_detail_date);
        tvTripDetailStartTime = (TextView) findViewById(R.id.tv_trip_detail_start_time);
        tvTripDetailEndTime = (TextView) findViewById(R.id.tv_trip_detail_end_time);
        tvTripDetailDistance = (TextView) findViewById(R.id.tv_trip_detail_distance);
        tvTripDetailDiscription = (TextView) findViewById(R.id.tv_trip_detail_discription);

        tvRoute = (TextView) findViewById(R.id.tv_route);
        tvRouteName = (TextView) findViewById(R.id.tv_route_name);
        tvRouteDate = (TextView) findViewById(R.id.tv_route_date);
        tvRouteStartTripLabel = (TextView) findViewById(R.id.tv_route_start_trip_label);
        tvRouteRndTripLabel = (TextView) findViewById(R.id.tv_route_end_trip_label);
        tvRouteStartTrip = (TextView) findViewById(R.id.tv_route_start_trip);
        tvRouteRndTrip = (TextView) findViewById(R.id.tv_route_end_trip);
//        tvRoutePoint = (TextView) findViewById(R.id.tv_route_point);


        //----------  Typeface  ----------//
        tvTripDetailNameLabel.setTypeface(raleway_medium);
        tvTripDetailNoLabel.setTypeface(raleway_medium);
        tvTripDetailDateLabel.setTypeface(raleway_medium);
        tvTripDetaiStartTimeLabel.setTypeface(raleway_medium);
        tvTripDetaiEndTimeLabel.setTypeface(raleway_medium);
        tvTripDetailDistanceLabel.setTypeface(raleway_medium);
        tvTripDetailDiscriptionLabel.setTypeface(raleway_medium);
        tvTripDetailName.setTypeface(raleway_semibold);
        tvTripDetailNo.setTypeface(raleway_semibold);
        tvTripDetailDate.setTypeface(raleway_semibold);
        tvTripDetailStartTime.setTypeface(raleway_semibold);
        tvTripDetailEndTime.setTypeface(raleway_semibold);
        tvTripDetailDistance.setTypeface(raleway_semibold);
        tvTripDetailDiscription.setTypeface(raleway_semibold);

        tvRoute.setTypeface(raleway_semibold);
        tvRouteName.setTypeface(raleway_semibold);
        tvRouteDate.setTypeface(raleway_semibold);
        tvRouteStartTripLabel.setTypeface(raleway_semibold);
        tvRouteRndTripLabel.setTypeface(raleway_semibold);
        tvRouteStartTrip.setTypeface(raleway_medium);
        tvRouteRndTrip.setTypeface(raleway_medium);

        llRoute = (LinearLayout) findViewById(R.id.ll_route);
        llRouteDetail = (LinearLayout) findViewById(R.id.ll_route_detail);
        llImageView = (LinearLayout) findViewById(R.id.ll_imageView);
        ivTripSelect = (ImageView) findViewById(R.id.iv_list_select);
        ivTripSelect.setSelected(true);
        ivTripDescription = (ImageView) findViewById(R.id.iv_trip_description);

        //----------  Get Data from intent ----------//
        strTripDetailName = getIntent().getStringExtra("customer_name");
        strTripDetailNo = getIntent().getStringExtra("customer_contact");
        strTripDetailDate = getIntent().getStringExtra("trip_createddate");
        strTripDetailStartTime = getIntent().getStringExtra("trip_starttime");
        strTripDetailEndTime = getIntent().getStringExtra("trip_endtime");
        strRouteStartTrip = getIntent().getStringExtra("trip_fromaddress");
        strRouteEndTrip = getIntent().getStringExtra("trip_toaddress");
        strDistance = getIntent().getStringExtra("distance");
        strDiscription = getIntent().getStringExtra("discription");
        locations = (ArrayList<ResponseTripList.TripListData.Location>) getIntent().getSerializableExtra("location");
        imageListUrl = getIntent().getStringExtra("images");

        Position = getIntent().getIntExtra("positoin", 0);

        for (int i = 0; i < locations.size(); i++) {
            strLocationPoint = locations.get(i).getTrip_address();
            arrayList.add(strLocationPoint);


        }

       /* for (int i = 0; i < imageList.size(); i++) {
            imageListUrl.add(imageList.get(i).getTrip_images());
        }*/

       if(!Utility.isNotNull(imageListUrl)) {
            listImage=imageListUrl.split(",");
       }
        Log.e("imageListUrl==>", "" + listImage.length);

        RoutePointAdapter routePointAdapter = new RoutePointAdapter(this, locations);

        if (arrayList.size() > 0) {
            rvRoutePoint.setVisibility(View.VISIBLE);
        } else {
            rvRoutePoint.setVisibility(View.GONE);
        }

        rvRoutePoint.setAdapter(routePointAdapter);


        //---------- convert into date and time ----------//
        String date = DateTimeUtils.changeDateTimeFormat(strTripDetailStartTime,
                DateTimeUtils.SERVER_FORMAT_DATE_TIME_T);
        String startTime = DateTimeUtils.changeTime(strTripDetailStartTime,
                DateTimeUtils.SERVER_FORMAT_DATE_TIME_T);
        String endTime = DateTimeUtils.changeTime(strTripDetailEndTime,
                DateTimeUtils.SERVER_FORMAT_DATE_TIME_T);


        //---------- set text into textview ----------//
        tvTripDetailName.setText(strTripDetailName);
        tvRouteName.setText(strTripDetailName);
        tvTripDetailNo.setText(strTripDetailNo);
        tvTripDetailDate.setText(date);
        tvTripDetailStartTime.setText(startTime);
        tvTripDetailEndTime.setText(endTime);
        tvRouteStartTrip.setText(strRouteStartTrip);
        tvRouteRndTrip.setText(strRouteEndTrip);
        tvRouteDate.setText(date);
        if (Utility.isNotNull(strDiscription)) {
            tvTripDetailDistance.setText(strDistance);
        } else {
            tvTripDetailDistance.setText("0 Km");
        }
        tvTripDetailDiscription.setText(strDiscription);


        for (int j = 0; j < listImage.length; j++) {
            if (Utility.isNotNull(String.valueOf(listImage[j]))) {
                llImageView.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(listImage[j])
                      //  .centerCrop()
                        //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(ivTripDescription);

            } else {
                llImageView.setVisibility(View.VISIBLE);
            }
        }



        llRoute.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {


                if (click == 0) {
                    click = 1;
                    ivTripSelect.setSelected(true);
                    llRouteDetail.setVisibility(View.GONE);
                } else {
                    click = 0;
                    ivTripSelect.setSelected(false);
                    llRouteDetail.setVisibility(View.VISIBLE);
                }
            }
        });


        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}