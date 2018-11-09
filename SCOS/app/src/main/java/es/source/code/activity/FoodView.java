package es.source.code.activity;

import android.app.ActionBar;
import android.app.Service;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import es.source.code.fragment.*;

import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Food;
import es.source.code.model.User;
import es.source.code.model.food_list;
import es.source.code.service.ServerObserverService;
import es.source.code.service.UpdateService;

public class FoodView extends AppCompatActivity{
    protected String TAG = "FoodView";
    private TabLayout mtablayout;
    private ViewPager mviewpager;
    private android.support.v7.app.ActionBar ab;
    //private ArrayList fragments;
    private String tabnames[] = {"冷菜","热菜","海鲜","酒水"};
    private Intent bs;
    private food_list fl_cold;
    User user;
    private class MyHandler extends Handler{
      @Override
      public void handleMessage(final Message msg){
          if(msg.what == 10){
              System.out.println("fv:收到服务器发来的消息");
              String name = (String)msg.getData().get("name");
              int storage = (int)msg.getData().get("storage");
              food_list.coldfood_name.add(name);
              food_list.coldfood_price.add(13);
              food_list.coldfood_store.add(storage);
              Intent toUpdate = new Intent(FoodView.this, UpdateService.class);
              toUpdate.putExtra("new food", msg.getData());
              startService(toUpdate);
          }
      }
    };
    Messenger cMessenger = new Messenger(new MyHandler());
    Messenger sMessenger;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sMessenger = new Messenger(service);
            System.out.println("fv：connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sMessenger = null;
            System.out.println("fv：disconnected");
        }
    };
    boolean mbound;
    Menu menu;
    //创建AcrtionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar ,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(FoodView.this, FoodOrderView.class);
        switch(item.getItemId()){
            case R.id.action_ordered:
                intent.putExtra("From FoodView", "ordered");
                startActivity(intent);
                break;
            case R.id.action_look:
                intent.putExtra("From FoodView", "look");
                startActivity(intent);
                break;
            case R.id.action_call:
                break;
            case R.id.action_service_start:
                String transto = "默认消息";
                Message message = Message.obtain();
                if(item.getTitle().toString().equals("启动实时更新")){
                    item.setTitle("停止实时更新");
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("toService",transto);
                    message.setData(bundle);
                    message.replyTo = cMessenger;
                    try{
                        sMessenger.send(message);
                        System.out.println("fv:发送消息ed！");
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                    System.out.println("启动");

                }else{
                    item.setTitle("启动实时更新");
                    System.out.println("停止");
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("toService",transto);
                    message.setData(bundle);
                    message.replyTo = cMessenger;
                    try{
                        sMessenger.send(message);
                        System.out.println("fv:发送消息ed！");
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.food_view_2);
        mtablayout = (TabLayout) findViewById(R.id.foodview);
        mviewpager = (ViewPager) findViewById(R.id.viewPager);
        for(int i = 0; i < tabnames.length; i++){
            mtablayout.addTab(mtablayout.newTab().setText(tabnames[i]));
        }
        Intent intent = getIntent();
        user = intent.getParcelableExtra("FromMain");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(NewFoodFragment.newInstance(0));
        fragments.add(NewFoodFragment.newInstance(1));
        fragments.add(NewFoodFragment.newInstance(2));
        fragments.add(NewFoodFragment.newInstance(3));
        System.out.println("createFragmentSucccess!");
        TabPageAdapter pageAdapter  //创建适配器
                = new TabPageAdapter(getSupportFragmentManager(),fragments,tabnames);
        mviewpager.setAdapter(pageAdapter);
        mtablayout.setupWithViewPager(mviewpager);//实现TabLayout与ViewPager的联动
        //mtablayout.setOnTouchListener();
        ab = getSupportActionBar();
        bs = new Intent(this, ServerObserverService.class);
        bindService(bs, mConnection, Context.BIND_AUTO_CREATE);


    }

    public class TabPageAdapter extends FragmentPagerAdapter {  //viewpage适配器
        private ArrayList<Fragment> fragments;
        private String[] tabnames;

        public TabPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String tabnames[]) {
            super(fm);
            this.fragments = fragments;
            this.tabnames = tabnames;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //返回tab选项的名字
            return tabnames[position];
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(mConnection);
    }
}


