package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.dao.DevBatchRestartQueryStbDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.resource.dao.DevBatchRestartQueryDAO;


@SuppressWarnings("rawtypes")
public class DevBatchRestartQueryBIO 
{
	private static Logger logger = LoggerFactory.getLogger(DevBatchRestartQueryBIO.class);
	private Map<String,String> vendorMap=null;
	private Map<String,String> vendorAddMap=null;
	private Map<String,String> modelMap=null;
	private Map<String,String> versionMap=null;
	private String instArea=Global.instAreaShortName;

	private static final String SYSTEM_ITMS = "1";

	private DevBatchRestartQueryDAO dao;
	private DevBatchRestartQueryStbDAO stbDao;

	public List<Map> devBatchRestartQueryInfo(String cityId,
			String startTime, String endTime, int curPage_splitPage,
			int num_splitPage, String gwType, String gwShareCityId,
			String gwShareVendorId, String gwShareDeviceModelId,
			String gwShareDevicetypeId, String loid, String deviceSerialnumber,String servAccount)
	{
		logger.debug("voiceRegisterDeviceQueryInfo()");
		if (!SYSTEM_ITMS.equals(gwType)) {
			return stbDao.devBatchRestartQueryInfo(cityId, startTime, endTime,
					curPage_splitPage, num_splitPage, gwType, gwShareCityId,
					gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId,
					loid,deviceSerialnumber,servAccount);
		}
		else {
			return dao.devBatchRestartQueryInfo(cityId, startTime, endTime,
					curPage_splitPage, num_splitPage, gwType, gwShareCityId,
					gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId,
					loid,deviceSerialnumber,servAccount);
		}

	}

	public int countDevBatchRestartQueryInfo(String cityId, String startTime,
			String endTime, int curPage_splitPage, int num_splitPage,
			String gwType, String gwShareCityId, String gwShareVendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId,
			String loid,String deviceSerialnumber,String servAccount)
	{
		logger.debug("countVoiceRegisterDeviceQueryInfo()");
		if (!SYSTEM_ITMS.equals(gwType)) {
			return stbDao.countDevBatchRestartQueryInfo(cityId, startTime, endTime,
					curPage_splitPage, num_splitPage, gwType, gwShareCityId,
					gwShareVendorId, gwShare_deviceModelId, gwShare_devicetypeId,
					loid,deviceSerialnumber,servAccount);
		}
		else {
			return dao.countDevBatchRestartQueryInfo(cityId, startTime, endTime,
					curPage_splitPage, num_splitPage, gwType, gwShareCityId,
					gwShareVendorId, gwShare_deviceModelId, gwShare_devicetypeId,
					loid,deviceSerialnumber,servAccount);
		}
	}

