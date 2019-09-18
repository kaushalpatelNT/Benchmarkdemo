package com.nichetech.smartonsite.benchmark.ResponseClass;

import com.nichetech.smartonsite.benchmark.Data.Dealer;

import java.util.ArrayList;

public class ResponseDealer {

    public int error_code;
    public String error_message;
    public ArrayList<Dealer> Data;


    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public ArrayList<Dealer> getData() {
        return Data;
    }
}
