package com.nichetech.smartonsite.benchmark.WS;

import com.nichetech.smartonsite.benchmark.RequestClass.*;
import com.nichetech.smartonsite.benchmark.ResponseClass.*;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kaushal on 6/12/16.
 */

public interface SAASApi {


    @POST("UserApi/UserLogin")
    Call<ResponseLogin> checkLogin(@Body RequestLogin requestLogin);

    @FormUrlEncoded
    @POST("Login/forgotpass")
    Call<ResponseCommon> ForgotPassword(@Field("phone_number") String phoneNumber);


    //Complaint Module Web Service List
    @POST("Complains/GetComplainsCount")
    Call<ResponseComplaintCount> GetComplaintCount();


    @POST("Complains/GetComplainsForTechnician")
    Call<ResponseAssignedList> GetComplaintList(@Body RequestComplaintList requestComplaintList);

    @POST("complaints/update/")
    Call<ResponseComplaintStatus> ChangeComplaintStatus(@Body RequestComplaintStatus requestComplaintStatus);

    @POST("Complains/OnComplainAcceptByTechnician")
    Call<ResponsecomplaintDetails> AcceptComplaint(@Body RequestComplaintDetails requestComplaintDetails);

    @POST("Complains/OnComplainRejectByTechnician")
    Call<ResponsecomplaintDetails> RejectComplaint(@Body RequestComplaintReject requestComplaintReject);

    @POST("Complains/GetComplainForTechnician")
    Call<ResponsecomplaintDetails> GetComplaintDetails(@Body RequestComplaintDetails requestComplaintDetails);

    @POST("Changepassword/")
    Call<RequestChangePassword> changePassword(@Body RequestChangePassword requestChangePassword);

    @POST("Complains/UpdateComplainRemarks")
    Call<ResponseComplainFillup> ComplaintFillUp(@Body RequestComplaintFillup requestComplaintFillup);

    @POST("complaints/getActionItem/")
    Call<ResponseComplaintDetail> getComplaintDetail(@Body RequestComplaintActionList requestComplaintActionList);

    @POST("Complains/GetPartStockByCompainID")
    Call<ResponsePart> getParts(@Body RequestParts requestParts);

    @Multipart
    @POST("Complains/uploadImage")
    Call<ResponseUploadImage> uploadComplaintImage(@Part MultipartBody.Part file_image);

    //User Tracking Web Service List
    @POST("UserTrack/AddUserLocationTrack")
    Call<ResponseStartTrip> AddStartTrip(@Body RequestStartTrip requestStartTrip);

    @POST("UserTrack/AddLocationDetails")
    Call<ResponseCommon> UpdateTrip(@Body RequestUpdateTrip requestUpdateTrip);

    @POST("UserTrack/UpdateUserLocationTrack")
    Call<ResponseCommon> EndTrip(@Body RequestEndTrip requestEndTrip);

    @POST("UserTrack/GetUserTripsByUserId")
    Call<ResponseTripList> TripList(@Body RequestTripList requestTripList);

    @Multipart
    @POST("UserLocations/uploadimage/")
    Call<ResponseUploadImage> uploadTripImage(@Part MultipartBody.Part file_image);

    @Multipart
    @POST("UserLocations/uploadtext/")
    Call<ResponseUploadTextFile> uploadTripTextFile(@Part MultipartBody.Part file_image);

    // Custom Form Web Service
    @POST("Forms/")
    Call<ResponseCustomForm> GetCustomForm(@Body RequestCustomForm requestCustomForm);

    @POST("Forms/savedata/")
    Call<ResponseCustomFormUpload> CustomFormUpload(@Body RequestCustomFormUpload requestCustomFormUpload);

    @POST("Forms/getdata/")
    Call<ResponseCustomFormDetail> getCustomFormUpload(@Body RequestCustomFormDetail requestCustomFormDetail);

    @POST("Login/logout/")
    Call<ResponseLogout> getLogout(@Body RequestLogout requestLogout);

    @POST("Complains/GetDealerList")
    Call<ResponseDealer> getDealerList();

    @POST("Complains/GetProductListOfCustomer")
    Call<ResponseProduct> getProductList(@Body RequestGetProduct requestGetProduct);

    @POST("Complains/UpdateComplainDetailsByTechnician")
    Call<ResponseCommon> updateComplaint(@Body RequestUpdateComplaint requestUpdateComplaint);

}
