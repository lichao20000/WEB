
package com.linkage.module.gtms.stb.resource.serv;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.ImportMacInitDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-6-4
 * @category com.linkage.module.lims.stb.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ImportMacInitBIO
{

	private static Logger logger = LoggerFactory.getLogger(ImportMacInitBIO.class);
	private ImportMacInitDAO dao = null;
	// 初始化厂商数据
	private static final List<String> vendorNameList = Arrays.asList("华为", "中兴", "创维",
			"海信", "长虹", "UT", "乐视", "文广", "百事通", "烽火", "扬谷", "杰赛", "其他");

	/**
	 * 解析上传的文件
	 */
	public String readUploadFile(File file, int rowNum, String fileType, String fileName,
			String staff_id, String city_id)
	{
		return analyzeExcel(file, rowNum, fileName, staff_id, city_id);
	}

	public String analyzeExcel(File file, int rowNum, String fileName, String staff_id,
			String city_id)
	{
		Workbook wwb = null;
		Sheet ws = null;
		String retMsg = null;
		String deviceId = dao.getDeviceId();
		int num = 10000;
		boolean isNum = deviceId.matches("[0-9]+");
		if (isNum)
		{
			num = Integer.parseInt(deviceId);
		}
		try
		{
			wwb = Workbook.getWorkbook(file);
			// 默认是第一页
			int sheetNumber = 1;
			for (int m = 0; m < sheetNumber; m++)
			{
				// 订单号
				String order_id = "";
				// 包装箱号
				String package_no = "";
				// 厂商
				String vendor_name = "";
				// 购货方式
				String supply_model = "";
				// 产品型号
				String device_model = "";
				// 设备MAC始
				String deviceMac_start = "";
				// 设备MAC终
				String deviceMac_end = "";
				// 设备序列号
				String device_sn = "";
				ws = wwb.getSheet(m);
				int rowCount = ws.getRows(); // 行数
				int columeCount = ws.getColumns(); // 列数
				if (columeCount != 8)
				{
					logger.warn("导入的文件“" + fileName + "”的列数不等于8，不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				// 判断模板是否符合标准
				order_id = ws.getCell(0, 0).getContents().trim();
				package_no = ws.getCell(1, 0).getContents().trim();
				vendor_name = ws.getCell(2, 0).getContents().trim();
				supply_model = ws.getCell(3, 0).getContents().trim();
				device_model = ws.getCell(4, 0).getContents().trim();
				deviceMac_start = ws.getCell(5, 0).getContents().trim();
				deviceMac_end = ws.getCell(6, 0).getContents().trim();
				device_sn = ws.getCell(7, 0).getContents().trim();
				// String macMatch =
				// "^[A-Z|0-9]{2}[:]{1}[A-Z|0-9]{2}[:]{1}[A-Z|0-9]{2}[:]{1}[A-Z|0-9]{2}[:]{1}[A-Z|0-9]{2}[:]{1}[A-Z|0-9]{2}$";
				String macMatch = "^[A-F|0-9]{12}$";
				// 判断用户上传的Excel是否符合规范
				if (null != order_id && !"订单号".equals(order_id))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != package_no && !"包装箱号".equals(package_no))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != vendor_name && !"厂商".equals(vendor_name))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != supply_model && !"供货方式".equals(supply_model))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != device_model && !"产品型号".equals(device_model))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != deviceMac_start && !"设备MAC始".equals(deviceMac_start))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != deviceMac_end && !"设备MAC终".equals(deviceMac_end))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (null != device_sn && !"设备序列号".equals(device_sn))
				{
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (rowCount > rowNum + 1)
				{
					logger.warn("导入文件“" + fileName
							+ "”的总记录数大于2万行，返回不做处理（规定不允许大于2万，超过则不处理）");
					return "1"; // 记录数不可以超过2万，请重新导入
				}
				// 解析xls，因为第一行为标题行不作解析
				if (rowCount > 1 && columeCount > 0)
				{
					// 初始化属地名与属地ID的对应关系<city_name, city_id>
					// Map<String, String> cityNameCityIdMap = dao.getCityIdCityNameMap();
					// 用于存放批量SQL
					ArrayList<String> arrayList = new ArrayList<String>();
					for (int i = 1; i < rowCount; i++)
					{
						order_id = ws.getCell(0, i).getContents().trim();
						package_no = ws.getCell(1, i).getContents().trim();
						vendor_name = ws.getCell(2, i).getContents().trim();
						supply_model = ws.getCell(3, i).getContents().trim();
						device_model = ws.getCell(4, i).getContents().trim();
						deviceMac_start = ws.getCell(5, i).getContents().trim();
						deviceMac_end = ws.getCell(6, i).getContents().trim();
						device_sn = ws.getCell(7, i).getContents().trim();
						if (StringUtil.IsEmpty(vendor_name))
						{
							logger.warn("厂商（要求不可以为空）");
							return "-1";
						}
						if (StringUtil.IsEmpty(device_model))
						{
							logger.warn("产品型号（要求不可以为空）");
							return "-2";
						}
						// JXDX-ITV-REQ-20170622-WUWF-001(ITV终端管理平台MAC批量导入设备厂家规则去除)
						/* 
						if (!vendorNameList.contains(vendor_name))
						{
							logger.warn("厂商不存在！");
							return "-4";
						}*/
						if (StringUtil.IsEmpty(deviceMac_start))
						{
							logger.warn("设备MAC始（要求不可以为空）");
							return "-6";
						}
						else
						{
							if (!deviceMac_start.matches(macMatch))
							{
								logger.warn("设备MAC:" + deviceMac_start + "的格式不符合要求，返回");
								return "6";
							}
						}
						if (StringUtil.IsEmpty(deviceMac_end))
						{
							logger.warn("设备MAC终（要求不可以为空）");
							return "-7";
						}
						else
						{
							if (!deviceMac_end.matches(macMatch))
							{
								logger.warn("设备MAC:" + deviceMac_end + "的格式不符合要求，返回");
								return "7";
							}
						}
						ArrayList<String> deviceMacList = new ArrayList<String>();
						//修改处
						ArrayList<String> deviceMacListnew = new ArrayList<String>();
						if (!StringUtil.IsEmpty(deviceMac_start)
								&& !StringUtil.IsEmpty(deviceMac_end))
						{
							deviceMacList = this.getMacArray(deviceMac_start,
									deviceMac_end);
						}
						logger.warn("deviceMacList==="+deviceMacList);
						//修改处判断是否有重复
						for (String str: deviceMacList)
						{
							if(deviceMacListnew.contains(str)){
								logger.warn("mac地址有重复,重复值为：", str);
							}else{
								deviceMacListnew.add(str);
							}
						}
						for (int j = 0; j < deviceMacListnew.size(); j++)
						{
							String deviceMac = deviceMacListnew.get(j);
							int number = dao.checkDeviceMacAndSn(deviceMac, device_sn);
							if (number == 0)
							{
								++num;
								StringBuffer ssql = getSql(num, city_id, order_id,
										package_no, vendor_name, supply_model,
										device_model, deviceMac, device_sn, staff_id);
								arrayList.add(ssql.toString());
								
								//WEB上传导入MAC地址时，解析文件后，生成sql时满200即刻提交。  
								if(arrayList.size()>=200)
								{
									int result = dao.batchSql(arrayList);
									arrayList.clear();
									logger.warn("SQL达到200,批量提交，数据库返回：result=[{}]", result);
								}
							}
							else
							{
								logger.warn("所导入文件" + fileName + "中设备mac:" + deviceMac
										+ " 在预置设备表（stb_tab_gw_device_init）中已经存在");
							}
						}
						int result = dao.batchSql(arrayList);
						arrayList.clear();
						logger.warn("SQL批量提交，数据库返回：result=[{}]", result);
					}
					retMsg = "0";
				}
				else
				{
					logger.warn("上传的Excel文件为空！");
					return "3"; // 上传的Excel文件为空，请重新上传
				}
			}
			return retMsg;
		}
		catch (Exception e)
		{
			logger.warn("解析“" + fileName + "”失败");
			logger.warn(e.getMessage(), e);
			return "5"; // 解析Excel失败！
		}
	}

	/**
	 * 日期格式化 YYYY-MM-DD
	 * 
	 * @param cell
	 * @return
	 */
	public String formatDate(Cell cell)
	{
		logger.debug("ImportMacInitBIO==>formatDate()");
		if (cell.getType() == CellType.DATE)
		{
			DateCell dateCell = (DateCell) cell;
			Date date = dateCell.getDate();
			return new DateTimeUtil(date).getYYYY_MM_DD();
		}
		else
		{
			return cell.getContents().trim();
		}
	}

	private StringBuffer getSql(int num, String city_id, String order_id,
			String package_no, String vendor_name, String supply_model,
			String device_model, String deviceMac, String device_sn, String staff_id)
	{
		long time = System.currentTimeMillis() / 1000;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into stb_tab_devmac_init(device_id,city_id,buy_time,cpe_currentupdatetime ");
		if (!StringUtil.IsEmpty(staff_id))
		{
			sql.append(",staff_id ");
		}
		if (!StringUtil.IsEmpty(order_id))
		{
			sql.append(",order_id ");
		}
		if (!StringUtil.IsEmpty(package_no))
		{
			sql.append(",package_no ");
		}
		if (!StringUtil.IsEmpty(supply_model))
		{
			sql.append(",supply_mode ");
		}
		sql.append(",vendor_name, device_model, cpe_mac ");
		if (!StringUtil.IsEmpty(device_sn))
		{
			sql.append(",device_sn");
		}
		sql.append(") values('").append(num).append("','").append(city_id).append("',")
				.append(time).append(",").append(time);
		if (!StringUtil.IsEmpty(staff_id))
		{
			sql.append(",'").append(staff_id).append("'");
		}
		if (!StringUtil.IsEmpty(order_id))
		{
			sql.append(",'").append(order_id).append("'");
		}
		if (!StringUtil.IsEmpty(package_no))
		{
			sql.append(",' ").append(package_no).append("'");
		}
		if (!StringUtil.IsEmpty(supply_model))
		{
			sql.append(",'").append(supply_model).append("'");
		}
		sql.append(",'").append(vendor_name).append("','").append(device_model)
				.append("','").append(deviceMac).append("'");
		if (!StringUtil.IsEmpty(device_sn))
		{
			sql.append(",'").append(device_sn).append("'");
		}
		sql.append(")");
		logger.info(sql.toString());
		return sql;
	}

	private  ArrayList<String> getMacArray(String deviceMac_start, String deviceMac_end)
	{
		ArrayList<String> deviceMacList = new ArrayList<String>();
		long macStart = Long.parseLong(deviceMac_start, 16);
		long macEnd = Long.parseLong(deviceMac_end, 16);
		long num = macEnd - macStart + 1;
		for (int i = 0; i < num; i++)
		{
			long numData = macStart + i;
			String mac = Long.toHexString(numData).toUpperCase();
			String newMac = AddZero(mac);
			deviceMacList.add(newMac);
		}
		return deviceMacList;
	}

	private  String AddZero(String mac)
	{
		String newMac = mac;
		int num = mac.length();
		while (num < 12)
		{
			newMac = "0" + newMac;
			num++;
		}
		return newMac;
	}

	public ImportMacInitDAO getDao()
	{
		return dao;
	}

	public void setDao(ImportMacInitDAO dao)
	{
		this.dao = dao;
	}
}
