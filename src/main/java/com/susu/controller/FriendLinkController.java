package com.susu.controller;

import com.susu.damian.Code;
import com.susu.damian.FriendLink;
import com.susu.damian.Result;
import com.susu.dao.FriendLinkDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Date:2023/7/31 22:27
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/friendlink")
@CrossOrigin
@Slf4j
public class FriendLinkController {
    @Autowired
    private FriendLinkDao  friendLinkDao;

    @GetMapping
    public Result getAll(){
        List<FriendLink> friendLinks = friendLinkDao.selectList(null);
        Integer code = friendLinks != null ? Code.GET_OK : Code.GET_ERR;
        String msg = friendLinks != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(friendLinks,code,msg);
    }
}
