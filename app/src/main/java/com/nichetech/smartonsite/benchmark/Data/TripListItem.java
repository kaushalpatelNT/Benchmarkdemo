package com.nichetech.smartonsite.benchmark.Data;

/**
 * Created by android-2 on 4/18/17.
 */

public class TripListItem {

    public String name;
    public String no;
    public String date;

    public TripListItem(String name, String no, String date) {
        this.name = name;
        this.no = no;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }


}
