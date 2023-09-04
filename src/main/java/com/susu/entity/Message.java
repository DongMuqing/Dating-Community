package com.susu.entity;

import lombok.*;

/**
 * @Date:2023/8/27 21:04
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private Long id;
    private String name;
    private String time;
    private String msg;
}
