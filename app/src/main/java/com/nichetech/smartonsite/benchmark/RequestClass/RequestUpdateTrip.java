package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by Kaushal on 17-04-2017.
 */

public class RequestUpdateTrip {


    public String user_loc_id,trip_user_id,trip_latitude,trip_longitude,trip_address;

    public RequestUpdateTrip(String user_trip_id,String user_id,String trip_latitude,String trip_longitude,String trip_address){

        this.user_loc_id=user_trip_id;
        this.trip_user_id=user_id;
        this.trip_latitude=trip_latitude;
        this.trip_longitude=trip_longitude;
        this.trip_address=trip_address;

    }


}
