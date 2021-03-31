package bio.report;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.report.StateDevUserDao;

/**
 * @author Jason(3412)
 * @date 2008-11-13
 */
public class StateDevUserBio {

	private StateDevUserDao stateDao;

	/**
	 * 获取设备按属地统计的结果集
	 * 
	 * @param city_id
	 * @author Jason(3412)
	 * @date 2008-11-13
	 * @return List
	 */
	public String getStateByCity(String gw_type, String strStartTime, String strEndTime, String paramCityId){
		StringBuffer strResult = new StringBuffer(); 
		long longStartTime = 0L,longEndTime = 0L;
		if(null == strStartTime || "".equals(strStartTime)){
			longStartTime = 0;
		}else{
			longStartTime = new DateTimeUtil(strStartTime).getLongTime();
		}
		
		if(null == strEndTime || "".equals(strEndTime)){
			longEndTime = 0;
		}else{
			longEndTime = new DateTimeUtil(strEndTime).getLongTime();
		}
		//把本级属地加入
		int iDev = 0, iUser = 0;
		int iAdslUser = 0, iLanUser = 0, iEponUser = 0;
		String tmpCityId = paramCityId;
		String tmpCityName = "";
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		tmpCityName = cityMap.get(tmpCityId);
		cityMap = null;
		if (LipossGlobals.isXJDX()) {
			iDev = stateDao.getDeviceCount(longStartTime, longEndTime, tmpCityId);
		}else{
			iDev = stateDao.getDeviceCountByYN(longStartTime, longEndTime, tmpCityId);
		}
		iUser = stateDao.getCountUserByCity(gw_type, longStartTime, longEndTime, tmpCityId);
		iAdslUser = stateDao.getCountAccessTypeUserByCity(gw_type,longStartTime, longEndTime, tmpCityId, "ADSL");
		iLanUser = stateDao.getCountAccessTypeUserByCity(gw_type,longStartTime, longEndTime, tmpCityId, "LAN");
		iEponUser = stateDao.getCountAccessTypeUserByCity(gw_type,longStartTime, longEndTime, tmpCityId, "EPON");
		strResult.append(paramCityId);
		strResult.append("|");
		strResult.append(tmpCityName);
		strResult.append("|");
		strResult.append(iDev);
		strResult.append("|");
		strResult.append(iUser);
		strResult.append("|");
		strResult.append(iAdslUser);
		strResult.append("|");
		strResult.append(iLanUser);
		strResult.append("|");
		strResult.append(iEponUser);
		strResult.append("|");
		if(iUser == 0){
			strResult.append("-");
		}else{
			strResult.append(new DecimalFormat("###.##").format(iDev*100.0/iUser) + "%");
		}
		//获取下级属地加入
		List citylist = stateDao.getNextCityList(paramCityId);
		if(citylist != null){
			int citySize = citylist.size();
			for(int i = 0; i < citySize; i++){
				tmpCityId = ((Map)citylist.get(i)).get("city_id").toString();
				tmpCityName = ((Map)citylist.get(i)).get("city_name").toString();
				if (LipossGlobals.isXJDX()) {
					iDev = stateDao.getDeviceCount(longStartTime, longEndTime, tmpCityId);
				}else{
					iDev = stateDao.getDeviceCountByYN(longStartTime, longEndTime, tmpCityId);
				}
				iUser = stateDao.getCountUserByCity(gw_type, longStartTime, longEndTime, tmpCityId);
				iAdslUser = stateDao.getCountAccessTypeUserByCity(gw_type,longStartTime, longEndTime, tmpCityId, "ADSL");
				iLanUser = stateDao.getCountAccessTypeUserByCity(gw_type,longStartTime, longEndTime, tmpCityId, "LAN");
				iEponUser = stateDao.getCountAccessTypeUserByCity(gw_type,longStartTime, longEndTime, tmpCityId, "EPON");
				strResult.append("|||");
				strResult.append(tmpCityId);
				strResult.append("|");
				strResult.append(tmpCityName);
				strResult.append("|");
				strResult.append(iDev);
				strResult.append("|");
				strResult.append(iUser);
				strResult.append("|");
				strResult.append(iAdslUser);
				strResult.append("|");
				strResult.append(iLanUser);
				strResult.append("|");
				strResult.append(iEponUser);
				strResult.append("|");
				if(iUser == 0){
					strResult.append("-");
				}else{
					strResult.append(new DecimalFormat("###.##").format(iDev*100.0/iUser) + "%");
				}
			}
		}
		citylist = null;
		
		//logger.debug(strResult);
		return strResult.toString();
	}

	/**
	 * 
	 * @param gw_type
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getStateUser(String gw_type,String cityId,String startTime,String endTime, String accessType){
		
		if("2".equals(gw_type)){
			return stateDao.getBBMSStateUser(cityId, startTime, endTime, accessType);
		}else{
			return stateDao.getITMSStateUser(cityId, startTime, endTime);
		}
	}
	
	public void setStateDao(StateDevUserDao stateDao) {
		this.stateDao = stateDao;
	}
}
