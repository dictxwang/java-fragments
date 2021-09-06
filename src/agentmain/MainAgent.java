package agentmain;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

public class MainAgent {

	public static void agentmain(String agentArgs, Instrumentation inst)
			throws UnmodifiableClassException, ClassNotFoundException {
		// 添加transformer，并且允许重新加载类
		inst.addTransformer(new CustomTransformer(), true);
		inst.retransformClasses(Class.forName("agentmainsample.SampleOne"));
	}
}

class CustomTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		System.out.println(String.format("Agentmain process by ClassFileTransformer, target class=%s", className));
		byte[] byteCode = null;
		className = className.replace("/", ".");
		
		CtClass ctClass = null;
		try {
			ClassPool cpool = ClassPool.getDefault();
			cpool.appendClassPath(new LoaderClassPath(loader));
			ctClass = cpool.get(className);
			CtMethod[] ctMethods = ctClass.getMethods();
			for (CtMethod ctMethod : ctMethods) {
				// 对特定方法进行修改
				if (ctMethod.getName().startsWith("handle")) {
					String methodName = ctMethod.getName();
					/** 对于已经加载的类，jvm不允许新增方法，否则会报错：
					/* class redefinition failed: attempted to add a method
					 */
					// 这里需要通过 addLocalVariable 定义局部变量
					ctMethod.addLocalVariable("st", CtClass.longType);
					ctMethod.addLocalVariable("et", CtClass.longType);
					ctMethod.insertBefore("st = System.currentTimeMillis();");
					ctMethod.insertAfter("et = System.currentTimeMillis();");
					ctMethod.insertAfter("System.out.println(\"method " + methodName + " cost \" + (et - st) + \"ms.\");");
				}
			}
			byteCode = ctClass.toBytecode();
			ctClass.detach();
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		return byteCode;
	}
	
}