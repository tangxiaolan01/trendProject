package com.how2j.trend.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.how2j.trend.pojo.Index;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.how2j.trend.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "index")
public class IndexService {
    private List<Index> indexs;

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "thirdPartNotConnect")
    public List<Index> fresh(){
        indexs = fetchIndexes();
        try {
            IndexService indexService = SpringContextUtil.getBean(IndexService.class);
            indexService.remove();
            return indexService.store();
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }
    private List<Index> map2Index(List<Map> list){
        List<Index> result = new ArrayList<>();
        list.forEach(x->{
            Index index = new Index();
            index.setCode(x.get("code").toString());
            index.setName(x.get("name").toString());
            result.add(index);

        });

        return result;
    }

    public List<Index> thirdPartNotConnect(){
        System.out.println("thirdPardNotConnect");
        Index index = new Index();
        index.setCode("000000");
        index.setName("无效基金代码");
        return CollectionUtil.toList(index);
    }

    public List<Index> fetchIndexes(){
        List<Map> maps = restTemplate.getForObject("http://127.0.0.1:8090/indexes/codes.json",List.class);
        return  map2Index(maps);
    }

    //清空redis数据
    @CacheEvict(allEntries = true)
    public void remove(){
    }

    @Cacheable(key = "'all_codes'")
    public List<Index> get(){
        return CollUtil.toList();
    }

    @Cacheable(key = "'all_codes'")
    public List<Index> store(){
        System.out.println("store ====>" + this);
        return indexs;
    }


}
