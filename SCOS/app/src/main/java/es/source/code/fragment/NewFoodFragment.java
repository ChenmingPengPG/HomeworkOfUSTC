package es.source.code.fragment;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.food_list;

import static java.lang.String.valueOf;

public class NewFoodFragment extends Fragment {
    private View view;
    private ListView listview;
    private List<HashMap<String, Food>> listData;
    private int choose = 0;
    int totalprice = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (container == null) return null;
        view = inflater.inflate(R.layout.cold_fragment, container, false);
        listview = (ListView) view.findViewById(R.id.cood_view);
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.food_item);*/

        choose = getArguments().getInt("index");
        switch(choose){
            case 0:
                listData = new food_list("coldfood").getData();
                break;
            case 1:
                listData = new food_list("hotfood").getData();
                break;
            case 2:
                listData = new food_list("seafood").getData();
                break;
            case 3:
                listData = new food_list("drink").getData();
                break;
            default:
                listData = new food_list("coldfood").getData();
                break;
        }

        MyListAdapter myListAdapter = new MyListAdapter(this.getActivity(),
                LayoutInflater.from(this.getActivity()));
        listview.setAdapter(myListAdapter);
        /*TextView  tv = (TextView)view.findViewById(R.id.tv);
        tv.setText("hello");*/
        System.out.println("coldfragment");
        return view;
    }

    public static NewFoodFragment newInstance (int index){
        NewFoodFragment f = new NewFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        f.setArguments(bundle);
        return f;
    }
    public int getShowIndex() {
        return getArguments().getInt("index", 0);

    }

    public class MyListAdapter extends BaseAdapter {
        Context con;
        LayoutInflater li;
        ViewHolder viewHolder;
        MyListAdapter(Context context, LayoutInflater li) {
            this.con = context;
            this.li = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int pos = position;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = li.inflate(R.layout.food_item, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.title);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
                viewHolder.priceView = (TextView) convertView.findViewById(R.id.price);
                viewHolder.storageView = (TextView) convertView.findViewById(R.id.num);
                viewHolder.button = (Button) convertView.findViewById(R.id.button);
                viewHolder.button.setFocusable(false);
                viewHolder.button.setTag(pos);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Food f = listData.get(pos).get("name");
            viewHolder.textView.setText((String)f.getFoodName());

            viewHolder.storageView.setText(valueOf(f.getStore()));
            viewHolder.storageView.setTextColor(getResources().getColor(R.color.google_blue)); //蓝色
            TextPaint tp = viewHolder.storageView.getPaint();//加粗
            tp.setFakeBoldText(true);

            viewHolder.imageView.setImageResource((int)f.getImgId());
            viewHolder.priceView .setText((String)valueOf(f.getPrice()));
            viewHolder.isPressed = (boolean)f.getOredered();
            if (!viewHolder.isPressed) {
                viewHolder.button.setText("点餐");
            } else {
                viewHolder.button.setText("退订");
            }

            viewHolder.button.setOnClickListener(onclick);
            return convertView;
        }

        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v.findViewById(v.getId());
                int pos = (int)btn.getTag();
                int price = listData.get(pos).get("name").getPrice();
                if( btn.getText()== "点餐"){
                    btn.setText("退点");
                    v.getId();
                    Toast.makeText(getContext(),"点餐成功", Toast.LENGTH_SHORT).show();
                    totalprice += price;
                    System.out.println(totalprice);
                }else{
                    btn.setText("点餐");
                    String p = viewHolder.priceView.getText().toString();
                    System.out.println(p);
                    Toast.makeText(getContext(),"退点成功", Toast.LENGTH_SHORT).show();
                    totalprice -= price;
                    System.out.println(totalprice);
                }
            }
        };
    }

    public class ViewHolder {
        TextView textView;
        TextView priceView;
        TextView storageView;
        ImageView imageView;
        Button button;
        boolean isPressed;
    }

}