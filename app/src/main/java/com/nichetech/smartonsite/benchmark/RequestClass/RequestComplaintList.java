package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by kaushal on 19/12/16.
 */

public class RequestComplaintList {




    public int RowPerPage, Page, Status;

    public RequestComplaintList(int RowPerPage, int Page, int Status) {
        this.RowPerPage = RowPerPage;
        this.Page = Page;
        this.Status = Status;
    }
}
