package cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class LogInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object obj, Method method, Object[] param,
			MethodProxy proxy) throws Throwable {
		System.out.println("before invoke");
		Object result = null;
		try {
			result = proxy.invokeSuper(obj, param);
		} catch (Exception exp) {
			System.out.println("some exception");
			throw exp;
		} finally {
			System.out.println("after invoke");
		}
		return result;
	}
}
