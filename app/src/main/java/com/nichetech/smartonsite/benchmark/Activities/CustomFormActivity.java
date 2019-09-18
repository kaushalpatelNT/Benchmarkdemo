package com.nichetech.smartonsite.benchmark.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import com.bumptech.glide.request.target.Target;
import com.nichetech.smartonsite.benchmark.Common.*;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestCustomForm;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestCustomFormUpload;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCustomForm;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCustomFormUpload;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseUploadImage;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Kaushal on 08-05-2017.
 */

public class CustomFormActivity extends AppCompatActivity implements View.OnTouchListener {

    private static String TAG = "CustomFormActivity";

    private Utility utility;
    private Preferences mPref;
    private Toolbar toolbar;

    private TextView tvHeader;
    private LinearLayout linCustomBody;
    private Button btnSubmit;
    private Spinner spComplaintStatus;
    private String Status[] = {"Status", "Completed", "Pending"};
    LinearLayout.LayoutParams params;
    private List<View> allViews = new ArrayList<View>();
    private static int viewsCount = 0;
    private String Description, status = "";
    private NestedScrollView nestedScrollView;
    private RadioButton radioButton;
    private List<String> name = new ArrayList<>();
    private ProgressDialog progressDialog;
    String company_id, user_id, form_id, strComplaint;
    RequestCustomFormUpload responseCustomFormUpload;
    List<String> stringList = new ArrayList<>();
    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<String> pathfordb = new ArrayList<>();
    private int complete = 0;
    private GridView gridView;
    private TextView textView;
    private Uri fileUri;
    private String path;
    private ImageView ivBack;
    public static int MAXIMUM_UPLOAD_FILE_SIZE = 1024 * 5;

