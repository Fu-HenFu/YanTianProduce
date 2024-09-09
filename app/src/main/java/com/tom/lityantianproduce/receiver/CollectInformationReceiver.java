package com.tom.lityantianproduce.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tom.lityantianproduce.thread.PhoneInfoRunnable;

public class CollectInformationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        PhoneInfoRunnable runnable = new PhoneInfoRunnable(context.getApplicationContext());
        new Thread(runnable).start();
    }
}
