package basic;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RegexBasic {
     public static void main(String[] args) {
           
           String regex = "(\\d{2})(\\d)";
           // 调用静态方法将整个串与模式进行匹配
           System.out.println(Pattern.matches(regex, "123"));
           // 通过成员方法进行匹配
           Pattern pattern = Pattern.compile(regex);
           String value = "123abc890efg12xyz";
           Matcher matcher = pattern.matcher(value);
           System.out.println(matcher.matches());
           // 因为调用了matches()，需要先重置再重新开始匹配
           matcher.reset();
           // 遍历所有匹配结果
           while (matcher.find()) {
	           // 返回匹配的组数（表达式中一对括号为一组）
	           System.out.println(matcher.groupCount());
	           // 返回第一组匹配
	           System.out.println(matcher.group(1));
	           // 返回本次所有匹配内容
	           System.out.println(matcher.group());
	           System.out.println(matcher.start() + " -> " + matcher.end());
	           // 尝试将输入串开头与模式进行匹配（如果匹配成功，会将匹配位置移动到开头，从而重新开启匹配）
	           // System.out.println(matcher.lookingAt());
           }
     }
}