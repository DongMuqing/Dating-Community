package xyz.qingmumu.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Date:2023/7/23 15:37
 * @Created by Muqing
 */
public class TimeUtil {
    public static LocalDateTime getLocalDateTime() {
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        //修复bug  LocalDateTime解析日期字符串值时丢弃秒值“00“
        String visitTimeFormat = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        // 将格式化后的字符串转换为 LocalDateTime 对象
        LocalDateTime LocalDateTime = java.time.LocalDateTime.parse(visitTimeFormat);
        return LocalDateTime;
    }

    public static String formatTime(Date date) {
        SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return myFmt.format(date);
    }
}
