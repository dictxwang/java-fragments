package generic;

import java.util.ArrayList;
import java.util.List;

// 有关泛型的应用
public class GenericBasic {

	public static void main(String[] args) {
		// 泛型最主要的作用是在编译阶段进行类型校验，编译后类型将会被擦除
		List<String> lstStr = new ArrayList<String>();
		List<Integer> lstInt = new ArrayList<Integer>();
		System.out.println(lstStr.getClass()); // class java.util.ArrayList
		System.out.println(lstStr.getClass().equals(lstInt.getClass())); // true
		
		// 对于明确指定了泛型类型的集合，既可以向集合添加元素，也可以从集合读取元素
		lstStr.add("wangqiang");
		String itemStr = lstStr.get(0);
		System.out.println(itemStr); // wangqiang
		
		// 可以添加Item类或Item子孙类的对象元素
		List<Item> itemLst = new ArrayList<>();
		itemLst.add(new Item("001"));
		itemLst.add(new Child("002", "wangqiang"));
		itemLst.add(new Grandson("003", "liudehua", "sing a song"));
		// 但是这种方式下，从list中读取的元素会自动被转换成Item类型
		System.out.println(itemLst.get(1)); // generic.GenericBasic$Child@xxxxxx
		System.out.println(itemLst.get(2).id); // 此时只有父类属性能被直接访问
		System.out.println(((Grandson)itemLst.get(2)).content); // 经过强制转换后才能访问子类属性
		
		List<Child> childLst = new ArrayList<>();
		childLst.add(new Child("xyz", "zhangxueyou"));
		// childLst.add(new Item("001")); // 此时不能添加父类对象元素
		
		/**
		 * put原则：通过super指定泛型的下届，此时集合中存放的是Item的父类型元素（包括Item）
		 * 只能向集合中添加Item的子类型元素（包括Item），这样在编译期间将元素转换为Item类型是安全的
		 * 但是因为集合包含的父类型众多，因此从集合中获取元素时，无法判断具体类型而只能得到Object超类
		 */
		List<? super Item> itemExtLst = new ArrayList<>();
		itemExtLst.add(new Item("101"));
		itemExtLst.add(new Child("102", "liming"));
		itemExtLst.add(new Grandson("103", "guofucheng", "He is a dancer"));
		// 此时读取出来的元素是Object类型，无法直接访问原有对象的属性了
		System.out.println(itemExtLst.get(0)); // generic.GenericBasic$Item@xxxxxxx
		System.out.println(itemExtLst.get(1)); // generic.GenericBasic$Child@xxxxxx
		System.out.println(itemExtLst.get(2)); // generic.GenericBasic$Grandson@xxxxxx

		/**
		 * get原则：通过extends指定泛型的上届，此时集合中存放的是Child的子类型元素（包括Child）
		 * 由于集合中的元素有共同的父类型Child，因此从集合中获取的元素会自动转换为Child类型
		 * 但是此时不能向集合中添加元素，因为集合中Child的多个子类会造成编译失败
		 */
		List<Grandson> grandsonLst = new ArrayList<>();
		grandsonLst.add(new Grandson("0010", "likeqin", "He is CD-Qin"));
		// 通过extends上届指定基类，可以在集合中放置基类的子集合
		List<? extends Child> childExtLst = new ArrayList<>(grandsonLst);
		// 此时从集合中取出的元素类型为Child类型
		System.out.printf("id=%s,name=%s\n", childExtLst.get(0).id, childExtLst.get(0).name);
		// 此时不能直接向childExtLst集合中添加元素
		// childExtLst.add(new Child("0011", "tanyonglin")); // Got Error
		// childExtLst.add(new Grandson("0012", "linzixiang", "NB-Singer")); // Got Error
		
		// 以下两种类型可作为方法参数
		printItemSuper(itemLst);
		printItemSuper(itemExtLst);
		// 以下类型不能作为方法参数
		// printItemSuper(grandsonLst); // Got Error
		// printItemSuper(childExtLst); // Got Error
		
		// 以下两种类型均可作为方法的参数
		printChildExtends(childExtLst);
		printChildExtends(grandsonLst);
		// 以下类型不能作为方法参数
		// printChildExtends(itemLst); // Got Error
		// printChildExtends(itemExtLst); // Got Error
		
		// Child集合与Child父类的集合均可作为方法的参数
		printChildSuper(childLst);
		 printChildSuper(itemLst);
		// Child子类的集合不能作为方法参数
		// printChildSuper(grandsonLst); // Got Error
		
		// 此时只能是List<Item>作为其参数
		printItem(itemLst);
		// printItem(itemExtLst); // Got Error
		// printItem(grandsonLst); // Got Error
		
		/**
		 * PECS原则：Producer Extends, Consumer Super
		 * 如果想从一个数据类型里获取数据，使用 ? extends 通配符
		 * 如果想把对象写入一个数据结构里，使用 ? super 通配符
		 * 如果既想存，又想取，那就别用通配符。
		 * */
		// 对于extends，集合中的元素是Number或者其子类
		// 此时如果向集合中添加元素，无法确定是Number、Integer、Double中的哪一种，因此添加元素被禁止
		// 但是集合中的元素有共同的父类Number，因此可以从集合中读取元素（集合在生产元素）
		List<? extends Number> extList1 = new ArrayList<Integer>();
		List<? extends Number> extList2 = new ArrayList<Double>();
		List<? extends Number> extList3 = new ArrayList<Number>();

		// 对于super，集合中的元素是Integer或者其父类
		// 此时可以向集合添加Integer或者其子类元素，因为它们也是Number和Object的子孙类
		// 理解为集合在消费元素
		// 但是集合中的元素只能确定具有相同的父类Object，因此从集合中只能读取Object元素
		List<? super Integer> supList1 = new ArrayList<Integer>();
		List<? super Integer> supList2 = new ArrayList<Number>();
		List<? super Integer> supList3 = new ArrayList<Object>();
	}
	
