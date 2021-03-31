package com.linkage.liposs.action.securitygw;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.liposs.buss.dao.securitygw.SgwPerformanceDao;

/**
 * @author Zhaof(工号) tel：12345678
 * @version 1.0
 * @since Apr 8, 2008
 * @category com.linkage.liposs.action.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class PBDetailAction extends splitPageAction
{
	private static final long serialVersionUID = 75098372198375982L;
	private static Logger log = LoggerFactory.getLogger(PBDetailAction.class);
	
	private PBTopNDAO pbTopN;
	private SgwPerformanceDao sgwPerformanceDao;
	private String deviceid;// 设备id
	private String device_name;// 设备名称
	private String loopback_ip;
	private String tip;// 目标用户IP
	private String sip;// 源用户IP
	private String tp;// 目标端口
	private String sp;// 源端口
	private String tm;// 目标Mac
	private String sm;// 源Mac
	private String prot_type; // 协议类型
	private String date; // 页面传递的时间
	private String step;// 步长 1：天 7：周 30：月
	private String start_time; // 开始时间
	private String end_time; // 结束时间
	private String numPage;// 每页显示的页数
	private String maxPage;// 总共显示的页数 0:全部显示
	private List<Map> connDetails;
	private void init()
	{
		if (numPage == null || numPage.equals(""))
			{
				numPage = "50";
			}
		if (maxPage == null || maxPage.equals(""))
			{
				maxPage = "0";
			}
		if (date != null && !date.equals("")) {
			if (step == null || step.equals("")) {
				step = "1";
			}
			String[] time = getTime(date, Integer.parseInt(step));
			if (time != null && time.length == 2) {
				start_time = time[0];
				end_time = time[1];
			}
		} else {
			if (start_time == null || start_time.equals("") || end_time == null || end_time.equals("")) {
				Date d = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String s = df.format(d);
				String[] time = getTime(s, 1);
				if (time != null && time.length == 2) {
					start_time = time[0];
					end_time = time[1];
				}
			}
		}
		log.debug("-----------------------------" + start_time);
		log.debug("-----------------------------" + end_time);
		initDeviceInfo();
	}
	private void initDeviceInfo()
	{
		log.debug("paramList_splitPage------------------------------------" + paramList_splitPage);
		Map map = sgwPerformanceDao.getDevicePerformanceInfo(deviceid, "");
		Object o1 = map.get("device_name");
		device_name = o1 == null ? "" : (String) o1;
		Object o2 = map.get("loopback_ip");
		loopback_ip = o2 == null ? "" : (String) o2;
	}
	private String[] getTime(String date, int step) {
		String[] time = new String[2];
		String start = date.replace("-", "_");
		if (step == 1) {
			time[0] = date;
			time[1] = PBTopNDAO.getTimeStr(start, step).replace("_", "-");
		} else {
			time[0] = PBTopNDAO.getTimeStr(start, step).replace("_", "-");
			time[1] = date;
		}
		time[0] += " 00:00:00";
		time[1] += " 00:00:00";
		return time;
	}
	/**
	 * 查询网络连接情况的原始信息
	 * 
	 * @return：queryData
	 */
	public String execute() throws Exception
	{
		init();
		String sql = "";
		// 设置每页显示的页数
		setNum_splitPage(Integer.parseInt(numPage));
		maxPage_splitPage = Integer.parseInt(maxPage); // 总共显示的页数
		connDetails = null;
		sql = pbTopN.getConnOriginalDataSQL(deviceid, tip, sip, tp, sp, tm, sm, prot_type,
				start_time, end_time);
		maxPage_splitPage = pbTopN.getCountForOriginalData(sql);		
		// 计算最大页大小
		maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
				: (maxPage_splitPage / num_splitPage + 1));
		connDetails = pbTopN.getConnOriginalData(sql, curPage_splitPage, num_splitPage);
		return "queryData";
	}
	
	public PBTopNDAO getPbTopN()
	{
		return pbTopN;
	}
	public void setPbTopN(PBTopNDAO pbTopN)
	{
		this.pbTopN = pbTopN;
	}
	public String getDeviceid()
	{
		return deviceid;
	}
	public void setDeviceid(String deviceid)
	{
		this.deviceid = deviceid;
	}
	public String getTip()
	{
		return tip;
	}
	public void setTip(String tip)
	{
		this.tip = tip;
	}
	public String getSip()
	{
		return sip;
	}
	public void setSip(String sip)
	{
		this.sip = sip;
	}
	public String getTp()
	{
		return tp;
	}
	public void setTp(String tp)
	{
		this.tp = tp;
	}
	public String getSp()
	{
		return sp;
	}
	public void setSp(String sp)
	{
		this.sp = sp;
	}
	public String getTm()
	{
		return tm;
	}
	public void setTm(String tm)
	{
		this.tm = tm;
	}
	public String getSm()
	{
		return sm;
	}
	public void setSm(String sm)
	{
		this.sm = sm;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getStep()
	{
		return step;
	}
	public void setStep(String step)
	{
		this.step = step;
	}
	public String getNumPage()
	{
		return numPage;
	}
	public void setNumPage(String numPage)
	{
		this.numPage = numPage;
	}
	public String getMaxPage()
	{
		return maxPage;
	}
	public void setMaxPage(String maxPage)
	{
		this.maxPage = maxPage;
	}
	public List<Map> getConnDetails()
	{
		return connDetails;
	}
	public void setConnDetails(List<Map> connDetails)
	{
		this.connDetails = connDetails;
	}
	public String getProt_type()
	{
		return prot_type;
	}
	public void setProt_type(String prot_type)
	{
		this.prot_type = prot_type;
	}
	public String getStart_time()
	{
		return start_time;
	}
	public void setStart_time(String start_time)
	{
		this.start_time = start_time;
	}
	public String getEnd_time()
	{
		return end_time;
	}
	public void setEnd_time(String end_time)
	{
		this.end_time = end_time;
	}
	public String getDevice_name()
	{
		return device_name;
	}
	public void setDevice_name(String device_name)
	{
		this.device_name = device_name;
	}
	public SgwPerformanceDao getSgwPerformanceDao()
	{
		return sgwPerformanceDao;
	}
	public void setSgwPerformanceDao(SgwPerformanceDao sgwPerformanceDao)
	{
		this.sgwPerformanceDao = sgwPerformanceDao;
	}
	public String getLoopback_ip()
	{
		return loopback_ip;
	}
	public void setLoopback_ip(String loopback_ip)
	{
		this.loopback_ip = loopback_ip;
	}
}
