package sc.ustc.controller;

import org.omg.PortableInterceptor.Interceptor;
import sc.ustc.utils.ParseXML;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class SimpleController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String XML_RELATIVE_PATH = "\\WEB-INF\\classes\\controller.xml";

    public static final String SUCCESS = "sucess";

    public static final String FAILURE = "failure";

    public static final String TYPE_FORWARD = "forward";

    public static final String TYPE_REDIRECT = "redirect";

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
        System.out.println("url:" + url);
        String actionStr = url.substring(url.lastIndexOf('/')+1, url.length()-3);
        System.out.println("last action:"+actionStr);
        String resultStr = "";
        //action查找状态
        boolean findAction = false;
        //result查找状态
        boolean findResult = false;

        //开始解析本地的xml文件来获得和action匹配的标签
        try {
            ParseXML parseXML = new ParseXML(XML_RELATIVE_PATH, req.getSession().getServletContext());

            Node actionNode = parseXML.findActionNode(actionStr);
            if (actionNode == null) {
                // 说明没有找到匹配的action结点
                findAction = false;
            } else {
                // 找到了匹配的action结点

                // 获得action结点的属性的键值
                Map<String, String> mapAction = parseXML.getAttributionOfActionNode(actionNode);

                // 获得类名和方法名
                String className = mapAction.get(ParseXML.CLASS_TAG);
                String methodName = mapAction.get(ParseXML.METHOD_TAG);

                // 利用反射创建类对象
                Class<?> c = Class.forName(className);
                Method method = c.getMethod(methodName, null);
                String result = (String) method.invoke(c.getDeclaredConstructor(null).newInstance(null), null);

                System.out.println("result " + result);

                Node resultNode = parseXML.findResultNodeFromActionNode(actionNode, result);
                if (resultNode == null) {
                    // 没有找到匹配的result
                    findResult = false;
                } else {

                    // 找到了匹配的result
                    Map<String, String> mapResult = parseXML.getAttributionOfResultNode(resultNode);
                    System.out.println("name "+ mapResult.get("name"));
                    System.out.println("type " + mapResult.get("type"));
                    System.out.println("value " + mapResult.get("value"));

                    String type = mapResult.get(ParseXML.TYPE_TAG);
                    String value = mapResult.get(ParseXML.VALUE_TAG);
                    if (type.equals(TYPE_FORWARD)) {
                        // 使用forward方式
                        req.getRequestDispatcher(value).forward(req, resp);
                    } else {
                        // 使用redirect方式
                        System.out.println(req.getRequestURL().toString());
                        resp.sendRedirect("http://localhost:8080/"+value);
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (findAction == false || findResult == false) {
            String message = (findAction == false ? "Can't find the request of action!"
                    : "No resource of request(result)!");
            PrintWriter out = resp.getWriter();
            out.write("<html><head><title>COS</title></head><body>" + message + "</body></html>");
        }
        /*resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String title = "SimpleController";
        String body = "欢迎使用SimpleController";
        String docType = "<!DOCTYPE html> \n";
        out.println(docType +
                "<html> \n" +
                "<head><meta charset = \"utf-8\"><title>" + title +"</title></head>\n" +
                "<body><meta charset = \"utf-8\">" + body + "</body>\n" +
                "</html>"
        );*/

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        System.out.println("发送get请求");
        doPost(req, resp);
    }

    /**
     *	根据结果来跳转
     * @param result
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void doWithResult(String result, HttpServletRequest req, HttpServletResponse resp, ServletContext context) throws ServletException, IOException {
        if(result == null)return;

        InputStream is = context.getResourceAsStream(result);

        if (result.equals("success")) {
            // 使用forward方式
            req.getRequestDispatcher(result).forward(req, resp);
        } else {
            // 使用redirect方式
            resp.sendRedirect(result);
        }
    }

}