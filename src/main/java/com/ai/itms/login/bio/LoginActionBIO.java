package com.ai.itms.login.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ai.itms.login.dao.LoginActionDAO;
import com.linkage.commons.util.StringUtil;

/**
 * 江西电信天翼网关版本一致率
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2019-4-15
 */
public class LoginActionBIO {
	Logger logger = LoggerFactory.getLogger(LoginActionBIO.class);
	// 持久层
	LoginActionDAO dao;
	
	
	public Map<String, String> getUserPhone(String username){
		String per_mobile="";
		String acc_oid="";
		String acc_loginname="";
		Map<String, String> map=new HashMap<String, String>();
		List<Map<String, Object>> userPhoneList = dao.getUserPhone(username);
		if (userPhoneList!=null && userPhoneList.size()>0) {
			Map userPhoneMap=userPhoneList.get(0);
			per_mobile=StringUtil.getStringValue(userPhoneMap, "per_mobile");
			acc_oid=StringUtil.getStringValue(userPhoneMap, "acc_oid");
			acc_loginname=StringUtil.getStringValue(userPhoneMap, "acc_loginname");
		}
		if (StringUtil.IsEmpty(per_mobile)) {
			map.put("code", "0");
			map.put("message", "用户不存在或未绑定手机号，请联系管理员！");
		}
		else {
			map.put("code", "1");
			map.put("per_mobile", per_mobile);
			map.put("acc_oid", acc_oid);
			map.put("acc_loginname", acc_loginname);
			map.put("message", "获取到用户手机号");
		}
		
		return map;
	}

	
	public List<Map<String, Object>> queryMessageLog(String acc_oid){
		return dao.queryMessageLog(acc_oid);
	}

	public void updateMessageLog(String acc_oid,String per_mobile){
		dao.updateMessageLog(acc_oid, per_mobile);
	}
	
	
	public void insertMessageLog(String acc_oid,String acc_loginname,String per_mobile){
		dao.insertMessageLog(acc_oid, acc_loginname, per_mobile);
	}
	
	
	public LoginActionDAO getDao() {
		return dao;
	}

	public void setDao(LoginActionDAO dao) {
		this.dao = dao;
	}
	
}
