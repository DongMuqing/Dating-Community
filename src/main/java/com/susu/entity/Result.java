package com.susu.entity;

import lombok.*;

/**
 * @Date:2023/6/9 17:49
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Object data;
    private Integer code;
    private String msg;
}
