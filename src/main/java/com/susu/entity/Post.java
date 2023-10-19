package com.susu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
@TableName("tb_post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    private String avatar;
    @TableField(value="create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String title;
    private String content;
    private String location;
    @TableField(value="upvote_num")
    private Integer upvoteNum;

    @TableField(value="music_url")
    private String musicUrl;

    @TableField(value="img_srclist")
    private  String imgSrclist;

    @TableField(exist = false) //  不映射到数据库
    private List<Comment> comments;
}
