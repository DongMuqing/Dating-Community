package com.susu.entity;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * @Date:2023/4/29 20:29
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_user")
public class User {
    @TableId(type = IdType.AUTO)
    private  Integer id;
    private String avatar;
    private String email;
    private String username;
    private String password;
    @TableField(value="login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String loginTime;
    private String role;

    @TableField(exist = false)
    private SaTokenInfo tokenInfo;
    //邮箱注册
    public User( String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    //登录时返回信息
    public User(Integer id, String avatar, String username, String loginTime, String role,SaTokenInfo tokenInfo) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.loginTime = loginTime;
        this.role = role;
        this.tokenInfo=tokenInfo;
    }

    //用户修改自己信息构造方法
    public User(String avatar, String email, String username, String password) {
        this.avatar = avatar;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
