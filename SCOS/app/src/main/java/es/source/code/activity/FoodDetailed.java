package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.source.code.model.Food;

public class FoodDetailed extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_desc, tv_beizhu;
    private ImageView img;
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detailed);
        Intent intent = getIntent();
        Food food = (Food)intent.getParcelableExtra("newfood");
        tv_desc = (TextView)findViewById(R.id.describe);
        tv_beizhu = (TextView)findViewById(R.id.beizhu);
        button = (Button)findViewById(R.id.button);
        tv_desc.setText("This is a new kind of food!"+
        "name: "+ food.getFoodName() +" \n" +
        "price: "+food.getPrice()+"\n"+
        "storage: " + food.getStore());
        tv_beizhu.setText("备注： ");
        if(food.getOredered()){
            button.setText("退点");
        }else{
            button.setText("点菜");
        }
        button.setOnClickListener(this);
    }
    public void onClick(View v){
        if(button.getText().equals("点菜")){
            button.setText("退点");
            Toast.makeText(this, "点菜成功",Toast.LENGTH_SHORT).show();
        }else{
            button.setText("点菜");
            Toast.makeText(this, "退点成功",Toast.LENGTH_SHORT).show();
        }
    }
}
