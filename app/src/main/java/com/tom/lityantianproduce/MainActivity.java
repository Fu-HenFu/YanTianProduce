package com.tom.lityantianproduce;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tom.lityantianproduce.thread.PhoneInfoRunnable;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 0;

    private static final int REQUEST_FINE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        findViewById(R.id.action_btn).setOnClickListener(this);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks ();

        checkPermission();
//        getPhoneInfo();



    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_READ_PHONE_STATE_PERMISSION);

        }

//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 权限未被授予，向用户请求权限
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},
//                    REQUEST_READ_PHONE_STATE_PERMISSION);
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // 如果没有授予权限，则请求权限
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_FINE_LOCATION_PERMISSION);
//        }
    }

    private void getPhoneInfo() {
        PhoneInfoRunnable runnable = new PhoneInfoRunnable(getApplicationContext());
        new Thread(runnable).start();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 两个权限都被授予了
            } else {
                // 权限被拒绝，你可以向用户解释为什么需要这些权限，并引导用户重新授权
            }
        }
    }
}