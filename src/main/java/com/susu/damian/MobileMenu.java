package com.susu.damian;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @Date:2023/7/28 17:32
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_mobile_menu")
public class MobileMenu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
}
