package bio.webtopo.warn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.GatherIDEvent;
import bio.webtopo.warn.column.ColumnInterface;
import bio.webtopo.warn.filter.BaseFilter;
import bio.webtopo.warn.filter.CacheBaseFilter;
import bio.webtopo.warn.filter.EventFilterUtil;

import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.webtopo.MCDataSource;
import com.linkage.litms.webtopo.Scheduler;

import dao.webtopo.warn.RealTimeWarnDao;

/**
 * WebTopo实时告警牌BIO
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 贲友朋
 * @version 1.0
 * @since 2008-4-8
 * @category WebTopo/实时告警牌
 * 
 */
public class RealTimeWarnBIO {
	private static final Logger LOG = LoggerFactory.getLogger(RealTimeWarnBIO.class);
	private RealTimeWarnDao rwd;// 实时告警牌DAO
	private static ArrayList ColumnList = null;
	private CacheBaseFilter cacheFilter = CacheBaseFilter.getInstance();
	private EventFilterUtil filterUtil;
	private BaseFilter eventFilterObj = null;
	private BaseFilter clearEvenFilterObj = null;
	private HashMap<Integer,Integer> WarnLevMap=new HashMap<Integer,Integer>(6);
	// *************************************************************
	/**
	 * 获取用户告警声音
	 */
	public String GetWarnVoiceStr(long acc_oid) {
		List<Map> warns = rwd.getWarnInfoList(acc_oid);
		String warnInfo = "{";
		for (Map map : warns) {
			warnInfo += map.get("warnlevel") + ":{warnvoice:'"
					+ map.get("warnvoice") + "',voicetype:'"
					+ map.get("voicetype") + "'},";
		}
		int pos = warnInfo.lastIndexOf(",");
		if (pos != -1) {
			warnInfo = warnInfo.substring(0, pos);
		}
		warnInfo += "}";
		return warnInfo;
	}

