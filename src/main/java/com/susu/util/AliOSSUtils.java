package com.susu.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 上传文件到oss
     *
     * @param file       文件
     * @param folderPath 上传路径 文件层级
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile file, String folderPath) throws IOException {
        //获取阿里云OSS参数

        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();

        //去掉uuid中间的-
        String fileName = UUID.randomUUID().toString().replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, folderPath + fileName, inputStream);

        //文件访问路径(替换自定义域名)
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        String originalUrl = endpoint.replace("https://", "https://" + bucketName + ".");

        //表示文件层级时  folderPath /会在末尾
        //eg:"Blog/friendlink/"
        //但是在最后在拼接url时会多出一个/ 需要再处理一次
        String output = folderPath.replaceAll("/+$", "");
        String newUrl = url.replace(originalUrl, customDomain + "/" + output);

        // 关闭ossClient
        ossClient.shutdown();
        inputStream.close();

        // 把上传到oss的路径返回
        return newUrl;
    }

    /**
     * @param path 指定的目录路径
     * @return 所有文件的url地址
     * @throws Exception
     */
    public List<String> ListRequest(String path) throws Exception {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsV2Request请求。
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);

        // 设置prefix参数来获取fun目录下的所有文件与文件夹。需要在末尾加上/
        listObjectsV2Request.setPrefix(path + "/");

        // 设置正斜线（/）为文件夹的分隔符。
        listObjectsV2Request.setDelimiter("/");
        // 发起列举请求。
        ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);
        List<String> fileList = new ArrayList<>();
        // objectSummaries的列表中给出的是目录下的文件。
        for (OSSObjectSummary objectSummary : result.getObjectSummaries()) {
            fileList.add(customDomain + "/" + objectSummary.getKey());
        }
        //去除第一个无文件路径 [https://oss.qingmumu.xyz/Userpics/,
        fileList.remove(0);
        return fileList;
    }

    public void testListObjects() {

        // commonPrefixs列表中显示的是fun目录下的所有子文件夹。由于fun/movie/001.avi和fun/movie/007.avi属于fun文件夹下的movie目录，因此这两个文件未在列表中。
//        for (String commonPrefix : result.getCommonPrefixes()) {
//            System.out.println(commonPrefix);
//        }
    }
}
