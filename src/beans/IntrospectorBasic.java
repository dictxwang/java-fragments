package beans;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// Introspector与java反射的区别在于，introspector的操作对象是JavaBean
public class IntrospectorBasic {
	
	public static void main(String[] args) {
		
		Item item = new Item();
		item.setId(101);
		item.setContent("introspector");
		
		Pojo pojo = new Pojo();
		pojo.setSize(10);
		pojo.setName("p1");
		pojo.setItem(item);
		
		try {
			/** 获取指定的单个值 */
			String propertyName = "id";
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, Item.class);
			Method readMethod = descriptor.getReadMethod();
			// 这里设置方法可见是无效的，因为如果非public方法，在创建PropertyDescriptor就会出错
			readMethod.setAccessible(true);
		
			Class<?> typeClass = descriptor.getPropertyType();
			// 执行读方法，获取属性的值
			Object object = readMethod.invoke(item);
			// 判断是否是基本数据类型（原始类型）
			if (typeClass.isPrimitive()) {
				System.out.println(object); // 101
			}
			
			Method writeMethod = descriptor.getWriteMethod();
			writeMethod.invoke(item, 10001);
			System.out.println(item.getId()); // 1001
			
			/** 调用read方法遍历所有的属性值*/
			BeanInfo beanInfo = Introspector.getBeanInfo(pojo.getClass());
			PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
			// Object有getClass方法，所以下面一行将输出 class,item,name,size
			System.out.println(Stream.of(properties).map(x -> x.getName()).reduce((x, y) -> x + "," + y).get());
			
			Map<String, Object> beanMap = new HashMap<String, Object>();
			for (PropertyDescriptor property : properties) {
				String name = property.getName();
				if (!"class".equals(name)) {
					Method reader = property.getReadMethod();
					Object value = reader.invoke(pojo);
					beanMap.put(name, value);
				}
			}
			// 输出beanMap的内容
			beanMap.entrySet().stream().forEach(e -> System.out.printf("%s->%s\n", e.getKey(), e.getValue().toString()));
			
			/** 调用write方法填充所有的属性值*/
			Map<String, Object> beanInfoMap = new HashMap<String, Object>();
			beanInfoMap.put("size", 10);
			beanInfoMap.put("name", "liudehua");
			beanInfoMap.put("item", item);
			Pojo po = new Pojo();
			BeanInfo beanInfos = Introspector.getBeanInfo(po.getClass());
			PropertyDescriptor[] pds = beanInfos.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (!pd.getName().equals("class")) {
					String name = pd.getName();
					if (beanInfoMap.containsKey(name)) {
						Method method = pd.getWriteMethod();
						method.invoke(po, beanInfoMap.get(name));
					}
				}
			}
			System.out.println(po);
			
			/** BeanDescriptor*/
			BeanDescriptor bd = beanInfos.getBeanDescriptor();
			System.out.println(bd);
			
			/** MethodDescriptor[]*/
			MethodDescriptor[] mds = beanInfos.getMethodDescriptors();
			Stream.of(mds).forEachOrdered(m -> System.out.println(m));
			
			// 这里设置了stopClass，将不获取从stopClass开始的父类及超类的属性
			BeanInfo beanInfoWithStop = Introspector.getBeanInfo(Child.class, Pojo.class);
			System.out.println("======= test stop class");
			MethodDescriptor[] mdsStop = beanInfoWithStop.getMethodDescriptors();
			Stream.of(mdsStop).forEach(m -> System.out.println(m));
			
			PropertyDescriptor[] pdsStop = beanInfoWithStop.getPropertyDescriptors();
			Stream.of(pdsStop).forEach(p -> System.out.println(p));
			
			System.out.println("======= test stop class for grandson");
			// 测试孙子类的stopClass，将会获取自身属性和Child的属性
			BeanInfo beanInfoWithGrandson = Introspector.getBeanInfo(Grandson.class, Pojo.class);
			PropertyDescriptor[] pdsGrandson = beanInfoWithGrandson.getPropertyDescriptors();
			Stream.of(pdsGrandson).forEach(p -> System.out.println(p));
			
