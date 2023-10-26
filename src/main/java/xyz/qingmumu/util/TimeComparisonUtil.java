package xyz.qingmumu.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @Date:2023/8/12 13:35
 * @Created by Muqing
 */
public class TimeComparisonUtil {
    /**
     * 时间格式化
     *
     * @param inputTime
     * @return
     */
    public static String compareTime(LocalDateTime inputTime) {
        LocalDateTime now = LocalDateTime.now();

        if (isSameDay(inputTime, now)) {
            return inputTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        } else if (isYesterday(inputTime, now)) {
            return "昨天 " + inputTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        } else if (isSameYear(inputTime, now)) {
            return inputTime.format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));
        } else {
            return inputTime.toString();
        }
    }

    private static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    private static boolean isYesterday(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.plus(1, ChronoUnit.DAYS).toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    private static boolean isSameYear(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.getYear() == dateTime2.getYear();
    }
}
