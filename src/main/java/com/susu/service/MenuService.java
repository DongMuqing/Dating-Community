package com.susu.service;

import com.susu.entity.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Date:2023/6/12 11:34
 * @Created by Muqing
 */
@Transactional
@Service
public interface MenuService {
    List<Menu> getAll();
}
