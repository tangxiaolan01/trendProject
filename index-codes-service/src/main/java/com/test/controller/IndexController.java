package com.test.controller;

import com.how2j.trend.pojo.Index;
import com.test.configuration.IpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.test.service.IndexService;

import java.util.List;

@RestController
public class IndexController {
    @Autowired
    IndexService indexService;

    @Autowired
    IpConfiguration ipConfiguration;

    // @CrossOrigin 表示允许跨域，因为后续的回测视图是另一个端口号的，访问这个服务是属于跨域了。
    @GetMapping("/codes")
    @CrossOrigin
    public List<Index> codes(){
        System.out.println("current instance's port is" + ipConfiguration.getPort());
        return indexService.get();
    }
    }
