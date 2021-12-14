package com.amanullah.dhopaelo.domain;

public class StatusTimelineDetailsModel {
    private String orderKey;
    private String title;
    private String message;
    private String estimateTime;
    private int image;
    private boolean step1Complete;
    private boolean step2Complete;
    private boolean step3Complete;
    private boolean step4Complete;
    private boolean step5Complete;

    public StatusTimelineDetailsModel() {
    }

    public StatusTimelineDetailsModel(String orderKey, String title, String message, String estimateTime, int image) {
        this.orderKey = orderKey;
        this.title = title;
        this.message = message;
        this.estimateTime = estimateTime;
        this.image = image;
    }

    public StatusTimelineDetailsModel(String orderKey, String title, String message, String estimateTime, int image, boolean step1Complete) {
        this.orderKey = orderKey;
        this.title = title;
        this.message = message;
        this.estimateTime = estimateTime;
        this.image = image;
        this.step1Complete = step1Complete;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public int getImage() {
        return image;
    }

    public boolean isStep1Complete() {
        return step1Complete;
    }

    public boolean isStep2Complete() {
        return step2Complete;
    }

    public boolean isStep3Complete() {
        return step3Complete;
    }

    public boolean isStep4Complete() {
        return step4Complete;
    }

    public boolean isStep5Complete() {
        return step5Complete;
    }

}
