package com.dhopaelo.dhopaelo.domain;

public class UserAddress {
    private String areaName;
    private String sectorNo;
    private String roadNo;
    private String houseNo;
    private String flatNo;

    public UserAddress() {
    }

    public UserAddress(String areaName, String sectorNo, String roadNo, String houseNo, String flatNo) {
        this.areaName = areaName;
        this.sectorNo = sectorNo;
        this.roadNo = roadNo;
        this.houseNo = houseNo;
        this.flatNo = flatNo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSectorNo() {
        return sectorNo;
    }

    public void setSectorNo(String sectorNo) {
        this.sectorNo = sectorNo;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    @Override
    public String toString() {
        return "Area Name : " + areaName + ",\n" +
                "Sector No : " + sectorNo + ",\n" +
                "Road No : " + roadNo + ",\n" +
                "House No : " + houseNo + ",\n" +
                "Flat No : " + flatNo +"\n";
    }
}
