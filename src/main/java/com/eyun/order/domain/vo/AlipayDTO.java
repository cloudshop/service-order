package com.eyun.order.domain.vo;

public class AlipayDTO {

	/**
	 * 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
	 */
	private String body;

	/**
	 * 商户传入业务信息，具体值要和支付宝约定，应用于安全，营销等参数直传场景，格式为json格式
	 */
	private String businessParams;

	/**
	 * 禁用渠道，用户不可用指定渠道支付 当有多个渠道时用“,”分隔 注，与enable_pay_channels互斥
	 */
	private String disablePayChannels;

	/**
	 * 可用渠道，用户只能在指定渠道范围内支付 当有多个渠道时用“,”分隔 注，与disable_pay_channels互斥
	 */
	private String enablePayChannels;

	/**
	 * 外部指定买家
	 */

	/**
	 * 业务扩展参数
	 */

	/**
	 * 商品主类型 :0-虚拟类商品,1-实物类商品
	 */
	private String goodsType;

	/**
	 * 开票信息
	 */

	/**
	 * 商户网站唯一订单号
	 */
	private String outTradeNo;

	/**
	 * 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝只会在同步返回（包括跳转回商户网站）和异步通知时将该参数原样返回。
	 * 本参数必须进行UrlEncode之后才可以发送给支付宝。
	 */
	private String passbackParams;

	/**
	 * 销售产品码，商家和支付宝签约的产品码
	 */
	private String productCode;

	/**
	 * 优惠参数 注：仅与支付宝协商后可用
	 */
	private String promoParams;

	/**
	 * 描述分账信息，json格式，详见分账参数说明
	 */

	/**
	 * 收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
	 */
	private String sellerId;

	/**
	 * 描述结算信息，json格式，详见结算参数说明
	 */

	/**
	 * 指定渠道，目前仅支持传入pcredit 若由于用户原因渠道不可用，用户可选择是否用其他渠道支付。 注：该参数不可与花呗分期参数同时传入
	 */
	private String specifiedChannel;

	/**
	 * 商户门店编号
	 */
	private String storeId;

	/**
	 * 间连受理商户信息体，当前只对特殊银行机构特定场景下使用此字段
	 */

	/**
	 * 商品的标题/交易标题/订单标题/订单关键字等。
	 */
	private String subject;

	/**
	 * 绝对超时时间，格式为yyyy-MM-dd HH:mm。
	 */
	private String timeExpire;

	/**
	 * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，
	 * 都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
	 */
	private String timeoutExpress;

	/**
	 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	 */
	private String totalAmount;
	
	public AlipayDTO() {}

/*	public AlipayDTO(String body, String outTradeNo, String passbackParams, String subject, String timeExpire,
			String timeoutExpress, String totalAmount) {
		super();
		this.body = body;
		this.outTradeNo = outTradeNo;
		this.passbackParams = passbackParams;
		this.subject = subject;
		this.timeExpire = timeExpire;
		this.timeoutExpress = timeoutExpress;
		this.totalAmount = totalAmount;
	}*/

	 /*AlipayDTO apiPayDTO = new AlipayDTO();
    apiPayDTO.setBody("贡融积分商城");
    apiPayDTO.setOutTradeNo(save1.getOrderNo());
    apiPayDTO.setSubject("支付");
    apiPayDTO.setPassbackParams("deposit");
    apiPayDTO.setTotalAmount(totalPrice.toString());
    apiPayDTO.setTimeoutExpress("30m");*/
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBusinessParams() {
		return businessParams;
	}

	public void setBusinessParams(String businessParams) {
		this.businessParams = businessParams;
	}

	public String getDisablePayChannels() {
		return disablePayChannels;
	}

	public void setDisablePayChannels(String disablePayChannels) {
		this.disablePayChannels = disablePayChannels;
	}

	public String getEnablePayChannels() {
		return enablePayChannels;
	}

	public void setEnablePayChannels(String enablePayChannels) {
		this.enablePayChannels = enablePayChannels;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getPassbackParams() {
		return passbackParams;
	}

	public void setPassbackParams(String passbackParams) {
		this.passbackParams = passbackParams;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPromoParams() {
		return promoParams;
	}

	public void setPromoParams(String promoParams) {
		this.promoParams = promoParams;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSpecifiedChannel() {
		return specifiedChannel;
	}

	public void setSpecifiedChannel(String specifiedChannel) {
		this.specifiedChannel = specifiedChannel;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

	public String getTimeoutExpress() {
		return timeoutExpress;
	}

	public void setTimeoutExpress(String timeoutExpress) {
		this.timeoutExpress = timeoutExpress;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
