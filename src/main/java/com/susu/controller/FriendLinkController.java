package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.susu.damian.Code;
import com.susu.damian.FriendLink;
import com.susu.damian.Result;
import com.susu.dao.FriendLinkDao;
import com.susu.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Date:2023/7/31 22:27
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/friendlink")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class FriendLinkController {
    @Autowired
    private FriendLinkDao friendLinkDao;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    //上传指定的文件夹
    private String folderPath = "Blog/friendlink/";

    @GetMapping
    @SaIgnore
    public Result getAll() {
        List<FriendLink> friendLinks = friendLinkDao.selectList(null);
        Integer code = friendLinks != null ? Code.GET_OK : Code.GET_ERR;
        String msg = friendLinks != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(friendLinks, code, msg);
    }

    @PostMapping
    @SaIgnore
    //使用@RequestPart 不能使用@RequestBody 改用表单提交
    public Result upload(@RequestParam("url") String url,
                         @RequestParam("name") String name,
                         @RequestParam("intro") String intro,
                         @RequestPart("logo") MultipartFile image) throws IOException {
        String logoUrl = aliOSSUtils.upload(image, folderPath);
        FriendLink friendLink = new FriendLink(logoUrl, url, name, intro);
        int friendLinks = friendLinkDao.insert(friendLink);
        Integer code = friendLinks != 0 ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = friendLinks != 0 ? "提交成功！" : "数据提交失败，请重试！";
        return new Result(null, code, msg);
    }


}
