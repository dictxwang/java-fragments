package cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * cglib相关的使用，cglib是依赖asm实现的。
 * cglib全称Code Generation Library
 * 实例版本对应关系 cglib-3.3.0 <=> asm-7.3.1
 * */
public class CglibBasic {

	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(SomeService.class);
		enhancer.setCallback(new LogInterceptor());
		
		SomeService service = (SomeService) enhancer.create();
		service.echo("Hello World");
		service.hello();
		service.helloFinal();
		try {
			service.division(10, 2);
			service.division(1, 0);
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}
	}

	private static class LogInterceptor implements MethodInterceptor {

		@Override
		public Object intercept(Object obj, Method method, Object[] param,
				MethodProxy proxy) throws Throwable {
			String methodName = method.getName();
			System.out.printf("\nbefore %s invoke\n", methodName);
			Object result = null;
			try {
				result = proxy.invokeSuper(obj, param);
			} catch (Exception exp) {
				System.out.println("some exception");
				throw exp;
			} finally {
				System.out.printf("after %s invoke\n", methodName);
			}
			return result;
		}
	}
	
	private static class SomeService {
		
		// 这里需要一个显式的构造方法，否则在enhancer创建对象是会报异常
		public SomeService() {}
		
		// 无参的方法同样可以代理
		public void hello() {
			System.out.println("Hello World");
		}
		
		// final方法不能被代理
		public final void helloFinal() {
			System.out.println("\nHello World Final");
		}
		
		public void echo(String input) {
			System.out.printf("Method echo execute: Your input is %s\n", input);
		}
		
		/**
		 * 相除
		 * @param molecular 分子
		 * @param denominator 分母
		 * @return
		 */
		public int division(int molecular, int denominator) throws Exception {
			try {
				return molecular / denominator;
			} catch (Exception exp) {
				throw exp;
			}
		}
	}
}
