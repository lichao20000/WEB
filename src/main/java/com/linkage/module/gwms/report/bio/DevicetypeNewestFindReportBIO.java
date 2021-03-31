/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.DevicetypeNewestFindReportDAO;
import com.linkage.module.gwms.report.obj.DevicetypeChildOBJ;
import com.linkage.module.gwms.report.obj.DevicetypeNewestFindReportOBJ;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-9-9
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class DevicetypeNewestFindReportBIO {
	
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DevicetypeNewestFindReportBIO.class);
	
	DevicetypeNewestFindReportDAO dao;
	
	/**
	 * @return the dao
	 */
	public DevicetypeNewestFindReportDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(DevicetypeNewestFindReportDAO dao) {
		this.dao = dao;
	}

	/**
	 * 获取属地的下一级
	 * 
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public List getCityList(String cityId){
		
		logger.debug("getCityList(cityId:{})",cityId);
		
		return CityDAO.getNextCityListByCityPid(cityId);
	}
	
	/**
	 * 查询当月指定时间类上报的设备版本的信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param cityTempList
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List getData(long startTime,long endTime,List cityTempList,String cityId,String userCityId,String gw_type){
		
		logger.debug("getData()");
		
		List rsList = new ArrayList();
		
		//取得所有版本的详细信息
		List devicetypeList = dao.getDevicetype(startTime,endTime);
		//取出版本ID
		List<String> typeList = new ArrayList<String>();
		Map<String, Map> mapService = new HashMap<String, Map>();
		
		for(int i=0;i<devicetypeList.size();i++){
			String temp_vendor_id = String.valueOf(((Map)devicetypeList.get(i)).get("vendor_id")).toString();
			String temp_vendor_name = String.valueOf(((Map)devicetypeList.get(i)).get("vendor_add")).toString();
			String temp_device_model = String.valueOf(((Map)devicetypeList.get(i)).get("device_model")).toString();
			String temp_devicetype_id = String.valueOf(((Map)devicetypeList.get(i)).get("devicetype_id")).toString();
			String temp_softwareversion = String.valueOf(((Map)devicetypeList.get(i)).get("softwareversion")).toString();
			String temp_vendor_id_name = temp_vendor_name+"|"+temp_vendor_id;
			typeList.add(temp_devicetype_id);
			
			Map<String, List> modelMap = null;
			if(null==mapService.get(temp_vendor_id_name)){
				modelMap = new HashMap<String, List>();
			}else{
				modelMap = (Map)mapService.get(temp_vendor_id_name);
			}
			List<String> tempdevicetypeList = null;
			if(null==modelMap.get(temp_device_model)){
				tempdevicetypeList = new ArrayList<String>();
			}else{
				tempdevicetypeList = (List) modelMap.get(temp_device_model);
			}
			tempdevicetypeList.add(temp_devicetype_id+"|"+temp_softwareversion);
			modelMap.put(temp_device_model,tempdevicetypeList);
			mapService.put(temp_vendor_id_name,modelMap);
		}
		
		//根据属地查询版本对应的数目
		List deviceList = dao.getdeviceNumByType(typeList,gw_type);
		
		//处理数据
		Map<String,Integer> deviceMap = new HashMap<String,Integer>();
		if(null!=deviceList){
			for(int i=0;i<deviceList.size();i++){
				Map tempMap = (Map)deviceList.get(i);
				String temp_devicetype_id = String.valueOf(tempMap.get("devicetype_id")).toString();
				String temp_city_id = String.valueOf(tempMap.get("city_id")).toString();
				int temp_num = Integer.parseInt(String.valueOf(tempMap.get("num")).toString());
				deviceMap.put(temp_devicetype_id+"_"+temp_city_id,temp_num);
			}
		}
		
		Iterator itVender = mapService.keySet().iterator();
		while (itVender.hasNext()) { 
			
			String key = (String) itVender.next();
			Map valueMap= (Map) mapService.get(key);
			long venderSize = 0;
            List childList = new ArrayList();
            Iterator itDevicetype = valueMap.keySet().iterator();
            while(itDevicetype.hasNext()){
            	
            	String keyModel = (String) itDevicetype.next();
            	List tempDevicetypeList = (List)valueMap.get(keyModel);
            	venderSize = venderSize + tempDevicetypeList.size();
            	DevicetypeChildOBJ typeObj = new DevicetypeChildOBJ();
            	typeObj.setDevice_model(keyModel);
            	typeObj.setChildInt(tempDevicetypeList.size());
            	List num = new ArrayList();
            	for(int i=0;i<tempDevicetypeList.size();i++){
            		
            		String temp_softwareversion[] = ((String)tempDevicetypeList.get(i)).split("\\|");
            		List<String> one = new ArrayList<String>();
            		one.add(temp_softwareversion[1]);
            		long typeTotal = 0;
            		for(int j=0;j<cityTempList.size();j++){
            			
            			String tempCityId = String.valueOf(((Map)cityTempList.get(j)).get("city_id")).toString();
            			
            			if(tempCityId.equals(userCityId)){
            				if(null==deviceMap.get(temp_softwareversion[0]+"_"+tempCityId)){
                				one.add("0");
                			}else{
                				one.add(String.valueOf(deviceMap.get(temp_softwareversion[0]+"_"+tempCityId)));
                			}
            			}else{
            				ArrayList list = CityDAO.getAllNextCityIdsByCityPid(tempCityId);
            				one.add(getCityVendorCount(list,temp_softwareversion[0],deviceMap));
            				list = null;
            			}
            		}
            		if(cityId.equals(userCityId)){
            			for(int k=1;k<one.size();k++){
            				typeTotal = typeTotal + Long.parseLong(one.get(k));
            			}
            			one.add(String.valueOf(typeTotal));
            		}
            		num.add(one);
            	}
            	typeObj.setNum(num);
            	childList.add(typeObj);
            }
            DevicetypeNewestFindReportOBJ vendorObj = new DevicetypeNewestFindReportOBJ();
			vendorObj.setVendor_name(key.split("\\|")[0]);
			vendorObj.setChildInt(venderSize);
			vendorObj.setChildList(childList);
			rsList.add(vendorObj);
		}
		
		return rsList;
	}
	
	/**
	 * 将传入的属地列表中所有数据相加
	 * @param cityAll
	 * @param editionId
	 * @param tmpMap
	 * @return
	 */
	private String getCityVendorCount(ArrayList cityAll,String editionId,Map tmpMap){
		
		long total = 0;
		String tmpCity = "";
		String tmp = "";
		long tmpValue = 0;
		
		Iterator it = cityAll.listIterator();
		
		while (it.hasNext()){
			tmpCity = (String)it.next();
			
			tmp = String.valueOf(tmpMap.get(editionId + "_" + tmpCity));
			
			if (null != tmp && !"".equals(tmp) && !"null".equals(tmp)){
				tmpValue = Long.parseLong(tmp);
				total += tmpValue;
			}
		}
		
		return String.valueOf(total);
	}
}