	public List<Map> devBatchRestartQueryExcel(String cityId,
			String startTime, String endTime, String gwType,
			String gwShareCityId,	String gwShareVendorId, String gwShareDeviceModelId,
			String gwShareDevicetypeId,String loid,String deviceSerialnumber,String servAccount)
	{
		logger.debug("voiceRegisterDeviceQueryExcel()");
		if (!SYSTEM_ITMS.equals(gwType)) {
			return stbDao.devBatchRestartQueryExcel(cityId, startTime, endTime,
					gwType, gwShareCityId,
					gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId,
					loid,deviceSerialnumber,servAccount);
		}
		else {
			return dao.devBatchRestartQueryExcel(cityId, startTime, endTime,
					gwType, gwShareCityId,
					gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId,
					loid,deviceSerialnumber,servAccount);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> devBatchRestartQueryInfoStat(String cityId,
			String startOpenDate, String endOpenDate, String gwType,
			String vendorId,String deviceModelId,String devicetypeId) 
	{
		if(dao==null){
			dao=new DevBatchRestartQueryDAO();
		}

		List<Map> list = new ArrayList<Map>();
		if (!SYSTEM_ITMS.equals(gwType)) {
			list = stbDao.devBatchRestartQueryInfoStat(cityId,startOpenDate,endOpenDate,gwType,
					vendorId,deviceModelId,devicetypeId);
		}
		else {
			list = dao.devBatchRestartQueryInfoStat(cityId,startOpenDate,endOpenDate,gwType,
					vendorId,deviceModelId,devicetypeId);
		}

		List<Map> infolist=new ArrayList<Map>();
		if(list!=null && !list.isEmpty())
		{
			String vendor_id=null;
			String device_model_id=null;
			String devicetype_id=null;
			
			String nameFix = "";
			if(!SYSTEM_ITMS.equals(gwType)){
				nameFix = "stb_";
				vendorMap=stbDao.getData(nameFix+"tab_vendor","vendor_id","vendor_name");
				vendorAddMap=stbDao.getData(nameFix+"tab_vendor","vendor_id","vendor_add");
				modelMap=stbDao.getData(nameFix+"gw_device_model","device_model_id","device_model");
				versionMap=stbDao.getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
			}
			else {
				vendorMap=dao.getData(nameFix+"tab_vendor","vendor_id","vendor_name");
				vendorAddMap=dao.getData(nameFix+"tab_vendor","vendor_id","vendor_add");
				modelMap=dao.getData(nameFix+"gw_device_model","device_model_id","device_model");
				versionMap=dao.getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
			}

			
			for(Map map:list)
			{
				vendor_id=StringUtil.getStringValue(map,"vendor_id");
				device_model_id=StringUtil.getStringValue(map,"device_model_id");
				devicetype_id=StringUtil.getStringValue(map,"devicetype_id");
				
				Map<String,String> cityDevice = new HashMap<String,String>();
				cityDevice.put("vendor_id", vendor_id);
				String vname=vendorAddMap.get(vendor_id);
				if(StringUtil.IsEmpty(vname)){
					vname=vendorMap.get(vendor_id);
				}
				cityDevice.put("vendor_name", vname);
				cityDevice.put("device_model_id", device_model_id);
				cityDevice.put("device_model", modelMap.get(device_model_id));
				cityDevice.put("devicetype_id", devicetype_id);
				cityDevice.put("softwareversion", versionMap.get(devicetype_id));
				cityDevice.put("numbers", StringUtil.getStringValue(map,"numbers"));
				infolist.add(cityDevice);
				
				vendor_id=null;
				vname=null;
				device_model_id=null;
				devicetype_id=null;
			}
		}
		
		return infolist;
	}

	List<Map> devBatchRestartQueryInfoCity(String cityId,
										   String startOpenDate, String endOpenDate, String gwType,
										   String vendorId,String deviceModelId,String devicetypeId,boolean flag)
	{
		if(dao==null){
			dao=new DevBatchRestartQueryDAO();
		}
		List<Map> list;
		if(flag){
			if(!SYSTEM_ITMS.equals(gwType)){
				list = stbDao.devBatchRestartQueryInfoCity(cityId,startOpenDate,endOpenDate,gwType,
						vendorId,deviceModelId,devicetypeId);
			}
			else {
				list = dao.devBatchRestartQueryInfoCity(cityId,startOpenDate,endOpenDate,gwType,
						vendorId,deviceModelId,devicetypeId);
			}
		}else {
			if(!SYSTEM_ITMS.equals(gwType)){
				list = stbDao.devNotRestartQueryInfoCity(cityId,startOpenDate,endOpenDate,gwType,
						vendorId,deviceModelId,devicetypeId);
			}
			else {
				list = dao.devNotRestartQueryInfoCity(cityId,startOpenDate,endOpenDate,gwType,
						vendorId,deviceModelId,devicetypeId);
			}
		}
		List<Map> infolist=new ArrayList<Map>();
		if(list!=null && !list.isEmpty())
		{
			String vendor_id=null;
			String device_model_id=null;
			String devicetype_id=null;

			String nameFix = "";
			if(!SYSTEM_ITMS.equals(gwType)){
				nameFix = "stb_";
			}
			if(vendorMap == null || vendorMap.size() == 0){
				if(!SYSTEM_ITMS.equals(gwType)){
					vendorMap=stbDao.getData(nameFix+"tab_vendor","vendor_id","vendor_name");
				}
				else {
					vendorMap=dao.getData(nameFix+"tab_vendor","vendor_id","vendor_name");
				}
			}
			if(vendorAddMap == null || vendorAddMap.size() == 0){
				if(!SYSTEM_ITMS.equals(gwType)){
					vendorAddMap=stbDao.getData(nameFix+"tab_vendor","vendor_id","vendor_add");
				}
				else {
					vendorAddMap=dao.getData(nameFix+"tab_vendor","vendor_id","vendor_add");
				}
			}
			if(modelMap == null || modelMap.size() == 0){
				if(!SYSTEM_ITMS.equals(gwType)){
					modelMap=stbDao.getData(nameFix+"gw_device_model","device_model_id","device_model");
				}
				else {
					modelMap=dao.getData(nameFix+"gw_device_model","device_model_id","device_model");
				}
			}
			if(versionMap == null || versionMap.size() == 0){
				if(!SYSTEM_ITMS.equals(gwType)){
					versionMap=stbDao.getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
				}
				else {
					versionMap=dao.getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
				}
			}

			for(Map map:list)
			{
				vendor_id=StringUtil.getStringValue(map,"vendor_id");
				device_model_id=StringUtil.getStringValue(map,"device_model_id");
				devicetype_id=StringUtil.getStringValue(map,"devicetype_id");

				Map<String,String> cityDevice = new HashMap<String,String>();
				cityDevice.put("vendor_id", vendor_id);
				String vname=vendorAddMap.get(vendor_id);
				if(StringUtil.IsEmpty(vname)){
					vname=vendorMap.get(vendor_id);
				}
				cityDevice.put("vendor_name", vname);
				cityDevice.put("device_model_id", device_model_id);
				cityDevice.put("device_model", modelMap.get(device_model_id));
				cityDevice.put("devicetype_id", devicetype_id);
				cityDevice.put("softwareversion", versionMap.get(devicetype_id));
				cityDevice.put("city_id", StringUtil.getStringValue(map,"city_id"));
				cityDevice.put("numbers", StringUtil.getStringValue(map,"numbers"));
				infolist.add(cityDevice);
			}
		}

		return infolist;
	}
	
	@SuppressWarnings("unchecked")
	public String getTableString(String city_id,String startOpenDate1,String endOpenDate1,
			String gwType,String gwShareVendorId,String gwShareDeviceModelId,
			String gwShareDevicetypeId,String startOpenDate,String endOpenDate,String opera)
	{
		List<Map> restartDevMap = devBatchRestartQueryInfoStat(city_id,startOpenDate1,endOpenDate1,
				gwType,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId);


		StringBuffer sbTable = new StringBuffer();
		if(!"toExcel".equals(opera))
		{
			sbTable.append("<table class='listtable' id='listTable'>");
			if(!SYSTEM_ITMS.equals(gwType)){
				sbTable.append("<caption>机顶盒批量重启分类统计 </caption>");
			}else{
				sbTable.append("<caption>光猫批量重启分类统计 </caption>");
			}
		}
		
		if(null == restartDevMap || restartDevMap.isEmpty()){
			sbTable.append("<TR><TD>系统中暂无任何业务!</TD></TR>");
			return sbTable.toString();
		}
		if(Global.NXDX.equals(instArea)){
			//宁夏增加属地维度的统计
			List<Map> restartCityDevMap = devBatchRestartQueryInfoCity(city_id,startOpenDate1,endOpenDate1,
					gwType,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId,true);

			// 统计每个城市下重启的数量
			List<Map> notRestartNumCityMap = new ArrayList<Map>();
			if(!SYSTEM_ITMS.equals(gwType)){
				notRestartNumCityMap = stbDao.devRestartNumByCityId(city_id, startOpenDate1, endOpenDate1, gwType,
						gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId);
			}
			else {
				notRestartNumCityMap = dao.devRestartNumByCityId(city_id, startOpenDate1, endOpenDate1, gwType,
						gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId);
			}
			getTable4NX(sbTable,restartDevMap,gwType,startOpenDate1,endOpenDate1,restartCityDevMap);

			//1、获取省中心下的左右一级子属地
			List<Map<String, String>> cityParentNameList = CityDAO.getNextCityListByCityPid("00");
			// 添加纵向的小计
			sbTable.append("<tr>");
			sbTable.append("<td>" + "" + "</td>");
			sbTable.append("<td>" + "" + "</td>");
			sbTable.append("<td>" + "小计" + "</td>");
			int allNum = 0;
			for (Map<String, String> city : cityParentNameList) {
				String cityIdFirst = city.get("city_id");

				int allNumCity = 0;
				List<String> cityIdList = new ArrayList<String>();
				if ("00".equals(cityIdFirst)) {
					//省中心不加子属地
					cityIdList.add("00");
				}
				else {
					// 其他的要加上所有的子属地的数量
					// 查询地市下的所有地市，包括自己
					cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityIdFirst);
				}

				for (Map map : notRestartNumCityMap) {
					String cityId = StringUtil.getStringValue(map.get("city_id"));
					if (cityIdList.contains(cityId)) {
						int numbers = StringUtil.getIntegerValue(map.get("numbers"));
						allNumCity = allNumCity + numbers;
					}
				}

				allNum = allNum + allNumCity;
				if (allNumCity == 0) {
					sbTable.append("<td>").append(allNumCity).append("</td>");
				}
				else {
					String para = "'" + gwType + "','" + startOpenDate1 + "','" + endOpenDate1
							+ "','" + gwShareVendorId + "','" + gwShareDeviceModelId + "','" + gwShareDevicetypeId + "','" + cityIdFirst + "'";
					sbTable.append("<td><a href=\"javascript:detail4NX(" + para + ");\">"
							+ allNumCity + "</a></td>");
				}

			}

			// 全部数量(对应最后一列小计)
			if (allNum == 0) {
				sbTable.append("<td>").append(allNum).append("</td>");
			}
			else {
				String para = "'" + gwType + "','" + startOpenDate1 + "','" + endOpenDate1
						+ "','" + gwShareVendorId + "','" + gwShareDeviceModelId + "','" + gwShareDevicetypeId + "',''";
				sbTable.append("<td><a href=\"javascript:detail4NX(" + para + ");\">"
						+ allNum + "</a></td>");
			}
			sbTable.append("</tr>");

		}else {
			// 输出表头
			sbTable.append("<TH>厂商</TH><TH>设备型号</TH><TH>版本</TH><TH>数量</TH>");
			// 判断厂商有哪些,存储该厂商对应的型号
			Map<String, Map> vendorMap = new HashMap<String, Map>();
			// 存储同一厂商的数量，用于厂商的行判断
			Map<String,Integer> vendorSizeMap = new HashMap<String,Integer>();
			for (Map map : restartDevMap)
			{
				// 存储该型号对应的版本
				Map<String, Map> devicetypeMap = null;
				String vkey=StringUtil.getStringValue(map.get("vendor_name"))
						+"#"+StringUtil.getStringValue(map.get("vendor_id"));
				if(null == vendorSizeMap.get(vkey) || 0 == vendorSizeMap.get(vkey)){
					vendorSizeMap.put(vkey, 1);
				}else{
					vendorSizeMap.put(vkey,vendorSizeMap.get(vkey)+1);
				}

				if(null == vendorMap.get(vkey)){
					devicetypeMap = new HashMap<String, Map>();
				}else{
					devicetypeMap = vendorMap.get(vkey);
				}

				String key=null;
				// 存储版本和数量
				Map<String, String> typeNumMap = null;
				key=StringUtil.getStringValue(map.get("device_model"))
						+"#"+StringUtil.getStringValue(map.get("vendor_id"))
						+"#"+StringUtil.getStringValue(map.get("device_model_id"));
				if (null == devicetypeMap.get(key)){
					typeNumMap = new HashMap<String, String>();
				}else{
					typeNumMap = (Map) devicetypeMap.get(key);
				}

				// 存储版本+数量map
				//因为数据问题导致软件版本数据重复，特此加ID防止合并
				key=null;
				key=StringUtil.getStringValue(map.get("softwareversion"))
						+"#"+StringUtil.getStringValue(map.get("vendor_id"))
						+"#"+StringUtil.getStringValue(map.get("device_model_id"))
						+"#"+StringUtil.getStringValue(map.get("devicetype_id"));
				typeNumMap.put(key, StringUtil.getStringValue(map.get("numbers")));

				// 存储型号+版本map
				key=null;
				key=StringUtil.getStringValue(map.get("device_model"))
						+"#"+StringUtil.getStringValue(map.get("vendor_id"))
						+"#"+StringUtil.getStringValue(map.get("device_model_id"));
				devicetypeMap.put(key, typeNumMap);
				// 存储厂商+版本map
				vendorMap.put(vkey, devicetypeMap);
				key=null;
			}

			int vendor_i = 0;

			for (Entry<String,Map> entryS : vendorMap.entrySet())
			{
				// 某个厂商size
				vendor_i +=1;
				int vendorsize = entryS.getValue().size();
				Integer vendorIntSize = vendorSizeMap.get(entryS.getKey());
				sbTable.append("<tr>");
				sbTable.append("<td rowspan="+vendorIntSize+">"+entryS.getKey().split("#")[0]+"</td>");
				String vendor_id=entryS.getKey().split("#")[1];

				Map<String, Map> devicetype = entryS.getValue();
				int device_i = 0;
				for (Entry<String,Map> entryd : devicetype.entrySet())
				{
					if(device_i>0){
						sbTable.append("<tr>");
					}
					device_i += 1;
					int devicesize = entryd.getValue().size();
					sbTable.append("<td rowspan="+devicesize+">"+entryd.getKey().split("#")[0]+"</td>");
					String model_id=entryd.getKey().split("#")[2];

					Map<String,String> typeNum = entryd.getValue();
					for (Entry<String,String> entrytype : typeNum.entrySet())
					{
						sbTable.append("<td>"+entrytype.getKey().split("#")[0]+"</td>");
						String deviceType_id=entrytype.getKey().split("#")[3];

						if(Global.NXDX.equals(instArea) && !"0".equals(entrytype.getValue())){
							String para="'"+gwType+"','"+startOpenDate1+"','"+endOpenDate1
									+"','"+vendor_id+"','"+model_id+"','"+deviceType_id+"'";
							sbTable.append("<td><a href=\"javascript:detail("+para+");\">"
									+entrytype.getValue()+"</a></td>");
							para=null;
						}else{
							sbTable.append("<td>"+entrytype.getValue()+"</td>");
						}

						sbTable.append("</tr>");
					}
				}
				if(vendor_i == vendorsize && vendorsize != 1){
					sbTable.append("</tr>");
				}
			}
		}

		if(!"toExcel".equals(opera))
		{
			String url="DevBatchRestartInfoListStatExcel.jsp"
					+ "?gw_type="+gwType
					+ "&city_id="+city_id
					+ "&startOpenDate="+startOpenDate
					+ "&endOpenDate="+endOpenDate
					+ "&gwShare_vendorId="+gwShareVendorId
					+ "&gwShare_deviceModelId="+gwShareDeviceModelId
					+ "&gwShare_devicetypeId="+gwShareDevicetypeId
					+ "&opera=toExcel";
			
			sbTable.append("<tfoot><tr><td colspan=11 align='right'>");
			sbTable.append("<a href='"+url+"' alt='导出当前页数据到Excel中'>导出excel</a>");
			sbTable.append("</td></tr></tfoot>");
		}
		
		sbTable.append("</table>");
		
		return sbTable.toString();
	}

	private void getTable4NX(StringBuffer sbTable,List<Map> restartDevMap,String gwType,String startOpenDate1,String endOpenDate1,List<Map> cityDevStatMap){
		//1、获取省中心下的左右一级子属地
		List<Map<String,String>> cityParentNameList = CityDAO.getNextCityListByCityPid("00");

		//2、组装输出表头
		sbTable.append("<TH>厂商</TH><TH>设备型号</TH><TH>软件版本</TH>");
		for (Map<String,String> value : cityParentNameList) {
			sbTable.append("<TH>").append(value.get("city_name")).append("</TH>");
		}
		sbTable.append("<TH>小计</TH>");

		//3、拼接各个维度的map
		//获取vendor + vendorName value为厂商个数
		Map<String,Integer> vendorNumMap = new HashMap<String, Integer>();
		//获取key为vendorId,vendorName的统计MAP value为设备型号和设备版本统计
		Map<String,Map<String,String>> vendorDeviceTypeMap = new HashMap<String, Map<String, String>>();
		//获取key为 vendorId + modelId+modelName 的统计map value为设备版本统计
		Map<String,Map<String,String>> modelDeviceTypeMap = new HashMap<String, Map<String, String>>();
		//获取key为 vendorId + modelId + devTypeId + softVersion 的统计map
		Map<String,Integer> deviceTypeMap = new HashMap<String, Integer>();
		//属地维度的统计map key 为 vendorId + modelId + devTypeId + cityId
		Map<String,Integer> cityDevMap = new HashMap<String, Integer>();

		for(Map map : restartDevMap){
			String vendorId = StringUtil.getStringValue(map.get("vendor_id"));
			String modelId = StringUtil.getStringValue(map.get("device_model_id"));
			String devTypeId = StringUtil.getStringValue(map.get("devicetype_id"));
			String softVersion = StringUtil.getStringValue(map.get("softwareversion"));
			String modelName = StringUtil.getStringValue(map.get("device_model"));
			String vendorName = StringUtil.getStringValue(map.get("vendor_name"));
			//厂商个数维度
			getVendorNumMap(vendorNumMap,vendorId,vendorName);
			//设备版本维度
			getDevTypeMap(deviceTypeMap, map, vendorId, modelId, devTypeId, softVersion);
			//设备型号维度
			getDevModelMap(modelDeviceTypeMap, vendorId, modelId, devTypeId, modelName,softVersion);
			//厂商维度
			getVendorMap(vendorDeviceTypeMap, vendorId, modelId, modelName, vendorName,devTypeId);
		}
		//属地维度统计map
		for(Map map : cityDevStatMap){
			//属地维度
			String vendorId = StringUtil.getStringValue(map.get("vendor_id"));
			String modelId = StringUtil.getStringValue(map.get("device_model_id"));
			String devTypeId = StringUtil.getStringValue(map.get("devicetype_id"));
			String cityId = StringUtil.getStringValue(map.get("city_id"));
			getCityCountMap(cityDevMap, map, vendorId, modelId, devTypeId, cityId);
		}

		//4、构造表格
		int trIndex = 0;
		for(Map.Entry<String, Map<String,String>> entry : vendorDeviceTypeMap.entrySet()){
			trIndex++;

			// 根据vendorId + '|' + vendorName 获取厂商个数，即所占多少行
			String vendorKey = entry.getKey();
			Integer vendorCount = vendorNumMap.get(vendorKey);

			// 获取该厂商下所有的设备型号集合 modelId,modelName
			Map<String, String> vendorValueMap = entry.getValue();
			String[] vendor = vendorKey.split("\\|");
			String vendorId = vendor[0];
			String vendorName = vendor[1];
			sbTable.append("<tr>");
			sbTable.append("<td rowspan=" + vendorCount + ">" + vendorName+"</td>");
			//拼接设备型号、软件版本和之后的td
			getDevModelTable(sbTable, gwType, startOpenDate1, endOpenDate1, cityParentNameList, modelDeviceTypeMap, deviceTypeMap, cityDevMap, vendorValueMap, vendorId);
			if (trIndex == vendorCount && vendorCount != 1) {
				sbTable.append("</tr>");
			}
		}


	}

	private void getVendorNumMap(Map<String,Integer>vendorNumMap,String vendorId,String vendorName){
		String key = vendorId + '|' + vendorName;
		if(vendorNumMap.get(key) == null){
			vendorNumMap.put(key,1);
		}else {
			vendorNumMap.put(key,vendorNumMap.get(key) + 1);
		}
	}

	private void getDevModelTable(StringBuffer sbTable, String gwType, String startOpenDate1, String endOpenDate1, List<Map<String,String>> cityParentNameList, Map<String, Map<String, String>> modelDeviceTypeMap, Map<String, Integer> deviceTypeMap, Map<String, Integer> cityDevMap, Map<String, String> vendorValueMap, String vendorId) {
		int index = 0;

		// 循环设备型号集合
		for(Map.Entry<String,String> vendorModel : vendorValueMap.entrySet()){
			if(index > 0){
				sbTable.append("<tr>");
			}
			index++;
			String modelId = vendorModel.getKey();
			String modelName = vendorModel.getValue();
			String modelKey = vendorId + '|' + modelId + '|' + modelName;

			// 该设备型号下所有的软件版本
			Map<String,String> modelValueMap = modelDeviceTypeMap.get(modelKey);

			sbTable.append("<td rowspan=" + modelValueMap.size() + ">" + modelName + "</td>");

			//拼接设备软件版本和之后的td
			getDevTypeTable(sbTable, gwType, startOpenDate1, endOpenDate1, cityParentNameList, deviceTypeMap, cityDevMap, vendorId, modelId, modelValueMap);
		}
	}

	private void getDevTypeTable(StringBuffer sbTable, String gwType, String startOpenDate1, String endOpenDate1, List<Map<String,String>> cityParentNameList, Map<String, Integer> deviceTypeMap, Map<String, Integer> cityDevMap, String vendorId, String modelId, Map<String, String> modelValueMap) {
		for(Entry<String,String> devTypeEntry : modelValueMap.entrySet()){
			String devTypeId = devTypeEntry.getKey();
			String softVersion = devTypeEntry.getValue();
			sbTable.append("<td>" + softVersion+"</td>");
			//拼接省份统计部分
			for (Map<String,String> city : cityParentNameList) {
				String cityId = city.get("city_id");
				//获取该cityId下所有的子属地id（包括自己）统计总和
				int cityTotal = 0;
				if("00".equals(cityId)){
					//省中心不加子属地
					cityTotal = getCityCenterTotal(cityDevMap, vendorId, modelId, devTypeId, cityId);
				}
				else {
					// 其他的要加上所有的子属地的数量
					cityTotal = getCityTotal(cityDevMap, vendorId, modelId, devTypeId, cityId, cityTotal);
				}

				if(cityTotal == 0){
					sbTable.append("<td>").append(cityTotal).append("</td>");
				}else {
					String para="'" + gwType + "','" + startOpenDate1 + "','" + endOpenDate1
							+"','" + vendorId +"','" + modelId + "','" + devTypeId + "','" + cityId + "'";
					sbTable.append("<td><a href=\"javascript:detail4NX(" + para + ");\">"
							+ cityTotal + "</a></td>");
				}
			}
			//拼接总计
			String totalKey = vendorId + '|' + modelId + '|' + devTypeId + "|" + softVersion;
			String para = "'" + gwType + "','" + startOpenDate1 + "','" + endOpenDate1
					+"','" + vendorId +"','" + modelId + "','" + devTypeId + "'" + ",''";
			sbTable.append("<td><a href=\"javascript:detail4NX(" + para + ");\">").append(deviceTypeMap.get(totalKey)).append("</td>");
			sbTable.append("</tr>");
		}
	}

	private int getCityTotal(Map<String, Integer> cityDevMap, String vendorId, String modelId, String devTypeId, String cityId, int cityTotal) {
		List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		for(String childCityId : cityIdList){
			String cityKey = vendorId + '|' + modelId + '|' + devTypeId + '|' + childCityId;
			Integer cityCounts = cityDevMap.get(cityKey);
			if(cityCounts != null){
				cityTotal = cityTotal + cityCounts;
			}
		}
		return cityTotal;
	}

	private int getCityCenterTotal(Map<String, Integer> cityDevMap, String vendorId, String modelId, String devTypeId, String cityId) {
		int cityTotal;
		String cityKey = vendorId + '|' + modelId + '|' + devTypeId + '|' + cityId;
		Integer cityCounts = cityDevMap.get(cityKey);
		cityTotal = cityCounts == null ? 0 : cityCounts;
		return cityTotal;
	}

	private void getCityCountMap(Map<String, Integer> cityDevMap, Map map, String vendorId, String modelId, String devTypeId, String cityId) {
		String cityKey = vendorId + '|' + modelId + '|' + devTypeId + '|' + cityId;
		cityDevMap.put(cityKey, StringUtil.getIntegerValue(map.get("numbers")));
	}

	/**
	 * 厂商下所有的设备型号
	 * @param vendorDeviceTypeMap
	 * @param vendorId
	 * @param modelId
	 * @param modelName
	 * @param vendorName
	 * @param devTypeId
	 */
	private void getVendorMap(Map<String, Map<String, String>> vendorDeviceTypeMap, String vendorId, String modelId, String modelName, String vendorName,String devTypeId) {
		String vendorKey = vendorId + '|' + vendorName;
		if(vendorDeviceTypeMap.get(vendorKey) == null){
			Map<String,String> deviceTypeValueMap = new HashMap<String, String>();
			deviceTypeValueMap.put(modelId,modelName);
			vendorDeviceTypeMap.put(vendorKey, deviceTypeValueMap);
		}else {
			Map<String,String> modelValueMap = vendorDeviceTypeMap.get(vendorKey);
			if(modelValueMap.get(modelId) == null){
				modelValueMap.put(modelId,modelName);
			}
		}
	}

	private void getDevModelMap(Map<String, Map<String, String>> modelDeviceTypeMap, String vendorId, String modelId, String devTypeId,String modelName,String softVersion) {
		String modelKey = vendorId + '|' + modelId + '|' + modelName;
		if(modelDeviceTypeMap.get(modelKey) == null){
			Map<String,String> devTypeValueMap = new HashMap<String, String>();
			devTypeValueMap.put(devTypeId,softVersion);
			modelDeviceTypeMap.put(modelKey,devTypeValueMap);
		}else {
			Map<String,String> devTypeValueMap = modelDeviceTypeMap.get(modelKey);
			if(devTypeValueMap.get(devTypeId) == null){
				devTypeValueMap.put(devTypeId,softVersion);
			}
		}
	}

	private void getDevTypeMap(Map<String, Integer> deviceTypeMap, Map map, String vendorId, String modelId, String devTypeId, String softVersion) {
		String devTypeKey = vendorId + '|' + modelId + '|' + devTypeId + "|" + softVersion;
		if(deviceTypeMap.get(devTypeKey) == null){
			deviceTypeMap.put(devTypeKey, StringUtil.getIntegerValue(map.get("numbers")));
		}else {
			deviceTypeMap.put(devTypeKey,deviceTypeMap.get(devTypeKey) + StringUtil.getIntegerValue(map.get("numbers")));
		}
	}

	public List<Map> getDetail(String gwType, String startOpenDate,
			String endOpenDate, String vendorId,String deviceModelId,
			String devicetypeId,int curPage_splitPage,int num_splitPage)
	{
		if(!SYSTEM_ITMS.equals(gwType)){
			return stbDao.getDetail(gwType,startOpenDate,endOpenDate,vendorId,deviceModelId,
					devicetypeId,curPage_splitPage,num_splitPage);
		}
		else {
			return dao.getDetail(gwType,startOpenDate,endOpenDate,vendorId,deviceModelId,
					devicetypeId,curPage_splitPage,num_splitPage);
		}

	}

	public List<Map> getDetail4NX(String gwType, String startOpenDate,
							   String endOpenDate, String vendorId,String deviceModelId,
							   String devicetypeId,int curPage_splitPage,int num_splitPage,String cityId)
	{
		if(!SYSTEM_ITMS.equals(gwType)){
			return stbDao.getDetail4NX(gwType,startOpenDate,endOpenDate,vendorId,deviceModelId,
					devicetypeId,curPage_splitPage,num_splitPage,cityId);
		}
		else {
			return dao.getDetail4NX(gwType,startOpenDate,endOpenDate,vendorId,deviceModelId,
					devicetypeId,curPage_splitPage,num_splitPage,cityId);
		}
	}
	
	public int count(String gwType, String startOpenDate,
			String endOpenDate, String vendorId,String deviceModelId,
			String devicetypeId,int num_splitPage)
	{
		if(!SYSTEM_ITMS.equals(gwType)){
			return stbDao.count(gwType,startOpenDate,endOpenDate,vendorId,
					deviceModelId,devicetypeId,num_splitPage);
		}
		else {
			return dao.count(gwType,startOpenDate,endOpenDate,vendorId,
					deviceModelId,devicetypeId,num_splitPage);
		}
	}

	public int count4NX(String gwType, String startOpenDate,
					 String endOpenDate, String vendorId,String deviceModelId,
					 String devicetypeId,int num_splitPage,String cityId)
	{
		if(!SYSTEM_ITMS.equals(gwType)){
			return stbDao.count4NX(gwType,startOpenDate,endOpenDate,vendorId,
					deviceModelId,devicetypeId,num_splitPage,cityId);
		}
		else {
			return dao.count4NX(gwType,startOpenDate,endOpenDate,vendorId,
					deviceModelId,devicetypeId,num_splitPage,cityId);
		}
	}

	public List<Map> getDetailNoRestart(String gwType, String startOpenDate,
								  String endOpenDate, String vendorId,String deviceModelId,
								  String devicetypeId,int curPage_splitPage,int num_splitPage,String cityId)
	{
		if(!SYSTEM_ITMS.equals(gwType)){
			return stbDao.getNotRestartDetail(gwType,startOpenDate,endOpenDate,vendorId,deviceModelId,
					devicetypeId,curPage_splitPage,num_splitPage,cityId);
		}
		else {
			return dao.getNotRestartDetail(gwType,startOpenDate,endOpenDate,vendorId,deviceModelId,
					devicetypeId,curPage_splitPage,num_splitPage,cityId);
		}
	}

	public int countNoRestart(String gwType, String startOpenDate,
						String endOpenDate, String vendorId,String deviceModelId,
						String devicetypeId,int num_splitPage,String cityId)
	{
		if(!SYSTEM_ITMS.equals(gwType)){
			return stbDao.count4NoStart(gwType,startOpenDate,endOpenDate,vendorId,
					deviceModelId,devicetypeId,num_splitPage,cityId);
		}
		else {
			return dao.count4NoStart(gwType,startOpenDate,endOpenDate,vendorId,
					deviceModelId,devicetypeId,num_splitPage,cityId);
		}

	}
	
	public DevBatchRestartQueryDAO getDao() {
		return dao;
	}

	public void setDao(DevBatchRestartQueryDAO dao) {
		this.dao = dao;
	}

	public DevBatchRestartQueryStbDAO getStbDao() {
		return stbDao;
	}

	public void setStbDao(DevBatchRestartQueryStbDAO stbDao) {
		this.stbDao = stbDao;
	}

	public String getTableString4NX(String city_id, String startOpenDate1, String endOpenDate1,
									String gwType, String gwShareVendorId, String gwShareDeviceModelId,
									String gwShareDevicetypeId, String startOpenDate, String endOpenDate, String opera)
	{
		List<Map> restartDevMap = devNotRestartQueryInfoStat(city_id,startOpenDate1,endOpenDate1,
				gwType,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId);
		//加上属地维度的统计
		List<Map> restartCityDevMap = devBatchRestartQueryInfoCity(city_id,startOpenDate1,endOpenDate1,
				gwType,gwShareVendorId,gwShareDeviceModelId,gwShareDevicetypeId, false);


		StringBuffer sbTable = new StringBuffer();
		if (!"toExcel".equals(opera))
		{
			sbTable.append("<table class='listtable' id='listTable'>");
			if (!SYSTEM_ITMS.equals(gwType)) {
				sbTable.append("<caption>超过30天未重启的机顶盒终端数量统计 </caption>");
			}
			else {
				sbTable.append("<caption>超过30天未重启的用户终端数量统计 </caption>");
			}
		}

		if(null == restartDevMap || restartDevMap.isEmpty()){
			sbTable.append("<TR><TD>系统中暂无任何业务!</TD></TR>");
			return sbTable.toString();
		}

		// 统计每个城市下没有重启的数量
		List<Map> notRestartNumCityMap = new ArrayList<Map>();
		if(!SYSTEM_ITMS.equals(gwType)){
			notRestartNumCityMap = stbDao.devNotRestartNumByCityId(city_id, startOpenDate1, endOpenDate1, gwType,
					gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId);
		}
		else {
			notRestartNumCityMap = dao.devNotRestartNumByCityId(city_id, startOpenDate1, endOpenDate1, gwType,
					gwShareVendorId, gwShareDeviceModelId, gwShareDevicetypeId);
		}

		//宁夏增加属地维度的统计
		getTable4NX(sbTable,restartDevMap,gwType,startOpenDate1,endOpenDate1,restartCityDevMap);

		//1、获取省中心下的左右一级子属地
		List<Map<String,String>> cityParentNameList = CityDAO.getNextCityListByCityPid("00");
		// 添加纵向的小计
		sbTable.append("<tr>");
		sbTable.append("<td>" + "" + "</td>");
		sbTable.append("<td>" + "" + "</td>");
		sbTable.append("<td>" + "小计" + "</td>");
		int allNum = 0;
		for (Map<String, String> city : cityParentNameList) {
			String cityIdFirst = city.get("city_id");

			int allNumCity = 0;
			List<String> cityIdList = new ArrayList<String>();
			if ("00".equals(cityIdFirst)) {
				//省中心不加子属地
				cityIdList.add("00");
			}
			else {
				// 其他的要加上所有的子属地的数量
				// 查询地市下的所有地市，包括自己
				cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityIdFirst);
			}

			for (Map map : notRestartNumCityMap) {
				String cityId = StringUtil.getStringValue(map.get("city_id"));
				if (cityIdList.contains(cityId)) {
					int numbers = StringUtil.getIntegerValue(map.get("numbers"));
					allNumCity = allNumCity + numbers;
				}
			}

			allNum = allNum + allNumCity;


			if (allNumCity == 0) {
				sbTable.append("<td>").append(allNumCity).append("</td>");
			}
			else {
				String para = "'" + gwType + "','" + startOpenDate1 + "','" + endOpenDate1
						+ "','" + gwShareVendorId + "','" + gwShareDeviceModelId + "','" + gwShareDevicetypeId + "','" + cityIdFirst + "'";
				sbTable.append("<td><a href=\"javascript:detail4NX(" + para + ");\">"
						+ allNumCity + "</a></td>");
			}

		}

		// 全部数量(对应最后一列小计)
		if (allNum == 0) {
			sbTable.append("<td>").append(allNum).append("</td>");
		}
		else {
			String para = "'" + gwType + "','" + startOpenDate1 + "','" + endOpenDate1
					+ "','" + gwShareVendorId + "','" + gwShareDeviceModelId + "','" + gwShareDevicetypeId + "',''";
			sbTable.append("<td><a href=\"javascript:detail4NX(" + para + ");\">"
					+ allNum + "</a></td>");
		}
		sbTable.append("</tr>");


		if (!"toExcel".equals(opera))
		{
			String url="DevNotRestartInfoListStatExcel.jsp"
					+ "?gw_type="+gwType
					+ "&city_id="+city_id
					+ "&startOpenDate="+startOpenDate
					+ "&endOpenDate="+endOpenDate
					+ "&gwShare_vendorId="+gwShareVendorId
					+ "&gwShare_deviceModelId="+gwShareDeviceModelId
					+ "&gwShare_devicetypeId="+gwShareDevicetypeId
					+ "&opera=toExcel";

			sbTable.append("<tfoot><tr><td colspan=11 align='right'>");
			sbTable.append("<a href='"+url+"' alt='导出当前页数据到Excel中'>导出excel</a>");
			sbTable.append("</td></tr></tfoot>");
		}

		sbTable.append("</table>");

		return sbTable.toString();
	}

