package com.linkage.liposs.action.securitygw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.dao.securitygw.SgwPerformanceDao;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 增加高级查询及报表样式修改
 * 
 * @author suixz(5253)
 * @version 1.0
 * @category securitygw
 */
public class PBWebTopN extends ActionSupport {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9199703883462586251L;
	private static Logger log= LoggerFactory.getLogger(PBWebTopN.class);
	private PBTopNDAO pbTopN;
	private SgwPerformanceDao sgwPerformanceDao;
	private String imgUrlDay;// 生成的日报表图片路径
	private String imgUrlWeek;// 生成的周报表图片路径
	private String imgUrlMonth;// 生成的月报表图片路径
	private List<Map> dataDay;
	private List<Map> dataWeek;
	private List<Map> dataMonth;
	private Map top1 = new HashMap();
	private Map time = new HashMap();
	private String deviceid;
	private String device_name;
	private String loopback_ip;
	private String remark;
	private String year = "";
	private String month = "";
	private String day = "";
	private String date = "";
	private String start = "";
	private String end = "";
	private int topN = 10;
	private List<String> percentageOfDay;
	private List<String> percentageOfWeek;
	private List<String> percentageOfMon;
 
	private void init() {
		start = initDate(start);
		end = initDate(end);
		if (start.equals("")) {
			initDate();
			start = date;
		} else {
			date = start;
		}
		start = date.replace("_", "-");
		log.debug("date:" + date);
		initTop1();
		initTime();
		initDeviceInfo();
	}

	private void initDeviceInfo() {
		Map map = sgwPerformanceDao.getDevicePerformanceInfo(deviceid, "");
		Object o1 = map.get("device_name");
		device_name = o1 == null ? "" : (String) o1;
		Object o2 = map.get("loopback_ip");
		loopback_ip = o2 == null ? "" : (String) o2;
	}

	private void initTime() {
		time.put("dayStart", date);
		time.put("weekStart", PBTopNDAO.getTimeStr(date, 7));
		time.put("weekEnd", date);
		time.put("monthStart", PBTopNDAO.getTimeStr(date, 30));
		time.put("monthEnd", date);
		log.debug("time=" + time);
	}

	private void initTop1() {
		top1.put("day1ip", "");
		top1.put("day1times", "");
		top1.put("week1ip", "");
		top1.put("week1times", "");
		top1.put("month1ip", "");
		top1.put("month1times", "");
	}

	private void initDate() {
		initDate(new Date().getTime());
	}

	private String initDate(String date) {
		String s = "";
		if (date != null && !date.equals("")) {
			String[] ss = date.split("-");
			s += ss[0] + "_";
			if (ss[1].length() == 1) {
				s += "0" + ss[1] + "_";
			} else {
				s += ss[1] + "_";
			}
			if (ss[2].length() == 1) {
				s += "0" + ss[2];
			} else {
				s += ss[2];
			}
		}
		return s;
	}

