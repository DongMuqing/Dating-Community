package com.susu.damian;

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
    HashMap<Integer,List<E>> data;
    //总页数
    Integer totalPages;
    //总数据条数
    Integer size;
}
