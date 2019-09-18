package com.nichetech.smartonsite.benchmark.RequestClass;

import java.util.Map;

/**
 * Created by android-2 on 5/10/17.
 */

public class RequestCustomFormUpload {

    public String company_id;
    public String form_id;
    public String user_id;
    public String complaint_id;
    public String complaint_code;
    public String complaint_status;
    public Map<String, Object> form_body;

    public Map<String, Object> getForm_body() {
        return form_body;
    }


    public String getComplaint_status() {
        return complaint_status;
    }

    public String getComplaint_code() {
        return complaint_code;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public String getCompany_id() {
        return company_id;
    }


    /*private class FormBody {

    }*/
}
/*
company_id:7
        form_id:3
        user_id:20
        complaint_id:5
        complaint_code:NI-5
        complaint_status:Pending
        form_body:{"complaint-detail":"Compalint registered by company fix it ASAP",
        "complaint-images":"[\"FuOfWdP1oqN3hcD5.jpg\"]",
        "type-of-complaint":"Electrical",
        "complaint-category":"Normal",
        "spare-rquired":"Yes",
        "client-remarks":"Compalint Fixed. Spare changed",
        "spare-actions":"Change"}*/
