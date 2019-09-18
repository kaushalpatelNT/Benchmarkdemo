package com.nichetech.smartonsite.benchmark.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.nichetech.smartonsite.benchmark.Common.TypefaceUtils;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.ResponseClass.ResponseCommon;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private String TAG = "ForgotPassword";
    private Utility utility;

    private EditText etMobileNumber;
    private Button btnLogin;
    private TextView tvLogin;
    private String strMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        utility = new Utility(this);

        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);

        tvLogin = (TextView) findViewById(R.id.tvLogin);

        btnLogin = (Button) findViewById(R.id.btnSubmit);

        etMobileNumber.setTypeface(TypefaceUtils.MyriadProRegular(this));
        tvLogin.setTypeface(TypefaceUtils.MyriadProRegular(this));
        btnLogin.setTypeface(TypefaceUtils.MyriadProRegular(this));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(ForgotPasswordActivity.this);
                if (Utility.isOnline(ForgotPasswordActivity.this)) {

                    if (ValidationCheck()) {

                        forgotPassword();
                    }

                } else {
                    Utility.showAlert(ForgotPasswordActivity.this, getString(R.string.msgNoInternet));
                }

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    public boolean ValidationCheck() {

        strMobileNo = etMobileNumber.getText().toString().trim();

        if (!Utility.isNotNull(strMobileNo)) {
            Utility.showToast(ForgotPasswordActivity.this, getString(R.string.msgMobileNoNull));
            requestFocus(etMobileNumber);
            return false;

        } else if (strMobileNo.length() != 10) {
            Utility.showToast(ForgotPasswordActivity.this, getString(R.string.msgMobileNoInvalid));
            requestFocus(etMobileNumber);
            return false;
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void forgotPassword() {

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());

        Call<ResponseCommon> call = saasapi.ForgotPassword(strMobileNo);
        call.enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {

                Log.d(TAG, response.code() + "");

                if (response.isSuccessful()) {

                    if (response.body().getError_code() == 1) {

                        if (progressDialog.isShowing()) progressDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setMessage(response.body().getError_message())
                                .setPositiveButton(getString(R.string.strOk), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        onBackPressed();
                                    }
                                });

                        builder.show();

                    } else {
                        Utility.showAlert(ForgotPasswordActivity.this, response.body().getError_message());
                    }

                } else {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String error_message = error.getString("error_message");
                        Utility.showAlert(ForgotPasswordActivity.this, error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
                Utility.showToast(ForgotPasswordActivity.this, getString(R.string.msgServerNotConnect));

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(ForgotPasswordActivity.this);
    }
}

