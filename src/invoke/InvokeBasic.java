package invoke;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * java.lang.invoke包的应用
 * 主要入口是MethodHandles类，和reflect的部分功能类似，可以实现对方法和属性的反射
 * 但api不如reflect丰富
 * 
 * @author wangqiang
 *
 */
public class InvokeBasic {

	public static void main(String[] args) throws Throwable {
		
		InvokeBean bean = new InvokeBean();

		// findVirtual 获取成员方法
		MethodHandles.lookup()
			.findVirtual(bean.getClass(), "say", MethodType.methodType(void.class, String.class))
			.bindTo(bean)
			.invoke("You are right.");
		
		// findStatic 获取静态方法
		MethodHandles.lookup().findStatic(InvokeBean.class, "hello", MethodType.methodType(void.class))
			.invoke();

		/**
		 * findSetter和findGetter实际上都是直接对field进行操作，
		 * 和具体的get、set方法无关
		 * 另外需要被操作的属性是非private的
		 * */
		
		// findSetter 获取属性的set方法
		MethodHandles.lookup()
			.findSetter(bean.getClass(), "name", String.class)
			.bindTo(bean)
			.invoke("liudehua");
		
		// findGetter 获取属性的get方法
		String name = (String)MethodHandles.lookup()
					.findGetter(bean.getClass(), "name", String.class)
					.bindTo(bean)
					.invoke();
		System.out.println("getName: " + name);
		
		// 对String类的反射操作
		String result = (String)MethodHandles.lookup()
			.findVirtual(String.class, "replace",
					MethodType.methodType(String.class, char.class, char.class))
			.invoke("ABC", 'B', '1');
		System.out.println(result);
	}
}
