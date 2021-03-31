package com.ai.itms.login.act;

import com.ai.itms.login.bio.LoginActionBIO;
import com.ai.itms.sms.sendMessage;
import com.linkage.commons.util.StringUtil;
import com.linkage.system.extend.struts.splitpage.SplitPageAction;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.SkinUtils;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
/**
 * 登陆鉴权Action类
 * 
 * @author fanjm@asiainfo-sec.com
 * @version 1.0
 * @since 2016年11月1日
 */
public class LoginAction extends SplitPageAction {

	private static final long serialVersionUID = 2408145375650667872L;
	/** log */
	private static Logger logger = LoggerFactory.getLogger(LoginAction.class);

	private LoginActionBIO bio;
	
	//生成的验证码
	private String checkCode;
	private String ajax;
	private String username;
	/**
	 * 刷新验证码
	 * 
	 * @return ajax
	 */
	public String refresh() {
		logger.debug("refresh({})==>方法入口");
		HttpServletRequest request = ServletActionContext.getRequest();
		// 生成附加码
		long checkCode = Math.round(Math.random() * 10000);
		if (checkCode < 1000) {
			checkCode = checkCode + 1000;
		}
		SkinUtils.setSession(request, "checkCode", StringUtil.getStringValue(checkCode));
		logger.debug("LoginAction.checkuser,checkCode="
				+ request.getSession().getAttribute("checkCode") + "...");
		this.ajax = StringUtil.getStringValue(checkCode);
		logger.debug("refresh({})==>方法出口");
		return "ajax";
	}


	/**
	 * 内蒙古使用短信验证码
	 */
	public String getMessageCode() {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		
		Map<String, String> userPhoneMap = bio.getUserPhone(username);
		String code = userPhoneMap.get("code");
		String message = userPhoneMap.get("message");
		if ("0".equals(code)) {
			this.ajax="0##"+message+"";
			return "ajax";
		}else {
			String per_mobile = userPhoneMap.get("per_mobile");
			String acc_oid = userPhoneMap.get("acc_oid");
			String acc_loginname = userPhoneMap.get("acc_loginname");
			
			String mobile=per_mobile.substring(0, 3)+"****"+per_mobile.substring(7, 11);
			
			/*String sysCheckCode = (String) SkinUtils.getSession(request, "checkCode-"+per_mobile);
			String ValidTime = (String) SkinUtils.getSession(request, "ValidTime");
			if (!StringUtil.IsEmpty(sysCheckCode) && !StringUtil.IsEmpty(ValidTime)) {
				long nowTime =System.currentTimeMillis();
				long ValidTime1 = StringUtil.getLongValue(ValidTime);
				if(nowTime-ValidTime1<1000*60*2){
					this.ajax="0##短信码还在有效期内";
					return "ajax";
				}
			}*/
			
			
			//查询日志
			List<Map<String, Object>> logList = bio.queryMessageLog(acc_oid);
			if (logList!=null&&logList.size()>0) {
				Map map=logList.get(0);
				String dateTime =StringUtil.getStringValue(map, "update_time");
				long currectTime=System.currentTimeMillis()/1000;
				if (currectTime-StringUtil.getLongValue(dateTime)<1*60) {
					this.ajax="0##操作过于频繁，请60秒后重试";
					return "ajax";
				}
			}
			
			
			Integer messageCode = (int) ((Math.random() * 9 + 1) * 100000);
			//发送短信
			String ip=LipossGlobals.getLipossProperty("SMGP_IP");
			String port=LipossGlobals.getLipossProperty("SMGP_PORT");
			String username=LipossGlobals.getLipossProperty("SMGP_USERNAME");
			String pwd=LipossGlobals.getLipossProperty("SMGP_PWD");
			String serviceCode=LipossGlobals.getLipossProperty("SMGP_SERVICECODE");
			boolean send = sendMessage.send(ip, StringUtil.getIntegerValue(port), username, pwd, serviceCode, per_mobile, StringUtil.getStringValue(messageCode));
			if (send) {
				this.ajax = "1##短信码已发送到手机："+mobile+"";
				long currentTime = System.currentTimeMillis();
				SkinUtils.setSession(request, "checkCode-"+per_mobile, StringUtil.getStringValue(messageCode));
				//有效期设置，对比时长超过5分钟 验证码失效
				SkinUtils.setSession(request, "ValidTime", StringUtil.getStringValue(currentTime));
				if (logList!=null&&logList.size()>0) {
					bio.updateMessageLog(acc_oid, per_mobile);
				}else {
					bio.insertMessageLog(acc_oid, acc_loginname, per_mobile);
				}
			}else {
				this.ajax = "0##短信码发送失败";
			}
			
			logger.warn("getMessageCode({})",request.getSession().getAttribute("checkCode-"+per_mobile));
			return "ajax";
		}
	}

	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}


	public LoginActionBIO getBio() {
		return bio;
	}

	public void setBio(LoginActionBIO bio) {
		this.bio = bio;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	
}
