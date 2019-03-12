package com.kupang.androidbpbd.bpbdkupang.model;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileApi {
    @Multipart
    @POST("imageupload.php?apicall=uploadpic")
    Call<MyResponse> uploadImage(@Part MultipartBody.Part file);
}
