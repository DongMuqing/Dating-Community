package com.susu.damian;

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
    private Integer id;
    private String logo;
    private String url;
    private String name;
    private String intro;
}
