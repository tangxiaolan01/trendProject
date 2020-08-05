package com.test.controller;
import com.how2j.trend.pojo.IndexData;
import com.test.configuration.IpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.test.service.IndexDataService;

import java.util.List;

@RestController
public class indexDataController {
    @Autowired
    private IndexDataService service;

    @Autowired
    private IpConfiguration configuration;

    @GetMapping("/data/{code}")
    public List<IndexData> get(@PathVariable("code") String code){
        System.out.println("current instance is :" + configuration.getPort());
        List<IndexData> list = service.get(code);
        System.out.println("indexDataController size is " + list.size());
        return list;
    }
}
