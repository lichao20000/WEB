package com.linkage.module.gwms.report.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.act.interf.I_OnlineDevStatConfigACT;
import com.linkage.module.gwms.report.bio.interf.I_OnlineDevStatConfigBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author qxq(4174)
 * @date 2009-6-4
 */
public class OnlineDevStatConfigACT extends ActionSupport implements I_OnlineDevStatConfigACT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger log = LoggerFactory
			.getLogger(OnlineDevStatConfigACT.class);

	private I_OnlineDevStatConfigBIO onlineDevStatConfigBIO;
	
	List cityList = null;
	
	TimeBean timeBean = null;
	
	private String msg = null;
	
	String time_point[];
	String city_id[];
	
	public String execute() {
		
		log.debug("OnlineDevStatConfigAction=>execute start");
		
		onlineDevStatConfigBIO.batchConfig(this.time_point, this.city_id);
		
		onlineConfig();
		
		this.msg = "定制成功！";
		
		return SUCCESS;
		
	}

	public String onlineConfig(){
		
		
		this.cityList = onlineDevStatConfigBIO.getCity();
		
		List listTime = onlineDevStatConfigBIO.getTimePoint();
		
		timeBean = new TimeBean();
		
		for(int j=0;j<listTime.size();j++){
			Map tempTime = (Map) listTime.get(j);
			
			if("0".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime0("checked");
			}
			if("1".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime1("checked");
			}
			if("2".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime2("checked");
			}
			if("3".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime3("checked");
			}
			if("4".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime4("checked");
			}
			if("5".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime5("checked");
			}
			if("6".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime6("checked");
			}
			if("7".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime7("checked");
			}
			if("8".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime8("checked");
			}
			if("9".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime9("checked");
			}
			if("10".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime10("checked");
			}
			if("11".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime11("checked");
			}
			if("12".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime12("checked");
			}
			if("13".equals(String.valueOf(tempTime.get("time_point").toString().trim()))){
				timeBean.setTime13("checked");
			}
			if("14".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime14("checked");
			}
			if("15".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime15("checked");
			}
			if("16".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime16("checked");
			}
			if("17".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime17("checked");
			}
			if("18".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime18("checked");
			}
			if("19".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime19("checked");
			}
			if("20".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime20("checked");
			}
			if("21".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime21("checked");
			}
			if("22".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime22("checked");
			}
			if("23".equals(String.valueOf(tempTime.get("time_point").toString()).trim())){
				timeBean.setTime23("checked");
			}
		}
		
		// 返回页面
		return SUCCESS;
	}
	
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the city_id
	 */
	public String[] getCity_id() {
		return city_id;
	}

	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(String[] city_id) {
		this.city_id = city_id;
	}

	/**
	 * @return the time_point
	 */
	public String[] getTime_point() {
		return time_point;
	}

	/**
	 * @param time_point the time_point to set
	 */
	public void setTime_point(String[] time_point) {
		this.time_point = time_point;
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
	 * @return the timeBean
	 */
	public TimeBean getTimeBean() {
		return timeBean;
	}

	/**
	 * @param timeBean the timeBean to set
	 */
	public void setTimeBean(TimeBean timeBean) {
		this.timeBean = timeBean;
	}

	/**
	 * @return the onlineDevStatConfigBIO
	 */
	public I_OnlineDevStatConfigBIO getOnlineDevStatConfigBIO() {
		return onlineDevStatConfigBIO;
	}

	/**
	 * @param onlineDevStatConfigBIO the onlineDevStatConfigBIO to set
	 */
	public void setOnlineDevStatConfigBIO(
			I_OnlineDevStatConfigBIO onlineDevStatConfigBIO) {
		this.onlineDevStatConfigBIO = onlineDevStatConfigBIO;
	}
	
}

class TimeBean{
	
