package com.nichetech.smartonsite.benchmark.Activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import com.bumptech.glide.request.target.Target;
import com.nichetech.smartonsite.benchmark.Adapter.ActionDetailImageAdapter;
import com.nichetech.smartonsite.benchmark.BuildConfig;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestCustomFormDetail;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCustomFormDetail;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class CustomFormDetailActivity extends AppCompatActivity {
    private static String TAG = "CustomFormActivity";

    private Utility utility;
    private Preferences mPref;
    private Toolbar toolbar;
    private TextView tvHeader;
    private LinearLayout linCustomBody;
    LinearLayout.LayoutParams params;
    private List<View> allViews = new ArrayList<View>();
    private static int viewsCount = 0;
    private NestedScrollView nestedScrollView;
    private GridView gridView;
    private TextView textView;
    private ImageView ivBack;
    public ArrayList<String> images = new ArrayList<>();
    int position;

    private List<ResponseCustomFormDetail.FormData> form_fieldsList = new ArrayList<>();
    private List<ResponseCustomFormDetail.Action> action_fieldsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_form_detail);

        utility = new Utility(this);
        mPref = new Preferences(this);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(5, 5, 5, 5);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }

        tvHeader = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(CustomFormDetailActivity.this);
        tvHeader.setTypeface(raleway_semibold);

        linCustomBody = (LinearLayout) findViewById(R.id.lin_custom_form_body);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll);
        nestedScrollView.setSmoothScrollingEnabled(false);

        RequestCustomFormDetail requestCustomFormDetail = new RequestCustomFormDetail();
        requestCustomFormDetail.company_id = mPref.getStrUserOrgId();
        requestCustomFormDetail.user_id = mPref.getStrUserId();
        requestCustomFormDetail.form_id = getIntent().getStringExtra("org_form_id");
        requestCustomFormDetail.complaint_id = getIntent().getStringExtra("complaint_id");
        requestCustomFormDetail.complaint_code = getIntent().getStringExtra("company_code");
        requestCustomFormDetail.complaint_code = getIntent().getStringExtra("company_code");
        position = getIntent().getIntExtra("position", 0);
        Log.e("POSITION", "" + position);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Utility.isOnline(CustomFormDetailActivity.this)) {
            getCustomForm(requestCustomFormDetail);
        } else {
            Utility.showAlert(CustomFormDetailActivity.this, getString(R.string.msgNoInternet));
        }



    }

    public void getCustomForm(RequestCustomFormDetail requestCustomFormDetail) {

        final ProgressDialog progressDialog = new ProgressDialog(CustomFormDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());


        Call<ResponseCustomFormDetail> call = saasapi.getCustomFormUpload(requestCustomFormDetail);
        call.enqueue(new Callback<ResponseCustomFormDetail>() {
            @Override
            public void onResponse(Call<ResponseCustomFormDetail> call, Response<ResponseCustomFormDetail> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {
                    form_fieldsList = response.body().getData().getFormData();
                    action_fieldsList = response.body().getData().getAction();

                    for (int i = 0; i < form_fieldsList.size(); i++) {
                        tvHeader.setText(form_fieldsList.get(i).getForm_title());
                    }

                    createCustomForm();
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseCustomFormDetail> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }


    public void createCustomForm() {

        for (int k = 0; k < action_fieldsList.get(position).getForm_data().size(); k++) {
            for (int r = 0; r < form_fieldsList.get(k).getForm_fields().size(); r++) {

                switch (form_fieldsList.get(k).form_fields.get(r).getField_type()) {

                    case "text":
                        createTextView(form_fieldsList.get(k).form_fields.get(r).getField_label());
                        if (Utility.isNotNull((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()))) {
                            createHintTextView((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()));
                        } else {
                            createHintTextView("-");
                        }
                        createView();
                        break;

                    case "dropdown":
                        createTextView(form_fieldsList.get(k).form_fields.get(r).getField_label());
                        if (Utility.isNotNull((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()))) {
                            createHintTextView((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()));
                        } else {
                            createHintTextView("-");
                        }
                        createView();
                        break;

                    case "textarea":
                        createTextView(form_fieldsList.get(k).form_fields.get(r).getField_label());
                        if (Utility.isNotNull((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()))) {
                            createHintTextView((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()));
                        } else {
                            createHintTextView("-");
                        }

                        createView();
                        break;

                    case "checkbox":

                        createTextView(form_fieldsList.get(k).form_fields.get(r).getField_label());
                        HashMap<String, Collection<Object>> map = new HashMap<>(action_fieldsList.get(position).getForm_data().size());
                        String key = form_fieldsList.get(k).form_fields.get(r).getField_unique_id();
                        List<Map<String, Object>> values = action_fieldsList.get(position).form_data;

                        for (int i = 0; i < values.size(); i++) {
                            if (values.get(i).containsKey(key)) {
                                Log.e("values == ", "" + values.get(i).get(key));
                                map.put(key, (Collection<Object>) values.get(i).get(key));
                            }
                        }

                        if (map.get(key).size() != 0) {
                            for (int i = 0; i < map.get(key).size(); i++) {

                                if (Utility.isNotNull((String) map.get(key).toArray()[i])) {
                                    createHintTextView((String) map.get(key).toArray()[i]);
                                } else {
                                    createHintTextView("-");
                                }

                            }
                        } else {
                            createHintTextView("-");
                        }

                        createView();
                        break;

                    case "radio":
                        createTextView(form_fieldsList.get(k).form_fields.get(r).getField_label());
                        createHintTextView((String) action_fieldsList.get(position).getForm_data().get(k).get(form_fieldsList.get(k).getForm_fields().get(r).getField_unique_id()));
                        createView();
                        break;

                    case "image":

                        HashMap<String, Collection<Object>> mapImage = new HashMap<>(action_fieldsList.get(position).getForm_data().size());
                        String keyImage = form_fieldsList.get(k).form_fields.get(r).getField_unique_id();
                        List<Map<String, Object>> valuesImage = action_fieldsList.get(position).form_data;

                        for (int i = 0; i < valuesImage.size(); i++) {
                            if (valuesImage.get(i).containsKey(keyImage)) {
                                Log.e("values == ", "" + valuesImage.get(i).get(keyImage));
                                mapImage.put(keyImage, (Collection<Object>) valuesImage.get(i).get(keyImage));
                            }
                        }


                        for (int i = 0; i < mapImage.get(keyImage).size(); i++) {
                            images.add(BuildConfig.imageUrl + mapImage.get(keyImage).toArray()[i]);
                        }

                        if (images.size() > 0) {
                            createTextView(form_fieldsList.get(k).form_fields.get(r).getField_label());
                            createGridView();
                            createView();
                        }

                        break;

                }
            }
        }
    }


    //---------- TEXTVIEW ----------//
    public void createTextView(String hint) {
        TextView textView1 = new TextView(this);
        textView1.setId(viewsCount++);
        textView1.setText(hint);
        textView1.setTextSize(20f);
        textView1.setPadding(15, 0, 0, 0);
        textView1.setTypeface(TypefaceUtils.RalewayMedium(CustomFormDetailActivity.this));
        textView1.setTextColor(ContextCompat.getColor(CustomFormDetailActivity.this, R.color.colorPrimary));
        allViews.add(textView1);
        linCustomBody.addView(textView1, params);

    }

    //---------- TEXTVIEW ----------//
    public void createHintTextView(String hint) {
        textView = new TextView(this);
        textView.setId(viewsCount++);
        textView.setText(hint);
        textView.setTextSize(18f);
        textView.setGravity(Gravity.START);
        textView.setPadding(15, 0, 0, 0);
        textView.setTypeface(TypefaceUtils.RalewaySemiBold(CustomFormDetailActivity.this));
        textView.setTextColor(ContextCompat.getColor(CustomFormDetailActivity.this, R.color.text_black));
        allViews.add(textView);
        linCustomBody.addView(textView, params);
    }

    //---------- LINEVIEW ----------//
    public void createView() {
        View view = new View(this);
        view.setMinimumHeight(2);
        view.setBackgroundColor(ContextCompat.getColor(CustomFormDetailActivity.this, R.color.colorDivider));
        allViews.add(view);
        linCustomBody.addView(view, params);
    }

    //---------- GRIDVIEW ----------//
    private void createGridView() {
        gridView = new GridView(this);
        gridView.setNumColumns(3);
        ActionDetailImageAdapter img = new ActionDetailImageAdapter(CustomFormDetailActivity.this, images);
        gridView.setAdapter(img);
        gridView.setPadding(5, 5, 5, 5);
        gridView.setBackgroundResource(R.drawable.stylishimageborder);
        allViews.add(gridView);
        linCustomBody.addView(gridView, params);
    }

    //---------- IMAGEVIEW ----------//
    private void createImageView() {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setImageResource(R.drawable.ic_plus);
        imageView.setLayoutParams(layoutParams);
        allViews.add(imageView);
        linCustomBody.addView(imageView, params);
    }
}
