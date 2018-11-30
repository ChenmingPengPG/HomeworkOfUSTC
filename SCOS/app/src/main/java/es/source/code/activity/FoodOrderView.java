package es.source.code.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import es.source.code.activity.FoodView;
import es.source.code.fragment.NewFoodFragment;
import es.source.code.fragment.OrderedFragment;
import es.source.code.model.User;


public class FoodOrderView extends AppCompatActivity implements View.OnClickListener{
    protected String TAG = "FoodOrderView";
    private TabLayout mtablayout;
    private ViewPager mviewpager;
    private Button button;
    private String tabnames[] = {"未下单菜","已下单菜"};
    private User user;
    //private SharedPreferences myPreference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Intent getuser = getIntent();
        user = getuser.getParcelableExtra("FromMain");
        String data = intent.getStringExtra("From FoodView");
        System.out.println(data);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.food_order_view);
        mtablayout = (TabLayout) findViewById(R.id.tab_order);
        mviewpager = (ViewPager) findViewById(R.id.viewpager_order);
        button = (Button) findViewById(R.id.pay);


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(OrderedFragment.newInstance(0));
        fragments.add(OrderedFragment.newInstance(1));

        TabPageAdapter pageAdapter =
                new TabPageAdapter(getSupportFragmentManager(),fragments, tabnames, data);
        mviewpager.setAdapter(pageAdapter);
        mtablayout.setupWithViewPager(mviewpager);

        if(data!= null){
            if(data.equals("ordered")){
                mtablayout.getTabAt(0).select();
            }else if(data.equals("look")){
                mtablayout.getTabAt(1).select();
            }
        }
        button.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay) {
            if (user != null && user.isOldUser())
                Toast.makeText(this, "老用户！7折优惠", Toast.LENGTH_SHORT).show();
            MyTaskAsync ta = new MyTaskAsync();
            ta.execute(0);
        }
    }
    public class TabPageAdapter extends FragmentPagerAdapter {  //viewpage适配器
        private ArrayList<Fragment> fragments;
        private String[] tabnames;
        private String data;

        public TabPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String tabnames[], String data) {
            super(fm);
            this.fragments = fragments;
            this.tabnames = tabnames;
            this.data = data;
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


    class MyTaskAsync extends AsyncTask<Integer, Integer, Integer>{
        @Override
        protected Integer doInBackground(Integer... params){
            try{
                Thread.sleep(6000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return 100;
        }
        @Override
        protected void onPostExecute(Integer result){
            Toast.makeText(getApplicationContext(), "pay completed!", Toast.LENGTH_SHORT).show();
            button.setClickable(false);
        }

    }
}

