package com.how2j.trend.controller;

import com.how2j.trend.pojo.Index;
import com.how2j.trend.pojo.IndexData;
import com.how2j.trend.service.IndexDataService;
import com.how2j.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexDataController {

    @Autowired
    private IndexDataService service;

    @GetMapping("/getIndexData/{code}")
    public List<IndexData> getIndex(@PathVariable("code") String code){
       return service.get(code);
    }

    @GetMapping("/removeIndexData/{code}")
    public String remove(@PathVariable("code") String code) throws Exception{
        service.remove(code);
        return "remove codes successfully";
    }
    @GetMapping("/freshIndexData/{code}")
    public List<IndexData> fresh(@PathVariable("code") String code){
        return service.fresh(code);
    }
}
