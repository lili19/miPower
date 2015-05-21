package com.awong.micpower;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Broadcast_receiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent_service = new Intent(context, com.awong.micpower.MicService.class);
            context.startService(intent_service);
        }
    }
}
