package com.susu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @Date:2023/7/31 22:24
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_friendlink")
public class FriendLink {
    @TableId(type = IdType.AUTO)
    private Integer id;
//    @TableField(fill = FieldFill.DEFAULT)
    private String logo;
    private String url;
    private String name;
    private String intro;

    public FriendLink(String logo, String url, String name, String intro) {
        this.logo = logo;
        this.url = url;
        this.name = name;
        this.intro = intro;
    }
}
