package com.linkage.module.ids.act;

import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.bio.QueryUserInfoBIO;
		
public class QueryUserInfoACT 
{
	private static Logger logger = LoggerFactory.getLogger(QueryUserInfoACT.class);
	private String ajax;
	private Map<String,String> userInfoMap;
	private String device_serialnumber;
	private String loid;
	private QueryUserInfoBIO bio;
	/**
	 *      查询信息根据设备sn
			* @return
	 */
	public String getUserInfoBySn(){
		logger.warn("device_serialnumber is :"+device_serialnumber);
		String mark=bio.findOneDev(device_serialnumber.trim());
		//能确定一个设备
		if("0".equals(mark)||"2".equals(mark)){
			ajax=mark;
		}else {
			userInfoMap=bio.getDeviceInfo("device_serialnumber",mark);
			ajax=JSONObject.toJSONString(userInfoMap);
			ajax=ajax.replace("\\", "").replace("\"[", "[").replace("]\"", "]");			
		}
		return "ajax";
	}
	/**
	 *     查询信息根据loid
			* @return
	 */
	public String getUserInfoByLoid(){
		logger.warn("loid is :"+loid);
		if(bio.isUserExist(loid.trim())){
			userInfoMap=bio.getDeviceInfo("loid",loid.trim());
			ajax=JSONObject.toJSONString(userInfoMap);
			ajax=ajax.replace("\\", "").replace("\"[", "[").replace("]\"", "]");
		}else{
			ajax="0";
		}
		return "ajax";
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public Map<String, String> getUserInfoMap()
	{
		return userInfoMap;
	}
	
	public void setUserInfoMap(Map<String, String> userInfoMap)
	{
		this.userInfoMap = userInfoMap;
	}
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}
	
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}
	
	public String getLoid()
	{
		return loid;
	}
	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}
	
	public QueryUserInfoBIO getBio()
	{
		return bio;
	}
	
	public void setBio(QueryUserInfoBIO bio)
	{
		this.bio = bio;
	}
	

}

	