	String time0 = "";
	String time1 = "";
	String time2 = "";
	String time3 = "";
	String time4 = "";
	String time5 = "";
	String time6 = "";
	String time7 = "";
	String time8 = "";
	String time9 = "";
	String time10 = "";
	String time11 = "";
	String time12 = "";
	String time13 = "";
	String time14 = "";
	String time15 = "";
	String time16 = "";
	String time17 = "";
	String time18 = "";
	String time19 = "";
	String time20 = "";
	String time21 = "";
	String time22 = "";
	String time23 = "";
	/**
	 * @return the time0
	 */
	public String getTime0() {
		return time0;
	}
	/**
	 * @param time0 the time0 to set
	 */
	public void setTime0(String time0) {
		this.time0 = time0;
	}
	/**
	 * @return the time1
	 */
	public String getTime1() {
		return time1;
	}
	/**
	 * @param time1 the time1 to set
	 */
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	/**
	 * @return the time10
	 */
	public String getTime10() {
		return time10;
	}
	/**
	 * @param time10 the time10 to set
	 */
	public void setTime10(String time10) {
		this.time10 = time10;
	}
	/**
	 * @return the time11
	 */
	public String getTime11() {
		return time11;
	}
	/**
	 * @param time11 the time11 to set
	 */
	public void setTime11(String time11) {
		this.time11 = time11;
	}
	/**
	 * @return the time12
	 */
	public String getTime12() {
		return time12;
	}
	/**
	 * @param time12 the time12 to set
	 */
	public void setTime12(String time12) {
		this.time12 = time12;
	}
	/**
	 * @return the time13
	 */
	public String getTime13() {
		return time13;
	}
	/**
	 * @param time13 the time13 to set
	 */
	public void setTime13(String time13) {
		this.time13 = time13;
	}
	/**
	 * @return the time14
	 */
	public String getTime14() {
		return time14;
	}
	/**
	 * @param time14 the time14 to set
	 */
	public void setTime14(String time14) {
		this.time14 = time14;
	}
	/**
	 * @return the time15
	 */
	public String getTime15() {
		return time15;
	}
	/**
	 * @param time15 the time15 to set
	 */
	public void setTime15(String time15) {
		this.time15 = time15;
	}
	/**
	 * @return the time16
	 */
	public String getTime16() {
		return time16;
	}
	/**
	 * @param time16 the time16 to set
	 */
	public void setTime16(String time16) {
		this.time16 = time16;
	}
	/**
	 * @return the time17
	 */
	public String getTime17() {
		return time17;
	}
	/**
	 * @param time17 the time17 to set
	 */
	public void setTime17(String time17) {
		this.time17 = time17;
	}
	/**
	 * @return the time18
	 */
	public String getTime18() {
		return time18;
	}
	/**
	 * @param time18 the time18 to set
	 */
	public void setTime18(String time18) {
		this.time18 = time18;
	}
	/**
	 * @return the time19
	 */
	public String getTime19() {
		return time19;
	}
	/**
	 * @param time19 the time19 to set
	 */
	public void setTime19(String time19) {
		this.time19 = time19;
	}
	/**
	 * @return the time2
	 */
	public String getTime2() {
		return time2;
	}
	/**
	 * @param time2 the time2 to set
	 */
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	/**
	 * @return the time20
	 */
	public String getTime20() {
		return time20;
	}
	/**
	 * @param time20 the time20 to set
	 */
	public void setTime20(String time20) {
		this.time20 = time20;
	}
	/**
	 * @return the time21
	 */
	public String getTime21() {
		return time21;
	}
	/**
	 * @param time21 the time21 to set
	 */
	public void setTime21(String time21) {
		this.time21 = time21;
	}
	/**
	 * @return the time22
	 */
	public String getTime22() {
		return time22;
	}
	/**
	 * @param time22 the time22 to set
	 */
	public void setTime22(String time22) {
		this.time22 = time22;
	}
	/**
	 * @return the time23
	 */
	public String getTime23() {
		return time23;
	}
	/**
	 * @param time23 the time23 to set
	 */
	public void setTime23(String time23) {
		this.time23 = time23;
	}
	/**
	 * @return the time3
	 */
	public String getTime3() {
		return time3;
	}
	/**
	 * @param time3 the time3 to set
	 */
	public void setTime3(String time3) {
		this.time3 = time3;
	}
	/**
	 * @return the time4
	 */
	public String getTime4() {
		return time4;
	}
	/**
	 * @param time4 the time4 to set
	 */
	public void setTime4(String time4) {
		this.time4 = time4;
	}
	/**
	 * @return the time5
	 */
	public String getTime5() {
		return time5;
	}
	/**
	 * @param time5 the time5 to set
	 */
	public void setTime5(String time5) {
		this.time5 = time5;
	}
	/**
	 * @return the time6
	 */
	public String getTime6() {
		return time6;
	}
	/**
	 * @param time6 the time6 to set
	 */
	public void setTime6(String time6) {
		this.time6 = time6;
	}
	/**
	 * @return the time7
	 */
	public String getTime7() {
		return time7;
	}
	/**
	 * @param time7 the time7 to set
	 */
	public void setTime7(String time7) {
		this.time7 = time7;
	}
	/**
	 * @return the time8
	 */
	public String getTime8() {
		return time8;
	}
	/**
	 * @param time8 the time8 to set
	 */
	public void setTime8(String time8) {
		this.time8 = time8;
	}
	/**
	 * @return the time9
	 */
	public String getTime9() {
		return time9;
	}
	/**
	 * @param time9 the time9 to set
	 */
	public void setTime9(String time9) {
		this.time9 = time9;
	}
	
}