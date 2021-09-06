package hot;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 热部署实现的技术基础-ClassLoader
 * @author wangqiang
 */
public class HotMain {

	public static void main(String[] args) throws Exception {
		while (true) {
			// 同一个classloader不能重复加载同一个类，因此每次需要创建一个新的classloader
			ClassLoader loader = new ClassLoader() {
				@Override
				public Class<?> loadClass(String name) throws ClassNotFoundException {
					try {
						String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
						InputStream is = getClass().getResourceAsStream(fileName);
						if (is == null) {
							return super.loadClass(name);
						}
						byte[] b = new byte[is.available()];
						is.read(b);
						// 最终通过defineClass方法获取类
						return defineClass(name, b, 0, b.length);
					} catch (IOException exp) {
						exp.printStackTrace();
						throw new ClassNotFoundException(name);
					}
				}
			};
			
			Class clazz = loader.loadClass("hot.HandlerService");
			Object service = clazz.newInstance();
			service.getClass().getMethod("handle", new Class[] {}).invoke(service);
			TimeUnit.SECONDS.sleep(20);
		}
	}
}
