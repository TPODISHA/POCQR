package com.tpcodl.mylibdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MyLibrary {
    public static String secretKey = "CeSuTpDdLTpCoDl#";

    public static  String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";
    public static String fSalt = "mitraapplication";

    public static int add(int a, int b) {
        return a + b;
    }

    public static String openWebView(Context mContext,String caNumberS){


        String caNumber="";


        try {
            try {
                caNumber= getEncryptedString(secretKey, caNumberS);



            }  catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }/* catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }*/

 return  caNumber;

    /*    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.tpcentralodisha.com:4114/tpcodl-bill-view/getPrepaidInfoHtml?caEnc="+caNumber));
        mContext.startActivity(browserIntent);*/
    }

    public static String getEncryptedString(String key1, String stringToEncrypt)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        String encripString="";



        //String encryptedValue = android.util.Base64.encodeToString(encVal, 0);

        try {
            encripString=CryptoUtilAesCbc.encrypt(secretKey, fSalt, stringToEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return encripString;




    }
}
