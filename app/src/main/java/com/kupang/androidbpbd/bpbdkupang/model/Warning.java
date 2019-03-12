package com.kupang.androidbpbd.bpbdkupang.model;

public class Warning {

    private int peringatanDiniId;
    private String tanggal;
    private String judul;
    private String informasi;

    public Warning(){
    }

    public Warning(String tanggal, String judul, String informasi){
        this.tanggal = tanggal;
        this.judul = judul;
        this.informasi = informasi;
    }

    public Warning(int peringatanDiniId, String tanggal, String judul, String informasi){
        this.peringatanDiniId = peringatanDiniId;
        this.tanggal = tanggal;
        this.judul = judul;
        this.informasi = informasi;
    }

    public int getPeringatanDiniId() {
        return peringatanDiniId;
    }

    public void setPeringatanDiniId(int peringatanDiniId) {
        this.peringatanDiniId = peringatanDiniId;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getInformasi() {
        return informasi;
    }

    public void setInformasi(String informasi) {
        this.informasi = informasi;
    }
}
