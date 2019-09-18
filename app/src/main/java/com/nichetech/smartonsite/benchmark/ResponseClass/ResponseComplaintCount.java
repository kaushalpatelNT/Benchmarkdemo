package com.nichetech.smartonsite.benchmark.ResponseClass;

/**
 * Created by Kaushal on 07-06-2017.
 */

public class ResponseComplaintCount {

    private int error_code;
    private data Data;

    public int getError_code() {
        return error_code;
    }

    public ResponseComplaintCount.data getData() {
        return Data;
    }

    public class data {

        private int Assigned, Accepted, Rejected, Resolved;

        public int getAssigned() {
            return Assigned;
        }

        public int getAccepted() {
            return Accepted;
        }

        public int getRejected() {
            return Rejected;
        }

        public int getResolved() {
            return Resolved;
        }
    }


}
