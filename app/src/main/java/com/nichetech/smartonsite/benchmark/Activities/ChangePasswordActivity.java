package com.nichetech.smartonsite.benchmark.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.nichetech.smartonsite.benchmark.Common.Preferences;
import com.nichetech.smartonsite.benchmark.Common.Utility;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.RequestClass.RequestChangePassword;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Preferences preferences;
    private Utility utility;
    private Toolbar toolbar;

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private ImageView ivBack;
    private Button btnSubmit;
    private String strOldPassword, strNewPassword, strConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        preferences = new Preferences(this);
        utility = new Utility(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        }


        ivBack = (ImageView) findViewById(R.id.ivBack);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnSubmit = (Button) findViewById(R.id.btn_submit);


        btnSubmit.setOnClickListener(this);
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
        Utility.hideKeyboard(ChangePasswordActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (Utility.isOnline(ChangePasswordActivity.this)) {
                    if (checkValidation()) {

                        RequestChangePassword requestChangePassword = new RequestChangePassword();
                        requestChangePassword.company_id = preferences.getStrUserOrgId();
                        requestChangePassword.user_id = preferences.getStrUserId();
                        requestChangePassword.old_password = strOldPassword;
                        requestChangePassword.new_password = strNewPassword;

                        Log.e("PASSWORD :==> ", "" + requestChangePassword.new_password);

                        if (Utility.isOnline(ChangePasswordActivity.this)) {
                            wsChangePassword(requestChangePassword);
                        } else {
                            Utility.showAlert(ChangePasswordActivity.this, getString(R.string.msgNoInternet));
                        }

                    }

                } else {
                    Utility.showAlert(ChangePasswordActivity.this, getString(R.string.msgNoInternet));
                }
                break;
        }
    }


    // WS - ChangePassword
    private void wsChangePassword(RequestChangePassword requestChangePassword) {
        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.msgPleasewait));
        progressDialog.show();

        SAASApi saasapi = Utility.saasapi(getApplicationContext());
        Call<RequestChangePassword> call = saasapi.changePassword(requestChangePassword);
        call.enqueue(new Callback<RequestChangePassword>() {
            @Override
            public void onResponse(Call<RequestChangePassword> call, Response<RequestChangePassword> response) {
                try {
                    if (response.isSuccessful()) {
                        Utility.showAlert(ChangePasswordActivity.this, "Password changed", new Utility.OnOkClickListener() {
                            @Override
                            public void onOkClick() {
                                onBackPressed();
                            }
                        });
                    } else {
                        int errorCode = 0;
                        String errorMessage = null;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorMessage = jObjError.optString(Utility.ErrorCode.ERROR_MESSAGE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (Utility.isNotNull(errorMessage)) {
                            Utility.showAlert(ChangePasswordActivity.this, errorMessage);
                        } else {
                            Utility.showAlert(ChangePasswordActivity.this, String.valueOf(R.string.msgServerNotConnect));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showToast(ChangePasswordActivity.this, String.valueOf(R.string.msgServerNotConnect));
                } finally {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RequestChangePassword> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private boolean checkValidation() {

        strOldPassword = etOldPassword.getText().toString().trim();
        strNewPassword = etNewPassword.getText().toString().trim();
        strConfirmPassword = etConfirmPassword.getText().toString().trim();

        if (!Utility.isNotNull(strOldPassword)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgOldPasswordNull));
            requestFocus(etOldPassword);
            return false;
        } else if (!(strOldPassword.length() >= 5 && strOldPassword.length() <= 15)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgOldPasswordInvald));
            requestFocus(etOldPassword);
            return false;
        } else if (!Utility.isNotNull(strNewPassword)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgNewPasswordNull));
            requestFocus(etNewPassword);
            return false;
        } else if (!(strNewPassword.length() >= 5 && strNewPassword.length() <= 15)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgNewPasswordInvalid));
            requestFocus(etNewPassword);
            return false;
        } else if (strNewPassword.equalsIgnoreCase(strOldPassword)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgOldNewSame));
            requestFocus(etNewPassword);
            return false;
        } else if (!Utility.isNotNull(strConfirmPassword)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgConfirmPasswordNull));
            requestFocus(etConfirmPassword);
            return false;
        } else if (!(strConfirmPassword.length() >= 5 && strConfirmPassword.length() <= 15)) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgConfirmPasswordInvalid));
            requestFocus(etConfirmPassword);
            return false;
        } else if (!(strConfirmPassword.equals(strNewPassword))) {
            Utility.showToast(ChangePasswordActivity.this, getString(R.string.msgConfirmPasswordNotSame));
            requestFocus(etConfirmPassword);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {

        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }
}
