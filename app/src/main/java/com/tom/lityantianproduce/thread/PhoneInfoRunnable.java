package com.tom.lityantianproduce.thread;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.tom.lityantianproduce.utils.SocketClient;
import com.tom.lityantianproduce.utils.StorageTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PhoneInfoRunnable implements Runnable {

    private Context context;
    private Map<String, String> phone_dic_map;

    public PhoneInfoRunnable(Context context) {

        this.context = context.getApplicationContext();
        phone_dic_map = new HashMap<>();
    }

    @Override
    public void run() {
        getAppVersion();
        getPhoneType();
        getSN();
        getDisplaySize();
        getIPAddress();
        getSDCardExit();
        getMemory();
        getRootState();
        getFromBuild();
        getPhoneScreenSize();

        StorageTools.StoreInfoFile(context, "Litphone.dat", phone_dic_map);
        System.out.println("j");
    }

    /**
     * 获取App版本
     */
    private void getAppVersion() {
        try {
            String versionCode = String.valueOf(this.context.getPackageManager().getPackageInfo(context.getPackageName(), 0).getLongVersionCode());
            phone_dic_map.put("LitAppVersion", versionCode);
        } catch (Exception e) {

        }
    }

    /**
     * 获取网络类型和IMEI
     */
    private void getPhoneType() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int callState = telephonyManager.getCallState();            //  返回设备的呼叫状态.0 CALL_STATE_IDLE=空闲状态
        phone_dic_map.put("CallState", callState+"");

        int dataActivity = telephonyManager.getDataActivity();      //  查询设备当前的数据连接活动状态（如 2G、3G、4G、LTE、NR（5G）等）
        phone_dic_map.put("DataActivity", dataActivity+"");

        int dataState = telephonyManager.getDataState();            //
        phone_dic_map.put("DataState", dataState+"");

        String networkCountryIso = telephonyManager.getNetworkCountryIso();
        phone_dic_map.put("NetworkCountryIso", networkCountryIso);

        String networkOperator = telephonyManager.getNetworkOperator();
        phone_dic_map.put("NetworkOperator", networkOperator);

        String networkOperatorName = telephonyManager.getNetworkOperatorName();
        phone_dic_map.put("NetworkOperatorName", networkOperatorName);

        String simCountryIso = telephonyManager.getSimCountryIso();
        phone_dic_map.put("SimCountryIso", simCountryIso);

        String simOperator = telephonyManager.getSimOperator();
        phone_dic_map.put("SimOperator", simOperator);

        String simOperatorName = telephonyManager.getSimOperatorName();
        phone_dic_map.put("SimOperatorName", simOperatorName);

        String simSerialNumber = telephonyManager.getSimSerialNumber();
        phone_dic_map.put("SimSerialNumber", simSerialNumber);
        int simState = telephonyManager.getSimState();
        phone_dic_map.put("SimState", simState+"");

        String voiceMailNumber = telephonyManager.getVoiceMailNumber();
        phone_dic_map.put("VoiceMailNumber", voiceMailNumber);

        String voiceMailAlphaTag = telephonyManager.getVoiceMailAlphaTag();
        phone_dic_map.put("VoiceMailAlphaTag", voiceMailAlphaTag);

        boolean isNetworkRoaming = telephonyManager.isNetworkRoaming();
        phone_dic_map.put("isNetworkRoaming", isNetworkRoaming+"");
        String imei = telephonyManager.getDeviceId();
        phone_dic_map.put("IMEI", imei);
        String SWVersion = telephonyManager.getDeviceSoftwareVersion();
        phone_dic_map.put("SWVersion", SWVersion);
        int networkType = telephonyManager.getNetworkType();
        String networkTypeStr = "";
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                networkTypeStr = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                networkTypeStr = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                networkTypeStr = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                networkTypeStr = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                networkTypeStr = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                networkTypeStr = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE: // 这些通常是GSM网络类型
                // GSM网络处理
//                networkTypeStr = "GPRS";
                phone_dic_map.put("PhoneType", "GPRS");
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                networkTypeStr = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                networkTypeStr = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                networkTypeStr = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B: // 这些是CDMA网络类型
                // CDMA网络处理
//                networkTypeStr = "CDMA";
                phone_dic_map.put("PhoneType", "CDMA");
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                networkTypeStr = "UNKNOWN";
                break;
            // 其他网络类型...
        }
        phone_dic_map.put("NetworkType", networkTypeStr);
    }

    private void getSN() {
        String serialNumber = Build.SERIAL;
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

//        phone_dic_map.put("SerialNumber", serialNumber);
    }

    private void getDisplaySize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        display.getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        StringBuffer displayStr = new StringBuffer();
        displayStr.append(width);
        displayStr.append("x");
        displayStr.append(height);
        phone_dic_map.put("DisplaySize", displayStr.toString());
        Log.i("Hi", "hi");
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
    }

    /**
     * 获取IP Addr
     */
    private void getIPAddress() {
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        phone_dic_map.put("WifiMac", macAddress);


    }

    private void getSDCardExit() {
        if (!Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            phone_dic_map.put("SDCardExist", "NO");
        } else {
            phone_dic_map.put("SDCardExist", "YES");
        }

        File exterStorge = Environment.getExternalStorageDirectory();
        if (exterStorge.getPath() != null) {
            phone_dic_map.put("ExternalSDCardPath", exterStorge.getPath());
        } else {
            phone_dic_map.put("ExternalSDCardPath", "");

        }
    }

    private void getMemory() {
        File mFile = Environment.getDataDirectory();
        StatFs mStatFs = new StatFs(mFile.getPath());
        long mBlkSize = mStatFs.getBlockSize();
        long mAvaBlk = mStatFs.getAvailableBlocks();
        long AvaSize = mAvaBlk * mBlkSize;
        phone_dic_map.put("availableMemorySize", AvaSize+"");

        phone_dic_map.put("totalMemorySize", getPhoneTotalSize());

        phone_dic_map.put("totalRamSize", getTotalRam());
    }

    public String getPhoneTotalSize() {
        File mFile = Environment.getDataDirectory();
        StatFs mStatFs = new StatFs(mFile.getPath());
        long mBlkSize = mStatFs.getBlockSize();
        long mAvaBlk = mStatFs.getAvailableBlocks();
        long BlkCount = mStatFs.getBlockCount();
        long AvaSize = mAvaBlk * mBlkSize;
        long DataTotaileSize = BlkCount * mBlkSize;

        //get system partition size
        mFile = Environment.getRootDirectory();
        mStatFs = new StatFs(mFile.getPath());
        long RootTotaileSize = mStatFs.getBlockCount() * mStatFs.getBlockSize();

        //get cache partition size
        mFile = Environment.getDownloadCacheDirectory();
        mStatFs = new StatFs(mFile.getPath());
        long CacheTotaileSize = mStatFs.getBlockCount() * mStatFs.getBlockSize();

        //get tmpfs partition size
        mStatFs = new StatFs("/dev");
        long DevTotaileSize = mStatFs.getBlockCount() * mStatFs.getBlockSize();
        //Log.i(TAG, "DataTotaileSize = "+DataTotaileSize);
        long allSize = DataTotaileSize + RootTotaileSize + CacheTotaileSize + DevTotaileSize;
        return String.valueOf(DataTotaileSize); //modify by liuzhenting 20180723

    }

    public String getTotalRam() {
        String path = "/proc/meminfo";
        String firstLine = null;
        String totalRamString = null;
        int totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLine != null) {
            //totalRam = (int)Math.ceil((new Float(Float.valueOf(firstLine) / (1024 * 1024)).doubleValue()));
            totalRam = (int) Math.ceil((new Float(Float.valueOf(firstLine) / 1024).doubleValue()));
//            Log.i(TAG, "totalRam = " + totalRam);
            if (totalRam > 1024) {
                totalRam = (int) Math.ceil((Float.valueOf(totalRam) / 1024));
//                Log.i(TAG, "totalRam1024 = " + totalRam);
                totalRamString = totalRam + "GB";
            } else if (totalRam > 512) {
                totalRam = 1;
                totalRamString = totalRam + "GB";
            } else if (totalRam > 256) {
                totalRamString = 512 + "MB";
            } else if (totalRam > 128) {
                totalRamString = 256 + "MB";
            } else {
                totalRamString = 128 + "MB";
            }
        }
        return totalRamString;
    }

    private void getRootState() {
        int root = checkRoot();
        String rootResult = "unroot";
        if (0 == root) {
            rootResult = "unroot";
        } else if (1 == root) {
            rootResult = "root";
        } else {
            rootResult = "failure";
        }
        phone_dic_map.put("ROOT", rootResult);
    }

    private void getFromBuild() {
        String board = Build.BOARD;
        phone_dic_map.put("BOARD", board);

        String bootloader = Build.BOOTLOADER;
        phone_dic_map.put("BOOTLOADER", bootloader);

        String brand = Build.BRAND;
        phone_dic_map.put("BRAND", brand);
        phone_dic_map.put("CPU_ABI", Build.CPU_ABI);
        phone_dic_map.put("CPU_ABI2", Build.CPU_ABI2);
        phone_dic_map.put("DEVICE", Build.DEVICE);
        phone_dic_map.put("DISPLAY", Build.DISPLAY);
        phone_dic_map.put("FINGERPRINT", Build.FINGERPRINT);
        phone_dic_map.put("HARDWARE", Build.HARDWARE);
        phone_dic_map.put("HOST", Build.HOST);
        phone_dic_map.put("ID", Build.ID);
        phone_dic_map.put("MANUFACTURER", Build.MANUFACTURER);
        phone_dic_map.put("MODEL", Build.MODEL);
        phone_dic_map.put("PRODUCT", Build.PRODUCT);
        phone_dic_map.put("RADIO", Build.RADIO);
        phone_dic_map.put("TAGS", Build.TAGS);
        phone_dic_map.put("TIME", String.valueOf(Build.TIME));
        phone_dic_map.put("TYPE", Build.TYPE);
        phone_dic_map.put("USER", Build.USER);
        phone_dic_map.put("CODENAME", Build.VERSION.CODENAME);
        phone_dic_map.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
        phone_dic_map.put("AndroidVersion", Build.VERSION.RELEASE);
        phone_dic_map.put("SDK", Build.VERSION.SDK);

    }

    private void getPhoneScreenSize() {
        Configuration config = context.getApplicationContext().getResources().getConfiguration();
        int smallestScreenWidthDp = config.smallestScreenWidthDp;
        if (smallestScreenWidthDp >= 600) {
            // 可以认为是平板设备
            phone_dic_map.put("isTablet", 1+"");
        } else {
            // 可以认为是手机设备
            phone_dic_map.put("isTablet", 0+"");
        }
    }

    public int checkRoot() {
        int i = 1;
        try {
            String str = Build.TAGS;
            if ((str != null) && (str.contains("test-keys"))) {
                return i;
            }
            if (!new File("/system/app/Superuser.apk").exists()) {
                String[] arrayOfString = { "/system/bin/", "/system/xbin/",
                        "/system/sbin/", "/sbin/", "/vendor/bin/" };
                for (int j = 0;; j++) {
                    if (j >= arrayOfString.length) {
                        return 0;
                    }
                    File localFile = new File(arrayOfString[j] + "su");
                    if (localFile != null) {
                        boolean bool = localFile.exists();
                        if (bool) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            i = 2;
        }
        return i;
    }

}
