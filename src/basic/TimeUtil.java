package basic;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

	public static void main(String[] args) {
//		String[] firstPeriod = null;
//		String[] secondPeriod = null;
		
		String[] firstPeriod = {"08:00:00", "12:00:00"};
		String[] secondPeriod = {"12:00:00", "18:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // false

		firstPeriod = new String[]{"08:00:00", "12:00:00"};
		secondPeriod = new String[]{"08:00:00", "12:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true

		firstPeriod = new String[] {"08:00:00", "13:00:00"};
		secondPeriod = new String[] {"12:00:00", "18:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true

		firstPeriod = new String[] {"22:00:00", "02:00:00"};
		secondPeriod = new String[] {"08:00:00", "18:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // false
		
		firstPeriod = new String[] {"08:00:00", "18:00:00"};
		secondPeriod = new String[] {"22:00:00", "02:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // false

		firstPeriod = new String[] {"19:00:00", "02:00:59"};
		secondPeriod = new String[] {"19:00:00", "02:00:59"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true

		firstPeriod = new String[] {"22:00:00", "09:00:00"};
		secondPeriod = new String[] {"08:00:00", "09:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true

		firstPeriod = new String[] {"09:00:00", "23:00:00"};
		secondPeriod = new String[] {"13:00:00", "02:00:00"};
		System.out.println(isPeriodOverlapping(firstPeriod, secondPeriod)); // true
		
		showTimeDuration("2022-09-10 10:00:00", "2022-10-20 09:00:01");
		showDateDuration("2022-09-10", "2022-10-20");
		
		System.out.println(">>>> calNaturalMonthDays");
		int naturalMonthDays = calNaturalMonthDays("2022-10-10", "2022-10-21");
		System.out.println(naturalMonthDays);
		naturalMonthDays = calNaturalMonthDays("2022-10-01", "2022-10-31");
		System.out.println(naturalMonthDays);
		naturalMonthDays = calNaturalMonthDays("2022-10-10", "2022-11-21");
		System.out.println(naturalMonthDays);
		naturalMonthDays = calNaturalMonthDays("2022-11-10", "2023-01-01");
		System.out.println(naturalMonthDays);
		naturalMonthDays = calNaturalMonthDays("2023-01-01", "2023-01-31");
		System.out.println(naturalMonthDays);
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

	// 判断两段时间是否有重叠
	public static boolean isPeriodOverlapping(String[] first, String[] second) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime firstStart = LocalTime.parse(first[0], dtf);
		LocalTime firstEnd = LocalTime.parse(first[1], dtf);
		LocalTime secondStart = LocalTime.parse(second[0], dtf);
		LocalTime secondEnd = LocalTime.parse(second[1], dtf);

		boolean isFirstCrossDay = firstEnd.isBefore(firstStart);
		boolean isSecondCrossDay = secondEnd.isBefore(secondStart);
		
		if (isFirstCrossDay && isSecondCrossDay) {
			// 如果两个时间区间都跨天，必然会重叠
			return true;
		}
		
		Duration fd = Duration.between(firstStart, firstEnd);
		Duration sd = Duration.between(secondStart, secondEnd);

		long firstDuration = fd.getSeconds() >= 0 ? fd.getSeconds() : fd.getSeconds() + 86400;
		long secondDuration = sd.getSeconds() >= 0 ? sd.getSeconds() : sd.getSeconds() + 86400;

		LocalTime minStart = firstStart;
		LocalTime maxEnd = firstEnd;
		if (secondStart.isBefore(minStart)) {
			minStart = secondStart;
		}
		if (isSecondCrossDay) {
			// 如果第二个时间区间跨天，则以第二个时间区间的结束时间作为最大结束时间
			maxEnd = secondEnd;
		} else if (isFirstCrossDay) {
			// 如果第一个时间区间跨天，则以第一个时间区间的结束时间作为最大结束时间
			// maxEnd = firstEnd;
		} else {
			// 如果两个时间区间都不跨天，则以两者中最末时间作为最大结束时间
			if (secondEnd.isAfter(maxEnd)) {
				maxEnd = secondEnd;
			}
		}
		Duration td = Duration.between(minStart, maxEnd);
		long totalDuration = td.getSeconds() >= 0 ? td.getSeconds() : td.getSeconds() + 86400;
		
		// 如果总时间跨度小于两段时间跨度之和，则两段时间重叠
		return totalDuration < firstDuration + secondDuration;
	}
	
	public static int calNaturalMonthDays(String beginDate, String endDate) {
		LocalDate begin = LocalDate.parse(beginDate);
        LocalDate end = LocalDate.parse(endDate);

        LocalDate adjustBegin = LocalDate.of(begin.getYear(), begin.getMonth(), 1);
        LocalDate adjustEnd = LocalDate.of(end.getYear(), end.getMonth(), 1);
        adjustEnd = adjustEnd.plusMonths(1);

        // 计算所跨月份的自然天数
        int days = Long.valueOf(adjustBegin.until(adjustEnd, ChronoUnit.DAYS)).intValue();
        return days;
	}
}
