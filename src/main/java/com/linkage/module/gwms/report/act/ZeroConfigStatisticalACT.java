/**
 * 
 */
package com.linkage.module.gwms.report.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.bio.interf.I_ZeroConfigStatisticalBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-6
 * @category com.linkage.module.gwms.report.act
 * 
 */
public class ZeroConfigStatisticalACT extends ActionSupport implements ServletRequestAware {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ZeroConfigStatisticalACT.class);
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	
	// request取登陆帐号使用
	private HttpServletRequest request;
	
	//I_ZeroConfigStatisticalBIO
	I_ZeroConfigStatisticalBIO zeroConfigStatisticalBIO = null;
	
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
	/**
	 * 入口方法
	 */
	public String execute() throws Exception {
		
		logger.debug("ZeroConfigStatisticalACT=>execute(");
		
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE,-10);
		this.strEndTime = sdf.format(cal.getTime());
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		this.strStartTime = sdf.format(cal.getTime());
		
		if(null==this.cityId || "".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 根据不同的情况查询数据，并返回相关的结果
	 * 
	 * @return
	 */
	public String getDayReport(){
		
		if(null==this.cityId){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		this.cityList = zeroConfigStatisticalBIO.getChildCityList(cityId);
		this.zeroDataArr = zeroConfigStatisticalBIO.getData(cityList,startTime,endTime);
		
		if("excel".equals(isReport)){
			
			this.title = new String[4];
			this.title[0] = "属地";
			this.title[1] = "成功数";
			this.title[2] = "失败数";
			this.title[3] = "成功率";
			
			this.column = new String[4];
			this.column[0] = "city_name";
			this.column[1] = "num1";
			this.column[2] = "num2";
			this.column[3] = "avg";
			
			this.data = new ArrayList();
			for(int i=0;i<this.cityList.size();i++){
				Map cityMap = (Map) cityList.get(i);
				Map<String, String> oneZeroDataMap = new HashMap<String, String>();
				oneZeroDataMap.put("city_name", (String)cityMap.get("city_name"));
				oneZeroDataMap.put("num1", this.zeroDataArr[0][i+1]);
				oneZeroDataMap.put("num2", this.zeroDataArr[1][i+1]);
				oneZeroDataMap.put("avg", this.zeroDataArr[2][i+1]);
				data.add(oneZeroDataMap);
			}
			
			this.fileName = "零配置状态统计";
			
			return "excel";
		}else if("pdf".equals(isReport)){
			
			this.pdfFileName = "零配置状态统计";
			//标题
			this.pdfTitle = "零配置状态统计";
			//table tile
			this.tbTitle = new String[4];
			this.tbTitle[0] = "属地";
			this.tbTitle[1] = "成功数";
			this.tbTitle[2] = "失败数";
			this.tbTitle[3] = "成功率";
			//需要取的列
			this.tbName = new String[4];
			this.tbName[0] = "city_name";
			this.tbName[1] = "num1";
			this.tbName[2] = "num2";
			this.tbName[3] = "avg";
			
			//数据
			this.pdfListData = new ArrayList();
			for(int i=0;i<this.cityList.size();i++){
				Map cityMap = (Map) cityList.get(i);
				Map<String, String> oneZeroDataMap = new HashMap<String, String>();
				oneZeroDataMap.put("city_name", (String)cityMap.get("city_name"));
				oneZeroDataMap.put("num1", this.zeroDataArr[0][i+1]);
				oneZeroDataMap.put("num2", this.zeroDataArr[1][i+1]);
				oneZeroDataMap.put("avg", this.zeroDataArr[2][i+1]);
				pdfListData.add(oneZeroDataMap);
			}
			
			
			return "pdfByList";
		}else if("print".equals(isReport)){
			
			this.zeroDataList = new ArrayList<Map<String, String>>();
			Map<String, String> firstZeroDataMap = new HashMap<String, String>();
			firstZeroDataMap.put("city_id", "");
			firstZeroDataMap.put("city_name", "属地");
			firstZeroDataMap.put("hasCityId","false");
			firstZeroDataMap.put("num1", "成功数");
			firstZeroDataMap.put("num2", "失败数");
			firstZeroDataMap.put("avg", "成功率");
			zeroDataList.add(firstZeroDataMap);
			
			for(int i=0;i<this.cityList.size();i++){
				Map cityMap = (Map) cityList.get(i);
				Map<String, String> oneZeroDataMap = new HashMap<String, String>();
				oneZeroDataMap.put("city_id", (String)cityMap.get("city_id"));
				oneZeroDataMap.put("city_name", (String)cityMap.get("city_name"));
				oneZeroDataMap.put("hasCityId", (String)cityMap.get("hasCityId"));
				oneZeroDataMap.put("num1", this.zeroDataArr[0][i+1]);
				oneZeroDataMap.put("num2", this.zeroDataArr[1][i+1]);
				oneZeroDataMap.put("avg", this.zeroDataArr[2][i+1]);
				zeroDataList.add(oneZeroDataMap);
			}
			return "print";
		}else{
			this.zeroDataList = new ArrayList<Map<String, String>>();
			Map<String, String> firstZeroDataMap = new HashMap<String, String>();
			firstZeroDataMap.put("city_id", "");
			firstZeroDataMap.put("city_name", "属地");
			firstZeroDataMap.put("hasCityId","false");
			firstZeroDataMap.put("num1", "成功数");
			firstZeroDataMap.put("num2", "失败数");
			firstZeroDataMap.put("avg", "成功率");
			zeroDataList.add(firstZeroDataMap);
			
			for(int i=0;i<this.cityList.size();i++){
				Map cityMap = (Map) cityList.get(i);
				Map<String, String> oneZeroDataMap = new HashMap<String, String>();
				oneZeroDataMap.put("city_id", (String)cityMap.get("city_id"));
				oneZeroDataMap.put("city_name", (String)cityMap.get("city_name"));
				oneZeroDataMap.put("hasCityId", (String)cityMap.get("hasCityId"));
				oneZeroDataMap.put("num1", this.zeroDataArr[0][i+1]);
				oneZeroDataMap.put("num2", this.zeroDataArr[1][i+1]);
				oneZeroDataMap.put("avg", this.zeroDataArr[2][i+1]);
				zeroDataList.add(oneZeroDataMap);
			}
			
			return "list";
		}
	}
	
	public String getBindData(){
		
		logger.debug("getBindData()");
		
		this.bindDataList = zeroConfigStatisticalBIO.getBindData(cityId,startTime,endTime);
		
		if("excel".equals(isReport)){
			
			this.title = new String[3];
			this.title[0] = "宽带账号";
			this.title[1] = "设备序列号";
			this.title[2] = "注册时间";
			
			this.column = new String[3];
			this.column[0] = "username";
			this.column[1] = "device_serilnumber";
			this.column[2] = "binddate";
			
			this.data = new ArrayList();
			for(int i=0;i<this.bindDataList.size();i++){
				Map cityMap = (Map) bindDataList.get(i);
				data.add(cityMap);
			}
			
			this.fileName = "零配置成功统计";
			
			return "excel";
		}else if("pdf".equals(isReport)){
			
			this.pdfFileName = "零配置成功统计";
			//标题
			this.pdfTitle = "零配置成功统计";
			//table tile
			this.tbTitle = new String[3];
			this.tbTitle[0] = "宽带账号";
			this.tbTitle[1] = "设备序列号";
			this.tbTitle[2] = "注册时间";
			//需要取的列
			this.tbName = new String[3];
			this.tbName[0] = "username";
			this.tbName[1] = "device_serialnumber";
			this.tbName[2] = "binddate";
			
			//数据
			this.pdfListData = new ArrayList();
			for(int i=0;i<this.bindDataList.size();i++){
				Map cityMap = (Map) bindDataList.get(i);
				pdfListData.add(cityMap);
			}
			
			return "pdfByList";
		}else if("print".equals(isReport)){
			return "bindPrint";
		}else{
			return "bindData";
		}
		
	}
	
	public String getNoBindData(){
		
		logger.debug("getNoBindData()");
		
		this.bindNoDataList = zeroConfigStatisticalBIO.getNoBindData(cityId,startTime,endTime);
		if("excel".equals(isReport)){
			
			this.title = new String[3];
			this.title[0] = "宽带账号";
			this.title[1] = "设备序列号";
			this.title[2] = "注册时间";
			
			this.column = new String[3];
			this.column[0] = "username";
			this.column[1] = "device_serialnumber";
			this.column[2] = "binddate";
			
			this.data = new ArrayList();
			for(int i=0;i<this.bindNoDataList.size();i++){
				Map cityMap = (Map) bindNoDataList.get(i);
				data.add(cityMap);
			}
			
			this.fileName = "零配置失败统计";
			
			return "excel";
		}else if("pdf".equals(isReport)){
			
			this.pdfFileName = "零配置失败统计";
			//标题
			this.pdfTitle = "零配置失败统计";
			//table tile
			this.tbTitle = new String[3];
			this.tbTitle[0] = "宽带账号";
			this.tbTitle[1] = "设备序列号";
			this.tbTitle[2] = "注册时间";
			//需要取的列
			this.tbName = new String[3];
			this.tbName[0] = "username";
			this.tbName[1] = "device_serilnumber";
			this.tbName[2] = "binddate";
			
			//数据
			this.pdfListData = new ArrayList();
			for(int i=0;i<this.bindNoDataList.size();i++){
				Map cityMap = (Map) bindNoDataList.get(i);
				pdfListData.add(cityMap);
			}
			
			return "pdfByList";
		}else if("print".equals(isReport)){
			return "noBndPrint";
		}else{
			return "noBindData";
		}
	}
	
	/**
	 * 属地 cityId
	 */
	private String cityId = null;
	
	/**
	 * 属地列表，作为标题显示
	 */
	private List cityList = null;
	
	/**
	 * 列表数据,二维数组显示
	 */
	private String[][] zeroDataArr = null;
	
	/**
	 * 为了纵向展现，重新布局
	 */
	private List<Map<String, String>> zeroDataList = null;
	
	/**
	 * 开始时间(秒格式,作为传入格式)
	 */
	private String startTime = null;
	
	/**
	 * 结束时间(秒格式,作为传入格式)
	 */
	private String endTime = null;
	
	/**
	 * 开始时间(YYYY-MM-DD HH:mm:SS格式,作为传出格式)
	 */
	private String strStartTime = null;
	
	/**
	 * 结束时间(YYYY-MM-DD HH:mm:SS格式,作为传出格式)
	 */
	private String strEndTime = null;

	/**
	 * 是否是报表,如果是，则填报表类型：excel、pdf、print
	 */
	private String isReport = null;
	
	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
	//下载文件名
	private String pdfFileName = null;
	//标题
	private String pdfTitle = null;
	//table tile
	private String[] tbTitle = null;
	//需要取的列
	private String[] tbName = null;
	//数据
	private List<Map<String,String>> pdfListData = null;
	
	/**
	 * 成功数据
	 */
	private List bindDataList = null;
	
	/**
	 * 失败数据
	 */
	private List bindNoDataList = null;
	
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the strEndTime
	 */
	public String getStrEndTime() {
		return strEndTime;
	}

	/**
	 * @param strEndTime the strEndTime to set
	 */
	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}

	/**
	 * @return the strStartTime
	 */
	public String getStrStartTime() {
		return strStartTime;
	}

	/**
	 * @param strStartTime the strStartTime to set
	 */
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}

	/**
	 * @return the isReport
	 */
	public String getIsReport() {
		return isReport;
	}

	/**
	 * @param isReport the isReport to set
	 */
	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}

	/**
	 * @return the cityList
	 */
	public List getCityList() {
		return cityList;
	}

	/**
	 * @param cityList the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the zeroDataArr
	 */
	public String[][] getZeroDataArr() {
		return zeroDataArr;
	}

	/**
	 * @param zeroDataArr the zeroDataArr to set
	 */
	public void setZeroDataArr(String[][] zeroDataArr) {
		this.zeroDataArr = zeroDataArr;
	}

	/**
	 * @return the zeroConfigStatisticalBIO
	 */
	public I_ZeroConfigStatisticalBIO getZeroConfigStatisticalBIO() {
		return zeroConfigStatisticalBIO;
	}

	/**
	 * @param zeroConfigStatisticalBIO the zeroConfigStatisticalBIO to set
	 */
	public void setZeroConfigStatisticalBIO(
			I_ZeroConfigStatisticalBIO zeroConfigStatisticalBIO) {
		this.zeroConfigStatisticalBIO = zeroConfigStatisticalBIO;
	}

	/**
	 * @return the zeroDataList
	 */
	public List getZeroDataList() {
		return zeroDataList;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String[] column) {
		this.column = column;
	}

	/**
	 * @return the data
	 */
	public ArrayList<Map> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the pdfFileName
	 */
	public String getPdfFileName() {
		return pdfFileName;
	}

	/**
	 * @param pdfFileName the pdfFileName to set
	 */
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	/**
	 * @return the pdfListData
	 */
	public List<Map<String, String>> getPdfListData() {
		return pdfListData;
	}

	/**
	 * @param pdfListData the pdfListData to set
	 */
	public void setPdfListData(List<Map<String, String>> pdfListData) {
		this.pdfListData = pdfListData;
	}

	/**
	 * @return the pdfTitle
	 */
	public String getPdfTitle() {
		return pdfTitle;
	}

	/**
	 * @param pdfTitle the pdfTitle to set
	 */
	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	/**
	 * @return the tbName
	 */
	public String[] getTbName() {
		return tbName;
	}

	/**
	 * @param tbName the tbName to set
	 */
	public void setTbName(String[] tbName) {
		this.tbName = tbName;
	}

	/**
	 * @return the tbTitle
	 */
	public String[] getTbTitle() {
		return tbTitle;
	}

	/**
	 * @param tbTitle the tbTitle to set
	 */
	public void setTbTitle(String[] tbTitle) {
		this.tbTitle = tbTitle;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String[] title) {
		this.title = title;
	}

	/**
	 * @param zeroDataList the zeroDataList to set
	 */
	public void setZeroDataList(List<Map<String, String>> zeroDataList) {
		this.zeroDataList = zeroDataList;
	}

	/**
	 * @return the bindDataList
	 */
	public List getBindDataList() {
		return bindDataList;
	}

	/**
	 * @param bindDataList the bindDataList to set
	 */
	public void setBindDataList(List bindDataList) {
		this.bindDataList = bindDataList;
	}

	/**
	 * @return the bindNoDataList
	 */
	public List getBindNoDataList() {
		return bindNoDataList;
	}

	/**
	 * @param bindNoDataList the bindNoDataList to set
	 */
	public void setBindNoDataList(List bindNoDataList) {
		this.bindNoDataList = bindNoDataList;
	}
	
}
