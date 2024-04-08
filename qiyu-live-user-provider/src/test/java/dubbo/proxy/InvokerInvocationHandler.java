package dubbo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: QingY
 * @Date: Created in 16:38 2024-04-07
 * @Description:
 */
public class InvokerInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("getUserInfo")) {
            return objects[0] + "-test";
        }
        return "no match";
    }
}
