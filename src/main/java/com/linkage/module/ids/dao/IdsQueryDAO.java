
package com.linkage.module.ids.dao;

import java.awt.event.FocusAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.elasticsearch.bio.ElasticsearchInitBIO;
import com.linkage.commons.elasticsearch.util.ElasticDataUtil;
import com.linkage.commons.elasticsearch.util.MySearchOption;
import com.linkage.commons.elasticsearch.util.MySearchOption.OperType;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchLogic;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchType;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IdsQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(IdsQueryDAO.class);
	private ElasticDataUtil edu = null;
	private Map vendorMap = null;
	private Map modelMap = null;
	private Cursor cursor = null;
	private Map fields = null;
	private static String sql = null;
	static{
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql="select b.device_serialnumber,a.devicetype_id from tab_gw_device a,tab_hgwcustomer b where a.device_id=b.device_id and a.customer_id is not null  ";
		}else{
			sql="select b.device_serialnumber,t.vendor_id,t.device_model_id,t.hardwareversion,t.softwareversion from tab_gw_device a,tab_devicetype_info t,tab_hgwcustomer b where a.devicetype_id=t.devicetype_id and a.device_id = b.device_id and a.customer_id is not null  ";
		}
	}
			
	// 回传消息
	private String msg = null;

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public List getQueryStatusListByLoid(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String starttime1, String endtime1,
			String quertTimeType, String loid)
	{
		
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		// 获取厂商 和型号map
		getVendorMap(1);
		getModelMap();
		//传入loid 显示一条。
		if (!StringUtil.IsEmpty(loid))
		{
			num_splitPage = 1;
		}
	
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
				curPage_splitPage, num_splitPage, starttime1, endtime1, quertTimeType,
				loid);
		int from = (curPage_splitPage - 1) * num_splitPage;
		String time = "upload_time";
		// 从数据库捞取厂商，型号，软硬件版本，设备序列号信息 保存在listResult
		List<Map> listResult = new ArrayList<Map>();
		List<Map<String, Object>> list = edu.simpleSearch(indexType, searchOptionList,
				from, num_splitPage, time, "desc");
		if (null != list)
		{
			Map<String, Object> map = null;
			for (Map<String, Object> remap : list)
			{
				if (null != remap)
				{
					logger.warn("ElasticDeviceMonitorDAO:remap", remap);
					map = new HashMap<String, Object>();
					// loid 和 上报时间取自es
					map.put("loid", remap.get("loid"));
					String date = timeStamp2Date(String.valueOf(remap.get("upload_time")), "yyyy-MM-dd HH:mm:ss"); 
					map.put("upload_time", date);
					map.put("state","已上报");
					// 其他信息取自之前从数据库中捞取的
					listResult = getDeviceInfo(sql, (String) remap.get("loid"));
					if (!listResult.isEmpty())
					{
						map.put("vendor_name", listResult.get(0).get("vendor_name"));
						map.put("device_model", listResult.get(0).get("device_model"));
						map.put("softwareversion",
								listResult.get(0).get("softwareversion"));
						map.put("hardwareversion",
								listResult.get(0).get("hardwareversion"));
						map.put("device_serialnumber",
								listResult.get(0).get("device_serialnumber"));
						
					}
				}
				returnList.add(map);
			}
		}
		return returnList;
	}

	public List getQueryStatusListByFile(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String starttime1, String endtime1,
			String quertTimeType, String filepath)
	{
		
		if(StringUtil.IsEmpty(indexName))
		{
			indexName = "ids";
		}
		if(StringUtil.IsEmpty(indexType))
		{
			indexType = "ponstatus";
		}
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
		// 获取厂商 和型号map
		getVendorMap(1);
		getModelMap();
		String fileName_ = filepath.substring(filepath.length() - 3, filepath.length());
		List<String> dataList = null;
		try
		{
			if ("txt".equals(fileName_) || "csv".equals(fileName_))
			{
				dataList = getImportDataByTXT(filepath);
			}
			/*else if("csv".equals(fileName_))
			{
				dataList = getImportDataByCSV(filepath);
			}*/
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", filepath);
			this.msg = "文件没找到！";
			return null;
		}
		catch (IOException e)
		{
			logger.warn("{}文件解析出错！", filepath);
			this.msg = "文件解析出错！";
			return null;
		}
		catch (Exception e)
		{
			logger.warn("{}文件解析出错！", filepath);
			this.msg = "文件解析出错！";
			return null;
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (null != dataList && dataList.size() > 1000)
		{
			
			logger.warn("文件行数超过1000");
			return returnList;
		}
		
		for (int i = 0 ; i < 10 ; i++)
		{
			
			int valuePage = (curPage_splitPage - 1) * 10;
			valuePage = valuePage + i;
			if(valuePage == dataList.size())
			{
				return returnList;
			}
			String loid = dataList.get(valuePage);
			//num_splitPage = dataList.size();
			// 从数据库捞取厂商，型号，软硬件版本，设备序列号信息 保存在listResult
			List<Map> listResult = new ArrayList<Map>();
			ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
					curPage_splitPage, num_splitPage, starttime1, endtime1,
					quertTimeType, loid);
			
			int from = 0;
			// int timeType = StringUtil.getIntegerValue(quertTimeType);
			String time = "upload_time";
			List<Map<String, Object>> list = edu.simpleSearch(indexType, 
					searchOptionList, from, num_splitPage, time, "desc");
			Map<String, Object> map = null;
			if (null != list && !list.isEmpty() )
			{
				for (Map<String, Object> remap : list)
				{
					map = new HashMap<String, Object>();
					if (null != remap)
					{
						logger.warn("ElasticDeviceMonitorDAO:remap", remap);
						// loid 和 上报时间取自es
						map.put("loid", remap.get("loid"));
						String date = timeStamp2Date(String.valueOf(remap.get("upload_time")), "yyyy-MM-dd HH:mm:ss"); 
						map.put("upload_time", date);
						map.put("state","已上报");
						// 其他信息取自之前从数据库中捞取的
						listResult = getDeviceInfo(sql,(String)remap.get("loid"));
						if (!listResult.isEmpty())
						{
							map.put("vendor_name", listResult.get(0).get("vendor_name"));
							map.put("device_model", listResult.get(0).get("device_model"));
							map.put("softwareversion",
									listResult.get(0).get("softwareversion"));
							map.put("hardwareversion",
									listResult.get(0).get("hardwareversion"));
							map.put("device_serialnumber",
									listResult.get(0).get("device_serialnumber"));
							
						}
					
					}
					
					returnList.add(map);
					break;
				}
			}
			else 
			{
				//未上报信息统计
				map = new HashMap<String, Object>();
				map.put("loid", loid);
				map.put("upload_time", "");
				listResult = getDeviceInfo(sql,loid);
				
				if (!listResult.isEmpty())
				{	
					//设备信息 不为空 表示未上报
					
					map.put("state", "未上报");
					map.put("vendor_name", listResult.get(0).get("vendor_name"));
					map.put("device_model", listResult.get(0).get("device_model"));
					map.put("softwareversion",
							listResult.get(0).get("softwareversion"));
					map.put("hardwareversion",
							listResult.get(0).get("hardwareversion"));
					map.put("device_serialnumber",
							listResult.get(0).get("device_serialnumber"));
				}
				else 
				{
					//设备信息 为空 表示未上报中的未注册，state为未做
					map.put("state", "未做");
				}
				returnList.add(map);
			}
			if(i == 9)
			{
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * 设置查询条件
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public ArrayList<MySearchOption> getQueryParam(int curPage_splitPage,
			int num_splitPage, String startTime, String endTime, String quertTimeType,
			String loid)
	{
		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
		/*
		 * MySearchOption mySearchOption = new MySearchOption( "device_serialnumber",
		 * SearchType.term, SearchLogic.must, deviceSerialnumber);
		 */
		MySearchOption mySearchOption1 = null;
		if (!StringUtil.IsEmpty(loid))
		{
			mySearchOption1 = new MySearchOption("loid", SearchType.term,
					SearchLogic.must, loid);
		}
		else
		{
			mySearchOption1 = new MySearchOption("loid", SearchType.term,
					SearchLogic.must_not, loid);
		}
		String time = "upload_time";
		// searchOptionList.add(mySearchOption);
		searchOptionList.add(mySearchOption1);
		MySearchOption mySearchOption2 = null;
		MySearchOption mySearchOption3 = null;
		mySearchOption2 = new MySearchOption(time, SearchType.range, SearchLogic.must,
				OperType.gt, String.valueOf(new DateTimeUtil(startTime).getLongTime()));
		mySearchOption3 = new MySearchOption(time, SearchType.range, SearchLogic.must,
				OperType.lt, String.valueOf(new DateTimeUtil(endTime).getLongTime()));
		searchOptionList.add(mySearchOption2);
		searchOptionList.add(mySearchOption3);
		return searchOptionList;
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,
			IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 解析文件（csv格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 */
	/*public List<String> getImportDataByCSV(String fileName) throws BiffException,
			IOException
	{
		logger.debug("getImportDataByCSV{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格后，去掉最后一位','
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				line = line.trim().substring(0, line.length()-1);
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		return list;
	}*/

	/**
	 * 查询总记录数
	 * 
	 * @param indexName
	 * @param indexType
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param startTime
	 * @param endTime
	 * @param quertTimeType
	 * @param deviceSerialnumber
	 * @param loid
	 * @return
	 */
	public int queryPonstatusCount(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime, String endTime,
			String quertTimeType, String loid)
	{
		logger.debug("queryPonstatusCount1-->");
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
		// edu = new ElasticDataUtil(indexName);
		ArrayList<MySearchOption> searchOptionList = this
				.getQueryParam(curPage_splitPage, num_splitPage, startTime, endTime,
						quertTimeType, loid);
		int total = StringUtil.getIntegerValue(edu.getCount(indexType, searchOptionList));
		logger.warn(String.valueOf(total));
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = (int) (total / num_splitPage);
		}
		else
		{
			maxPage = (int) (total / num_splitPage + 1);
		}
		return maxPage;
	}

	public int queryPonstatusCount2(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String startTime, String endTime,
			String quertTimeType, String filepath)
	{
		logger.debug("queryPonstatusCount1-->");
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
		String fileName_ = filepath.substring(filepath.length() - 3, filepath.length());
		List<String> dataList = null;
		try
		{
			if ("txt".equals(fileName_) || "csv".equals(fileName_) )
			{
				dataList = getImportDataByTXT(filepath);
			}
			/*else
			{
				dataList = getImportDataByCSV(filepath);
			}*/
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", filepath);
			this.msg = "文件没找到！";
			return 0;
		}
		catch (IOException e)
		{
			logger.warn("{}文件解析出错！", filepath);
			this.msg = "文件解析出错！";
			return 0;
		}
		catch (Exception e)
		{
			logger.warn("{}文件解析出错！", filepath);
			this.msg = "文件解析出错！";
			return 0;
		}
		if (null != dataList && dataList.size() > 1000)
		{
			
			logger.warn("文件行数超过1000");
			return 0;
		}
		int maxPage = 1;
		int toatlAll = dataList.size();
		/*for (String loid : dataList)
		{
			ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
					curPage_splitPage, num_splitPage, startTime, endTime, quertTimeType,
					loid);
			int total = StringUtil.getIntegerValue(edu.getCount(indexType,
					searchOptionList));
		
			toatlAll = toatlAll + total;
			
		}*/
		if (toatlAll % num_splitPage == 0)
		{
			maxPage = (int) (toatlAll / num_splitPage);
		}
		else
		{
			maxPage = (int) (toatlAll / num_splitPage + 1);
		}
		return maxPage;
	}

	/**
	 * 将厂商信息放入MAP中
	 */
	private void getVendorMap(int gw_type)
	{
		String sql;
		if (4 == gw_type)
		{
			sql = "select vendor_id,vendor_add,vendor_name from stb_tab_vendor order by vendor_add";
		}
		else
		{
			sql = "select vendor_id,vendor_add,vendor_name from tab_vendor order by vendor_add";
		}
		
		if (vendorMap == null)
		{
			vendorMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null)
			{
				String vendor_add = (String) fields.get("vendor_add");
				if (vendor_add != null && !"".equals(vendor_add))
				{
					vendorMap.put(fields.get("vendor_id"),
							vendor_add + "(" + fields.get("vendor_id") + ")");
				}
				else
				{
					vendorMap.put(fields.get("vendor_id"), fields.get("vendor_name")
							+ "(" + fields.get("vendor_id") + ")");
				}
				fields = cursor.getNext();
			}
		}
	}

	/**
	 * 将型号信息放入MAP中
	 */
	private void getModelMap()
	{
		String sql = "select device_model_id,device_model from gw_device_model order by device_model_id";
		if (modelMap == null)
		{
			modelMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			fields = cursor.getNext();
			while (fields != null)
			{
				modelMap.put(fields.get("device_model_id"), fields.get("device_model"));
				fields = cursor.getNext();
			}
		}
	}

	/*
	 * 根据loid 获取设备信息
	 */
	private List getDeviceInfo(String sql, String loid)
	{
		List<Map> listResult = new ArrayList<Map>();
		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append(sql);
		if (!StringUtil.IsEmpty(loid))
		{
			sqlCount.append(" and b.username = " + "'" + loid + "'");
		}
		PrepareSQL sql1 = new PrepareSQL("");
		sql1.append(sqlCount.toString());
		listResult = jt.queryForList(sql1.getSQL());
		if (listResult != null && listResult.size() > 0)
		{
			Map<String,String> versionMap=null;
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				versionMap=getDeviceTypeInfo();
			}
			
			for (int i = 0; i < listResult.size(); i++)
			{
				Map map=(Map)listResult.get(i);
				map.put("device_serialnumber",
						StringUtil.getStringValue(map.get("device_serialnumber")));
				
				String vendor_id=null;
				String device_model_id=null;
				String hardwareversion=null;
				String softwareversion=null;
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					String ver=versionMap.get(StringUtil.getStringValue(map,"devicetype_id"));
					if(StringUtil.IsEmpty(ver)){
						continue;
					}
					
					String[] versions=ver.split(",");
					vendor_id=versions[0];
					device_model_id=versions[1];
					hardwareversion="none".equals(versions[2])?"":versions[2];
					softwareversion=versions[3];
					
					ver=null;
					versions=null;
				}else{
					vendor_id=StringUtil.getStringValue(map,"vendor_id");
					device_model_id=StringUtil.getStringValue(map,"device_model_id");
					hardwareversion=StringUtil.getStringValue(map,"hardwareversion");
					softwareversion=StringUtil.getStringValue(map,"softwareversion");
				}
				map.put("vendor_name",(String) vendorMap.get(vendor_id));
				map.put("device_model",(String) modelMap.get(device_model_id));
				map.put("hardwareversion",hardwareversion);
				map.put("softwareversion",softwareversion);
				
				vendor_id=null;
				device_model_id=null;
				hardwareversion=null;
				softwareversion=null;
			}
		}
		return listResult;
	}
	
	/**
	 * 获取所有版本
	 */
	private Map<String,String> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,vendor_id,device_model_id,hardwareversion,softwareversion ");
		psql.append("from tab_devicetype_info order by devicetype_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				String str=StringUtil.getStringValue(m,"vendor_id")
							+","+StringUtil.getStringValue(m,"device_model_id");
				String hardwareversion=StringUtil.getStringValue(m,"hardwareversion");
				if(StringUtil.IsEmpty(hardwareversion)){
					hardwareversion="none";
				}
				str+=","+hardwareversion+StringUtil.getStringValue(m,"softwareversion");
				map.put(StringUtil.getStringValue(m,"devicetype_id"),str);
			}
		}
		
		return map;
	}

	public static String timeStamp2Date(String seconds, String format)
	{
		if (seconds == null || seconds.isEmpty() || seconds.equals("null"))
		{
			return "";
		}
		if (format == null || format.isEmpty())
		{
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}
	
	
	
	
	
	
	public List getQueryStatusListByFileToExcel(String indexName, String indexType,
			int curPage_splitPage, int num_splitPage, String starttime1, String endtime1,
			String quertTimeType, String filepath)
	{
		
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
		// 获取厂商 和型号map
		getVendorMap(1);
		getModelMap();
		String fileName_ = filepath.substring(filepath.length() - 3, filepath.length());
		List<String> dataList = null;
		try
		{
			if ("txt".equals(fileName_) || "csv".equals(fileName_))
			{
				dataList = getImportDataByTXT(filepath);
			}
			/*else if("csv".equals(fileName_))
			{
				dataList = getImportDataByCSV(filepath);
			}*/
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", filepath);
			this.msg = "文件没找到！";
			return null;
		}
		catch (IOException e)
		{
			logger.warn("{}文件解析出错！", filepath);
			this.msg = "文件解析出错！";
			return null;
		}
		catch (Exception e)
		{
			logger.warn("{}文件解析出错！", filepath);
			this.msg = "文件解析出错！";
			return null;
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (null != dataList && dataList.size() > 1000)
		{
			
			logger.warn("文件行数超过1000");
			return returnList;
		}
		
		for (int i = 0 ; i < dataList.size() ; i++)
		{
			logger.warn("curPage_splitPage :"+String.valueOf(curPage_splitPage));
			String loid = dataList.get(i);
			//num_splitPage = dataList.size();
			// 从数据库捞取厂商，型号，软硬件版本，设备序列号信息 保存在listResult
			List<Map> listResult = new ArrayList<Map>();
			ArrayList<MySearchOption> searchOptionList = this.getQueryParam(
					curPage_splitPage, num_splitPage, starttime1, endtime1,
					quertTimeType, loid);
			
			int from = (curPage_splitPage - 1) * 10;
			// int timeType = StringUtil.getIntegerValue(quertTimeType);
			String time = "upload_time";
			List<Map<String, Object>> list = edu.simpleSearch(indexType, 
					searchOptionList, from, num_splitPage, time, "desc");
			Map<String, Object> map = null;
			logger.warn("loid :"+loid);
			if (null != list && !list.isEmpty() )
			{
				for (Map<String, Object> remap : list)
				{
					map = new HashMap<String, Object>();
					if (null != remap)
					{
						logger.warn("ElasticDeviceMonitorDAO:remap", remap);
						// loid 和 上报时间取自es
						map.put("loid", remap.get("loid"));
						String date = timeStamp2Date(String.valueOf(remap.get("upload_time")), "yyyy-MM-dd HH:mm:ss"); 
						map.put("upload_time", date);
						map.put("state","已上报");
						// 其他信息取自之前从数据库中捞取的
						listResult = getDeviceInfo(sql,(String)remap.get("loid"));
						if (!listResult.isEmpty())
						{
							map.put("vendor_name", listResult.get(0).get("vendor_name"));
							map.put("device_model", listResult.get(0).get("device_model"));
							map.put("softwareversion",
									listResult.get(0).get("softwareversion"));
							map.put("hardwareversion",
									listResult.get(0).get("hardwareversion"));
							map.put("device_serialnumber",
									listResult.get(0).get("device_serialnumber"));
							
						}
					
					}
					
					returnList.add(map);
					break;
				}
			}
			else 
			{
				//未上报信息统计
				map = new HashMap<String, Object>();
				map.put("loid", loid);
				map.put("upload_time", "");
				listResult = getDeviceInfo(sql,loid);
				
				if (!listResult.isEmpty())
				{	
					//设备信息 不为空 表示未上报
					
					map.put("state", "未上报");
					map.put("vendor_name", listResult.get(0).get("vendor_name"));
					map.put("device_model", listResult.get(0).get("device_model"));
					map.put("softwareversion",
							listResult.get(0).get("softwareversion"));
					map.put("hardwareversion",
							listResult.get(0).get("hardwareversion"));
					map.put("device_serialnumber",
							listResult.get(0).get("device_serialnumber"));
				}
				else 
				{
					//设备信息 为空 表示未上报中的未注册，state为未做
					map.put("state", "未做");
				}
				returnList.add(map);
			}
			
		}
		return returnList;
	}
}