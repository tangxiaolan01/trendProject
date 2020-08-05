package com.test.service;

import cn.hutool.core.collection.CollectionUtil;
import com.how2j.trend.pojo.IndexData;
import com.test.client.IndexDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackTestService {

    @Autowired
    private IndexDataClient client;

    public List<IndexData> getIndexData(String code){
        List<IndexData>  result = client.getIndexData(code);
        CollectionUtil.reverse(result);
        result.stream().forEach(x ->System.out.println(x.getDate()));
        return result;
    }
}
