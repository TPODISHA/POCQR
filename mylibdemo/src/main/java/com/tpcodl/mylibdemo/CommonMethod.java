package com.tpcodl.mylibdemo;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.appsearch.GetSchemaResponse;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tpcodl.mylibdemo.qrcodemodel.GetSanDataValueModel;
import com.tpcodl.mylibdemo.qrcodemodel.ScanDataRequest;
import com.tpcodl.mylibdemo.qrcodemodel.ScanDataResponse;
import com.tpcodl.mylibdemo.qrcodemodel.TokenResponse;
import com.tpcodl.mylibdemo.webservice.ApiInterface;
import com.tpcodl.mylibdemo.webservice.RetrofitClientInstanceQrCode;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonMethod {
    static boolean  isSent=true;
    String sToken;
    static String sCA = "80033357577";
    //    private String sMR = "987654";
    static String sBillType = "11";
    static String sPayAmount = "200.50";
    static String sCurBillDate = "2024-05-15";
    static String sArrear = "40.50";
    static String sLastBillDate = "2024-04-12";
    static String sEncryptedCaMrText;
    static String sIV;
    static String sSaltKey;
    static String sSecretKey;
    static String sEncryptedTypeId;
    static String sPosition;

    static int iPos;
    public static String sFirstEncData;
    static String sLastEncData;
    static String sEncryptedScanId;
    static String sDecryptedTypeId;

    static String sRedirectUrl = "https://qapg.tpcentralodisha.com/HDFCUrlEncryptn/HDFCIntent/RedirectToUrl?encString=";

    static ArrayList<GetSanDataValueModel> dataValueModels = new ArrayList<>();
    // private String sec="";
    static String iv = null;
    static String saltkey = null;
    static String versionkey = null;

    static String enctypeid = null;


    static String sApiSaltKey;//a1
    static String sApiVersionKey; //a2
    static String sApiIvDec; // a3 for dec
    static String sApiEncBaseUrl;
   static Bitmap bitmap=null;
    static Bitmap bitmap2=null;


    static String sVersionKey; //a2
    static String sIvDec; // a3 for dec
    static String sEncBaseUrl;

    static String sEncryptedPosition;
    static String sIvEnc;
    static String sEncryptedStartDate;
    static String sEncryptedEndDate;
    static String sDecryptedBaseUrl;
    static String sDecryptedPosition;
    static String sDecryptedStartDAte;
    static String sDecryptedEndDate;




//    static String sRedirectUrl = "https://qapg.tpcentralodisha.com/HDFCUrlEncryptn/HDFCIntent/RedirectToUrl?encString=";











    public static Bitmap generateQrCodeValue(Context context, String CA, String billTyp, double totalPayAmount, String curBilDate, double arrear, String prevblDate, String duedate, double BillBefDigRbt, double blBfrDueDt,double currentDemand,int i,ScanDataResponse dataResponse) {
        //curAmount=1004
        //AmountBefDigRbt=1004
        // totalPayAmount=1020
        // blBfrDueDt=1010
        if (blBfrDueDt < 0)
            blBfrDueDt = 0;
        //   getScanId();
      //  DatabaseHelper db = new DatabaseHelper(context);


        for (int j=0;j<dataResponse.getData().size();j++){
            GetSanDataValueModel modalLocalData=new GetSanDataValueModel();

            modalLocalData.setSaltkey(dataResponse.getSaltKey());
            modalLocalData.setVersionkey(dataResponse.getVersionKey());
            modalLocalData.setIv_dec(dataResponse.getIv());
            modalLocalData.setBaseUrl(dataResponse.getBaseUrl());
            modalLocalData.setEnctypeid(dataResponse.getData().get(j).getENC_TYPE_ID());
            modalLocalData.setScanid(dataResponse.getData().get(j).getScanId());
            modalLocalData.setPosition(dataResponse.getData().get(j).getPosition());
            modalLocalData.setIv_enc(dataResponse.getData().get(j).getT1());
            modalLocalData.setStartDate(dataResponse.getData().get(j).getStartdate());
            modalLocalData.setEndDate(dataResponse.getData().get(j).getEndDate());


            dataValueModels.add(modalLocalData);

        }


       // dataValueModels = db.getScanDataValue();

       // int sec = db.fetch_SecData();

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        // Format the time
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, sec);
        Log.d("CurrentTime", timeString);


        int scanLength = 0;
        scanLength = dataValueModels.size();//6
        int cntKey = 0;
        try {
            if (sec < scanLength) {
                cntKey = sec;
            } else {
                cntKey = sec % scanLength;
            }

            sSaltKey = dataValueModels.get(cntKey).getSaltkey(); //using for enc and dec
            sVersionKey = dataValueModels.get(cntKey).getVersionkey(); //using for enc and dec
            sIvDec = dataValueModels.get(cntKey).getIv_dec(); //using for dec
            sEncBaseUrl = dataValueModels.get(cntKey).getBaseUrl(); // to dec
            sEncryptedTypeId = dataValueModels.get(cntKey).getEnctypeid(); //sending direct value
            sEncryptedScanId = dataValueModels.get(cntKey).getScanid(); // sending direct value
            sEncryptedPosition = dataValueModels.get(cntKey).getPosition(); // to dec
            sIvEnc = dataValueModels.get(cntKey).getIv_enc(); //using for enc
            sEncryptedStartDate = dataValueModels.get(cntKey).getStartDate(); // to dec
            sEncryptedEndDate = dataValueModels.get(cntKey).getEndDate(); // to dec


        } catch (Exception e) {
            cntKey = 0;
        }


        // new TestAsync().execute();

        System.out.println("sec==" + sec);

        try {
            sEncryptedCaMrText = encryptAES(CA + "|" + billTyp + "|" + totalPayAmount + "|" + curBilDate + "|" + arrear + "|" + prevblDate + "|" + duedate + "|" + BillBefDigRbt + "|" + blBfrDueDt +"|"+currentDemand,sVersionKey, sIvEnc, sSaltKey);//with all param
            //  sEncryptedCaMrText = encryptAES(sCA + "|" + sBillType+ "|" + sPayAmount+ "|" + sCurBillDate+ "|" + sArrear + "|" + sLastBillDate + "|" + sDueDate + "|" + sBillBefDigReb + "|" + sBillBefDueDate, versionkey, iv, saltkey);
            //  sEncryptedCaMrText = encryptAES(sCA + "|" + sBillType+ "|" + sPayAmount+ "|" + sCurBillDate+ "|" + sArrear + "|" + sLastBillDate + "|" + sDueDate + "|" + sBillBefDigReb + "|" + sBillBefDueDate, versionkey, iv, saltkey);//with all param hard coded
            System.out.println("Encrypted text: " + sEncryptedCaMrText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sDecryptedBaseUrl = decryptAES(sEncBaseUrl, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedBaseUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sDecryptedPosition = decryptAES(sEncryptedPosition, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sDecryptedStartDAte = decryptAES(sEncryptedStartDate, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedStartDAte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sDecryptedEndDate = decryptAES(sEncryptedEndDate, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedEndDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String fghjk = decryptAES(sEncryptedCaMrText, sVersionKey, sIvEnc, sSaltKey);
            System.out.println("Decrypted text: " + fghjk);
        } catch (Exception e) {
            e.printStackTrace();
        }


        iPos = Integer.parseInt(sDecryptedPosition.substring(0, 2));
        System.out.println("fhcdxgf==" + iPos);
        sFirstEncData = sEncryptedCaMrText.substring(0, iPos);
        System.out.println("fhcdxgf==" + sFirstEncData);
        sLastEncData = sEncryptedCaMrText.substring(iPos);
        System.out.println("fhcdxgf==" + sLastEncData);
        // Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

        // int validflg=chkValidity();

        Bitmap bitmap=  generateQR(i);

        bitmap=AppUtils.compressImage(bitmap);


        Intent intent = new Intent("com.example.SEND_IMAGE"); // Custom action string
       // intent.setPackage("com.tpcodl.samplehimu"); // Optional: specify the target app
        byte[] imageBytes = bitmapToByteArray(bitmap);
        intent.putExtra("image_data", imageBytes);
        context.sendBroadcast(intent);

        return bitmap;

    }




    public static Bitmap generateQrCodeValueCA(Context context, String CA, String billTyp,int i,ScanDataResponse dataResponse) {
        //curAmount=1004
        //AmountBefDigRbt=1004
        // totalPayAmount=1020
        // blBfrDueDt=1010

        //   getScanId();
        //  DatabaseHelper db = new DatabaseHelper(context);


        for (int j=0;j<dataResponse.getData().size();j++){
            GetSanDataValueModel modalLocalData=new GetSanDataValueModel();

            modalLocalData.setSaltkey(dataResponse.getSaltKey());
            modalLocalData.setVersionkey(dataResponse.getVersionKey());
            modalLocalData.setIv_dec(dataResponse.getIv());
            modalLocalData.setBaseUrl(dataResponse.getBaseUrl());
            modalLocalData.setEnctypeid(dataResponse.getData().get(j).getENC_TYPE_ID());
            modalLocalData.setScanid(dataResponse.getData().get(j).getScanId());
            modalLocalData.setPosition(dataResponse.getData().get(j).getPosition());
            modalLocalData.setIv_enc(dataResponse.getData().get(j).getT1());
            modalLocalData.setStartDate(dataResponse.getData().get(j).getStartdate());
            modalLocalData.setEndDate(dataResponse.getData().get(j).getEndDate());


            dataValueModels.add(modalLocalData);

        }


        // dataValueModels = db.getScanDataValue();

        // int sec = db.fetch_SecData();

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        // Format the time
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, sec);
        Log.d("CurrentTime", timeString);


        int scanLength = 0;
        scanLength = dataValueModels.size();//6
        int cntKey = 0;
        try {
            if (sec < scanLength) {
                cntKey = sec;
            } else {
                cntKey = sec % scanLength;
            }

            sSaltKey = dataValueModels.get(cntKey).getSaltkey(); //using for enc and dec
            sVersionKey = dataValueModels.get(cntKey).getVersionkey(); //using for enc and dec
            sIvDec = dataValueModels.get(cntKey).getIv_dec(); //using for dec
            sEncBaseUrl = dataValueModels.get(cntKey).getBaseUrl(); // to dec
            sEncryptedTypeId = dataValueModels.get(cntKey).getEnctypeid(); //sending direct value
            sEncryptedScanId = dataValueModels.get(cntKey).getScanid(); // sending direct value
            sEncryptedPosition = dataValueModels.get(cntKey).getPosition(); // to dec
            sIvEnc = dataValueModels.get(cntKey).getIv_enc(); //using for enc
            sEncryptedStartDate = dataValueModels.get(cntKey).getStartDate(); // to dec
            sEncryptedEndDate = dataValueModels.get(cntKey).getEndDate(); // to dec


        } catch (Exception e) {
            cntKey = 0;
        }


        // new TestAsync().execute();

        System.out.println("sec==" + sec);

        try {
            sEncryptedCaMrText = encryptAES(CA + "|" + billTyp + "|" , sVersionKey, sIvEnc, sSaltKey);//with all param
            //  sEncryptedCaMrText = encryptAES(sCA + "|" + sBillType+ "|" + sPayAmount+ "|" + sCurBillDate+ "|" + sArrear + "|" + sLastBillDate + "|" + sDueDate + "|" + sBillBefDigReb + "|" + sBillBefDueDate, versionkey, iv, saltkey);
            //  sEncryptedCaMrText = encryptAES(sCA + "|" + sBillType+ "|" + sPayAmount+ "|" + sCurBillDate+ "|" + sArrear + "|" + sLastBillDate + "|" + sDueDate + "|" + sBillBefDigReb + "|" + sBillBefDueDate, versionkey, iv, saltkey);//with all param hard coded
            System.out.println("Encrypted text: " + sEncryptedCaMrText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sDecryptedBaseUrl = decryptAES(sEncBaseUrl, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedBaseUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sDecryptedPosition = decryptAES(sEncryptedPosition, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sDecryptedStartDAte = decryptAES(sEncryptedStartDate, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedStartDAte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sDecryptedEndDate = decryptAES(sEncryptedEndDate, sVersionKey, sIvDec, sSaltKey);
            System.out.println("Decrypted text: " + sDecryptedEndDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String fghjk = decryptAES(sEncryptedCaMrText, sVersionKey, sIvEnc, sSaltKey);
            System.out.println("Decrypted text: " + fghjk);
        } catch (Exception e) {
            e.printStackTrace();
        }


        iPos = Integer.parseInt(sDecryptedPosition.substring(0, 2));
        System.out.println("fhcdxgf==" + iPos);
        sFirstEncData = sEncryptedCaMrText.substring(0, iPos);
        System.out.println("fhcdxgf==" + sFirstEncData);
        sLastEncData = sEncryptedCaMrText.substring(iPos);
        System.out.println("fhcdxgf==" + sLastEncData);
        // Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

        // int validflg=chkValidity();

        Bitmap bitmap=  generateQR(i);

        bitmap=AppUtils.compressImage(bitmap);


        Intent intent = new Intent("com.example.SEND_IMAGE"); // Custom action string
        // intent.setPackage("com.tpcodl.samplehimu"); // Optional: specify the target app
        byte[] imageBytes = bitmapToByteArray(bitmap);
        intent.putExtra("image_data", imageBytes);
        context.sendBroadcast(intent);

        return bitmap;

    }










    public static Bitmap generateQRbILL(Context context,String CA, String billTyp, double totalPayAmount, String curBilDate, double arrear, String prevblDate, String duedate, double BillBefDigRbt, double blBfrDueDt,double currentDemand,int i,String base1,String base2,String clientId,String clientSecret,String resource,String appId,String token,String type,String discom) {

        if (isNetworkConnected(context)) {
           // clientId,clientSecret,resource,appId,token,type,discom

        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("clientId", clientId);
            obj.addProperty("clientSecret", clientSecret);
            obj.addProperty("resource", resource);
            obj.addProperty("appId", appId);

            ApiInterface service = RetrofitClientInstanceQrCode.postAuthenticationInstance(base1).create(ApiInterface.class);
            Call<TokenResponse> stringCall = service.callAuthenticationAPI(token, obj);

            stringCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.code() == 200) {
                        Log.e("success", "" + response);
                        if (response.body().getMessage().equalsIgnoreCase("Success")) {
                            System.out.println("token---   " + response.body().getToken());
                            //getImages(true, "Bearer "+response.body().getToken());
                            String sToken = response.body().getToken();
                            bitmap2 = postScanData(context, sToken, CA, billTyp, totalPayAmount, curBilDate, arrear, prevblDate, duedate, BillBefDigRbt, blBfrDueDt, currentDemand,i, base2,type,discom);

                        } else {

                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getIv());

                            generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);


                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();

                        SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsons = mPrefs.getString("MyObject", "");
                        ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                        System.out.println("Sadfgh=="+obj.getIv());

                        generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt, blBfrDueDt,currentDemand, i,obj);


                    } else {
                        Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG).show();
                        SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsons = mPrefs.getString("MyObject", "");
                        ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                        System.out.println("Sadfgh=="+obj.getIv());

                        generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);

                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                    SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String jsons = mPrefs.getString("MyObject", "");
                    ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                    System.out.println("Sadfgh=="+obj.getIv());

                    generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);

                }
            });

            //}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String jsons = mPrefs.getString("MyObject", "");
            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

            System.out.println("Sadfgh=="+obj.getIv());

            generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);


        }

    }else {

            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String jsons = mPrefs.getString("MyObject", "");
            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

            System.out.println("Sadfgh=="+obj.getIv());

            generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);
        }
        return bitmap2;
    }

    public static Bitmap postScanData(Context context,String sToken,String CA, String billTyp, double totalPayAmount, String curBilDate, double arrear, String prevblDate, String duedate, double BillBefDigRbt, double blBfrDueDt,double currentDemand,int i,String base2,String type,String discom) {
        if (isNetworkConnected(context)) {
        try {
            ApiInterface service = RetrofitClientInstanceQrCode.postRetrofitInstance(base2).create(ApiInterface.class);
            ScanDataRequest scanData = new ScanDataRequest();
            scanData.setType(type);
            scanData.setDiscom(discom);

            Call<ScanDataResponse> call = service.call_ScanDataApi("Bearer" + " " + sToken, scanData);
            call.enqueue(new Callback<ScanDataResponse>() {
                @Override
                public void onResponse(Call<ScanDataResponse> call, Response<ScanDataResponse> response) {

                    if (response.code() == 200) {

                        ScanDataResponse dataResponse = response.body();

                        // System.out.println("response---- " + response.body().toString());
                        if (dataResponse.getStatus().equalsIgnoreCase("Success")) {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                            sApiSaltKey = dataResponse.getSaltKey();
                            sApiVersionKey = dataResponse.getVersionKey();
                            sApiIvDec = dataResponse.getIv();
                            sApiEncBaseUrl = dataResponse.getBaseUrl();

                            int scanLength = 0;

                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);

                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(dataResponse);
                            prefsEditor.putString("MyObject", json);
                            prefsEditor.commit();





                           /* String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getBaseUrl());*/



                            scanLength = dataValueModels.size();//6

                       /*     int cntKey = 0;
                            try {
                                if (sec < scanLength) {
                                    cntKey = sec;
                                } else {
                                    cntKey = sec % scanLength;
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }


                            GetSanDataValueModel getSanDataValueModel=new GetSanDataValueModel();



                            dataValueModels*/

                            //  getScanId();
                           /* DatabaseHelper db=new DatabaseHelper(context);
                            db.deleteTable();
                            for (int i = 0; i < dataResponse.getData().size(); i++) {
                                db.insertscandataval(sApiSaltKey, sApiVersionKey, sApiIvDec, sApiEncBaseUrl, dataResponse.getData().get(i).getENC_TYPE_ID(), dataResponse.getData().get(i).getScanId(),dataResponse.getData().get(i).getPosition(),dataResponse.getData().get(i).getT1(),dataResponse.getData().get(i).getStartdate(),dataResponse.getData().get(i).getEndDate());

                            }*/


                                bitmap = generateQrCodeValue(context, CA, billTyp, totalPayAmount, curBilDate, arrear, prevblDate, duedate, BillBefDigRbt, blBfrDueDt, currentDemand,i, dataResponse);



                        } else {
                            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();

                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getIv());

                            generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt, currentDemand,i,obj);


                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();
                        SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsons = mPrefs.getString("MyObject", "");
                        ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                        System.out.println("Sadfgh=="+obj.getIv());

                        generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);

                    } else {
                        Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG).show();
                        SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsons = mPrefs.getString("MyObject", "");
                        ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                        System.out.println("Sadfgh=="+obj.getIv());

                        generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt, currentDemand,i,obj);

                    }
                }

                @Override
                public void onFailure(Call<ScanDataResponse> call, Throwable t) {

                    t.printStackTrace();
                    Log.i("TAG", t.getMessage());

                    SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String jsons = mPrefs.getString("MyObject", "");
                    ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                    System.out.println("Sadfgh=="+obj.getIv());

                    generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();

            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String jsons = mPrefs.getString("MyObject", "");
            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

            System.out.println("Sadfgh=="+obj.getIv());

            generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);


        }

    }else {

            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String jsons = mPrefs.getString("MyObject", "");
            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

            System.out.println("Sadfgh=="+obj.getIv());

            generateQrCodeValue( context, CA,  billTyp,  totalPayAmount,  curBilDate,  arrear,  prevblDate,  duedate,  BillBefDigRbt,  blBfrDueDt,currentDemand, i,obj);
        }
        return bitmap;
    }





    public static Bitmap generateQRCA(Context context,String CA, String billTyp,int i,String base1,String base2,String clientId,String clientSecret,String resource,String appId,String token,String type,String discom) {

        if (isNetworkConnected(context)) {
            // clientId,clientSecret,resource,appId,token,type,discom

            try {
                JsonObject obj = new JsonObject();
                obj.addProperty("clientId", clientId);
                obj.addProperty("clientSecret", clientSecret);
                obj.addProperty("resource", resource);
                obj.addProperty("appId", appId);

                ApiInterface service = RetrofitClientInstanceQrCode.postAuthenticationInstance(base1).create(ApiInterface.class);
                Call<TokenResponse> stringCall = service.callAuthenticationAPI(token, obj);

                stringCall.enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        if (response.code() == 200) {
                            Log.e("success", "" + response);
                            if (response.body().getMessage().equalsIgnoreCase("Success")) {
                                System.out.println("token---   " + response.body().getToken());
                                //getImages(true, "Bearer "+response.body().getToken());
                                String sToken = response.body().getToken();
                                bitmap2 = postScanDataCA(context, sToken, CA, billTyp, i, base2,type,discom);

                            } else {
                                SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                                Gson gson = new Gson();
                                String jsons = mPrefs.getString("MyObject", "");
                                ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                                System.out.println("Sadfgh=="+obj.getIv());

                                generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();

                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getIv());

                            generateQrCodeValueCA( context, CA,  billTyp,  i,obj);


                        } else {
                            Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG).show();

                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getIv());

                            generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                        SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsons = mPrefs.getString("MyObject", "");
                        ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                        System.out.println("Sadfgh=="+obj.getIv());

                        generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
                    }
                });

                //}
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String jsons = mPrefs.getString("MyObject", "");
                ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                System.out.println("Sadfgh=="+obj.getIv());

                generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
            }

        }else {
            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String jsons = mPrefs.getString("MyObject", "");
            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

            System.out.println("Sadfgh=="+obj.getIv());

            generateQrCodeValueCA( context, CA,  billTyp,  i,obj);

        }
        return bitmap2;
    }

    public static Bitmap postScanDataCA(Context context,String sToken,String CA, String billTyp,int i,String base2,String type,String discom) {
        if (isNetworkConnected(context)) {
            try {
                ApiInterface service = RetrofitClientInstanceQrCode.postRetrofitInstance(base2).create(ApiInterface.class);
                ScanDataRequest scanData = new ScanDataRequest();
                scanData.setType(type);
                scanData.setDiscom(discom);

                Call<ScanDataResponse> call = service.call_ScanDataApi("Bearer" + " " + sToken, scanData);
                call.enqueue(new Callback<ScanDataResponse>() {
                    @Override
                    public void onResponse(Call<ScanDataResponse> call, Response<ScanDataResponse> response) {

                        if (response.code() == 200) {

                            ScanDataResponse dataResponse = response.body();

                            // System.out.println("response---- " + response.body().toString());
                            if (dataResponse.getStatus().equalsIgnoreCase("Success")) {
                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                sApiSaltKey = dataResponse.getSaltKey();
                                sApiVersionKey = dataResponse.getVersionKey();
                                sApiIvDec = dataResponse.getIv();
                                sApiEncBaseUrl = dataResponse.getBaseUrl();



                                SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);

                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(dataResponse);
                                prefsEditor.putString("MyObject", json);
                                prefsEditor.commit();





                                int scanLength = 0;

                                scanLength = dataValueModels.size();//6

                       /*     int cntKey = 0;
                            try {
                                if (sec < scanLength) {
                                    cntKey = sec;
                                } else {
                                    cntKey = sec % scanLength;
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }


                            GetSanDataValueModel getSanDataValueModel=new GetSanDataValueModel();



                            dataValueModels*/

                                //  getScanId();
                           /* DatabaseHelper db=new DatabaseHelper(context);
                            db.deleteTable();
                            for (int i = 0; i < dataResponse.getData().size(); i++) {
                                db.insertscandataval(sApiSaltKey, sApiVersionKey, sApiIvDec, sApiEncBaseUrl, dataResponse.getData().get(i).getENC_TYPE_ID(), dataResponse.getData().get(i).getScanId(),dataResponse.getData().get(i).getPosition(),dataResponse.getData().get(i).getT1(),dataResponse.getData().get(i).getStartdate(),dataResponse.getData().get(i).getEndDate());

                            }*/


                                    bitmap = generateQrCodeValueCA(context, CA, billTyp, i, dataResponse);



                            } else {
                                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();

                                SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                                Gson gson = new Gson();
                                String jsons = mPrefs.getString("MyObject", "");
                                ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                                System.out.println("Sadfgh=="+obj.getIv());

                                generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();
                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getIv());

                            generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
                        } else {
                            Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG).show();
                            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String jsons = mPrefs.getString("MyObject", "");
                            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                            System.out.println("Sadfgh=="+obj.getIv());

                            generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
                        }
                    }

                    @Override
                    public void onFailure(Call<ScanDataResponse> call, Throwable t) {

                        t.printStackTrace();
                        Log.i("TAG", t.getMessage());

                        SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsons = mPrefs.getString("MyObject", "");
                        ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                        System.out.println("Sadfgh=="+obj.getIv());

                        generateQrCodeValueCA( context, CA,  billTyp,  i,obj);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();

                SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String jsons = mPrefs.getString("MyObject", "");
                ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                System.out.println("Sadfgh=="+obj.getIv());

                generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
            }

        }else {
            SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String jsons = mPrefs.getString("MyObject", "");
            ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

            System.out.println("Sadfgh=="+obj.getIv());

            generateQrCodeValueCA( context, CA,  billTyp,  i,obj);
        }
        return bitmap;
    }




    public static Bitmap initSDK(Context context ,String base1,String base2,String clientId,String clientSecret,String resource,String appId,String token,String type,String discom) {

        if (isNetworkConnected(context)) {
            // clientId,clientSecret,resource,appId,token,type,discom

            try {
                JsonObject obj = new JsonObject();
                obj.addProperty("clientId", clientId);
                obj.addProperty("clientSecret", clientSecret);
                obj.addProperty("resource", resource);
                obj.addProperty("appId", appId);

                ApiInterface service = RetrofitClientInstanceQrCode.postAuthenticationInstance(base1).create(ApiInterface.class);
                Call<TokenResponse> stringCall = service.callAuthenticationAPI(token, obj);

                stringCall.enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        if (response.code() == 200) {
                            Log.e("success", "" + response);
                            if (response.body().getMessage().equalsIgnoreCase("Success")) {
                                System.out.println("token---   " + response.body().getToken());
                                //getImages(true, "Bearer "+response.body().getToken());
                                String sToken = response.body().getToken();
                                bitmap2 = postScanDataLogin(context, sToken, base2,type,discom);

                            } else {
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

                //}
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return bitmap2;
    }

    public static Bitmap postScanDataLogin(Context context,String sToken,String base2,String type,String discom) {
        if (isNetworkConnected(context)) {
            try {
                ApiInterface service = RetrofitClientInstanceQrCode.postRetrofitInstance(base2).create(ApiInterface.class);
                ScanDataRequest scanData = new ScanDataRequest();
                scanData.setType(type);
                scanData.setDiscom(discom);

                Call<ScanDataResponse> call = service.call_ScanDataApi("Bearer" + " " + sToken, scanData);
                call.enqueue(new Callback<ScanDataResponse>() {
                    @Override
                    public void onResponse(Call<ScanDataResponse> call, Response<ScanDataResponse> response) {

                        if (response.code() == 200) {

                            ScanDataResponse dataResponse = response.body();

                            // System.out.println("response---- " + response.body().toString());
                            if (dataResponse.getStatus().equalsIgnoreCase("Success")) {
                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                sApiSaltKey = dataResponse.getSaltKey();
                                sApiVersionKey = dataResponse.getVersionKey();
                                sApiIvDec = dataResponse.getIv();
                                sApiEncBaseUrl = dataResponse.getBaseUrl();

                                SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);

                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(dataResponse);
                                prefsEditor.putString("MyObject", json);
                                prefsEditor.commit();



                             /*

                           SharedPreferences mPrefs = context.getSharedPreferences("QRCODE", Context.MODE_PRIVATE);

                           String jsons = mPrefs.getString("MyObject", "");
                                ScanDataResponse obj = gson.fromJson(jsons, ScanDataResponse.class);

                                System.out.println("Sadfgh=="+obj.getIv());*/




                                int scanLength = 0;

                                scanLength = dataValueModels.size();//6

                       /*     int cntKey = 0;
                            try {
                                if (sec < scanLength) {
                                    cntKey = sec;
                                } else {
                                    cntKey = sec % scanLength;
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }


                            GetSanDataValueModel getSanDataValueModel=new GetSanDataValueModel();



                            dataValueModels*/

                                //  getScanId();
                           /* DatabaseHelper db=new DatabaseHelper(context);
                            db.deleteTable();
                            for (int i = 0; i < dataResponse.getData().size(); i++) {
                                db.insertscandataval(sApiSaltKey, sApiVersionKey, sApiIvDec, sApiEncBaseUrl, dataResponse.getData().get(i).getENC_TYPE_ID(), dataResponse.getData().get(i).getScanId(),dataResponse.getData().get(i).getPosition(),dataResponse.getData().get(i).getT1(),dataResponse.getData().get(i).getStartdate(),dataResponse.getData().get(i).getEndDate());

                            }*/



                            } else {
                                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ScanDataResponse> call, Throwable t) {

                        t.printStackTrace();
                        Log.i("TAG", t.getMessage());

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bitmap;
    }






    private static Bitmap generateQR(int i) {
        Bitmap bitmap=null;
        BitMatrix matrix;
        String sQrCode = sDecryptedBaseUrl + sDecryptedPosition + sFirstEncData + sEncryptedScanId + sLastEncData + "&typeId="+  sEncryptedTypeId;
        System.out.println("qrCode==" + sQrCode);

        MultiFormatWriter writer = new MultiFormatWriter();
        try {

            if (i==1){
                 matrix = writer.encode(sQrCode, BarcodeFormat.QR_CODE, 300, 300);
            }else {
                matrix = writer.encode(sQrCode, BarcodeFormat.QR_CODE, 400, 400);

            }
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);

            //  iv_qr.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public static String encryptAES(String plainText, String encryptionKey, String encryptionIV, String salt) throws Exception {
        // Combine the encryption key and salt to derive a stronger key
        byte[] keyBytes = (encryptionKey + salt).getBytes(StandardCharsets.UTF_8);

        Cipher aesAlg = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(encryptionIV.getBytes(StandardCharsets.UTF_8));

        aesAlg.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedBytes;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, aesAlg)) {
                try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(cipherOutputStream, StandardCharsets.UTF_8)) {
                    outputStreamWriter.write(plainText);
                }
            }
            encryptedBytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new Exception("Error encrypting the text: " + e.getMessage());
        }

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    public static String decryptAES(String encryptedText, String encryptionKey, String encryptionIV, String salt) throws Exception {
        // Combine the encryption key and salt to derive a stronger key
        byte[] keyBytes = (encryptionKey + salt).getBytes(StandardCharsets.UTF_8);

        Cipher aesAlg = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(encryptionIV.getBytes(StandardCharsets.UTF_8));

        aesAlg.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        String plainText;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptedBytes)) {
            try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream, aesAlg)) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(cipherInputStream, StandardCharsets.UTF_8)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    char[] buffer = new char[1024];
                    int read;
                    while ((read = inputStreamReader.read(buffer)) != -1) {
                        stringBuilder.append(buffer, 0, read);
                    }
                    plainText = stringBuilder.toString();
                }
            }
        } catch (IOException e) {
            throw new Exception("Error decrypting the text: " + e.getMessage());
        }

        return plainText;
    }




    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
