package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.dao.ModifyVlanToolsDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
//import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class ModifyVlanToolsBIO {
	
	private ModifyVlanToolsDAO dao;
	/** 宁夏删除 iptv vlan 43 */
	public static String SERVICE_ID_DEL_IPTV = "2601";
	/** 宁夏增加 iptv vlan 44 */
	public static String SERVICE_ID_ADD_IPTV = "2602";

	private static Logger logger = LoggerFactory.getLogger(ModifyVlanToolsBIO.class);
	// 回传消息
	private String msg = null;

	public static boolean flag = true;
	

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() { 
		return dao.getTodayCount();		
	}
	
	
	public String addTask4NX(long taskid, long accoid, String deviceIds, String param, String strategy_type, String type){
		try {
			logger.warn("ModifyVlanToolsBIO.addTask4NX()");
			
			String serviceId = "";
			if ("1".equals(type)) {
				serviceId = SERVICE_ID_DEL_IPTV;
			} else if ("2".equals(type)) {
				serviceId = SERVICE_ID_ADD_IPTV;
			} 
			
			List<String> netAccounts = new ArrayList<String>();
			
			if(StringUtil.IsEmpty(deviceIds) || "0".equals(deviceIds)){
				String sqlSpell = null;
				if(!StringUtil.IsEmpty(param)){
					String[] paramArr = param.split("\\|");
					if(null!=paramArr &&  paramArr.length>=11){
						sqlSpell = paramArr[10];
					}
				}
				
				if(StringUtil.IsEmpty(sqlSpell)){
					logger.warn("==[{}]设备为空，查询sql为空，程序结束==", taskid);
					return "false";
				}
				
				if(LipossGlobals.inArea(Global.HBLT) && sqlSpell.contains("[")){
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				if((LipossGlobals.inArea(Global.NMGDX) || LipossGlobals.inArea(Global.SDDX)) && sqlSpell.contains("[")){
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds4NX(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
				
				int num = dao.addTask4NX(taskid, accoid, sqlSpell, 2, serviceId, strategy_type);
				if(num>0){
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}]设备查询结果为空，程序结束==", taskid);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid + "";
			dao.insertTmp(filenameTmp, netAccounts);
			// 根据设备序列号获取设备信息
			ArrayList<HashMap<String, String>> devList = dao.getTaskDevList4NX(filenameTmp);
			List<HashMap<String, String>> devListNew = new ArrayList<HashMap<String, String>>();
			
			if(null!=devList && devList.size()>0){
				for(HashMap<String, String> map : devList)
				{
					String aDeviceId = StringUtil.getStringValue(map, "a_device_id", "");
					
					if(StringUtil.IsEmpty(map.get("device_id")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到设备", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("username")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到loid", taskid, aDeviceId);
					}
					else{
						devListNew.add(map);
					}
				}
				if(null==devListNew || devListNew.size()==0)
				{
					logger.warn("taskid[{}] devListNew is null", taskid);
					return "false";
				}
			}else{
				logger.warn("taskid[{}] devList is null", taskid);
				return "false";
			}
			
			logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devListNew.size());
			if(devListNew.size()>10000){
				logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
				return "false10000";
			}
			// 获取sqlList
			ArrayList<String> sqlList = dao.sqlList(devListNew, taskid, serviceId);
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int count = 0;
			
			// 批量插入设备信息
			if(null!=sqlList && sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int res = dao.insertTaskDev4NX(sqlListTmp);
						if(res>0){
							count += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int res = dao.insertTaskDev4NX(sqlListTmp);
				if(res>0){
					count += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			
			logger.warn("==[{}]插入tab_modify_vlan_task_dev表[{}]条数据==", taskid, count);
			
			int num = dao.addTask4NX(taskid, accoid, "", 1, serviceId, strategy_type);
			
			List<String> devIdList = new ArrayList<String>();
			for(Map<String,String> map : devListNew){
				devIdList.add(StringUtil.getStringValue(map,"device_id",""));
			}
			
			String[] array = new String[devIdList.size()];
			
			if(num>0){
				
				/*PreProcessCorba ppc = new PreProcessCorba("1");
				if (1 == ppc.processDeviceStrategy(new String[]{"deviceIds"}, serviceId,
							new String[] { StringUtil.getStringValue(taskid) })) {
					logger.warn("调用后台预读模块成功");
					return "true";
				} else {
					logger.warn("调用后台预读模块失败");
					return "false";
				}*/
						
						if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
								devIdList.toArray(array), serviceId,
								new String[] { StringUtil.getStringValue(taskid) })) {
							logger.warn("调用后台预读模块成功");
							return "true";
						} else {
							logger.warn("调用后台预读模块失败");
							return "false";
						}
				
			}else{
				return "false";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	
	public List<Map> queryModifyVlanInfo(String type, String cityId) {
		logger.warn("ModifyVlanCountBIO--queryModifyVlanInfo(),type:{},cityID:{}", type, cityId);
		List date = this.dao.queryModifyVlanInfo(getService(type));
		if ((date == null) || (date.isEmpty())) {
			return new ArrayList();
		}
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		List list = new ArrayList();
		for (int i = 0; i < cityList.size(); i++) {
			long countAll = 0L;
			long failnum = 0L;
			long successnum = 0L;
			long noupnum = 0L;
			String city_id = (String) cityList.get(i);
			ArrayList tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map tmap = new HashMap();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = (String) tlist.get(j);
				for (int k = 0; k < date.size(); k++) {
					Map rmap = (Map) date.get(k);
					if (!cityId2.equals(rmap.get("city_id")))
						continue;
					String status = StringUtil.getStringValue(rmap.get("status"));
					String result_id = StringUtil.getStringValue(rmap.get("result_id"));
					if (("100".equals(status)) && ("1".equals(result_id))) {
						successnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (((!"".equals(status)) || (!"".equals(result_id)))
							&& ((!"100".equals(status)) || (!"1".equals(result_id)))) {
						failnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (("".equals(status)) && ("".equals(result_id))) {
						noupnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else {
						countAll += StringUtil.getLongValue(rmap.get("total"));
					}
				}
			}
			countAll = countAll + successnum + noupnum + failnum;
			tmap.put("allup", Long.valueOf(countAll));
			tmap.put("successnum", Long.valueOf(successnum));
			tmap.put("noupnum", Long.valueOf(noupnum));
			tmap.put("failnum", Long.valueOf(failnum));
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		return this.dao.getDevList(getService(type), status, cityId, curPage_splitPage, num_splitPage);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		return this.dao.getDevCount(getService(type), status, cityId, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDevExcel(String type, String cityId, String status) {
		return this.dao.getDevExcel(getService(type), status, cityId);
	}

	/**
	 * 查询单双栈刷新统计结果
	 * 
	 * @param type
	 * @param cityId3
	 * @param type2
	 * @return
	 */
	public List<Map> queryModifyVlanInfo(String starttime1, String endtime1, String type, String cityId) {
		logger.debug("ModifyVlanCountBIO--queryModifyVlanInfo(),starttime:{},endtime:{},type:{},cityID:{}",
				starttime1, endtime1, type, cityId);
		List<Map> date = dao.queryModifyVlanInfo(getService(type), starttime1, endtime1, cityId);
		if (null == date || date.isEmpty()) {
			return new ArrayList<Map>();
		}
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 最终结果的list
		List<Map> list = new ArrayList<Map>();
		// 遍历属地
		for (int i = 0; i < cityList.size(); i++) {
			// 计数
			// 总数
			long countAll = 0;
			// 失败总数
			long failnum = 0;
			// 成功总数
			long successnum = 0;
			// 未触发总数
			long noupnum = 0;
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = tlist.get(j);
				for (int k = 0; k < date.size(); k++) {
					Map rmap = (Map) date.get(k);
					if (cityId2.equals(rmap.get("city_id"))) {
						//
						String status = StringUtil.getStringValue(rmap.get("status"));
						String result_id = StringUtil.getStringValue(rmap.get("result_id"));
						// 判断成功0---100
						if ("100".equals(status) && "1".equals(result_id)) {
							successnum = successnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 判断失败非0---100
						else if ((!"".equals(status) || !"".equals(result_id))
								&& (!"100".equals(status) || !"1".equals(result_id))) {
							failnum = failnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 判断未作状态
						else if ("".equals(status) && "".equals(result_id)) {
							noupnum = noupnum + StringUtil.getLongValue(rmap.get("total"));
						}
						else {
							countAll = countAll + StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			countAll = countAll + successnum + noupnum + failnum;
			tmap.put("allup", countAll);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("failnum", failnum);
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	/**
	 * 详情页查询
	 * 
	 * @param type
	 * @param status
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		return dao.getDevList(getService(type), status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		return dao
				.getDevCount(getService(type), status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
	}

	public List<Map> getDevExcel(String type, String cityId, String status, String starttime1, String endtime1) {
		return dao.getDevExcel(getService(type), status, cityId, starttime1, endtime1);
	}

	private String getService(String type) {
		String serviceId1 = "";
		if ("1".equals(type)) {
			serviceId1 = SERVICE_ID_DEL_IPTV;
		}
		else if ("2".equals(type)) {
			serviceId1 = SERVICE_ID_ADD_IPTV;
		}
		return serviceId1;
	}
	
	
	public ModifyVlanToolsDAO getDao() {
		return dao;
	}

	public void setDao(ModifyVlanToolsDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
