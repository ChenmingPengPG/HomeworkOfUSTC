package es.source.code.br;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import es.source.code.service.UpdateService;



public class DeviceStartedListener extends BroadcastReceiver {

    private static final String TAG = "DeviceStartedListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context, UpdateService.class);
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int hasPermission = 0;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasPermission = context.checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED);
                if(hasPermission != PackageManager.PERMISSION_GRANTED) {

                }
                context.startForegroundService(serviceIntent);
            } else{
                context.startService(serviceIntent);
            }
        } else if (intent.getAction().equals("scos.intent.action.CLOSE_NOTIFICATION")) {
            NotificationManager notifyManager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notifyManager.cancel(intent.getIntExtra("notification_id", 0));
        }
    }
}
