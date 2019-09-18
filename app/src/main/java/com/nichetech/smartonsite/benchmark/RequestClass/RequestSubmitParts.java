package com.nichetech.smartonsite.benchmark.RequestClass;

public class RequestSubmitParts {


    public String Comppartrelid;
    public String ComplainId;
    public String PartId;
    public String Quantity;
    public String BranchId;
    public String PartDiscount;

    public RequestSubmitParts(String comppartrelid, String complainId, String partId, String quantity, String branchId, String partDiscount) {
        Comppartrelid = comppartrelid;
        ComplainId = complainId;
        PartId = partId;
        Quantity = quantity;
        BranchId = branchId;
        PartDiscount = partDiscount;
    }


}
