package com.nichetech.smartonsite.benchmark.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nichetech.smartonsite.benchmark.Adapter.PartsListAdapter;
import com.nichetech.smartonsite.benchmark.Common.*;
import com.nichetech.smartonsite.benchmark.Data.UseParts;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestComplaintFillup;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestParts;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestSubmitParts;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseComplainFillup;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponsePart;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComplaintFillupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = ComplaintFillupActivity.class.getSimpleName();
    private Preferences preferences;
    private ImageView ivHeader, ivUpload, ivAddParts;
    private TextView tvComplaintDescription, tvHeader;
    private Spinner mSp;
    private String Status[] = {"Status", "Completed", "Pending"};
    private Button btnSubmit;
    private Uri fileUri;
    private String path;
    private List<String> name = new ArrayList<>();
    private ProgressDialog progressDialog;
    private GridView imagegrid;
    private TextView imagehint;
    private ArrayList<String> pathfordb = new ArrayList<>();
    private ArrayList<Bitmap> images = new ArrayList<>();
    private EditText etComplaintfillupDescription, etLaborCharge;
    private String Description, status, LabourCharge;
    private int complete = 0;
    private Bitmap bitmap;
    public static int MAXIMUM_UPLOAD_FILE_SIZE = 1024 * 5;

    private String complain_id = "";
    private Spinner spCompanyRemark;

    private ArrayList<UseParts> usePartsArrayList = new ArrayList<>();
    private ArrayList<ResponsePart.Parts> usePartsList = new ArrayList<>();
    private ArrayList<RequestSubmitParts> list = new ArrayList<>();
    private RecyclerView rvPartsList;
    private PartsAdapter partsAdapter;
    private EditText edOtherReason;

    private RadioGroup rdgReceivedPayment;
    private int PaymentReceived=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_fillup);

        preferences = new Preferences(this);

        if (getIntent().hasExtra("complaint_id")) {
            complain_id = getIntent().getStringExtra("complaint_id");
        }

        progressDialog = new ProgressDialog(ComplaintFillupActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));

        tvHeader = (TextView) findViewById(R.id.tv_title);
        tvHeader.setText(R.string.header_complaint_fillup);

        Typeface raleway_semibold = TypefaceUtils.RalewaySemiBold(ComplaintFillupActivity.this);
        tvHeader.setTypeface(raleway_semibold);

        tvComplaintDescription = (TextView) findViewById(R.id.et_complaintfillup_Description);
        ivHeader = (ImageView) findViewById(R.id.iv_back);
        etComplaintfillupDescription = (EditText) findViewById(R.id.et_complaintfillup_Description);
        etLaborCharge = findViewById(R.id.et_labor_charge);
        ivUpload = (ImageView) findViewById(R.id.iv_upload);
        ivUpload.setOnClickListener(this);
        ivAddParts = findViewById(R.id.iv_add_parts);
        ivAddParts.setOnClickListener(this);

        spCompanyRemark = findViewById(R.id.spCompanyRemark);

        imagegrid = (GridView) findViewById(R.id.gv_images);
        imagehint = (TextView) findViewById(R.id.imagehint);

        rdgReceivedPayment=findViewById(R.id.rdgPaymentReceived);

        edOtherReason = (EditText) findViewById(R.id.edOtherReason);

        getGetPart();

        usePartsArrayList.add(new UseParts("", ""));

        rvPartsList = findViewById(R.id.rvParts);
        rvPartsList.setLayoutManager(new LinearLayoutManager(this));
        partsAdapter = new PartsAdapter(this);
        rvPartsList.setAdapter(partsAdapter);


       /* partsAdapter.setOnClickListener(new PartsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                usePartsArrayList.remove(position);
                partsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemSelect(int position) {

            }
        });*/


        Typeface raleway_medium = TypefaceUtils.RalewayMedium(ComplaintFillupActivity.this);
        tvComplaintDescription.setTypeface(raleway_medium);

        mSp = (Spinner) findViewById(R.id.sp_complaint_fillup);
        btnSubmit = (Button) findViewById(R.id.btn_fillup_submit);
        btnSubmit.setOnClickListener(this);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Status);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp.setAdapter(dataAdapter);
        mSp.setPrompt("Status");
        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                } else if (i == 1) {
                    status = "Completed";
                } else if (i == 2) {
                    status = "Pending";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rdgReceivedPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdbYes:
                        PaymentReceived=1;
                        break;
                    case R.id.rdbNo:
                        PaymentReceived=0;
                        break;
                }
            }
        });

        spCompanyRemark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spCompanyRemark.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                    edOtherReason.setVisibility(View.VISIBLE);
                } else {
                    edOtherReason.setText("");
                    edOtherReason.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

        imagegrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                (v.getParent()).requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public Boolean checkValidation() {

        if (spCompanyRemark.getSelectedItem().toString().equalsIgnoreCase("Other")) {
            Description = edOtherReason.getText().toString().trim();
        } else {
            Description = spCompanyRemark.getSelectedItem().toString();
        }

        LabourCharge = etLaborCharge.getText().toString().trim();

        if (!Utility.isNotNull(Description)) {
            Utility.showAlert(ComplaintFillupActivity.this, getString(R.string.msgDescriptionNull));
            return false;
        } else if (!Utility.isNotNull(status)) {
            Utility.showAlert(ComplaintFillupActivity.this, getString(R.string.msgStatusNull));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_fillup_submit:
                Description = etComplaintfillupDescription.getText().toString();
                Log.e("", "" + Description);


                if (Utility.isOnline(ComplaintFillupActivity.this)) {
                    if (checkValidation()) {

                        if (images.size() > 0) {
                            if (Utility.isOnline(ComplaintFillupActivity.this)) {
                                new uploadImage().execute();
                            } else {
                                Utility.showAlert(ComplaintFillupActivity.this, getString(R.string.msgNoInternet));
                            }

                        } else {
                            if (Utility.isOnline(ComplaintFillupActivity.this)) {
                                wsComplaintFillup();
                            } else {
                                Utility.showAlert(ComplaintFillupActivity.this, getString(R.string.msgNoInternet));
                            }

                        }


                    }
                } else {
                    Utility.showToast(ComplaintFillupActivity.this, R.string.msgNoInternet);
                }


                break;
            case R.id.iv_upload:


                Utility.hideKeyboard(this);

                try {
                    if (ContextCompat.checkSelfPermission(ComplaintFillupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(ComplaintFillupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(ComplaintFillupActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        if (images.size() < 3) {
                            OpenImagePicker();
                        } else {
                            Utility.showToast(ComplaintFillupActivity.this, getString(R.string.msgMaxExpensesImageupload));
                        }

                    } else {
                        ActivityCompat.requestPermissions(ComplaintFillupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.iv_add_parts:
                refreshList();
                break;

        }

    }

    public void refreshList() {

        usePartsArrayList.add(new UseParts("", ""));
        partsAdapter.notifyDataSetChanged();

    }

    //OPEN CAMERA
    private void openCamera() {
        String fileName = "SAAS" + DateTimeUtils.getCurrentDateTimeMix() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by Camera on CONECT");
        fileUri = ComplaintFillupActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
        startActivityForResult(gintent, Constant.REQUEST_FOR_GALLERY);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constant.REQUEST_FOR_CAMERA:

                if (resultCode == Activity.RESULT_OK) {
                    try {

                        InputStream inputStream = this.getContentResolver().openInputStream(fileUri);

                        Uri selectedImageURI = fileUri;
                        File imageFile = new File(getPath(ComplaintFillupActivity.this.getApplicationContext(), selectedImageURI));

                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        bitmap = ((BitmapDrawable) d).getBitmap();
                        FileOutputStream out = openFileOutput("imagename", Context.MODE_PRIVATE);
                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "SAAS" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".png";
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
                            imagegrid.setAdapter(img);
                            imagegrid.setVisibility(View.VISIBLE);

                            if (Utility.isNotNull(img_name)) {

                                pathfordb.add(img_name);
                                imagehint.setVisibility(View.GONE);
                            } else
                                for (int i = 0; i < pathfordb.size(); i++) {
                                    String pathImage = pathfordb.get(i);
                                    imagehint.setVisibility(View.GONE);
                                }
                        } else {
                            Utility.showAlert(this, getString(R.string.upload_file_size_large_msg));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (imagegrid.getChildCount() == 0)
                        imagehint.setVisibility(View.VISIBLE);
                }
                break;

            case Constant.REQUEST_FOR_GALLERY:

                if (resultCode == Activity.RESULT_OK) {

                    try {

                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());

                        Uri selectedImageURI = data.getData();
                        File imageFile = new File(getPath(ComplaintFillupActivity.this.getApplicationContext(), selectedImageURI));

                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        bitmap = ((BitmapDrawable) d).getBitmap();
                        FileOutputStream out = openFileOutput("imagename", Context.MODE_PRIVATE);
                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "SAAS" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".png";
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
                            imagegrid.setAdapter(img);
                            imagegrid.setVisibility(View.VISIBLE);

                            if (Utility.isNotNull(img_name)) {

                                pathfordb.add(img_name);
                                imagehint.setVisibility(View.GONE);
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
                    if (imagegrid.getChildCount() == 0)
                        imagehint.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    private void getGetPart() {

        final ProgressDialog progressDialog = new ProgressDialog(ComplaintFillupActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();


        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponsePart> call = saasapi.getParts(new RequestParts(complain_id));
        call.enqueue(new Callback<ResponsePart>() {
            @Override
            public void onResponse(Call<ResponsePart> call, Response<ResponsePart> response) {

                if (progressDialog.isShowing()) progressDialog.dismiss();
                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        usePartsList = response.body().getData();

                        partsAdapter = new PartsAdapter(ComplaintFillupActivity.this);
                        rvPartsList.setAdapter(partsAdapter);

                    } else {
                        Utility.showAlert(ComplaintFillupActivity.this, response.body().getError_message());
                    }

                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(ComplaintFillupActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePart> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(ComplaintFillupActivity.this, getString(R.string.msgServerNotConnect));

            }
        });

    }

    // ---------  wsComplaintFillup Call--------------//
    public void wsComplaintFillup() {

        progressDialog.show();
        String imageName = "";
        if (name.size() > 0) {
            for (int i = 0; i < name.size(); i++) {
                if (i < name.size() - 1) {
                    imageName += name.get(i) + ",";
                } else {
                    imageName += name.get(i);
                }
            }
        }


        for (int i = 0; i < usePartsArrayList.size(); i++) {


            list.add(new RequestSubmitParts("0", complain_id, usePartsArrayList.get(i).getPartsID(), usePartsArrayList.get(i).getPartQty(), "1", "0"));

        }

        if(PaymentReceived==0 && status.equalsIgnoreCase("Completed")) {
            status="Closed";
        }

        RequestComplaintFillup requestComplaintFillup = new RequestComplaintFillup();
        requestComplaintFillup.ComplainID = getIntent().getStringExtra("complaint_id");
        requestComplaintFillup.CompanyRemark = Description;
        requestComplaintFillup.ComplainStatus = status;
        requestComplaintFillup.Labourcharge = LabourCharge;
        requestComplaintFillup.lstAddUpdatePartUsedForComplainModal = list;
        requestComplaintFillup.ImageNames = imageName;

        SAASApi saasapi = Utility.saasapi(getApplicationContext());
        Call<ResponseComplainFillup> call = saasapi.ComplaintFillUp(requestComplaintFillup);
        call.enqueue(new Callback<ResponseComplainFillup>() {
            @Override
            public void onResponse(Call<ResponseComplainFillup> call, Response<ResponseComplainFillup> response) {
                try {
                    if (response.isSuccessful()) {
                        Utility.showAlert(ComplaintFillupActivity.this, response.body().getError_message(), new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                Intent inDashBoard = new Intent(ComplaintFillupActivity.this, DashboardActivity.class);
                                inDashBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(inDashBoard);
                                finish();
                            }
                        });

                    } else {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        int errorCode = 0;
                        String errorMessage = null;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorMessage = jObjError.optString(Utility.ErrorCode.ERROR_MESSAGE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (Utility.isNotNull(errorMessage)) {
                            Utility.showAlert(ComplaintFillupActivity.this, response.body().getError_message(), new Utility.OnOkClickListener() {
                                @Override
                                public void onOkClick() {
                                    progressDialog.dismiss();
                                }
                            });
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                        } else {
                            Utility.showToast(ComplaintFillupActivity.this, R.string.msgServerNotConnect);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showToast(ComplaintFillupActivity.this, R.string.msgServerNotConnect);
                } finally {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseComplainFillup> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
            }
        });
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


            if (Utility.isOnline(ComplaintFillupActivity.this)) {
                wsComplaintFillup();
            } else {
                Utility.showAlert(ComplaintFillupActivity.this, getString(R.string.msgNoInternet));
            }

        }
    }


    public void OpenImagePicker() {
        if (Utility.isOnline(ComplaintFillupActivity.this)) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                OpenImagePicker();

            } else {
                Utility.showAlert(ComplaintFillupActivity.this, getString(R.string.msgPermission));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        super.onStop();
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

    public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.partHolder> {


        public Context mContext;


        public PartsAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public PartsAdapter.partHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parts, parent, false);
            return new PartsAdapter.partHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PartsAdapter.partHolder holder, final int position) {

            holder.atParts.setAdapter(new PartsListAdapter(mContext, R.layout.item_spinner, usePartsList));

            holder.atParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Print Log", "" + id);
                    usePartsArrayList.set(holder.getAdapterPosition(), new UseParts(String.valueOf(id), usePartsArrayList.get(holder.getAdapterPosition()).getPartQty()));

                }
            });

            holder.edQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().trim().isEmpty()) {
                        usePartsArrayList.set(holder.getAdapterPosition(), new UseParts(usePartsArrayList.get(holder.getAdapterPosition()).getPartsID(), s.toString().trim()));
                    }
                }
            });

       /* holder.spParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        itemClickListener.onItemSelect(position);
            }
        });*/

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usePartsArrayList.remove(holder.getAdapterPosition());
                    partsAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return usePartsArrayList.size();
        }


        public class partHolder extends RecyclerView.ViewHolder {

            public AutoCompleteTextView atParts;
            public TextView tvPartPrice;
            public EditText edQty;
            public ImageView ivDelete;

            public partHolder(@NonNull View itemView) {
                super(itemView);

                tvPartPrice = itemView.findViewById(R.id.tvPartPrice);
                atParts = itemView.findViewById(R.id.atParts);
                edQty = itemView.findViewById(R.id.edQty);
                ivDelete = itemView.findViewById(R.id.ivDelete);
            }
        }


    }

}

