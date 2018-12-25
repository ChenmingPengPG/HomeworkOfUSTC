package sc.ustc.utils;

import sc.ustc.items.Action;
import sc.ustc.items.Interceptor;
import sc.ustc.items.ProxyStackElement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

public class ProxyHandler implements InvocationHandler {
    Object target;

    public ProxyHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Action action = (Action) args[0]; //第一个参数是Action
        List<Interceptor> list = action.getInterceptors(); //获得需要执行的拦截器
        Stack<ProxyStackElement> stack = new Stack<>(); //拦截器存在stack中用于执行后面的after方法

        for(Interceptor interceptor : list){
            Class<?> interceptorClass = Class.forName(interceptor.getClassName());
            Object interceptorInstance = interceptorClass.newInstance();
            //该方法中含有类型为Action的参数
            Method pre = interceptorClass.getMethod(interceptor.getPreMethodName(), Action.class);
            Method after = interceptorClass.getMethod(interceptor.getAfterMethodName(), Action.class);

            pre.invoke(interceptorInstance, action);
            stack.push(new ProxyStackElement(interceptorInstance, after));
        }

        //执行action的方法
        //获得类名和方法
        String className = action.getClassName();
        String methodName = action.getMethodName();

        //利用反射创建类对象
        Class<?> actionClass = Class.forName(className);
        Method actionMethod = actionClass.getMethod(methodName,
                        HttpServletRequest.class, HttpServletResponse.class);
        //获得结果
        String result = (String)actionMethod.invoke(actionClass.newInstance(), args);
        action.setResult(result);
        System.out.println("result: " + result);

        //执行after方法
        while(!stack.isEmpty()){
            ProxyStackElement element = stack.pop();
            element.getAfterMethod().invoke(element.getInterceptorInstance(), action);
        }

        return result;
    }
}