			// PropertyChangeSupport
			System.out.println("======= test property change support");
			ItemChange itemChange = new ItemChange();
			itemChange.addPropertyChangeListener(event -> {
				System.out.printf("all property changed: %s -> %s\n", String.valueOf(event.getOldValue()), String.valueOf(event.getNewValue()));
			});
			itemChange.addPropertyChangeListener("content", event -> {
				System.out.printf("one property changed: %s -> %s\n", String.valueOf(event.getOldValue()), String.valueOf(event.getNewValue()));
			});
			// PropertyChangeListenerProxy
			PropertyChangeListenerProxy listenerProxy = new PropertyChangeListenerProxy("content", event -> {
				System.out.printf("listenerProxy, %s changed: %s -> %s\n", event.getPropertyName(), event.getOldValue(), event.getNewValue());
			});
			itemChange.addPropertyChangeListener(listenerProxy);
			
			// VetoableChangeListener
			itemChange.addVetoableChangeListener(event -> {
				if ("linxi".equals(event.getNewValue())) {
					// 非法属性，抛出异常阻止改变
					throw new PropertyVetoException("Exception: invalid value of " + event.getNewValue(), event);
				}
				System.out.printf("all vetoable changed: %s -> %s\n", String.valueOf(event.getOldValue()), String.valueOf(event.getNewValue()));
			});
			
			// 如果没有对setAddress调用firePropertyChange方法，那么不会产生event从而不listener捕获
			itemChange.setAddress("Hong Kong");
			itemChange.setContent("concert");
			itemChange.setContent("anthor concert");
			try {
				itemChange.setText("zhangxueyou");
				// 设置了非法值，会抛出异常
				itemChange.setText("linxi");
			} catch (PropertyVetoException exp) {
				System.out.println(exp.getMessage());
			}
			try {
				// listener移除后，将不再触发其方法
				itemChange.setText("liudehua");
			} catch (PropertyVetoException exp) {
				System.out.println(exp.getMessage());
			}
			
			// BeanContextSupport
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
}
class Grandson extends Child {
	private int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
class Child extends Pojo {
	private String ext;

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
}

class Pojo {
	private int size;
	private String name;
	private Item item;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}

class Item {
	private int id;
	private String content;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}

class ItemChange {
	public String id = "liudehua";
	private String text;
	private String address;
	private String content;
	private PropertyChangeSupport pcs;
	private VetoableChangeSupport vcs;
	private PropertyChangeListener listener;
	// 分属性记录的listner，list支持对同一个属性多次添加listner
	private Map<String, List<PropertyChangeListener>> listenerMap;

	public ItemChange() {
		// 使ItemChange类支持属性改变通知
		this.pcs = new PropertyChangeSupport(this);
		// 使ItemChange类支出判断数据是否可以改变
		this.vcs = new VetoableChangeSupport(this);
		this.listenerMap = new HashMap<String, List<PropertyChangeListener>>(2);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
		this.listener = listener;
	}
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(propertyName, listener);
		if (!this.listenerMap.containsKey(propertyName)) {
			this.listenerMap.putIfAbsent(propertyName, new ArrayList<PropertyChangeListener>(2));
		}
		this.listenerMap.get(propertyName).add(listener);
	}
	public void addVetoableChangeListener(VetoableChangeListener listener) {
		this.vcs.addVetoableChangeListener(listener);
	}
	// 直接添加proxy，省去了参数propertyName
	public void addPropertyChangeListener(PropertyChangeListenerProxy listenerProxy) {
		addPropertyChangeListener(listenerProxy.getPropertyName(), listenerProxy);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) throws PropertyVetoException {
		String oldText = this.text;
		// 这里可能会抛出异常PropertyVetoException，从而阻止属性改变的发生
		vcs.fireVetoableChange("text", oldText, text);
		this.text = text;
		if (text == null || !text.equals(oldText)) {
			// 需要调用firePropertyChange方法，才会创建event对象，从而被listener捕获
			pcs.firePropertyChange("text", oldText, text);
			// 如果移除了listener，则再次改变text属性，将不会再执行listener的propertyChange方法
			pcs.removePropertyChangeListener(this.listener);
		}
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		String oldContent = this.content;
		this.content = content;
		if (content == null || !content.equals(oldContent)) {
			pcs.firePropertyChange("content", oldContent, content);
			if (this.listenerMap.containsKey("content")) {
				// 如果移除了改属性的监听器，
				// 该属性再次发生变化时产生的event将不会被自身的listener监听到，
				// 但是不影响绑定到所有属性的listener的工作
				for (PropertyChangeListener l : this.listenerMap.get("content")) {
					pcs.removePropertyChangeListener("content", l);
				}
				this.listenerMap.remove("content");
			}
		}
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
