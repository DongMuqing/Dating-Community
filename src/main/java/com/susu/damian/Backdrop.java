package com.susu.damian;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @Date:2023/6/13 19:38
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_backdrop")
public class Backdrop {
    private Integer id;
    private String url;
}
