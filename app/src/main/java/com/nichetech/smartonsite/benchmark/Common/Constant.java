package com.nichetech.smartonsite.benchmark.Common;

/**
 * Created by android-2 on 3/15/17.
 */

public class Constant {

    public static class RequestPermissions {
        public static final int READ_EXTERNAL_STORAGE = 1111;
    }
    public final static int REQUEST_FOR_CAMERA = 11;
    public final static int REQUEST_FOR_GALLERY = 12;
    public static final int REQUEST_RESULT_GALLERY = 1011;
    public static final int REQUEST_PERMISSION_CAMERA = 1112;
    public static final int REQUEST_RESULT_CAMERA = 1010;

    public static final String ASSIGNED = "Assigned";
    public static final String PENDING = "Pending";
    public static final String COMPLETED = "Completed";
    public static final String REJECTED = "Rejected";

    public static final String ACTIVITY_NAME ="ActivityName";

    public static class ComplaintType{

        public static final int Allocated=2;
        public static final int Accepted=6;
        public static final int Rejected=3;
        public static final int Closed=4;
    }

    public static class ActivityForResult {
        public static final int ADD_UPDATE_PROJECT = 1012;
        public static final int FILE_SELECT = 1013;
        public static final int FILTER_JOB = 1014;
        public static final int SEARCH_JOB = 1015;
    }

    public static class ActivityTitle {

        public static final String ASSIGNED_LIST="AssignedList";
        public static final String PENDING="Pending";
        public static final String COMPLETED="Completed";
        public static final String REJECTED="Rejected";

    }

    public static class Complaint{

        public static final String ComplainId = "complainId";
        public static final String CustomerName = "customerName";
        public static final String CustomerPhone="customerPhone";
        public static final String CustomerAddress="customerAddress";
        public static final String CustomerAlternatePhone="customerAlternatePhone";
        public static final String CustomerEmail="customerEmail";
        public static final String ProductType="productType";
        public static final String ProductName="productName";
        public static final String BillNo="billNo";
        public static final String DealerName="dealerName";
        public static final String ProductCode="productCode";
        public static final String ComplaintType="complaitType";
        public static final String CustomerRemark="customerRemark";
        public static final String CompanyRemark="companyRemark";
        public static final String Status="satus";
        public static final String Priority="priority";
        public static final String ComplaintDate="complaintDate";

    }
}
