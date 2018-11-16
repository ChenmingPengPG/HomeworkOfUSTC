package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private User user;
    private int progress = 0;
    Intent intent;
    final String data_back = "return";
    final String data_suc = "LoginSuccess";
    final String data_suc_reg = "RegisterSuccess";
    String inputText;
    String passText;
    SharedPreferences.Editor editor;
    public class MyThread extends Thread {
        int info;
        public MyThread(int info){
            super();
            this.info = info;
        }
        @Override
        public void run() {
            buttonLog.getTag();
            while(progress <= 100){
                progress += 10;
                EventBus.getDefault().post(new updateProgress(progress));
                try{
                    Thread.sleep(200);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
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
                editor.putInt("state", info);
                editor.apply();
                  /*  intent.putExtra("From Login", data_suc);
                    intent.putExtra("loginUser", user);*/
                startActivity(intent);
            }else{
                progressBar.setVisibility(GONE);
                progressBar.setProgress(0);
            }
        }
    };
    /*Timer timer = new Timer();
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
    };*/
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
        intent = new Intent(LoginOrRegister.this, MainScreen.class);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(updateProgress event){
        progressBar.setProgress(event.getMsg());
    }
    public class updateProgress {
        private int mMsg;
        public updateProgress(int msg) {
            mMsg = msg;
        }
        public int getMsg(){
            return mMsg;
        }
    }
    @Override
    public void onClick(View v){
        inputText = user_text.getText().toString();//读取账号密码
        passText = pass_text.getText().toString();
        editor = myPreference.edit();
        switch(v.getId()){
            case R.id.button_login:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                MyThread updateProgressThread = new MyThread(1);//1表示已登录
                updateProgressThread.start();
                SendDataByPost();
               /* timer.schedule(task,0,200);*/
                /*for(int i = 0; i < 10; i++) { //会阻塞主线程，progress到100后才会进行更新
                    //应该考虑用线程发送消息来实现更新（handler）
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(progressBar.getProgress() + 10);
                }*/
                break;
            case R.id.button_back:
                editor.putInt("state", 0);
                editor.apply();
                intent.putExtra("From Login", data_back);
                startActivity(intent);
                break;
            case R.id.button_register:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                MyThread updateProgressThread_1 = new MyThread(1);
                updateProgressThread_1.start();
                SendDataByPost();
                break;
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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public String SendDataByPost(){
        URL url = null ;//"http://localhost:8080/SCOSServer/Servers";
        String result="";//要返回的结果
        try {
            url=new URL("http://localhost:8080/SCOSServer/Servers"+"?userName="+user.getUserName()
            +"&passWord="+user.getPassword());
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(2000);//设置连接超时时间，单位ms
            httpURLConnection.setReadTimeout(2000);//设置读取超时时间，单位ms

            //设置是否向httpURLConnection输出，因为post请求参数要放在http正文内，所以要设置为true
            httpURLConnection.setDoOutput(true);

            //设置是否从httpURLConnection读入，默认是false
            httpURLConnection.setDoInput(true);

            //POST请求不能用缓存，设置为false
            httpURLConnection.setUseCaches(false);

            //传送的内容是可序列化的
            //如果不设置此项，传送序列化对象时，当WEB服务默认的不是这种类型时，会抛出java.io.EOFException错误
            httpURLConnection.setRequestProperty("Content-type","application/String");

            //设置请求方法是POST
            httpURLConnection.setRequestMethod("POST");

            //连接服务器
            httpURLConnection.connect();

            //getOutputStream会隐含调用connect()，所以不用写上述的httpURLConnection.connect()也行。
            //得到httpURLConnection的输出流
            OutputStream os= httpURLConnection.getOutputStream();

            //构建输出流对象，以实现输出序列化的对象
            ObjectOutputStream objOut=new ObjectOutputStream(os);

            //dataPost类是自定义的数据交互对象，只有两个成员变量

            //向对象输出流写出数据，这些数据将存到内存缓冲区中
            objOut.writeObject(user);

            //刷新对象输出流，将字节全部写入输出流中
            objOut.flush();

            //关闭流对象
            objOut.close();
            os.close();

            //将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端，并获取访问状态
            /*if(HttpURLConnection.HTTP_OK==httpURLConnection.getResponseCode()){

                //得到httpURLConnection的输入流，这里面包含服务器返回来的java对象
                InputStream in=httpURLConnection.getInputStream();

                //构建对象输入流，使用readObject()方法取出输入流中的java对象
                ObjectInputStream inObj=new ObjectInputStream(in);
                data= (dataPost) inObj.readObject();

                //取出对象里面的数据
                result=data.password;

                //输出日志，在控制台可以看到接收到的数据
                Log.w("HTTP",result+"  :by post");

                //关闭创建的流
                in.close();
                inObj.close();
            }else{
                Log.w("HTTP","Connction failed"+httpURLConnection.getResponseCode());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

