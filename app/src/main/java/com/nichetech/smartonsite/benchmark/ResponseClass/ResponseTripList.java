package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by android-2 on 4/20/17.
 */

public class ResponseTripList {

    public int error_code;
    public String error_message;
    public int trip_count;
    private ArrayList<TripListData> Data;

    public int getTrip_count() {
        return trip_count;
    }


    public ArrayList<TripListData> getData() {
        return Data;
    }


    public String getError_message() {
        return error_message;
    }


    public int getError_code() {
        return error_code;
    }


    public class TripListData {
        public String user_loc_id;
        public String user_id;
        public String organization_id;
        public String customer_name;
        public String customer_contact;
        public String trip_starttime;
        public String trip_endtime;
        public String trip_fromlocation;
        public String trip_tolocation;
        public String trip_createddate;
        public String trip_fromaddress;
        public String trip_toaddress;
        public String trip_description;
        public String trip_distance;
        public ArrayList<Location> lstLocation;
        public String trip_images;
        public String getTrip_images() {
            return trip_images;
        }



        public String getTrip_description() {
            return trip_description;
        }

        public String getTrip_toaddress() {
            return trip_toaddress;
        }

        public String getTrip_fromaddress() {
            return trip_fromaddress;
        }

        public String getTrip_createddate() {
            return trip_createddate;
        }

        public String getTrip_tolocation() {
            return trip_tolocation;
        }

        public String getTrip_fromlocation() {
            return trip_fromlocation;
        }

        public String getTrip_starttime() {
            return trip_starttime;
        }

        public String getTrip_endtime() {
            return trip_endtime;
        }

        public String getCustomer_contact() {
            return customer_contact;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getOrganization_id() {
            return organization_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUser_loc_id() {
            return user_loc_id;
        }

        public String getTrip_distance() {
            return trip_distance;
        }

        public ArrayList<Location> getLocations() {
            return lstLocation;
        }


        public class Location implements Serializable {
            public String user_loc_detail_id;
            public String user_loc_id;
            public String trip_user_id;
            public String trip_latitude;
            public String trip_longitude;
            public String trip_address;
            public String trip_createddate;
            public String trip_updateddate;

            public String getTrip_updateddate() {
                return trip_updateddate;
            }

            public String getTrip_createddate() {
                return trip_createddate;
            }

            public String getTrip_address() {
                return trip_address;
            }

            public String getTrip_longitude() {
                return trip_longitude;
            }

            public String getTrip_latitude() {
                return trip_latitude;
            }

            public String getTrip_user_id() {
                return trip_user_id;
            }

            public String getUser_loc_id() {
                return user_loc_id;
            }

            public String getUser_loc_detail_id() {
                return user_loc_detail_id;
            }


        }

    }
}



