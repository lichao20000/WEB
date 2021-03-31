
package com.linkage.module.gwms.blocTest.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.share.action.FileUploadAction;
import com.linkage.module.gwms.blocTest.dao.BatchAdditionPhoneDAO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-21
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchAdditionPhoneBIO
{
	private static Logger logger = LoggerFactory.getLogger(BatchAdditionPhoneBIO.class);
	private BatchAdditionPhoneDAO dao;
	private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
	
	public static ArrayList<Map> importToExcel_col(String fileName)
	{
		// 初始化返回值和字段名数组
		ArrayList<Map> arr = new ArrayList<Map>();
		String[] title;
		String[] tmp;
		Workbook wwb = null;
		try
		{
			// 读取excel文件
			File f = new File(getFilePath() + fileName);
			wwb = Workbook.getWorkbook(f);
			// 总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			Map<String,String> mapshow=mapshow();
			for (int m = 0; m < sheetNumber; m++)
			{
				Sheet ws = wwb.getSheet(m);
				
				// 当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				
				// 第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0)
				{
					// 取第一列为字段名，若有重复则增加数字做标记
					title = new String[columeCount];
					tmp = new String[columeCount];
					int count = 0;
					for (int k = 0; k < columeCount; k++)
					{
						tmp[k] = ws.getCell(k, 0).getContents();
						count = checkExit(tmp, tmp[k]);
						if (count <= 1)
						{
							title[k] = mapshow.get(ws.getCell(k, 0).getContents());
						}
						else
						{
							title[k] = mapshow.get(ws.getCell(k, 0).getContents()) + "_" + count;
						}
					}
					
					// 取当前页所有值放入list中
					for (int i = 1; i < rowCount; i++)
					{
						
						Map dataMap = new HashMap();
						for (int j = 0; j < columeCount; j++)
						{
							if (ws.getCell(j, i).getType() == CellType.DATE)
							{
								
					            format.setTimeZone(gmtZone);   
					            dataMap.put(title[j], format.format(((DateCell) ws.getCell(j, i))
										.getDate()));
							}
							else
							{
								dataMap.put(title[j], ws.getCell(j, i).getContents());
							}
						}
						
						arr.add(dataMap);
					}
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (wwb != null)
			{
				wwb.close();
				wwb = null;
			}
		}
		return arr;
	}
	
	private static Map<String, String> mapshow()
	{
		Map<String, String> map=new HashMap<String, String>();
		map.put("宽带账号", "net_account");
		map.put("手机号码", "phone_number");
		map.put("终端型号", "device_model");
		map.put("号码运营商", "phone_operator");
		map.put("号码省份", "province");
		map.put("号码地市", "city_name");
		map.put("手机卡类型", "card_type");
		map.put("终端品牌", "brand");
		map.put("制式", "zhishi");
		map.put("运营商类型", "operator_type");
		map.put("号码第一次出现时间", "phone_firsttime");
		map.put("号码最后一次出现时间", "phone_lasttime");
		map.put("号码总活跃天数", "active_days");
		map.put("号码近30天活跃天数", "active_day2");
		map.put("终端第一次出现时间", "dev_firsttime");
		map.put("终端最后一次出现时间", "dev_lasttime");
		map.put("终端总活跃天数", "dev_active_days");
		map.put("终端近30天活跃天数", "dev_active_day2");
		return map;
	}
	
	public int gettabphoneinfo(ArrayList<Map> list) throws ParseException
	{
		return dao.gettabphoneinfo(list);
	}
	
	public int gettabmaccompany(List<Map<String,String>> list) throws ParseException
	{
		return dao.gettabmaccompany(list);
	}
	
	public int insertTabmaccompany(List<Map<String, String>> list)
	{
		return dao.insertTabmaccompany(list);
	}
	
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public static ArrayList<Map> getImportDataByTXT1(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		ArrayList<Map> list = new ArrayList<Map>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				boolean aa=line.contains("(base 16)");
				if(aa)
				{
					String[] test=line.split("\\(base 16\\)");
					Map map=new HashMap();
					map.put("mac", test[0].trim());
					map.put("company_name", test[1].trim());
					list.add(map);
				}
			}
		}
		in.close();
		in = null;
		//处理完毕时，则删掉文件
		File f = new File(getFilePath()+fileName);
		f.delete();
		f = null;
		return list;
	}
	
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<Map<String,String>> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.warn("getImportDataByTXT{}",fileName);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		logger.warn("getFilePath()===="+getFilePath());
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line=in.readLine();
		
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				boolean aa=line.contains("(base 16)");
				if(aa)
				{
					String[] test=line.split("\\(base 16\\)");
					Map<String,String> map=new HashMap<String, String>();
					map.put("mac", test[0].trim());
					map.put("company_name", test[1].trim().replace("'", "'||'''"));
					list.add(map);
				}
			}

		}
		in.close();
		in = null;
		//处理完毕时，则删掉文件
		File f = new File(getFilePath()+fileName);
		f.delete();
		f = null;
		return list;
	}
	
	
	
	public List<Map<String,String>> getImportDataByTXT_jxdx(String fileName) throws FileNotFoundException,IOException
	{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<String> mac_list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line=in.readLine();
		
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) 
		{
			if(!StringUtil.IsEmpty(line) && line.contains("(base 16)"))
			{
				String[] test=line.split("\\(base 16\\)");
				Map<String,String> map=new HashMap<String, String>();
				if(!StringUtil.IsEmpty(test[0]) 
						&& !StringUtil.IsEmpty(test[1]))
				{
					if(mac_list.contains(test[0].trim())){
						for(Map<String,String> m:list){
							if(test[0].trim().equals(m.get("mac"))){
								list.remove(m);
								break;
							}
						}
					}else{
						mac_list.add(test[0].trim());
					}
					
					map.put("mac", test[0].trim());
					map.put("company_name", test[1].trim().replace("'", "''"));
					list.add(map);
				}
			}
		}
		mac_list=null;
		
		in.close();
		in = null;
		//处理完毕时，则删掉文件
		File f = new File(getFilePath()+fileName);
		f.delete();
		f = null;
		
		logger.warn("导入数据量："+list.size());
		return list;
	}
	
	
	
	
	/**
	 * 返回数组中有几个重复纪录
	 * 
	 * @param arr
	 * @param value
	 * @return
	 */
	private static int checkExit(String[] arr, String value)
	{
		int j = 0;
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] != null && value.equals(arr[i]))
			{
				j++;
			}
		}
		return j;
	}
	
	public List<String> getImportDataByXLS(String fileName) throws BiffException,
			IOException
	{
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++)
		{
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++)
			{
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp))
				{
					if (!"".equals(ws.getCell(0, i).getContents().trim()))
					{
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}
	
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public static String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	public BatchAdditionPhoneDAO getDao()
	{
		return dao;
	}

	public void setDao(BatchAdditionPhoneDAO dao)
	{
		this.dao = dao;
	}
	
	public static TimeZone getGmtzone()
	{
		return gmtZone;
	}
	
	public static SimpleDateFormat getFormat()
	{
		return format;
	}
	
}
