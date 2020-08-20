package com.test.controller;

import cn.hutool.core.util.StrUtil;
import com.how2j.trend.pojo.Index;
import com.how2j.trend.pojo.IndexData;
import com.how2j.trend.pojo.Profit;
import com.test.service.BackTestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.DateFormatter;
import java.lang.reflect.Parameter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BackTestController {

    @Autowired
    private BackTestService service;

    @GetMapping("/simulate/{code}/{startDate}/{endDate}")
    @CrossOrigin
    public Map<String, Object> getIndexData(@PathVariable("code") String code,
            @PathVariable("startDate") String startDate,    @PathVariable("endDate") String endDate){
        List<IndexData> list =  service.getIndexData(code);
        list = filter(list, startDate, endDate);

        String indexStartDate = list.get(0).getDate();
        String indexEndDate = list.get(list.size()-1).getDate();

        int ma = 20;
        float sellRate = 0.95f;
        float buyRate = 1.05f;
        float serviceCharge = 0f;
        Map<String,?>  stringMap  = service.simulate(ma, sellRate, buyRate, serviceCharge,list );
        List<Profit> profts = (List<Profit>) stringMap.get("profits");
        float year = service.getYear(list);
        IndexData start = list.get(0);
        IndexData end = list.get(list.size()-1);
        float indexIncomeTotal = (end.getClosePoint() - start.getClosePoint())/start.getClosePoint();
        float indexIncomeAnnual = (float) (Math.pow(end.getClosePoint()/start.getClosePoint(),1/year) -1);
        float trendIncomeTotal = (profts.get(profts.size()-1).getValue() - profts.get(0).getValue())/profts.get(0).getValue();
        float trendIncomeAnnual = (float) Math.pow(1+trendIncomeTotal, 1/year) - 1;

        int winCount = (Integer) stringMap.get("winCount");
        int lossCount = (Integer) stringMap.get("lossCount");
        float avgWinRate = (Float) stringMap.get("avgWinRate");
        float avgLossRate = (Float) stringMap.get("avgLossRate");


        HashMap<String, Object> map = new HashMap();
        map.put("indexDatas", list);
        map.put("indexStartDate", indexStartDate);
        map.put("indexEndDate", indexEndDate);
        map.put("profits",stringMap.get("profits"));
        map.put("trades",stringMap.get("trades"));
        map.put("years", year);
        map.put("indexIncomeTotal", indexIncomeTotal);
        map.put("indexIncomeAnnual", indexIncomeAnnual);
        map.put("trendIncomeTotal", trendIncomeTotal);
        map.put("trendIncomeAnnual", trendIncomeAnnual);

        map.put("winCount", winCount);
        map.put("lossCount", lossCount);
        map.put("avgWinRate", avgWinRate);
        map.put("avgLossRate", avgLossRate);
        return map;
    }

    private List<IndexData> filter(List<IndexData> list, String startDate, String endDate){
        if(StrUtil.isBlankOrUndefined(startDate) || StrUtil.isBlankOrUndefined(endDate) ){
            return list;
        }
        LocalDate localStartDate = LocalDate.parse(startDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate localEndDate = LocalDate.parse(endDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<IndexData> result = list.stream().filter(x -> LocalDate.parse(x.getDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(localStartDate) >= 0)
                .filter(x -> LocalDate.parse(x.getDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(localEndDate) <= 0).collect(Collectors.toList());
        return result;
    }
}
