package com.susu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @Date:2023/8/11 0:02
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_comment")
public class Comment {
//    @TableField(value = "comment_id")
    @TableId(value = "comment_id",type = IdType.AUTO)
    private Integer commentId;
    @TableField(value = "post_id")
    private Integer postId;
    @TableField(value = "user_id")
    private Integer userId;
    private String avatar;
    private String username;
    private  String content;

    @TableField(value ="created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String address;


    public Comment(Integer postId, String avatar,String username, String content, LocalDateTime createTime, String address) {
        this.postId = postId;
        this.avatar=avatar;
        this.username = username;
        this.content = content;
        this.createTime = createTime;
        this.address = address;
    }
}
