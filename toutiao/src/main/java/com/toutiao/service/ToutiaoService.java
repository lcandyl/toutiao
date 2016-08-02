package com.toutiao.service;

import org.springframework.stereotype.Service;

/**
 * Created by admin on 16-7-2.
 */

// 有了@Service 系统自动创建对象
@Service
public class ToutiaoService {
    public String say(){
        return "This is from ToutiaoService";
    }
}
