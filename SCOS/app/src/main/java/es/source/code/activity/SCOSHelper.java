package es.source.code.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.source.code.model.MailSender;
import es.source.code.model.User;

public class SCOSHelper extends AppCompatActivity implements
        AdapterView.OnItemClickListener, Runnable {
    private SimpleAdapter adapter;
    private GridView gridView;
    private Thread thread;
    private String[] title = {"用户使用协议", "关于系统", "电话人工帮助", "短信帮助", "邮件帮助"};
    private int[] icon = {R.drawable.help,R.drawable.help,R.drawable.help,R.drawable.help,R.drawable.help};
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoshelp);
        gridView = (GridView)findViewById(R.id.help_view);
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < title.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", title[i]);
            dataList.add(map);
        }
        adapter=new SimpleAdapter(this, dataList, R.layout.items,
                new String[]{"image","text"},
                new int[]{R.id.itemImage, R.id.itemText});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        switch(position){
            case 0:

                break;
            case 1:

                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:5554"));
                startActivity(intent);

                break;
            case 3:
                String message = "test scos helper";
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("5554", null, message, null, null);
                Toast.makeText(this,"求助短信发送成功",Toast.LENGTH_SHORT).show();
                /*Intent sentIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:5554"));
                sentIntent.putExtra("sms_body", message);
                startActivity(sentIntent); //这个方式需要手动确认发送
                */
                break;
            case 4:
                /*Intent sendMail = new Intent(SCOSHelper.this, mailsender.class);
                startActivity(sendMail);*/
                thread = new Thread(SCOSHelper.this);
                thread.start();
                break;
            case 5:
                break;
            default:
        }
    }
    @Override
    public void run(){
        MailSender sender = new MailSender();
        sender.run();
    }
}
