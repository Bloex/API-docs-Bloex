# API说明书

# 一、开通接口

## 申请授权

请登录交易平台，进入个人中心，开通API接入功能，获得API-Key和Secret-Key。

API-Key：用户身份标识。
Secret-Key：用户签名密钥。

**特别提示： 请勿泄露API-Key和Secret-Key。**

## 参数签名

在调用敏感接口时，必须传入API-Key，并使用Secret-Key对参数（signature除外）进行签名。

待签名的参数：
```
"apiKey=8117490ede734a5eab6a3bf151cb83c6"、"symbol=ETHBTC"、"price=0.056"、"amount=10"、"type=1"
```
首先，生成待签名字符串，把参数按英文字母顺序排列：
```
"amount=10&apiKey=8117490ede734a5eab6a3bf151cb83c6&price=0.056&symbol=ETHBTC&type=1"
```
然后，将待签名字符串作为签名内容,Secret-Key作为签名密钥，利用HmacSHA256算法获得签名摘要内容（全小写）：
```
"signature=3cafe40f92be6ac77d2792b4b267c2da11e3f3087b93bb19c6c5133786984b44"
```

最后，将签名添加到原始参数的末尾处：
```
"amount=10&apiKey=8117490ede734a5eab6a3bf151cb83c6&price=0.056&symbol=ETHBTC&type=1&signature=3cafe40f92be6ac77d2792b4b267c2da11e3f3087b93bb19c6c5133786984b44"
```

# 二、交易接口

- 通讯协议： ` https  `
- 请求方式： ` post  `
- 返回结果： ` JSON` 
- 接口地址： ` api.bloex.com  `

|返回结果属性|类型|说明|
|:----    |:---|:----- |
|statusCode | int |返回的状态码：200=成功，其他=失败，请参考各接口的返回状态码|
|data |可变|返回的数据：请参考各接口的返回数据结构|

- 返回结果示例
```json
{
	"statusCode":200,
	"data": {
	    "l": 650.01,
	    "h": 900.06,
	    "o": 700.03,    
	}
}
```

- 状态码

|状态码|说明|
|:-----  |:-----  |
|200 |成功|
|101 |失败：交易暂未开启  |
|102 |失败：可用保证金不足或可用资产不足  |
|103 |失败：杠杆类型与已有委托单和持仓不一致  |
|104 |失败：买入持仓时，委托价比爆仓价低  |
|105 |失败：卖出持仓时，委托价比爆仓价高  |
|106 |失败：持仓不存在  |
|107 |失败：已经存在平仓单|
|108 |失败：做多时，预估爆仓价比指数高|
|109 |失败：做空时，预估爆仓价比指数低|
|201 |失败：没有对应数据|
|401 |失败：API 密钥或API 令牌不存在 |
|402 |失败：请求时间与服务器时间误差过大 |
|429 |失败：请求过于频繁 |
|500 |失败：参数错误  |
|501 |失败：缺少参数  |

- 示例代码： ` JavaScript `

```javascript
//获取行情
function getTicker(type) {
    //请求地址
    var url = "https://api.bloex.com/ticker/get";
    //提交参数
    var parameters = {
        symbol: type
    };
    //提交请求
    ajax.post(url, parameters, function (result) {
        if (result.statusCode == 200) {
            //成功：显示行情数据
            showTicker(result.data);
        } else {
            //失败：显示错误信息（根据状态码确定失败原因）
            showEorror(result.statusCode);
        }
    });
}
```

## 1、获取行情

