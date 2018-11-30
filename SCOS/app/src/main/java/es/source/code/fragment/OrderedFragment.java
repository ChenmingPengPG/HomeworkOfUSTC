package es.source.code.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.User;
import es.source.code.model.food_list;


public class OrderedFragment extends Fragment {
    private View view;
    private ListView listview;
    private List<HashMap<String, Food>> listData;
    private TextView tv;
    private int choose;
    private User user;
    int totalprice = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (container == null) return null;
        view = inflater.inflate(R.layout.ordered_fragment, container, false);
        listview = (ListView) view.findViewById(R.id.ordered_item);
        tv = (TextView)view.findViewById(R.id.ifo);
        tv.setText("Total Price:39");

        choose = getArguments().getInt("index");
        //直接拿cold和hot菜单当做未下单和下单菜了
        if(choose == 0) {
            listData = new food_list("coldfood").getData(); //后期改为获得orderedfood数据
        }else {
            listData = new food_list("hotfood").getData();
        }
        MyListAdapter myListAdapter = new MyListAdapter(this.getActivity(),
                LayoutInflater.from(this.getActivity()));

        listview.setAdapter(myListAdapter);
        System.out.println("OrderedFramentSuccess!");
        return view;
    }

    public static OrderedFragment newInstance(int index) {
        OrderedFragment f = new OrderedFragment();
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
                viewHolder.numView = (TextView) convertView.findViewById(R.id.num);
                viewHolder.button = (Button) convertView.findViewById(R.id.button);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Food f = listData.get(pos).get("name");
            viewHolder.textView.setText((String)f.getFoodName());
            viewHolder.imageView.setImageResource((int)f.getImgId());
            viewHolder.priceView .setText((String)String.valueOf(f.getPrice()));
            viewHolder.numView.setText("num: 1");
            viewHolder.numView.setTextColor(getResources().getColor(R.color.google_blue)); //蓝色
            viewHolder.numView.setTypeface(Typeface.DEFAULT_BOLD); //加粗
            /*TextPaint tp = viewHolder.numView.getPaint();//加粗
            tp.setFakeBoldText(true);*/

            viewHolder.button.setTag(pos);
            viewHolder.button.setFocusable(false);
            viewHolder.button.setText("退订");
            viewHolder.button.setOnClickListener(onclick);
            return convertView;
        }
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v.findViewById(v.getId());
                if( btn.getText()== "点餐"){
                    btn.setText("退点");
                    v.getId();
                    Toast.makeText(getContext(),"点餐成功", Toast.LENGTH_SHORT).show();

                }else{
                    btn.setText("点餐");
                    String p = viewHolder.priceView.getText().toString();
                    System.out.println(p);
                    float price = Float.parseFloat(p);
                    Toast.makeText(getContext(),"退点成功", Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    public class ViewHolder {
        TextView textView;
        TextView priceView;
        TextView numView;
        ImageView imageView;
        Button button;
        boolean isPressed;
    }

}