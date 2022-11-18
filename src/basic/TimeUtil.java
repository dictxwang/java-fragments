package basic;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

	public static void main(String[] args) {
		
		String[] firstPeriod = {"08:00:00", "12:00:00"};
		String[] secondPeriod = {"12:00:00", "18:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // false

		firstPeriod = new String[] {"08:00:00", "13:00:00"};
		secondPeriod = new String[] {"12:00:00", "18:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true

		firstPeriod = new String[] {"22:00:00", "02:00:00"};
		secondPeriod = new String[] {"08:00:00", "18:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // false
		
		firstPeriod = new String[] {"08:00:00", "18:00:00"};
		secondPeriod = new String[] {"22:00:00", "02:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // false

		firstPeriod = new String[] {"23:00:00", "01:00:00"};
		secondPeriod = new String[] {"22:00:00", "02:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true

		firstPeriod = new String[] {"22:00:00", "09:00:00"};
		secondPeriod = new String[] {"08:00:00", "09:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true
		
		showTimeDuration("2022-09-10 10:00:00", "2022-10-20 09:00:01");
		showDateDuration("2022-09-10", "2022-10-20");
	}
	
	// 使用时间差
	public static void showTimeDuration(String beginDateTime, String endDateTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime begin = LocalDateTime.parse(beginDateTime, dtf);
		LocalDateTime end = LocalDateTime.parse(endDateTime, dtf);
		Duration duration = Duration.between(begin ,end);
		System.out.println(duration.toDays());
	}
	
	// 使用日期差
	public static void showDateDuration(String beginDate, String endDate) {
		LocalDate begin = LocalDate.parse(beginDate);
		// 默认日期格式即为 ”yyyy-MM-dd“
		LocalDate end = LocalDate.parse(endDate);
		long days = begin.until(end, ChronoUnit.DAYS);
		System.out.println(days);
	}
	
	public static boolean isPeriodOverlapping(String[] first, String[] second) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime firstStart = LocalTime.parse(first[0], dtf);
		LocalTime firstEnd = LocalTime.parse(first[1], dtf);
		LocalTime secondStart = LocalTime.parse(second[0], dtf);
		LocalTime secondEnd = LocalTime.parse(second[1], dtf);
		
		boolean isFirstCrossDay = firstEnd.isBefore(firstStart);
		boolean isSecondCrossDay = secondEnd.isBefore(secondStart);
		
		// 如果两个时间区间都跨天，必然会重叠
		if (isFirstCrossDay && isSecondCrossDay) {
			return true;
		}
		
		LocalTime maxStart = null;
		LocalTime minEnd = null;
		if (isFirstCrossDay) {
			maxStart = secondStart;
		} else if (isSecondCrossDay) {
			maxStart = firstStart;
		} else {
			maxStart = firstStart.isAfter(secondStart) ? firstStart : secondStart;
		}
		minEnd = firstEnd.isBefore(secondEnd) ? firstEnd : secondEnd;
		
		// 如果两个时间段，最大开始时间 < 最小结束时间，则时间段重复
		return maxStart.isBefore(minEnd);
	}
}
