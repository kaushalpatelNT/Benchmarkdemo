package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.io.Serializable;

/**
 * Created by kaushal on 19/12/16.
 */

public class ResponsecomplaintDetails implements Serializable {

    private int error_code;
    public String error_message;

    private ComplaintDetails Data;

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public ComplaintDetails getData() {
        return Data;
    }

    public class ComplaintDetails {

        private String ComplainId;
        private String CustomerName;
        private String CustomerPhone;
        private String CustomerAddress;
        private String CustomerAlternatePhone;
        private String CustomerEmail;
        private String ProductType;
        private String ProductName;
        private String BillNumber;
        private String DealerName;
        private String ProductCode;
        private String ComplaintType;
        private String CustomerRemark;
        private String CompanyRemark;
        private String Status;
        private String Priority;
        private String ComplaintDate;


        public String getComplainId() {
            return ComplainId;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public String getCustomerPhone() {
            return CustomerPhone;
        }

        public String getCustomerAddress() {
            return CustomerAddress;
        }

        public String getCustomerAlternatePhone() {
            return CustomerAlternatePhone;
        }

        public String getCustomerEmail() {
            return CustomerEmail;
        }

        public String getProductType() {
            return ProductType;
        }

        public String getProductName() {
            return ProductName;
        }

        public String getProductCode() {
            return ProductCode;
        }

        public String getComplaintType() {
            return ComplaintType;
        }

        public String getCustomerRemark() {
            return CustomerRemark;
        }

        public String getCompanyRemark() {
            return CompanyRemark;
        }

        public String getStatus() {
            return Status;
        }

        public String getPriority() {
            return Priority;
        }

        public String getComplaintDate() {
            return ComplaintDate;
        }

        public String getBillNumber() {
            return BillNumber;
        }

        public String getDealerName() {
            return DealerName;
        }
    }
}