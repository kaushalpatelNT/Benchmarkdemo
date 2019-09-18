package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.util.List;

/**
 * Created by Kaushal on 08-05-2017.
 */

public class ResponseCustomForm {

    private int error_code;
    private List<data> data;

    public int getError_code() {
        return error_code;
    }

    public List<ResponseCustomForm.data> getData() {
        return data;
    }

    public class data{

        private String org_form_id,form_title;
        private List<form_fields> form_fields;

        public String getOrg_form_id() {
            return org_form_id;
        }

        public String getForm_title() {
            return form_title;
        }

        public List<ResponseCustomForm.form_fields> getForm_fields() {
            return form_fields;
        }
    }

    public class form_fields{

        private String form_field_id,form_id,org_id,field_unique_id,field_name,field_type,field_label,field_isrequired;
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

}
