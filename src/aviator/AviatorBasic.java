package aviator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

/**
 * 表达式计算组件aviator的应用
 * https://github.com/killme2008/aviatorscript
 * aviator是通过将表达式编译成字节码实现的
 * @author wangqiang
 */
public class AviatorBasic {

	public static void main(String[] args) {
		/**
		 * execute的使用
		 */
		// 数值型只有Long和Double两种
		Object i1 = AviatorEvaluator.execute("1+3+4");
		System.out.println(i1 instanceof Long); // true
		Object l1 = AviatorEvaluator.execute("1.2+3+4");
		System.out.println(l1 instanceof Double); // true
		// 转换成string输出
		System.out.println(AviatorEvaluator.execute("1.2+3+4").toString());
		// 带参数的表达式
		Map<String, Object> p1 = new HashMap<>();
		p1.put("name", "wangqiang");
		System.out.println(AviatorEvaluator.execute("'Your name is ' + name", p1));
		
		// 调用系统函数
		System.out.println(AviatorEvaluator.execute("string.length('hello')")); // 5
		System.out.println(AviatorEvaluator.execute("string.contains('test', string.substring('hello', 1, 2))")); // true
		// 自定义函数
		AviatorEvaluator.addFunction(new AddFunction());
		System.out.println(AviatorEvaluator.execute("add(1, 2)")); // 3.0
		
		/**
		 * exec的使用，和execute类似，但是省略了通过map传参
		 * 但是exec是一个废弃的方式
		 */
		System.out.println(AviatorEvaluator.exec("'You name is ' + name", "wangqiang"));
		
		/**
		 * compile的使用：现将表达式编译成Expression中间对象，再使用
		 */
		Map<String, Object> p2 = new HashMap<>();
		p2.put("a", 2);
		p2.put("b", 3);
		String expression1 = "a+b";
		Expression compiledExp1 = AviatorEvaluator.compile(expression1);
		System.out.println(compiledExp1.execute(p2)); // 5
		
		String expression2 = "a+b>3";
		Expression compiledExp2 = AviatorEvaluator.compile(expression2);
		System.out.println(compiledExp2.execute(p2)); // true
		
		/**
		 * 访问集合和数组
		 */
		List<String> plist = new ArrayList<>();
		plist.add("liudehua");
		int[] parray = new int[] {1, 2, 3};
		Map<String, Date> pmap = new HashMap<>();
		pmap.put("date", new Date());
		Map<String, Object> env = new HashMap<>();
		env.put("list", plist);
		env.put("array", parray);
		env.put("map", pmap);
		System.out.println(AviatorEvaluator.execute("'Singer is ' + list[0]", env));
		System.out.println(AviatorEvaluator.execute("'First number is ' + array[0]", env));
		// map["date"] 同样可行
		System.out.println(AviatorEvaluator.execute("'today is ' + map.date", env));
		
		/**
		 * 三元运算符
		 */
		System.out.println(AviatorEvaluator.exec("a>0? 'yes':'no'", 1)); // yes
		
		/**
		 * 正则表达
		 */
		Map<String, Object> env2 = new HashMap<>();
		env2.put("email", "dictwang@163.com");
		String uid = (String)AviatorEvaluator.execute("email=~/([\\w]+)@\\w+[\\.\\w]+/ ? $1 : 'unknow'", env2);
		System.out.println(uid);
		
		/**
		 * 操作数组和集合的seq库
		 */
		List<Integer> plist2 = new ArrayList<>();
		plist2.add(5);
		plist2.add(10);
		plist2.add(13);
		Map<String, Object> env3 = new HashMap<>();
		env3.put("list", plist2);
		System.out.println(AviatorEvaluator.execute("count(list)", env3));
		System.out.println(AviatorEvaluator.execute("reduce(list,+,0)", env3)); // 28
		System.out.println(AviatorEvaluator.execute("filter(list, seq.gt(6))", env3)); // [10,13]
		System.out.println(AviatorEvaluator.execute("include(list, 10)", env3)); // true
		System.out.println(AviatorEvaluator.execute("sort(list)", env3));
		System.out.println(AviatorEvaluator.execute("map(list, println)", env3));
		
		/**
		 * nil对象：类似java的null，用户变量中的null会自动转为nil
		 */
		System.out.println(AviatorEvaluator.execute("nil == nil")); // true
		System.out.println(AviatorEvaluator.execute("3>nil")); // true
		System.out.println(AviatorEvaluator.execute("''>nil")); // true
		System.out.println(AviatorEvaluator.execute("true != nil")); // true
		System.out.println(AviatorEvaluator.execute("a == nil")); // true
		
		/**
		 * 设置运行方式
		 */
		// 以执行速度优先
		AviatorEvaluator.setOptimize(AviatorEvaluator.EVAL);
		// 用setOption代替过期的方法
		AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
		// 以编译速度优先
		AviatorEvaluator.setOptimize(AviatorEvaluator.COMPILE);
		AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);
	}
}

class AddFunction extends AbstractFunction {

	@Override
	public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
			final AviatorObject arg2) {
		Number left = FunctionUtils.getNumberValue(arg1, env);
		Number right = FunctionUtils.getNumberValue(arg2, env);
		return new AviatorDouble(left.doubleValue() + right.doubleValue());
	}
	
	@Override
	public String getName() {
		return "add";
	}
	
}
