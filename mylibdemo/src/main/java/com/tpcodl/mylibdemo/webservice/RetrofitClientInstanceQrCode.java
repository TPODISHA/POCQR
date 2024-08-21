package com.tpcodl.mylibdemo.webservice;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstanceQrCode {
    private static Retrofit retrofit;
    private static Retrofit retrofit1;

//    https://testselread.tpcentralodisha.com/PDIWEB/

    //private static final String BASE_URL = "https://testselread.tpcentralodisha.com/PDIWEB/";  // Test
  //  private static final String BASE_URL = "https://testselread.tpcentralodisha.com/"; // Prod


   // https://tqr.tpodisha.com/ScanIdAPI/GetScanId

   // private static final String BASE_URL = "https://qapg.tpcentralodisha.com/";
    private static final String BASE_URL = "https://tqr.tpodisha.com/ScanIdAPI/";




    private static final String BASE_URL1 = "https://authservice.tpcentralodisha.com/";

    public static Retrofit postRetrofitInstance() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.protocols(Util.immutableList(Protocol.HTTP_1_1));
        httpClient.connectTimeout(300, TimeUnit.SECONDS);
        httpClient.readTimeout(300, TimeUnit.SECONDS);
        httpClient.writeTimeout(300, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        OkHttpClient client = httpClient.build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit postAuthenticationInstance() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.protocols(Util.immutableList(Protocol.HTTP_1_1));
        httpClient.connectTimeout(300, TimeUnit.SECONDS);
        httpClient.readTimeout(300, TimeUnit.SECONDS);
        httpClient.writeTimeout(300, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }
}
