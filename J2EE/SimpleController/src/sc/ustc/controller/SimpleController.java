package sc.ustc.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SimpleController extends HttpServlet {
    public void init(ServletConfig arg0) throws SecurityException{
        System.out.println("========init========");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("发送post方法");
        doGet(req, resp);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        System.out.println("发送get请求");
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

}