- 请求地址： ` /ticker/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|symbol |是  |string |交易标记符|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----|
|a|卖一价|
|b|买一价|
|l |最低成交价  |
|h |最高成交价  |
|o |24h前成交价  |
|c |最新成交价  |
|v |24h成交量  |
|t |时间戳  |

- 返回数据示例

```json
{
    "a": 800.02,
    "b": 800.01,
    "l": 650.01,
    "h": 900.06,
    "o": 700.03,
    "c": 800.05,
    "v": 9999.99,
    "t": 1520315402211
}
```

- 交易标记符

-- 币币交易标记符

|BTC交易区|最小委托价格|价格小数位限制|单次最小委托数量|委托数量小数位限制|
|:----    |:---|:---|:---|:---|
|ETHBTC|0.000001|6位小数|0.001|3位小数|
|BCHBTC|0.000001|6位小数|0.001|3位小数|
|LTCBTC|0.000001|6位小数|0.01|2位小数|
|ETCBTC|0.000001|6位小数|0.1|2位小数|
|NULSBTC|0.0000001|7位小数|1|2位小数|

|USDT交易区|最小委托价格|价格小数位限制|单次最小委托数量|委托数量小数位限制|
|:---|:---|:---|:---|:---|
|BTCUSDT|0.01|2位小数|0.0001|4位小数|
|ETHUSDT|0.01|2位小数|0.001|3位小数|
|BCHUSDT|0.01|2位小数|0.001|3位小数|
|LTCUSDT|0.01|2位小数|0.01|2位小数|
|ETCUSDT|0.01|2位小数|0.1|2位小数|
|NULSUSDT|0.001|3位小数|1|2位小数|


-- 合约交易标记符

|合约交易|最小委托价格|价格小数位限制|单次最小委托数量|委托数量小数位限制|
|:----    |:---|:---|:---|:---|
|BTCUSDT_C|0.01|2位小数|0.001|3位小数|
|ETHUSDT_C|0.001|3位小数|0.05|2位小数|
|BCHUSDT_C|0.001|3位小数|0.01|2位小数|
|FTUSDT_C|0.000001|6位小数|100|0位小数|
|EOSUSDT_C|0.001|3位小数|1.0|1位小数|
|BNBUSDT_C|0.0001|4位小数|1.0|1位小数|
|NULSUSDT_C|0.0001|4位小数|10.0|1位小数|
|XRPUSDT_C|0.0001|4位小数|30|0位小数|
|HTUSDT_C|0.0001|4位小数|5.0|1位小数|

- **时间参数 time与timeError**

-- 请求时间若与服务器时间相差过大（默认10秒，用户可自定义误差值），出于安全考虑，服务器将拒绝该请求，并返回402错误代码

## 2、获取深度

- 请求地址： ` /trade/depth/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|symbol |是  |string |交易标记符|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:----- |
|asks |卖方深度[价格，数量]  |
|bids |买方深度[价格，数量]  |

- 返回数据示例

```json
{
    "asks":[
		[411.8,6],
		[411.75,11],
		[411.6,22],
		[411.5,9],
		[411.3,16]
	],
	"bids":[
		[410.65,12],
		[410.64,3],
		[410.19,15],
		[410.18,40],
		[410.09,10]
	]
}
```

## 3、获取交易记录

- 请求地址：` /trade/record/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|symbol |是  |string |交易标记符|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

[交易记录1，交易记录2，...,交易记录n]

- 交易记录对象

|属性|说明|
|:-----  |:-----  |
|amount |交易数量  |
|price |交易价格  |
|type|交易类型（1：买，2：卖）|
|date|交易时间戳|

- 返回数据示例

```json
[
	{
		"amount":11,
		"price":7.076,
		"type":1,
		"date":140807646000
	},
	{
		"amount":100,
		"price":7.076,
		"type":2,
		"date":1408076464000
	}
]
```

## 4、获取美元人民币汇率

- 请求地址：` /currency/exchange/rate/get `
- 提交参数：

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----|
|rate |美元-人民币汇率  |

- 返回数据示例

```json
{
	"rate": 6.67
}
```


## 5、获取币币兑换账户

- 请求地址：` /token/account/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|available |可用资产  |
|frozen |冻结资产  |

- 返回数据示例

```json
{
    "BTC": {
      "available": 790.12,
      "frozen": 860.89
    },
    "ETH": {
      "available": 990.12,
      "frozen": 660.89
	}
}
```

## 6、获取单个订单

- 请求地址：` /order/get/by-id `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|orderId |是  |long |订单ID|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|id |订单ID  |
|create_date |委托时间  |
|symbol |交易标记符  |
|total_amount |委托数量  |
|deal_amount |成交数量  |
|price |委托价格  |
|avg_price |平均成交价格  |
|fee |手续费  |
|blj_fee |BLJ手续费  |
|type |订单类型 （1：买入 2：卖出）  |
|status |订单状态(1全部成交，2部分成交，3未成交，4撤单)  |
|lever_rate |杠杆倍数（2, 5，10，15，20），合约交易专属   |

- 返回数据示例

```json
        {
            "id":111,
            "create_date":1408076414000,
	    "symbol":"BTCUSDT",
	    "total_amount":10,
            "deal_amount":1,
	    "price":1111,
            "avg_price":0,
            "fee":0,
	    "blj_fee":0,
            "type":1,
            "status":"0",
            "lever_rate":"10"
        }
```

## 7、获取订单列表（待成交订单）

