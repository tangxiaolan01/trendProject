package com.how2j.trend.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.how2j.trend.pojo.IndexData;
import com.how2j.trend.util.SpringContextUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "index_datas")
public class IndexDataService {

    private Map<String, List<IndexData>> indexDatas = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "thirdPartNotConnected")
    public List<IndexData> fresh(String code){
        List<IndexData> indexData = this.fetchIndexFromThirdPart(code);
        indexDatas.put(code, indexData);

        System.out.println("code :" + code);
        System.out.println("indexData :" + indexDatas.get(code).size());

        IndexDataService service = SpringContextUtil.getBean(IndexDataService.class);
        service.remove(code);
        return service.store(code);


    }
    @CacheEvict(key = "'indexData-code-'+ #p0")
    public void remove(String code){

    }

    @CachePut(key = "'indexData-code-'+ #p0")
    public List<IndexData> store(String code){
        return indexDatas.get(code);
    }

    @Cacheable(key = "'indexData-code-'+ #p0")
    public List<IndexData> get(String code){
        return CollUtil.toList();
    }
    public List<IndexData> fetchIndexFromThirdPart(String code){
        List<Map> mapList = restTemplate.getForObject("http://127.0.0.1:8090/indexes/"+code+".json",List.class);
        return this.map2List(mapList);

    }

    private List<IndexData> map2List( List<Map> mapList ){
        List<IndexData> result = new ArrayList<>();
        mapList.forEach(x ->{
            String date = x.get("date").toString();
            float point = Convert.toFloat(x.get("closePoint"));
            IndexData data = new IndexData();
            data.setClosePoint(point);
            data.setDate(date);
            result.add(data);

        });
        return result;
    }

    public List<IndexData> thirdPartNotConnected(String code){
        System.out.println("third_part_not_connected()");
        IndexData index= new IndexData();
        index.setClosePoint(0);
        index.setDate("n/a");
        return CollectionUtil.toList(index);
    }

}
