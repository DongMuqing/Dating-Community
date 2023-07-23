package com.susu.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Date:2023/7/23 15:37
 * @Created by Muqing
 */
public class TimeUtil {
    public static LocalDateTime getLocalDateTime(){
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间为指定格式
        String formattedTimeAsString = currentTime.format(formatter);
        // 将格式化后的字符串转换为 LocalDateTime 对象
        LocalDateTime LocalDateTime = java.time.LocalDateTime.parse(formattedTimeAsString, formatter);
        return LocalDateTime;
    }
}
