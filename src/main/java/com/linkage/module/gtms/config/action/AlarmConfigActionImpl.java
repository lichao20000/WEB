package com.linkage.module.gtms.config.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.AlarmConfigServ;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class AlarmConfigActionImpl extends ActionSupport implements SessionAware, AlarmConfigAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2623431592217003675L;
	private static Logger logger = LoggerFactory.getLogger(AlarmConfigActionImpl.class);
	private Map session;
	private String ajax = null;
	/**区分ITMS和BBMS的功能*/
	private String gw_type ="";
	private String deviceIds ;
	private String param;
	private String url =""  ;
	private String threshold =""   ;
	private String upload ="";
	private String samplePeriod="" ;
	private String upPeriod  =""   ;
	private String alarmTime =""   ;
	private String oldTime   =""   ;
	private String fileName ="";
	private String gwShare_queryType ="";
	private String importQueryField;
	
	private AlarmConfigServ bio;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	
	
	public void setBio(AlarmConfigServ bio) {
		this.bio = bio;
	}
	/**
	 * 告警配置
	 * @return
	 */
	public String doConfig(){
		
		logger.warn("doConfig({})",gwShare_queryType);
		//告警参数
		String[] paramArr = new String[]{url,threshold,samplePeriod,upPeriod,alarmTime,oldTime,upload};
		//安徽查询
		if ("4".equals(gwShare_queryType))
		{
			int[] result;
			String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
			List<String> dataList = null;

			try{
				if("txt".equals(fileName_)){
					dataList = getImportDataByTXT(fileName);
				}else{
					dataList = getImportDataByXLS(fileName);
				}
			}catch(FileNotFoundException e){
				logger.warn("文件没找到！");
				return null;
			}catch(IOException e){
				logger.warn("文件解析出错！");
				return null;
			}catch(Exception e){
				logger.warn("文件解析出错！");
				return null;
			}
			//dataList 为帐号或者序列号，2代表告警
			result = gwDeviceQueryBio.insertImportDataTmp(dataList,2);
			logger.warn("@@@@插入数结果result="+result.toString());
			String sql = "";
			if(this.importQueryField == "username"){
				String tableName = "tab_hgwcustomer";
				if(!StringUtil.IsEmpty(gw_type)&&"2".equals(gw_type)){
					tableName = "tab_egwcustomer";
				}
				sql = bio.getAccSQL(tableName,gw_type);
			}else if(this.importQueryField == "device_serialnumber"){
				sql = bio.getDevSQL(gw_type);
			}
			logger.warn("###########sql ="+sql.toString().replace("[", "\'"));
			//直接调用软件升级模块，处理查询到的
			ajax = 	bio.doAlarmConfig(new String[] { sql.toString().replace("[", "\'") }, "911",  paramArr ,gw_type);
			return "ajax";
		}else{
		//非特殊查询
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			logger.warn("deviceIds==========="+deviceIds);
			if (StringUtil.IsEmpty(deviceIds))
			{
				logger.warn("任务中没有设备");
			}
			if (!"0".equals(deviceIds))
			{
				String[] deviceId_array = deviceIds.split(",");
				ajax = bio.doAlarmConfig(deviceId_array, "911", paramArr, gw_type);
			}
			// 调用后台corba接口时，小于100的情况，传deviceID数组，否则传SQL
			else
			{
				if (StringUtil.IsEmpty(param))
				{
					logger.warn("param为空");
				}
				String[] _param = param.split("\\|");
				String matchSQL = "";
				long total = 0l;
				//logger.warn("param========="+param+"=====param.length="+_param.length);
				if(_param.length>11){
					matchSQL = _param[10];
					total = StringUtil.getLongValue(_param[11]);
				}
				if (total < 100)
				{
					logger.warn("调用后台软件升级corba接口：数量小于100，传deviceID数组");
					logger.warn("curUser.getAreaId()==="+curUser.getAreaId());
					List list = gwDeviceQueryBio.getDeviceList(gw_type, curUser.getAreaId(), param);
					String[] deviceId_array = new String[list.size()];
					for (int i = 0; i < list.size(); i++)
					{
						Map map = (Map) list.get(i);
						deviceId_array[i] = StringUtil.getStringValue(map
								.get("device_id"));
					}
				ajax = 	bio.doAlarmConfig(deviceId_array, "911", paramArr,gw_type);
				}
				else
				{
					logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
				ajax = 	bio.doAlarmConfig(new String[] { matchSQL.replace("[", "\'") }, "911",paramArr,gw_type);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
			return "result";
		}    
		}
		return "ajax";
		
	}
	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws  
	 * 		   
	 */
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
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else{
					this.importQueryField = "username";
				}
			}
			
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
		f.delete();
		f = null;
		return list;
	}
	
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		File f = new File(getFilePath()+fileName);
		f.delete();
		return list;
	}
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
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
	public Map getSession() {
		return session;
	}
	public void setSession(Map session) {
		this.session = session;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public String getGw_type() {
		return gw_type;
	}
	public void setGw_type(String gwType) {
		gw_type = gwType;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getSamplePeriod() {
		return samplePeriod;
	}
	public void setSamplePeriod(String samplePeriod) {
		this.samplePeriod = samplePeriod;
	}
	public String getUpPeriod() {
		return upPeriod;
	}
	public void setUpPeriod(String upPeriod) {
		this.upPeriod = upPeriod;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getOldTime() {
		return oldTime;
	}
	public void setOldTime(String oldTime) {
		this.oldTime = oldTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}
	public void setGwShare_queryType(String gwShareQueryType) {
		gwShare_queryType = gwShareQueryType;
	}
	public String getImportQueryField() {
		return importQueryField;
	}
	public void setImportQueryField(String importQueryField) {
		this.importQueryField = importQueryField;
	}
	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio) {
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	
}
