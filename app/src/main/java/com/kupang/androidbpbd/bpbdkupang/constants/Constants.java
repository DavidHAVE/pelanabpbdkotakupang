package com.kupang.androidbpbd.bpbdkupang.constants;

public class Constants {


    private static final String ROOT_URL = "http://bpbdkupang.000webhostapp.com/Api.php?apicall=";
    public static final String ROOT_URL_UPLOAD_IMAGE = "http://bpbdkupang.000webhostapp.com/";

    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_REGISTER_PENGGUNA = ROOT_URL + "register_pengguna";
    public static final String URL_READ_PENGGUNA = ROOT_URL + "read_pengguna&pengguna_id=";
    public static final String URL_UPDATE_PENGGUNA = ROOT_URL + "update_pengguna";

    public static final String URL_REGISTER_LAPORAN_WITH_PIC = ROOT_URL + "register_laporan_pic";
    public static final String URL_READ_HISTORY_LAPORAN = ROOT_URL + "read_history_laporan&pengguna_id=";
    public static final String URL_READ_LATEST_LAPORAN = ROOT_URL + "read_latest_laporan";

    public static final String URL_READ_PERINGATAN_DINI = ROOT_URL + "read_peringatan_dini";

    public static final String URL_READ_PRAKIRAAN_CUACA = ROOT_URL + "read_prakiraan_cuaca";

    public static final String URL_IMAGE = "http://bpbdkupang.000webhostapp.com/login/foto_prakiraan_cuaca/";
    public static final String URL_IMAGE_LAPORAN = "http://bpbdkupang.000webhostapp.com/foto_laporan/";


    public static final String TAG_WARNING_ID = "peringatan_dini_id";
    public static final String TAG_WARNING_DATE = "tanggal";
    public static final String TAG_WARNING_TITLE = "judul";
    public static final String TAG_WARNING_INFORMATION = "informasi";


    public static final String TAG_WEATHER_ID = "prakiraan_cuaca_id";
    public static final String TAG_WEATHER_DATE = "tanggal";
    public static final String TAG_WEATHER_IMAGE = "foto";
    public static final String TAG_WEATHER_INFORMATION = "informasi";

    public static final String TAG_REPORT_ID = "laporan_id";
    public static final String TAG_REPORT_DATE = "tanggal";
    public static final String TAG_REPORT_LATITUDE = "latitude";
    public static final String TAG_REPORT_LONGITUDE = "longitude";
    public static final String TAG_REPORT_ADDRESS = "alamat";
    public static final String TAG_REPORT_DISASTER_TYPE = "jenis_kejadian";
    public static final String TAG_REPORT_PHOTO = "foto";
    public static final String TAG_REPORT_INFORMATION = "informasi";
    public static final String TAG_REPORT_USER_ID = "pengguna_id";


    public static final String TAG_USER_NAME = "nama";
    public static final String TAG_USER_TELEPHONE_NUMBER = "nomor_telepon";
    public static final String TAG_USER_EMAIL = "email";

}
