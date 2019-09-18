package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by kaushal on 20/12/16.
 */

public class RequestForgotPassword {

    private String company_id, user_id, phone_number;

    public RequestForgotPassword(String company_id, String user_id, String phone_number) {
        this.company_id = company_id;
        this.user_id = user_id;
        this.phone_number = phone_number;
    }
}
