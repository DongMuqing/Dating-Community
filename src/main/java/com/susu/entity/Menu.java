package com.susu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

/**
 * @Date:2023/6/12 11:30
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Menu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
}
