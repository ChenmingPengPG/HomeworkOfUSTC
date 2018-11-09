package es.source.code.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ServerObserverService extends Service {
    private static final String Tag = "ServerObserverService";
    public Messenger cMessenger;
    private MyHandler cMeeeengerHandler = new MyHandler();
    public Messenger sMessenger = new Messenger( cMeeeengerHandler);
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    //开始更新
                    System.out.println("Msg_Food_Get_Start");
                    System.out.println("service:获取客户端message 1 成功");
                    try {
                        for(int i = 3; i > 0; i--) {
                            Thread.sleep(300);
                            Message m = getFoodInfo();
                            System.out.println(m.what);
                            System.out.println(m.getData().get("name"));
                            System.out.println(m.getData().get("storage"));
                            cMessenger = msg.replyTo;
                            try {
                                cMessenger.send(m);
                                System.out.println("发送菜品信息成功");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    break;

                case 0:
                    Thread.interrupted();
                    System.out.println("Msg_Food_Get_Stop");
                    System.out.println("service:获取客户端message 0 成功");
                    //停止更新
                    break;
                default:

            }
        }
    }
    //考虑到要进行双向通信
    @Override
    public IBinder onBind(Intent intent){
        Log.w("TAG", "Service is binded");
        return sMessenger.getBinder();
    }
    //模拟产生的food信息
    public Message getFoodInfo(){
        Message m =  Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("name", "new food");
        int storage = (int)(Math.random()*20);
        bundle.putInt("storage", storage);
        m.setData(bundle);
        m.what = 10;
        return m;
    }
/*    class update extends Thread{
        @Override
        public void run(){
            Message msg =  cMeeeengerHandler.obtainMessage();
            msg.what = 10;
            Bundle data = new Bundle();
            data.putString("name", "new food");
            data.putInt("storage", 11);
            msg.setData(data);
            cMeeeengerHandler.sendMessage(msg);
        }
    }*/


}
