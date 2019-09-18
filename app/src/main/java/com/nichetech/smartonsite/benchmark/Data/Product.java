package com.nichetech.smartonsite.benchmark.Data;

import androidx.annotation.NonNull;

public class Product {

    public String ProductId;
    public String Productcode;
    public String Productname;

    @NonNull
    @Override
    public String toString() {
        return Productname;
    }
}
