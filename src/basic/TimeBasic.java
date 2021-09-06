package basic;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/**
 * 关于时间的一些缩写符号
 * GMT Greenwich Mean Time 格林威治标准时间
 * UT Universal Time 世界时间
 * UTC Coordinated Universal Time 经协调的世界时间
 * CST 包含多种释义，如 China Standard Time 中国标准时间
 * */
public class TimeBasic {
	
	public static void main(String[] args) throws Exception {
		// 有关时间的一个问题
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		String ts1 = "1927-12-31 23:54:07";
		String ts2 = "1927-12-31 23:54:08";
		Date t1 = sdf.parse(ts1);
		Date t2 = sdf.parse(ts2);
		long tts1 = t1.getTime() / 1000;
		long tts2 = t2.getTime() / 1000;
		// 在jdk1.5之前，这里的时间差将是300多秒；可能的原因是1927年政府进行过一次时间调整
		System.out.println(tts2 - tts1);
		
		// 打印当前时间戳，实际上调用的是一个native本地方法
		// 此方法是不稳定的，存在一定的误差，根据系统的不同存在几毫秒到数十毫秒误差
		System.out.println(System.currentTimeMillis());
		// 通过纳秒来转换毫秒，但这种方式获取的不是当前时间，仅是一个递增的数值，并且有溢出的可能
		System.out.println(System.nanoTime() / 1000);
		
		// 此构造方法实际上调用的是System.currentTimeMillis()
		// Date对象内部保存了一个时间戳，是当前时间距离1970-01-01 00:00:00的秒数
		// 无论当前系统处于哪个市区，同一时间点获取到的秒数都是一样的
		// 在打印或显示阶段，才会依据实际的时区进行时间展示
		Date d1 = new Date();
		Date d2 = new Date(0);
		// Tue Feb 23 13:41:21 CST 2021
		// CST表示中国标准时间
		System.out.println(d1);
		// Thu Jan 01 08:00:00 CST 1970
		System.out.println(d2);
		
		// 时间的格式化
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 这里的时区设置可以是 GMT+x或者 timezoneId形式
		// 如 GMT+8,Asia/Shanghai
		sdf2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
		// 1970-01-01 06:00:00
		System.out.println(sdf2.format(d2));
		
		sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		// 东八区当前时间
		System.out.println(sdf2.format(d1));
		
		// jdk1.8开始的时间api java.time包
		// 当前日期
		LocalDate today = LocalDate.now();
		// yyyy-MM-dd
		System.out.println(today);
		// 所在当月的天数
		System.out.println(today.getDayOfMonth());
		// 是否是闰年
		System.out.println(today.isLeapYear());
		
		LocalDate custom1 = LocalDate.ofEpochDay(10);
		// 1970-01-11 从1970-01-01开始的x天后的日期
		System.out.println(custom1);
		LocalDate custom2 = LocalDate.of(2021, 1, 10);
		// 2021-01-10
		System.out.println(custom2);
		
		// 当前时间
		LocalTime now = LocalTime.now();
		System.out.println(now);
		// 当前日期+时间
		LocalDateTime dateTime = LocalDateTime.now();
		// 输出格式如 2021-02-23T14:30:42.798
		System.out.println(dateTime);
		
		// 单个时间的操作
		// 明天日期
		LocalDate nextDay = today.plusDays(1);
		System.out.println(nextDay);
		// 后天日期
		LocalDate after2Day = nextDay.plus(1, ChronoUnit.DAYS);
		System.out.println(after2Day);
		// 一周前的日期
		LocalDate lastWeek = today.minus(1, ChronoUnit.WEEKS);
		System.out.println(lastWeek);
		
		// 两个时间的操作
		System.out.println(nextDay.isAfter(today));
		
		// 格式化处理
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("G yyyy年MM月dd日 E a hh时mm分ss秒");
		dtf = dtf.withZone(ZoneId.of("GMT+0"));
		// 公元 2021年02月23号 星期二 下午 04时05分14秒
		System.out.println(dtf.format(dateTime));
		
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.parse("2020-12-25 10:10:05", dtf2);
		System.out.println(ldt);
		
		Instant inow = Instant.now();
		// 获取当前时间毫秒数
		System.out.println(inow.toEpochMilli());
		// 毫秒时间戳转换成Instant
		Instant ins2 = Instant.ofEpochMilli(1614068958474L);
		System.out.println(ins2);
		
		// instant转LocalDateTime
		LocalDateTime insldt = LocalDateTime.ofInstant(ins2, ZoneId.of("America/Chicago"));
		// 2021-02-23 02:29:18 芝加哥时间
		System.out.println(dtf2.format(insldt));
		// 2021-02-23 16:29:18 北京时间
		System.out.println(dtf2.format(LocalDateTime.ofInstant(insldt.atZone(ZoneId.of("America/Chicago")).toInstant(), ZoneId.of("GMT+8"))));
		// 2021-02-23 00:29:18 西8区时间
		dtf2 = dtf2.withZone(ZoneId.of("GMT-8"));
		System.out.println(dtf2.format(ins2));
		
		// LocalDateTime转instant
		Instant ins3 = insldt.atZone(ZoneId.of("America/Chicago")).toInstant();
		// 2021-02-23T08:29:18.474Z
		System.out.println(ins3);
		
		// 时间差计算
		LocalDateTime dur01 = LocalDateTime.of(2021, 2, 22, 10, 10);
		LocalDateTime dur02 = LocalDateTime.of(2021, 2, 23, 0, 0);
		Duration d01 = Duration.between(dur01, dur02);
		System.out.println(d01.toDays()); // 0
		System.out.println(d01.toHours()); // 13
		System.out.println(d01.toMinutes()); // 830
		
		// 新老时间api的转换
		Date olddate = new Date();
		Instant ins21 = olddate.toInstant();
		Date olddate2 = Date.from(ins21);
		System.out.println(olddate2);
		
		Calendar cal21 = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));
		Instant ins22 = cal21.toInstant();
		System.out.println(ins22);
	}
}
