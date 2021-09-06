package bytebuddy;

//当存在多个拦截方法时，将采用就近原则（依据参数类型匹配程度）
public class GreetingInterceptor {
	// 这里方法名任意即可
	public Object greet(Object argument) {
		return "Hello from " + argument;
	}
	
	public static String greetX(String argument) {
		return "in greetX interceptor";
	}
}