	/**
	 * 确认告警
	 * @param account
	 * @param passwd
	 * @param alarmList
	 * @return
	 */
	public String ConfigAlarm(String account,String passwd,String[] alarmList){
		MCDataSource mcs = new MCDataSource(account, passwd);
		return String.valueOf(mcs.AckAlarm(alarmList));
	}
	/**
	 * 删除告警
	 * @param alarmList:需要删除的告警列表:m_Alarmid+m_GatherID
	 * @param account
	 * @param passwd
	 * @return
	 */
	public String DelWarn(String[] alarmList,String account,String passwd){
		MCDataSource mcs = new MCDataSource(account, passwd);
		return String.valueOf(mcs.RemoveAlarm(alarmList));
	}
	/**
	 * 获得清除告警
	 * @param account
	 * @param passwd
	 * @return
	 */
	public String getClearWarnList(String area_id,String gather,int rule_id){
		GatherIDEvent obj = new GatherIDEvent();
		String[] tmp=gather.split(";");
		int n=tmp.length;
		GatherIDEvent[] gatherlist=new GatherIDEvent[n];
		for(int i=0;i<n;i++){
			obj.gather_id=tmp[i].split(":")[0];
			obj.max_event_id=tmp[i].split(":")[1];
			gatherlist[i]=obj;
		}
		Scheduler sc=new Scheduler();
		RemoteDB.AlarmEvent[] result;
		result = sc.getClearAlarm(area_id, gatherlist);
		n=result.length;
		filterUtil = cacheFilter.getFilterUtil(rule_id);
		clearEvenFilterObj = filterUtil.getClearEventFilter();
		String id="";
		String temp="";
		RemoteDB.AlarmEvent e;
		for(int i=0;i<n;i++){
			e = result[i];
			e.m_strCity       = Encoder.AsciiToChineseString(e.m_strCity);
			e.m_CreatorName   = Encoder.AsciiToChineseString(e.m_CreatorName);
			e.m_SourceName    = Encoder.AsciiToChineseString(e.m_SourceName);
			e.m_DisplayString = Encoder.AsciiToChineseString(e.m_DisplayString);
			//e.buss_name       = Encoder.AsciiToChineseString(e.buss_name);
			if (clearEvenFilterObj.accept(e)) {// 茂才告警过滤模块
				LOG.debug("清除告警：" + e.m_SourceName + " time=" + e.m_AckTime);
				id+="-/-"+e.m_Number+"-"+e.m_GatherID;
				temp+=",'"+e.m_Number+"-"+e.m_GatherID+"':{t:'"+(e.m_AckTime==0?"":StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",e.m_AckTime))+"',s:'"+e.m_DisplayString+"'}";
			}
		}
		return id.equals("")?"":"{id:'"+id.substring(3)+"'"+temp+"}";
	}
	/**
	 * 从MC获取最近的200条告警
	 * 
	 * @param serialno
	 *                当前取告警前，获得的最大告警序列号（无用）
	 * @param fetchCount
	 *                需要获取告警的条数（无用）
	 * @param maxSerialNo
	 *                从MC获取告警后，得到的告警最大序列号
	 * @param gatherId
	 *                采集点
	 * @param area_id
	 *                域id
	 * @return 返回数据
	 */
	private RemoteDB.AlarmEvent[] getAlarmEventList(int serialno,
	    int fetchCount, org.omg.CORBA.IntHolder maxSerialHolder,
	    String gatherId, String area_id) {
	    maxSerialHolder.value = 0;
	    Scheduler scheduler = new Scheduler();
	    GatherIDEvent gatherIdEvent = new GatherIDEvent();
	    gatherIdEvent.gather_id = gatherId;
	    gatherIdEvent.max_event_id = "0";
	    RemoteDB.AlarmEvent[] result = scheduler.getAllAlarm(area_id, new GatherIDEvent[]{gatherIdEvent});
	    gatherIdEvent = null;
	    fetchCount = result.length;
	    if(fetchCount>0){
	    	maxSerialHolder.value = Integer.parseInt(result[0].m_Number);
	    }else{
	    	maxSerialHolder.value=0;
	    }
	    LOG.debug("从MC获取告警 length=" + result.length + " 最大序列号=" + maxSerialHolder.value);
	    return result;
	}
	/**
	 * 初始化进来获取所有告警
	 * 
	 * @param account
	 * @param passwd
	 * @param gatherlist
	 * @param rule_id
	 * @param fetchCount
	 * @param area_id
	 * @return
	 */
	public String[] getAllWarnData(String account, String passwd,List<String> gatherlist,int rule_id,int fetchCount,String area_id){
		String[] str = new String[4];
		//封装GatherList
		long start = System.currentTimeMillis();
		//获取列对象
		Collection<ColumnInterface> listColumn = ColumnInit.getInstance().getColumn();
		Iterator<ColumnInterface> it= listColumn.iterator();
		StringBuffer bf = new StringBuffer();// 数据
		bf.append("<tr>");
		String column="";
		ColumnInterface ci=null;
		
		while (it.hasNext()) {
			ci=it.next();
			bf.append("<th>").append(ci.getName()).append("</th>");
			column+=ci.getId()+"-/-";
		}
		bf.append("</tr>");
		str[0]=bf.toString();
		bf=null;
		bf=new StringBuffer();
		filterUtil = cacheFilter.getFilterUtil(rule_id);
		MCDataSource mcs = new MCDataSource(account, passwd);
		org.omg.CORBA.IntHolder maxSerialHolder = new org.omg.CORBA.IntHolder();
		String data = "";
		int n = gatherlist.size();
		RemoteDB.AlarmEvent[] result;
		for(int i=0;i<n;i++){
			result = getAlarmEventList(0,0,maxSerialHolder,gatherlist.get(i), area_id);//mcs.getAlarmEventList(0, 400/n, maxSerialHolder,gatherlist.get(i), area_id);
			data += gatherlist.get(i)+":" + maxSerialHolder.value + ";";
			bf = DealWithFilter( bf, result, listColumn);
		}
		long end = System.currentTimeMillis();
		LOG.debug("过滤时间：" + (end - start) / 1000.00);
		if(data.contains(";"))
		    data=data.substring(0,data.length()-1);
		str[1] = bf.toString();
		str[2] = data;
		if(column.contains("-/-")){
		    column=column.substring(0,column.length()-3);
		}
		str[3]=column;
		bf=null;
		return str;
	}

	/**
	 * 刷新告警调用函数
	 * @param account
	 * @param passwd
	 * @param gatherlist
	 * @param rule_id
	 * @param serialno
	 * @param fetchCount
	 * @param maxSerialNo
	 * @param Columnmap
	 * @param area_id
	 * @param ColumnID
	 * @return
	 */
	public String getRefWarnData(String account, String passwd,
			List gatherlist, int rule_id, HashMap<String,Integer> serialMap, int fetchCount,
		    String area_id,String ColumnID) {
		if(ColumnID==null || ColumnID.trim().equals("")){
			return null;
		}
		long start = System.currentTimeMillis();
		Collection<ColumnInterface> listColumn = ColumnInit.getInstance().getColumn(ColumnID.split("-/-"));
		Iterator<ColumnInterface> it = listColumn.iterator();
		StringBuffer bf = new StringBuffer();// 数据
		filterUtil = cacheFilter.getFilterUtil(rule_id);
		int n = gatherlist.size();// 采集点列表的数目
		MCDataSource mcs = new MCDataSource(account, passwd);
		RemoteDB.AlarmEvent[] result;
		String gather = "";
		org.omg.CORBA.IntHolder maxSerialHolder = new org.omg.CORBA.IntHolder();
		Integer serialno = 0;
		for (int i = 0; i < n; i++) {
		    //获取web这边已经得到的最大序列号
		    serialno = serialMap.get(gatherlist.get(i));
		    result = mcs.getAlarmEventList(serialno, fetchCount/n, maxSerialHolder, (String)gatherlist.get(i), area_id);
		    bf = DealWithFilterSecond(bf, result, listColumn);
		    //如果MC那边没有取到新告警时,最大序列号为变为0,所以web这边需要判断
		    if(maxSerialHolder.value == 0){
			maxSerialHolder.value = serialno;
		    }
		    gather += ";" + gatherlist.get(i) + ":" + maxSerialHolder.value;
		}
		long end = System.currentTimeMillis();
		LOG.debug("过滤时间：" + (end - start) / 1000.00);
		String data="{data:'"+bf.toString()+"',gather:'"+(gather.equals("")?"":gather.substring(1))+"',";
		LOG.debug("获取更新告警最大告警序列号:"+account+":"+gather);
		bf = null;
		for(int i=0;i<6;i++){
		    data+="lev_"+i+":"+(WarnLevMap.get(i)==null?0:WarnLevMap.get(i))+",";
		}
		data=data.substring(0,data.length()-1)+"}";
		return data;
	}
	/**
	 * 根据告警过滤模块过滤告警
	 * 
	 * @param ColumnList
	 * @param data
	 * @param result
	 * @return
	 */
	private StringBuffer DealWithFilterSecond(StringBuffer bf,
			RemoteDB.AlarmEvent[] result, Collection<ColumnInterface> listColumn) {
		int length = result.length;
		if (length > 0) {
			LOG.debug("开始过滤.............");
			eventFilterObj = filterUtil.getEventFilter();
			RemoteDB.AlarmEvent e;
			Iterator<ColumnInterface> it;
			for (int i = length-1; i >=0; i--) {// 遍历结果集
				e = result[i];
				e.m_strCity       = Encoder.AsciiToChineseString(e.m_strCity);
				//e.segmentName     = Encoder.AsciiToChineseString(e.segmentName);
				e.m_CreatorName   = Encoder.AsciiToChineseString(e.m_CreatorName);
				e.m_SourceName    = Encoder.AsciiToChineseString(e.m_SourceName);
				e.m_DisplayTitle  = Encoder.AsciiToChineseString(e.m_DisplayTitle);
				e.m_DisplayString = Encoder.AsciiToChineseString(e.m_DisplayString);
				//e.buss_name       = Encoder.AsciiToChineseString(e.buss_name);
				if (eventFilterObj.accept(e)) {// 茂才告警过滤模块
					//log.debug("过滤=" + e.m_Number + " level=" + e.m_Severity + "WarnLevMap="+WarnLevMap);
					bf.append("<tr ").append(" class=\\\"level_").append(e.m_Severity).append(" tr_b\\\"")
									 .append(" id=\\\"").append(e.m_Number).append("-").append(e.m_GatherID).append("\\\"")
									 .append(" lev=").append(e.m_Severity)
									 .append(" time=").append(e.m_CreateTime)
									 .append(" devid=").append(e.m_DeviceCoding)
									 .append(">");
					it = listColumn.iterator();
					while (it.hasNext()) {
						bf.append(it.next().getValue(e));
					}
					bf.append("</tr>");
					WarnLevMap.put((int)e.m_Severity,(WarnLevMap.get((int)e.m_Severity)==null?1:WarnLevMap.get((int)e.m_Severity)+1));
				}
			}
			LOG.debug("过滤完成size=" + length);
		}
		return bf;
	}
	/**
	 * 根据告警过滤模块过滤告警
	 * 
	 * @param ColumnList
	 * @param data
	 * @param result
	 * @return
	 */
	private StringBuffer DealWithFilter(StringBuffer bf,
			RemoteDB.AlarmEvent[] result, Collection<ColumnInterface> listColumn) {
		int length = result.length;
		if (length > 0) {
			RemoteDB.AlarmEvent e;
			LOG.debug("开始过滤.............");
			eventFilterObj = filterUtil.getEventFilter();
			Iterator<ColumnInterface> it;
			for (int i = 0; i < length; i++) {// 遍历结果集
				e = result[i];
				e.m_strCity       = Encoder.AsciiToChineseString(e.m_strCity);
			//	e.segmentName     = Encoder.AsciiToChineseString(e.segmentName);
				e.m_CreatorName   = Encoder.AsciiToChineseString(e.m_CreatorName);
				e.m_SourceName    = Encoder.AsciiToChineseString(e.m_SourceName);
				e.m_DisplayTitle  = Encoder.AsciiToChineseString(e.m_DisplayTitle);
				e.m_DisplayString = Encoder.AsciiToChineseString(e.m_DisplayString);
			//	e.buss_name       = Encoder.AsciiToChineseString(e.buss_name);
				if (eventFilterObj.accept(e)) {// 茂才告警过滤模块
					LOG.debug("过滤=" + e.m_Number + " level=" + e.m_Severity + "WarnLevMap="+WarnLevMap);
					bf.append("<tr ").append(" class=level_").append(e.m_Severity)
									 .append(" id=\\\"").append(e.m_Number).append("-").append(e.m_GatherID).append("\\\"")
									 .append(" lev=").append(e.m_Severity)
									 .append(" time=").append(e.m_CreateTime)
									 .append(" devid=").append(e.m_DeviceCoding)
									 .append(">");
					it = listColumn.iterator();
					while (it.hasNext()) {
						bf.append(it.next().getValue(e));
					}
					bf.append("</tr>");
					WarnLevMap.put((int)e.m_Severity,(WarnLevMap.get((int)e.m_Severity)==null?1:WarnLevMap.get((int)e.m_Severity)+1));
				}
			}
			LOG.debug("过滤完成size=" + length);
		}
		return bf;
	}

	// **********************************************
	public void setRwd(RealTimeWarnDao rwd) {
		this.rwd = rwd;
	}

}
