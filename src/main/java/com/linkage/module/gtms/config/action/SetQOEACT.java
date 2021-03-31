package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.SetQOEServ;

public class SetQOEACT implements SessionAware {
	private static Logger logger = LoggerFactory
			.getLogger(SetQOEACT.class);
	// Session
	private Map session;
	private long acc_oid;
	private String deviceIds = "";
	// 符合条件的loid
	private static String isSuccess = "1";
	private String ajax = "";
	private SetQOEServ bio;
	private String batchOn_exeWay;
	private String exeWay_QoeOff;
	private String singleOn_url;
	private String batchOn_url;
	private String do_type;
	private String gwShare_queryField;
	private String gwShare_queryParam;
	private String service_id;
	private Map returnMap;
	private String return_type;

	public String refrsehMsg() {
		ajax = bio.refrsehMsg(deviceIds);
		return "ajax";
	}
	public String isBindUser() {
		ajax = bio.isBindUser(deviceIds);
		return "ajax";
	}
	public String isQoe() {
		ajax = bio.isQoe(deviceIds);
		return "ajax";
	}

	public String isBind() {
		ajax = bio.isBind(gwShare_queryField, gwShare_queryParam);
		return "ajax";
	}

	public String queryQOE(){
		logger.warn("111111111111111");
		logger.warn("deviceIds================"+deviceIds);
		returnMap=bio.doQueryQOE(deviceIds);
		if(1 == returnMap.size()){
			return_type = (String)returnMap.get("res");
		}else if(5 == returnMap.size()&&!deviceIds.contains(",")){
			return_type = "single_on";
		}
		return "showList";
	}

	public String doConfig(){
		List<String> listChecked = new ArrayList<String>();
		UserRes curUser = (UserRes) session.get("curUser");
		acc_oid = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();
		String[] deviceStr = null;
		// 如果loid在用户 表中存在并且绑定了设备
		if (deviceIds.contains(",")) // 多个设备
		{
			deviceStr = deviceIds.split(",");
			for (String deviceId : deviceStr) {
				String result = this.getStringValue(deviceId);
				if (result.equals("1")) {
					list.add(deviceId);
				}
			}
			// 如果loid在用户 表中存在并且绑定了设备并且支持QOE
			if(list.size()!=0){
				for (String deviceId : list) {
					String isQoe = bio.isQoe(deviceId);
					if("1".equals(isQoe)){
						listChecked.add(deviceId);
					}
				}
			}else{
				//QOE功能状态节点采集终端必须绑定用户
				return_type = "1";
			}
		} else // 单个设备
		{
			listChecked.add(deviceIds);
		}

		if(listChecked.size()!=0){
			String set_url="";
			String strategy_type="";
			String set_enable="";
			if("single_on".equals(do_type)){
				set_url = singleOn_url;
				strategy_type="1";
				set_enable="1";
			}else if("single_off".equals(do_type)||"batch_off".equals(do_type)){
				set_url = null;
				strategy_type=exeWay_QoeOff;
				set_enable="0";
			}else if("batch_on".equals(do_type)){
				set_url = batchOn_url;
				strategy_type=batchOn_exeWay;
				set_enable="1";
			}
			logger.warn("listChecked==============="+listChecked);
			if("query".equals(do_type)){
				returnMap = bio.queryQOE(listChecked);
			}else{
				returnMap = bio.doConfig(acc_oid, listChecked,service_id,strategy_type,set_enable,set_url,do_type);
			}

			if(1 == returnMap.size()){
				return_type = (String)returnMap.get("res");
			}else if(5 == returnMap.size()&&!deviceIds.contains(",")){
				return_type = "single_on";
			}
		}else{
			//终端版本不支持QOE功能
			return_type = "2";
		}
		logger.warn("returnMap======"+returnMap);
		logger.warn("return_type======"+return_type);
		return "showList";
	}
	public String getStringValue(String deviceId) { 
		String msn = "";
		if(deviceId.contains(",")){
			String[] deviceIdArray = deviceId.split(",");
			for(String arr : deviceIdArray){
				msn += bio.isBindUser(arr)+",";
			}
			msn.substring(0, msn.lastIndexOf(","));
		}else{
			msn = bio.isBindUser(deviceId);
		}
		return msn;
	}

	public Map getSession()
	{
		return session;
	}


	public void setSession(Map session)
	{
		this.session = session;
	}

	public long getAcc_oid()
	{
		return acc_oid;
	}

	public void setAcc_oid(long acc_oid)
	{
		this.acc_oid = acc_oid;
	}
	public String getDeviceIds()
	{
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	public static String getIsSuccess()
	{
		return isSuccess;
	}

	public static void setIsSuccess(String isSuccess)
	{
		SetQOEACT.isSuccess = isSuccess;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public SetQOEServ getBio()
	{
		return bio;
	}

	public void setBio(SetQOEServ bio)
	{
		this.bio = bio;
	}

	public String getBatchOn_exeWay()
	{
		return batchOn_exeWay;
	}

	public void setBatchOn_exeWay(String batchOn_exeWay)
	{
		this.batchOn_exeWay = batchOn_exeWay;
	}

	public String getExeWay_QoeOff()
	{
		return exeWay_QoeOff;
	}

	public void setExeWay_QoeOff(String exeWay_QoeOff)
	{
		this.exeWay_QoeOff = exeWay_QoeOff;
	}

	public String getSingleOn_url()
	{
		return singleOn_url;
	}

	public void setSingleOn_url(String singleOn_url)
	{
		this.singleOn_url = singleOn_url;
	}


	public String getGwShare_queryField()
	{
		return gwShare_queryField;
	}


	public void setGwShare_queryField(String gwShare_queryField)
	{
		this.gwShare_queryField = gwShare_queryField;
	}


	public String getGwShare_queryParam()
	{
		return gwShare_queryParam;
	}


	public void setGwShare_queryParam(String gwShare_queryParam)
	{
		this.gwShare_queryParam = gwShare_queryParam;
	}


	public String getBatchOn_url()
	{
		return batchOn_url;
	}


	public void setBatchOn_url(String batchOn_url)
	{
		this.batchOn_url = batchOn_url;
	}


	public String getDo_type()
	{
		return do_type;
	}


	public void setDo_type(String do_type)
	{
		this.do_type = do_type;
	}


	public String getService_id()
	{
		return service_id;
	}


	public void setService_id(String service_id)
	{
		this.service_id = service_id;
	}






	public Map getReturnMap()
	{
		return returnMap;
	}


	public void setReturnMap(Map returnMap)
	{
		this.returnMap = returnMap;
	}

	public String getReturn_type()
	{
		return return_type;
	}


	public void setReturn_type(String return_type)
	{
		this.return_type = return_type;
	}

}
