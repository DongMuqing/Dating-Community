package com.susu.entity;

import lombok.*;

/**
 * @Date:2023/8/16 20:30
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AliOss {
    private Long id;
    private String path;
    private String relativePath;
    private String lastModified;
}
