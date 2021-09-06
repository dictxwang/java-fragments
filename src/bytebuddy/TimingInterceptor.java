package bytebuddy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class TimingInterceptor {

	// @SuperCall是指调用父级方法，除此还有@DefaultCall
	// @RuntimeType 必须要添加此注解，才能实现拦截
	@RuntimeType
	public static Object interceptorX(@Origin Method method, @SuperCall Callable<?> callable) {
		long st = System.currentTimeMillis();
		Object result = null;
		try {
			result = callable.call();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			System.out.println("\n");
			System.out.println("[Start] TimingInterceptor.interceptorX");
			System.out.println("MethodName:" + method.getName());
			System.out.println("ParameterNumber:" + method.getParameterCount());
			System.out.println("RetrunValueType:" + method.getReturnType());
			System.out.println("ReturnValue:" + result);
			System.out.println("[Finish] took " + (System.currentTimeMillis() - st));
		}
		return result;
	}
	
	@RuntimeType
	public Object interceptorY(@Origin Method method, @AllArguments Object[] args,
			@Argument(0) Object arg0, @SuperCall Callable<?> callable) {
		long st = System.currentTimeMillis();
		Object result = null;
		try {
			result = callable.call();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			System.out.println("\n");
			System.out.println("[Start] TimingInterceptor.interceptorY");
			System.out.println("MethodName:" + method.getName());
			System.out.println("ParameterNumber:" + method.getParameterCount());
			System.out.println("First parameter:" + arg0);
			System.out.println("ReturnValueType:" + method.getReturnType());
			System.out.println("ReturnValue:" + result);
			System.out.println("[Finish] took " + (System.currentTimeMillis() - st));
		}
		return result;
	}
}
