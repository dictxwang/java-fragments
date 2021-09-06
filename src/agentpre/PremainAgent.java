package agentpre;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

public class PremainAgent {
	
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("**** premain 方法执行开始");
		inst.addTransformer(new CustomTransformer(agentArgs));
		System.out.println("**** premain 方法执行结束");
	}
	
	// 此方法将被上面的方法覆盖而不会执行
	public static void premain(String agentArgs) {
		System.out.println("**** premain 方法执行开始");
		System.out.println(agentArgs);
		System.out.println("**** premain 方法执行结束");
	}
}

/**
 * 定义自主的transformer，配合javassist进行类的改写
 * @author wangqiang
 */
class CustomTransformer implements ClassFileTransformer {

	// 保存被重写的方法
	private final static Map<String, List<String>> methodMap = new ConcurrentHashMap<>();
	private final static Pattern classPattern = Pattern.compile("^(\\w+\\.)+[\\w]+$");
	
	private CustomTransformer() {
		addMethod("agentpresample.SampleOne.handleFirst");
		addMethod("agentpresample.SampleOne.handleSecond");
	}
	
	public CustomTransformer(String methodString) {
		this();
		if (methodString == null || !classPattern.matcher(methodString).matches()) {
			return;
		}
		addMethod(methodString);
	}

	private void addMethod(String methodString) {
		String className = methodString.substring(0, methodString.lastIndexOf("."));
		String methodName = methodString.substring(methodString.lastIndexOf(".") + 1);
		List<String> methodList = methodMap.computeIfAbsent(className, k -> new ArrayList<>());
		methodList.add(methodName);
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = null;
		className = className.replace("/", ".");
		if (methodMap.containsKey(className)) {
			CtClass ctClass;
			try {
				ClassPool cpool = ClassPool.getDefault();
				// 将要修改的类的classpath添加到ClassPool
				cpool.appendClassPath(new LoaderClassPath(loader));
				ctClass = cpool.get(className);
				for (String methodName : methodMap.get(className)) {
					CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
					// 创建新方法，通过复制原来的方法实现
					CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctClass, null);
					// 定义一个方法名，用于区分修改之前的方法
					String oldMethodName = methodName + "$old";
					ctMethod.setName(oldMethodName);
					
					// 构建新方法
					StringBuilder methodBody = new StringBuilder();
					methodBody.append("{");
					methodBody.append("long st = System.currentTimeMillis();");
					// 调用原来的方法，其中($$)表示所有参数
					methodBody.append(oldMethodName).append("($$);");
					methodBody.append("long et = System.currentTimeMillis();");
					methodBody.append("System.out.println(\"method " + methodName + " cost: \" + (et - st) + \"ms.\");");
					methodBody.append("}");
					
					newMethod.setBody(methodBody.toString());
					ctClass.addMethod(newMethod);
				}
				byteCode = ctClass.toBytecode();
				// 从ClassPool中删除此类
				ctClass.detach();
			} catch (Exception exp) {
				System.out.println(exp.getMessage());
				exp.printStackTrace();
			}
		}
		return byteCode;
	}
	
}
