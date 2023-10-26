package xyz.qingmumu.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @Date:2023/8/20 20:31
 * @Created by Muqing
 */
public class VerCodeGenerateUtil {
    //邮箱字符串提取，去除了容易混淆的几个字符比如0,o~
    private static final String SYMBOLS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    /**
     * 生成4位随机验证码
     * @return 返回4位验证码
     */
    public static String getVerCode() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}
