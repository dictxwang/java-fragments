package reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

/**
 * java.lang.reflect的相关应用
 * 
 * @author wangqiang
 *
 */
public class ReflectBasic {

	public static void main(String[] args) throws Exception {
		/**
		 * 批量获取成员属性（包括static属性）
		 * getDeclaredFields() 获取自身的属性，包括public、private和protected的属性
		 * getFields() 获取自身及父类的public属性，一直追溯到Object超类
		 */
		Field[] dfields = ReflectChildBean.class.getDeclaredFields();
		System.out.println(dfields.length);
		
		/**
		 * 构造方法的反射
		 * 分为带参数构造方法和无参数构造方法
		 * 注意包装类的class和其对应的基本类型的class是不等价的
		 * 比如Integer.class <> int.class
		 */
		Constructor<ReflectChildBean> constructor = ReflectChildBean.class
					.getDeclaredConstructor(String.class, int.class, String.class);
		ReflectChildBean liudehua = constructor.newInstance("liudehua", 100, "HongKong");
		
		ReflectParentBean tianwang = ReflectParentBean.class.newInstance();
		tianwang.setName("King in the sky");
		tianwang.say();
		
		/**
		 * 获取指定的成员属性
		 * getDeclaredField(feildName)
		 * getField(fieldName)
		 */
		Field address = ReflectChildBean.class.getDeclaredField("address");
		address.setAccessible(true);
		System.out.println(address.get(liudehua));
		
		/**
		 * 获取父类信息
		 * getSuperclass() 获取普通继承的父类信息
		 * getGenericSuperclass() 获取泛型继承的父类信息
		 */
		System.out.println(ReflectChildBean.class.getSuperclass().getName());
		
		/**
		 * 获取成员方法（包括static方法）
		 * getMethods() 获取自身及其父类的public方法，一直追溯到Object超类
		 * getDeclaredMethods() 获取自身所有方法，包括public、protected和private方法
		 * */
		Method[] methods = ReflectParentBean.class.getDeclaredMethods();
		Stream.of(methods).forEach(System.out::println);
		
		/**
		 * 获取一个静态方法，并执行此方法
		 */
		Method methodGetCommon = ReflectParentBean.class.getMethod("getCommon");
		String commonContent = (String)methodGetCommon.invoke(ReflectParentBean.class);
		System.out.println(commonContent);
		
		/**
		 * 获取一个成员方法（private方法）
		 * 先改变方法的可见性，并执行此方法
		 */
		Constructor<ReflectParentBean> parentConstructor = ReflectParentBean.class.getDeclaredConstructor(String.class, int.class);
		ReflectParentBean parentBean = parentConstructor.newInstance("guofucheng", 99);
		Method methodSend = ReflectParentBean.class.getDeclaredMethod("send", String.class, List.class, String[].class);
		// 设置private方法为可访问
		methodSend.setAccessible(true);
		String sendContent = (String)methodSend.invoke(parentBean, null, null, null);
		System.out.println(sendContent);
	}
}
