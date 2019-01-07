package sc.ustc.controller;

import sc.ustc.items.*;
import sc.ustc.utils.*;
import sc.ustc.myInterface.ExecuteHelper;

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
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class SimpleController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String XML_RELATIVE_PATH = "/WEB-INF/classes/controller.xml";

    public static final String SUCCESS = "sucess";

    public static final String FAILURE = "failure";

    public static final String TYPE_FORWARD = "forward";

    public static final String TYPE_REDIRECT = "redirect";
    //action查找状态
    boolean findAction = false;
    //result查找状态
    boolean findResult = false;
    String url = "";

    public void init(ServletConfig arg0) throws SecurityException{
        System.out.println("========init========");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("发送post方法");
        //第二次添加
        //解析url获取请求action名称
        url = req.getRequestURL().toString();
        System.out.println("url:" + url);
        String actionStr = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
        System.out.println("last action:" + actionStr);
        String resultStr = "";
        ServletContext context = req.getServletContext();


        //开始解析本地的xml文件来获得和action匹配的标签
        try {
            ControllerXMLHelper helper = new ControllerXMLHelper(XML_RELATIVE_PATH, context);
            List<Action> actions = helper.getActions(); //获取所有的actions
            List<Interceptor> interceptors = helper.getInterceptors(); //获取所有的Interceptors
            System.out.println(interceptors.get(0).getClassName());
            Action action = searchAction(actions, actionStr);
            //resultStr = getResultStringByActionProxy(action, interceptors);
            resultStr = getResultStringByAction(action, interceptors, req, resp);
            Result result = searchResult(action, resultStr);
            doWithResult(result, req, resp, context);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }  catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        doWithFailed(resp);
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
    public void doWithResult(Result result, HttpServletRequest req, HttpServletResponse resp,
                                ServletContext context) throws ServletException, IOException, ParserConfigurationException, SAXException {
        if(result == null)return;
        InputStream is = context.getResourceAsStream(result.getValue());

        if (result.getName().equals("success")) {
            // 使用forward方式
            if(result.getValue().endsWith("_view.xml")){
                System.out.println("!!!!!!!!!!!!!!!!");
                XMLToHTMLHelper helper = new XMLToHTMLHelper(result.getValue(),context);
                String htmlString = helper.parseXML().produceHTML();
                PrintWriter out = resp.getWriter();
                out.print(htmlString);
            }else{
                if (url.indexOf("pages") == -1) {
                    req.getRequestDispatcher(result.getValue()).forward(req, resp);
                } else {
                    String res = result.getValue();
                    String rr =res.substring(res.lastIndexOf('/')+1);
                    req.getRequestDispatcher(rr).forward(req, resp);
                }
            }
        } else {
            // 使用redirect方式
            resp.sendRedirect("//localhost:8080/" + result.getValue());
        }
    }
    /**
     *	失败的处理
     * @param resp
     * @throws IOException
     */
    public void doWithFailed(HttpServletResponse resp) throws IOException {
        if (findAction == false || findResult == false) {
            String message = (findAction == false ? "Can't find the request of action!"
                    : "No resource of request(result)!");
            PrintWriter out = resp.getWriter();
            out.write("<html><head><title>COS</title></head><body>" + message + "</body></html>");
        }
    }

    public Action searchAction(List<Action> actions, String actionStr){
        for(Action action : actions){
            if(action.getName().equals(actionStr)) {
                findAction = true;
                return action;
            }
        }
        findAction = false;
        return null;
    }

    public Result searchResult(Action action,String resultString) {
        if(action == null || resultString == null) {
            return null;
        }
        List<Result> results = action.getResultNodes();
        for(Result result:results) {
            if(result.getName().equals(resultString)){
                findResult = true;
                return result;
            }
        }

        //没有找到
        findResult = false;
        return null;
    }

    public String getResultStringByAction(Action action, List<Interceptor> interceptors, HttpServletRequest req, HttpServletResponse resp) throws
            ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InstantiationException {
        if(action == null)return null;

        // 获得类名和方法名
        String className = action.getClassName();
        String methodName = action.getMethodName();
        System.out.println(className);
        System.out.println(methodName);
        System.out.println("------------");

        List<InterceptroREF> list = action.getInterceptroRefNodes();//获得需要拦截的拦截器
        System.out.println(list.size());
        for(InterceptroREF ll : list){
            System.out.println(ll.getName());
        }
        Stack<ProxyStackElement> stack = new Stack<>(); //拦截器存在stack中用于执行后面的after方法
        for(InterceptroREF interceptorRef : list){
            Interceptor interceptor = null;
            for(Interceptor interceptor_ite : interceptors){
                if(interceptor_ite.getName().equals(interceptorRef.getName())){
                    interceptor = interceptor_ite;
                }
            }
            Class<?> interceptorClass = Class.forName(interceptor.getClassName());
            Object interceptorInstance = interceptorClass.newInstance();
            //该方法中含有类型为Action的参数
            Method pre = interceptorClass.getMethod(interceptor.getPreMethodName(), Action.class);
            Method after = interceptorClass.getMethod(interceptor.getAfterMethodName(), Action.class);

            pre.invoke(interceptorInstance, action);
            stack.push(new ProxyStackElement(interceptorInstance, after));
        }
        // 利用反射创建类对象
        Class<?> c = Class.forName(className);
        Method method = c.getMethod(methodName,HttpServletRequest.class, HttpServletResponse.class);
        String result = (String)method.invoke(c.newInstance(), req, resp);
        action.setResult(result);
        //System.out.println(stack.size());
        while(!stack.isEmpty()) {
            ProxyStackElement element = stack.pop();
            element.getAfterMethod().invoke(element.getInterceptorInstance(),action);
        }
        return result;
    }
    public String getResultStringByActionProxy(Action action, List<Interceptor> interceptors){
        if(action == null)return null;
        //初始化获得要执行的拦截器列表
        action.searchInterceptors(interceptors);

        //要被代理的对象，实际上只是实现了一个什么也不做的接口
        //所有要做的内容都在代理中实现了，包括获得结果的方法，在代理中也已经使用反射实现
        ExecutHelperImp executer = new ExecutHelperImp();
        ProxyHandler handler = new ProxyHandler(executer);
        ExecuteHelper imp = (ExecuteHelper) Proxy.newProxyInstance(ExecutHelperImp.class.getClassLoader(), executer.getClass().getInterfaces(), handler);
        return imp.doNothing(action);
    }

}