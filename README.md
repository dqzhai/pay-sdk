# pay-sdk
this is a simple pay sdk with alipay and wechat-pay

## pay-jdk in jcenter
https://bintray.com/bintray/jcenter?filterByPkgName=sdk-release 

# now let's begin

## step1: 加入项目所需权限

```

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />

```

### step2: 引入SDK

```
dependencies {
       compile 'com.payelves:sdk-release:3.2.0'
}

```


### step3: 支付服务初始化

```

/**
     * 支付服务初始化
     * @param openId
     *      用户id(不能为空,区分大小写,数据来源:后台->设置->API接口信息->OPEN_ID)
     * @param token
     *      秘钥(不能为空,区分大小写,数据来源:后台->设置->API接口信息->TOKEN)
     * @param appKey
     *      appKey(不能为空,数据来源:后台->应用->该应用appKey)
     * @param channel
     *      channel(可为空)"baidu","xiaomi" ,"360"
     * @return
     */
EPay.getInstance(getApplicationContext()).init(String openId, String token,
                     String appKey, String channel);

 ```

 ### step5: 调用支付

```

/**
     * 发起支付
     *
     * @param subject       商品名称,不可为空和空字符串
     * @param body          商品内容,不可为空和空字符串
     * @param amount        支付金额，单位分，不能为null和<1
     * @param orderId       商户系统的订单号(如果有订单的概念),没有可为空
     * @param payUserId     商户系统的用户id(如果有用户的概念),没有可为空
     * @param backPara      支付成功后支付精灵会用此参数回调配置的url
     *					(回调url在后台应用->添加应用时候配置)
     * demo: backParas 的value(建议json) ： {"a":1,"b":"2"},如不需要可为空。
     * @param payResultListener，不能为null 支付结果回调
     */
EPay.getInstance(this).pay(subject, body, amount,
                        orderId, payUserId,backPara, new PayResultListener() {
    /**
	     * @param context
	     * @param orderId   商户系统订单id
	     * @param payUserId 商户系统用户ID
	     * @param payResult
	     * @param payType   支付类型:1 支付宝，2 微信 3 银联
	     * @param amount    支付金额
	     * @see EPayResult#FAIL_CODE
	     * @see EPayResult#SUCCESS_CODE
	     * 1支付成功，2支付失败
  	*/
     @Override
     public void onFinish(Context context, String orderId, String payUserId,
              EPayResult payResult , int payType, Integer amount) {
	EPay.getInstance(context).closePayView();//关闭快捷支付页面
	if(payResult.getCode() == EPayResult.SUCCESS_CODE.getCode()){
	    //支付成功逻辑处理
	    Toast.makeText(MainActivity.this, payResult.getMsg(), Toast.LENGTH_LONG).show();
	}else if(payResult.getCode() == EPayResult.FAIL_CODE.getCode()){
	    //支付失败逻辑处理
	    Toast.makeText(MainActivity.this, payResult.getMsg(), Toast.LENGTH_LONG).show();
	}
    }
});   


```

# 动态参数配置(可选)


```
/**
     * 获取动态配置（后台->配置列表）
     * @param key
     * @param itemConfigResultListener
     */
EPay.getInstance(getApplicationContext()).loadConfig(String key, 
	new ConfigResultListener() {
    @Override
    public void onSuccess(String value) {
        Log.e("e", value);
    }
});

```