- 请求地址：` /order/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|pageNum|否|int|当前页，默认为第1页|
|pageSize|否|int|每页条数，默认10条，最大为200条|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|id |订单ID  |
|create_date |委托时间  |
|symbol |交易标记符  |
|total_amount |委托数量  |
|deal_amount |成交数量  |
|price |委托价格  |
|avg_price |平均成交价格  |
|fee |手续费  |
|blj_fee |BLJ手续费  |
|type |订单类型 （1：买入 2：卖出）  |
|status |订单状态(1全部成交，2部分成交，3未成交，4撤单)  |
|lever_rate |杠杆倍数（2, 5，10，15，20），合约交易专属   |

- 返回数据示例

```json
[
        {
            "id":111,
            "create_date":1408076414000,
            "symbol":"BTCUSDT",
            "total_amount":10,
            "deal_amount":1,
            "price":1111,
            "avg_price":0,
            "fee":0,
            "blj_fee":0,
            "type":1,
            "status":"0",
            "lever_rate":"10"
        },
		{
            "id":112,
            "create_date":1408076414000,
            "symbol":"BTCUSDT",
            "total_amount":10,
            "deal_amount":1,
            "price":1111,
            "avg_price":0,
            "fee":0,
            "blj_fee":0,
            "type":1,
            "status":"0",
            "lever_rate":"10"
        }
 ]
```

## 8、提交订单

- 请求地址：` /order/submit `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|price|否|double|委托价格，不传则为市价|
|amount|是|double|委托数量|
|type|是|int|订单类型（1：买入 2：卖出）|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|
|lever|否|string|杠杆倍数（2，5，10，20，25），合约交易必选|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|orderId |订单ID  |

- 返回数据示例

```json
  {
    "orderId":46485184845646
  }
```

## 9、取消订单

- 请求地址：` /order/cancel `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|orderId|是|long|订单ID|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|successId |成功订单ID  |

- 返回数据示例

```json
{
    "successId":46485184845646
}
```

## 10、批量取消订单

- 请求地址：`/order/cancel/by-symbol `
- 提交参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|code |执行结果，200为成功  |

- 返回数据示例

``` json
  {
    "code":200
  }
```

## 11、获取成交记录列表

- 请求地址：` /deal/record/get `
- 提交参数 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API 密匙|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|pageNum|否|int|当前页，默认为第1页|
|pageSize|否|int|每页条数，默认10条，最大为200条|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回参数说明

|参数名|说明|
|:-----  |:-----                           |
|id |订单ID  |
|create_date |委托时间  |
|symbol |交易类型名称  |
|deal_amount |成交数量  |
|price |成交价格  |
|fee |手续费  |
|blj_fee |BLJ手续费  |
|type |订单类型 （1：买入 2：卖出）  |
|order_id |对应订单ID  |

 **返回示例**

``` json
  [
        {
            "id":123,
            "create_date":1408076414000,
	    "symbol":"BTCUSDT",
	    "total_amount":10,
            "deal_amount":1,
	    "price":1111,
            "avg_price":0,
            "fee":0,
	    "blj_fee":0,
            "type":1,
            "order_id":"321"
        },
		{
            "id":124,
            "create_date":1408076414000,
	    "symbol":"BTCUSDT",
	    "total_amount":10,
            "deal_amount":1,
	    "price":1111,
            "avg_price":0,
            "fee":0,
	    "blj_fee":0,
            "type":1,
            "order_id":"322"
        }
  ]
```


## 12、获取合约账户

- 请求地址：` /contract/account/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|rights |账户权益  |
|balance |账户余额  |
|unrealizedProfitLoss |未实现盈亏  |
|available |可用保证金  |
|orderMargin |委托保证金  |
|positionMargin |持仓保证金  |
|coins |合约各币种的资产数量  |

- 返回数据示例

```json
{
    "BTC": {
      "rights": "6200",
      "balance": "6000",
      "unrealizedProfitLoss": "200",
      "available": "5600",
      "orderMargin": "0",
      "positionMargin": "600",
      "coins": {
                "USDT":"100",
                "BTC":"1",
                "ETH":"0"
               }
    },
    "FT": {
      "rights": "6200",
      "balance": "6000",
      "unrealizedProfitLoss": "200",
      "available": "5600",
      "orderMargin": "0",
      "positionMargin": "600",
      "coins": {
                "USDT":"100",
		"FT":"1000",
                "BTC":"0.9",
                "ETH":"0"
              }
    }
}
```

## 13、获取合约仓位信息

- 请求地址：` /contract/position/get `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|symbol |交易类型  |
|type |方向(1：买入，2：卖出)  |
|margin |保证金  |
|amount |持仓数量  |
|entry_price |开仓均价  |
|create_date |创建日期  |
|lever_rate |杠杆倍数（2，5，10，20，25） |
|force_burst_price |预计爆仓价  |

