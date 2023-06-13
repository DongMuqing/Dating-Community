package com.susu.damian;

import lombok.*;

/**
 * @Date:2023/6/12 11:30
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Menu {
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
}
