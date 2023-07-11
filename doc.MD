<center><font face="黑体" size=6>Excar车辆业务查询接入文档</font></center>

## 业务说明

车辆维保查询业务流程简易说明：

1、商家请求平台车辆维保下单接口下单，传给平台VIN码和回调地址，获取到订单号等待平台回调。

2、平台请求商家的回调地址，返回下单的订单号与车辆的维保信息。

3、商家通过订单号匹配后保存维保信息。

## 接口说明
#### 一、账号申请

> 1、客户向我司申请开通账号，由我司提供接口调用的账号【app_key】和【secret_key】;
>
> 2、app_key与secret_key请用户保存妥当!!!!!!!!

#### 二、接口规则
> ##### 接口说明
> * 遵循统一的Restful的设计风格
>
> * 使用JSON作为数据交互的格式
>
> * 请求接口统一地址：http://47.98.142.48:4100
>
> * 请求方式：POST
> ##### 系统参数
> * head参数（鉴权参数）
    >   * app_key：分配给客户端的app_key（必填）
>   * secret_key：分配给客户端的secret_key（必填）
> * body参数
    >   * vin："车架号" (必填)
>   * callbackurl:回调地址(必填)
> ##### 系统参数示例
```
head = {
  "app_key": "xxxxxxxxxapp_keyxxxxxx",
  "secret_key": "xxxxxxsecret_keyxxxxxxx"
}

body  = {
  "vin": "",
  "callbackurl": "http://backUrl.com/callback"
}
```
>
> * 响应统一格式示例：
>
 ```json
  {
      "success": true,
      "message": "操作成功！",
      "code": 200,
      "result": {
          "code": 1,
          "data": {
              "vin": "xxxxx",
              "order_id": 1254403231581
          }
      }
  }
``````
> 请注意！！ order_id 是Long 类型！！本系统内的order_id传递都为Long类型。

#### 三、接口交互说明

##### 一、车辆维保下单接口
> ##### 接口说明
>
> 调用该接口创建维保订单,成功后会返回对应的订单号
>
> 查询成功后，推送车辆维保数据至请求参数中指定的接收地址。
>
> 查询失败后，推送错误信息至请求参数中指定的接收地址。
>
> <font face="黑体">请求URL：</font>/maintenance/order
>
> <font face="黑体">请求方式：</font>POST
>
> <font face="黑体">参数传递：</font>application/json
>
> ##### Head鉴权
> ```
> head = {
>  "app_key": "xxxxxxxxxapp_keyxxxxxx",
>  "secret_key": "xxxxxxsecret_keyxxxxxxx"
> }
> ```
>
> ##### 请求体body
> ```
> {
>  "vin": "LSK231321124S4N5",
>  "callbackurl": "http://backUrl.com/callback"
> }
> ```
> ##### 成功返回示例
>
> ```json
> {
>    "success": true,
>    "message": "操作成功！",
>    "code": 200,
>    "data": {
>        "order_code": 3,
>        "data": {
>             "vin": "查询车架号",
>             "order_id": "返回的订单id"
>         }
>    }
>}
> ```
> ##### 失败返回示例
>
> ```json
> {
>    "success": false,
>    "message": "此品牌暂不支持查询",
>    "code": 1001,
>    "data": {
>         "vin": "查询车架号",
>         "order_id": "返回的订单id"
>     }
>}
> ```
> ##### 订单状态码（data.order_code）
>
> * order_code = -100 ：下单失败
> * order_code =  100 ：下单中
> * order_code =  101 ：下单成功
> * order_code =  102 ：查询中
> * order_code =  103 ：回调成功
> * order_code =  104 ：回调失败
> * order_code =  105 ：查询无记录
> ##### 下单错误码（code）
>
> * code = 4001 ：此品牌暂不支持查询,请联系客服。
> * code = 4002 ：此vin该时间段不支持查询，请联系客服。
> * code = 4003 ：vin码无法解析识别。
> * code = 4004 : 下单失败，请联系客服。
> * code = 4005 ： 回调商家异常!
> * code = 4006 :  订单超时,已失效。

> ##### 系统异常编码（code）
> * code = 4200 :  未知错误
> * code = 4201 :  系统错误
> * code = 4244 :  缺少必填秘钥参数

二、订单回调说明
> ##### 回调说明
> 请按照下面的接口规范定义接收维保数据的接口供平台调用。成功创建维保订单号后，平台将推送车辆维保数据至该接口。
>
> <font face="黑体">请求URL：</font>车辆维保下单接口传入的callBackUrl参数
>
> <font face="黑体">请求方式：</font>POST
>
> <font face="黑体">业务参数：</font>
>
> | 参数名   | 变量          | 类型   | 必填 | 描述 |
> | :------- | :------------ | :----- | ---- | ---- |
> | 订单ID   | order_id      | String | 是   |      |
> | 维保数据 | maintain_data | Object | 是   |      |
>
> maintain_data数据
>
> | 参数名          | 变量           | 类型     | 必填 | 描述 |
> | :-------------- | :------------- | :------- | ---- | ---- |
> | VIN码（车架号） | vin            | String   | 是   |      |
> | 品牌名称        | brand          | String   | 是   |      |
> | 车型            | model          | String   | 是   |      |
> | 总里程数        | total_milage   | String   | 是   |      |
> | 记录最后时间    | last_date      | String   | 是   |      |
> | 维保详情        | details        | Object[] | 是   |      |
>
> details数据
>
> | 参数名       | 变量          | 类型     | 必填 | 描述             |
> | :----------- | :------------ | :------- | ---- | ---------------- |
> | 里程数       | milage        | String   | 是   |                  |
> | 维保时间     | repair_date   | String   | 是   |                  |
> | 维保内容拼接 | repair_labors | String   | 是   |                  |
> | 维保部件拼接 | repair_parts  | String   | 是   |                  |
> | 维保类型     | repair_type   | String   | 是   |                  |
> | 维保内容     | parts         | Object[] | 否   | 有些品牌不予提供 |
> | 维保部件     | labors        | Object[] | 否   | 有些品牌不予提供 |
>
> parts数据
>
> | 参数名       | 变量  | 类型   | 必填 | 描述 |
> | :----------- | :---- | :----- | ---- | ---- |
> | 维保部件名称 | name  | String | 是   |      |
> | 数量         | count | String | 是   |      |
>
> labors
>
> | 参数名   | 变量 | 类型   | 必填 | 描述 |
> | :------- | :--- | :----- | ---- | ---- |
> | 维保内容 | name | String | 是   |      |
> | 工时     | hour | String | 是   |      |
>
> ##### 成功返回示例：
>
> ```json
> {
>     "code": "0",
>     "data": {
>         "order_id": "12004271631035770330",
>         "maintain_data": {
>             "brand": "路虎",
>             "details": [
>                 {
>                     "milage": "20050",
>                     "repair_date": "2014-04-29",
>                     "repair_labors": "更换排挡杆;车辆健康检查（优惠减免0.25工时）;基础保养服务包(工时) 第十九次 路虎全新一代揽胜 3.0 SC 汽油",
>                     "repair_parts": "变速箱控制模�;高性能发动机强力保护剂;高性能发动机内部清洗剂;空气滤清器滤�;FILTER - ODOUR AND P;机油滤清器滤�;Edge Professional E;旅行毯",
>                     "repair_type": "一般维修;一般维修;常规保养",
>                     "labors": [
>                         {
>                             "hour": "0.0",
>                             "name": "更换滑阀箱"
>                         }
>                     ],
>                     "parts": [
>                         {
>                             "count": "1.0",
>                             "name": "机电单元"
>                         },
>                         {
>                             "count": "1.9",
>                             "name": "变速箱油"
>                         }
>                     ]
>                 },
>                 {
>                     "milage": "9837",
>                     "repair_date": "2013-12-09",
>                     "repair_labors": "",
>                     "repair_parts": "",
>                     "repair_type": "机油保养"
>                 },
>                 {
>                     "milage": "1104",
>                     "repair_date": "2013-09-25",
>                     "repair_labors": "",
>                     "repair_parts": "进行车辆测试;拆卸和安装或更新前部两个盘式制动器的制动 摩擦片;",
>                     "repair_type": "异响"
>                 },
>                 {
>                     "milage": "2",
>                     "repair_date": "2013-09-09",
>                     "repair_labors": "保修;",
>                     "repair_parts": "配置免匙车辆模块",
>                     "repair_type": "其他"
>                 }
>             ],
>             "last_date": "2020-04-27 16:31:09",
>             "model": "Range Rover 3.0 Litre V6 S/C Petrol Vouege",
>             "total_milage": "20050",
>             "vin": "SALGA2VF7EA154705"
>         }
>     }
> }
> ```
> ##### 回调错误码【注意：回调返回错误码也带有order_id返回】
>
> * code = 4201 ：系统错误
>
> * code = 4202 ：系统登录失败
>
> * code = 4203 ：查询记录失败
>
> * code = 4204 ：查询无记录
>
> ```json
>  {
>     "msg": "未知错误",
>     "code": "1200",
>     "data": {
>          "orderId": "12005071341185424468"
>      }
>  }
> ```
#### java示例
```
MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\"vin\": \"SALAN2V63EA730171\", \"call_back_url\": \"http://localhost:4100/test2\", \"brand\": \"\",\"engine_code\": \"\"}");
Request request = new Request.Builder()
   .url("http://47.98.142.48/:4100/maintenance/order")
   .method("POST", body)
   .addHeader("app_key", "bdb2eb1xxxXXXXX1a338e23f")
   .addHeader("secret_key", "ZbkPAvetXXXXXXXXXXXXXgGBJx/bs5mk=")
   .build();
Response response = client.newCall(request).execute();

```


