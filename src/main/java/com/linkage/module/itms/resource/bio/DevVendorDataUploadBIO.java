
package com.linkage.module.itms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.resource.dao.DevVendorDataUploadDAO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年10月15日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevVendorDataUploadBIO
{

	private static Logger logger = LoggerFactory.getLogger(DevVendorDataUploadBIO.class);
	/** 解析入库用dao */
	private DevVendorDataUploadDAO dao;
	/** 单次读取行数 */
	private static final int count = 200;

	/**
	 * 保存文件
	 * 
	 * @param source
	 * @param newFileDir
	 * @param newFileName
	 * @return
	 */
	public String saveFile(File source, String newFileDir, String newFileName)
	{
		logger.debug("saveFile({})", new Object[] { source.getAbsolutePath(), newFileDir,
				newFileName });
		String result = "1";
		File target = new File(newFileDir, newFileName);
		try
		{
			// 文件复制
			FileUtils.copyFile(source, target);
		}
		catch (Exception e)
		{
			logger.error("copy file error:", e);
			result = "0";
		}
		// 1成功,0失败
		return result;
	}

	/**
	 * 解析txt文件
	 * 
	 * @param downLoadTargetFile
	 *            目标文件
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String parseTxt(String downLoadTargetFile) throws FileNotFoundException,
			IOException
	{
		logger.warn("开始解析:" + downLoadTargetFile);
		// 获取每一行的记录
		List<String> repairDevInfoList = getImportDataByTXT(downLoadTargetFile);
		Map<String, String> tempMap = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int successCount = 0;
		long importTime = new DateTimeUtil().getLongTime();
		for (int i = 1; i <= repairDevInfoList.size(); i++)
		{
			tempMap = new HashMap<String, String>();
			logger.debug("第" + i + "行数据-------" + repairDevInfoList.get(i - 1));
			String repairDevInfo[] = repairDevInfoList.get(i - 1).split(",");
			if (repairDevInfo.length != 15)
			{
				logger.warn(i + "行数据格式不对,跳过");
				continue;
			}
			// 维修厂家
			if (StringUtil.IsEmpty(repairDevInfo[0].trim()))
			{
				logger.warn("维修厂家为空,跳过");
				continue;
			}
			tempMap.put("repair_vendor", repairDevInfo[0]);
			// web页面上传 默认是保内
			tempMap.put("insurance_status", "1");
			// 批号
			tempMap.put("batch_number", repairDevInfo[1]);
			// 设备序列号
			if (StringUtil.IsEmpty(repairDevInfo[2].trim()))
			{
				logger.warn("设备序列号为空,跳过");
				continue;
			}
			tempMap.put("device_serialnumber", repairDevInfo[2]);
			// 主厂家
			tempMap.put("device_vendor", repairDevInfo[3]);
			// 设备型号
			tempMap.put("device_model", repairDevInfo[4]);
			// 硬件版本
			tempMap.put("hardwareversion", repairDevInfo[5]);
			// 软件版本
			tempMap.put("softwareversion", repairDevInfo[6]);
			// 版本检测
			if ("合格".equals(repairDevInfo[7]))
			{
				tempMap.put("version_check", "1");
			}
			else
			{
				tempMap.put("version_check", "0");
			}
			// 出厂配置检测
			if ("合格".equals(repairDevInfo[8]))
			{
				tempMap.put("config_check", "1");
			}
			else
			{
				tempMap.put("config_check", "0");
			}
			// 业务下发检测
			if ("合格".equals(repairDevInfo[9]))
			{
				tempMap.put("serv_issue_check", "1");
			}
			else
			{
				tempMap.put("serv_issue_check", "0");
			}
			// 语音注册检测
			if ("合格".equals(repairDevInfo[10]))
			{
				tempMap.put("voice_regist_check", "1");
			}
			else
			{
				tempMap.put("voice_regist_check", "0");
			}
			// 检测结果
			if ("合格".equals(repairDevInfo[11]))
			{
				tempMap.put("check_result", "1");
			}
			else
			{
				tempMap.put("check_result", "0");
			}
			// 发往城市
			tempMap.put("send_city", repairDevInfo[12]);
			// 归属城市
			tempMap.put("attribution_city", repairDevInfo[13]);
			// 返修后的出厂时间
			if (StringUtil.IsEmpty(repairDevInfo[14].trim()))
			{
				logger.warn("出厂时间为空,跳过");
				continue;
			}
			tempMap.put("manufacture_date", setTime(repairDevInfo[14]));
			successCount++;
			list.add(tempMap);
			// 每 count 行执行一次
			if (i % count == 0)
			{
				dao.insertRepairDevInfo(list, importTime);
				// 清空list内存
				list.clear();
			}
		}
		// 不足 count 行 或最后不足 count 行 执行入库
		dao.insertRepairDevInfo(list, importTime);
		logger.warn("解析入库完成");
		return "1#解析入库完成,解析成功" + successCount + "条";
	}

	/**
	 * 获取文件每行信息（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	private List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		InputStreamReader txtFile = new InputStreamReader(new FileInputStream(fileName),
				"utf-8");
		BufferedReader in = new BufferedReader(txtFile);
		String line = "";
		// 开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				// 解决txt是utf-8带pom问题
				if (line.startsWith("\uFEFF"))
				{
					line = line.replace("\uFEFF", "");
				}
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		return list;
	}

	public void setDao(DevVendorDataUploadDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 解析excel入库操作
	 * 
	 * @param downLoadTargetFile
	 *            文件名
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String parseExcel(String downLoadTargetFile) throws FileNotFoundException,
			IOException
	{
		logger.warn("开始解析 " + downLoadTargetFile);
		// 维修厂家
		String repair_vendor = "";
		// 批号
		String batch_number = "";
		// 设备序列号
		String device_serialnumber = "";
		// 主厂家
		String device_vendor = "";
		// 设备型号
		String device_model = "";
		// 硬件版本
		String hardwareversion = "";
		// 软件版本
		String softwareversion = "";
		// 版本检测
		String version_check = "";
		// 出厂配置检测
		String config_check = "";
		// 业务下发检测
		String serv_issue_check = "";
		// 语音注册检测
		String voice_regist_check = "";
		// 检测结果
		String check_result = "";
		// 发往城市
		String send_city = "";
		// 归属城市
		String attribution_city = "";
		// 返修后的出厂时间
		String manufacture_date = "";
		File target = new File(downLoadTargetFile);
		// 为了兼容2003与2007
		Workbook workbook = null;
		try
		{
			workbook = new XSSFWorkbook(new FileInputStream(target));
		}
		catch (Exception e)
		{
			workbook = new HSSFWorkbook(new FileInputStream(target));
		}
		if (null != target && target.exists())
		{
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> tempMap = null;
			// 创建工作表
			// 获得sheet
			Sheet sheet = workbook.getSheetAt(0);
			// 获得sheet的总行数
			int rowCount = sheet.getLastRowNum();
			logger.debug("获得sheet的总行数:" + rowCount);
			Cell cellFirst = null;
			Cell cellSecond = null;
			Cell cellThird = null;
			Cell cellFourth = null;
			Cell cellFiveth = null;
			Cell cellSixth = null;
			Cell cellSeventh = null;
			Cell cellEghth = null;
			Cell cellNighth = null;
			Cell cellTen = null;
			Cell cellEleven = null;
			Cell cellTwelve = null;
			Cell cellThirteen = null;
			Cell cellFourteen = null;
			Cell cellFifteen = null;
			int successCount = 0;
			long importTime = new DateTimeUtil().getLongTime();
			// 循环解析每一行
			for (int i = 0; i <= rowCount; i++)
			{
				tempMap = new HashMap<String, String>();
				// 获得行对象
				Row row = sheet.getRow(i);
				cellFirst = row.getCell(0);
				cellSecond = row.getCell(1);
				cellThird = row.getCell(2);
				cellFourth = row.getCell(3);
				cellFiveth = row.getCell(4);
				cellSixth = row.getCell(5);
				cellSeventh = row.getCell(6);
				cellEghth = row.getCell(7);
				cellNighth = row.getCell(8);
				cellTen = row.getCell(9);
				cellEleven = row.getCell(10);
				cellTwelve = row.getCell(11);
				cellThirteen = row.getCell(12);
				cellFourteen = row.getCell(13);
				cellFifteen = row.getCell(14);
				if (i == 0)
				{
					if (cellFirst == null || cellSecond == null || cellThird == null
							|| cellFourth == null || cellFiveth == null
							|| cellSixth == null || cellSeventh == null
							|| cellEghth == null || cellNighth == null || cellTen == null
							|| cellEleven == null || cellTwelve == null
							|| cellThirteen == null || cellFourteen == null
							|| cellFifteen == null)
					{
						logger.warn("Excel格式错误,字段不对", new Object[] { i + 1 });
						return "0#Excel格式错误,字段不对";
					}
					// 判断excel里sheet抬头是否正确
					if (!"维修厂家".equals(StringUtil.getStringValue(cellFirst
							.getRichStringCellValue()))
							|| !"批号".equals(StringUtil.getStringValue(cellSecond
									.getRichStringCellValue()))
							|| !"设备序列号".equals(StringUtil.getStringValue(cellThird
									.getRichStringCellValue()))
							|| !"主厂家".equals(StringUtil.getStringValue(cellFourth
									.getRichStringCellValue()))
							|| !"设备型号".equals(StringUtil.getStringValue(cellFiveth
									.getRichStringCellValue()))
							|| !"硬件版本".equals(StringUtil.getStringValue(cellSixth
									.getRichStringCellValue()))
							|| !"软件版本".equals(StringUtil.getStringValue(cellSeventh
									.getRichStringCellValue()))
							|| !"版本检测".equals(StringUtil.getStringValue(cellEghth
									.getRichStringCellValue()))
							|| !"出厂配置检测".equals(StringUtil.getStringValue(cellNighth
									.getRichStringCellValue()))
							|| !"业务下发检测".equals(StringUtil.getStringValue(cellTen
									.getRichStringCellValue()))
							|| !"语音注册检测".equals(StringUtil.getStringValue(cellEleven
									.getRichStringCellValue()))
							|| !"检测结果".equals(StringUtil.getStringValue(cellTwelve
									.getRichStringCellValue()))
							|| !"发往城市".equals(StringUtil.getStringValue(cellThirteen
									.getRichStringCellValue()))
							|| !"归属城市".equals(StringUtil.getStringValue(cellFourteen
									.getRichStringCellValue()))
							|| !"返修后的出厂时间".equals(StringUtil.getStringValue(cellFifteen
									.getRichStringCellValue())))
					{
						logger.warn("Excel格式错误,字段不对", new Object[] { i + 1 });
						return "0#Excel格式错误,字段不对";
					}
					continue;
				}
				// 重新初始化各字段数据
				// 维修厂家
				repair_vendor = "";
				// 批号
				batch_number = "";
				// 设备序列号
				device_serialnumber = "";
				// 主厂家
				device_vendor = "";
				// 设备型号
				device_model = "";
				// 硬件版本
				hardwareversion = "";
				// 软件版本
				softwareversion = "";
				// 版本检测
				version_check = "";
				// 出厂配置检测
				config_check = "";
				// 业务下发检测
				serv_issue_check = "";
				// 语音注册检测
				voice_regist_check = "";
				// 检测结果
				check_result = "";
				// 发往城市
				send_city = "";
				// 归属城市
				attribution_city = "";
				// 返修后的出厂时间
				manufacture_date = "";
				if (null != cellFirst)
				{
					cellFirst.setCellType(HSSFCell.CELL_TYPE_STRING);
					repair_vendor = StringUtil.getStringValue(
							cellFirst.getRichStringCellValue()).trim();
				}
				if (null != cellSecond)
				{
					cellSecond.setCellType(HSSFCell.CELL_TYPE_STRING);
					batch_number = StringUtil.getStringValue(
							cellSecond.getRichStringCellValue()).trim();
				}
				if (null != cellThird)
				{
					cellThird.setCellType(HSSFCell.CELL_TYPE_STRING);
					device_serialnumber = StringUtil.getStringValue(
							cellThird.getRichStringCellValue()).trim();
				}
				if (null != cellFourth)
				{
					cellFourth.setCellType(HSSFCell.CELL_TYPE_STRING);
					device_vendor = StringUtil.getStringValue(
							cellFourth.getRichStringCellValue()).trim();
				}
				if (null != cellFiveth)
				{
					cellFiveth.setCellType(HSSFCell.CELL_TYPE_STRING);
					device_model = StringUtil.getStringValue(
							cellFiveth.getRichStringCellValue()).trim();
				}
				if (null != cellSixth)
				{
					cellSixth.setCellType(HSSFCell.CELL_TYPE_STRING);
					hardwareversion = StringUtil.getStringValue(
							cellSixth.getRichStringCellValue()).trim();
				}
				if (null != cellSeventh)
				{
					cellSeventh.setCellType(HSSFCell.CELL_TYPE_STRING);
					softwareversion = StringUtil.getStringValue(
							cellSeventh.getRichStringCellValue()).trim();
				}
				if (null != cellEghth)
				{
					cellEghth.setCellType(HSSFCell.CELL_TYPE_STRING);
					version_check = StringUtil.getStringValue(
							cellEghth.getRichStringCellValue()).trim();
				}
				if (null != cellNighth)
				{
					cellNighth.setCellType(HSSFCell.CELL_TYPE_STRING);
					config_check = StringUtil.getStringValue(
							cellNighth.getRichStringCellValue()).trim();
				}
				if (null != cellTen)
				{
					cellTen.setCellType(HSSFCell.CELL_TYPE_STRING);
					serv_issue_check = StringUtil.getStringValue(
							cellTen.getRichStringCellValue()).trim();
				}
				if (null != cellEleven)
				{
					cellEleven.setCellType(HSSFCell.CELL_TYPE_STRING);
					voice_regist_check = StringUtil.getStringValue(
							cellEleven.getRichStringCellValue()).trim();
				}
				if (null != cellTwelve)
				{
					cellTwelve.setCellType(HSSFCell.CELL_TYPE_STRING);
					check_result = StringUtil.getStringValue(
							cellTwelve.getRichStringCellValue()).trim();
				}
				if (null != cellThirteen)
				{
					cellThirteen.setCellType(HSSFCell.CELL_TYPE_STRING);
					send_city = StringUtil.getStringValue(
							cellThirteen.getRichStringCellValue()).trim();
				}
				if (null != cellFourteen)
				{
					cellFourteen.setCellType(HSSFCell.CELL_TYPE_STRING);
					attribution_city = StringUtil.getStringValue(
							cellFourteen.getRichStringCellValue()).trim();
				}
				if (null != cellFifteen)
				{
					cellFifteen.setCellType(HSSFCell.CELL_TYPE_STRING);
					manufacture_date = StringUtil.getStringValue(
							cellFifteen.getRichStringCellValue()).trim();
				}
				if (StringUtil.IsEmpty(repair_vendor)
						|| StringUtil.IsEmpty(device_serialnumber)
						|| StringUtil.IsEmpty(manufacture_date))
				{
					logger.warn("第{}行数据不整合", new Object[] { i + 1 });
					continue;
				}
				successCount++;
				// 维修厂家
				tempMap.put("repair_vendor", repair_vendor);
				// web页面上传 默认是保内
				tempMap.put("insurance_status", "1");
				// 批号
				tempMap.put("batch_number", batch_number);
				// 设备序列号
				tempMap.put("device_serialnumber", device_serialnumber);
				// 主厂家
				tempMap.put("device_vendor", device_vendor);
				// 设备型号
				tempMap.put("device_model", device_model);
				// 硬件版本
				tempMap.put("hardwareversion", hardwareversion);
				// 软件版本
				tempMap.put("softwareversion", softwareversion);
				// 版本检测
				if ("合格".equals(version_check))
				{
					tempMap.put("version_check", "1");
				}
				else
				{
					tempMap.put("version_check", "0");
				}
				// 出厂配置检测
				if ("合格".equals(config_check))
				{
					tempMap.put("config_check", "1");
				}
				else
				{
					tempMap.put("config_check", "0");
				}
				// 业务下发检测
				if ("合格".equals(serv_issue_check))
				{
					tempMap.put("serv_issue_check", "1");
				}
				else
				{
					tempMap.put("serv_issue_check", "0");
				}
				// 语音注册检测
				if ("合格".equals(voice_regist_check))
				{
					tempMap.put("voice_regist_check", "1");
				}
				else
				{
					tempMap.put("voice_regist_check", "0");
				}
				// 检测结果
				if ("合格".equals(check_result))
				{
					tempMap.put("check_result", "1");
				}
				else
				{
					tempMap.put("check_result", "0");
				}
				// 发往城市
				tempMap.put("send_city", send_city);
				// 归属城市
				tempMap.put("attribution_city", attribution_city);
				// 返修后的出厂时间
				tempMap.put("manufacture_date", setTime(manufacture_date));
				list.add(tempMap);
				// 每 count 行执行一次
				if (i % count == 0)
				{
					dao.insertRepairDevInfo(list, importTime);
					// 清空list内存
					list.clear();
				}
			}
			// 不足 count 行 或最后不足 count 行 执行入库
			dao.insertRepairDevInfo(list, importTime);
			logger.warn("解析入库完成");
			return "1#解析入库完成,解析成功" + successCount + "条";
		}
		return null;
	}

	/***
	 * 下载模板文件
	 * @param filepath
	 * @param response
	 */
	public void download(String filepath, HttpServletResponse response) {
		logger.debug("download({},{})", new Object[]{filepath, response});
		
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			// path是指欲下载的文件的路径
			File file = new File(filepath);
			// 取得文件名
			String filename = file.getName();

			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}
		catch (IOException e)
		{
			logger.error("download file:[{}], error:", filepath, e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 时间转化
	 */
	/**
	 * 时间转化
	 */
	private String setTime(String time)
	{
		logger.debug("setTime()" + time);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (!StringUtil.IsEmpty(time))
		{
			dt = new DateTimeUtil(time);
			time = StringUtil.getStringValue(dt.getLongTime());
			return time;
		}
		else
		{
			return "";
		}
	}
}
