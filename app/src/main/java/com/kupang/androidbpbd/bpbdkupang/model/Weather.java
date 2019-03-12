package com.kupang.androidbpbd.bpbdkupang.model;

public class Weather {

    private int prakiraanCuacaId;
    private String tanggal;
    private String informasi;
    private String foto;

    public Weather(){
    }

    public Weather(String tanggal, String informasi, String foto){
        this.tanggal = tanggal;
        this.informasi = informasi;
        this.foto = foto;
    }

    public Weather(int prakiraanCuacaId, String tanggal, String informasi, String foto){
        this.prakiraanCuacaId = prakiraanCuacaId;
        this.tanggal = tanggal;
        this.informasi = informasi;
        this.foto = foto;
    }

    public int getPrakiraanCuacaId() {
        return prakiraanCuacaId;
    }

    public void setPrakiraanCuacaId(int prakiraanCuacaId) {
        this.prakiraanCuacaId = prakiraanCuacaId;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getInformasi() {
        return informasi;
    }

    public void setInformasi(String informasi) {
        this.informasi = informasi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
