package com.linkage.module.itms.service.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.dao.ZeroconfigurationDAO_XJ;

public class ZeroconfigurationBIO_XJ {
	private static Logger logger = LoggerFactory.getLogger(ZeroconfigurationBIO_XJ.class);
	private ZeroconfigurationDAO_XJ dao;

	public List<Map<String,String>> queryServiceType(){
		List<Map<String,String>> list=dao.queryServiceType();
		List<Map<String,String>> lis=new ArrayList<Map<String,String>>();

		for(Map<String,String> map:list){
			Map<String,String> m=new HashMap<String,String>();
			String service_type=StringUtil.getStringValue(map,"service_type","0");
			
			String service_type_name="";
			switch(Integer.parseInt(service_type)){
				case 1:
					service_type_name = "家庭网关e8-b";
					break;
				case 2:
					service_type_name = "家庭网关e8-c";
					break;
				case 3:
					service_type_name = "政企网关";
					break;
				case 4:
					service_type_name = "机顶盒";
					break;
			}
			logger.warn("ZeroconfigurationBIO_XJ queryServiceType() service_type="+service_type+" service_type_name="+service_type_name);
			m.put("service_type",service_type);
			m.put("service_type_name",service_type_name);
			lis.add(m);
			
			map=null;
			m=null;
		}
		return lis;
	}
	
	public List<Map<String,String>> queryOperateType(){
		List<Map<String,String>> list=dao.queryOperateType();
		List<Map<String,String>> lis=new ArrayList<Map<String,String>>();

		for(Map<String,String> map:list){
			Map<String,String> m=new HashMap<String,String>();
			String operate_type=StringUtil.getStringValue(map,"operate_type","0");
			
			String operate_type_name="";
			switch(Integer.parseInt(operate_type)){
				case 1:
					operate_type_name = "绑定";
					break;
				case 2:
					operate_type_name = "解绑";
					break;
			}
			logger.warn("ZeroconfigurationBIO_XJ queryOperateType() operate_type="+operate_type+" operate_type_name="+operate_type_name);
			m.put("operate_type",operate_type);
			m.put("operate_type_name",operate_type_name);
			lis.add(m);
			
			map=null;
			m=null;
		}
		return lis;
	}
	
	public List<Map> queryList(int curPage_splitPage, int num_splitPage,String user_info,String device_sn,
			String service_type,String operate_type,String cityId,String starttime,String endtime, String devsn){
		logger.warn("ZeroconfigurationBIO_XJ queryList() user_info="+user_info
					+" device_sn="+device_sn+" service_type="+service_type+" operate_type"+operate_type);
		List<Map> list = dao.queryList(curPage_splitPage,num_splitPage,user_info,device_sn,service_type,operate_type,cityId,starttime,endtime,devsn);
		if(null==user_info||"".equals(user_info)){
			if(list != null && list.size() > 0){
				if(null==list.get(0).get("user_info")||"".equals(list.get(0).get("user_info"))){
					for(Map map:list){
						map.put("bind_result", "无绑定工单");
					}
				}
			}else{
				return null;
			}
		}
		return list;
	}
	
	public String queryBindMsg(String user_info,String device_sn,String service_type,String operate_type,String devsn){
		logger.warn("ZeroconfigurationBIO_XJ queryBindMsg() user_info="+user_info
					+" device_sn="+device_sn+" service_type="+service_type+" operate_type"+operate_type);
		return dao.getFailMsg(user_info,device_sn,service_type,operate_type,devsn);
	}
	
	public int getCount(int num_splitPage,String user_info,String device_sn,String service_type,String operate_type,
						String cityId,String starttime,String endtime, String devsn){
		
		return dao.getCount(num_splitPage,user_info, device_sn, service_type, operate_type, cityId, starttime, endtime,devsn);
	}

	public ZeroconfigurationDAO_XJ getDao() {
		return dao;
	}

	public void setDao(ZeroconfigurationDAO_XJ dao) {
		this.dao = dao;
	}
}
