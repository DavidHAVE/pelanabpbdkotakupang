package com.kupang.androidbpbd.bpbdkupang.model;

public class Address {
    private String addressLine;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String featureName;

    public Address(){
    }

    public Address(String addressLine){
        this.addressLine = addressLine;
    }

    public Address(String addressLine, String city, String state, String country, String postalCode, String featureName){
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.featureName = featureName;
    }

    public String getAddressLine(int i) {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
