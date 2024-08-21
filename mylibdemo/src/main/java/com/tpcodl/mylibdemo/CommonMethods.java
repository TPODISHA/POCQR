package com.tpcodl.mylibdemo;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CommonMethods {

    public static String secretKey = "CeSuTpDdLTpCoDl#";

    public static  String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";
    public static String fSalt = "mitraapplication";

    public static String getEncryptedString(String key1, String stringToEncrypt)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        String encripString="";



        //String encryptedValue = android.util.Base64.encodeToString(encVal, 0);

        try {
            encripString=CryptoUtilAesCbc.encrypt(CommonMethods.secretKey, CommonMethods.fSalt, stringToEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return encripString;




    }
}
