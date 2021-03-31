package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.dao.tabquery.CityOfficeZoneDAO;
import com.linkage.module.gwms.dao.tabquery.HgwCustDAO;
import com.linkage.module.gwms.dao.tabquery.OfficeDAO;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-9-28
 */
public class HgwcustManageBIO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(HgwcustManageBIO.class);

	private CityOfficeZoneDAO cityOfficeZoneDao;
	private HgwCustDAO hgwCustDao;
	
	private UserInstReleaseBIO userInstReleaseBIO = new UserInstReleaseBIO();

	
	/**
	 * 根据属地ID获取局向的List<Map<officeId,officeName>>
	 * 
	 * @param 属地ID
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return List<Map>
	 */
	public List<Map> getOfficeList(String cityId) {
		logger.debug(" getOfficeList({})", cityId);
		List<Map> officeIdNameMapList = null;
		List<String> officeList = null;
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if (null != cityList && false == cityList.isEmpty()) {
			officeList = new ArrayList<String>();
			Map<String, List<String>> cityOfficeMap = cityOfficeZoneDao
					.getCityIdOfficeIds();
			if(cityOfficeMap!=null){
				for (String city : cityList) {     
					List<String> tmpList = null;
					if (null != (tmpList = cityOfficeMap.get(city))) {
						officeList.addAll(tmpList);
					}
				}
			}		
			if (false == officeList.isEmpty()) {
				Map offIdNameMap = OfficeDAO.getInstance().getOfficeIdNameMap();
				if (null != offIdNameMap && false == offIdNameMap.isEmpty()) {
					officeIdNameMapList = new ArrayList<Map>();
					for (String officeId : officeList) {
						Map tmap = new HashMap();
						tmap.put("office_id", officeId);
						tmap.put("office_name", offIdNameMap.get(officeId));
						officeIdNameMapList.add(tmap);
					}
				}
			} else {
				logger.warn("officeList is null");
			}
		} else {
			logger.warn("cityList is null");
		}

		cityList = null;
		return officeIdNameMapList;
	}

	
	/**
	 * 增加或编辑用户信息
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return int
	 */
	public int manageHgwcust(HgwCustObj hgwCustObj, int isAdd){
		logger.debug("manageHgwcust({},{})", hgwCustObj, isAdd);
		int iret = -1;
		if(null == hgwCustObj || StringUtil.IsEmpty(hgwCustObj.getUsername())){
			logger.warn("hgwCustObj is null or username is empty");
			
		}else{
			if(isAdd == 1){
				//Add
				iret = hgwCustDao.saveCust(hgwCustObj);
				
			}else{
				//Edit
				iret = hgwCustDao.updateCust(hgwCustObj);
				
				//通知资源绑定模块重新加载用户
				String msg = userInstReleaseBIO.itmsUpdateUser(hgwCustObj.getUsername());
				logger.info("hgwCustObj update:" + msg);
			}
		}
		return iret;
	}

	
	
	/**
	 * 根据user_id获取家庭网关用户对象
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return HgwCustObj
	 */
	public HgwCustObj getHgwcust(String userId,String gw_type){
		logger.debug("getHgwcust({})", userId);
		return hgwCustDao.getUserInfo(userId,gw_type);
	}
	
	
	
	/**
	 * 根据所有的终端
	 * 
	 * @param 
	 * @author 
	 * @date 
	 * @return list
	 */
public List	getTypeNameList(){
	return hgwCustDao.getTypeNameList();
	
}
	
	/**
	 * 检查用户表中是否已经存在该用户， 存在返回1，否则返回-1
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return int 存在返回1，否则返回-1
	 */
	public int checkUser(String username){
		logger.debug("checkUser({})", username);
		return hgwCustDao.checkUser(username);
	}
	
	

	/** DAO setter methods */
	
	public void setCityOfficeZoneDao(CityOfficeZoneDAO cityOfficeZoneDao) {
		this.cityOfficeZoneDao = cityOfficeZoneDao;
	}


	public void setHgwCustDao(HgwCustDAO hgwCustDao) {
		this.hgwCustDao = hgwCustDao;
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getOfficeById(String officeId)
	{
		return cityOfficeZoneDao.getOfficeById(officeId);
	}


	public List<Map<String, String>> getOfficeListByName(String cityId, String officeName)
	{
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		cityId = CityDAO.getLocationCityIdByCityId(cityId);
		cityList.add(cityId);
		return cityOfficeZoneDao.getOfficeListByName(cityList,officeName);
	}
	
	/**
	 * 获取List<Map<name,value>>的下拉列表Select标签；
	 * 
	 * @param name为
	 *            <select name; value
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return String
	 */
	public String getHtmlSelect(List someList, String name, String value,
			String text, String onclickMethod) {
		logger.debug("getHtmlSelect({},{},{},{},{})", new Object[] {
				someList, name, value, text, onclickMethod });
		StringBuffer htmlStr = new StringBuffer();
		if (null == onclickMethod) {
			// 表示不触发任何方法
			htmlStr.append("<SELECT NAME=" + name + " CLASS=bk >");
		} else {
			htmlStr.append("<SELECT NAME=" + name
					+ " CLASS=bk onchange=showChild('" + onclickMethod + "')>");
		}
		
		if (someList == null || someList.isEmpty()) {
			htmlStr.append("<OPTION VALUE='xuanze'>==此项没有记录==</OPTION>");
			logger.debug("someList is empty");
		} else {
			htmlStr.append("<OPTION VALUE='xuanze'>==请选择==</OPTION>");
			int size = someList.size();
			for (int i = 0; i < size; i++) {
				Map tmap = (Map) someList.get(i);
				if(tmap != null && false == tmap.isEmpty()) {
					String tmp = StringUtil.getStringValue(tmap.get(value));
					String txt = StringUtil.getStringValue(tmap.get(text));
					htmlStr.append("<OPTION VALUE='" + tmp + "'>--" + txt
							+ "--</OPTION>");
				}
			}
		}

		htmlStr.append("</SELECT>");
		return htmlStr.toString();
	}
	
}
