package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by Kaushal on 17-04-2017.
 */

public class RequestStartTrip {

    private String user_id,organization_id, customer_name, customer_contact, trip_start, trip_end, trip_toaddress, trip_fromaddress, device_type, trip_deviceinfo,trip_fromlocation;
   // private trip_fromlocation trip_fromlocation;
    private trip_tolocation trip_tolocation;

    public RequestStartTrip(String user_id, String customer_name, String customer_contact, String trip_fromaddress, String trip_fromlocation,String device_type, String trip_deviceinfo) {
        this.user_id = user_id;
        this.organization_id = "1";
        this.customer_name = customer_name;
        this.customer_contact = customer_contact;
        this.trip_fromlocation = trip_fromlocation;
        this.trip_fromaddress = trip_fromaddress;
    //    this.device_type = device_type;
        this.trip_deviceinfo = trip_deviceinfo;
    }


    public static class trip_fromlocation {

        private String lat, lon;

        public trip_fromlocation(String lat, String lon) {
            this.lat = lat;
            this.lon = lon;
        }

    }

    public static class trip_tolocation {

        private String lat, lon;

        public trip_tolocation(String lat, String lon) {
            this.lat = lat;
            this.lon = lon;
        }

    }

}
