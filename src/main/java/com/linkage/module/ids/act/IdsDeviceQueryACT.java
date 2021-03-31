package com.linkage.module.ids.act;

import action.splitpage.splitPageAction;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.ids.bio.IdsDeviceQueryBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-17
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class IdsDeviceQueryACT extends splitPageAction implements ServletRequestAware
{
	/**
	 *  serial
	 */
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(IdsDeviceQueryACT.class);
	
	HttpServletRequest request = null;
	/** 查询模式  */
	private String gwShare_queryField;
	/** 查询参数  */
	private String gwShare_queryParam;
	/** 针对江西新的http测速 **/
	private String idsShare_queryType;
	private String idsShare_queryField;
	private String idsShare_queryParam;
	
	private List deviceList = null;
	
	//查询结果的合计条数
	private int total = 0;
	
	private String gwShare_msg = null;
	
	private IdsDeviceQueryBIO bio = null;
	
	private Map<String,String> devMap = null;
	private List<Map<String,String>> date = new ArrayList<Map<String,String>>();
	
	private String ajax;
	@Override
	public String execute() throws Exception
	{
		// TODO Auto-generated method stub
		return super.execute();
	}
	
	public String queryIdsDevice(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();

		if(Global.HBLT.equals(Global.instAreaShortName)
				||Global.JLLT.equals(Global.instAreaShortName)
				||Global.SXLT.equals(Global.instAreaShortName)
				||Global.JXLT.equals(Global.instAreaShortName)
				||Global.AHLT.equals(Global.instAreaShortName)
				||Global.NXLT.equals(Global.instAreaShortName) 
				||Global.ZJLT.equals(Global.instAreaShortName)){
			date = bio.queryList(idsShare_queryField, idsShare_queryParam, city_id);
			return "getDevice";
		}
		if(Global.JXDX.equals(Global.instAreaShortName) && !StringUtil.IsEmpty(idsShare_queryType) && "httpSpeed".equals(idsShare_queryType)){
			ajax = bio.queryDevList(idsShare_queryField, idsShare_queryParam, city_id);
			return "ajax";
		}
		if (Global.SDDX.equals(Global.instAreaShortName)) {
			date = bio.queryListForSDDX(idsShare_queryField, idsShare_queryParam);
			
			if (date == null || date.isEmpty()) {
				ajax = "1#查询设备信息出错";
				return "ajax";
			}
			StringBuffer sb = new StringBuffer();
			sb.append(StringUtil.getStringValue(date.get(0), "device_serialnumber", "")).append("#");
			sb.append(StringUtil.getStringValue(date.get(0), "oui", "")).append("#");
			sb.append("").append("#");
			sb.append("").append("#");
			sb.append(StringUtil.getStringValue(date.get(0), "city_id", "")).append("#");
			sb.append(StringUtil.getStringValue(date.get(0), "city_name", "")).append("#");
			ajax = "0#" + sb.toString();
			return "ajax";
		}
		ajax = bio.queryIdsDevice(idsShare_queryField, idsShare_queryParam, city_id);
		return "ajax";
	}
	
	public String queryIdsDeviceVoiceDialJllt(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		ajax = bio.queryIdsDevice(idsShare_queryField, idsShare_queryParam, city_id);
		return "ajax";
	}
	
	public String queryIdsWANDetail(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		ajax = bio.queryWanDetail(idsShare_queryField, idsShare_queryParam, city_id);
		return "ajax";
	}
	
	public String queryPPPOE(){
		ajax = bio.queryPPPOE(idsShare_queryParam);
		return "ajax";
	}
	/**
	 *       安徽电信下载拨测
			* @return
	 */
	public String queryIdsWANDetailForAH(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		ajax = bio.queryWanDetailForAH(idsShare_queryField, idsShare_queryParam, city_id);
		return "ajax";
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public IdsDeviceQueryBIO getBio()
	{
		return bio;
	}

	
	public void setBio(IdsDeviceQueryBIO bio)
	{
		this.bio = bio;
	}

	
	public String getGwShare_msg()
	{
		return gwShare_msg;
	}

	
	public void setGwShare_msg(String gwShare_msg)
	{
		this.gwShare_msg = gwShare_msg;
	}

	
	public String getGwShare_queryField()
	{
		return gwShare_queryField;
	}

	
	public void setGwShare_queryField(String gwShare_queryField)
	{
		this.gwShare_queryField = gwShare_queryField;
	}

	
	public String getGwShare_queryParam()
	{
		return gwShare_queryParam;
	}

	
	public void setGwShare_queryParam(String gwShare_queryParam)
	{
		this.gwShare_queryParam = gwShare_queryParam;
	}

	
	public String getIdsShare_queryField()
	{
		return idsShare_queryField;
	}

	
	public void setIdsShare_queryField(String idsShare_queryField)
	{
		this.idsShare_queryField = idsShare_queryField;
	}

	
	public String getIdsShare_queryParam()
	{
		return idsShare_queryParam;
	}

	
	public void setIdsShare_queryParam(String idsShare_queryParam)
	{
		this.idsShare_queryParam = idsShare_queryParam;
	}

	
	public List getDeviceList()
	{
		return deviceList;
	}

	
	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	
	public Map<String, String> getDevMap()
	{
		return devMap;
	}

	
	public void setDevMap(Map<String, String> devMap)
	{
		this.devMap = devMap;
	}

	public List<Map<String, String>> getDate() {
		return date;
	}

	public void setDate(List<Map<String, String>> date) {
		this.date = date;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


	public String getIdsShare_queryType()
	{
		return idsShare_queryType;
	}

	
	public void setIdsShare_queryType(String idsShare_queryType)
	{
		this.idsShare_queryType = idsShare_queryType;
	}

	
}
