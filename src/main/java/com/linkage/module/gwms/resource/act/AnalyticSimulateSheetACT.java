
package com.linkage.module.gwms.resource.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.resource.bio.AnalyticSimulateSheetBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-21 下午02:21:47
 * @category com.linkage.module.gwms.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class AnalyticSimulateSheetACT implements ServletRequestAware, SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(AnalyticSimulateSheetACT.class);
	// request取登陆帐号使用
	private HttpServletRequest request;
	private AnalyticSimulateSheetBIO bio;
	// 导入文件,根据此文件解析用户账号
	private File file;
	private Map session;
	private String ajax;
	// private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName;
	private String[] column; // 需要导出的列名，对应data中的键值
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	// 资料失败的
	private List<String> inforSheet = new ArrayList<String>();
	private String inforSheetStr;
	// 上网失败的
	private List<String> netSheet = new ArrayList<String>();
	private String netSheetStr;
	// itv失败的
	private List<String> itvSheet = new ArrayList<String>();
	private String itvSheetStr;
	// voip失败的
	private List<String> voipSheet = new ArrayList<String>();
	private String voipSheetStr;
	// 成功的
	private List<String> successSheet = new ArrayList<String>();
	private int successNum;
	private List<String[]> arr;
	// 是否显示结果
	private String isResult;
	private String str;
	private InputStream exportExcelStream;

	public String execute()
	{
		logger.debug("excute");
		return "success";
	}

	public String analyticSimulateSheet()
	{
		arr = bio.analyticFile(this.file);
		bio.simulateSheet(arr);
		inforSheet = bio.getInforSheet();
		inforSheetStr = getStrFromList(inforSheet);
		netSheet = bio.getNetSheet();
		netSheetStr = getStrFromList(netSheet);
		itvSheet = bio.getItvSheet();
		itvSheetStr = getStrFromList(itvSheet);
		voipSheet = bio.getVoipSheet();
		voipSheetStr = getStrFromList(voipSheet);
		successSheet = bio.getSuccessSheet();
		successNum = successSheet.size();
		isResult = "1";
		return "sheetResult";
	}

	public String getStrFromList(List list)
	{
		String str = "";
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				str = str + StringUtil.getStringValue(list.get(i)) + ",";
			}
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public String getExcelTemplate()
	{
		logger.debug("getExcelTemplate()");
		data = new ArrayList<Map>();
		String excelCol = "dealtime#username#accesstype#name#address#place#account#password#vlanID#MGC#MGCport#MGCaddress#MGCport1#yuming#biaoshi#tel#iptvusername#IPTVnum#userType#customerId#customerAccount#customerPwd";
		column = excelCol.split("#");
		String excelTitle = "工单受理时间#逻辑ID#接入方式(必须填光纤)#用户姓名#家庭住址#属地#宽带账号#宽带密码#宽带vlanID#主MGC地址（必须填172.16.0.12）#主MGC端口（必须填2944）#备MGC地址（必须填172.16.0.12）#备MGC端口（必须填2944）#语音注册域名（软交换）#终端物理标识#电话号码#iptv账号#IPTV个数#用户类型（家庭，企业）#客户Id#客户帐号#客户密码";
		title = excelTitle.split("#");
		fileName = "example";
		return "excel";
	}

	/**
	 * 批量导入升级任务定制初始化页面
	 * 
	 * @author wufei
	 * @date Nov 12, 2010
	 * @param
	 * @return String
	 */
	public String downloadTemplate()
	{
         return "toExport";
	}

	public InputStream getExportExcelStream()
	{
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "gwms" + separa + "resource" + separa
				+ "batchImporttemplate.xls";
		try
		{
			fio = new FileInputStream(filePath);
		}
		catch (FileNotFoundException e)
		{
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	public void setExportExcelStream(InputStream exportExcelStream)
	{
		this.exportExcelStream = exportExcelStream;
	}

	public AnalyticSimulateSheetBIO getBio()
	{
		return bio;
	}

	public void setBio(AnalyticSimulateSheetBIO bio)
	{
		this.bio = bio;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getSession()
	{
		return session;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<String[]> getArr()
	{
		return arr;
	}

	public void setArr(List<String[]> arr)
	{
		this.arr = arr;
	}

	public List<String> getInforSheet()
	{
		return inforSheet;
	}

	public void setInforSheet(List<String> inforSheet)
	{
		this.inforSheet = inforSheet;
	}

	public List<String> getNetSheet()
	{
		return netSheet;
	}

	public void setNetSheet(List<String> netSheet)
	{
		this.netSheet = netSheet;
	}

	public List<String> getItvSheet()
	{
		return itvSheet;
	}

	public void setItvSheet(List<String> itvSheet)
	{
		this.itvSheet = itvSheet;
	}

	public List<String> getVoipSheet()
	{
		return voipSheet;
	}

	public void setVoipSheet(List<String> voipSheet)
	{
		this.voipSheet = voipSheet;
	}

	public String getInforSheetStr()
	{
		return inforSheetStr;
	}

	public void setInforSheetStr(String inforSheetStr)
	{
		this.inforSheetStr = inforSheetStr;
	}

	public String getNetSheetStr()
	{
		return netSheetStr;
	}

	public void setNetSheetStr(String netSheetStr)
	{
		this.netSheetStr = netSheetStr;
	}

	public String getItvSheetStr()
	{
		return itvSheetStr;
	}

	public void setItvSheetStr(String itvSheetStr)
	{
		this.itvSheetStr = itvSheetStr;
	}

	public String getVoipSheetStr()
	{
		return voipSheetStr;
	}

	public void setVoipSheetStr(String voipSheetStr)
	{
		this.voipSheetStr = voipSheetStr;
	}

	public int getSuccessNum()
	{
		return successNum;
	}

	public void setSuccessNum(int successNum)
	{
		this.successNum = successNum;
	}

	public String getIsResult()
	{
		return isResult;
	}

	public void setIsResult(String isResult)
	{
		this.isResult = isResult;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getStr()
	{
		return str;
	}

	public void setStr(String str)
	{
		this.str = str;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public List<String> getSuccessSheet()
	{
		return successSheet;
	}

	public void setSuccessSheet(List<String> successSheet)
	{
		this.successSheet = successSheet;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}
}
