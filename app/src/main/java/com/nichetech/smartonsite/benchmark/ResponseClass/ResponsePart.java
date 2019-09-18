package com.nichetech.smartonsite.benchmark.ResponseClass;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ResponsePart {

    private int error_code;
    private String error_message;
    private ArrayList<Parts> Data;

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public ArrayList<Parts> getData() {
        return Data;
    }

    public class Parts{

        public String UserPartRelId;
        public String UserId;
        public String PartId;
        public String UserStock;
        public String PartName;

        @NonNull
        @Override
        public String toString() {
            return PartName;
        }
    }

}


