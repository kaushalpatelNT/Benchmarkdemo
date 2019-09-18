package com.nichetech.smartonsite.benchmark.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.nichetech.smartonsite.benchmark.BuildConfig;
import com.nichetech.smartonsite.benchmark.R;
import com.nichetech.smartonsite.benchmark.WS.SAASApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by kaushal on 5/12/16.
 */

public class Utility {

    public static final String CONTACT_REPLACE_EXPRESSION = "[^+0-9]";

    public static Context mContext;
    public static Preferences mPref;
    private Activity activity;
    private static GPSTracker gpsTracker;
    public File gpxfile;

    //Live Link
    //public static String Url="http://smartonsite.in/api/";
    //public static String imageUrl="http://smartonsite.in/uploads/images/thumb/";

    //QA LIink
//    public static String Url="http://smartonsite.nichetech.in/api/";
//    public static String imageUrl="http://smartonsite.nichetech.in/uploads/images/thumb/";

    public Utility(Context context) {
        mContext = context;
        mPref = new Preferences(context);
        context.startService(new Intent(context, GPSTracker.class));
    }


    public static boolean isOnline(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnected();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static boolean isNotNull(String str) {
        return !(str == null || str.equalsIgnoreCase("null") || str.length() == 0);
    }

    public static boolean validateCompanyId(String companyId) {
        return !(companyId.matches("^[A-Z]{3}+[0-9]{6}") || companyId.length() == R.integer.maxCompanyId);
    }

    public static boolean validateUserId(String userID) {
        return !(userID.length() == R.integer.maxUserId);
    }

    public static boolean validateMobileNo(String mobileNo) {
        return !(mobileNo.matches("^[0-9]{10}") || mobileNo.length() == R.integer.maxMobileNo);
    }


    public static void showAlert(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static void showAlert(@NonNull Context context, @NonNull String message,
                                 final OnOkClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (listener != null) listener.onOkClick();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static void showAlert(String message, OnOkClickListener listener) {
        showAlert(message, listener);
    }

    public static void showToast(Context context, int message) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void showToast(Context context, String message) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static SAASApi saasapi(Context context) {

        mPref=new Preferences(context);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.Url)
                .addConverterFactory(GsonConverterFactory.create());


        /*String credentials = "admin:SAAS";
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);*/


        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                String basic = "";
                if(!mPref.getStrToken().isEmpty()) {
                    basic =
                            "bearer " + mPref.getStrToken();
                }

                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });


        OkHttpClient httpClient = client.build();
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(SAASApi.class);

    }

    public static class ErrorCode {
        public final static int AUTHENTICATION_FAIL = -1;
        public final static int SUCCESS = 1;
        public final static int FAILED = 0;
        public final static String ERROR_CODE = "error_code";
        public final static String ERROR_MESSAGE = "error_message";
    }

    //Date Convter

    public static String convertDate(String date) {

        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = currentFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        return convertFormat.format(date1);


    }

    public static String convertDateTrip(String date) {

        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = currentFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        return convertFormat.format(date1);


    }

    public static int intToDP(Context context, int size) {
        try {
            float d = context.getResources().getDisplayMetrics().density;
            return (int) (size * d);
        } catch (Exception e) {
            return 0;
        }
    }

    public interface OnOkClickListener {
        void onOkClick();
    }

    //DIRECTORY FOR IMAGE
    public static final File MEDIA_STORAGE_DIR = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SAAS");

    static Handler mHandler;

    public static Handler getmHandler() {
        return mHandler;
    }

    public static void setmHandler(Handler mHandler) {
        Utility.mHandler = mHandler;
    }

    // Creating file uri to store image/video
    public static Uri getOutputMediaFileUri(String ImgName) {

        String TAG = "GET_OUTPUT_MEDIA_URI";
        // Create the storage directory if it does not exist
        if (!MEDIA_STORAGE_DIR.exists()) {
            if (!MEDIA_STORAGE_DIR.mkdirs()) {
                return null;
            }
        }

        File mediaFile;
        Date date = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);

        String Filename = ImgName + "_" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".jpeg";

        mediaFile = new File(MEDIA_STORAGE_DIR.getPath() + File.separator + Filename);

        return Uri.fromFile(mediaFile);
    }

    //SCALING IMAGE
    public static Bitmap Preview(String path) {
        //SCALE IMAGE
        int SCALE = 3;
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = SCALE;
            Bitmap bitmap = BitmapFactory.decodeFile(path, o2);
            OutputStream os = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
            os.flush();
            os.close();
            File file = new File(path);

            /*while (file.length() > 800000) {
                SCALE += 1;
                o2.inSampleSize = SCALE;
                bitmap = BitmapFactory.decodeFile(path, o2);
                os = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                file = new File(path);
            }*/

            bitmap = BitmapFactory.decodeFile(path, o2);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //GET BITMAP IMAGE INFORM OF BYTE ARRAY
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static String getClassName(Class c) {
        String className = c.getName();
        int firstChar;
        firstChar = className.lastIndexOf('.') + 1;
        if (firstChar > 0) {
            className = className.substring(firstChar);
        }
        return className;
    }

    public static String getAddress() {
        String strAddress = "";
        Log.e("LATITUDE", "" + GPSTracker.currentLocation.getLatitude());
        Log.e("LONGITUDE", "" + GPSTracker.currentLocation.getLongitude());
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(GPSTracker.Latitude, GPSTracker.Longitude, 1);

            Log.d("ADDress", addresses.toString());
            Log.d("Address", addresses.get(0).getAddressLine(0));
            Log.d("City", addresses.get(0).getLocality());
            Log.d("State", addresses.get(0).getSubLocality());
            Log.d("Country", addresses.get(0).getCountryName());

            strAddress = addresses.get(0).getSubLocality() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Utility.isNotNull(strAddress)) {
            return strAddress;
        } else {
            return "";
        }

    }

    public void generateNoteOnSD(Context mContext, String userId) {
        try {


            File root = new File(Environment.getExternalStorageDirectory(), "SOS");
            if (!root.exists()) {
                root.mkdirs();
            }
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

            String file_name = "Id_" + userId + "_" + dateFormat.format(date) + ".txt";

            Preferences mPref = new Preferences(mContext);
            mPref.setFilename(file_name);

            Log.e("Filename", "" + mPref.getFilename());

            gpxfile = new File(root, file_name);
            if (!gpxfile.exists()) {
                gpxfile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void adddatainfile(String sBody, Context context) {
        try {

            Preferences mPref = new Preferences(context);

            File root = new File(Environment.getExternalStorageDirectory(), "SOS");
            File gpxfile = new File(root, mPref.getFilename());

            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line + "\n");
                //text.append('\n'); // commented for one line only
            }

            FileWriter writer = new FileWriter(gpxfile);
            writer.append(text + sBody);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("File write Error =", e.toString());
        }


    }

    public static void openMap(Context context, String name, String latitude, String longitude) {
        if (Validate.isNotNull(latitude) && Validate.isNotNull(longitude)) {
            String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
            if (Validate.isNotNull(name))
                geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + name + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            context.startActivity(intent);
        }
    }

    private static String blockCharacterSet = ",~:;^|$%&*!{}[]<>`=¥£¢€∆π®©√°¶™℅•÷×()";
    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder sb = new StringBuilder(end - start);
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    sb.append("");
//return "";
                } else if (Utility.blockCharacterSet.contains("" + source)) {
                    sb.append("");
//return "";
                } else {
                    sb.append(source.charAt(i));
                }
            }
            return sb;
//return null;
        }
    };

}
