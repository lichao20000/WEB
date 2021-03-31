package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.GetNodeBatchConfigServ;
import com.linkage.litms.LipossGlobals;
import com.linkage.commons.ftp.FtpClient;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

public class GetNodeBatchConfigActionImpl extends splitPageAction implements
GetNodeBatchConfigAction, SessionAware{
	private static Logger logger = LoggerFactory
			.getLogger(GetNodeBatchConfigActionImpl.class);
	// bio
	private GetNodeBatchConfigServ bio;

	/** request取登陆帐号使用 * */
	private String ajax = null;
	/** 区分ITMS和BBMS的功能 */
	private String gw_type = "";
	private String deviceIds;
	/** 参数节点路径 */
	private String paramNodePath;
	private String param;

	private String dpi;
	private String device_id;
	private String oui;
	private String device_serialnumber;
	private String loid;
	private String gather_path;
	private String file_name;
	private String city_id;
	private Map session;
	private Map<String, String> cityMap = null;
	// 定制人
	private String customId = "";
	// 开始时间
	private String starttime = "";
	// 结束时间
	private String endtime = "";
	// 文件名
	private String fileName = "";
	// 查询记录结果
	private List<Map> rlist = null;
	// 下载文件名
	private String nameDowmload;

	private String checkRepeatname;



	private String resultfile;

	/** HttpServletResponse */
	private HttpServletResponse response;

	public String init()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		return "init";
	}

	public String getNodeBatchList()
	{
		rlist = bio.getFileOperRecordList(curPage_splitPage, num_splitPage, customId, fileName, starttime, endtime);
		maxPage_splitPage = bio.getNodeBatchCount(curPage_splitPage, num_splitPage, customId, fileName, starttime, endtime);
		return "nodeRecordlist";
	}

	/**
	 * 批量修改参数节点
	 * 
	 * @return
	 */
	public String getConfig() {
		ajax = "操作成功";
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();
		long add_time = System.currentTimeMillis()/1000;
		int recordNum1 = bio.recordTask(add_time, gather_path, file_name, acc_oid);
		if(recordNum1 != 1){
			ajax = "操作失败";
		}
		List<String> device_idlist = new ArrayList<String>();
		List<String> ouilist = new ArrayList<String>();
		List<String> device_serialnumberlist = new ArrayList<String>();
		List<String> loidlist = new ArrayList<String>();
		List<String> city_idlist = new ArrayList<String>();
		if (device_id.contains(",")) // 多个设备
		{
			String[] device_idStr = device_id.split(",");
			String[] ouiStr = oui.split(",");
			String[] device_serialnumberStr = device_serialnumber.split(",");
			String[] city_idStr = city_id.split(",");
			String[] loidStr;
			
			//若为设备序列号则用户账号设为空字符串
			if(!"".equals(loid)){
				loidStr = loid.split(",");
			}else{
				loidStr = new String[device_idStr.length];
				for(int i =0;i<loidStr.length;i++){
					loidStr[i]="";
				}
			}
			for (int i = 0; i < device_idStr.length; i ++) {
				device_idlist.add(device_idStr[i]);
				ouilist.add(ouiStr[i]);
				device_serialnumberlist.add(device_serialnumberStr[i]);
				city_idlist.add(city_idStr[i]);
				loidlist.add(loidStr[i]);
			}
		}else{
			device_idlist.add(device_id);
			ouilist.add(oui);
			device_serialnumberlist.add(device_serialnumber);
			city_idlist.add(city_id);
			loidlist.add(loid);
		}
		int recordNum2 = bio.recordDevs(add_time, device_idlist, ouilist, device_serialnumberlist, loidlist, gather_path, city_idlist);
		if(recordNum2 == 0){
			ajax = "操作失败";
		}

		return "ajax";

	}


	public String checkRepeat() {
		ajax = String.valueOf(bio.checkRepeatName(checkRepeatname));
		return "ajax";
	}

	/**
	 * 查询策略表中已作的数量，超过一定数值就不做了
	 * 
	 * @return
	 * @add by zhuzhengdong 2016-7-19
	 */
	public String queryCustomNum() {
		ajax = String.valueOf(bio.queryCustomNum());
		return "ajax";
	}
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}




	public GetNodeBatchConfigServ getBio()
	{
		return bio;
	}


	public void setBio(GetNodeBatchConfigServ bio)
	{
		this.bio = bio;
	}


	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	public String getParamNodePath() {
		return paramNodePath;
	}

	public void setParamNodePath(String paramNodePath) {
		this.paramNodePath = paramNodePath;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDpi() {
		return dpi;
	}

	public void setDpi(String dpi) {
		this.dpi = dpi;
	}


	public String getDevice_id()
	{
		return device_id;
	}



	public String getGather_path()
	{
		return gather_path;
	}


	public void setGather_path(String gather_path)
	{
		this.gather_path = gather_path;
	}


	public String getFile_name()
	{
		return file_name;
	}


	public void setFile_name(String file_name)
	{
		this.file_name = file_name;
	}


	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}


	public String getOui()
	{
		return oui;
	}


	public void setOui(String oui)
	{
		this.oui = oui;
	}


	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}


	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}


	public String getLoid()
	{
		return loid;
	}


	public void setLoid(String loid)
	{
		this.loid = loid;
	}


	public Map getSession()
	{
		return session;
	}


	public void setSession(Map session)
	{
		this.session = session;
	}


	public String getCity_id()
	{
		return city_id;
	}


	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}


	public String getStarttime()
	{
		return starttime;
	}


	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}


	public String getEndtime()
	{
		return endtime;
	}


	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}


	public String getCustomId()
	{
		return customId;
	}


	public void setCustomId(String customId)
	{
		this.customId = customId;
	}


	public String getFileName()
	{
		return fileName;
	}


	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	public List<Map> getRlist()
	{
		return rlist;
	}


	public void setRlist(List<Map> rlist)
	{
		this.rlist = rlist;
	}




	public String getNameDowmload()
	{
		return nameDowmload;
	}


	public void setNameDowmload(String nameDowmload)
	{
		this.nameDowmload = nameDowmload;
	}

	public String getResultfile()
	{
		return resultfile;
	}


	public void setResultfile(String resultfile)
	{
		this.resultfile = resultfile;
	}


	public String getCheckRepeatname()
	{
		return checkRepeatname;
	}


	public void setCheckRepeatname(String checkRepeatname)
	{
		this.checkRepeatname = checkRepeatname;
	}


	public HttpServletResponse getResponse()
	{
		return response;
	}


	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}




}
