package xyz.qingmumu.controller.open;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qingmumu.dao.VisitorInfoDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import xyz.qingmumu.entity.VisitorInfo;
import xyz.qingmumu.util.IPUtil;
import xyz.qingmumu.util.IpInfo;
import xyz.qingmumu.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Date:2023/7/20 16:33
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/ip")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AddressController {
    @Autowired
    private VisitorInfoDao visitorInfoDao;
    @Autowired
    private ResourceLoader resourceLoader;


    /**
     * 记录访客信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/visitorInfo")
    @SaIgnore
    public Result visitorInfo(HttpServletRequest request) throws Exception {
        int flag;
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));

        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        String browser = userAgent.getBrowser().toString();
        String os = userAgent.getOperatingSystem().getName();
        String ip = IPUtil.getIpAddr(request);
        String ipInfo = IpInfo.getInfo(request, resourceLoader);
        LocalDateTime accessTime = TimeUtil.getLocalDateTime();

        VisitorInfo visitorInfo = new VisitorInfo(accessTime, ip, ipInfo, clientType, os, browser);
        flag = visitorInfoDao.insert(visitorInfo);
        Integer code = flag == 1 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag == 1 ? "插入成功" : "插入失败！";
        return new Result(null, code, msg);
    }
}
