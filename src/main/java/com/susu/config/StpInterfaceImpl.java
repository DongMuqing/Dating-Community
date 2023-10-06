package com.susu.config;

import cn.dev33.satoken.stp.StpInterface;
import com.susu.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date:2023/10/6 17:29
 * @Created by Muqing
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UserDao userDao;
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        //o属性就是刚刚绑定的id，通过这个id去数据库查询权限
        Integer id= Integer.parseInt((String) o);
        String role = userDao.selectById(id).getRole();
        List<String> roleList = new ArrayList<>();
        //“*”权限表示什么都可以访问
        roleList.add(role);
        return roleList;
    }
}
