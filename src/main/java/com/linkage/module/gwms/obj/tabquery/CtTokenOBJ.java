package com.linkage.module.gwms.obj.tabquery;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poson.ecss.util.security.Cipher;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2010-3-9
 */
public class CtTokenOBJ {

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CtTokenOBJ.class);

	//自定义超时时间(毫秒)
	private static final long timeOut = 10 * 60 * 1000;
	
	// UserToken
	private String userToken;
	// 用户帐号
	private String username;
	// 登录超时时间
	private long expireTime;
	// 用户(浏览器)IP
	private String clientIp;
	// BsToken
	private String bsToken;
	// 更新时间
	private long updateTime;
	//结果
	private int result;
	//登录时间
	private long loginTime;

	
	
	/**
	 * 方法用于获取BSTokenResponseVlalue值，转换为CtTokenOBJ对象
	 * 
BSTokenResponseValue的生成算法如下：
Digest = Base64(Hash(Result + “$” + UserID + “$”+ ClientIP +“$”＋LoginTime +“$”+ LoginType +“$”+ LoginLevel +“$”+ AccNbrList + “$”+ TimeStamp + “$” ＋ ExpireTime))
其中，Hash算法采用SHA-1。
BSTokenResponseValue = Base64(BSID + “$$” + Encrypt (Result + “$” + UserID + “$”+ ClientIP +“$”＋LoginTime +“$”+ LoginType +“$”+ LoginLevel +“$”+ AccNbrList + “$”+ TimeStamp + “$” ＋ ExpireTime + “$”+ Digest))
其中，加密算法采用3DES，Key=BSSecret，BSSecret是网上客服中心颁发给该业务系统的密钥。

	 * 
	 * 
	 * @param bsToken
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return CtTokenOBJ 正常返回CtTokenOBJ对象,如果参数bsToken为空返回null; 如果split("\\$")的字符串数组为空或长度小于10则返回null
	 */
	public static CtTokenOBJ getBSTokenResponseValue(String bsToken) {
		logger.debug("getBSTokenResponseValue({})", bsToken);
		
		//参数判空
		if (StringUtil.IsEmpty(bsToken)) {
			logger.warn("getBSTokenResponseValue BSToken is null");
			return null;
		}

		//解码
		String[] arrBsToken = decodeBsToken(bsToken);
		if (null != arrBsToken && 10 == arrBsToken.length) {
			
			long nowTime = System.currentTimeMillis();
			
			String result = arrBsToken[0];
			String username = arrBsToken[1];
			String clientIp = arrBsToken[2];
			String loginTime = arrBsToken[3];
			
			long ctStampTime = StringUtil.getLongValue(arrBsToken[7]);
			long ctExpireTime = StringUtil.getLongValue(arrBsToken[8]);
			long expireTime = nowTime + (ctExpireTime - ctStampTime);
			
			if(expireTime - nowTime <= 10000){
				//如果超时时间小于10S则用自定义的超时时间
				expireTime = nowTime + timeOut;
			}
			logger.debug("expireTime:({})", expireTime);
			CtTokenOBJ ctTokenObj = new CtTokenOBJ();

			ctTokenObj.setResult(StringUtil.getIntegerValue(result));
			ctTokenObj.setBsToken(bsToken);
			ctTokenObj.setClientIp(clientIp);
			ctTokenObj.setExpireTime(expireTime);
			ctTokenObj.setUsername(username);
			ctTokenObj.setLoginTime(new DateTimeUtil(loginTime).getLongTime());

			return ctTokenObj;
		} else {
			logger.warn("BSToken({})不合法", bsToken);
			return null;
		}
	}

	
	/**
	 * 
	 * Digest = Base64( Hash（OriginalUserToken）)
	 * 
	 * usertoken＝Base64（BSID + "$$" + Encrypt（OriginalUserToken＋"$"+Digest））
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return String
	 */
	public static String encodeToken(String originalToken){
		logger.debug("encodeToken({})", originalToken);
		String digest = Cipher.base64(1, Cipher.sha1(originalToken));

		String bsTokenRequestValue = Cipher.base64(1, LipossGlobals.getBsid() + "$$" + Cipher.tripleDES(1, originalToken + "$" + digest, LipossGlobals.getKey()));

		return bsTokenRequestValue;
	}
	
	
	
	/**
	 * 根据编码规则进行解码
	 * 
BSTokenResponseValue的生成算法如下：
Digest = Base64(Hash(Result + “$” + UserID + “$”+ ClientIP +“$”＋LoginTime +“$”+ LoginType +“$”+ LoginLevel +“$”+ AccNbrList + “$”+ TimeStamp + “$” ＋ ExpireTime))
其中，Hash算法采用SHA-1。
BSTokenResponseValue = Base64(BSID + “$$” + Encrypt (Result + “$” + UserID + “$”+ ClientIP +“$”＋LoginTime +“$”+ LoginType +“$”+ LoginLevel +“$”+ AccNbrList + “$”+ TimeStamp + “$” ＋ ExpireTime + “$”+ Digest))
其中，加密算法采用3DES，Key=BSSecret，BSSecret是网上客服中心颁发给该业务系统的密钥。
	 * 
	 * 产品号码中，如果含有“$”符号，会被替换成“$$$$”，如发现号码中有“$$$$”，请取出后自行还原为“$”
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return String
	 */
	public static String[] decodeBsToken(String bsToken){
		logger.debug("decodeBsToken({})", bsToken);
		String decodeBase64BsToken = Cipher.base64(2, bsToken);
		if(StringUtil.IsEmpty(decodeBase64BsToken)){
			logger.error("bsToken decodeBase64 is empty");
			return null;
		}
		
		logger.debug("decodeBase64BsToken:({})", decodeBase64BsToken);
		//分隔BSID
		int index = -1;
		if((index = decodeBase64BsToken.indexOf("$$")) < 0){
			logger.error("decodeBase64BsToken not contains $$ wrong");
			return null;
		}
		String des3BsToken = decodeBase64BsToken.substring(index + 2);
		if(StringUtil.IsEmpty(des3BsToken)){
			logger.error("bsToken Des3 is empty");
			return null;
		}
		
		logger.debug("des3BsToken:({})", des3BsToken);
		//DES3加密
		String decodeDes3BsToken = Cipher.tripleDES(2, des3BsToken, LipossGlobals.getKey());
		if(StringUtil.IsEmpty(decodeDes3BsToken)){
			logger.error("bsToken decodeDes3 is empty");
			return null;
		}
		logger.debug("decodeDes3BsToken:({})", decodeDes3BsToken);
		//已认证号码列表可能会出现
		decodeDes3BsToken.replaceAll("\\$\\$\\$\\$", "#");
		
		return decodeDes3BsToken.split("\\$");
	}
	
	
	
	
	
	/** getter, setter methods*/
	
	public String toString(){
		logger.debug("toString()");
		return "userToken:" + userToken + "  username=" + username;
	}
	
	
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getBsToken() {
		return bsToken;
	}

	public void setBsToken(String bsToken) {
		this.bsToken = bsToken;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

}
