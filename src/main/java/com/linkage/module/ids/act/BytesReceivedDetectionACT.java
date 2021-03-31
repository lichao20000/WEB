package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.ids.bio.BytesReceivedDetectionBIO;

@SuppressWarnings("serial")
public class BytesReceivedDetectionACT extends splitPageAction {

	private Logger logger = LoggerFactory
			.getLogger(BytesReceivedDetectionACT.class);

	private BytesReceivedDetectionBIO bio;


	private String starttime;
	private String endtime;
	private String queryTimeType;

	private String deviceSerialnumber;

	private String loid;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private List<Map<String, Object>> list = null;

	@Override
	public String execute() throws Exception {
		logger.debug("init()");
		starttime = getStartTempDate();
		endtime = getEndTempDate();
		return "success";
	}

	public String queryLanAndPonData() {
		String indexName="ids";
		String indexType="";
		list = bio.queryLanAndPonData(indexName, indexType, starttime, endtime,
				queryTimeType, deviceSerialnumber, loid, curPage_splitPage,
				num_splitPage);
//		maxPage_splitPage=bio.queryLanAndPonDataCount(indexName, indexType, starttime, endtime,
//				queryTimeType, deviceSerialnumber, loid, curPage_splitPage,
//				num_splitPage);
		return "list";
	}
	
	@SuppressWarnings("unchecked")
	public String queryLanAndPonDataExcel(){
		String indexName="ids";
		String indexType="";
		column = new String[] { "loid", "device_serialnumber", "bytes1","bytespert1","bytes2","bytespert2","bytes3","bytespert3", "bytes4","bytespert4","ponbytes","upload_time","add_time"};
		title = new String[] { "loid", "设备序列号", "LAN1口流量","LAN1口平均速率", "LAN2口流量","LAN2口平均速率","LAN3口流量","LAN3口平均速率", "LAN4口流量","LAN4口平均速率", "PON口流量","上报时间", "入库时间"};
		fileName = "单用户网口流量监控";
		data = (List)bio.queryLanAndPonData(indexName, indexType, starttime, endtime,
				queryTimeType, deviceSerialnumber, loid, curPage_splitPage,
				num_splitPage);
		return "excel";
	}

	// 当前时间的23:59:59,如 2011-05-11 00:00:00
	private String getStartTempDate() {
		DateTimeUtil dt = new DateTimeUtil(new Date());
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 00);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndTempDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}


	public BytesReceivedDetectionBIO getBio() {
		return bio;
	}

	public void setBio(BytesReceivedDetectionBIO bio) {
		this.bio = bio;
	}


	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public String getQueryTimeType() {
		return queryTimeType;
	}

	public void setQueryTimeType(String queryTimeType) {
		this.queryTimeType = queryTimeType;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}


}
