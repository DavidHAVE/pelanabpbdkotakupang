package com.kupang.androidbpbd.bpbdkupang.model;

public class Report {

    private int reportId;
    private String date;
    private String latitude;
    private String longitude;
    private String address;
    private String disasterType;
    private String photo;
    private String information;
    private String userId;

    public Report(){
    }

    public Report(String latitude, String longitude, String address){
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Report(String date, String latitude, String longitude, String address, String disasterType,
                  String photo, String information, String userId){
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.disasterType = disasterType;
        this.photo = photo;
        this.photo = information;
        this.userId = userId;
    }

    public Report(int reportId, String date, String latitude, String longitude, String address, String disasterType,
                  String photo, String information, String userId){
        this.reportId = reportId;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.disasterType = disasterType;
        this.photo = photo;
        this.information = information;
        this.userId = userId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
