package dubbo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: QingY
 * @Date: Created in 16:34 2024-04-07
 * @Description:
 */
public class UserRpcProxyDemo implements UserRpcService {

    private InvocationHandler handler;
    private Method[] methods = new Method[2];

    public UserRpcProxyDemo(InvocationHandler invocationHandler) throws NoSuchMethodException {
        this.handler = invocationHandler;
        Method getUserInfoMethod = UserRpcService.class.getDeclaredMethod("getUserInfo", String.class);
        methods[0] = getUserInfoMethod;
    }

    @Override
    public String getUserInfo(String name) {
        try {
            return (String) this.handler.invoke(this, methods[0], new String[]{name});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoSuchMethodException {
        UserRpcProxyDemo demoService = new UserRpcProxyDemo(new InvokerInvocationHandler());
        String result = demoService.getUserInfo("qingy");
        System.out.println(result);
    }

}
