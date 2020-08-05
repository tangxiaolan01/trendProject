package com.how2j.trend.job;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.how2j.trend.pojo.Index;
import com.how2j.trend.service.IndexDataService;
import com.how2j.trend.service.IndexService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class IndexDataSyncJob extends QuartzJobBean {

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDataService indexDataService;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时启动" + DateUtil.now());
        List<Index> indices = indexService.fresh();
        for(Index index : indices){
            indexDataService.fresh(index.getCode());
        }
        System.out.println("定时结束" + DateUtil.now());
    }
}
