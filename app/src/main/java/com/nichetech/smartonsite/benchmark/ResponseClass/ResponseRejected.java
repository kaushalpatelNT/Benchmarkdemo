package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kaushal on 19/12/16.
 */

public class ResponseRejected implements Serializable {

    private int error_code;
    public String error_message;

    private Boolean Data;

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }


}
