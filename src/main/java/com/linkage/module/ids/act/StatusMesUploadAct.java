package com.linkage.module.ids.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.ids.bio.StatusMesUploadBio;
import com.linkage.module.ids.util.WSClientUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author chendong (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * 
 */
public class StatusMesUploadAct extends ActionSupport implements
		ServletRequestAware {

	private static Logger logger = LoggerFactory
			.getLogger(StatusMesUploadAct.class);

	private HttpServletRequest request;

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public StatusMesUploadBio getBio() {
		return bio;
	}

	public void setBio(StatusMesUploadBio bio) {
		this.bio = bio;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getTimelist() {
		return timelist;
	}

	public void setTimelist(String timelist) {
		this.timelist = timelist;
	}

	public String getServerurl() {
		return serverurl;
	}

	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}

	public String getParalist() {
		return paralist;
	}

	public void setParalist(String paralist) {
		this.paralist = paralist;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTftp_port() {
		return tftp_port;
	}

	public void setTftp_port(String tftp_port) {
		this.tftp_port = tftp_port;
	}

	public String getAcc_oid() {
		return acc_oid;
	}

	public void setAcc_oid(String acc_oid) {
		this.acc_oid = acc_oid;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	

	public String getExcute_type() {
		return excute_type;
	}

	public void setExcute_type(String excute_type) {
		this.excute_type = excute_type;
	}


	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public List getIdsList() {
		return IdsList;
	}

	public void setIdsList(List idsList) {
		IdsList = idsList;
	}
	
	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getGwShare_deviceModelId() {
		return gwShare_deviceModelId;
	}

	public void setGwShare_deviceModelId(String gwShare_deviceModelId) {
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	public String getGwShare_devicetypeId() {
		return gwShare_devicetypeId;
	}

	public void setGwShare_devicetypeId(String gwShare_devicetypeId) {
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	public String getGwShare_onlineStatus() {
		return gwShare_onlineStatus;
	}

	public void setGwShare_onlineStatus(String gwShare_onlineStatus) {
		this.gwShare_onlineStatus = gwShare_onlineStatus;
	}

	public String getGwShare_bindType() {
		return gwShare_bindType;
	}

	public void setGwShare_bindType(String gwShare_bindType) {
		this.gwShare_bindType = gwShare_bindType;
	}

	public String getGwShare_deviceSerialnumber() {
		return gwShare_deviceSerialnumber;
	}

	public void setGwShare_deviceSerialnumber(String gwShare_deviceSerialnumber) {
		this.gwShare_deviceSerialnumber = gwShare_deviceSerialnumber;
	}


	private StatusMesUploadBio bio;

	private String add_time;

	private String enable;

	private String timelist;

	private String serverurl;

	private String paralist;

	private String port;

	private String tftp_port;

	private String acc_oid;

	private String gwShare_fileName;

	private String ajax;
	
	private String excute_type;
	
	private String deviceSN;
	
	private List IdsList;
	
	private String gwShare_cityId;
	
	private String gwShare_vendorId;
	
	private String gwShare_deviceModelId;
	
	private String gwShare_devicetypeId;
	
	private String gwShare_onlineStatus;
	
	private String gwShare_bindType;
	
	private String gwShare_deviceSerialnumber;

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub

		logger.debug("batchUp fileName=" + gwShare_fileName + "timelist="
				+ timelist + "serverurl=" + serverurl + "tftp_port="
				+ tftp_port + "excute_type="+excute_type+"deviceSN="+deviceSN+"paralist="+paralist);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		acc_oid = StringUtil.getStringValue(curUser.getUser().getId());
		List<String> deviceList = new ArrayList<String>();
		long currTime = new Date().getTime() / 1000L;
		//为null就转换成""
		paralist = paralist==null?"":paralist;
		paralist = paralist.replace(" ", "");
		//单台设备导入
		if ("0".equals(excute_type))
		{
			deviceList.add(deviceSN);
		}else if("1".equals(excute_type)){
			//批量文件导入
			String fileName_ = gwShare_fileName.substring(
					gwShare_fileName.length() - 3, gwShare_fileName.length());
			try {
				if("txt".equals(fileName_)){
					deviceList = getImportDataByTXT(gwShare_fileName);
				}else{
					deviceList = getImportDataByXLS(gwShare_fileName);
				}
				logger.warn("********deviceList" + deviceList.toString());
			} catch (FileNotFoundException e) {
				logger.warn("e="+e.getMessage());
				logger.warn("{}文件没找到!", fileName_);
				this.ajax = "文件没找到!";
				return "init";
			} catch (IOException e) {
				logger.warn("{}文件解析出错！", fileName_);
				this.ajax = "文件解析出错！";
				return "init";
			} catch (Exception e) {
				logger.warn("{}文件解析出错！", fileName_);
				this.ajax = "文件解析出错！";
				return "init";
			}
		} else if ("2".equals(excute_type)){
			if(null!=gwShare_cityId){
				gwShare_cityId = gwShare_cityId.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId = gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId = gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId = gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus = gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType = gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber = gwShare_deviceSerialnumber.trim();
			}
			
			int total = queryDeviceListCount(); 
			
			logger.warn("设备数量：total[{}] ", total);
//			if (total > 1000)
//			{
//				ajax = "设备数量超过1000，请重新选择条件！";
//				return "init";
//			}
			
			if (total == 0)
			{
				ajax = "没有查询到设备！";
				return "init";
			}
			ArrayList<HashMap<String,String>> resultList = queryDeviceList();
			
			if(null!=resultList){
				if(resultList.size()>1000){
//					ajax = "设备数量超过1000，请重新选择条件！";
//					return "init";
					ajax = "单次定制设备数量不可超过1000条！还有"+(total-1000)+"条没有定制";
				}
				for(int i=0;i<resultList.size();i++){
					if(i<1000){
						HashMap<String,String> map = resultList.get(i);
						if(null!=map){
							deviceList.add(map.get("device_serialnumber"));
						}
					}
				}
//				for(HashMap<String,String> map : resultList){
//					if(null!=map){
//						deviceList.add(map.get("device_serialnumber"));
//					}
//				}
			}
		}
		if (deviceList.size() > 1000)
		{
			ajax = "导入的设备数量超过1000，请重新导入！";
			return "init";
		}
		
		if (deviceList.size() == 0)
		{
			ajax = "没有查询到设备！";
			return "init";
		}
		
		bio.importReportTask(acc_oid,currTime,enable,timelist,serverurl,paralist,tftp_port);
		bio.insertTaskDev(currTime,deviceList);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version='1.0' encoding='GBK'?>");
		buffer.append("<root>");
		buffer.append("<CmdID>123456789012345</CmdID>");
		buffer.append("<CmdType> CX_01</CmdType>");
		buffer.append("<ClientType>5</ClientType>");
		buffer.append("<Param>");
		buffer.append("<taskId>").append(currTime).append("</taskId>");
		buffer.append(" <period>").append(timelist).append("</period>");
		buffer.append("<fileServerIp>").append(serverurl)
				.append("</fileServerIp>");
		buffer.append("<fileServerPort>").append(tftp_port)
				.append("</fileServerPort>");
		buffer.append("<enable>").append(enable).append("</enable>");
		buffer.append("<paramList>").append(paralist).append("</paramList>");
		buffer.append("<devs>");
		for (String deviceSN : deviceList) {
			buffer.append("<dev>").append(deviceSN).append("</dev>");
		}
		buffer.append("</devs>");
		buffer.append("</Param>");
		buffer.append("</root>");
		
		
		logger.warn("发送的参数="+buffer.toString());
	    String url = LipossGlobals.getLipossProperty("IDSServiceUri");
		String resultString = WSClientUtil.callItmsService(url, buffer.toString(),
				"diagnosticEnable");
		logger.warn("接收的参数为="+resultString);
		String taskid = "";
		String deviceSn ="";
		String result = "";
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(resultString));
			Element root2 = document.getRootElement();
			List<Element> elements = root2.elements();
			Element CmdID = elements.get(0);
			Element RstCode = elements.get(1); // RstCode
			Element RstMsg = elements.get(2);
			Element taskId = elements.get(3);
			Element devs = elements.get(4);
			if (!"CmdID".equals(CmdID.getName()) || !"RstCode".equals(RstCode.getName())
					|| !"RstMsg".equals(RstMsg.getName())
					|| !"devs".equals(devs.getName()))
			{
				logger.error("xml格式异常");
				ajax = "定制失败";
				return "init";
			}
			else
			{
				taskid = taskId.getTextTrim();
				List<Element> devList = devs.elements();
				
				for (Element dev : devList)
				{
					deviceSn = dev.getTextTrim();
					result = dev.attributeValue("result");
					bio.updateTaskDev(taskid,deviceSn,result);
				}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		IdsList = bio.getQueryStatusList(currTime);
		
		// 更新 tab_gw_device_open(已开启状态信息上报功能设备表)
		int updateResult = bio.updateOpenedDevices(deviceList, enable);
		if(updateResult>0){
			logger.warn("更新 tab_gw_device_open(已开启状态信息上报功能设备表)成功");
		}else{
			logger.warn("更新 tab_gw_device_open(已开启状态信息上报功能设备表)失败");
		}
		if(null==ajax||!ajax.contains("单次定制设备数量不可超过1000条")){
			ajax = "定制成功";
		}
		logger.warn("定制结果: "+ajax);
		return "init";
	}
	
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);
			
			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			//int columeCount = ws.getColumns();
			
			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
			
			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
			}
		}
		f.delete();
		f = null;
		return list;
}
	
	
	/**
	 * 瑙ｆ瀽鏂囦欢锛坱xt鏍煎紡锛�
	 * 
	 * @param fileName 鏂囦欢鍚�
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		String line = in.readLine();
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}
	
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
	
	/**
	 * 查询设备数量
	 * 
	 * @return
	 */
	public int queryDeviceListCount() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		long areaId = curUser.getAreaId();
		
		if (null==gwShare_cityId || gwShare_cityId.trim().length()==0 || "-1".equals(gwShare_cityId.trim()) || "00".equals(gwShare_cityId.trim())) {
			this.gwShare_cityId = curUser.getCityId();
		}
		
		int total = bio.getDeviceListCount("1", gwShare_cityId,	gwShare_onlineStatus, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
		
		return total;
	}
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String,String>> queryDeviceList() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
//		long areaId = curUser.getAreaId();
		if (null==gwShare_cityId || gwShare_cityId.trim().length()==0 || "-1".equals(gwShare_cityId.trim()) || "00".equals(gwShare_cityId.trim())) {
			this.gwShare_cityId = curUser.getCityId();
		}

		ArrayList<HashMap<String,String>> deviceList = bio.getDeviceList("1", gwShare_cityId, gwShare_onlineStatus, 
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
		
		return deviceList;
	}

}
