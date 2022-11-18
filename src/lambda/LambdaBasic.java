package lambda;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class LambdaBasic {

     public static void main(String[] args) {
           // 通过lambda表达式实现一个接口
           Interface1 interface1 = i -> i * 2;
           System.out.println(interface1.doubleNum(10));
           System.out.println(interface1.add(1, 2));
           System.out.println(Interface1.sub(10, 5));

           // 定义函数
           Function<Integer, String> moneyFormat = x -> new DecimalFormat("#,###").format(x);
           Person person = new Person("wangqiang", 1_000_000);
           person.printMoney(moneyFormat);

           // 定义断言
           IntPredicate predicate = i -> i > 0;
           System.out.println(predicate.test(10));
           System.out.println(predicate.negate().test(20));
           System.out.println(predicate.and(i -> i % 2 == 0).test(8));

           // 定义消费者
           Consumer<String> sout = System.out::println;
           Consumer<String> upsout = x -> System.out.println(x.toUpperCase());
           sout.accept("wangqiang");
           // 这里会传递消费，传递的值为初始值
           sout.andThen(upsout).accept("liudehua");

           // 两个参数的消费者
           BiConsumer<String, String> biConsumer = (x, y) -> System.out.println(x + ":" + y);
           biConsumer.accept("wangqiang", "Chengdu");

           // 函数柯里化
           Function<Integer, Function<Integer, Integer>> currying = x -> y -> x + y;
           System.out.println(currying.apply(10).apply(20));

           // 函数级联
           Function<Integer, Integer> func1 = x -> x * 2;
           Function<Integer, Integer> func2 = x -> x - 1;
           // func1 -> func2: return 19
           System.out.println(func1.andThen(func2).apply(10));
           // func2 -> func1: return 18
           System.out.println(func1.compose(func2).apply(10));

           List<Person> personLst = new ArrayList<>();
           personLst.add(new Person("zhao", 1000));
           personLst.add(new Person("qian", 2000));
           // list的stream操作，filter过滤
           List<Person> personFilter1 = personLst.stream().filter(p -> p.money > 1000).collect(Collectors.toList());
           personFilter1.forEach(p -> System.out.printf("%s -> %d\n", p.name, p.money));
           // map并行方法
           List<Integer> personFilter2 = personLst.stream().map(p -> p.money = p.money * 2).collect(Collectors.toList());
           personFilter2.forEach(p -> System.out.printf("%d\n", p));

           // flatMap方法，可以将两个stream展开后合并
           List<Integer> intLst1 = new ArrayList<>();
           intLst1.add(1);
           intLst1.add(2);
           List<Integer> intLst2 = new ArrayList<>();
           intLst2.add(3);
           intLst2.add(4);
           List<List<Integer>> mergeLst = Stream.of(intLst1, intLst2).collect(Collectors.toList());
           mergeLst.forEach(l -> l.forEach(System.out::println));
           List<Integer> mergeLst2 = Stream.of(intLst1, intLst2).flatMap(l -> l.stream()).collect(Collectors.toList());
           mergeLst2.forEach(System.out::println);

           // Optional的用法
           String text = "what is your name.";
           Optional<String> optional1 = Stream.of(text.split("\\s")).reduce((x, y) -> x+"|"+y);
           // 返回单词拼接结果
           System.out.println(optional1.get());
           System.out.println(optional1.orElse("liudehua"));

           String text2 = "I am a programing engineer";
           // 返回句子中长度最长的单词
           Optional<String> maxOptional = Stream.of(text2.split("\\s")).max((p1, p2) -> p1.length() - p2.length());
           System.out.println(maxOptional.get());
           Optional<String> maxOptional2 = Stream.of(text2.split("\\s")).max(Comparator.comparingInt(String::length));
           System.out.println(maxOptional2.get());

           OptionalInt findFirst = new Random().ints().findFirst();
           System.out.println(findFirst.getAsInt());

           // set的引用
           List<Person> personLstSet = new ArrayList<>();
           personLstSet.add(new Person("zhao", 10));
           personLstSet.add(new Person("qian", 20));
           personLstSet.add(new Person("qian", 30));
           Set<String> nameSet = personLstSet.stream().map(Person::getName).collect(Collectors.toSet());
           nameSet.stream().forEach(System.out::println);

           // summarizing统计相关应用
           List<Person> personLstSum = new ArrayList<>();
           personLstSum.add(new Person("zhao", 10));
           personLstSum.add(new Person("qian", 20));
           personLstSum.add(new Person("sun", 30));
           personLstSum.add(new Person("li", 10));
           IntSummaryStatistics statistics = personLstSum.stream().collect(Collectors.summarizingInt(Person::getMoney));
           System.out.println(statistics);

           // map类型相关的操作
           Map<String, Integer> map = new HashMap<>();
           map.put("a", 120);
           map.put("b", 30);
           map.put("c", 12);
           map.computeIfAbsent("d", k -> new Random().nextInt(100));
           System.out.println(map);
           map.computeIfPresent("d", (k, v)-> 150);
           System.out.println(map);
           map.merge("d", 100, Integer::sum);
           System.out.println(map);
           
           // 专项测试collect操作
           testCollect();
           
           // optional专项
           testOptional();
     }
     
     private static void testOptional() {
    	 
    	 Person p = new Person("sun", 18);
    	 List<String> books = new ArrayList();
    	 p.setBooks(books);
    	 String book = Optional.ofNullable(p.getBooks().get(0)).orElse("NULL Book");
    	 System.out.println(book);
     }
     
     private static void testCollect() {
    	 
    	 Person p1 = new Person("wang", 30);
    	 Person p2 = new Person("zhao", 10);
    	 Person p3 = new Person("wang", 20);
    	 Person p4 = new Person("qian", 10);
    	 
    	 // 分组
    	 List<Person> plist = new ArrayList<>();
    	 plist.add(p1);
    	 plist.add(p2);
    	 plist.add(p3);
    	 plist.add(p4);
    	 Map<String, List<Person>> groupMap = plist.stream().collect(Collectors.groupingBy(Person::getName));
    	 System.out.println(groupMap);
    	 
    	 List<Person> plistSorted = plist.stream().sorted((px, py) -> py.getMoney() - py.getMoney()).collect(Collectors.toList());
    	 System.out.println(plistSorted);
    	 boolean anyMatch = plist.stream().anyMatch(p -> p.getMoney() == 10);
    	 System.out.println(anyMatch);
    	 
    	 // 转map
    	 
    	 // 这种方式如果key重复会报错： Duplicate key
    	 // Map<String, Person> pMap = plist.stream().collect(Collectors.toMap(p -> p.getName(), p -> p));
    	 // 这种模式下 (k1, k2) -> k1 表示key冲突时，选择k1
    	 Map<String, Person> pMap = plist.stream().collect(Collectors.toMap(p -> p.getName(), p -> p, (k1, k2) -> k1));
    	 System.out.println(pMap);
    	 
    	 Person pf = plist.stream().filter(p -> p.money == 10).findAny().orElse(null);
    	 System.out.println(pf);
     }
     
     @FunctionalInterface
     private interface Interface1 {
           // 通过注解标注为函数类的接口，仅能定义一个抽象方法
           int doubleNum(int i);
           // 接口允许定义默认方法，需要标注为default方法才能有方法体
           default int add(int x, int y) {
                return x + y;
           }
           // 接口允许定义静态方法
           static int sub(int x, int y) {
                return x - y;
           }
     }
     
     private static class Person {
           String name;
           int money;
           List<String> books;

           public Person(String name, int money) {
                super();
                this.name = name;
                this.money = money;
           }
           public String getName() {
                return name;
           }
           public int getMoney() {
                return money;
           }
           public void printMoney(Function<Integer, String> format) {
                System.out.println(format.apply(this.money));
           }
           
           public String toString() {
        	   return String.format("{name=%s, money=%d}", this.name, this.money);
           }
           
           public void setBooks(List<String> books) {
        	   this.books = books;
           }
           
           public List<String> getBooks() {
        	   return this.books;
           }
     }
}