- 返回数据示例

```json
{
    "symbol":"BTCUSDT_C",
    "type":"1",
    "margin":"600",
    "amount":"1",
    "entry_price":"6000",
    "create_date":"1536033600000",
    "lever_rate":"10",
    "force_burst_price":"5000"
}
```

## 14、提交合约平仓单

- 请求地址：` /contract/position/close `
- 提交参数

|参数|必选|类型|说明|
|:----    |:---|:----- |-----   |
|apiKey |是  |string |API-Key|
|signature |是  |string |API 签名|
|symbol |是  |string |交易标记符|
|price|否|double|平仓价格，不传则为市价|
|time|是|long|请求时间，13位毫秒时间|
|timeError|否|long|请求时间与服务器时间误差值，默认10秒|

- 返回数据对象

|属性|说明|
|:-----  |:-----   |
|orderId |订单ID  |

- 返回数据示例

```json
  {
    "orderId":46485184845646
  }
```

# 三、消息接口

- 通讯协议： ` wss  `
- 请求方式： ` WebSocket  `
- 返回结果： ` JSON` 
- 接口地址： ` message.bloex.com/ws  `
- 示例代码： ` JavaScript `

```javascript
var websocket;

if(window.WebSocket){                  
	websocket = new WebSocket("wss://message.bloex.com/ws");//与平台建立连接

    websocket.onmessage = function(event){ //已收到推送的消息
		var message = event.data;
        var messageList = document.getElementById("message-list");
        messageList.value = messageList.value + "\n" + message;
	}

    websocket.onopen = function(event){//已经连接到平台
        var subscribeRequest="{c:'s',p:[t:'ticker',l:'BTCETH_24h']}";//订阅消息
		websocket.send(subscribeRequest);
    }
}
```


## 1、订阅

客户端连接到平台，必须首先发送订阅请求，平台会按照订阅请求，主动推送消息。

- 订阅请求

```json
{
	"c":"s", //命令(command)
	"p":[    //参数(parameters)
		{
			"t":"ticker",            //主题(topic)：报价信息
			"l":"BTCETH_24h"         //限定(limit)：交易标记符_时间粒度			  
		},
		{
			"t":"trade",             //主题：交易信息
			"l":"BTCETH"             //限定：交易标记符				
		}
	]
}
```

- 交易标记符

-- 币币交易标记符

ETHBTC、BCHBTC、LTCBTC、ETCBTC、NULSBTC、BTCUSDT、ETHUSDT、BCHUSDT、LTCUSDT、ETCUSDT、NULSUSDT

-- 合约交易标记符

BTCUSDT_C、ETHUSDT_C、BCHUSDT_C、FTUSDT_C、EOSUSDT_C、BNBUSDT_C、NULSUSDT_C、XRPUSDT_C、HTUSDT_C

- 时间粒度

1m、5m、15m、30m、1h、2h、4h、6h、12h、24h、1w


- 心跳请求

```json
{	
	"c":"h"
}
```
- 心跳回复

```json
{
	"c":"r",
	"r":"h"
}
```

## 2、行情信息
``` 
{
    "e":"ticker",
    "s":"BTCUSDT",
    "d":{
        "l":"最低价",
        "h":"最高价",
        "o":"开盘价",
        "c":"关盘价",
        "v":"数量",
        "t":"时间"
    }
}
```

## 3、交易信息

平台接收到订阅请求，推送一次最近成交记录和最近订单，之后实时推送最新成交记录和最新订单。

- 最近成交记录

```json 
{
	"e":"AllTrade",
	"d": [{
            "t":"时间",
    	    "p":"价格",
    	    "v":"数量"
        },
	{
            "t":"时间",
            "p":"价格",
            "v":"数量"
        }]
}
```

- 最新成交记录

```json
{
	"e":"t",
	"d":{
		"t":"时间",
		"p":"价格",
		"v":"数量"
	}
}
```

- 最近订单

```json
{
	"e":"AllBid", //AllBid=买单，AllAsk=卖单
	"d":[{
    	"s":"类型", //1=买单，2=卖单
    	"p":"价格",
    	"v":"数量"
	},
	{
    	"s":"类型",
    	"p":"价格",
    	"v":"数量"
	}]
}
```
- 最新订单

```json 
{
	"e":"o",
	"d":{
    	"s":"类型",
    	"p":"价格",
    	"v":"数量"
	}
}
```

