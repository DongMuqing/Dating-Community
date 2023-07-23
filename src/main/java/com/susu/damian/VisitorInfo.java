package com.susu.damian;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @Date:2023/7/23 14:36
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("tb_visitorinfo")
public class VisitorInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value="access_time")
    private LocalDateTime accessTime;
    private String ip;
    @TableField(value = "client_type")
    private String clientType;
    private String os;
    private String browser;

    private String address;
    public VisitorInfo(LocalDateTime accessTime, String ip, String clientType, String os, String browser) {
        this.accessTime = accessTime;
        this.ip = ip;
        this.clientType = clientType;
        this.os = os;
        this.browser = browser;

    }
}
