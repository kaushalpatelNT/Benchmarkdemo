package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by kaushal on 20/12/16.
 */

public class RequestComplaintStatus {

    private String complaint_id, complain_status, complain_reason;

    public RequestComplaintStatus(String complaint_id, String complain_status, String complain_reason) {
        this.complaint_id = complaint_id;
        this.complain_status = complain_status;
        this.complain_reason = complain_reason;
    }
}
