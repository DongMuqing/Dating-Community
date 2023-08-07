package com.susu.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Date:2023/8/5 14:37
 * @Created by Muqing
 */

public class IpInfo {

    public static String getInfo(HttpServletRequest request, ResourceLoader resourceLoader) throws Exception {
        String ip = IPUtil.getIpAddr(request);
        String fileName = "ip2region.xdb";
        Resource resource = resourceLoader.getResource("classpath:data/" + fileName);
        InputStream inputStream = resource.getInputStream();
        byte[] cBuff = inputStream.readAllBytes();
        inputStream.close();
        // 1、从 dbPath 加载整个 xdb 到内存。
//        String dbPath = new String(Buff);
        // 1、从 dbPath 加载整个 xdb 到内存。
//        byte[] cBuff = Searcher.loadContentFromFile(dbPath);
        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        Searcher searcher = Searcher.newWithBuffer(cBuff);
        // 3、查询
        long sTime = System.nanoTime();
        //region: 0|0|0|内网IP|内网IP, ioCount: 0, took: 62 μs
        //        国家|区域|省份|城市|ISP
        String region = searcher.search(ip);
        String[] regions = region.split("\\|");
        Map<Integer, String> regionMap = new HashMap<>();
        String ipInfo = "";
        if (regions.length == 5) {
            for (int i = 0; i < regions.length; i++) {
                regionMap.put(i, regions[i]);
            }
            //国内地址则显示具体省份和运营商
            if (regionMap.get(0).equals("中国")) {
                ipInfo = regionMap.get(2) + "|" + regionMap.get(3) + "|" + regionMap.get(4);
            } else if (regionMap.get(3).equals("内网IP")) {
                ipInfo = "内网";
            } else {
                ipInfo = regionMap.get(0);
            }
        }
        //查询时间
        long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
        return ipInfo;
    }
}
