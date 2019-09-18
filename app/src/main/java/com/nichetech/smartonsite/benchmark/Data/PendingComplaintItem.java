package com.nichetech.smartonsite.benchmark.Data;

/**
 * Created by nichetech on 6/12/16.
 */

public class PendingComplaintItem {
    String name, company_name, id, date;

    public PendingComplaintItem(String name, String company_name, String id, String date) {
        this.name = name;
        this.company_name = company_name;
        this.id = id;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
