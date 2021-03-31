/**
 * 
 */
package com.linkage.module.itms.resource.obj;


/**
 * @author chenjie(67371)
 * 卡状态OBJ
 */
public class CardStatusOBJ {
	
	/**
	 * 采集结果码
	 */
	private int resultCode;
	
	/**
	 * 结果信息
	 */
	private String resultStr;
	
	/**
	 * 卡序列号
	 */
	private String cardNo;
	
	/**
	 * 写卡标识
	 */
	private String status;
	
	/**
	 * 卡是否在线
	 */
	private String cardStatus;
	
	/**
	 * 关联设备ID
	 */
	private String deviceId;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultStr() {
		return resultStr;
	}

	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("CardStatusOBJ{").append("cardNo:").append(cardNo)
				.append(", ").append("status:").append(status)
				.append(", ").append("cardStatus:").append(cardStatus)
				.append("}");
		return sb.toString();
	}
}
