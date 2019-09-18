package com.nichetech.smartonsite.benchmark.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.nichetech.smartonsite.benchmark.BuildConfig;

/**
 * Created by kaushal on 6/12/16.
 */

public class Preferences {

    private Context mContext;
    private SharedPreferences pref;

    public String distance;


    private static final String COMPANY_CODE = BuildConfig.PREF_PREFIX + "companyId";
    private static final String USER_CODE = BuildConfig.PREF_PREFIX + "userCode";
    private static final String USER_NAME = BuildConfig.PREF_PREFIX + "userName";
    private static final String USER_ID = BuildConfig.PREF_PREFIX + "userId";
    private static final String USER_ORG_ID = BuildConfig.PREF_PREFIX + "userOrgId";
    private static final String COMPANY_ID = BuildConfig.PREF_PREFIX + "companyId";
    private static final String USER_TRIP_ID = BuildConfig.PREF_PREFIX + "user_trip_id";
    private static final String USER_CLIENT_NAME = BuildConfig.PREF_PREFIX + "user_client_name";
    private static final String USER_CLIENT_NUMBER = BuildConfig.PREF_PREFIX + "user_client_number";
    private static final String USER_TRIP_DISTANCE = BuildConfig.PREF_PREFIX + "user_trip_distance";
    private static final String LATTITUDE = BuildConfig.PREF_PREFIX + "lattitude";
    private static final String LONGITUDE = BuildConfig.PREF_PREFIX + "longitude";
    private static final String LOCATION = BuildConfig.PREF_PREFIX + "location";
    private static final String FILENAME = BuildConfig.PREF_PREFIX + "filename";
    private static final String TOKEN = BuildConfig.PREF_PREFIX + "token";


    private String strCompanyCode, strUserCode, strUserName, strUserId, strUserOrgId, strCompanyId, strUserTripId, strUserClientName, strUserClientNumber,strToken;
    private float flUserTripDistance;
    public String lattitude, longitude;
    private String location,fileName;

    public Preferences(Context context) {
        this.mContext = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        refreshStrCompanyCode();
        refreshStrUserCode();
        refreshStrUserName();
        refreshStrUserId();
        refreshStrToken();
        refreshstrCompanyId();
        refreshStrUserOrgId();
        refreshstrUserTripId();
        refreshstrUserClientName();
        refreshstrUserClientNumber();
        refreshFlUserTripDistance();
        refreshFileName();
    }

    public void refreshStrCompanyCode() {
        strCompanyCode = pref.getString(COMPANY_CODE, "");
    }

    public void refreshFileName() {
        fileName = pref.getString(FILENAME, "");
    }


    public String getStrCompanyCode() {
        return strCompanyCode;
    }

    public void setStrCompanyCode(String strCompanyCode) {
        this.strCompanyCode = strCompanyCode;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(COMPANY_CODE, strCompanyCode);
        editor.apply();
    }


    public void refreshStrUserCode() {
        strUserCode = pref.getString(USER_CODE, "");
    }

    public String getStrUserCode() {
        return strUserCode;
    }

    public void setStrUserCode(String strUserCode) {
        this.strUserCode = strUserCode;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(COMPANY_CODE, strUserCode);
        editor.apply();

    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FILENAME, fileName);
        editor.apply();

    }


    public void refreshStrUserName() {
        strUserName = pref.getString(USER_NAME, "");
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_NAME, strUserName);
        editor.apply();

    }

    public void refreshstrCompanyId() {
        strCompanyId = pref.getString(USER_ID, "");
    }

    public String getstrCompanyId() {
        return strCompanyId;
    }

    public void setstrCompanyId(String strCompanyId) {
        this.strCompanyId = strCompanyId;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(COMPANY_ID, strCompanyId);
        editor.apply();

    }

    public void refreshStrUserId() {
        strUserId = pref.getString(USER_ID, "");
    }

    public String getStrUserId() {
        return strUserId;
    }

    public void setStrUserId(String strUserId) {
        this.strUserId = strUserId;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ID, strUserId);
        editor.apply();

    }

    public void refreshStrToken() {
        strToken = pref.getString(TOKEN, "");
    }

    public String getStrToken() {
        return strToken;
    }

    public void setStrToken(String strToken) {
        this.strToken = strToken;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN, strToken);
        editor.apply();

    }


    public void refreshStrUserOrgId() {
        strUserOrgId = pref.getString(USER_ORG_ID, "");
    }

    public String getStrUserOrgId() {
        return strUserOrgId;
    }

    public void setStrUserOrgId(String strUserOrgId) {
        this.strUserOrgId = strUserOrgId;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ORG_ID, strUserOrgId);
        editor.apply();

    }

    public void refreshstrUserTripId() {
        strUserTripId = pref.getString(USER_TRIP_ID, "");
    }

    public String getStrUserTripId() {
        return strUserTripId;
    }

    public void setStrUserTripId(String strUserTripId) {
        this.strUserTripId = strUserTripId;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_TRIP_ID, strUserTripId);
        editor.apply();

    }

    public void refreshstrUserClientName() {
        strUserClientName = pref.getString(USER_CLIENT_NAME, "");
    }

    public String getStrUserClientName() {
        return strUserClientName;
    }

    public void setStrUserClientName(String strUserClientName) {
        this.strUserClientName = strUserClientName;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_CLIENT_NAME, strUserClientName);
        editor.apply();

    }

    public void refreshstrUserClientNumber() {
        strUserClientNumber = pref.getString(USER_CLIENT_NUMBER, "");
    }

    public String getStrUserClientNumber() {
        return strUserClientNumber;
    }

    public void setStrUserClientNumber(String strUserClientNumber) {
        this.strUserClientNumber = strUserClientNumber;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_CLIENT_NUMBER, strUserClientNumber);
        editor.apply();

    }

    public void refreshFlUserTripDistance() {
        flUserTripDistance = pref.getFloat(USER_TRIP_DISTANCE, 0f);
    }

    public float getFlUserTripDistance() {
        return pref.getFloat(USER_TRIP_DISTANCE, 0f);
    }

    public void setFlUserTripDistance(float flUserTripDistance) {
        this.flUserTripDistance = flUserTripDistance;
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(USER_TRIP_DISTANCE, flUserTripDistance);
        editor.apply();

    }

    public String Lattitude() {
        return pref.getString(LATTITUDE, "");
    }

    public void setLattitude(String Lattitude) {
        this.lattitude = Lattitude;
        SharedPreferences.Editor Editor = pref.edit();
        // Editor.putLong(LATTITUDE, Double.doubleToRawLongBits(Lattitude));
        Editor.putString(LATTITUDE, Lattitude);
        Editor.apply();
        Editor.commit();


    }


    public String Longitude() {
        return pref.getString(LONGITUDE, "");
    }

    public void setLongitude(String Longitude) {
        this.longitude = Longitude;
        SharedPreferences.Editor Editor = pref.edit();
        // Editor.putLong(LONGITUDE,Double.doubleToRawLongBits(Longitude));
        Editor.putString(LONGITUDE, Longitude);
        Editor.apply();
        Editor.commit();


    }

    public void logout() {

        strCompanyCode = "";
        strUserCode = "";
        strUserName = "";
        strUserId = "";
        strUserOrgId = "";
        flUserTripDistance = 0f;
        strUserTripId = "";
        strToken="";


        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

    }
}
