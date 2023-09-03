package com.susu.util;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.susu.damian.AliOss;
import com.susu.damian.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        // 创建ClientConfiguration实例，您可以根据实际情况修改默认参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
// 开启CNAME，CNAME用于将自定义域名绑定到目标Bucket。
        conf.setSupportCname(true);
        //获取阿里云OSS参数
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();
        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        //去掉uuid中间的-
        String fileName = UUID.randomUUID().toString().replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(customDomain, accessKeyId, accessKeySecret, conf);
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
    public List<AliOss> ListRequest(String path) throws Exception {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsV2Request请求。
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);

        // 设置prefix参数来获取fun目录下的所有文件与文件夹。需要在末尾加上/
        listObjectsV2Request.setPrefix(path);

        // 设置正斜线（/）为文件夹的分隔符。
        listObjectsV2Request.setDelimiter("/");
        // 发起列举请求。
        ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);
        List<AliOss> fileList = new ArrayList<>();
        int i = 0;
        // objectSummaries的列表中给出的是目录下的文件。
        for (OSSObjectSummary objectSummary : result.getObjectSummaries()) {
            SimplifiedObjectMeta objectMeta = ossClient.getSimplifiedObjectMeta(bucketName, objectSummary.getKey());
            Date lastModified = objectMeta.getLastModified();
            String localDateTime = TimeUtil.formatTime(lastModified);
            fileList.add(new AliOss(i, customDomain + "/" + objectSummary.getKey(), objectSummary.getKey(), localDateTime));
            i++;
        }
        // 关闭ossClient
        ossClient.shutdown();
        //去除第一个文件目录路径 [https://*/Userpics/,

        return fileList;
    }

    public Map<Integer, List<String>> getDirectoryAndFilePath() {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        // 列举文件。
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        List<String> directory = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        Map<Integer, List<String>> ossMap = new HashMap<>();
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            String key = objectSummary.getKey();
            //如果最后的字符为/则表示是一个路径
            //与传统文件系统中的层级结构不同，OSS内部使用扁平结构存储数据，即所有数据均以对象（Object）的形式保存在存储空间（Bucket）中。
            // 为方便管理，OSS管理控制台将所有文件名以正斜线（/）结尾的文件显示为文件夹，实现类似于Windows文件夹的基本功能
            if (key.substring(key.length() - 1).equals("/")) {
                directory.add(key);
            } else {
                //不是文件目录
                filePaths.add(customDomain + "/" + key);
            }
        }
        // 关闭ossClient
        ossClient.shutdown();
        ossMap.put(0, directory);
        ossMap.put(1, filePaths);
        return ossMap;
    }

    /**
     * 删除指定的单个文件
     *
     * @param objectName 文件在oss中的路径
     * @return
     */
    public boolean delete(String objectName) throws Exception {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //        使用Java SDK删除单个文件后，如何确定文件是否已成功删除？
//        在OSS Java SDK中使用OSSClient的deleteObject方法删除单个文件时，如果该方法没有抛出异常，
//        则说明已成功删除该文件。如果您需要进一步确认该文件是否已成功删除，
//        可以调用OSSClient的doesObjectExist方法，该方法可以判断指定的文件是否存在。如果该方法返回false，则说明该文件已成功删除。
        boolean found = ossClient.doesObjectExist(bucketName, objectName);
        if (found) {
            //当路径正确文件存在时才执行删除方法
            // 删除文件或目录。如果要删除目录，目录必须为空。
            VoidResult voidResult = ossClient.deleteObject(bucketName, objectName);
            // 关闭ossClient
            ossClient.shutdown();
            return true;
        } else {
            // 关闭ossClient
            ossClient.shutdown();
            return false;
        }
    }

    /**
     * 分页列举指定存储空间下指定前缀的所有文件。每页列举的文件个数通过maxKeys指定。
     * @param keyPrefix 查询的路径
     * @param page 查询第几页
     * @param maxKeys 一页的最大个数
     * @return
     */
    public Paging<AliOss> pagingEnumeration(String keyPrefix, Integer page,Integer maxKeys) {
        Paging<AliOss> aliOssPaging = new Paging<>();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String nextContinuationToken = null;
        ListObjectsV2Result result = null;
        //从第一页开始计数
        Integer i = 1;
        //文件的id
        Integer j = 1;
        //文件的总数
        Integer sum = 0;
        // 分页列举指定前缀的文件。
        HashMap<Integer, List<AliOss>> pagingMap = new HashMap<>();
        do {
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName).withMaxKeys(maxKeys);
            listObjectsV2Request.setPrefix(keyPrefix);
            listObjectsV2Request.setContinuationToken(nextContinuationToken);
            result = ossClient.listObjectsV2(listObjectsV2Request);
            List<OSSObjectSummary> sums = result.getObjectSummaries();
            List<AliOss> aliOssList = new ArrayList<>();
            for (OSSObjectSummary s : sums) {
                //当前页数和需要分页查询的页数一样
                if (i == page) {
                    SimplifiedObjectMeta objectMeta = ossClient.getSimplifiedObjectMeta(bucketName, s.getKey());
                    Date lastModified = objectMeta.getLastModified();
                    String localDateTime = TimeUtil.formatTime(lastModified);
                    aliOssList.add(new AliOss(j++, customDomain + "/" + s.getKey(), s.getKey(), localDateTime));
                }
                sum++;
            }
            if (i == page) {
                pagingMap.put(i, aliOssList);
                aliOssPaging.setData(pagingMap);
            }
            //下一页
            i++;
            nextContinuationToken = result.getNextContinuationToken();
        } while (result.isTruncated());
        // 关闭ossClient
        ossClient.shutdown();
        //当查询的页数小于等于当前总页数的时候
        if(page<=i-1){
            aliOssPaging.setTotalPages(i - 1);
            aliOssPaging.setSize(sum);
            return aliOssPaging;
        }else{
            return null;
        }
    }
}
