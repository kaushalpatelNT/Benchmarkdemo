package com.nichetech.smartonsite.benchmark.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.nichetech.smartonsite.benchmark.Adapter.ActionDetailImageAdapter;
import com.nichetech.smartonsite.benchmark.Common.DateTimeUtils;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseActionDetail;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseComplaintDetail;

import java.util.ArrayList;
import java.util.List;

public class ActionDetailActivity extends AppCompatActivity {

    private GridView imagegrid;
    public ArrayList<String> images = new ArrayList<>();
    private ResponseComplaintDetail.Data complaintDatas;
    private List<ResponseActionDetail> actionDetails = new ArrayList<>();
    private String strActionId, strActionTaken, strDate, strStatus;
    private TextView tvComplaintStatus, tvComplaintActionId, tvComplaintActionTaken, tvComplaintActionCrdate, tvHeader, tvNoImages;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_detail);

        imagegrid = (GridView) findViewById(R.id.gv_images);
        imagegrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                (v.getParent()).requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        tvHeader = (TextView) findViewById(R.id.toolbar_title);
        tvHeader.setText(R.string.Complaint_action_detail_title);

        ivBack = (ImageView) findViewById(R.id.ivBack);

        tvComplaintStatus = (TextView) findViewById(R.id.tv_complaint_status);
        tvComplaintActionId = (TextView) findViewById(R.id.tv_complaint_action_id);
        tvComplaintActionTaken = (TextView) findViewById(R.id.tv_complaint_action_taken);
        tvComplaintActionCrdate = (TextView) findViewById(R.id.tv_complaint_action_crdate);
        tvNoImages = (TextView) findViewById(R.id.tv_noImages);

        strActionId = getIntent().getStringExtra("company_id");
        strActionTaken = getIntent().getStringExtra("action_taken");
        strStatus = getIntent().getStringExtra("status");
        strDate = getIntent().getStringExtra("date");
        images.add(TextUtils.join(",", getIntent().getStringArrayListExtra("images")));

        if (getIntent().hasExtra("images")) {
            ArrayList<String> ima = getIntent().getStringArrayListExtra("images");
            if (ima != null && ima.size() > 0) images.addAll(ima);
        }

        String date = DateTimeUtils.changeDateTimeFormat(strDate,
                DateTimeUtils.SERVER_FORMAT_DATE_TIME);

        tvComplaintActionId.setText(strActionId);
        tvComplaintActionTaken.setText(strActionTaken);
        tvComplaintStatus.setText(strStatus);
        tvComplaintActionCrdate.setText(date);

        //--------- TYPEFACE  ------//
        ((TextView) findViewById(R.id.tv_label_complaint_action_id)).setTypeface(TypefaceUtils.RalewayMedium(ActionDetailActivity.this));
        ((TextView) findViewById(R.id.tv_label_complaint_action_taken)).setTypeface(TypefaceUtils.RalewayMedium(ActionDetailActivity.this));
        ((TextView) findViewById(R.id.tv_label_complaint_status)).setTypeface(TypefaceUtils.RalewayMedium(ActionDetailActivity.this));
        ((TextView) findViewById(R.id.tv_label_complaint_action_crdate)).setTypeface(TypefaceUtils.RalewayMedium(ActionDetailActivity.this));


        tvComplaintActionId.setTypeface(TypefaceUtils.RalewaySemiBold(ActionDetailActivity.this));
        tvComplaintActionTaken.setTypeface(TypefaceUtils.RalewaySemiBold(ActionDetailActivity.this));
        tvComplaintStatus.setTypeface(TypefaceUtils.RalewaySemiBold(ActionDetailActivity.this));
        tvComplaintActionCrdate.setTypeface(TypefaceUtils.RalewaySemiBold(ActionDetailActivity.this));

        if (images != null && images.size() > 0) {
            ActionDetailImageAdapter img = new ActionDetailImageAdapter(ActionDetailActivity.this, images);
            imagegrid.setAdapter(img);
            imagegrid.setVisibility(View.VISIBLE);
            tvNoImages.setVisibility(View.GONE);
        } else {
            imagegrid.setVisibility(View.GONE);
            tvNoImages.setVisibility(View.VISIBLE);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
