package com.susu.controller.admin;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.susu.dao.VisitorInfoDao;
import com.susu.entity.Code;
import com.susu.entity.Result;
import com.susu.entity.VisitorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Date:2023/7/20 16:33
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/admin/ip")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AdminAddressController {
    @Autowired
    private VisitorInfoDao visitorInfoDao;

    /**
     * 获取访客信息
     *
     * @return
     */
    @PostMapping("/getVisitorInfo")
    @SaCheckRole("管理员")
    public Result visitorInfo(@RequestParam(defaultValue = "1") long current,
                              @RequestParam(defaultValue = "20") long size) {
        LambdaQueryWrapper<VisitorInfo> visitorInfoLambdaQueryWrapper = Wrappers.lambdaQuery();
        visitorInfoLambdaQueryWrapper.orderBy(true, false, VisitorInfo::getAccessTime);
        Page<VisitorInfo> visitorInfoPage = new Page<>(current, size);
        IPage<VisitorInfo> postIPage = visitorInfoDao.selectPage(visitorInfoPage, visitorInfoLambdaQueryWrapper);
        HashMap<String, Object> visitorInfoMap = new HashMap<>();
        if (postIPage != null) {
            visitorInfoMap.put("data", postIPage.getRecords());
            visitorInfoMap.put("pages", postIPage.getPages());
            visitorInfoMap.put("total", postIPage.getTotal());
            return new Result(visitorInfoMap, Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }

}
