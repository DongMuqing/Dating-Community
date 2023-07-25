package com.susu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/7/15 20:43
 * @Created by Muqing
 */
@Transactional
@Service
public interface AmapService {
   String getAddress();
   String getWeather(String province);
 }
