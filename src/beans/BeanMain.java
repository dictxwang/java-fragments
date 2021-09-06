package beans;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanMain {

	/**
	 * @param args
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		
		try {
			Map<String, String> definitionMap = new HashMap<String, String>() {{
				put("name", "wangqiang");
				put("content", "是好人");
			}};
			PBean pbean = new PBean();
			PropertyDescriptor[] pds = Introspector.getBeanInfo(PBean.class).getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
//				System.out.println(pd.getName());
//				System.out.println(pd.getWriteMethod());
				
				if (definitionMap.containsKey(pd.getName())) {
					if (pd.getWriteMethod() != null) {
						Method wm = pd.getWriteMethod();
						wm.invoke(pbean, definitionMap.get(pd.getName()));
					}
				}
			}
			
			System.out.println(pbean.toString());
			
			PBean songer = null;
			Class clazz = Class.forName("beans.PBean");
			Constructor<PBean>[] cls = clazz.getConstructors();
			for (Constructor<PBean> cl : cls) {
				if (cl.getGenericParameterTypes().length == 2) {
					songer = cl.newInstance("刘德华", "四大天王之一");
					break;
				}
			}
			System.out.println(songer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
