package com.linkage.module.gwms.report.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.VoipCountReportBIO;
import com.opensymphony.xwork2.ActionSupport;
/**
 *按厂商统计家庭网关设备
 */

import action.splitpage.splitPageAction;
public class VoipCountReportACT  extends splitPageAction implements SessionAware {
	private static final long serialVersionUID = 1L;
	/** 日志记录*/
	private static Logger logger = LoggerFactory.getLogger(VoipCountReportACT.class);
	private Map<String, Object> session;
	
	private String ajax;
	
	private String gw_type;
	private String cityId;
	private String dataCityId;
	private String protocol;
	private List<Map<String, String>> dataList=new ArrayList<Map<String,String>>();
	
	/**表头*/
	private String[] title;
	/** 导出文件数据列 */
	private String[] column;
	/**导出文件名*/
	private String fileName;
	/** 导出Excel的数据*/
	/**导出*/
	private String returnType;
	
	private List<Map> data = null;
	
	private VoipCountReportBIO bio;
	
	private List<Map<String, String>> cityList = null;
	
	private List<Map> detailList = null;

	public String init() {
		
		if ("00".equals(WebUtil.getCurrentUser().getCityId()))
		{
			cityList = CityDAO.getNextCityListByCityPid(WebUtil.getCurrentUser().getCityId());
		}
		else
		{
			cityList = new ArrayList<Map<String,String>>();
			HashMap<String,String> map = new HashMap<String,String>();
			String localId = CityDAO.getLocationCityIdByCityId(WebUtil.getCurrentUser().getCityId());
			String localName = CityDAO.getCityName(localId);
			map.put("city_name", localName);
			map.put("city_id", localId);
			cityList.add(map);
		}
		return "init";
	}
	
	public String queryDataList() 
	{
		logger.warn("gw_type:"+gw_type);
		logger.warn("cityId:"+cityId);
		if(null==this.cityId || "".equals(this.cityId)){
			UserRes curUser = (UserRes) session.get("curUser");
			this.cityId = curUser.getCityId();
		}
		
		try {
			//获取下级属地
			List<Map<String, String>> list = bio.getCityList(cityId);
			//数据
			List<Map<String, String>> queryData = bio.queryDataList(cityId,gw_type);
			int countSip=0;
			int countH248=0;
			int countTotal=0;
			
			//循环属地
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> cityMap=list.get(i);
				cityMap.put("sip", "0");
				cityMap.put("h248", "0");
				cityMap.put("total", "0");
				String city_id=cityMap.get("city_id");
				ArrayList<String> allCityIds= CityDAO.getAllNextCityIdsByCityPid(city_id);
				//只展示所选属地本级的数据
				if (city_id.equals(cityId)) {
					//循环数据
					for (int j = 0; j < queryData.size(); j++) {
						Map<String, String> map=queryData.get(j);
						String city_id_data=map.get("city_id_data");
						if (city_id.equals(city_id_data)) {
							cityMap.putAll(map);
							countSip+=Integer.parseInt(String.valueOf(map.get("sip"))==null?"0":String.valueOf(map.get("sip")));
							countH248+=Integer.parseInt(String.valueOf(map.get("h248"))==null?"0":String.valueOf(map.get("h248")));
							countTotal+=Integer.parseInt(String.valueOf(map.get("total"))==null?"0":String.valueOf(map.get("total")));
							continue;
						}
					}
				}else {
					//循环数据
					for (int j = 0; j < queryData.size(); j++) {
						Map<String, String> map=queryData.get(j);
						String city_id_data=map.get("city_id_data");
						int	sipNum=Integer.parseInt(String.valueOf(map.get("sip")));
						int h248Num=Integer.parseInt(String.valueOf(map.get("h248")));
						int totalNum=Integer.parseInt(String.valueOf(map.get("total")));
						
						if (allCityIds.contains(city_id_data)) {
							cityMap.put("sip", sipNum+Integer.parseInt(cityMap.get("sip")==null?"0":cityMap.get("sip"))+"");
							cityMap.put("h248", h248Num+Integer.parseInt(cityMap.get("h248")==null?"0":cityMap.get("h248"))+"");
							cityMap.put("total", totalNum+Integer.parseInt(cityMap.get("total")==null?"0":cityMap.get("total"))+"");
							countSip+=sipNum;
							countH248+=h248Num;
							countTotal+=totalNum;
						}
					}
				}
			}
			Map<String, String> totalMap=new HashMap<String, String>();
			totalMap.put("city_id", "-1");
			totalMap.put("city_name", "总计");
			totalMap.put("sip", countSip+"");
			totalMap.put("h248", countH248+"");
			totalMap.put("total", countTotal+"");
			list.add(totalMap);
			dataList.addAll(list);
			logger.warn(dataList.toString());
		} catch (Exception e) {
			dataList.clear();
			e.printStackTrace();
		}
		
		if ("excel".equals(returnType))
		{
			fileName = "语音业务按协议统计报表";
			title = new String[3];
			column = new String[3];
			
			title[0] = "属地";
			title[1] = "H248";
			title[2] = "SIP";
			
			column[0] = "city_name";
			column[1] = "h248";
			column[2] = "sip";
			data = new ArrayList();
			for(int i=0;i<this.dataList.size();i++){
				data.add((Map)dataList.get(i));
			}
			return "excel";
		}
		
		return "queryDataList";
	}
	
	
	public String querydetailList(){
		
		if(null==this.cityId || "".equals(this.cityId)){
			UserRes curUser = (UserRes) session.get("curUser");
			this.cityId = curUser.getCityId();
		}
		logger.warn("gw_type:"+gw_type);
		logger.warn("cityId:"+cityId);
		logger.warn("dataCityId:"+dataCityId);
		
		detailList = bio.querydetailList(gw_type,cityId,protocol,dataCityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.querydetailListCount(gw_type,cityId,protocol,dataCityId,
				curPage_splitPage, num_splitPage);
		return "queryDetailList";
	}
	
	public String queryDetailForExcel(){
			
		if(null==this.cityId || "".equals(this.cityId)){
			UserRes curUser = (UserRes) session.get("curUser");
			this.cityId = curUser.getCityId();
		}
		logger.warn("gw_type:"+gw_type);
		logger.warn("cityId:"+cityId);
		logger.warn("dataCityId:"+dataCityId);
		
		data = bio.queryDetailForExcel(gw_type,cityId,protocol,dataCityId);
		fileName = "语音业务按协议统计详细";
		title = new String[] { "地市", "区县", "LOID", "语音类型", "语音号码", "语音口", "语音VLAN", "IP", "掩码", "网关", "MGC1", "MGC2" };
		column = new String[] { "scity_name", "city_name", "loid", "protocol", "voip_phone", "voip_port", "vlanid", "ipaddress", "ipmask", "gateway", "prox_serv", "stand_prox_serv" };
		return "excel";
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public VoipCountReportBIO getBio() {
		return bio;
	}

	public void setBio(VoipCountReportBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, String>> dataList) {
		this.dataList = dataList;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public List<Map> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map> detailList) {
		this.detailList = detailList;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}


	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDataCityId() {
		return dataCityId;
	}

	public void setDataCityId(String dataCityId) {
		this.dataCityId = dataCityId;
	}
	
}
