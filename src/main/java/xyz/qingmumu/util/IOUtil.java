package xyz.qingmumu.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date:2023/10/22 15:32
 * @Created by Muqing
 */
public class IOUtil {

    private static final List<String> fileSuffix = new ArrayList<>();

    static {
        fileSuffix.add("image/jpeg");
        fileSuffix.add("image/png");
    }

    /**
     * 判断文件是否为图片
     *
     * @param multipartFile
     * @return
     */
    public static boolean isImage(MultipartFile multipartFile) {
//        使用 getContentType() 方法获取上传文件的 MIME 类型。
        String fileType = multipartFile.getContentType();
        return fileSuffix.contains(fileType);
    }

}
