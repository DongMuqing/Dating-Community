package com.susu.damian;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @Date:2023/6/13 17:33
 * @Created by Muqing
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_dynamic")
public class Dynamic {
    private Integer id;
    @TableField(value="create_time")
    private String createTime;
    private String title;
    private String content;
    private String location;
    @TableField(value="upvote_num")
    private Integer upvoteNum;
}
