package com.linkage.module.gtms.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.resource.dao.DeviceInitDAO;

/**
 * 设备数据是否录入初始表tab_gw_device_init功能查询
 * @author wanghong
 *
 */
public class DeviceInitBIO 
{
	private static Logger logger = LoggerFactory.getLogger(DeviceInitBIO.class);
	private DeviceInitDAO dao=null;
	
	/**
	 * 查询初始账数据
	 */
	public String query(String sn) 
	{
		@SuppressWarnings("rawtypes")
		List<Map> list=dao.query(sn);
		String result="不存在";
		if(list!=null && !list.isEmpty())
		{
			StringBuffer sb=new StringBuffer();
			sb.append(StringUtil.getStringValue(list.get(0),"device_serialnumber"));
			if(list.size()>1){
				for(int i=1;i<list.size();i++){
					sb.append(","+StringUtil.getStringValue(list.get(i),"device_serialnumber"));
				}
			}
			result=sb.toString();
		}
		
		logger.warn("DeviceInitBIO.query([{}]:[{}])",sn,result);
		return result;
	}
	
	
	
	
	public DeviceInitDAO getDao() {
		return dao;
	}
	
	public void setDao(DeviceInitDAO dao) {
		this.dao = dao;
	}

	
	
}
