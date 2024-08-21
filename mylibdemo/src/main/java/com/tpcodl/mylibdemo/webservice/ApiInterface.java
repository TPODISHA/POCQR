package com.tpcodl.mylibdemo.webservice;

import com.google.gson.JsonObject;
import com.tpcodl.mylibdemo.qrcodemodel.ScanDataRequest;
import com.tpcodl.mylibdemo.qrcodemodel.ScanDataResponse;
import com.tpcodl.mylibdemo.qrcodemodel.TokenResponse;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("GetScanId")
    Call<ScanDataResponse> call_ScanDataApi(@Header("Authorization") String token, @Body ScanDataRequest scanDataRequest);
    @POST("api/Auth/GetToken")
    Call<TokenResponse> callAuthenticationAPI(@Header("APIKEY") String token, @Body JsonObject object);





}
