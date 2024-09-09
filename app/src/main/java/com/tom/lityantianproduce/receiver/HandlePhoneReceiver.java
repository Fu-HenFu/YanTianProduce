package com.tom.lityantianproduce.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.tom.lityantianproduce.utils.StorageTools;

public class HandlePhoneReceiver extends BroadcastReceiver {

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mDeviceAdmin;
    private String TAG = "HandlePhoneReceiver";

    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        mDevicePolicyManager = ((DevicePolicyManager) context.getApplicationContext().getSystemService("device_policy"));
        this.context = context;
        resetPhone();
    }

    void resetPhone() {
        mDeviceAdmin = new ComponentName(context, TomReceiver.class);
        String result = "PhoneReset" + "=" + "FALSE";
        if (!mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
            result = result + "\n" + "FailedResult=PhoneAdmin";
        } else {
            result = "PhoneReset" + "=" + "OK";
        }

        StorageTools.StoreResultFile(this.context, result);

        Log.i(TAG, "before Reset_Phone_Factroy");
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            if (mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
                Log.d("sdcard", "wipe sdcard data");
                Log.i(TAG, "Reset_Phone_Factroy sd");
                mDevicePolicyManager.wipeData(1); // formate sdcard and data
//                mHandler.sendEmptyMessageDelayed(1, 40000);
            }
        } else {
            if (mDevicePolicyManager.isAdminActive(mDeviceAdmin)) {
                Log.i(TAG, "Reset_Phone_Factroy");
                mDevicePolicyManager.wipeData(0);
            }
        }
    }
}
