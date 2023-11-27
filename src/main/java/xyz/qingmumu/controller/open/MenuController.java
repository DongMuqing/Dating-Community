package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qingmumu.dao.MenuDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Menu;
import xyz.qingmumu.entity.Result;

import java.util.List;

/**
 * @Date:2023/6/12 11:36
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/menu")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class MenuController {

    private static final String BEFORE_MENU = "before";
    @Autowired
    private MenuDao menuDao;

    /**
     * 获取前台导航栏
     *
     * @return
     */
    @GetMapping
    @SaIgnore
    public Result getMenu() {
        List<Menu> menus = menuDao.selectList(new QueryWrapper<Menu>().lambda().eq(Menu::getLocated, BEFORE_MENU));
        Integer code = menus != null ? Code.GET_OK : Code.GET_ERR;
        String msg = menus != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(menus, code, msg);
    }
}
