package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.qingmumu.dao.FriendLinkDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.FriendLink;
import xyz.qingmumu.entity.Result;

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
                         @RequestParam("logo") String image) {
        FriendLink friendLink = new FriendLink(image, url, name, intro);
        int friendLinks = friendLinkDao.insert(friendLink);
        Integer code = friendLinks != 0 ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = friendLinks != 0 ? "提交成功！" : "数据提交失败，请重试！";
        return new Result(null, code, msg);
    }


}
