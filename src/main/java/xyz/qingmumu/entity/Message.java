package xyz.qingmumu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @Date:2023/11/25 13:40
 * @Created by Muqing
 * socket信息实体类
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_message")
public class Message {
    //消息主键
    @TableId(type = IdType.AUTO)
    private Long id;

    //发送用户id
    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "username")
    private String username;

    @TableField(value = "avatar")
    private String avatar;
    //消息发送时间
    @TableField(value = "sending_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendingTime;

    //消息内容  后续扩展可发送文件
    @TableField(value = "msg")
    private String msg;

    public Message(Integer userId, String username, String avatar, LocalDateTime sendingTime, String msg) {
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.sendingTime = sendingTime;
        this.msg = msg;
    }
}
