package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.R;
import es.source.code.model.Food;

public class UpdateService extends IntentService {
    public UpdateService() {
        super("UpdateService");
    }

    public UpdateService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //update foodview;
        System.out.println("onHandleIntent");
        Bundle bundle =  (Bundle)intent.getBundleExtra("new food");
        String name = (String)bundle.get("name");
        int storage = (int)bundle.get("storage");
        System.out.println(name+storage);
        Food food = new Food("new food", 13, 10, R.drawable.order_item_logo,
                false);
        sendNotification(food);
    }
    private void sendNotification(Food food){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("channel_01", "name",
                    NotificationManager.IMPORTANCE_HIGH);
            Intent resultIntent = new Intent(this,FoodDetailed.class);
            resultIntent.putExtra("newfood", food);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                    resultIntent,PendingIntent.FLAG_IMMUTABLE);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId("channel_01")
                    .setContentTitle("新菜")
                    .setContentText("New food.name:"+food.getFoodName()+
                            " price:" + food.getPrice() +" kind:"+"coldfood")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationManager.notify(1, notification);
        } else {
            Intent resultIntent = new Intent(this,FoodDetailed.class);
            //intent可以携带参数到指定页面的，这里省略
            //resultIntent.putExtra(key,value);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                    resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("新菜")
                    .setContentText("New food.name:"+food.getFoodName()+
                            " price:" + food.getPrice() +" kind:"+"coldfood")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setChannelId("channel_01")//无效
                    .setContentIntent(pendingIntent);
            notification = notificationBuilder.build();
            notificationManager.notify(1, notification);
        }

        System.out.println("builded");
    }

}

