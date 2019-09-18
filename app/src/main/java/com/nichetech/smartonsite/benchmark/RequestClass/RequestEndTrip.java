package com.nichetech.smartonsite.benchmark.RequestClass;

import java.util.List;

/**
 * Created by Kaushal on 19-04-2017.
 */

public class RequestEndTrip {

    private String user_id,user_loc_id,trip_toaddress,trip_tolocation,trip_description,trip_distance;
   // private trip_tolocation trip_tolocation;
    private String trip_images;

    public RequestEndTrip(String user_id,String user_trip_id,String description,String trip_toaddress,String trip_distance,String trip_tolocation,String trip_images){

        this.user_id=user_id;
        this.user_loc_id=user_trip_id;
        this.trip_description=description;
        this.trip_toaddress=trip_toaddress;
        this.trip_tolocation=trip_tolocation;
        this.trip_images=trip_images;
        this.trip_distance=trip_distance;

    }

    public static class trip_tolocation{
        String lat,lon;

        public trip_tolocation(String lat,String lon){
            this.lat=lat;
            this.lon=lon;
        }
    }
}