package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by kaushal on 20/12/16.
 */

public class RequestComplaintReject {

    private String ComplainId;
    private String Reason;

    public RequestComplaintReject(String complain_id,String reason) {

        this.ComplainId = complain_id;
        this.Reason = reason;
    }
}