	public static void printItemSuper(List<? super Item> lst) {
		lst.stream().forEach(i -> {
			Item item = (Item)i; // 此时取出的元素类型是Object，因此需要强制转换
			System.out.printf("printItemSuper: id=%s\n", item.id);
		});
	}
	
	public static void printChildSuper(List<? super Child> lst) {
		lst.stream().forEach(i -> {
			if (i instanceof Item) {
				Item item = (Item)i;
				System.out.printf("printChildSuper item: id=%s\n", item.id);
			} else if (i instanceof Child) { // 这里的判断会无效，对象元素会自动转换成Item类型
				Child child = (Child)i;
				System.out.printf("printChildSuper child: id=%s,name=%s\n", child.id, child.name);
			}
		});
	}
	
	// 泛型不能作为方法重载的参数类型区分，因此此处需要重命名方法名
	public static void printChildExtends(List<? extends Child> lst) {
		lst.stream().forEach(i -> {
			System.out.printf("printChildExtends: id=%s,name=%s\n", i.id, i.name);
		});
	}
	
	public static void printItem(List<Item> lst) {
		lst.stream().forEach(i -> {
			System.out.printf("printItem: id=%s\n", i.id);
		});
	}
	
	// 泛型方法示例
	public static <T> void call(T t) {}
	public static <A, B> B biCall(A a) {
		return null;
	}
	public static <S, T> T convert(S f, T t) {
		return null;
	}
	
	// 泛型类
	private static class G1<T> {
		private T t;
		public void call(T t) {}
	}
	// 此时只能用 extends上届指定，不能使用super下届指定
	private static class G2<T extends Item> {}
	private static class G21 extends G2<Child> {}
	private static class G22 extends G2<Grandson> {}
	
	private static class Item {
		public String id;
		public Item(String id) {
			this.id = id;
		}
	}
	private static class Child extends Item {
		public String name;
		public Child(String id, String name) {
			super(id);
			this.name = name;
		}
	}
	private static class Grandson extends Child {
		public String content;
		public Grandson(String id, String name, String content) {
			super(id, name);
			this.content = content;
		}
	}
}
