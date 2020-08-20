package com.test.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.how2j.trend.pojo.IndexData;
import com.how2j.trend.pojo.Profit;
import com.how2j.trend.pojo.Trade;
import com.test.client.IndexDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
    public Map<String,Object> simulate(int ma, float sellRate, float buyRate, float serviceCharge,
                                       List<IndexData> indexDatas){
        List<Profit> Profits = new ArrayList<>();
        List<Trade> trades = new ArrayList<>();

        float initCash = 1000;
        float cash = initCash;
        float share = 0;
        float value = 0;
        float init = 0;

        int winCount = 0;
        float totalWinRate = 0;
        float avgWinRate = 0;
        float totalLossRate = 0;
        int lossCount = 0;
        float avgLossRate = 0;
        if(indexDatas.size() > 0){
            init = indexDatas.get(0).getClosePoint();
        }

        for(int i = 0; i < indexDatas.size(); i++) {
            float avg = getMA(i, 20, indexDatas);
            float max = getMax(i, 20, indexDatas);
            float closePoint = indexDatas.get(i).getClosePoint();
            float increaseRate = closePoint / avg;
            float decreaseRate = closePoint / max;
            if (avg != 0) {
                if (increaseRate > buyRate) {
                    if (share == 0) {
                        share = cash / closePoint;
                        cash = 0;

                        Trade trade = new Trade();
                        trade.setBuyDate(indexDatas.get(i).getDate());
                        trade.setBuyClosePoint(indexDatas.get(i).getClosePoint());
                        trade.setSellClosePoint(0);
                        trade.setSellDate("n/a");
                        trades.add(trade);
                    }
                } else if (decreaseRate < sellRate) {
                    if (cash == 0) {
                        cash = share * closePoint * (1 - serviceCharge);
                        share = 0;

                       Trade trade = trades.get(trades.size()-1);
                       trade.setSellDate(indexDatas.get(i).getDate());
                       trade.setSellClosePoint(indexDatas.get(i).getClosePoint());
                       trade.setRate(cash/initCash);

                       if(trade.getSellClosePoint() - trade.getBuyClosePoint() >0){
                           totalWinRate += (trade.getSellClosePoint() - trade.getBuyClosePoint())/trade.getBuyClosePoint();
                           winCount++;
                       }else {
                           totalLossRate += (trade.getSellClosePoint() - trade.getBuyClosePoint())/trade.getBuyClosePoint();
                           lossCount++;
                       }
                    }
                }
            }
                if (share == 0) {
                    value = cash;
                } else {
                    value = share * closePoint;
                }




                float rate = value / initCash;
                Profit p = new Profit();
                p.setDate(indexDatas.get(i).getDate());
                p.setValue(rate * init);
                Profits.add(p);
        }
        Profits.stream().forEach(x -> System.out.println(x));
        avgLossRate = totalLossRate/lossCount;
        avgWinRate = totalWinRate/winCount;
        Map<String,Object> map = new HashMap<>();
        map.put("profits", Profits);
        map.put("trades",trades);

        map.put("winCount", winCount);
        map.put("lossCount", lossCount);
        map.put("avgWinRate", avgWinRate);
        map.put("avgLossRate", avgLossRate);

        return map;
    }

    private static float getMax(int i, int day, List<IndexData> list){
        int start = i-1-day;
        int end = i-1;
        if(start <0){
            start = 0;
        }
        float max = 0;
        if(start < 0){
            return  0;
        }

        for(int j = start; j < end; j++){
            if(max < list.get(j).getClosePoint()){
                max = list.get(j).getClosePoint();
            }
        }
        return max;
    }
    private static float getMA(int i, int ma, List<IndexData> list){
        int start = i-1-ma;
        int end = i-1;
        float sum = 0;
        float avg = 0;

        if(start < 0){
            return 0;
        }
        for(int j = start; j < end; j++){
            sum += list.get(j).getClosePoint();
        }
        avg = sum/ma;
        return avg;
    }

    public float getYear(List<IndexData> list){
        Date startDate = DateUtil.parse(list.get(0).getDate());
        Date endDate = DateUtil.parse(list.get(list.size()-1).getDate());

        return DateUtil.between(startDate,endDate,DateUnit.DAY)/365f;

    }
}
