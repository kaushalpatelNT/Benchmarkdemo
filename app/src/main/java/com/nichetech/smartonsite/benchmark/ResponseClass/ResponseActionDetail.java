package com.nichetech.smartonsite.benchmark.ResponseClass;

import java.util.ArrayList;

/**
 * Created by android-2 on 3/25/17.
 */
public class ResponseActionDetail {

    public String complaint_action_id;
    public String complaint_action_taken;
    public String complaint_status;
    public String complaint_action_crdate;

    public ArrayList<String> getComplaint_action_images() {
        return complaint_action_images;
    }

    public ArrayList<String> complaint_action_images;

    public String getComplaint_action_id() {
        return complaint_action_id;
    }

    public String getComplaint_action_taken() {
        return complaint_action_taken;
    }

    public String getComplaint_status() {
        return complaint_status;
    }

    public String getComplaint_action_crdate() {
        return complaint_action_crdate;
    }


}