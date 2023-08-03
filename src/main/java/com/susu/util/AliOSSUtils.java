package com.susu.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Date:2023/8/3 11:44
 * @Created by Muqing
 */
@Component
@Slf4j
public class AliOSSUtils {
    @Autowired
    private AliOSSProperties aliOSSProperties;
    @Value("${aliyun.oss.customDomain}")
    private String customDomain;

    /**
     * 上传文件到oss
     * @param file 文件
     * @param folderPath 上传路径
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile file,String folderPath) throws IOException {
        //获取阿里云OSS参数
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();

        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();

        //去掉uuid中间的-
        String fileName = UUID.randomUUID().toString().replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, folderPath+fileName, inputStream);

        //文件访问路径(替换自定义域名)
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        String originalUrl = endpoint.replace("https://", "https://" + bucketName + ".");
        String newUrl = url.replace(originalUrl, customDomain);

        // 关闭ossClient
        ossClient.shutdown();
        inputStream.close();

        // 把上传到oss的路径返回
        return  newUrl;
    }
}
