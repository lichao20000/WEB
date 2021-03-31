package com.linkage.module.itms.resource.act;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.bio.MemBookBIO;

import action.splitpage.splitPageAction;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年12月18日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MemBookACT extends splitPageAction implements ServletResponseAware,ServletRequestAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8096514308617455774L;
	private static Logger logger = LoggerFactory.getLogger(MemBookACT.class);
	private HttpServletResponse response;
	private HttpServletRequest request;
	private MemBookBIO bio;
	private String ajax;
	//查询信息
	private String busNo;
	private String vendor = "";
	private String vendor_id;
	private String spec_type = "";
	private String spec_model = "";
	private String hardware = "";
	private String software = "";
	private String workOpts = "";
	private String connPerson = "";
	private String connPhone;
	private String reception = "";
	private String startTime;
	private String endTime;
	
	private List<Map> workMemorialList;
	private List<HashMap<String, String>> vendorList = null;
	private List<HashMap<String, String>> specList = null;
	private List<HashMap<String, String>> workOptsList = null;
	private List<HashMap<String, String>> receptionsList = null;
	
	//保存信息
	private String operType ;
	//导出
	private String workContent = "";
	private int status = 0;
	private String add_file;
	private String remark;
	/** 导出数据 */
	private List<HashMap<String,String>> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	
	//导入
	private String gwShare_fileName;
	
	public String init(){
		startTime = getStartDate();
		endTime = getEndDate();
		return "init";
	}
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		//now.set(GregorianCalendar.DATE, 1);
		//now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = "";
		time = fmtrq.format(now.getTime());
		return time;
	} 
	
	//查询
	public String queryList(){
		setFilterEncode(); 
		setTime();
		logger.warn("queryList:{},{},{},{},{},{},{},{},{},{},{},{}",new Object[]{busNo,vendor, spec_type,spec_model, hardware, software,
				workOpts, connPerson, connPhone, reception, startTime, endTime});
		workMemorialList = bio.qryWorkList(busNo, vendor, spec_type,spec_model, hardware, software, 
				workOpts, connPerson, connPhone, reception, startTime, endTime, curPage_splitPage, num_splitPage);
		int total = bio.qryWorkListCount(busNo, vendor, spec_type,spec_model, hardware, software, 
				workOpts, connPerson, connPhone, reception, startTime, endTime);	
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		logger.warn("queryList -> paramList_splitPage:{}",paramList_splitPage);
		paramList_splitPage = parseParamListSplitPage();
		return "list";
	}
	
	public String queryNextList(){
		setFilterEncodeForUpdate(); 
		setTime();
		logger.warn("queryNextList:{},{},{},{},{},{},{},{},{},{},{},{}",new Object[]{busNo,vendor, spec_type,spec_model, hardware, software,
				workOpts, connPerson, connPhone, reception, startTime, endTime});
		workMemorialList = bio.qryWorkList(busNo, vendor, spec_type,spec_model, hardware, software, 
				workOpts, connPerson, connPhone, reception, startTime, endTime, curPage_splitPage, num_splitPage);
		int total = bio.qryWorkListCount(busNo, vendor, spec_type,spec_model, hardware, software, 
				workOpts, connPerson, connPhone, reception, startTime, endTime);	
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		logger.warn("queryNextList -> sparamList_splitPage:{}",paramList_splitPage);
		paramList_splitPage = parseParamListSplitPage();
		return "list";
	}
	
	public String parseParamListSplitPage(){
		
		//connPerson_b_b__a_a_hardware_b_b__a_a_
		//vendor_b_b_-1_a_a_
		//endTime_b_b_2020-01-02 23:59:59_a_a_
		//参数间分隔符
		String code1 = "_a_a_";
		//参数与值的分割符
		String code2 = "_b_b_";
		//含中文的参数
		HashMap<String,String>  dataMaps = new HashMap<String,String>();
		dataMaps.put("vendor",vendor);
		dataMaps.put("spec_type",spec_type);
		dataMaps.put("workOpts",workOpts);
		dataMaps.put("reception",reception);
		dataMaps.put("connPerson",connPerson);
		String ret = "";
		String[] paramArr = paramList_splitPage.split(code1);
		for(int st = 0; st < paramArr.length ; st++){
			String[] paramNVArr = paramArr[st].split(code2);
			String paramName = paramNVArr[0];
			logger.warn("current cycle {}",paramName);
			if(dataMaps.containsKey(paramName)){
				if(!StringUtil.IsEmpty(dataMaps.get(paramName))){
					String paramVal = "";
					try
					{
						paramVal = paramNVArr[1];
						//ret = ret + paramName + code2 + dataMaps.get(paramName) + code1;
						ret = ret + paramName + code2 + URLEncoder.encode(dataMaps.get(paramName), "GBK") + code1;
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						continue;
					}catch(IOException eo){
						logger.error("parseParamListSplitPage => msga：{}",eo.getMessage());
						eo.printStackTrace();
					}
					logger.warn("paramName : {} , paramVal:{} ",new Object[]{paramName,paramVal});
					logger.warn("paramVal = >ret:{}",ret);
				}else{
					ret = ret + paramArr[st] + code1;
				}
			}else{
				ret = ret + paramArr[st] + code1;
				logger.warn("ret : {}",ret);
			}
		}
		logger.warn("ret:"+ret);
		return ret;
	}
	
	//导出
	public String getExcelOut(){
		setTime();
		setFilterEncode();
		fileName = "工作备忘录";
		title = new String[] { "业务编号", "厂家", "终端类型","终端型号","硬件版本","软件版本","工作事项","工作内容","厂家联系人",
				"厂家联系方式","接待人员","开始时间","结束时间","完成状态","备注","资料文件"};
		column = new String[] { "bus_no", "vendor_name", "spec_type", "spec_model","hardwareversion","softwareversion","work_opts",
				"work_content","conn_person","conn_phone","reception","st","end","work_status","remark","file_name"};
		data = bio.qryWorkExcel(busNo, vendor, spec_type, spec_model,hardware, software, workOpts, connPerson, connPhone, reception, startTime, endTime);
		return "excel";
	}
	
	//新增or编辑or删除
	public String operWorkMem(){
		setFilterEncodeForUpdate(); 
		setTime();
		logger.warn("operWorkMem=>busNo:{},vendor:{},spec_type:{},spec_model:{},hardware:{},software:{},"
				+ "workOpts:{},connPerson:{},connPhone:{},reception:{},startTime:{},endTime:{},remark:{} ,gwShare_fileName:{}",
				new Object[]{busNo,vendor, spec_type,spec_model, hardware, software,
				workOpts, connPerson, connPhone, reception, startTime, endTime,remark,gwShare_fileName});
		logger.warn("operWorkMem => operTtype[{}]busNo:{}",operType,busNo);
		if("3".equals(operType)){
			//删除
			ajax = bio.deletMem(busNo);
		}else{
			if("1".equals(operType)){
				//新增
				//busNo = StringUtil.getStringValue(Math.round(Math.random() * 100000L));
				busNo = StringUtil.getStringValue(System.currentTimeMillis()/1000);
			}
			ajax = bio.operMemorandum(StringUtil.getIntegerValue(operType), busNo, vendor, spec_type, spec_model, hardware, software, workOpts, workContent, 
					connPerson, connPhone, reception, startTime, endTime, status, remark, gwShare_fileName);
		}
		return "ajax";
	}
	
	//下载附件
	public String downLoad(){
		try
		{
			if(!StringUtil.IsEmpty(fileName)){
				fileName = URLDecoder.decode(fileName,"UTF-8");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			logger.error("downLoad => msgs : {}",e.getMessage());
		}	
		bio.downLoad(fileName,response);
		return null;
	}
	
	//通过厂商id获取终端类型
	public String getSpecType(){
		ajax = bio.getSpecType(vendor_id);
		return "ajax";
	}
	
	//导入
	public String impFileParse(){
		try
		{
			if(!StringUtil.IsEmpty(gwShare_fileName)){
				gwShare_fileName = URLDecoder.decode(gwShare_fileName,"UTF-8");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			logger.error("impFileParse => msgs : {}",e.getMessage());
		}
		logger.warn("impFileParse => gwShare_fileName:"+gwShare_fileName);
		ajax = bio.impFileParse(gwShare_fileName);
		return "ajax";
	}
	
	//获取厂商
	public String getVendors(){
		ajax = bio.getParamMap(1);
		return "ajax";
	}
	
	//获取工作事项下拉
	public String getWorkOpt(){
		ajax = bio.getParamMap(3);
		return "ajax";
	}
	
	//获取工作事项下拉
	public String getReceptions(){
		ajax = bio.getParamMap(4);
		return "ajax";
	}
	
	private void setTime(){
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime)){
			startTime = null;
		}else{
			dt = new DateTimeUtil(startTime);
			startTime = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)){
			endTime = null;
		}else{
			dt = new DateTimeUtil(endTime);
			endTime = String.valueOf(dt.getLongTime());
		}
	}
	
	public void setFilterEncode(){
		try
		{
			if(!StringUtil.IsEmpty(vendor)){
				vendor =new String(vendor.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(spec_type)){
				spec_type = new String(spec_type.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(spec_model)){
				spec_model = new String(spec_model.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(software)){
				software = new String(software.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(hardware)){
				hardware = new String(hardware.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(workOpts)){
				workOpts = new String(workOpts.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(workContent)){
				workContent = new String(workContent.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(connPerson)){
				connPerson = new String(connPerson.getBytes("ISO8859-1"), "GBK");
			}
			if(!StringUtil.IsEmpty(reception)){
				reception = new String(reception.getBytes("ISO8859-1"), "GBK");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			 logger.error("setFilterEncode error : {}",e.getMessage());
		}
	}
	
	public void setFilterEncodeForUpdate(){
		try
		{
			if(!StringUtil.IsEmpty(vendor)){
				vendor =URLDecoder.decode(vendor, "UTF-8");
			}
			if(!StringUtil.IsEmpty(spec_type)){
				spec_type = URLDecoder.decode(spec_type, "UTF-8");
			}
			if(!StringUtil.IsEmpty(spec_model)){
				spec_model = URLDecoder.decode(spec_model, "UTF-8");
			}
			if(!StringUtil.IsEmpty(software)){
				software = URLDecoder.decode(software, "UTF-8");
			}
			if(!StringUtil.IsEmpty(hardware)){
				hardware = URLDecoder.decode(hardware, "UTF-8");
			}
			if(!StringUtil.IsEmpty(workOpts)){
				workOpts = URLDecoder.decode(workOpts, "UTF-8");
			}
			if(!StringUtil.IsEmpty(workContent)){
				workContent = URLDecoder.decode(workContent, "UTF-8");
			}
			if(!StringUtil.IsEmpty(connPerson)){
				connPerson = URLDecoder.decode(connPerson, "UTF-8");
			}
			if(!StringUtil.IsEmpty(reception)){
				reception = URLDecoder.decode(reception, "UTF-8");
			}
			if(!StringUtil.IsEmpty(remark)){
				remark = URLDecoder.decode(remark, "UTF-8");
			}
			
			if(!StringUtil.IsEmpty(gwShare_fileName)){
				gwShare_fileName = URLDecoder.decode(gwShare_fileName,"UTF-8");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			 logger.error("setFilterEncodeForUpdate error : {}",e.getMessage());
		}
	}
	public String getBusNo()
	{
		return busNo;
	}

	
	public void setBusNo(String busNo)
	{
		this.busNo = busNo;
	}

	
	public String getVendor()
	{
		return vendor;
	}

	
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}

	
	public String getSpec_type()
	{
		return spec_type;
	}

	
	public void setSpec_type(String spec_type)
	{
		this.spec_type = spec_type;
	}

	
	public String getSpec_model()
	{
		return spec_model;
	}

	
	public void setSpec_model(String spec_model)
	{
		this.spec_model = spec_model;
	}

	
	public String getHardware()
	{
		return hardware;
	}

	
	public void setHardware(String hardware)
	{
		this.hardware = hardware;
	}

	
	public String getSoftware()
	{
		return software;
	}

	
	public void setSoftware(String software)
	{
		this.software = software;
	}

	
	public String getWorkOpts()
	{
		return workOpts;
	}

	
	public void setWorkOpts(String workOpts)
	{
		this.workOpts = workOpts;
	}

	
	public String getConnPerson()
	{
		return connPerson;
	}

	
	public void setConnPerson(String connPerson)
	{
		this.connPerson = connPerson;
	}

	
	public String getConnPhone()
	{
		return connPhone;
	}

	
	public void setConnPhone(String connPhone)
	{
		this.connPhone = connPhone;
	}

	
	public String getReception()
	{
		return reception;
	}

	
	public void setReception(String reception)
	{
		this.reception = reception;
	}

	
	public String getStartTime()
	{
		return startTime;
	}

	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	
	public String getEndTime()
	{
		return endTime;
	}

	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public MemBookBIO getBio()
	{
		return bio;
	}


	public void setBio(MemBookBIO bio)
	{
		this.bio = bio;
	}

	public List<Map> getWorkMemorialList()
	{
		return workMemorialList;
	}

	public void setWorkMemorialList(List<Map> workMemorialList)
	{
		this.workMemorialList = workMemorialList;
	}
	public List<HashMap<String, String>> getData()
	{
		return data;
	}

	
	public void setData(List<HashMap<String, String>> data)
	{
		this.data = data;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getOperType()
	{
		return operType;
	}

	public void setOperType(String operType)
	{
		this.operType = operType;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getWorkContent()
	{
		return workContent;
	}

	
	public void setWorkContent(String workContent)
	{
		this.workContent = workContent;
	}

	
	public int getStatus()
	{
		return status;
	}

	
	public void setStatus(int status)
	{
		this.status = status;
	}

	
	public String getAdd_file()
	{
		return add_file;
	}

	
	public void setAdd_file(String add_file)
	{
		this.add_file = add_file;
	}
	

	public String getRemark()
	{
		return remark;
	}

	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getVendor_id()
	{
		return vendor_id;
	}

	public void setVendor_id(String vendor_id)
	{
		this.vendor_id = vendor_id;
	}
	

	public List<HashMap<String, String>> getVendorList()
	{
		return vendorList;
	}

	
	public void setVendorList(List<HashMap<String, String>> vendorList)
	{
		this.vendorList = vendorList;
	}

	
	public List<HashMap<String, String>> getSpecList()
	{
		return specList;
	}

	
	public void setSpecList(List<HashMap<String, String>> specList)
	{
		this.specList = specList;
	}

	
	public List<HashMap<String, String>> getWorkOptsList()
	{
		return workOptsList;
	}

	
	public void setWorkOptsList(List<HashMap<String, String>> workOptsList)
	{
		this.workOptsList = workOptsList;
	}

	
	public List<HashMap<String, String>> getReceptionsList()
	{
		return receptionsList;
	}

	
	public void setReceptionsList(List<HashMap<String, String>> receptionsList)
	{
		this.receptionsList = receptionsList;
	}


	@Override
	public void setServletResponse(HttpServletResponse arg0)
	{
		this.response = arg0;
		
	}


	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
	   this.request = arg0;
		
	}


}
