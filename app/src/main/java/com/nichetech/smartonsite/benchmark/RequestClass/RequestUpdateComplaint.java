package com.nichetech.smartonsite.benchmark.RequestClass;

import java.util.ArrayList;

/**
 * Created by android-2 on 3/23/17.
 */

public class RequestUpdateComplaint {

    public String ComplainID;
    public String BillNumber;
    public String DealerId;
    public String ProductId;

    public RequestUpdateComplaint(String complainID, String billNumber, String dealerId, String productId) {
        ComplainID = complainID;
        BillNumber = billNumber;
        DealerId = dealerId;
        ProductId = productId;
    }
}
