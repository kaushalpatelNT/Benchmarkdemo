package com.nichetech.smartonsite.benchmark.ResponseClass;

import com.nichetech.smartonsite.benchmark.Data.Dealer;
import com.nichetech.smartonsite.benchmark.Data.Product;

import java.util.ArrayList;

public class ResponseProduct {

    public int error_code;
    public String error_message;
    public ArrayList<Product> Data;


    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public ArrayList<Product> getData() {
        return Data;
    }
}
