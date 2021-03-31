/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.bio.interf.I_SheetFailureReasonBIO;
import com.linkage.module.gwms.report.dao.interf.I_SheetFailureReasonDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category bio.report
 * 
 */
@SuppressWarnings("unchecked")
public class SheetFailureReasonBIO implements I_SheetFailureReasonBIO {

	private Logger log = LoggerFactory.getLogger(SheetFailureReasonBIO.class);
	
	/**
	 * 注入Dao
	 */
	I_SheetFailureReasonDAO sheetFailureReasonDAO;

	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {
		return sheetFailureReasonDAO.getChildCity(cityId);
	}

	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String[][] getReportData(String cityId, long startDate, long endDate,String bookparam) {

		// 取出子一级属地
		List cityList = sheetFailureReasonDAO.getChildCity(cityId);
		
		//获取所有的失败码
		List faultCodeList = new ArrayList();
		List list = sheetFailureReasonDAO.getFaultCode();
		Map codeMap = new HashMap();
		for(int i=0;i<list.size();i++){
			Map one = (Map)list.get(i);
			codeMap.put(String.valueOf(one.get("fault_code")), one.get("fault_desc"));
		}
		
		if(null!=bookparam && !"".equals(bookparam)){
			String tempParam[] = bookparam.split("\\$"); 
			for(int i=0;i<tempParam.length;i++){
				Map one = new HashMap();
				one.put("fault_code", tempParam[i]);
				one.put("fault_desc", codeMap.get(tempParam[i]));
				faultCodeList.add(one);
			}
		}else{
			faultCodeList = list;
		}
		
		//失败码的个数
		int faultInt = faultCodeList.size();
		
		//返回数据
		String[][] dataArr = new String[faultInt+2][cityList.size()+1];
		
		// 求合计数组
		int sum[] = new int[cityList.size()];
		
		//初始化值
		for (int i = 0; i < cityList.size(); i++) {
			sum[i] = 0;
		}
		for (int i = 1; i < faultInt+2; i++) {
			for (int j = 1; j < cityList.size() + 1; j++) {
				dataArr[i][j] = "0";
			}
		}
		
		//失败原因码
		String[] reaseStr = new String[faultInt];
		
		for(int i=0;i<faultInt;i++){
			Map one = (Map)faultCodeList.get(i);
			String faultCode = String.valueOf(one.get("fault_code"));
			String faultDesc = String.valueOf(one.get("fault_desc"));
			reaseStr[i] = faultCode;
			if(Integer.parseInt(faultCode)>1){
				dataArr[i+1][0] =faultDesc+"("+faultCode+")";
			}else{
				dataArr[i+1][0] = faultDesc;
			}
		}
		
		//旮旯
		dataArr[0][0] = "原因\\属地";
		//最后一项【合计】
		dataArr[faultInt+1][0] = "合计";		

		// 初始化属地返回值
		for (int i = 0; i < cityList.size(); i++) {
			Map tempCityMap = (Map) cityList.get(i);
			dataArr[0][i+1] = String.valueOf(tempCityMap.get("city_name"))
					.toString();
		}

		// 查出所有属地，以便求子属地
		Map<String, String> cityAllMap = getCityPC();

		for (int i = 0; i < cityList.size(); i++) {

			Map oneCityGetMap = (Map) cityList.get(i);
			String temp_cityId = String.valueOf(oneCityGetMap.get("city_id"))
					.toString();

			List<String> childCityList = null;
			List dataFrom = null;

			log.debug("temp_cityId:" + temp_cityId);

			if ("00".equals(temp_cityId)) {
				childCityList = new ArrayList<String>();
				childCityList.add(temp_cityId);
				dataFrom = sheetFailureReasonDAO.getDevNum(childCityList,
						startDate, endDate);

			} else {

				childCityList = getAllChild(cityAllMap, temp_cityId);
				childCityList.add(temp_cityId);
				dataFrom = sheetFailureReasonDAO.getDevNum(childCityList,
						startDate, endDate);

			}

			for (int j = 0; j < dataFrom.size(); j++) {

				Map oneDataFrom = (Map) dataFrom.get(j);

				String temp_serv_package_name = String.valueOf(
						oneDataFrom.get("fault_code")).toString();
				String temp_num = String.valueOf(oneDataFrom.get("num"))
						.toString();

				for (int k = 0; k <faultInt; k++) {
					if(temp_serv_package_name.equals(reaseStr[k])){
						dataArr[k+1][i+1] = temp_num;
						sum[i] = sum[i] + Integer.parseInt(temp_num);
					}
				}
			}
		}
		
		/**
		 * 将合计数据合并
		 */
		for (int i = 0; i < cityList.size(); i++) {
			dataArr[faultInt+1][i+1] = String.valueOf(sum[i]).toString() ;
		}
		
		return dataArr;
	}

	/**
	 * 获取属地父子关系
	 * 
	 * @return
	 */
	public Map<String, String> getCityPC() {

		Map<String, String> cityPCMap = new HashMap<String, String>();
		List cityList = sheetFailureReasonDAO.getAllCity();
		for (int i = 0; i < cityList.size(); i++) {
			Map one = (Map) cityList.get(i);
			cityPCMap.put(String.valueOf(one.get("city_id")).toString(), String
					.valueOf(one.get("parent_id")).toString());
		}

		return cityPCMap;

	}

	/**
	 * @category 查询子节点，不包含自己
	 * 
	 * @param cityMap
	 * @param cityId
	 * @return
	 */
	public List<String> getAllChild(Map<String, String> cityMap, String cityId) {

		List<String> childList = new ArrayList<String>();

		Set<String> citySet = cityMap.keySet();
		for (String strCityId : citySet) {
			String strPid = cityMap.get(strCityId);
			if (cityId.equals(strPid)) {
				childList.add(strCityId);
			}
		}

		if (childList.size() > 1) {
			for (int i = 0; i < childList.size(); i++) {
				childList.addAll(getAllChild(cityMap, childList.get(i)));
			}
		}

		return childList;
	}
	
	/**
	 * 查询所有的错误码
	 * 
	 * @return
	 */
	public List getFaultCode(){
		
		return sheetFailureReasonDAO.getFaultCode();
	}
	

	/**
	 * @return the sheetFailureReasonDAO
	 */
	public I_SheetFailureReasonDAO getSheetFailureReasonDAO() {
		return sheetFailureReasonDAO;
	}

	/**
	 * @param sheetFailureReasonDAO the sheetFailureReasonDAO to set
	 */
	public void setSheetFailureReasonDAO(
			I_SheetFailureReasonDAO sheetFailureReasonDAO) {
		this.sheetFailureReasonDAO = sheetFailureReasonDAO;
	}

}
