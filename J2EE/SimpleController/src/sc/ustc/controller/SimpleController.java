package sc.ustc.controller;

import org.omg.PortableInterceptor.Interceptor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class SimpleController extends HttpServlet {
    public void init(ServletConfig arg0) throws SecurityException{
        System.out.println("========init========");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("发送post方法");
        //第二次添加
        //解析url获取请求action名称
        String url = req.getRequestURL().toString();
        System.out.println(url);
        String actionStr = url.substring(url.lastIndexOf('/')+1, url.length()-3);
        String resultStr = "";
        //action查找状态
        boolean findAction = false;
        //result查找状态
        boolean findResult = false;
        //查找不到action或result的页面提示语句
        String erroMsg;
        List<Action> actionList = null;
        List<Interceptor> interceptorList = null;

        //获取参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        //通过工具类获取配置文件解析结果
        //ControllerResolveHelper

        for(Action action : actionList){
            if(action.getName().equals(actionStr));
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String title = "SimpleController";
        String body = "欢迎使用SimpleController";
        String docType = "<!DOCTYPE html> \n";
        out.println(docType +
                "<html> \n" +
                "<head><meta charset = \"utf-8\"><title>" + title +"</title></head>\n" +
                "<body><meta charset = \"utf-8\">" + body + "</body>\n" +
                "</html>"
        );

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        System.out.println("发送get请求");
        doPost(req, resp);
    }

}