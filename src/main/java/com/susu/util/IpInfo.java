package com.susu.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Date:2023/8/5 14:37
 * @Created by Muqing
 */

public class IpInfo {
    private static final Searcher searcher;

    /*
     * Initialize the Searcher object.
     */
    static {
        long startTime = System.nanoTime();
        try (InputStream inputStream = IpInfo.class.getClassLoader().getResourceAsStream("data/ip2region.xdb")) {
            if (inputStream == null) {
                throw new IOException("ip2region.xdb loading failed.");
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                searcher  = Searcher.newWithBuffer(output.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInfo(HttpServletRequest request, ResourceLoader resourceLoader) throws Exception {
        String ip = IPUtil.getIpAddr(request);
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

    public static String getAddress(HttpServletRequest request, ResourceLoader resourceLoader) throws Exception {
        String ip = IPUtil.getIpAddr(request);
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
            if (regionMap.get(0).equals("中国")) {
                //只显示城市
                ipInfo = regionMap.get(2);
            } else if (regionMap.get(3).equals("内网IP")) {
                ipInfo = "内网";
            } else {
                ipInfo = regionMap.get(0);
            }
        }
        return ipInfo;
    }
}
