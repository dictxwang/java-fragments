package jssist;

import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

/**
 * javassist字节码增强工具的示例
 * @author wangqiang
 * 
 * https://www.javassist.org/tutorial/tutorial.html
 */
public class JavassistBasic {

	public static void main(String[] args) throws Exception {
//		createClass();
//		getCreatedClass();
//		useWithInterface();
//		updateExistsClass();
		loadWithCustomLoader();
	}
	
	// 读取先前创建的.class文件
	private static void getCreatedClass() throws Exception {
		System.out.println("***** Method: getCreatedClass *****");
		ClassPool cpool = ClassPool.getDefault();
		cpool.appendClassPath("/Users/wangqiang/EclipseSpace/lab/bin");
		CtClass clazz = cpool.get("jssist.DynamicPerson");
		// 使用反射的方式调用类的方法
		Object dperson = clazz.toClass().newInstance();
		Method printMethod = dperson.getClass().getMethod("printName");
		printMethod.invoke(dperson);
	}

	// 新建一个类
	private static void createClass() throws Exception {
		System.out.println("***** Method: createClass *****");
		ClassPool cpool = ClassPool.getDefault();
		// 创建一个空类型
		CtClass dpc = cpool.makeClass("jssist.DynamicPerson");
		// 增加一个属性字段
		CtField pname = new CtField(cpool.get("java.lang.String"), "name", dpc);
		// 设置属性的访问级别
		pname.setModifiers(Modifier.PRIVATE);
		// 设置初始值
		dpc.addField(pname, CtField.Initializer.constant("wangqiang"));
		// 生成getter和setter方法
		dpc.addMethod(CtNewMethod.setter("setName", pname));
		dpc.addMethod(CtNewMethod.getter("getName", pname));
		// 添加无参的构造方法
		CtConstructor cons = new CtConstructor(new CtClass[] {}, dpc);
		cons.setBody("{name=\"wangqiang\";}");
		dpc.addConstructor(cons);
		
		// 添加带参数的构造方法
		cons = new CtConstructor(new CtClass[] {cpool.get("java.lang.String")}, dpc);
		// $0=this / $1,$2,$3... 代表方法参数
		// 更多用法参见 javassist的内省(Introspection)
		cons.setBody("{$0.name = $1;}");
		dpc.addConstructor(cons);
		
		// 创建一个无参数无返回值的方法
		CtMethod printMethod = new CtMethod(CtClass.voidType, "printName", new CtClass[] {}, dpc);
		printMethod.setModifiers(Modifier.PUBLIC);
		printMethod.setBody("{System.out.println(name);}");
		dpc.addMethod(printMethod);
		
		// 将创建的类编译成.class文件
		dpc.writeFile("/Users/wangqiang/EclipseSpace/lab/bin");
		
		// 使用刚创建的类
		Object dperson = dpc.toClass().newInstance();
		// 通过反射调用方法
		Method pmethod = dperson.getClass().getMethod("printName");
		pmethod.invoke(dperson);
		
		Method smethod = dperson.getClass().getMethod("setName", String.class);
		smethod.invoke(dperson, "liudehua");
		pmethod.invoke(dperson);
		dpc.defrost();
	}
	
	// 通过接口方式
	public static void useWithInterface() throws Exception {
		System.out.println("***** Method: useWithInterface *****");
		ClassPool cpool = ClassPool.getDefault();
		
		cpool.appendClassPath("/Users/wangqiang/EclipseSpace/lab/bin");
		CtClass cperson = cpool.get("jssist.DynamicPerson");
		// 内部类的get方式
		// CtClass iperson = cpool.get("jssist.JavassistBasic$PersonInterface");
		CtClass iperson = cpool.get("jssist.PersonInterface");
		cperson.addInterface(iperson);
		
		PersonInterface person = (PersonInterface)cperson.toClass().newInstance();
		person.setName("guofucheng");
		System.out.println(person.getName());
	}
	
	// 修改已经存在类
	public static void updateExistsClass() throws Exception {
		System.out.println("***** Method: updateExistsClass *****");
		ClassPool cpool = ClassPool.getDefault();
		CtClass oneService = cpool.get("jssist.OneService");
		
		// 修改原有的方法
		CtMethod echoMethod = oneService.getDeclaredMethod("echo");
		echoMethod.insertBefore("System.out.println(\":=> before method echo\");");
		echoMethod.insertAfter("System.out.println(\":=> after method echo\");");
		
		// 新增方法
		CtMethod nMethod = new CtMethod(CtClass.voidType, "hello", new CtClass[] {}, oneService);
		nMethod.setModifiers(Modifier.PUBLIC);
		nMethod.setBody("{System.out.println(\"Say hello.\");}");
		oneService.addMethod(nMethod);

		// 通过反射方式调用方法
		Object service = oneService.toClass().newInstance();
		Method m1 = service.getClass().getDeclaredMethod("echo");
		m1.invoke(service);
		
		Method m2 = service.getClass().getDeclaredMethod("hello");
		m2.invoke(service);
	}
	
	// 通过自定义的ClassLoader加载类
	public static void loadWithCustomLoader() throws Exception {
		System.out.println("***** Method: loadWithCustomLoader *****");
		ClassPool pool = ClassPool.getDefault();
		Translator t = new CustomTranslator();
		Loader loader = new Loader();
		loader.addTranslator(pool, t);
		
		CtClass clazz = pool.get("jssist.OneService");
		/**
		 *  这里需要将类的访问权限设置为public，否则会出异常
		 *  也可以放到translate的onLoad方法中进行，效果是一样的
		 */
		clazz.setModifiers(Modifier.PUBLIC);
		Class<?> service = loader.loadClass("jssist.OneService");
		/**
		 *  出现下面的错误，说明缺少public的构造方法
		 *  can not access a member of class jssist.OneService with modifiers ""
		 */
		Object oneService = service.newInstance();
		Method methodEcho = oneService.getClass().getDeclaredMethod("echo");
		methodEcho.invoke(oneService);
	}
}

class CustomTranslator implements Translator {
	
	@Override
	public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
		System.out.println("::=> translator start method");
	}

	@Override
	public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
		System.out.println("::=> translator onLoad method");
		// 这里可以做一下访问权限的控制之类的
		// CtClass cc = pool.get(className);
		// cc.setModifiers(Modifier.PUBLIC);
	}
}

class OneService {
	
	// 如果不创建public的构造方法，在执行newInstance()方法是会出错
	public OneService() {}
	
	public void echo() {
		System.out.println("execute method 'echo'.");
	}
}

interface PersonInterface {
	void setName(String name);
    String getName();
    void printName();
}
