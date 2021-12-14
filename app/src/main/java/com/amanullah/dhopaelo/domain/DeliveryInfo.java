package com.amanullah.dhopaelo.domain;

public class DeliveryInfo {
    private UserInfo userInfo;
    private UserAddress userAddress;

    public DeliveryInfo() {
    }

    public DeliveryInfo(UserInfo userInfo, UserAddress userAddress) {
        this.userInfo = userInfo;
        this.userAddress = userAddress;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }
}
