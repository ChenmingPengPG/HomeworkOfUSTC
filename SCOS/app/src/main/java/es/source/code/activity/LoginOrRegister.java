package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern; //用于正则判断
import java.util.regex.Matcher;

import es.source.code.model.User;

import static android.view.View.GONE;


public class LoginOrRegister extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLog, buttonReg;
    private ProgressBar progressBar;
    private EditText user_text, pass_text;
    private SharedPreferences myPreference;
    private Thread thread;
    private User user;
    private int progress = 0;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if(progress < 100){
                progress += 10;
                handler.sendEmptyMessage(0X111);
            }else if(progress >= 100){
                progress = 0;
                handler.sendEmptyMessage(0X222);
                task.cancel();
                timer.cancel();
            }
        }
    };
    Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg){
          super.handleMessage(msg);
          switch(msg.what){
              case 0x111:
                  progressBar.setProgress(progress);
                  break;
              case 0x222:
                  progressBar.setVisibility(GONE);
                  break;
          }
      }
    };
    //Java 中，非静态的内部类和匿名类都会隐式地持有一份对外部类的引用，而静态的内部类则不会包含对外部类的引用。
    /*也就是说，错误提示中的匿名 Handler 包含着对外部类(Activity 类)的引用。如果你给这个 Handler 发一个延迟执行的
     Message，Message 会被添加进主线程 Looper 的 MessageQueue 中，且 Message 持有对 Handler 的引用。
     然而在延迟时间到达之前，Activity 可能已经被返回键等等 finish() 掉了。
     此时这个 Activity 对象已经没用啦，应该被 Garbage Collector 回收。
     然而 GC 扫描到这个 Activity 对象时，发现还有其他对象(我们可爱的 Handler)在引用它，因此不会回收这个 Activity，于是造成了内存泄露。
    https://blog.csdn.net/banxiali/article/details/51494842
     */

    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_register);

        //读取myPreference中保存的user数据
        myPreference = getSharedPreferences("myPreference", Context.MODE_PRIVATE);

        String userIfo = myPreference.getString("userIfo", "none");
        if(userIfo.equals("none")){
            System.out.println("用户未登陆过");
            user = new User();
        }else{
            Gson gson = new Gson();
            user = gson.fromJson(userIfo, User.class);
            System.out.println(user.getUserName());
        }
        buttonLog = (Button) findViewById(R.id.button_login);
        buttonReg = (Button) findViewById(R.id.button_register);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        user_text = (EditText)findViewById(R.id.usernum_text);
        pass_text = (EditText)findViewById(R.id.password_text);
        if(!user.isOldUser()) {   //新用户
            buttonLog.setVisibility(GONE);
        }else{
            buttonReg.setVisibility(GONE);
            user_text.setText(user.getUserName());
        }

        buttonLog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(LoginOrRegister.this, MainScreen.class);
        String data_back = "return";
        String data_suc = "LoginSuccess";
        String data_suc_reg = "RegisterSuccess";
        String inputText = user_text.getText().toString();//读取账号密码
        String passText = pass_text.getText().toString();
        SharedPreferences.Editor editor = myPreference.edit();
        switch(v.getId()){
            case R.id.button_login:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                timer.schedule(task,0,200);
                /*for(int i = 0; i < 10; i++) { //会阻塞主线程，progress到100后才会进行更新
                    //应该考虑用线程发送消息来实现更新（handler）
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(progressBar.getProgress() + 10);
                }*/
                if(!isOK(inputText)){
                    user_text.setError("输入内容不符合规则");
                }
                if(!isOK(passText)){
                    pass_text.setError("输入内容不符合规则");
                }
                if(isOK(inputText)&&isOK(passText)){
                    //更新user并存储到sp中
                    user.setUserName(inputText);
                    user.setPassword(passText);
                    user.setOldUser(true);
                    Gson gson = new Gson();
                    String userIfo = gson.toJson(user);
                    editor.putString("userIfo",userIfo);
                    editor.putInt("state", 1);
                    editor.apply();


                  /*  intent.putExtra("From Login", data_suc);
                    intent.putExtra("loginUser", user);*/
                    startActivity(intent);
                }else{
                    progressBar.setProgress(0);
                    progressBar.setVisibility(GONE);
                }
                break;


            case R.id.button_back:
                editor.putInt("state", 0);
                editor.apply();
                intent.putExtra("From Login", data_back);
                startActivity(intent);
                break;
            case R.id.button_register:
                if(!isOK(inputText)){
                    user_text.setError("输入内容不符合规则");
                }
                if(!isOK(passText)){
                    pass_text.setError("输入内容不符合规则");
                }
                if(isOK(inputText)&&isOK(passText)){
                    user.setUserName(inputText);
                    user.setPassword(passText);
                    user.setOldUser(true);
                    Gson gson = new Gson();
                    String userIfo = gson.toJson(user);
                    editor.putString("userIfo",userIfo);
                    editor.putInt("state", 1);
                    editor.apply();

                    /*intent.putExtra("From Login",data_suc_reg);
                    intent.putExtra("loginUser", user);*/
                    startActivity(intent);
                }
        }
    }

    public boolean isOK(String str){
        Pattern pattern = Pattern.compile("^[a-z0-9]+$");
        Matcher isok = pattern.matcher(str);
        if( !isok.matches()){
            return false;
        }
        return true;
    }

}
