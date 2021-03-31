package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.AutoSoftUpRuleDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 湖南联通自动软件升级规则管理
 */
@SuppressWarnings("rawtypes")
public class AutoSoftUpRuleBIO 
{
	private static Logger logger = LoggerFactory.getLogger(AutoSoftUpRuleBIO.class);
	private AutoSoftUpRuleDAO dao;
	
	
	/**查询厂商列表*/
	public List<Map<String,String>> querVentorList()
	{
		List<Map<String,String>> queryList=dao.queryVendor();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		if(queryList!=null && !queryList.isEmpty()){
			for(Map<String,String> m:queryList)
			{
				Map<String,String> map=new HashMap<String,String>();
				map.putAll(m);
				if(StringUtil.IsEmpty(StringUtil.getStringValue(m,"vendor_add"))){
					map.put("vendor_add",StringUtil.getStringValue(m,"vendor_name"));
				}
				
				list.add(map);
			}
		}
		return list;
	}
	
	/**根据厂商ID查询设备型号*/
	public String getDeviceModelS(String vendorId)
	{
		String deviceModelS = "";
		List<Map<String,String>> list = dao.getDeviceModelList(vendorId);
		if(null != list && !list.isEmpty()){
			for(Map<String,String> map:list){
				if(StringUtil.IsEmpty(deviceModelS)){
					deviceModelS += StringUtil.getStringValue(map,"device_model_id") 
										+ "$" + StringUtil.getStringValue(map,"device_model");
				}else{
					deviceModelS += "#" + StringUtil.getStringValue(map,"device_model_id") 
										+ "$" + StringUtil.getStringValue(map,"device_model");
				}
			}
		}
		logger.debug("[{}] getDeviceModelS:[{}]",vendorId,deviceModelS);
		return deviceModelS;
	}
	
	/** 根据设备型号ID查询设备硬件版本*/
	public String getHardwareversionS(String deviceModelId)
	{		
		String versions = "";
		List<Map<String,String>> list = dao.getHardwareversionS(deviceModelId);
		if(null != list && !list.isEmpty()){
			for(Map<String,String> map:list){
				if(StringUtil.IsEmpty(versions)){
					versions +=StringUtil.getStringValue(map,"hardwareversion");
				}else{
					versions += "#" + StringUtil.getStringValue(map,"hardwareversion");
				}
			}
		}
		logger.debug("[{}] getHardwareversionS:[{}]",deviceModelId,versions);
		return versions;
	}
	
	/**根据型号ID、硬件版本查询设备软件版本*/
	public String getSoftwareversionS(String deviceModelId,String hardwareversion)
	{
		String versions = "";
		List<Map<String,String>> list = dao.getSoftwareversionS(deviceModelId,hardwareversion);
		if(null != list && !list.isEmpty()){
			for(Map<String,String> map:list){
				if(StringUtil.IsEmpty(versions)){
					versions += StringUtil.getStringValue(map,"softwareversion");
				}else{
					versions += "#" + StringUtil.getStringValue(map,"softwareversion");
				}
				
			}
		}
		logger.debug("[{}-{}] getSoftwareversionS:[{}]",deviceModelId,hardwareversion,versions);
		return versions;
	}
	
	/**查询版本数据*/
	public String getDevType(String vendorId, String deviceModelId,
			String hardwareversion, String softwareversion) 
	{
		String versions = "";
		Map map = dao.getDevType(vendorId,deviceModelId,hardwareversion,softwareversion);
		if(null != map && !map.isEmpty())
		{
			String net_type=StringUtil.getStringValue(map,"net_type");
			if("public_net".equals(net_type)){
				net_type="公网";
			}else if("private_net".equals(net_type)){
				net_type="专网";
			}else{
				net_type="未知";
			}
			versions=StringUtil.getStringValue(map,"devicetype_id")+","
						+StringUtil.getStringValue(map,"epg_version")+","
						+net_type;
		}
		return versions;
	}
	
	/**查询目标版本数据*/
	public String getTargetSoftVersion(String deviceModelId) 
	{
		String versions = "";
		List<Map<String,String>> list = dao.getTargetSoftVersion(deviceModelId);
		if(null != list && !list.isEmpty()){
			for(Map<String,String> map:list){
				String desc=StringUtil.getStringValue(map,"id","")+","
								+StringUtil.getStringValue(map,"version_name","")
								+"("+StringUtil.getStringValue(map,"version_path","")+")";
				if(StringUtil.IsEmpty(versions)){
					versions+=desc;
				}else{
					versions+="#"+desc;
				}
			}
		}
		return versions;
	}
	
	/**查询目标版本详细数据*/
	public String getTargetSoftVersionDetail(String id) 
	{
		String versions = "";
		Map map = dao.getTargetSoftVersionDetail(StringUtil.getLongValue(id));
		if(null != map && !map.isEmpty())
		{
			String net_type=StringUtil.getStringValue(map,"net_type");
			if("public_net".equals(net_type)){
				net_type="公网";
			}else if("private_net".equals(net_type)){
				net_type="专网";
			}else{
				net_type="未知";
			}
			versions=StringUtil.getStringValue(map,"version_desc")+","
						+StringUtil.getStringValue(map,"version_path")+","
						+StringUtil.getStringValue(map,"file_size")+","
						+StringUtil.getStringValue(map,"md5")+","
						+net_type+","
						+StringUtil.getStringValue(map,"epg_version");
		}
		return versions;
	}
	
	/**校验适用规则是否存在，1：不存在 */
	public int checkTargetVersionRule(String devicetype_id,String user_net_type)
	{
		return dao.getTargetVersionRuleNum(devicetype_id,user_net_type);
	}
	
	/**新增规则*/
	public String addSoftUpRule(String devicetype_id,String dev_net_type,String user_net_type,
			String version_net_type,String version_id,long opertor)
	{
		return dao.addSoftUpRule(devicetype_id,dev_net_type,
									user_net_type,version_net_type,version_id,opertor);
	}
	
	/**分页查询*/
	public List<Map> queryRuleList(int curPage_splitPage,int num_splitPage,String vendorId,
			String deviceModelId,String hardwareversion,String softwareversion)
	{
		return dao.queryRuleList(curPage_splitPage,num_splitPage,
				vendorId,deviceModelId,hardwareversion,softwareversion);
	}
	
	/**计算最大页数*/
	public int queryRuleCount(int curPage_splitPage,int num_splitPage,String vendorId,
			String deviceModelId,String hardwareversion,String softwareversion)
	{
		int total=dao.queryRuleCount(vendorId,deviceModelId,hardwareversion,softwareversion);
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/** 获取编辑规则的数据*/
	public Map getSoftUpRuleInfo(String rule_id) 
	{
		return dao.queryRuleInfo(rule_id);
	}
	
	/**编辑规则*/
	public String updateSoftUpRule(String rule_id,String devicetype_id,String dev_net_type,String user_net_type,
			String version_net_type,String version_id,long opertor)
	{
		return dao.updateSoftUpRule(rule_id,devicetype_id,dev_net_type,
									user_net_type,version_net_type,version_id,opertor);
	}
	
	/**删除规则*/
	public String deleteSoftUpRule(String rule_id)
	{
		return dao.deleteSoftUpRule(rule_id);
	}
		
	

	public AutoSoftUpRuleDAO getDao() {
		return dao;
	}

	public void setDao(AutoSoftUpRuleDAO dao) {
		this.dao = dao;
	}
	
}
