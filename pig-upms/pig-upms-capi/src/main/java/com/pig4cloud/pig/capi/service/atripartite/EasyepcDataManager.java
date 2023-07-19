package com.pig4cloud.pig.capi.service.atripartite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.capi.entity.BizVinParsing;
import com.pig4cloud.pig.capi.service.BizVinParsingService;
import com.pig4cloud.pig.common.core.util.UUidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 精友数据查询
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EasyepcDataManager {


	private static final String REQUEST_URL = "http://www.easyepc.com/vf/api/500020";

	private static final String USERNAME = "2fb699z38wy0";

	private static final String PASSWORD = "11fa02da21be56a43b91f25034d378d5";

	private final BizVinParsingService bizVinParsingService;


	public String getRequestParams(Map<String, String> head, Map<String, String> body) {
		JSONObject _head = new JSONObject();
		JSONObject _body = new JSONObject();
		_head.put("uacId", head.get("uacId"));//账号
		_head.put("password", head.get("password"));//密码

		for (String key : body.keySet()) {
			_body.put(key, body.get(key));
		}

		_head.put("data", _body);
		return _head.toString();
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0 || s.trim().length() == 0;
	}

	/**
	 * HTTP_POST 请求
	 *
	 * @param requestUrl 请求地址
	 * @param jsonStr    请求参数JSON字符创
	 * @return
	 */
	public static String sendPostRequest(String requestUrl, String jsonStr) {
		String result = "";
		if (isEmpty(requestUrl)) {
			System.out.println("参数requestUrl(请求地址)为空!");
			return "请求地址为空!";
		}
		if (isEmpty(jsonStr)) {
			System.out.println("参数jsonStr(请求参数)为空!");
			return "请求参数为空!";
		}
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");//提交模式
			//是否允许输入输出
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//是否允许缓存
			conn.setUseCaches(false);
			//设置超时时间（毫秒）
			conn.setConnectTimeout(30000);
			//设置请求头信息
			conn.setRequestProperty("Content-Type", "application/json");
			//连接地址
			conn.connect();
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			//发送参数
			writer.write(jsonStr);
			//清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
			writer.flush();
			//关闭输出流
			writer.close();
			//读取返回结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			//读取返回结果
			String lines = reader.readLine();
			//关闭流
			reader.close();
			JSONObject jsonObject = JSONObject.parseObject(lines);
			result = jsonObject.toString();
		} catch (MalformedURLException e) {
			System.out.println("请求失败,信息为：" + e.getMessage());
		} catch (IOException e) {
			System.out.println("请求失败,信息为：" + e.getMessage());
		}
		return result;
	}

	public String getSaleVinInfo(String vin) {
		//请求头信息
		Map<String, String> head = new HashMap<String, String>();
		head.put("uacId", USERNAME);
		head.put("password", PASSWORD);
		//请求参数
		Map<String, String> body = new HashMap<String, String>();
		body.put("vin", vin);

		String requestParams = getRequestParams(head, body);
		String data = "";
		if (!isEmpty(requestParams)) {
			log.info("请求URL---requestUrl:" + REQUEST_URL);
			log.info("请求参数---requestParams:" + requestParams);
			data = sendPostRequest(REQUEST_URL, requestParams);
		}
		log.info("精友数据vin解析原始数据：" + data);
		//解析数据
		JSONObject carBrandObject = JSONObject.parseObject(data);
		String code = carBrandObject.getString("code");
		if (code.equals("000000")) {
			//精友数据异步保存
//            CompletableFuture.runAsync(()-> this.saveEasyepcDataToMongo(body));
			JSONArray result = carBrandObject.getJSONArray("result");
			if (Objects.nonNull(result)) {
				/**
				 * TODO  这里的解析要从新更新；并存入解析结果记录中去
				 * {
				 * 	"result": [
				 *         {
				 * 		"amVehicleId": "45E1A59E044EFA01E050A8C077507226",
				 * 		"bodyType": "三厢车",
				 * 		"factoryCode": "AUTO0128",
				 * 		"cnHorsePower": "95.69",
				 * 		"cnPower": "70",
				 * 		"amMainBrandName": "大众",
				 * 		"amBrandName": "上汽大众",
				 * 		"amBrandId": "4028d00c57bd37430157dbd1df9a01ca",
				 * 		"purchasePrice": "70800",
				 * 		"seats": "5",
				 * 		"countriesName": "德国",
				 * 		"vehCateTwoNames": "中型轿车(B)",
				 * 		"absFlag": "有",
				 * 		"price": "76800",
				 * 		"importFlag": "合资",
				 * 		"publicationNos": "SVW7180LED",
				 * 		"amMainBrandId": "4028d00c57045e630157b2a2c2f9013a",
				 * 		"displacement": "1.8",
				 * 		"marketDate": "2007-02-25",
				 * 		"frontTyreSize": "195/60 R14",
				 * 		"arrayType": "L",
				 * 		"amVinYear": "2009",
				 * 		"amGroupName": "(一代)桑塔纳 三厢(83.04-12.12)",
				 * 		"factoryName": "上汽大众",
				 * 		"amGroupId": "4028d00c5af5e2e6015b1937f05e02ea",
				 * 		"vehCateOneNames": "轿车",
				 * 		"uploadDate": "2009-12-19",
				 * 		"airIntakeType": "自然吸气",
				 * 		"gearboxType": "手动档",
				 * 		"fullWeight": "1100",
				 * 		"amYear": "2007",
				 * 		"bigGroupGeneration": "一代",
				 * 		"engineDesc": "1.8L",
				 * 		"powerType": "汽油",
				 * 		"fuelJetType": "多点电喷",
				 * 		"wheelBase": "2548",
				 * 		"cnDisplacement": "1781",
				 * 		"rearTyreSize": "195/60 R14",
				 * 		"amVehicleName": "2007款 经典 1.8L 手动 景畅型",
				 * 		"drivenType": "前置前驱",
				 * 		"doorNum": "四门",
				 * 		"valveNum": "2",
				 * 		"vehicleSize": "4546*1710*1427",
				 * 		"power": "70",
				 * 		"trackFront": "1414",
				 * 		"roz": "90号(京89号)",
				 * 		"vinSource": "实码解析",
				 * 		"vehicleColor": "深黑",
				 * 		"engineModel": "BSA",
				 * 		"amSeriesName": "桑塔纳",
				 * 		"effluentStandard": "国III",
				 * 		"trackRear": "1422",
				 * 		"gearNum": "5",
				 * 		"cfgLevel": "景畅型",
				 * 		"vehCateNames": "乘用车",
				 * 		"amSeriesId": "4028d00c57bd37430157dbf23c7601ed",
				 * 		"stopDate": "2012-12-16"
				 *        }],
				 * 	"code": "000000",
				 * 	"message": "成功"
				 * }
				 *
				 */
				if (CollectionUtils.isNotEmpty(result)) {
					JSONObject o = result.getJSONObject(0);
					String amBrandId = o.getString("amBrandId");
					String amMainBrandName = o.getString("amMainBrandName");
					String amBrandName = o.getString("amBrandName");
					log.info("精友数据解析数据：" + amBrandId + "+" + amBrandName);
					BizVinParsing bizVinParsing = BizVinParsing.builder()
							.id(UUidUtils.uuLongId())
							.vinCode(vin)
							.brand(amMainBrandName)
							.subBrand(amBrandName)
							.content(JSON.toJSONString(o))
							.build();
					bizVinParsing.setCreateBy("api管理员");
					bizVinParsing.setCreateBy("api管理员");
					bizVinParsingService.save(bizVinParsing);
					return amMainBrandName;
				}
			}
		}

		log.info("精友数据解析数据失败！");
		return null;
	}


}
