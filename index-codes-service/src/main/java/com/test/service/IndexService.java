package com.test.service;

import cn.hutool.core.collection.CollUtil;
import com.how2j.trend.pojo.Index;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "index")
public class IndexService {

    @Cacheable(key = "'all_codes'")
    public List<Index> get(){
        Index index = new Index();
        index.setCode("000000");
        index.setName("无效指数代码");
        return CollUtil.toList(index);
    }

}
