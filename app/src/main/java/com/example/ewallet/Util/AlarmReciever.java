package com.example.ewallet.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {
    public static final int REQUEST_CODE = 2023;
    public static final String ACTION = "com.example.ewallet.util";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent (context, FindPlaceService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
