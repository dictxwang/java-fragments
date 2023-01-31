package basic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalBasic {

	public static void main(String[] args) {

		System.out.println(calAttendanceRateCommon(120, 200));
	}

	/**
	 * 计算百分比
	 * @param actuallyDays
	 * @param shouldDays
	 * @return
	 */
    public static String calAttendanceRateCommon(int actuallyDays, int shouldDays) {
        BigDecimal actuallyCheckinDays = new BigDecimal(actuallyDays);
        BigDecimal shouldCheckinDays = new BigDecimal(shouldDays);

        if (shouldCheckinDays.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO.toString();
        }

        return actuallyCheckinDays.multiply(new BigDecimal(100))
                .divide(shouldCheckinDays, 2, RoundingMode.HALF_UP)
                .toString();
    }
}
