package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.TerminalVersionNormDAO;

public class TerminalVersionNormBIO {
	private static Logger logger = LoggerFactory
			.getLogger(TerminalVersionNormBIO.class);
	private TerminalVersionNormDAO terminalVersionNormDAO;
    /**
     * 查询终端版本规范率
     * @param vendorId 
     * @param deviceModelId
     * @param cityId
     * @param starttime
     * @param endtime
     * @return
     */
	public List<Map> queryResultList(String vendorId, String deviceModelId,
			String cityId, String accessType, String userType, String starttime, String endtime,String gw_type,String isActive) {
		logger.debug("queryResultList({},{},{},{},{},{},{},{})", new Object[] {
				vendorId, deviceModelId, cityId,accessType,userType, starttime, endtime });
		// 用于返回
		List<Map> dataList = new ArrayList<Map>();

		List<Map> tempList = terminalVersionNormDAO.queryAllResult(vendorId,
				deviceModelId, cityId,accessType,userType ,starttime, endtime,gw_type,isActive);
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);

		if (null != cityList && cityList.size() > 0) {

			String city_id = "";
			List cCityID = null;
			Map<String, Object> tempMap = null;
			long normNum = 0l;   //注意是0和字母L
			long totalNum = 0l;
			long notNormNum = 0l;
			for (int i = 0; i < cityList.size(); i++) {

				city_id = cityList.get(i);
				//所有子属地ID(包括自己)
				cCityID = CityDAO.getAllNextCityIdsByCityPid(city_id);
				tempMap = new HashMap<String, Object>();
				normNum = 0;
				totalNum = 0;
				notNormNum = 0;
				if (null != tempList && tempList.size() > 0) {
				   String cityID ="";
				   String pCityID = "";
				   String gCityID = "";
					for (int j = 0; j < tempList.size(); j++) {
						cityID = StringUtil.getStringValue(tempList.get(j).get("city_id"));
						for(int k = 0 ; k < cCityID.size(); k++){
						// 如果结果中的cityID和city_id相等或者是它的上一级和city_id相等就将该总数计算出来
							if (cityID.equals(cCityID.get(k))) {
	
								totalNum += StringUtil.getLongValue(tempList.get(j)
										.get("num"));
								// 如果是is_check为1则该版本为规范版本
								if (1 == StringUtil.getLongValue(tempList.get(j)
										.get("is_check"))) {
	
									normNum += StringUtil.getLongValue(tempList.get(
											j).get("num"));
								}
							}
						}
					}
					notNormNum = totalNum - normNum;
					tempMap.put("cityId", city_id);
					tempMap.put("cityName", cityMap.get(city_id));
					tempMap.put("normNum", normNum);
					tempMap.put("notNormNum", notNormNum);
					tempMap.put("normRate", percent(normNum, totalNum));
					tempMap.put("totalNum", totalNum);
				} else {
					// 当查询的结果为空时，所有值赋值0
					notNormNum = totalNum - normNum;
					tempMap.put("cityId", city_id);
					tempMap.put("cityName", cityMap.get(city_id));
					tempMap.put("normNum", normNum);
					tempMap.put("notNormNum", notNormNum);
					tempMap.put("totalNum", totalNum);
					tempMap.put("normRate", percent(normNum, totalNum));
				}
				dataList.add(tempMap);
			}
		}
		return dataList;
	}

	/**
	 * 计算百分比
	 * 
	 * @param p1
	 *            分子
	 * @param p2
	 *            分母
	 * @return
	 */
	public String percent(long p1, long p2) {

		logger.debug("percent({},{})", new Object[] { p1, p2 });

		double p3;
		if (p2 == 0) {
			return "N/A";
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}
   /**
    * 查询 设备信息
    * @param flag 当flag=1时为规范版本，2时为不规范版本，3时查询全部
    * @param vendorId
    * @param deviceModelId
    * @param cityId
    * @param starttime
    * @param endtime
    * @param curPage_splitPage
    * @param num_splitPage
    * @return
    */
	public List<Map> queryDeviceList(String flag, String vendorId,
			String deviceModelId, String cityId, String gw_type, String accessType,String userType,String starttime,
			String endtime,String isActive, int curPage_splitPage, int num_splitPage) {
		logger.debug("queryResultList({},{},{},{},{},{},{},{},{},{})", new Object[] {
				flag, vendorId, deviceModelId, cityId,accessType,userType, starttime, endtime });

		return terminalVersionNormDAO.queryDeviceResult(flag,
				vendorId, deviceModelId, cityId,gw_type, accessType,userType,starttime, endtime,isActive,
				curPage_splitPage, num_splitPage);
	}
	/**
	 * 查询 总页数
	 * @param flag flag 当flag=1时为规范版本，2时为不规范版本，3时查询全部
	 * @param vendorId
	 * @param deviceModelId
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDeviceListCount(String flag, String vendorId,
			String deviceModelId, String cityId,String gw_type, String accessType,String userType, String starttime1,
			String endtime1,String isActive,int curPage_splitPage,int num_splitPage) {

		logger.debug("queryDeviceListCount({},{},{},{},{},{},{},{},{},{})", new Object[] {
				flag, vendorId, deviceModelId, cityId,accessType,userType, starttime1, endtime1 });
		return terminalVersionNormDAO.getDeviceListCount(flag, cityId,gw_type,accessType,userType,
				starttime1, endtime1,isActive, vendorId, deviceModelId,
				curPage_splitPage, num_splitPage);
	}
	/**
	 *
	 * @param flag flag 当flag=1时为规范版本，2时为不规范版本，3时查询全部
	 * @param vendorId
	 * @param deviceModelId
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> queryDeviceExcel(String flag,String vendorId, String deviceModelId,
			String cityId,String accessType,String userType, String starttime1, String endtime1,String gw_type,String isActive) {
		logger.debug("queryDeviceExcel({},{},{},{},{},{},{},{},{},{})", new Object[] {
				flag, vendorId, deviceModelId, cityId,accessType,userType, starttime1, endtime1 });
		return terminalVersionNormDAO.getDeviceExcel(flag, vendorId, deviceModelId, cityId, accessType,userType,starttime1, endtime1,gw_type,isActive);
	}
	public TerminalVersionNormDAO getTerminalVersionNormDAO() {
		return terminalVersionNormDAO;
	}

	public void setTerminalVersionNormDAO(
			TerminalVersionNormDAO terminalVersionNormDAO) {
		this.terminalVersionNormDAO = terminalVersionNormDAO;
	}
}
