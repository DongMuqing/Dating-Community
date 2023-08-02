package com.susu.damian;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private String username;
    private String password;
}