    private List<ResponseCustomForm.form_fields> form_fieldsList = new ArrayList<>();


    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_form);

        utility = new Utility(this);
        mPref = new Preferences(this);
        progressDialog = new ProgressDialog(CustomFormActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));


        company_id = mPref.getStrUserOrgId();
        user_id = mPref.getStrUserId();
        form_id = getIntent().getStringExtra("org_form_id");

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }

        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.customForm);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(CustomFormActivity.this);
        tvHeader.setTypeface(raleway_semibold);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linCustomBody = (LinearLayout) findViewById(R.id.lin_custom_form_body);
        spComplaintStatus = (Spinner) findViewById(R.id.sp_complaint_status);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll);
        nestedScrollView.setSmoothScrollingEnabled(false);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Status);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComplaintStatus.setAdapter(dataAdapter);
        spComplaintStatus.setPrompt("Status");
        getCustomForm();
        btnSubmit.setOnClickListener(submitListener);
    }


    public void getCustomForm() {

        progressDialog.show();
        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseCustomForm> call = saasapi.GetCustomForm(new RequestCustomForm(company_id, user_id, form_id));
        call.enqueue(new Callback<ResponseCustomForm>() {
            @Override
            public void onResponse(Call<ResponseCustomForm> call, Response<ResponseCustomForm> response) {

                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().getData().get(0).getForm_fields().toString());
                    form_fieldsList = response.body().getData().get(0).getForm_fields();

                    if (Utility.isNotNull(response.body().getData().get(0).getForm_title())) {
                        tvHeader.setText(response.body().getData().get(0).getForm_title());
                    } else {
                        tvHeader.setText(R.string.customForm);
                    }

                    createCustomForm();
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseCustomForm> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();

            }
        });


    }

    public void UploadCustomForm(RequestCustomFormUpload requestCustomFormUpload) {

        progressDialog.show();
        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseCustomFormUpload> call = saasapi.CustomFormUpload(requestCustomFormUpload);
        call.enqueue(new Callback<ResponseCustomFormUpload>() {
            @Override
            public void onResponse(Call<ResponseCustomFormUpload> call, Response<ResponseCustomFormUpload> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Utility.showToast(CustomFormActivity.this, response.body().getError_message());

                    Intent inDashBoard = new Intent(CustomFormActivity.this, DashboardActivity.class);
                    inDashBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inDashBoard);
                    finish();

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(CustomFormActivity.this, error_message, new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCustomFormUpload> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
            }
        });

    }


    public void createCustomForm() {


        for (int i = 0; i < form_fieldsList.size(); i++) {
            Log.d(TAG, form_fieldsList.get(i).getField_name() + " --- " + form_fieldsList.get(i).getField_type());
            switch (form_fieldsList.get(i).getField_type()) {

                case "text":
                    createTextView(form_fieldsList.get(i).getField_label());
                    createEditText(form_fieldsList.get(i).getField_label(), form_fieldsList.get(i).getField_unique_id(), form_fieldsList.get(i).getField_isrequired());
                    createView();
                    break;

                case "dropdown":
                    createTextView(form_fieldsList.get(i).getField_label());
                    createSpinner(form_fieldsList.get(i).getField_default_values(), form_fieldsList.get(i).getField_unique_id());
                    createView();
                    break;

                case "textarea":
                    createTextView(form_fieldsList.get(i).getField_label());
                    createTextArea(form_fieldsList.get(i).getField_label(), form_fieldsList.get(i).getField_unique_id(), form_fieldsList.get(i).getField_isrequired());
                    createView();
                    break;

                case "checkbox":
                    createTextView(form_fieldsList.get(i).getField_label());
                    for (int j = 0; j < form_fieldsList.get(i).getField_default_values().size(); j++) {
                        createCheckBox(form_fieldsList.get(i).getField_default_values().get(j), form_fieldsList.get(i).getField_unique_id());
                    }
                    createView();
                    break;

                case "radio":
                    createTextView(form_fieldsList.get(i).getField_label());
                    createRadioButton(form_fieldsList.get(i).getField_default_values(), form_fieldsList.get(i).getField_unique_id());
                    createView();
                    break;

                case "image":
                    createTextView(form_fieldsList.get(i).getField_label());
                    createGridView(form_fieldsList.get(i).getField_unique_id());
                    createHintTextView(getString(R.string.display_image));
                    createImageView();
                    createView();
                    break;
            }
        }
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View view) {

            int flag = 0;
            status = spComplaintStatus.getSelectedItem().toString();
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, Object> jsonObject = new HashMap<String, Object>();
            for (View singView : allViews) {
                String className = Utility.getClassName(singView.getClass());

                if (className.equalsIgnoreCase("EditText")) {
                    EditText editText = (EditText) singView;
                    stringBuilder.append(" ").append(editText.getText().toString()).append("\n");
                    strComplaint = editText.getText().toString().trim();

                    String strTag = editText.getTag().toString();
                    String strFieldUniqueKey = strTag.substring(0, strTag.lastIndexOf("_"));
                    String strIsRequired = strTag.substring(strTag.lastIndexOf("_") + 1, strTag.length());

                    if (strIsRequired.equalsIgnoreCase("yes")) {

                        if (Utility.isNotNull(strComplaint)) {
                            jsonObject.put(strFieldUniqueKey, strComplaint);
                        } else {
                            flag = 1;
                            Utility.showAlert(CustomFormActivity.this, "Please Enter " + strFieldUniqueKey);
                            break;
                        }
                    }

                } else if (className.equalsIgnoreCase("Spinner")) {
                    Spinner spiner = (Spinner) singView;
                    stringBuilder.append(" ").append(spiner.getSelectedItem()).append("\n");
                    jsonObject.put(spiner.getTag().toString(), spiner.getSelectedItem());
                } else if (className.equalsIgnoreCase("CheckBox")) {
                    CheckBox checkBox = (CheckBox) singView;
                    if (checkBox.isChecked()) {
                        stringBuilder.append(" ").append(checkBox.getText()).append("\n");
                        stringList.add(checkBox.getText().toString());
                    }
                    jsonObject.put(checkBox.getTag().toString(), stringList);
                } else if (className.equalsIgnoreCase("RadioGroup")) {
                    RadioGroup radioGroup = (RadioGroup) singView;
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedId);
                    stringBuilder.append(" ").append(radioButton.getText()).append("\n");
                    jsonObject.put(radioGroup.getTag().toString(), radioButton.getText());

                } else if (className.equalsIgnoreCase("ImageView")) {
                    /*GridView gridView = (GridView) singView;*/
                    jsonObject.put(gridView.getTag().toString(), name);
                }

                responseCustomFormUpload = new RequestCustomFormUpload();
                responseCustomFormUpload.company_id = company_id;
                responseCustomFormUpload.form_id = getIntent().getStringExtra("org_form_id");
                responseCustomFormUpload.user_id = user_id;
                responseCustomFormUpload.complaint_id = getIntent().getStringExtra("complaint_id");
                responseCustomFormUpload.complaint_code = getIntent().getStringExtra("company_code");
                responseCustomFormUpload.complaint_status = status;
                responseCustomFormUpload.form_body = jsonObject;
            }

            if (flag == 0) {
                if (!status.equalsIgnoreCase("Status")) {
                    if (images.size() > 0) {
                        if (Utility.isOnline(CustomFormActivity.this)) {
                            new uploadImage().execute();
                        } else {
                            Utility.showAlert(CustomFormActivity.this, getString(R.string.msgNoInternet));
                        }
                    } else {
                        if (Utility.isOnline(CustomFormActivity.this)) {
                            UploadCustomForm(responseCustomFormUpload);
                        } else {
                            Utility.showAlert(CustomFormActivity.this, getString(R.string.msgNoInternet));
                        }


                    }
                } else {
                    Utility.showAlert(CustomFormActivity.this, getString(R.string.msgStatusNull));
                }
            }
        }
    };

    //---------- EDITTEXT ----------//
    public void createEditText(String hint, String tag, String isRequired) {
        EditText editText = new EditText(this);
        editText.setId(viewsCount++);
        editText.setGravity(Gravity.TOP | Gravity.START);
        editText.setHeight(100);
        editText.setHint(hint);
        editText.setBackgroundResource(R.drawable.et_comp_fillup_border);
        editText.setPadding(15, 0, 0, 0);
        editText.setMaxLines(1);
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setVerticalScrollBarEnabled(true);
        editText.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(100)
        });
        editText.setSingleLine(true);
        editText.setTag(tag + "_" + isRequired);
        editText.setOnTouchListener(this);
        allViews.add(editText);
        linCustomBody.addView(editText, params);
    }


    //----------  EDITTEXT ----------//
    public void createTextArea(String hint, String tag, String isRequired) {
        EditText editText = new EditText(this);
        editText.setId(viewsCount++);
        editText.setHeight(200);
        editText.setHint(hint);
        editText.setGravity(Gravity.TOP | Gravity.START);
        editText.setBackgroundResource(R.drawable.et_comp_fillup_border);
        editText.setPadding(15, 0, 0, 0);
        editText.setMaxLines(5);
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setVerticalScrollBarEnabled(true);
        editText.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            editText.setNestedScrollingEnabled(true);
        }
        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(500)
        });
        editText.setTag(tag + "_" + isRequired);
        editText.setOnTouchListener(this);
        allViews.add(editText);
        linCustomBody.addView(editText, params);
    }

    //----------  TextView ----------//
    public void createTextView(String hint) {
        TextView textView1 = new TextView(this);
        textView1.setId(viewsCount++);
        textView1.setPadding(5, 5, 5, 5);
        textView1.setText(hint);
        textView1.setTextSize(20f);
        textView1.setTypeface(TypefaceUtils.RalewayMedium(CustomFormActivity.this));
        textView1.setPadding(15, 0, 0, 0);
        textView1.setTextColor(ContextCompat.getColor(CustomFormActivity.this, R.color.colorPrimary));
        allViews.add(textView1);
        linCustomBody.addView(textView1, params);
    }

    //----------  TextView ----------//
    public void createHintTextView(String hint) {
        textView = new TextView(this);
        textView.setId(viewsCount++);
        textView.setMinimumHeight(100);
        textView.setBackgroundResource(R.drawable.stylishimageborder);
        textView.setText(hint);
        textView.setTextSize(15f);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(TypefaceUtils.RalewaySemiBold(CustomFormActivity.this));
        textView.setPadding(15, 0, 0, 0);
        textView.setTextColor(ContextCompat.getColor(CustomFormActivity.this, R.color.text_black));
        allViews.add(textView);
        linCustomBody.addView(textView, params);
    }


    //----------  SPINNER ----------//
    public void createSpinner(List<String> spinnerList, String tag) {
        Spinner spinner = new Spinner(this);
        spinner.setId(viewsCount++);
        spinner.setBackgroundResource(R.drawable.sp_complaint_fillup_border);
        spinner.setPadding(15, 0, 0, 0);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setTag(tag);
        allViews.add(spinner);
        linCustomBody.addView(spinner, params);
    }


    //---------- CHECKBOX ----------//
    public void createCheckBox(final String label, String tag) {
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setId(viewsCount++);
        checkBox.setText(label);
        checkBox.setPadding(5, 5, 5, 5);
        checkBox.setTag(tag);
        allViews.add(checkBox);
        linCustomBody.addView(checkBox, params);
    }


    //---------- RADIOBUTTON ----------//
    private void createRadioButton(List<String> radioList, String tag) {
        final RadioButton[] rb = new RadioButton[5];
        RadioGroup rg = new RadioGroup(this);
        rg.setPadding(5, 5, 5, 5);
        rg.setBackgroundResource(R.drawable.et_comp_fillup_border);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        rg.setSelected(true);
        for (int i = 0; i < radioList.size(); i++) {
            rb[i] = new RadioButton(this);
            rb[i].setText(radioList.get(i));
            rb[i].setId(i + 100);
            rb[0].setChecked(true);
            rg.addView(rb[i]);
        }
        rg.setTag(tag);
        allViews.add(rg);
        linCustomBody.addView(rg, params);
    }

    //---------- GRIDVIEW ----------//
    private void createGridView(String tag) {
        gridView = new GridView(this);
        gridView.setNumColumns(3);
        gridView.setBackgroundResource(R.drawable.stylishimageborder);
        gridView.setTag(tag);
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (images.size() < 3) {
                    OpenImagePicker();
                } else {
                    Utility.showToast(CustomFormActivity.this, getString(R.string.msgMaxExpensesImageupload));
                }
            }
        });
    }

    //---------- LINEVIEW ----------//
    public void createView() {
        View view = new View(this);
        view.setMinimumHeight(2);
        view.setBackgroundColor(ContextCompat.getColor(CustomFormActivity.this, R.color.colorDivider));
        allViews.add(view);
        linCustomBody.addView(view, params);
    }


    //OPEN CAMERA
    private void openCamera() {
        String fileName = "SAAS" + DateTimeUtils.getCurrentDateTimeMix() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by Camera on CONECT");
        fileUri = CustomFormActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, Constant.REQUEST_FOR_CAMERA);
    }

    //OPEN GALLERY
    public void openGallery() {

        Intent gintent = new Intent();

        gintent.setType("image/*");
        gintent.putExtra("outputX", 120);
        gintent.putExtra("outputY", 120);
        gintent.putExtra("aspectX", 1);
        gintent.putExtra("aspectY", 1);
        gintent.putExtra("scale", true);

        gintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gintent, CustomFormActivity.this.getString(R.string.image_open_gallary_title)), Constant.REQUEST_FOR_GALLERY);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constant.REQUEST_FOR_CAMERA:

                if (resultCode == Activity.RESULT_OK) {
                    try {

                        InputStream inputStream = this.getContentResolver().openInputStream(fileUri);

                        Uri selectedImageURI = fileUri;
                        File imageFile = new File(getPath(CustomFormActivity.this.getApplicationContext(), selectedImageURI));

                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        bitmap = ((BitmapDrawable) d).getBitmap();
                        FileOutputStream out = openFileOutput("imagename", Context.MODE_PRIVATE);
                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "SAAS" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".jpeg";
                        File f = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + filename);

                        if (!f.exists())
                            f.createNewFile();

                        // write the bytes in file
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                        String filePath = f.getAbsolutePath();
                        long file_size = (f.length() / 1024);
                        String img_name = filePath.substring(filePath.lastIndexOf("/") + 1);


                        if (file_size < MAXIMUM_UPLOAD_FILE_SIZE) {
                            images.add(bitmap);
                            ImageAdapter img = new ImageAdapter(this);
                            int gvsize = img.getCount();
                            Log.e("gridview", gvsize + "");
                            gridView.setAdapter(img);
                            gridView.setVisibility(View.VISIBLE);

                            if (Utility.isNotNull(img_name)) {

                                pathfordb.add(img_name);
                                textView.setVisibility(View.GONE);
                            } else
                                for (int i = 0; i < pathfordb.size(); i++) {
                                    String pathImage = pathfordb.get(i);
                                    textView.setVisibility(View.GONE);
                                }
                        } else {
                            Utility.showAlert(this, getString(R.string.upload_file_size_large_msg));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (gridView.getChildCount() == 0)
                        textView.setVisibility(View.VISIBLE);
                }
                break;

            case Constant.REQUEST_FOR_GALLERY:

                if (resultCode == Activity.RESULT_OK) {

                    try {

                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());

                        Uri selectedImageURI = data.getData();
                        File imageFile = new File(getPath(CustomFormActivity.this.getApplicationContext(), selectedImageURI));

                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        bitmap = ((BitmapDrawable) d).getBitmap();
                        FileOutputStream out = openFileOutput("imagename", Context.MODE_PRIVATE);
                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "SAAS" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".jpeg";
                        File f = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + filename);

                        if (!f.exists())
                            f.createNewFile();

                        // write the bytes in file
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                        String filePath = f.getAbsolutePath();
                        long file_size = (f.length() / 1024);
                        String img_name = filePath.substring(filePath.lastIndexOf("/") + 1);


                        if (file_size < MAXIMUM_UPLOAD_FILE_SIZE) {
                            images.add(bitmap);
                            ImageAdapter img = new ImageAdapter(this);
                            int gvsize = img.getCount();
                            Log.e("gridview", gvsize + "");
                            gridView.setAdapter(img);
                            gridView.setVisibility(View.VISIBLE);

                            if (Utility.isNotNull(img_name)) {

                                pathfordb.add(img_name);
                                textView.setVisibility(View.GONE);
                            } else
                                for (int i = 0; i < pathfordb.size(); i++) {
                                    String pathImage = pathfordb.get(i);

                                }
                        } else {
                            Utility.showAlert(this, getString(R.string.upload_file_size_large_msg));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    if (gridView.getChildCount() == 0)
                        textView.setVisibility(View.VISIBLE);
                }
                break;

        }
    }


    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            v.getParent().requestDisallowInterceptTouchEvent(true);

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            v.getParent().requestDisallowInterceptTouchEvent(false);
        }

        return false;
    }

    public class uploadImage extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            complete = 0;
            for (int i = 0; i <= pathfordb.size(); i++) {
                if (i < pathfordb.size()) {
                    complete = i;

                    File file = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + pathfordb.get(i));
                    // MultipartBody.Part is used to send also the actual file name
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("file_image", pathfordb.get(i), requestBody);


                    try {
                        SAASApi ftsInterface = Utility.saasapi(getApplicationContext());
                        Call<ResponseUploadImage> call = ftsInterface.uploadComplaintImage(body);

                        Response<ResponseUploadImage> responseBody = call.execute();
                        if (responseBody.isSuccessful()) {
                            name.add(responseBody.body().getData());
                        } else {

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (Utility.isOnline(CustomFormActivity.this)) {
                UploadCustomForm(responseCustomFormUpload);
            } else {
                Utility.showAlert(CustomFormActivity.this, getString(R.string.msgNoInternet));
            }

        }
    }

    public void OpenImagePicker() {
        if (Utility.isOnline(CustomFormActivity.this)) {
            String file_name = "saas" + ".jpg";

            ContentValues value = new ContentValues();
            value.put(MediaStore.Images.Media.TITLE, file_name);

            Uri imageurl = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);

            path = String.valueOf(imageurl) + ", " + path;

            fileUri = Utility.getOutputMediaFileUri("SAAS");
            assert fileUri != null;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Image Source");
            builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //GET IMAGE FROM GALLERY.
                                    openGallery();
                                    break;

                                case 1:
                                    //GET IMAGE FROM CAMERA.
                                    openCamera();
                                    break;

                                default:
                                    break;
                            }
                        }
                    });

            builder.show();

        } else {
//                    Utility.showToast(ComplaintFillupActivity.this,R.string.msgNoInternet);
        }
    }

    public class ImageAdapter extends BaseAdapter {

        public android.app.AlertDialog Add_popup;

        Context context;
        private LayoutInflater inflater = null;

        public ImageAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            ImageView img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Holder holder = new Holder();
            View rowView;


            rowView = inflater.inflate(R.layout.image_adepter_row, null);
            holder.img = rowView.findViewById(R.id.image_row);
            holder.img.setImageBitmap(images.get(position));


            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popup_dialog(images.get(position), position);
                }
            });

            return rowView;
        }

        public void popup_dialog(final Bitmap bitmap, final int position) {
            LayoutInflater factory = LayoutInflater.from(context);
            final View dialogview = factory.inflate(R.layout.custom_image_popup, null);

            final ImageView iv_popup = dialogview.findViewById(R.id.iv_popup);
            final Button iv_cancel = dialogview.findViewById(R.id.iv_cancel);
            final Button iv_remove = dialogview.findViewById(R.id.iv_remove);

            iv_popup.setImageBitmap(bitmap);
            Add_popup = new android.app.AlertDialog.Builder(context).create();
            Add_popup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Add_popup.setView(dialogview);


            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_popup.setImageBitmap(null);
                    Add_popup.dismiss();
                }
            });

            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    iv_popup.setImageBitmap(null);
                    images.remove(images.indexOf(bitmap));
                    pathfordb.remove(pathfordb.get(position));
                    notifyDataSetChanged();
                    Add_popup.dismiss();
                }
            });
            Add_popup.show();


        }

    }

}
