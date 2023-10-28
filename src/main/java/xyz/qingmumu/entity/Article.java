package xyz.qingmumu.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @Date:2023/6/9 10:29
 * @Created by Muqing
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    private String username;
    private String cover;
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Article(Integer userId, String username, String cover, String title, String content, LocalDateTime createTime) {
        this.userId = userId;
        this.username = username;
        this.cover = cover;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }
}
