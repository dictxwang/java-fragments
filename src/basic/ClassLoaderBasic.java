package basic;

/**
 * 关于classLoader相关的知识
 * @author wangqiang
 * 
 * Bootstrap ClassLoader ：最顶层加载器，主要加载核心类库（%JRE_HOME%\lib下的rt.jar、resources.jar、charsets.jar和class等）。
 * Extention ClassLoader：扩展类加载器（%JRE_HOME%\lib\ext目录下的jar包和class文件）
 * Appclass Loader：也称为SystemAppClass ，加载当前应用的classpath的所有类
 * 执行顺序：从上到下依次执行。
 * sum.misc.Launcher 初始化了ExtClassLoader和AppClassLoader
 * 
 * jvm判断是否是同一个类的依据是：包名+类名是否相同，ClassLoader是否相同
 */
public class ClassLoaderBasic {

	public static void main(String[] args) throws Exception {

		// BootstrapClassLoader 加载的jar包路径，全部是jre路径下的jar包
		System.out.println(System.getProperty("sun.boot.class.path"));
		// ExtClassLoader 加载的jar包路径
		System.out.println(System.getProperty("java.ext.dirs"));
		// AppClassLoader 加载的class路径，包括当前工程编译路径和lib路径下的jar包
		System.out.println(System.getProperty("java.class.path"));
		
		String s = "Hello";
		ClassLoader loaderNative = s.getClass().getClassLoader();
		// 核心api的类由引导类加载器加载，所以输出null
		System.out.println(loaderNative);  // null
		
		// 当前线程上下文的ClassLoader
		ClassLoader loaderContext = Thread.currentThread().getContextClassLoader();
		System.out.println(loaderContext); // AppClassLoader
		// 父级类加载器（父加载器和父类是不一样的）
		System.out.println(loaderContext.getParent()); // ExtClassLoader

		// 获取系统的ClassLoader
		ClassLoader loaderSystem = ClassLoader.getSystemClassLoader().getParent();
		System.out.println(loaderSystem); // ExtClassLoader

		// 获取当前路径
		// /Users/wangqiang/EclipseSpace/lab/bin/
		System.out.println(loaderContext.getResource("").getPath());
		// /Users/wangqiang/EclipseSpace/lab/bin/basic/
		System.out.println(ClassLoaderBasic.class.getResource("").getPath());
		// /Users/wangqiang/EclipseSpace/lab/bin/
		System.out.println(ClassLoaderBasic.class.getResource("/").getPath());
	}
}
