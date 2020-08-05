package com.how2j.trend.controller;

import com.how2j.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.how2j.trend.pojo.Index;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private IndexService service;

    @GetMapping("/getCodes")
    public List<Index> getIndex(){
       return service.get();
    }

    @GetMapping("/removeCodes")
    public String remove() throws Exception{
        service.remove();
        return "remove codes successfully";
    }
    @GetMapping("/freshCodes")
    public List<Index> fresh(){
        return service.fresh();
    }
}
