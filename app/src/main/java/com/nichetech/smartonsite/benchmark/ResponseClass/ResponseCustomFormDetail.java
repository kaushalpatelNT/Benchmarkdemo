package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.util.List;
import java.util.Map;

/**
 * Created by android-2 on 5/10/17.
 */

public class ResponseCustomFormDetail {

    public int error_code;
    public String error_message;

    public Data getData() {
        return data;
    }

    public int getError_code() {
        return error_code;
    }

    public Data data;

    public class Data {

        public String comp_id;
        public String comp_user_id;
        public String comp_org_id;
        public String comp_assigned_userid;
        public String complaint_reason;
        public String user_firstname;
        public String user_lastname;
        public String comp_code;
        public String pri_name;
        public String comp_prio_id;
        public String comp_ctype_id;
        public String ctype_type;
        public String org_form_id;
        public String comp_description;
        public String comp_createddate;



        public String comp_updateddate;
        public String comp_status;
        public String customer_name;
        public String customer_address;
        public String customer_phone;
        public String person_firstname;
        public String person_lastname;
        public String customer_company_name;
        public String company_address;
        public String company_city;
        public String company_state;
        public String company_country;
        public String company_pincode;
        public String person_contactno;
        public String person_email;
        public List<FormData> form_data;
        public List<Action> action;

        public String getCustomer_address() {
            return customer_address;
        }

        public String getCustomer_phone() {
            return customer_phone;
        }

        public List<Action> getAction() {
            return action;
        }

        public String getCustomer_name() {
            return customer_name;
        }

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

        public String getUser_firstname() {
            return user_firstname;
        }

        public String getUser_lastname() {
            return user_lastname;
        }

        public String getComp_code() {
            return comp_code;
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

        public String getOrg_form_id() {
            return org_form_id;
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

        public String getPerson_firstname() {
            return person_firstname;
        }

        public String getPerson_lastname() {
            return person_lastname;
        }

        public String getCompany_name() {
            return customer_company_name;
        }

        public String getCompany_address() {
            return company_address;
        }

        public String getCompany_city() {
            return company_city;
        }

        public String getCompany_state() {
            return company_state;
        }

        public String getCompany_country() {
            return company_country;
        }

        public String getCompany_pincode() {
            return company_pincode;
        }

        public String getPerson_contactno() {
            return person_contactno;
        }

        public String getPerson_email() {
            return person_email;
        }

        public List<FormData> getFormData() {
            return form_data;
        }


    }

    public class FormData {

        public String org_form_id;

        public String getOrg_form_id() {
            return org_form_id;
        }

        public String getForm_title() {
            return form_title;
        }

        public String getForm_description() {
            return form_description;
        }

        public String form_title;
        public String form_description;

        public List<FormFields> getForm_fields() {
            return form_fields;
        }

        public List<FormFields> form_fields;


    }

    public class FormFields {
        private String form_field_id, form_id, org_id, field_unique_id, field_name, field_type, field_label, field_isrequired;
        private List<String> field_default_values;

        public String getForm_field_id() {
            return form_field_id;
        }

        public String getForm_id() {
            return form_id;
        }

        public String getOrg_id() {
            return org_id;
        }

        public String getField_unique_id() {
            return field_unique_id;
        }

        public String getField_name() {
            return field_name;
        }

        public String getField_type() {
            return field_type;
        }

        public String getField_label() {
            return field_label;
        }

        public String getField_isrequired() {
            return field_isrequired;
        }

        public List<String> getField_default_values() {
            return field_default_values;
        }
    }

    public class Action {
        public String complaint_action_id,complaint_action_date;
        public List<Map<String, Object>> form_data;

        public List<Map<String, Object>> getForm_data() {
            return form_data;
        }

        public String getComplaint_action_date() {
            return complaint_action_date;
        }

        public String getComplaint_action_id() {
            return complaint_action_id;
        }

    }

}
