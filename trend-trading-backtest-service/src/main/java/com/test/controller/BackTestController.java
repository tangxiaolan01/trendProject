package com.test.controller;

import com.how2j.trend.pojo.Index;
import com.how2j.trend.pojo.IndexData;
import com.test.service.BackTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BackTestController {

    @Autowired
    private BackTestService service;

    @GetMapping("/simulate/{code}")
    @CrossOrigin
    public Map<String, List<IndexData>> getIndexData(@PathVariable("code") String code){
        List<IndexData> list =  service.getIndexData(code);
        HashMap<String, List<IndexData>> map = new HashMap();
        map.put("indexDatas", list);
        return map;
    }
}