	private void initDate(long millisecond) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(millisecond);
		year = "" + cale.get(Calendar.YEAR);
		int m = cale.get(Calendar.MONTH) + 1;
		month = m < 10 ? "0" + m : "" + m;
		int d = cale.get(Calendar.DATE);
		day = d < 10 ? "0" + d : "" + d;
		date = year + "_" + month + "_" + day;
	}

	public String execute() throws Exception {
		init();
		String s = "?deviceid=" + deviceid + "&date=" + date + "&topN=" + topN;
		dataDay = pbTopN.getWebTopNDay(deviceid, date, topN);
		// add by suixz
		int total = 0;
		for (Map m : dataDay) {
			total += Integer.parseInt(m.get("times").toString());
		}
		percentageOfDay = new ArrayList<String>();
		if (dataDay.size() > topN) {
			dataDay = dataDay.subList(0, topN);
		}
		for (Map m : dataDay) {
			String perc = String.valueOf((Double.parseDouble(m.get("times")
					.toString())
					/ total * 100));
		 
			percentageOfDay.add(getFitPrecent(perc));
		}
		if (dataDay != null && dataDay.size() > 0) {
			Map m_ = dataDay.get(0);
			top1.put("day1ip", m_.get("srcip"));
			top1.put("day1times", m_.get("times"));
			for (Map map : dataDay) {
				log.debug("map=" + map);
			}
		}
		dataWeek = pbTopN.getWebTopNWeek(deviceid, date, topN);
		int totalOfWeek = 0;
		for (Map m : dataWeek) {
			totalOfWeek += Integer.parseInt(m.get("times").toString());
		}
		percentageOfWeek = new ArrayList<String>();
		if (dataWeek.size() > topN) {
			dataWeek = dataWeek.subList(0, topN);
		}
		for (Map m : dataWeek) {
			String perc = String.valueOf((Double.parseDouble(m.get("times")
					.toString())
					/ totalOfWeek * 100)); 
			percentageOfWeek.add(getFitPrecent(perc));
		}
		if (dataWeek != null && dataWeek.size() > 0) {
			Map m_ = dataWeek.get(0);
			top1.put("week1ip", m_.get("srcip"));
			top1.put("week1times", m_.get("times"));
			for (Map map : dataWeek) {
				log.debug("map=" + map);
			}
		}
		dataMonth = pbTopN.getWebTopNMonth(deviceid, date, topN);
		int totalOfMon = 0;
		for (Map m : dataMonth) {
			totalOfMon += Integer.parseInt(m.get("times").toString());
		}
		percentageOfMon = new ArrayList<String>();
		if (dataMonth.size() > topN) {
			dataMonth = dataMonth.subList(0, topN);
		}
		for (Map m : dataMonth) {
			String perc="0.000";
			if(totalOfMon!=0){
				perc = String.valueOf((Double.parseDouble(m.get("times")
						.toString())
						/ totalOfMon * 100));
			}
			 
			percentageOfMon.add(getFitPrecent(perc));
		}
		if (dataMonth != null && dataMonth.size() > 0) {
			Map m_ = dataMonth.get(0);
			top1.put("month1ip", m_.get("srcip"));
			top1.put("month1times", m_.get("times"));
			for (Map map : dataMonth) {
				log.debug("map=" + map);
			}
		}
		imgUrlDay = "/securitygw/PBWebTopNAction!getWebTopNDay.action" + s;
		imgUrlWeek = "/securitygw/PBWebTopNAction!getWebTopNWeek.action" + s;
		imgUrlMonth = "/securitygw/PBWebTopNAction!getWebTopNMonth.action" + s;
		return SUCCESS;
	}
	private String getFitPrecent(String perc){
		 
		log.debug("perc="+perc);
		int sIndex = perc.indexOf(".");
		sIndex += 3;
		if(sIndex > perc.length()){
//			log.debug("xxxxxxxxxx   perc="+perc.length());
			sIndex = perc.length();
		} 
		return perc.substring(0, sIndex) + "%";
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Map> getDataDay() {
		return dataDay;
	}

	public void setDataDay(List<Map> dataDay) {
		this.dataDay = dataDay;
	}

	public List<Map> getDataWeek() {
		return dataWeek;
	}

	public void setDataWeek(List<Map> dataWeek) {
		this.dataWeek = dataWeek;
	}

	public List<Map> getDataMonth() {
		return dataMonth;
	}

	public void setDataMonth(List<Map> dataMonth) {
		this.dataMonth = dataMonth;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getTopN() {
		return topN;
	}

	public void setTopN(int topN) {
		this.topN = topN;
	}

	public String getImgUrlDay() {
		return imgUrlDay;
	}

	public void setImgUrlDay(String imgUrlDay) {
		this.imgUrlDay = imgUrlDay;
	}

	public String getImgUrlWeek() {
		return imgUrlWeek;
	}

	public void setImgUrlWeek(String imgUrlWeek) {
		this.imgUrlWeek = imgUrlWeek;
	}

	public String getImgUrlMonth() {
		return imgUrlMonth;
	}

	public void setImgUrlMonth(String imgUrlMonth) {
		this.imgUrlMonth = imgUrlMonth;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public PBTopNDAO getPbTopN() {
		return pbTopN;
	}

	public void setPbTopN(PBTopNDAO pbTopN) {
		this.pbTopN = pbTopN;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getLoopback_ip() {
		return loopback_ip;
	}

	public void setLoopback_ip(String loopback_ip) {
		this.loopback_ip = loopback_ip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Map getTop1() {
		return top1;
	}

	public void setTop1(Map top1) {
		this.top1 = top1;
	}

	public Map getTime() {
		return time;
	}

	public void setTime(Map time) {
		this.time = time;
	}

	public SgwPerformanceDao getSgwPerformanceDao() {
		return sgwPerformanceDao;
	}

	public void setSgwPerformanceDao(SgwPerformanceDao sgwPerformanceDao) {
		this.sgwPerformanceDao = sgwPerformanceDao;
	}

	public List<String> getPercentageOfDay() {
		return percentageOfDay;
	}

	public List<String> getPercentageOfWeek() {
		return percentageOfWeek;
	}

	public List<String> getPercentageOfMon() {
		return percentageOfMon;
	}
}
