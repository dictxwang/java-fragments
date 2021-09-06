package bytebuddy;

import java.lang.reflect.Method;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

//带注解的拦截器
public class GeneralInterceptor {

	@RuntimeType
	public Object interceptor(@AllArguments Object[] allArguments, @Origin Method method) {
		System.out.println(allArguments.length);
		System.out.println(method.getName());
		return "in GeneralInterceptor";
	}
}
