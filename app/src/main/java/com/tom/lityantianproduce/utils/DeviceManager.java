package com.tom.lityantianproduce.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tom.lityantianproduce.receiver.TomReceiver;

class DeviceManger {

    private DevicePolicyManager devicePolicyManager;
    private Context mContext;
    private ComponentName componentName;

    public DeviceManger(Context context) {
        mContext = context;
        //获取设备管理服务
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //DeviceReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(context, TomReceiver.class);
    }


    // 测试
    public void test(Context context) {
        DevicePolicyManager dm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

    }

    // 激活设备管理器
    public void enableDeviceManager() {
        //判断是否激活  如果没有就启动激活设备
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "激活设备管理器");
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "设备已经激活,请勿重复激活", Toast.LENGTH_SHORT).show();
        }
    }
}
