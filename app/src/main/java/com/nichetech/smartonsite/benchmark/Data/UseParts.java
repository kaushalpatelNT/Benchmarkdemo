package com.nichetech.smartonsite.benchmark.Data;

public class UseParts {

    public UseParts(String partsID, String partQty) {
        this.partsID = partsID;
        this.partQty = partQty;
    }

    public  String partsID,partName,partPrice,partQty;

    public String getPartsID() {
        return partsID;
    }

    public String getPartName() {
        return partName;
    }

    public String getPartPrice() {
        return partPrice;
    }

    public String getPartQty() {
        return partQty;
    }

    public void setPartsID(String partsID) {
        this.partsID = partsID;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setPartPrice(String partPrice) {
        this.partPrice = partPrice;
    }

    public void setPartQty(String partQty) {
        this.partQty = partQty;
    }
}
