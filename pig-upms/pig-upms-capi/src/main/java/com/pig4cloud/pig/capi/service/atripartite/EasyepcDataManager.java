package com.pig4cloud.pig.capi.service.atripartite;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 精友数据查询
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EasyepcDataManager {



    private static final String REQUEST_URL = "http://www.easyepc123.com/api/111002";

    private static final String USERNAME = "2fb699z38wy0";

    private static final String PASSWORD = "11fa02da21be56a43b91f25034d378d5";

	BizBuyerOrderService bizBuyerOrderService;


    public String getSaleVinInfo(String vin) {
        Map<String, Object> vinParam = new HashMap<>(16);
        vinParam.put("vinCode", vin);
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("uacId", USERNAME);
        requestParam.put("password", PASSWORD);
        requestParam.put("data", vinParam);

        String body = HttpRequest.post(REQUEST_URL)
                .body(JSON.toJSONString(requestParam))
                .contentType("application/json")
                .execute().body();
        log.info("精友数据vin解析原始数据：" + body);

        //解析数据
        JSONObject carBrandObject = JSONObject.parseObject(body);
        String code = carBrandObject.getString("code");
        if (code.equals("000000")){
            //精友数据异步保存
//            CompletableFuture.runAsync(()-> this.saveEasyepcDataToMongo(body));
            JSONObject result = carBrandObject.getJSONObject("result");
            if (Objects.nonNull(result)){
				/**
				 * TODO  这里的解析要从新更新；并存入解析结果记录中去
				 */
                JSONArray vehicleList = result.getJSONArray("vehicleList");
                if (CollectionUtils.isNotEmpty(vehicleList)){
                    JSONObject o = vehicleList.getJSONObject(0);
                    String prefix = o.getString("brandCode");
                    String subBrand = o.getString("brandName");
                    log.info("精友数据解析数据：" + prefix+ "+" +subBrand);
                    return subBrand;
                }
            }
        }

		log.info("精友数据解析数据失败！");
		return null;
    }


}
