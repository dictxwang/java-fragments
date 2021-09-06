package learn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TypeMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> l1 = new ArrayList<String>();
		l1.add("a");
		
		print(l1);
		
		Pair p = Pair.makePair(1, 2);
		System.out.printf("first is %d, second is %d\n", p.getFirst(), p.getSecond());
		
		Pair p2 = Pair.makePair("hello", "world");
		System.out.printf("first is %s, second is %s\n", p2.getFirst(), p2.getSecond());
		
		List<Fruit> l2 = new ArrayList<Fruit>();
		l2.add(new Apple("红富士", "红色"));
		l2.add(new Pear("雪梨", "黄色"));
		xprint(l2);


		Class clazz = ExPear.class;
		Method[] dmethods = clazz.getDeclaredMethods();
		for (Method m : dmethods) {
			System.out.println("declared method: " + m.getName());
		}
		
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			System.out.println("method: " + m.getName());
		}
		
		Field[] dfields = clazz.getDeclaredFields();
		for (Field f : dfields) {
			System.out.println("decleared field: " + f.getName());
		}
		
		Field[] fields = clazz.getFields();
		for (Field f : fields) {
			System.out.println("field: " + f.getName());
		}
		
		P pi = new P();
		pi.print();
		
		try {
			Method m = clazz.getDeclaredMethod("exPrint");
			Field f = clazz.getDeclaredField("sexname");
			Field fs = clazz.getDeclaredField("exname");
			try {
				System.out.println(m.getName() + " can be accessed:" + m.isAccessible());
				m.setAccessible(true);
				System.out.println(m.invoke(new ExPear()));
				System.out.println(f.get(clazz));
				fs.setAccessible(true);
				System.out.println(fs.get(new ExPear()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static <T extends Fruit> void xprint(List<T> list) {
		if (!list.isEmpty()) {
			for (T t : list) {
				System.out.printf("%s\t%s\n", t.getName(), t.getColor());
			}
		}
	}
	
	public static <T> void print(List<T> list) {
		
		if (!list.isEmpty()) {
			for (T t : list) {
				System.out.println(t);
			}
		}
	}
}
