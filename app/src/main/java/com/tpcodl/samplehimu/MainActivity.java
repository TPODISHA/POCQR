package com.tpcodl.samplehimu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tpcodl.mylibdemo.CommonMethod;
import com.tpcodl.mylibdemo.MyLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_view;
    ImageView iv_iamge;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.VIBRATE,
            android.Manifest.permission.CALL_PHONE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.VIBRATE,

            Manifest.permission.CALL_PHONE
    };

    BroadcastReceiver imageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.SEND_IMAGE".equals(intent.getAction())) {
                byte[] imageBytes = intent.getByteArrayExtra("image_data");
                if (imageBytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    iv_iamge.setImageBitmap(bitmap);
                    // Use the bitmap here
                    System.out.println("SAdfgh==");
                }
            }
        }
    };

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private SharedPreferences permissionStatus;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        checkPermissions();
        btn_view=findViewById(R.id.btn_view);
        iv_iamge=findViewById(R.id.iv_iamge);

        IntentFilter filter = new IntentFilter("com.example.SEND_IMAGE");
        registerReceiver(imageReceiver, filter);







        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent=new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("caNumber","80000002735");
                intent.putExtra("headerTitle","DEMO DATA");
                startActivity(intent);*/

               // Context context,String CA, String billTyp, double totalPayAmount, String curBilDate, double arrear, String prevblDate, String duedate, double BillBefDigRbt, double blBfrDueDt,int i


                String clientId="188189a4-48ae-4011-8941-cead0fa4e683";
                String clientSecret="44c81ff8-ae45-4a5c-833b-02e021f4069a";
                String resource="AppEncoder";
                String appId="47d21dc7-788c-4402-872d-bc55dac0e146";
                String token="2262cb58-76cf-4a9f-b7b8-bd4ff207f18f";
                String type="BILLQR";
                String discom="TPCODL";





               // CommonMethod.callGetTokenAPILogin();



          Bitmap bitmap= CommonMethod.callGetTokenAPI(MainActivity.this,"80029475342","S",26.3,"22-08-2024",100.0,"22-07-2024","09-09-2024",200.0,200.0,0,"https://authservice.tpcentralodisha.com/","https://tqr.tpodisha.com/ScanIdAPI/",clientId,clientSecret,resource,appId,token,type,discom);


                System.out.println("cvcb==caal");
            }
        });

      //  MyLibrary.openWebView(MainActivity.this,"80000002735");


    }

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = REQUIRED_SDK_PERMISSIONS;
        }
        return p;
    }

    public void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : permissions()) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int REQUEST_CODE_FILE_UPLOAD = 5902;
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        if (permissionStatus.getBoolean(REQUIRED_SDK_PERMISSIONS[0], true)) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This app needs storage permission.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sentToSettings = true;
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                        return;
                    }
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                editor.commit();


                break;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister receiver
        unregisterReceiver(imageReceiver);
    }
}