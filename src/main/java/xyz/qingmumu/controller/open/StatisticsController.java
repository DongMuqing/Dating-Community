package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qingmumu.dao.ArticleDao;
import xyz.qingmumu.dao.PostDao;
import xyz.qingmumu.dao.VisitorInfoDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date:2023/8/15 13:33
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/statistics")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class StatisticsController {

    @Autowired
    private PostDao postdao;
    @Autowired
    private VisitorInfoDao visitorInfoDao;
    @Autowired
    private ArticleDao articleDao;

    @PostMapping
    @SaIgnore
    public Result PostAndWatching() {
        Long watching = visitorInfoDao.selectCount(null);
        Long dynamic = postdao.selectCount(null);
        Long article = articleDao.selectCount(null);
        List<Long> numbers = new ArrayList<>();
        numbers.add(watching);
        numbers.add(dynamic + article);
        Integer code = Code.GET_OK;
        String msg = "获取成功";
        return new Result(numbers, code, msg);
    }
}
