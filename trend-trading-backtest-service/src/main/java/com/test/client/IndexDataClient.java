package com.test.client;

import com.how2j.trend.pojo.IndexData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Primary
@FeignClient(value = "INDEX-DATA" )
public interface IndexDataClient {

    @GetMapping("/data/{code}")
    public List<IndexData> getIndexData(@PathVariable("code") String code);
}
