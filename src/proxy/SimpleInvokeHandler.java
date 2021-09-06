package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SimpleInvokeHandler implements InvocationHandler {
	
	EasyModel target;
	
	public EasyModel newProxy(EasyModel target) {
		this.target = target;
		return (EasyModel)Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), this);
	}
	
	@Override
	public Object invoke(Object paramObject, Method paramMethod,
			Object[] paramArrayOfObject) throws Throwable {
		Object result = null;
		try {
			if (paramMethod.getName().equals("printX")) {
				printLog();
			}
			result = paramMethod.invoke(this.target, paramArrayOfObject);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return result;
	}
	
	private void printLog() {
		System.out.println("ioc method is called");
	}
}