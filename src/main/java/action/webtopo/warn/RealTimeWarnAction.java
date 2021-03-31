package action.webtopo.warn;

import static action.cst.AJAX;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.SessionAwareAction;
import bio.webtopo.warn.RealTimeWarnBIO;

import com.linkage.litms.system.User;

import dao.webtopo.warn.RealTimeWarnDao;

public class RealTimeWarnAction extends SessionAwareAction {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(RealTimeWarnAction.class);
	// ***********以下是用户基本信息****************************
	private static List   gatherlist;// 采集点列表
	private static List   ModuleList;// 告警模版列表
	private static String warnInfo;//告警声音内容
	// ***********以下是页面使用的变量***************************
	private int           ruleid;// 模版ID
	private String        serialno;//告警序列号:初始为0
	private int           fetchCount;//需要获得的告警条数
	private String 		  alarmid;//告警ID
	private String        table_head;//告警列标题
	private String        table_body;//告警数据
	private String        gather_val;//每个采集点的最大告警序列号:gather_id:max_serial;gather_id:max_serial
	private String columnID;//存放列标题
	private String rule;	//rule_id
	private String max;		//max_number
	private boolean debug;//是否显示调试信息
	private int     totalnum;//总共需要取得的数目条数
	private int     totalref;//刷新总数
	// ************以下是通用的变量******************************
	private RealTimeWarnDao rtd;// WebTopo实时告警牌显示DAO
	private RealTimeWarnBIO rtb;// WebTopo实时告警牌显示BIO
	private String ajax;// AJAX使用变量
	// ******************************************************
	/**
	 * 初始化
	 */
	public String execute() throws Exception {
		ModuleList = rtd.getRuleList(getAccounts());
		warnInfo=rtb.GetWarnVoiceStr(getAreaId());
		return INPUT;
	}

	/**
	 * 页面初始化进来获得的告警
	 * @return
	 * @throws Exception
	 */
	public String getALLWarnData() throws Exception{
		User user=getUser();
		String[] data = rtb.getAllWarnData(getAccounts(), getPasswd(), getGatherList(), ruleid, fetchCount,String.valueOf(getAreaId()));
		table_head=data[0];//table column
		table_body=data[1].replaceAll("\\\\\"", "'");//table body
		gather_val=data[2];//gather_value
		columnID=data[3];//存放的列的标题
		logger.debug("初始化最大告警序列号"+getAccounts()+":"+gather_val);
		return SUCCESS;
	}
	/**
	 * 获取更新的告警信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getupdateData() throws Exception {
		logger.debug("前次最大告警序列号"+getAccounts()+":"+gather_val);
		User user=getUser();
		HashMap<String,Integer> map=new HashMap();
		String[] ser=gather_val.split(";");
		int n=ser.length;
		for(int i=0;i<n;i++){
			map.put(ser[i].split(":")[0],Integer.parseInt(ser[i].split(":")[1]));
		}
		fetchCount=fetchCount>totalref?totalref:fetchCount;
		ajax = rtb.getRefWarnData(getAccounts(), getPasswd(), getGatherList(), ruleid, map, fetchCount,String.valueOf(getAreaId()), columnID);
		if(ajax==null){
			ajax="";
		}
		return AJAX;
	}
	/**
	 * 删除告警
	 * m_Number-m_GatherID-m_DeviceCoding-m_CreateTime-/-
	 * @return
	 * @throws Exception
	 */
	public String DelWarn() throws Exception {
		alarmid=alarmid.substring(0,alarmid.length()-3);
		String[] tmp=alarmid.split("-/-");
		int n=tmp.length;
		String[] Alarm=new String[n];
		for(int i=0;i<n;i++){
			rtd.ClearAlarm(Long.parseLong(tmp[i].split("-")[3]), tmp[i].split("-")[0], tmp[i].split("-")[1], getAccounts());
			Alarm[i]=tmp[i].split("-")[0]+"@"+tmp[i].split("-")[2]+";"+tmp[i].split("-")[1];
			logger.debug("确认：" + Alarm[i]);
		}
		ajax = rtb.DelWarn(Alarm, getAccounts(), getPasswd());
		return AJAX;
	}

	/**
	 * 确认告警
	 * m_Number-m_GatherID-m_DeviceCoding-m_CreateTime-/-
	 * @return
	 * @throws Exception
	 */
	public String ConfigWarn() throws Exception {
		alarmid=alarmid.substring(0,alarmid.length()-3);
		String[] tmp=alarmid.split("-/-");
		logger.debug("确认告警:"+alarmid);
		int n=tmp.length;
		String[] Alarm=new String[n];
		for(int i=0;i<n;i++){
			Alarm[i]=tmp[i].split("-")[0]+"@"+tmp[i].split("-")[2]+";"+tmp[i].split("-")[1];
			rtd.AckAlarm(Long.parseLong(tmp[i].split("-")[3]), tmp[i].split("-")[0], tmp[i].split("-")[1], getAccounts());
			logger.debug("确认：" + Alarm[i]);
		}
		ajax = rtb.ConfigAlarm(getAccounts(),getPasswd(),Alarm);
		return AJAX;
	}

	/**
	 * 获取清除告警信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getClearWarnData() throws Exception {
		User user=getUser();
		ajax = rtb.getClearWarnList(String.valueOf(getAreaId()), gather_val,ruleid);
		logger.debug("获取清除告警:"+ajax);
		return AJAX;
	}

	public static List getGatherlist() {
		return gatherlist;
	}

	public static void setGatherlist(List gatherlist) {
		RealTimeWarnAction.gatherlist = gatherlist;
	}

	public static List getModuleList() {
		return ModuleList;
	}

	public static void setModuleList(List moduleList) {
		ModuleList = moduleList;
	}

	public static String getWarnInfo() {
		return warnInfo;
	}

	public static void setWarnInfo(String warnInfo) {
		RealTimeWarnAction.warnInfo = warnInfo;
	}

	public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public int getFetchCount() {
		return fetchCount;
	}

	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}

	public RealTimeWarnDao getRtd() {
		return rtd;
	}

	public void setRtd(RealTimeWarnDao rtd) {
		this.rtd = rtd;
	}

	public RealTimeWarnBIO getRtb() {
		return rtb;
	}

	public void setRtb(RealTimeWarnBIO rtb) {
		this.rtb = rtb;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getColumnID() {
		return columnID;
	}

	public void setColumnID(String columnID) {
		this.columnID = columnID;
	}

	public String getAlarmid() {
		return alarmid;
	}

	public void setAlarmid(String alarmid) {
		this.alarmid = alarmid;
	}

	public String getGather_val() {
		return gather_val;
	}

	public void setGather_val(String gather_val) {
		this.gather_val = gather_val;
	}

	public String getTable_head() {
		return table_head;
	}

	public void setTable_head(String table_head) {
		this.table_head = table_head;
	}

	public String getTable_body() {
		return table_body;
	}

	public void setTable_body(String table_body) {
		this.table_body = table_body;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getTotalref() {
		return totalref;
	}

	public void setTotalref(int totalref) {
		this.totalref = totalref;
	}
	

}
