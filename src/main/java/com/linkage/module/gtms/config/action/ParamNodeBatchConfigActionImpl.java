package com.linkage.module.gtms.config.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.ParamNodeBatchConfigServ;
import com.linkage.module.gwms.Global;

public class ParamNodeBatchConfigActionImpl  extends splitPageAction implements ParamNodeBatchConfigAction,SessionAware, ServletResponseAware 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ParamNodeBatchConfigActionImpl.class);
	
	private static  String WifiChannel= "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.Channel";
	
	private static  String WifiSendPower= "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Powerlevel";
	
	private static  String WifiFaxModel= "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.FaxT38.Enable";
	
	private HttpServletRequest request;
	
	private ParamNodeBatchConfigServ bio;

	private String ajax = null;
	/** 区分ITMS和BBMS的功能 */
	private String gw_type = "";
	private String deviceIds;
	/** 参数节点路径 */
	private String paramNodePath;
	/** 参数值 */
	private String paramValue;
	/** 参数类型 */
	private String paramType;
	
	private String param;

	private String dpi;

	private String file_name;

	private String username;

	private String cityIds;
	
	private String caseDownload;

	private Map session;
	// 属地
	private String cityId = "00";
	private long userId;
	// 查询记录结果
	private List<Map> rlist = null;
	// 定制人
	private String customId = "";
	// 开始时间
	private String starttime = "";
	// 结束时间
	private String endtime = "";
	// 文件名
	private String file_Name = "";
	private String task_id;
	private String fileType;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private List<Map> data;
	private HttpServletResponse response;
	
	private String instArea=Global.instAreaShortName;
	private String gwShare_queryType = "";
	private String device_id = "";
	private String gwShare_nextCityId = "";
	private String gwShare_cityId = "";
	private String gwShare_vendorId = "";
	private String gwShare_deviceModelId = "";
	private String gwShare_devicetypeId = "";
	private String gwShare_bindType = "";
	private String gwShare_fileName = "";
	private String template_id = "";
	private String isBind = "";
	private String task_name = "";
	private String dotype;
	private String donow;
	private String expire_time_start = "";
	private String expire_time_end = "";
	private String expire_date_start = "";
	private String expire_date_end = "";
	
	public String init(){
		return "init";
	}
	
	public String initBatchSetTemplate(){
		DateTimeUtil dt = new DateTimeUtil();
		this.expire_time_start = "00:00:00";
		this.expire_time_end = "23:59:59";
		expire_date_start = dt.getYYYY_MM() + "-01";
		expire_date_end = dt.getYYYY_MM_DD();
		if("4".equals(gw_type)){
			logger.warn("initBatchSetTemplateStb");
			return "initBatchSetTemplateStb";
		}
		else{
			return "initBatchSetTemplate";
		}
	}
	
	
	//宁夏修改时钟同步
	public String init1(){
		return "init1";
	}
	
	/**
	 * 批量修改参数节点
	 * @return
	 */
 	public String doConfigAll() {
		logger.debug("doConfigAll({},{},{},{})", new Object[] { deviceIds,
				paramNodePath, paramValue, paramType });
		try {
			// 业务id
			String serviceId = "";
			if ("true".equals(dpi)) {
				serviceId = "117";
			} else {
				serviceId = "116";
			}
			String[] paramNodePaths = paramNodePath.split(",");
			String[] paramValues = paramValue.split(",");
			String[] paramTypes = paramType.split(",");
			for (int i = 0; i < paramNodePaths.length; i++)
			{
				logger.warn(paramNodePaths[i]);
			}
			if(Global.NXDX.equals(Global.instAreaShortName))
			{
				if(!doCheckParam(paramNodePaths,paramValues))
				{
					return "ajax";
				}
				
			}
			
			
			int len = paramNodePaths.length;
			String[] paramArr = new String[len - 1];
			for (int i = 0; i < len - 1; i++) {
				paramArr[i] = paramNodePaths[i + 1] + "ailk!@#"
						+ paramValues[i + 1] + "ailk!@#" + paramTypes[i + 1];
			}
			if (true == StringUtil.IsEmpty(deviceIds)) {
				logger.debug("任务中没有设备");
				ajax = "任务中没有设备";
			}
			// 直接传deviceId数组调配置模块接口
			else if (!"0".equals(deviceIds)) {
				logger.warn("批量参数配置小于50，直接传deviceId数组调配置模块接口");
				String[] deviceId_array = null;
				deviceId_array = deviceIds.split(",");
				ajax = bio.doConfigAll(deviceId_array, serviceId, paramArr,gw_type);
			} else {// 调用后台corba接口
				logger.warn("直接把SQL传给配置模块做批量参数修改,param=" + param);
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				ajax = bio.doConfigAll(
						new String[] {matchSQL.replace("[", "\'")},serviceId,paramArr,gw_type);
			}
		} catch (Exception e) {
			logger.error("doConfigAll err:"+e);
			e.printStackTrace();
			return "ajax";
		}

		return "ajax";
	}
 	
 	
 	public String addTemplateTask() {
		logger.warn("ParamNodeBatchConfigActionImpl=>addTemplateTask()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		logger.warn("acc_oid="+curUser.getUser().getId());
		
		if(null!=gwShare_queryType ){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
			this.gwShare_cityId = gwShare_nextCityId.trim();
		}
		
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			
			//int res = bio.addTask(template_id, "3",task_name,device_id,gwShare_fileName,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId,gwShare_bindType);
			int res = bio.addTask(template_id, "3",task_name,"",gwShare_fileName,"","","","","",dotype,curUser.getUser().getId(),starttime,endtime,donow);
			ajax = "0";
			if(res>0){
				ajax = "1";
			}
		}
		else if("2".equals(gwShare_queryType)){
			int res = bio.addTask(template_id, "2",task_name,"","",gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId,gwShare_bindType,dotype,curUser.getUser().getId(),starttime,endtime,donow);
			ajax = "0";
			if(res>0){
				ajax = "1";
			}
		}
		else if("1".equals(gwShare_queryType)){
			int res = bio.addTask(template_id, "1",task_name,device_id,"","","","","","",dotype,curUser.getUser().getId(),starttime,endtime,donow);
			ajax = "0";
			if(res>0){
				ajax = "1";
			}
		}
		else{
			ajax = "0";
		}
		return "ajax";
	}
 	
 	
 	/**
 	 * 机顶盒定制任务，入stb_tab_batchSetTemplate_task表
 	 * @return
 	 */
 	public String addTemplateTaskStb() {
		logger.warn("ParamNodeBatchConfigActionImpl=>addTemplateTask()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		logger.warn("acc_oid="+curUser.getUser().getId());
		
		if(null!=gwShare_queryType ){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
			this.gwShare_cityId = gwShare_nextCityId.trim();
		}
		
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			
			//int res = bio.addTask(template_id, "3",task_name,device_id,gwShare_fileName,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId,gwShare_bindType);
			int res = bio.addTaskStb(template_id, "3",task_name,"",gwShare_fileName,"","","","","",dotype,curUser.getUser().getId(),starttime,endtime,donow);
			ajax = "0";
			if(res>0){
				ajax = "1";
			}
		}
		else if("2".equals(gwShare_queryType)){
			int res = bio.addTaskStb(template_id, "2",task_name,"","",gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId,gwShare_bindType,dotype,curUser.getUser().getId(),starttime,endtime,donow);
			ajax = "0";
			if(res>0){
				ajax = "1";
			}
		}
		else if("1".equals(gwShare_queryType)){
			int res = bio.addTaskStb(template_id, "1",task_name,device_id,"","","","","","",dotype,curUser.getUser().getId(),starttime,endtime,donow);
			ajax = "0";
			if(res>0){
				ajax = "1";
			}
		}
		else{
			ajax = "0";
		}
		return "ajax";
	}
 	
 	
 	
 	/**
	 * 机顶盒-批量修改参数节点
	 * @return
	 */
 	public String doConfigAllStb() {
		logger.debug("doConfigAllStb({},{},{},{})", new Object[] { deviceIds,
				paramNodePath, paramValue, paramType });
		try {
			// 业务id
			String serviceId = "";
			if ("true".equals(dpi)) {
				serviceId = "117";
			} else {
				serviceId = "116";
			}
			String[] paramNodePaths = paramNodePath.split(",");
			String[] paramValues = paramValue.split(",");
			String[] paramTypes = paramType.split(",");
			int len = paramNodePaths.length;
			String[] paramArr = new String[len - 1];
			for (int i = 0; i < len - 1; i++) {
				paramArr[i] = paramNodePaths[i + 1] + "ailk!@#"
						+ paramValues[i + 1] + "ailk!@#" + paramTypes[i + 1];
			}
			if (true == StringUtil.IsEmpty(deviceIds)) {
				logger.debug("任务中没有设备");
				ajax = "任务中没有设备";
			}
			// 直接传deviceId数组调配置模块接口
			else if (!"0".equals(deviceIds)) {
				logger.warn("批量参数配置小于50，直接传deviceId数组调配置模块接口");
				String[] deviceId_array = null;
				deviceId_array = deviceIds.split(",");
				ajax = bio.doConfigAllStb(deviceId_array, serviceId, paramArr,gw_type);
			} else {// 调用后台corba接口
				logger.warn("直接把SQL传给配置模块做批量参数修改,param=" + param);
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				ajax = bio.doConfigAllStb(
						new String[] {matchSQL.replace("[", "\'")},serviceId,paramArr,gw_type);
			}
		} catch (Exception e) {
			logger.error("doConfigAll err:"+e);
			e.printStackTrace();
			return "ajax";
		}

		return "ajax";
	}

	/**
	 * 下载模板文件
	 * @return
	 */
	public String downModle()
	{
		if (!StringUtil.IsEmpty(caseDownload))
		{
			String storePath = LipossGlobals.getLipossProperty("uploaddir");
			if ("0".equals(caseDownload)){
				bio.download(storePath + "/批量配置参数节点 XLS模板.xls",response);
			}else{
				bio.download(storePath + "/批量配置参数节点 TXT模板.txt",response);
			}
			return null;
		}
		return null;
	}
	
	public String download()
	{
		data = this.getBio().getDao().getFileMsg(task_id);
		String storePath = LipossGlobals.getLipossProperty("uploaddir");
		if("txt".equals(fileType)){
			String strFileName = storePath+"/批量配置参数节点（灵活配置）.txt"; 
			String result = bio.deleteFile(strFileName);
			if("1".equals(result)){
				bio.saveListToTxt((List<Map>)data, strFileName);
				bio.download(strFileName, response);
			}
		}else{
			title = new String[5];
			title[0] = "属地";
			title[1] = "设备序列号-用户LOID";
			title[2] = "配置节点名称";
			title[3] = "配置节点值";
			title[4] = "配置时间";
			column = new String[5];
			column[0] = "city_id";
			column[1] = "username";
			column[2] = "pathvalue";
			column[3] = "paramvalue";
			column[4] = "add_time";
			fileName = "批量配置参数节点（灵活配置）";
			return "excel";
		}
		//		if (!StringUtil.IsEmpty(caseDownload))
		//		{
		//			String storePath = LipossGlobals.getLipossProperty("uploaddir");
		//			if ("0".equals(caseDownload)){
		//				bio.download(storePath + "/数图业务XLS模板.xls",
		//						response);
		//			}else{
		//				bio.download(storePath + "/数图业务TXT模板.txt",
		//						response);
		//			}
		//			return null;
		//		}
		return null;
	}

	public String getNodeBatchList()
	{
		rlist = bio.getParNodeList(curPage_splitPage, num_splitPage, customId, file_Name, starttime, endtime);
		maxPage_splitPage = bio.getParNodeCount(curPage_splitPage, num_splitPage, customId, file_Name, starttime, endtime);
		return "parNodelist";
	}
	
	/**
	 * 批量配置参数节点
	 * @return
	 */
	public String doConfigParamBatch(){
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		String serviceId = "116";
		String[] deviceStr = null;
		ajax = bio.parNodeBatch(userId, deviceIds, gw_type, paramNodePath, paramValue, file_name, cityIds,username);
		return "ajax";
	}

	public String checkRepeat() {
		ajax = String.valueOf(bio.checkRepeatName(file_name));
		return "ajax";
	}

	/**
	 * 查询策略表中未作的数量，超过一定数值就不做了
	 * @return
	 * @add by chenjie 2013-1-7
	 */
	public String queryUndoNum() {
		logger.debug("queryUndoNum()");
		ajax = String.valueOf(bio.queryUndoNum());
		return "ajax";
	}
	
	public String queryCustomNum() {
		logger.debug("queryUndoNum()");
		ajax = String.valueOf(bio.queryCustomNum());
		return "ajax";
	}
	
	public String getPreResult()
	{
		logger.warn("预读，获取组播VLANID");
		ajax = bio.getPreResult(deviceIds,gw_type);
		return "ajax";
	}
	
	public boolean doCheckParam(String[] paramNodePaths,String[] paramValues)
	{
		logger.warn("doCheckParam  start");
		for (int i = 0; i < paramNodePaths.length-1; i++)
		{
			String paramNodePath = paramNodePaths[i+1];
			int paramValue = Integer.parseInt(paramValues[i+1]);
			if(WifiChannel.equals(paramNodePath) && (paramValue < 0 || paramValue > 13 ))
			{
				ajax = "channel 非法";
				return false;
			}
			if(WifiSendPower.equals(paramNodePath) && (paramValue < 1 || paramValue > 5 ))
			{
				ajax = "powerlevel 非法";
				return false;
			}
			if(WifiFaxModel.equals(paramNodePath) && (paramValue != 0 && paramValue != 1 ))
			{
				ajax = "faxPattern 非法";
				return false;
			}
		}
		return true;
	}
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public ParamNodeBatchConfigServ getBio() {
		return bio;
	}

	public void setBio(ParamNodeBatchConfigServ bio) {
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

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
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

	public String getFile_name(){
		return file_name;
	}

	public void setFile_name(String file_name){
		this.file_name = file_name;
	}

	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getCityId(){
		return cityId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public long getUserId(){
		return userId;
	}

	public void setUserId(long userId){
		this.userId = userId;
	}

	public String getCityIds(){
		return cityIds;
	}

	public void setCityIds(String cityIds){
		this.cityIds = cityIds;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public List<Map> getRlist(){
		return rlist;
	}

	public void setRlist(List<Map> rlist){
		this.rlist = rlist;
	}

	public String getCustomId(){
		return customId;
	}

	public void setCustomId(String customId){
		this.customId = customId;
	}

	public String getStarttime(){
		return starttime;
	}

	public void setStarttime(String starttime){
		this.starttime = starttime;
	}

	public String getEndtime(){
		return endtime;
	}

	public void setEndtime(String endtime){
		this.endtime = endtime;
	}

	public String getFileName(){
		return fileName;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getTask_id(){
		return task_id;
	}

	public void setTask_id(String task_id){
		this.task_id = task_id;
	}

	public String getFileType(){
		return fileType;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}
	
	public String getFile_Name(){
		return file_Name;
	}

	public void setFile_Name(String file_Name){
		this.file_Name = file_Name;
	}
	
	public String[] getTitle(){
		return title;
	}
	
	public void setTitle(String[] title){
		this.title = title;
	}
	
	public String[] getColumn(){
		return column;
	}

	public void setColumn(String[] column){
		this.column = column;
	}

	public List<Map> getData(){
		return data;
	}
	
	public void setData(List<Map> data){
		this.data = data;
	}

	public void setServletResponse(HttpServletResponse response){
		this.response = response;
	}

	public String getCaseDownload()	{
		return caseDownload;
	}

	public void setCaseDownload(String caseDownload){
		this.caseDownload = caseDownload;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;

	}

	public HttpServletRequest getRequest()
	{
		return request;
	}
	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	
	public HttpServletResponse getResponse()
	{
		return response;
	}

	
	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	
	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}

	
	public void setGwShare_queryType(String gwShare_queryType)
	{
		this.gwShare_queryType = gwShare_queryType;
	}

	
	public String getDevice_id()
	{
		return device_id;
	}

	
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	
	public String getGwShare_nextCityId()
	{
		return gwShare_nextCityId;
	}

	
	public void setGwShare_nextCityId(String gwShare_nextCityId)
	{
		this.gwShare_nextCityId = gwShare_nextCityId;
	}

	
	public String getGwShare_cityId()
	{
		return gwShare_cityId;
	}

	
	public void setGwShare_cityId(String gwShare_cityId)
	{
		this.gwShare_cityId = gwShare_cityId;
	}

	
	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}

	
	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}

	
	public String getGwShare_deviceModelId()
	{
		return gwShare_deviceModelId;
	}

	
	public void setGwShare_deviceModelId(String gwShare_deviceModelId)
	{
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	
	public String getGwShare_devicetypeId()
	{
		return gwShare_devicetypeId;
	}

	
	public void setGwShare_devicetypeId(String gwShare_devicetypeId)
	{
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	
	public String getGwShare_bindType()
	{
		return gwShare_bindType;
	}

	
	public void setGwShare_bindType(String gwShare_bindType)
	{
		this.gwShare_bindType = gwShare_bindType;
	}

	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	
	public String getTemplate_id()
	{
		return template_id;
	}

	
	public void setTemplate_id(String template_id)
	{
		this.template_id = template_id;
	}

	
	public String getIsBind()
	{
		return isBind;
	}

	
	public void setIsBind(String isBind)
	{
		this.isBind = isBind;
	}

	
	public String getTask_name()
	{
		return task_name;
	}

	
	public void setTask_name(String task_name)
	{
		this.task_name = task_name;
	}

	
	public String getDotype()
	{
		return dotype;
	}

	
	public void setDotype(String dotype)
	{
		this.dotype = dotype;
	}

	
	public String getDonow()
	{
		return donow;
	}

	
	public void setDonow(String donow)
	{
		this.donow = donow;
	}

	
	public String getExpire_time_start()
	{
		return expire_time_start;
	}

	
	public void setExpire_time_start(String expire_time_start)
	{
		this.expire_time_start = expire_time_start;
	}

	
	public String getExpire_time_end()
	{
		return expire_time_end;
	}

	
	public void setExpire_time_end(String expire_time_end)
	{
		this.expire_time_end = expire_time_end;
	}

	
	public String getExpire_date_start()
	{
		return expire_date_start;
	}

	
	public void setExpire_date_start(String expire_date_start)
	{
		this.expire_date_start = expire_date_start;
	}

	
	public String getExpire_date_end()
	{
		return expire_date_end;
	}

	
	public void setExpire_date_end(String expire_date_end)
	{
		this.expire_date_end = expire_date_end;
	}

}
