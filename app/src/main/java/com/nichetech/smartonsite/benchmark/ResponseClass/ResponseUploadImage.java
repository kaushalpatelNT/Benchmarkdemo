package com.nichetech.smartonsite.benchmark.ResponseClass;

/**
 * Created by android-2 on 3/21/17.
 */

public class ResponseUploadImage {

    private int error_code;
    private String error_message;
    private String Data;

    public String getData() {
        return Data;
    }

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }
}
