/**
 * 
 */
package com.linkage.module.gwms.service.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.service.act.interf.I_SheetStateViewACT;
import com.linkage.module.gwms.service.bio.interf.I_SheetStateViewBIO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-16
 * @category action.business
 * 
 */
@SuppressWarnings("unchecked")
public class SheetStateViewACT extends splitPageAction implements
		ServletRequestAware,I_SheetStateViewACT {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * dao
	 */
	I_SheetStateViewBIO sheetStateViewBIO;

	/**
	 * 用户账号
	 */
	private String username = null;

	/**
	 * 属地
	 */
	private String city_id = null;

	/**
	 * 业务类型
	 */
	private String productSpecId = null;

	/**
	 * 开始时间
	 */
	private String startTime = null;

	/**
	 * 结束时间
	 */
	private String endTime = null;

	/**
	 * 属地列表
	 */
	private List cityList = null;

	/**
	 * 操作类型表
	 */
	private List gwOperTypeList = null;
	
	/**
	 * 操作类型
	 */
	private String oper_type_id = null;
	
	/**
	 * 绑定状态
	 */
	private String bind_state = null;
	
	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
	/**
	 * 工单数据
	 */
	private List dataSheet = null;

	// request取登陆帐号使用
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * 程序入口
	 */
	public String execute() throws Exception {

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		if (null == city_id || "".equals(city_id)) {
			city_id = curUser.getCityId();
		}

		this.cityList = sheetStateViewBIO.getAllCity(city_id);
		this.gwOperTypeList = sheetStateViewBIO.getGwOperType();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		StringBuffer sf = new StringBuffer();
		sf.append(cal.get(Calendar.YEAR));
		sf.append("-");
		sf.append(cal.get(Calendar.MONTH)+1);
		sf.append("-");
		sf.append(cal.get(Calendar.DATE));
		sf.append(" ");
		this.startTime = sf.toString() + "00:00:00";
		this.endTime = sf.toString() + "23:59:59";

		return SUCCESS;

	}

	/**
	 * 数据查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String startQuery() throws Exception {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long timeStart = simpleDateFormat.parse(startTime).getTime();
		long timeEnd = simpleDateFormat.parse(endTime).getTime();
		
		if (null == city_id || "".equals(city_id)) {
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			city_id = curUser.getCityId();
		}
		
		this.dataSheet = sheetStateViewBIO.getSheetState(curPage_splitPage,
				num_splitPage, username, city_id, productSpecId, timeStart,
				timeEnd,oper_type_id,bind_state);

		maxPage_splitPage = sheetStateViewBIO.getSheetStateCount(curPage_splitPage,
				num_splitPage, username, city_id, productSpecId, timeStart,
				timeEnd,oper_type_id,bind_state);

		return "list";
	}

	/**
	 * 分页查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goPage() throws Exception {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long timeStart = simpleDateFormat.parse(startTime).getTime();
		long timeEnd = simpleDateFormat.parse(endTime).getTime();
		
		if (null == city_id || "".equals(city_id)) {
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			city_id = curUser.getCityId();
		}
		
		this.dataSheet = sheetStateViewBIO.getSheetState(curPage_splitPage,
				num_splitPage, username, city_id, productSpecId, timeStart,
				timeEnd,oper_type_id,bind_state);

		maxPage_splitPage = sheetStateViewBIO.getSheetStateCount(curPage_splitPage,
				num_splitPage, username, city_id, productSpecId, timeStart,
				timeEnd,oper_type_id,bind_state);

		return "list";
		
	}
	
	public String getExcelReport() throws Exception{
		
		title = new String[]{"工单ID","来单时间","属地","客户名称","用户账号","操作类型","受理状态","绑定状态","配置执行状态"};
		//data字段Map对应的字段名称
		column = new String[]{"bss_sheet_id", "receive_date","city_name","customer_name",
				"username","oper_type_name","result","bind_state","result_spec_state"};
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long timeStart = simpleDateFormat.parse(startTime).getTime();
		long timeEnd = simpleDateFormat.parse(endTime).getTime();
		//数据
		data = new ArrayList<Map>();
		List<Map> list = sheetStateViewBIO.getSheetStateExcel(username, city_id, 
				productSpecId, timeStart, timeEnd, oper_type_id, bind_state);
		for(Map o:list){
			data.add(o);
		}
		//文件名称
		fileName = "SheetState" + Math.round(Math.random()*100);
		return "excel";
		
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
	 * @return the city_id
	 */
	public String getCity_id() {
		return city_id;
	}

	/**
	 * @param city_id
	 *            the city_id to set
	 */
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	/**
	 * @return the cityList
	 */
	public List getCityList() {
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the productSpecId
	 */
	public String getProductSpecId() {
		return productSpecId;
	}

	/**
	 * @param productSpecId
	 *            the productSpecId to set
	 */
	public void setProductSpecId(String productSpecId) {
		this.productSpecId = productSpecId;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
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
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the dataSheet
	 */
	public List getDataSheet() {
		return dataSheet;
	}

	/**
	 * @param dataSheet
	 *            the dataSheet to set
	 */
	public void setDataSheet(List dataSheet) {
		this.dataSheet = dataSheet;
	}

	/**
	 * @return the sheetStateViewBIO
	 */
	public I_SheetStateViewBIO getSheetStateViewBIO() {
		return sheetStateViewBIO;
	}

	/**
	 * @param sheetStateViewBIO the sheetStateViewBIO to set
	 */
	public void setSheetStateViewBIO(I_SheetStateViewBIO sheetStateViewBIO) {
		this.sheetStateViewBIO = sheetStateViewBIO;
	}

	/**
	 * @return the gwOperTypeList
	 */
	public List getGwOperTypeList() {
		return gwOperTypeList;
	}

	/**
	 * @param gwOperTypeList the gwOperTypeList to set
	 */
	public void setGwOperTypeList(List gwOperTypeList) {
		this.gwOperTypeList = gwOperTypeList;
	}

	/**
	 * @return the bind_state
	 */
	public String getBind_state() {
		return bind_state;
	}

	/**
	 * @param bind_state the bind_state to set
	 */
	public void setBind_state(String bind_state) {
		this.bind_state = bind_state;
	}

	/**
	 * @return the oper_type_id
	 */
	public String getOper_type_id() {
		return oper_type_id;
	}

	/**
	 * @param oper_type_id the oper_type_id to set
	 */
	public void setOper_type_id(String oper_type_id) {
		this.oper_type_id = oper_type_id;
	}
	
}
