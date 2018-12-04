package sc.ustc.controller;

import javax.servlet.ServletException;
import java.io.IOException;

public class mmm {
    public static void main(){
        System.out.println("first java web!");
        SimpleController a = new SimpleController();
        try{
            a.init();
        }catch (ServletException se){
            return ;
        }

    }
}
