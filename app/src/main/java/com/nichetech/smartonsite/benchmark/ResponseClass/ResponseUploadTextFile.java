package com.nichetech.smartonsite.benchmark.ResponseClass;

/**
 * Created by android-2 on 9/11/17.
 */

public class ResponseUploadTextFile {
    private int error_code;
    private String error_message;
    private String data;

    public String getData() {
        return data;
    }

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }
}