	public List<Map> devNotRestartQueryInfoStat(String cityId,
												  String startOpenDate, String endOpenDate, String gwType,
												  String vendorId,String deviceModelId,String devicetypeId)
	{
		if(dao==null){
			dao=new DevBatchRestartQueryDAO();
		}

		List<Map> list = new ArrayList<Map>();
		if(!SYSTEM_ITMS.equals(gwType)){
			list = stbDao.devNotRestartQueryInfoStat(cityId,startOpenDate,endOpenDate,gwType,
					vendorId,deviceModelId,devicetypeId);
		}
		else {
			list = dao.devNotRestartQueryInfoStat(cityId,startOpenDate,endOpenDate,gwType,
					vendorId,deviceModelId,devicetypeId);
		}
		List<Map> infolist=new ArrayList<Map>();
		if(list!=null && !list.isEmpty())
		{
			String nameFix = "";
			if(!SYSTEM_ITMS.equals(gwType)){
				nameFix = "stb_";
				vendorMap=stbDao.getData(nameFix+"tab_vendor","vendor_id","vendor_name");
				vendorAddMap=stbDao.getData(nameFix+"tab_vendor","vendor_id","vendor_add");
				modelMap=stbDao.getData(nameFix+"gw_device_model","device_model_id","device_model");
				versionMap=stbDao.getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
			}
			else {
				vendorMap=dao.getData(nameFix+"tab_vendor","vendor_id","vendor_name");
				vendorAddMap=dao.getData(nameFix+"tab_vendor","vendor_id","vendor_add");
				modelMap=dao.getData(nameFix+"gw_device_model","device_model_id","device_model");
				versionMap=dao.getData(nameFix+"tab_devicetype_info","devicetype_id","softwareversion");
			}


			for(Map map : list)
			{
				String vendor_id = StringUtil.getStringValue(map,"vendor_id");
				String device_model_id = StringUtil.getStringValue(map,"device_model_id");
				String devicetype_id = StringUtil.getStringValue(map,"devicetype_id");

				Map<String,String> cityDevice = new HashMap<String,String>();
				cityDevice.put("vendor_id", vendor_id);
				String vname = vendorAddMap.get(vendor_id);
				if(StringUtil.IsEmpty(vname)){
					vname=vendorMap.get(vendor_id);
				}
				cityDevice.put("vendor_name", vname);
				cityDevice.put("device_model_id", device_model_id);
				cityDevice.put("device_model", modelMap.get(device_model_id));
				cityDevice.put("devicetype_id", devicetype_id);
				cityDevice.put("softwareversion", versionMap.get(devicetype_id));
				cityDevice.put("city_id",StringUtil.getStringValue(map,"city_id"));
				cityDevice.put("numbers", StringUtil.getStringValue(map,"numbers"));
				infolist.add(cityDevice);
			}
		}

		return infolist;
	}
	
}
