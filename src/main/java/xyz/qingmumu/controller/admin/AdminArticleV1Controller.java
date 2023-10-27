package xyz.qingmumu.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.qingmumu.dao.ArticleDao;
import xyz.qingmumu.entity.Article;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Post;
import xyz.qingmumu.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/6/9 17:46
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/admin/article")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AdminArticleV1Controller {
    @Autowired
    private ArticleDao articleDao;

    @PostMapping("/add")
    @SaCheckRole("管理员")
    public Result addArticle(@RequestBody Article articles) {
        articles.setUserId((Integer) StpUtil.getLoginId());
        int flag = articleDao.insert(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "添加成功" : "数据添加失败，请重试！";
        return new Result(null, code, msg);
    }


    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @SaCheckRole("管理员")
    public Result delArticle(@RequestParam("id") Integer id) {
        int flag = articleDao.deleteById(id);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "删除成功" : "数据删除失败，请重试！";
        List<Article> allArticle = getAllArticle();
        return new Result(allArticle, code, msg);
    }

    /**
     * 修改文章
     *
     * @param articles
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole("管理员")
    public Result editArticle(@RequestBody Article articles) {
        int flag = articleDao.updateById(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "修改成功" : "数据修改失败，请重试！";
        List<Article> allArticle = getAllArticle();
        return new Result(allArticle, code, msg);
    }

    /**
     * 获取文章数据
     *
     * @return
     */
    @GetMapping("/get")
    @SaCheckRole("管理员")
    public Result getArticle(@RequestParam(defaultValue = "1") long current,
                             @RequestParam(defaultValue = "10") long size) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = Wrappers.lambdaQuery();
        articleLambdaQueryWrapper.orderBy(true, false, Article::getCreateTime);
        Page<Article> articlePage = new Page<>(current, size);
        IPage<Article> articleIPage = articleDao.selectPage(articlePage, articleLambdaQueryWrapper);
        HashMap<String, Object> postMap = new HashMap<>();
        if (articleIPage != null) {
            postMap.put("data", articleIPage.getRecords());
            postMap.put("pages", articleIPage.getPages());
            postMap.put("total", articleIPage.getTotal());
            return new Result(postMap, Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }
    /**
     * 获取所有文章时间降序
     *
     * @return
     */
    public List<Article> getAllArticle() {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.orderBy(true, false, Article::getCreateTime);
        List<Article> articles = articleDao.selectList(articleLambdaQueryWrapper);
        return articles;
    }
}