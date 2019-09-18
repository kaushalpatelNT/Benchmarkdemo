package com.nichetech.smartonsite.benchmark.Data;

/**
 * Created by nichetech on 1/12/16.
 */

public class DrawerItem {
    String ItemName, companyId, CompanyDetail;
    int imgResID;

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyDetail() {
        return CompanyDetail;
    }

    public DrawerItem(String itemName, int imgResID, String companyId, String companyDetail) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
        this.companyId = companyId;
        this.CompanyDetail = companyDetail;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }
}
