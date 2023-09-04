package com.susu.entity;

import lombok.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/9/3 17:35
 * @Created by Muqing
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Paging<E> {
    //      当前页数  实体的数据
    HashMap<Long,List<E>> data;
    //总页数
    Long totalPages;
    //总数据条数
    Long size;
}
