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
    private  String imgSrcList;
    private String avatar;
    private String username;
    @TableField(exist = false) //  不映射到数据库
    private List<Comment> comments;

    public Post(LocalDateTime createTime, String title, String content) {
        this.createTime = createTime;
        this.title = title;
        this.content = content;
    }

    public Post(Integer userId, LocalDateTime createTime, String title, String content, String location, String imgSrcList, String avatar, String username) {
        this.userId = userId;
        this.createTime = createTime;
        this.title = title;
        this.content = content;
        this.location = location;
        this.imgSrcList = imgSrcList;
        this.avatar = avatar;
        this.username = username;
    }
}
