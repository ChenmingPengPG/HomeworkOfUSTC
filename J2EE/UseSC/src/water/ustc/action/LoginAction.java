package water.ustc.action;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class LoginAction {
    private String userName;
    private String password;
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;

    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String handleLogin(){
        /*boolean flag = true;
        System.out.println("-----登录用户信息-----");
        System.out.println("--用户名："+userName);
        System.out.println("--密码："+password);
        Pattern passwd_pattern = Pattern.compile("[0-9]*");
        Pattern user_pattern = Pattern.compile("[A-Z]*");
        Matcher isPassOK = passwd_pattern.matcher(password);
        Matcher isUserOk = user_pattern.matcher(userName);
        if(!isPassOK.matches()){
            System.out.println("password illegal");
            flag = false;
        }else if(!isUserOk.matches()){
            System.out.println("username illegal");
            flag = false;
        }
        if(flag){
            return "success";
        }else{
            return "failure";
        }*/
        return "success";
    }

}
