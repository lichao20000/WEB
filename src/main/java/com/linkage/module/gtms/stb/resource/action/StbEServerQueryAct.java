package com.linkage.module.gtms.stb.resource.action;

import action.splitpage.splitPageAction;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.stb.resource.serv.StbEServerQueryBio;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 机顶盒工单信息查询
 * wanghong5
 */
@SuppressWarnings({"rawtypes","unused"})
public class StbEServerQueryAct extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(StbEServerQueryAct.class);
	private Map session;
	private HttpServletRequest request;
	
	/** 设备mac地址 */
	public String mac;
	/** 业务账号 */
	public String servAccount;
	/** 开始时间 */
	public String startTime;
	/** 结束时间 */
	public String endTime;
	/** 公客网格 */
	public String grid;
	/** 公客操作员 */
	public String opertor;
	/**工单id*/
	public String sheetId;

	/**工单处理状态*/
	public String status;

	public String ajax;
	/** 工单集 */
	public List<Map<String, String>> data;

	/** 查询总数 */
	public int queryCount;
	/**工单详细*/
	public Map<String,String> eserverInfo;
	
	private String[] title;
	private String[] column;
	private String fileName;
	
	private StbEServerQueryBio bio;


	/**
	 * 首页初始日期
	 */
	public String init()
	{
		startTime=setTime(0);
		endTime=setTime(1);
		
		return "init";
	}

	/**
	 * 首页初始日期
	 */
	public String init1()
	{
		startTime=setTime(0);
		endTime=setTime(1);

		return "init1";
	}

	/**
	 * 机顶盒列表查询
	 */
	public String queryEServerList()
	{
		logger.debug("queryEServerList({},{},{},{},{},{},{},{})",
						new Object[]{curPage_splitPage,num_splitPage,
					mac,servAccount,grid,opertor,
					startTime,endTime});
		long start_time = setTime(startTime);
		long end_time = setTime(endTime);
		
		data = bio.queryEServerList(curPage_splitPage,num_splitPage,
				mac,servAccount,grid,opertor,
				start_time, end_time);
		maxPage_splitPage = bio.countEServerList(num_splitPage,mac,
				servAccount,grid,opertor,start_time,end_time);
		queryCount = bio.getQueryCount();
		
		return "list";
	}

	/**
	 * 机顶盒列表查询
	 */
	public String query()
	{
		logger.debug("query({},{},{},{},{},{},{},{})",
				new Object[]{curPage_splitPage,num_splitPage,
						mac,servAccount,status,
						startTime,endTime});
		long start_time = setTime(startTime);
		long end_time = setTime(endTime);
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();

		String groupOid = bio.getGroupOid(acc_oid);
		if(groupOid.contains("2"))
		{
			groupOid = "2";
		}
		else
		{
			groupOid = "1";
		}

		data = bio.query(curPage_splitPage,num_splitPage,
				mac,servAccount,status,
				start_time, end_time);
		for(Map map : data)
		{
			map.put("groupOid",groupOid);
		}
		maxPage_splitPage = bio.count(num_splitPage,mac,
				servAccount,status,start_time,end_time);
		queryCount = bio.getQueryCount();

		return "list1";
	}



	/**
	 * 工单详细信息查询
	 */
	public String queryEServerInfo()
	{
		logger.warn("queryEServerInfo({})",sheetId);
		eserverInfo=bio.queryEServerInfo(sheetId);
		
		return "info";
	}

	public String queryDetail()
	{
		logger.warn("queryDetail({})",sheetId);
		eserverInfo=bio.queryDetail(sheetId);

		return "detail1";
	}


	public String passEserver()
	{
		logger.warn("queryDetail({})",sheetId);
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();
		String loginAccount = user.getAccount();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = "";
		result = df.format(System.currentTimeMillis())+
				" 账号:"+loginAccount+" 操作:通过;" ;
		bio.updateStbBindAccChgRecord(sheetId,"1");

		boolean isBind = bio.isbind(mac);
		if(!isBind)
		{
			result = result +  df.format(System.currentTimeMillis())+" mac 未绑定";
			bio.updateStbBindAccChgRecordOperLog(sheetId,result);
			ajax = "mac 未绑定";
			return "ajax";
		}

		ajax = bio.removebindcheck(mac);

		if(!"成功".equals(ajax))
		{
			result = result + df.format(System.currentTimeMillis())+" 【解绑工单】:失败";
			bio.updateStbBindAccChgRecordOperLog(sheetId,result);
			return "ajax";
		}
		result = result + df.format(System.currentTimeMillis())+" 【解绑工单】:成功;";
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ajax = bio.bindcheck(mac,servAccount);
		if(!"成功".equals(ajax))
		{
			result = result + df.format(System.currentTimeMillis())+" 【绑定工单】:失败";
			bio.updateStbBindAccChgRecordOperLog(sheetId,result);
			return "ajax";
		}
		result = result + df.format(System.currentTimeMillis())+" 【绑定工单】:成功";
		bio.updateStbBindAccChgRecordOperLog(sheetId,result);
		return "ajax";
	}


	public String refuseEserver()
	{
		logger.warn("refuseEserver({})",sheetId);
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();
		String loginAccount = user.getAccount();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = "";
		result = df.format(System.currentTimeMillis())+
				" 账号:"+loginAccount+" 操作:拒绝";
		ajax = bio.updateStbBindAccChgRecord(sheetId,"-1");
		bio.updateStbBindAccChgRecordOperLog(sheetId,result);
		return "ajax";
	}

	/**
	 * 导出excel
	 */
	public String toExcel()
	{
		long start_time = setTime(startTime);
		long end_time = setTime(endTime);
		
		data = bio.queryEServerList(-1,-1,mac,servAccount,grid,opertor,start_time, end_time);
		
		fileName = "机顶盒工单列表";
	    title = new String[] {"工单时间","工单来源","业务账号","机顶盒MAC","工单类型","网格","操作员","返回码","返回信息"};
	    column = new String[] {"receive_date","from_id","username","mac", 
	    						"server_type","grid","opertor","result","returnt_context"};

		return "excel";
	}
	
	/**
	 * 获取当前时间或本月的初始时间
	 */
	private String setTime(int i)
	{
		if(i==0){
			return DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss");
		}else{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
		}
	}
	
	/**
	 * 时间转化
	 */
	private long setTime(String time)
	{
		logger.debug("setTime()" + time);
		if (!StringUtil.IsEmpty(time)){
			return new DateTimeUtil(time).getLongTime();
		}
		return 0;
	}
	
	
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getOpertor() {
		return opertor;
	}

	public void setOpertor(String opertor) {
		this.opertor = opertor;
	}

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public StbEServerQueryBio getBio() {
		return bio;
	}

	public void setBio(StbEServerQueryBio bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
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

	public Map<String, String> getEserverInfo() {
		return eserverInfo;
	}

	public void setEserverInfo(Map<String, String> eserverInfo) {
		this.eserverInfo = eserverInfo;
	}

	public Map getSession() {
		return session;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
