package com.linkage.module.itms.report.act;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.bio.StatisticalLordMGCAddressBIO;
import com.linkage.system.utils.StringUtils;

@SuppressWarnings("serial")
public class StatisticalLordMGCAddressACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(StatisticalLordMGCAddressACT.class);
	
	private Cursor cursor = null;

	private Map fields = null;
	
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private Map session;
	private String city_id = null;

	private String prox_serv = null;

	// 导出属地
	private String cityId = "";

	// 属地列表
	private List<Map<String, String>> cityList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> presetList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> devList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> data;
	private String[] title;
	private String[] column;
	private String fileName;
	private String ajax;
	private int columSize;

	private String htmlList = "";
	private String dataList = "";

	private StatisticalLordMGCAddressBIO bio;

	public String init() {
		logger.debug("MoreBroadbandBusinessACT=>init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}

	public String countMoreBroadbandBusiness() {
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		columSize = cityList.size()+2;
		request.setAttribute("city_id", city_id);
		logger.debug("MoreBroadbandBusinessACT=>countMoreBroadbandBusiness");
		dataList = bio.countMoreBroadbandBusiness(city_id);
		htmlList = bio.getCityName(city_id);
		return "list";
	}
	
	public String getDevListForWbdTerminal() {
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String cityIds = StringUtils.weave(cityIdList);
		request.setAttribute("mycityId", cityIds);
		String mgcqueryurl = LipossGlobals.getLipossProperty("MGCQueryUrl");//查询url
		request.setAttribute("a", mgcqueryurl);
		String mgcpaceurl = LipossGlobals.getLipossProperty("MGCPaceUrl");//进度url
		request.setAttribute("b", mgcpaceurl);
		String mgcdownloadurl = LipossGlobals.getLipossProperty("MGCDownLoadUrl");//下载url
		request.setAttribute("c", mgcdownloadurl);
		
		
		devList = bio.getDeviceListForWBdTerminal(city_id, prox_serv,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getDeviceListForWBdTerminalCount(city_id,
				prox_serv, curPage_splitPage, num_splitPage);
		return "deviceList";
	}
	
	public String getMGCExcel(HttpServletRequest request){
		String city_id = request.getParameter("city_id");
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td >序号</td>");
		sb.append("<td >MGC地址</td>");
		for (int i = 0; i < cityList.size(); i++) {
			sb.append("<td>").append(cityMap.get(cityList.get(i)))
					.append("</td>");
		}
		sb.append("</tr>");
		Map<String, Map<String, String>> resultMap = getResult(city_id);
		if (resultMap.isEmpty()) {
			int row = cityList.size() + 2;
			sb.append("<tr><td colspan=" + row + ">该属地不存在任何MGC</td></tr>");
			return sb.toString();
		}
		int count = 1;
		Iterator<Map.Entry<String, Map<String, String>>> iterator = resultMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Map<String, String>> entry = iterator.next();
			String key = entry.getKey();
			// if (!StringUtil.IsEmpty(key)) {
			sb.append("<tr><td>" + (count++) + "").append("</td>");
			sb.append("<td>" + (key) + "").append("</td>");
			Map<String, String> myCityMap = entry.getValue();
			ArrayList<String> tlist = null;
			for (int j = 0; j < cityList.size(); j++) {
				String cityId = cityList.get(j);
				tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				int num = 0;
				for (int k = 0; k < tlist.size(); k++) {
					if (null != myCityMap.get(tlist.get(k))) {
						num += Long.valueOf(myCityMap.get(tlist.get(k)));
					}
				}
				if (0 != num) {
					sb.append("<td>").append(num).append("</td>");
				} else {
					sb.append("<td>").append("0").append("</td>");
				}
			}
			// } else {
			// continue;
			// }
		}
		sb.append("<tr><td colspan='2'>").append("小计").append("</td>");
		Map<String, String> niMap = getXiaoji(city_id);
		long ninum = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++) {
			ninum = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int j = 0; j < tlist.size(); j++) {
				if (null != niMap && !niMap.isEmpty()) {
					ninum += StringUtil.getLongValue(niMap.get(tlist.get(j)));
				}
			}
			if (0 != ninum) {
				sb.append("<td>").append(ninum).append("</td>");
			} else {
				sb.append("<td>").append("0</td>");
			}
			tlist = null;
		}
		sb.append("</tr>");
		cityList = null;
		return sb.toString();
	
	}

	private Map<String, String> getXiaoji(String city_id) 
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as num ");
		}else{
			sql.append("select a.city_id,count(1) as num ");
		}
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c ");
		sql.append("where a.user_id=b.user_id and b.sip_id=c.sip_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id ");
		
		Map<String, String> map = new HashMap<String, String>();
		cursor = DataSetBean.getCursor(sql.getSQL());
		fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				map.put(StringUtil.getStringValue(fields.get("city_id")),
						StringUtil.getStringValue(fields.get("num")));
				fields = cursor.getNext();
			}
		}
		return map;
	}

	private Map<String, Map<String, String>> getResult(String city_id) 
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,c.prox_serv,count(1) as num ");
		}else{
			sql.append("select a.city_id,c.prox_serv,count(1) as num ");
		}
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c ");
		sql.append("where a.user_id=b.user_id and b.sip_id=c.sip_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		sql.append(" group by c.prox_serv,a.city_id ");
		Map<String, String> cityMap = null;
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
		
		cursor = DataSetBean.getCursor(sql.getSQL());
		fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				String prox_serv = StringUtil.getStringValue(fields.get("prox_serv"));
				String cityId = StringUtil.getStringValue(fields.get("city_id"));
				String num = StringUtil.getStringValue(fields.get("num"));
				if (null == resultMap.get(prox_serv)) {
					cityMap = new HashMap<String, String>();
				}
				cityMap.put(cityId, num);
				resultMap.put(prox_serv, cityMap);
				fields = cursor.getNext();
			}
		}
		return resultMap;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getProx_serv() {
		return prox_serv;
	}

	public void setProx_serv(String prox_serv) {
		this.prox_serv = prox_serv;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getPresetList() {
		return presetList;
	}

	@SuppressWarnings("rawtypes")
	public void setPresetList(List<Map> presetList) {
		this.presetList = presetList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevList() {
		return devList;
	}

	@SuppressWarnings("rawtypes")
	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List<Map> data) {
		this.data = data;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getHtmlList() {
		return htmlList;
	}

	public void setHtmlList(String htmlList) {
		this.htmlList = htmlList;
	}

	public String getDataList() {
		return dataList;
	}

	public void setDataList(String dataList) {
		this.dataList = dataList;
	}

	public int getColumSize() {
		return columSize;
	}

	public void setColumSize(int columSize) {
		this.columSize = columSize;
	}

	public StatisticalLordMGCAddressBIO getBio() {
		return bio;
	}

	public void setBio(StatisticalLordMGCAddressBIO bio) {
		this.bio = bio;
	}

}
