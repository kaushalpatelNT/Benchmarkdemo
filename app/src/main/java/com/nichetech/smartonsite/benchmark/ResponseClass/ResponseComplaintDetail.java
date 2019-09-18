package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.util.List;

/**
 * Created by android-2 on 3/24/17.
 */

public class ResponseComplaintDetail {

    public int error_code;
    public String error_message;

    public Data data;

    public int getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public Data getData() {
        return data;
    }

    public class Data {

        public String comp_id;
        public String comp_user_id;
        public String comp_org_id;
        public String comp_assigned_userid;
        public String complaint_reason;
        public String cust_firstname;
        public String user_firstname;
        public String user_lastname;
        public String cust_lastname;
        public String pri_name;
        public String comp_prio_id;
        public String comp_ctype_id;
        public String ctype_type;
        public String cust_address;
        public String cust_email;
        public String cust_phone;
        public String comp_description;
        public String comp_createddate;
        public String comp_updateddate;
        public String comp_status;
        public String comp_code;
        public String person_firstname;
        public String person_lastname;
        public String company_name;
        public String person_contactno;

        public String getPerson_firstname() {
            return person_firstname;
        }

        public String getPerson_lastname() {
            return person_lastname;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getPerson_contactno() {
            return person_contactno;
        }

        public String getComp_code() {
            return comp_code;
        }


        public List<ResponseActionDetail> action;

        public String getComp_id() {
            return comp_id;
        }

        public String getComp_user_id() {
            return comp_user_id;
        }

        public String getComp_org_id() {
            return comp_org_id;
        }

        public String getComp_assigned_userid() {
            return comp_assigned_userid;
        }

        public String getComplaint_reason() {
            return complaint_reason;
        }

        public String getCust_firstname() {
            return cust_firstname;
        }

        public String getUser_firstname() {
            return user_firstname;
        }

        public String getUser_lastname() {
            return user_lastname;
        }

        public String getCust_lastname() {
            return cust_lastname;
        }

        public String getPri_name() {
            return pri_name;
        }

        public String getComp_prio_id() {
            return comp_prio_id;
        }

        public String getComp_ctype_id() {
            return comp_ctype_id;
        }

        public String getCtype_type() {
            return ctype_type;
        }

        public String getCust_address() {
            return cust_address;
        }

        public String getCust_email() {
            return cust_email;
        }

        public String getCust_phone() {
            return cust_phone;
        }

        public String getComp_description() {
            return comp_description;
        }

        public String getComp_createddate() {
            return comp_createddate;
        }

        public String getComp_updateddate() {
            return comp_updateddate;
        }

        public String getComp_status() {
            return comp_status;
        }

        public List<ResponseActionDetail> getAction() {
            return action;
        }

    }

}
