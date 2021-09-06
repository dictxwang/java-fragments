package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * bytebuddy的相关应用
 * @author wangqiang
 * https://github.com/raphw/byte-buddy
 * https://bytebuddy.net/
 */

public class ByteBuddyBasic {

	public static void main(String[] args) throws Exception {
		/**
		 * 创建类
		 */
		// 此时这个动态类并未加载到jvm中
		DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
				.subclass(Object.class)
				.name("exampl.Type")
				.make();
		// 指定ClassLoadingStrategy
		Class<?> type = new ByteBuddy()
				.subclass(Object.class)
				.make()
				.load(ByteBuddyBasic.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
		
		// 创建动态类，加载类，并且执行toString方法
		Class<?> clazzHello = new ByteBuddy()
				.subclass(Object.class)
				.method(ElementMatchers.named("toString"))
				.intercept(FixedValue.value("Hello World"))
				.make()
				.load(ByteBuddyBasic.class.getClassLoader())
				.getLoaded();
		System.out.println(clazzHello.newInstance().toString());
		
		// 使用MethodDelegation进行方法委托
		Class<? extends java.util.function.Function> functionType = new ByteBuddy()
				// 定义为Function的子类
				.subclass(java.util.function.Function.class)
				.method(ElementMatchers.named("apply"))
				// 拦截Function.apply的调用，委托给GreetingInterceptor处理
				// 这里委托类需要public访问域，否则会报错：
				// bytebuddy.GreetingInterceptor is not visible
				.intercept(MethodDelegation.to(new GreetingInterceptor()))
				.make()
				.load(ByteBuddyBasic.class.getClassLoader())
				.getLoaded();
		// Hello from wangqiang
		System.out.println(functionType.newInstance().apply("wangqiang"));
		
		// 使用带注解的委托类
		Class<? extends java.util.function.Function> functionType2 = new ByteBuddy()
				.subclass(java.util.function.Function.class)
				.method(ElementMatchers.named("apply"))
				.intercept(MethodDelegation.to(new GeneralInterceptor()))
				.make()
				.load(ByteBuddyBasic.class.getClassLoader())
				.getLoaded();
		System.out.println(functionType2.newInstance().apply("wangqiang"));
		
		// 通过委托分析方法执行耗时
		ByteBuddyService service1 = new ByteBuddy()
				.subclass(ByteBuddyService.class)
				// 根据方法名和参数数量拦截
				.method(ElementMatchers.nameStartsWith("handle").and(ElementMatchers.takesArguments(0)))
				// TimingInterceptor的方法是静态方法，这里不用new的方式
				.intercept(MethodDelegation.to(TimingInterceptor.class))
				.make()
				.load(ByteBuddyBasic.class.getClassLoader())
				.getLoaded()
				.newInstance();
		service1.handleX();
		service1.handleY();
		service1.handleX("liudehua");
		service1.handleS();
		
		// 使用拦截类的实例方法完成拦截
		ByteBuddyService service2 = new ByteBuddy()
				.subclass(ByteBuddyService.class)
				.method(ElementMatchers.nameStartsWith("handle").and(ElementMatchers.takesArguments(1)))
				.intercept(MethodDelegation.to(new TimingInterceptor()))
				.make()
				.load(ByteBuddyBasic.class.getClassLoader())
				.getLoaded()
				.newInstance();
		service2.handleX("zhangxueyou");
	}
}
