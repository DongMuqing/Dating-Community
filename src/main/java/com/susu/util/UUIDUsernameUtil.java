package com.susu.util;

import java.util.UUID;

/**
 * @Date:2023/8/11 17:56
 * @Created by Muqing
 */
public class UUIDUsernameUtil {
    public static String generateRandomUsername() {
        UUID uuid = UUID.randomUUID();
        String rawUsername = uuid.toString().replace("-", ""); // 去除横杠
        return rawUsername.substring(0, 10); // 取前10个字符作为用户名
    }
}
