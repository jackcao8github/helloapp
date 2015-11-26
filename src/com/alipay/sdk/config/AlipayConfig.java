package com.alipay.sdk.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	public static final String ALIPAY_GATEWAY = "https://mapi.alipay.com/gateway.do?";

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String PARTNER = "2088021824323937";
	// 商户的私钥，pkcs8格式
	public static String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMiQf0Z5huwjWAAjOs1o9ae+O56mJCcRjYnAYRB5eYqZFvcckOc3xMqvJLh4nXgDkCLQ+HGly1DDq8SLOIvJCxQ2r47L03TLWYWRHoedScOMcKqnu8rt2fmOy5wV0JLIrIbzMKYyxGg7DyYr9W7SJjvRXHzCZfjm2NaC5QXRzaa1AgMBAAECgYAnkrM6MZJYl2SuqkTY8MO/tD5JQFjTyJfXvTipqn47xZcNbPp+VLekkkg5ibrwSiuvYvISYncqWWFT53BhzGo92gtlzI1R8UgpPaOOn+eyXrTfedEU4agcSRuUlDPiVVQts1RXLi4OPBjrqx2gIGyFPR7/pSJb2y3tQMlnMCPr5QJBAPaTSv0sn689/r28Oc6eD3uWgH8k3Z2wkW8hni/Z+kMX9WCUWu63YfpyGk4xl7AGbHh1w+aAVA4yRlFGZIuHHiMCQQDQOv5W1eaeWjwD7mKQzCpoGC+wK7rqBvXeX/m+wVmZ+zeuuNTtMfA3AjczuCzGm6Els1UtL16dQ0VTZpqXj7lHAkAeBw/xxvZnU+hq2Lo9dDg0d76MPUQTyQXc/74Q273DX+6M5WQxf/mZkz2q2hU75XxeKxFBR810sSCpEyZ5a2n7AkA/UTD1hqVRkW6eY2ZlUS+z52bY0vNLJkKtE9IFRxXP1Kvh3SIh3gWFNI9EksRKwBgINre3vaevej0iVxi67WERAkEAtpXLeRLfQXtrsBZsx+fFWJzQ+BkH41JGxnn4G200EQfYlRzTD2oFkPU1IGIQe09PMMzonchlVIgieUw4KW5jrA==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// 商户收款账号
	public static final String SELLER = "huangjihui41@163.com";
	public static final String SELLER_NAME = "南京尊尚网络科技有限公司";
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	// public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 批量转帐签名方式
	public static String batch_trans_sign_type = "MD5";
	// 商户提供的支付宝批量转帐处理结果通知页面
	public static String batch_trans_notify_url = "http://112.124.114.120:8080/PinBa/alipayNotifyAction";
	// 商户提供的支付宝批量转帐处理结果通知页面
	public static String pay_notify_url = "";
	// 向平台支付时的签名方式
	public static String pay_sign_type = "utf-8";
	
}
