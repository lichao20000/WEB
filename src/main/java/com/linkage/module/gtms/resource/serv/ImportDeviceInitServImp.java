package com.linkage.module.gtms.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.liposs.common.util.TopoDAO;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.resource.dao.ImportDeviceInitDAO;

public class ImportDeviceInitServImp implements ImportDeviceInitServ {

	private static Logger logger = LoggerFactory
			.getLogger(ImportDeviceInitServImp.class);

	private ImportDeviceInitDAO dao = null;

	/**
	 * 解析上传的文件
	 */
	public String readUploadFile(File file, int rowNum, UserRes curUser,
			String gw_type, String fileType, String fileName) {

		logger.debug("ImportDeviceInitServImp==>readUploadFile()");

		if ("xls".equals(fileType)) {
			// analyzeExcel2 用于割接时，临时导入数据，analyzeExcel用户后期稳定之后用
			// return analyzeExcel2(file, rowNum, curUser, gw_type, fileName);
			return analyzeExcel(file, rowNum, curUser, gw_type, fileName);
		} else {
			return analyzeTxt(file, curUser, gw_type, fileName);
		}
	}

	/**
	 * 解析Excel
	 * 
	 * @param file
	 * @param rowNum
	 * @param curUser
	 * @param gw_type
	 * @param fileName
	 * @return
	 */
	public String analyzeExcel(File file, int rowNum, UserRes curUser,
			String gw_type, String fileName) {

		logger.debug("ImportDeviceInitServImp==>analyzeExcel()");

		Workbook wwb = null;
		Sheet ws = null;

		String retMsg = null;

		try {
			wwb = Workbook.getWorkbook(file);

			// 总sheet数
			// int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1; // 默认取第一页

			for (int m = 0; m < sheetNumber; m++) {

				String oui = "";
				String deviceSerialnumber = "";
				String cityId = "00";
				String cityName = "";
				String buyDate = "";
				String deviceMac = "";
				String devSubSn = "";
				String vendor_name = "";
				String model_name = "";
				String add_date = "";
				// 导入新增串号字段
				String serial_no = "";

				String sql = "";

				ws = wwb.getSheet(m);

				// 当前页总记录行数和列数
				int rowCount = ws.getRows(); // 行数
				int columeCount = ws.getColumns(); // 列数

				// 导入的Excel的列数不为5，则导入的文件不符合规范
				if (columeCount != 9) {
					logger.warn("导入的文件“" + fileName + "”的列数不等于9，不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}

				// 获取标题行，判断标题行是否符合模板的要求，防止前后数序乱套
				oui = ws.getCell(0, 0).getContents().trim(); // 第0列，第i行数据
				deviceSerialnumber = ws.getCell(1, 0).getContents().trim(); // 第1列，第i行数据
				cityName = ws.getCell(2, 0).getContents().trim(); // 第2列，第i行数据
				buyDate = ws.getCell(3, 0).getContents().trim(); // 第3列，第i行数据
				deviceMac = ws.getCell(4, 0).getContents().trim(); // 第4列，第i行数据
				vendor_name = ws.getCell(5, 0).getContents().trim(); // 第5列，第i行数据
				model_name = ws.getCell(6, 0).getContents().trim(); // 第6列，第i行数据
				add_date = ws.getCell(7, 0).getContents().trim(); // 第7列，第i行数据
				serial_no = ws.getCell(8, 0).getContents().trim(); // 第8列，第i行数据

				// 判断用户上传的Excel是否符合规范
				if (!"OUI".equals(oui) && !"oui".equals(oui)) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (!"设备序列号".equals(deviceSerialnumber)) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (cityName.startsWith("属") && cityName.endsWith("地")) {
				} else {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (!buyDate.startsWith("购买时间")) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (!"设备MAC".equals(deviceMac)) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (!"厂商".equals(vendor_name)) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				if (!"设备型号".equals(model_name)) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				// if (!"导入时间".equals(add_date)) {
				// logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
				// return "1"; // 导入的文件不符合规范
				// }
				if (!"设备串号".equals(serial_no)) {
					logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
					return "1"; // 导入的文件不符合规范
				}
				// rowNum 为每次最多能处理的记录行数 rowCount 为当前sheet的记录行数
				if (rowCount > rowNum + 1) {
					logger.warn("导入文件“" + fileName
							+ "”的总记录数大于2万行，返回不做处理（规定不允许大于2万，超过则不处理）");
					return "1"; // 记录数不可以超过2万，请重新导入
				}

				// 第一行为字段名，所以导入的Excel总行数大于1才做解析
				if (rowCount > 1 && columeCount > 0) {

					String staffId = String.valueOf(curUser.getUser().getId());

					// 初始化属地名与属地ID的对应关系<city_name, city_id>
					Map<String, String> cityNameCityIdMap = dao
							.getCityIdCityNameMap();

					// 用于存放批量SQL
					ArrayList<String> arrayList = new ArrayList<String>();
					// 用于check设备序列号
					List<String> devSnList = new ArrayList<String>();

					// i 从1开始，是因为第一行是标题行，不需要读取
					for (int i = 1; i < rowCount; i++) {

						// 读取Excel单元格的内容
						oui = ws.getCell(0, i).getContents().trim(); // 第0列，第i行数据
						deviceSerialnumber = ws.getCell(1, i).getContents()
								.trim(); // 第1列，第i行数据
						cityName = ws.getCell(2, i).getContents().trim(); // 第2列，第i行数据
						buyDate = ws.getCell(3, i).getContents().trim(); // 第3列，第i行数据
						deviceMac = ws.getCell(4, i).getContents().trim(); // 第4列，第i行数据
						vendor_name = ws.getCell(5, i).getContents().trim(); // 第5列，第i行数据
						model_name = ws.getCell(6, i).getContents().trim(); // 第6列，第i行数据
						add_date = ws.getCell(7, i).getContents().trim(); // 第7列，第i行数据
						serial_no = ws.getCell(8, i).getContents().trim(); // 第8列，第i行数据

						if (null == oui || "".equals(oui)) {
							logger.warn("OUI为空（要求不能为空），返回");
							return "-1"; // OUI不可以为空
						}
						if (null == deviceSerialnumber
								|| "".equals(deviceSerialnumber)) {
							logger.warn("设备序列号为空（要求不能为空），返回");
							return "-2"; // 设备序列号不可以为空
						}
						// if (null == cityName || "".equals(cityName)) {
						// logger.warn("属地为空（要求不能为空），返回");
						// return "-3"; // 属地不可以为空
						// }
						if (null != buyDate && !"".equals(buyDate)) {

							buyDate = formatDate(ws.getCell(3, i));

							if (buyDate.split("-").length != 3) {
								logger.warn("所导入文件“" + fileName + "”的购买时间："
										+ buyDate
										+ "格式不符合模板要求，要求格式为：YYYY-MM-DD，返回");
								return "1";
							} else {
								buyDate = String.valueOf(new DateTimeUtil(
										buyDate).getLongTime());
							}
						} else {
							buyDate = null;
						}

						// 将属地名称转换为city_id
						if (null != cityName && !"".equals(cityName)) {
							cityId = cityNameCityIdMap.get(cityName);
						}

						if (null == cityId || "".equals(cityId)) {
							logger.warn("属地表中不存在此属地：" + cityName);
							// return "2";
						}

						// 截取设备序列号的最后6位
						if (deviceSerialnumber.length() > 6) {
							devSubSn = deviceSerialnumber.substring(
									deviceSerialnumber.length() - 6,
									deviceSerialnumber.length());
						} else {
							devSubSn = deviceSerialnumber;
						}

						if (null == vendor_name || "".equals(vendor_name)) {
							logger.warn("厂商为空（要求不能为空），返回");
							return "-5"; // 设备序列号不可以为空
						}
						if (null == model_name || "".equals(model_name)) {
							logger.warn("设备型号为空（要求不能为空），返回");
							return "-6"; // 设备序列号不可以为空
						}

						if (null != add_date && !"".equals(add_date)) {
							add_date = formatDate(ws.getCell(7, i));
							if (add_date.split("-").length != 3) {
								logger.warn("所导入文件“" + fileName + "”导入时间："
										+ add_date
										+ "格式不符合模板要求，要求格式为：YYYY-MM-DD，返回");
								return "1";
							} else {
								add_date = String.valueOf(new DateTimeUtil(
										add_date).getLongTime());
							}
						} else {
							String date = new SimpleDateFormat("yyyy-MM-dd")
									.format(new Date());
							add_date = String.valueOf(new DateTimeUtil(date)
									.getLongTime());
						}

						int num = dao.checkDeviceSerialnumber(oui,
								deviceSerialnumber);

						// 根据
						// oui和deviceSerialnumber检查当前设备在预置设备表（tab_gw_device_init）中是否已存在，若已经存在，则不需要再次导入
						if (num == 0) {
							// 判断当前导入的文件中是否有重复的设备
							if (!devSnList.contains(oui + deviceSerialnumber)) {
								// 每解析一个设备，将当前的设备序列号号放到devSnList中，用于检测后续解析的设备在此列表中是否存在，如果存在，则无需再次导入
								devSnList.add(oui + deviceSerialnumber);

								sql = "insert into tab_gw_device_init (device_id,oui,device_serialnumber,city_id,buy_time,staff_id,remark,cpe_mac,cpe_currentupdatetime,gw_type,dev_sub_sn,vendor_name,model_name,add_date,serial_no) values('"
										+ TopoDAO.GetUnusedDeviceSerial(1)
										+ "', '"
										+ oui
										+ "', "
										+ "'"
										+ deviceSerialnumber
										+ "', '"
										+ cityId
										+ "', "
										+ buyDate
										+ ",'"
										+ staffId
										+ "', '导入预置设备', '"
										+ deviceMac
										+ "', null, "
										+ gw_type
										+ ", '"
										+ devSubSn
										+ "','"
										+ vendor_name
										+ "','"
										+ model_name
										+ "',"
										+ add_date
										+ ",'"
										+ serial_no
										+ "')";

								logger.warn("sql=" + sql);

								arrayList.add(sql);

							} else {
								devSnList.add(oui + deviceSerialnumber);
								logger.warn("此设备："
										+ deviceSerialnumber
										+ " 在预置设备表（tab_gw_device_init）中已经存在，无需再次导入");
							}
						} else {
							devSnList.add(oui + deviceSerialnumber);
							logger.warn("此设备：" + deviceSerialnumber
									+ " 在预置设备表（tab_gw_device_init）中已经存在，无需再次导入");
						}
					}

					devSnList = null; // 清除缓存

					// logger.warn("===arrayList="+arrayList+"===");
					int[] result = DataSetBean.doBatch(arrayList);
					logger.warn("SQL批量提交，数据库返回：result=[{}]", result.length);

					retMsg = "0"; // 导入成功

				} else {
					logger.warn("上传的Excel文件为空！");
					return "3"; // 上传的Excel文件为空，请重新上传
				}
			}

			return retMsg;

		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("解析“" + fileName + "”失败");
			logger.error("解析“" + fileName + "”失败，msg=({})", e.getMessage());
			return "5"; // 解析Excel失败！
		}

	}

	/**
	 * 解析Excel 这个方法用于割接时使用，割接是的OUI跟设备序列号是连在一起的
	 * 
	 * @param file
	 * @param rowNum
	 * @param curUser
	 * @param gw_type
	 * @param fileName
	 * @return
	 */
	public String analyzeExcel2(File file, int rowNum, UserRes curUser,
			String gw_type, String fileName) {

		logger.debug("ImportDeviceInitServImp==>analyzeExcel()");

		Workbook wwb = null;
		Sheet ws = null;

		String retMsg = null;
		String sql = "";

		try {
			wwb = Workbook.getWorkbook(file);

			// 总sheet数
			int sheetNumber = 1; // 默认取第一页

			for (int m = 0; m < sheetNumber; m++) {

				String oui = "";
				String ouideviceSerialnumber = "";
				String deviceSerialnumber = "";
				String cityId = "00";
				String buyDate = null;
//				String deviceMac = null;
				String devSubSn = null;

				ws = wwb.getSheet(m);

				// 当前页总记录行数和列数
				int rowCount = ws.getRows(); // 行数
				int columeCount = ws.getColumns(); // 列数

				// 第一行为字段名，所以导入的Excel总行数大于1才做解析
				if (rowCount > 1 && columeCount > 0) {

					String staffId = String.valueOf(curUser.getUser().getId());

					// 用于存放批量SQL
					ArrayList<String> arrayList = new ArrayList<String>();
					// 用户check设备序列号
					List<String> devSnList = new ArrayList<String>();

					// i 从1开始，是因为第一行是标题行，不需要读取
					for (int i = 1; i < rowCount; i++) {

						// 读取Excel单元格的内容
						ouideviceSerialnumber = ws.getCell(7, i).getContents()
								.trim(); // 第7列，第i行数据

						if (null == ouideviceSerialnumber
								|| "".equals(ouideviceSerialnumber)) {
							logger.warn("设备序列号为空（要求不能为空），返回");
							return "-2"; // 设备序列号不可以为空
						}

						// 截取设备序列号的最后6位
						if (!"".equals(ouideviceSerialnumber)
								&& ouideviceSerialnumber.length() > 6) {

							oui = ouideviceSerialnumber.substring(0, 6); // OUI

							deviceSerialnumber = ouideviceSerialnumber
									.substring(6); // 截掉前6位，剩下的是设备序列号

							if (deviceSerialnumber.length() > 6) {
								devSubSn = deviceSerialnumber.substring(
										deviceSerialnumber.length() - 6,
										deviceSerialnumber.length());
							} else {
								devSubSn = deviceSerialnumber;
							}

							int num = dao.checkDeviceSerialnumber(oui,
									deviceSerialnumber);

							// 预置设备表中没有当前设备，则加到批处理SQL中
							if (num == 0) {
								// 只有当前列表中没有此sql时，方可将此sql加入到列表中
								if (!devSnList.contains(ouideviceSerialnumber)) {
									// 每解析一个设备，将当前的设备序列号号放到devSnList中，用于检测后续解析的设备在此列表中是否存在，如果存在，则无需再次导入
									devSnList.add(ouideviceSerialnumber);

									sql = "insert into tab_gw_device_init values('"
											+ TopoDAO.GetUnusedDeviceSerial(1)
											+ "', '"
											+ oui
											+ "', "
											+ "'"
											+ deviceSerialnumber
											+ "', '"
											+ cityId
											+ "', "
											+ buyDate
											+ ",'"
											+ staffId
											+ "', '导入预置设备', null , null, "
											+ gw_type + ", '" + devSubSn + "')";
									logger.warn("sql=" + sql);

									arrayList.add(sql);

								} else {
									devSnList.add(ouideviceSerialnumber);
									logger.warn("此设备："
											+ ouideviceSerialnumber
											+ " 在预置设备表（tab_gw_device_init）中已经存在");
								}
							} else {
								devSnList.add(ouideviceSerialnumber);
								logger.warn("此设备：" + ouideviceSerialnumber
										+ " 在预置设备表（tab_gw_device_init）中已经存在");
							}

						}
					}

					// logger.warn("===arrayList="+arrayList+"===");
					if (null != arrayList && !"".equals(arrayList)
							&& arrayList.size() > 0) {
						int[] result = DataSetBean.doBatch(arrayList);
						// 此处的result.length是数据库处理最后一批SQL的记录数
						logger.warn("SQL批量提交，数据库返回：result=[{}]", result.length);
					}
					devSnList = null;

					retMsg = "0"; // 导入成功

				} else {
					logger.warn("上传的Excel文件为空！");
					return "3"; // 上传的Excel文件为空，请重新上传
				}
			}

			return retMsg;

		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("解析“" + fileName + "”失败");
			logger.error("解析“" + fileName + "”失败，msg=({})", e.getMessage());
			return "5"; // 解析Excel失败！
		}

	}

	/**
	 * 解析TXT文本
	 * 
	 * @param file
	 * @param rowNum
	 * @return
	 */
	public String analyzeTxt(File file, UserRes curUser, String gw_type,
			String fileName) {

		logger.debug("ImportDeviceInitServImp==>analyzeTxt()");

		// 用于存放批量SQL
		ArrayList<String> arrayList = new ArrayList<String>();
		// 用于check设备序列号
		List<String> devSnList = new ArrayList<String>();

		InputStreamReader read=null;
		BufferedReader bufferedReader=null;
		try {
			String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )

			if (file.isFile() && file.exists()) {

				read = new InputStreamReader(
						new FileInputStream(file), encoding);

				bufferedReader = new BufferedReader(read);

				String lineTXT = null;
				String oui = "";
				String deviceSerialnumber = "";
				String cityId = "00";
				String cityName = "";
				String buyDate = "";
				String deviceMac = "";
				String devSubSn = "";
				String vendor_name = "";
				String model_name = "";
				String add_date = "";
				// 导入新增串号字段
				String serial_no = "";

				String sql = "";

				String staffId = String.valueOf(curUser.getUser().getId());

				// 先读取首行（标题行） 判断标题行是否符合规范
				if ((lineTXT = bufferedReader.readLine()) != null) {

					// 因为TXT文本的首行为标题行：OUI|设备序列号|属地|购买时间(YYYY-MM-DD)|设备MAC|厂商|设备型号|导入时间(YYYY-MM-DD)|设备串号
					if (lineTXT.trim().startsWith("O")
							|| lineTXT.trim().startsWith("o")) {

						// 将标题行转换为数组，判断TXT文本内容是否按照注意事项来上传的
						String[] nameArray = lineTXT.trim().split("\\|");

						if (nameArray.length != 9) {
							logger.warn("所导入的TXT文本不符合要求，要求记录为9列");
							return "1";
						} else {
							// 判断用户上传的TXT是否符合规范
							if (!"OUI".equals(nameArray[0])
									&& !"oui".equals(nameArray[0])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!"设备序列号".equals(nameArray[1])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (nameArray[2].startsWith("属")
									&& nameArray[2].endsWith("地")) {
							} else {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!nameArray[3].startsWith("购买时间")) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!"设备MAC".equals(nameArray[4])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!"厂商".equals(nameArray[5])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!"设备型号".equals(nameArray[6])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!"导入时间".equals(nameArray[7])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
							if (!"设备串号".equals(nameArray[8])) {
								logger.warn("导入文件“" + fileName + "”的标题行不符合模板要求");
								return "1"; // 导入的文件不符合规范
							}
						}

						nameArray = null; // 清除缓存数据

					} else {
						logger.warn("所导入的TXT文本不符合要求，首行应为标题行");
						return "1";
					}
				} else {
					logger.warn("所导入的TXT文本为空");
					return "3";
				}

				// 初始化属地表<city_name,city_id>
				Map<String, String> cityNameCityIdMap = dao
						.getCityIdCityNameMap();

				// 从第二行开始读取，第一行为标题行
				while ((lineTXT = bufferedReader.readLine()) != null) {

					String[] array = lineTXT.trim().split("\\|");

					if (array.length != 9) {
						logger.warn(fileName + "文件内容不符合规范");
						return "1"; // 所导入的文本内容不符合规范，请确认
					} else {
						oui = array[0];
						deviceSerialnumber = array[1];
						cityName = array[2];
						buyDate = array[3];
						deviceMac = array[4];
						vendor_name = array[5];
						model_name = array[6];
						add_date = array[7];
						serial_no = array[8];
						if (null == oui || "".equals(oui)) {
							logger.warn("OUI为空（要求不能为空），返回");
							return "-1"; // OUI不可以为空
						}
						if (null == deviceSerialnumber
								|| "".equals(deviceSerialnumber)) {
							logger.warn("设备序列号为空（要求不能为空），返回");
							return "-2"; // 设备序列号不可以为空
						}
						// if (null == cityName || "".equals(cityName)) {
						// logger.warn("属地为空（要求不能为空），返回");
						// return "-3"; //属地不可以为空
						// }
						if (null != buyDate && !"".equals(buyDate)) {
							if (buyDate.split("-").length != 3) {
								logger.warn("所导入文件“" + fileName + "”的购买时间："
										+ buyDate
										+ "格式不符合模板要求，要求格式为：YYYY-MM-DD，返回");
								return "1";
							} else {
								buyDate = String.valueOf(new DateTimeUtil(
										buyDate).getLongTime());
							}
						} else {
							buyDate = null;
						}

						// // 将属地名称转换为city_id
						if (null != cityName && !"".equals(cityName)) {
							cityId = cityNameCityIdMap.get(cityName);
						}

						if (null == cityId || "".equals(cityId)) {
							logger.warn("属地表中不存在此属地：" + cityName);
							// return "2";
						}

						// 截取设备序列号的最后6位
						if (deviceSerialnumber.length() > 6) {
							devSubSn = deviceSerialnumber.substring(
									deviceSerialnumber.length() - 6,
									deviceSerialnumber.length());
						} else {
							devSubSn = deviceSerialnumber;
						}

						if (null == vendor_name || "".equals(vendor_name)) {
							logger.warn("厂商为空（要求不能为空），返回");
							return "-5"; // OUI不可以为空
						}
						if (null == model_name || "".equals(model_name)) {
							logger.warn("设备型号为空（要求不能为空），返回");
							return "-6"; // 设备序列号不可以为空
						}
						// if (null == cityName || "".equals(cityName)) {
						// logger.warn("属地为空（要求不能为空），返回");
						// return "-3"; //属地不可以为空
						// }
						if (null != add_date && !"".equals(add_date)) {
							if (add_date.split("-").length != 3) {
								logger.warn("所导入文件“" + fileName + "”的导入时间："
										+ add_date
										+ "格式不符合模板要求，要求格式为：YYYY-MM-DD，返回");
								return "1";
							} else {
								add_date = String.valueOf(new DateTimeUtil(
										add_date).getLongTime());
							}
						} else {
							add_date = null;
						}

						int num = dao.checkDeviceSerialnumber(oui,
								deviceSerialnumber);

						// 根据
						// oui和deviceSerialnumber检查当前设备在预置设备表（tab_gw_device_init）中是否已存在，若已经存在，则不需要再次导入
						if (num == 0) {
							// 判断当前导入的文件中是否有重复的设备
							if (!devSnList.contains(oui + deviceSerialnumber)) {
								// 每解析一个设备，将当前的设备序列号号放到devSnList中，用于检测后续解析的设备在此列表中是否存在，如果存在，则无需再次导入
								devSnList.add(oui + deviceSerialnumber);

								sql = "insert into tab_gw_device_init (device_id,oui,device_serialnumber,city_id,buy_time,staff_id,remark,cpe_mac,"
										+ "cpe_currentupdatetime,gw_type,dev_sub_sn,vendor_name,model_name,add_date,serial_no) values('"
										+ TopoDAO.GetUnusedDeviceSerial(1)
										+ "', '"
										+ oui
										+ "', "
										+ "'"
										+ deviceSerialnumber
										+ "', '"
										+ cityId
										+ "', "
										+ buyDate
										+ ",'"
										+ staffId
										+ "', '导入预置设备', '"
										+ deviceMac
										+ "', null, "
										+ gw_type
										+ ", '"
										+ devSubSn
										+ "','"
										+ vendor_name
										+ "','"
										+ model_name
										+ "',"
										+ add_date
										+ ",'"
										+ serial_no
										+ "')";
								logger.warn("sql=" + sql);
								arrayList.add(sql);

							} else {
								devSnList.add(oui + deviceSerialnumber);
								logger.warn("此设备：" + deviceSerialnumber
										+ " 在预置设备表（tab_gw_device_init）中已经存在");
							}
						} else {
							devSnList.add(oui + deviceSerialnumber);
							logger.warn("此设备：" + deviceSerialnumber
									+ " 在预置设备表（tab_gw_device_init）中已经存在");
						}
					}
				}
				read.close();
				read=null;
				

				devSnList = null; // 清除缓存

				// logger.warn("===arrayList="+arrayList+"===");
				int[] result = DataSetBean.doBatch(arrayList);
				// 此处的result.length是数据库处理最后一批SQL的记录数
				logger.warn("SQL批量提交，数据库返回：result=[{}]", result.length);

				return "0"; // 操作成功

			} else {
				logger.warn("文本文件没上传成功");
				return "4"; // 文本文件没上传成功，请重新上传
			}
		} catch (Exception e) {
			logger.warn("读取文件内容操作出错，fileNmae=" + fileName);
			logger.error("读取文件内容操作出错，fileNmae=" + fileName + "，msg=({})",
					e.getMessage());
			e.printStackTrace();
			return "5"; // 解析TXT文本出错
		}finally{
			try {
				if (null != read) {
					read.close();
					read = null;
				}
			} catch (Exception e) {
				logger.error("close error ,  msg=({})", e.getMessage());
			}
			
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (Exception e) {
				logger.error("close error ,  msg=({})", e.getMessage());
			}
		}
	}

	/**
	 * 日期格式化 YYYY-MM-DD
	 * 
	 * @param cell
	 * @return
	 */
	public String formatDate(Cell cell) {

		logger.debug("ImportDeviceInitServImp==>formatDate()");

		if (cell.getType() == CellType.DATE) {
			DateCell dateCell = (DateCell) cell;
			Date date = dateCell.getDate();
			return new DateTimeUtil(date).getYYYY_MM_DD();
		} else {
			return cell.getContents().trim();
		}
	}

	public ImportDeviceInitDAO getDao() {
		return dao;
	}

	public void setDao(ImportDeviceInitDAO dao) {
		this.dao = dao;
	}

	public static void main(String[] args) {
		//
		// Workbook wwb = null;
		// Sheet ws = null;
		// try {
		// wwb = Workbook.getWorkbook(new File("D:\\batchImportTemplate.xls"));
		//
		// //总sheet数
		// //int sheetNumber = wwb.getNumberOfSheets();
		// int sheetNumber = 1; // 默认取第一页
		//
		// for (int m = 0; m < sheetNumber; m++){
		//
		// String oui = "";
		// String deviceSerialnumber = "";
		// String cityId = "";
		// String cityName = "";
		// String buyDate = "";
		// String deviceMac = "";
		//
		// ws = wwb.getSheet(m);
		//
		// //当前页总记录行数和列数
		// int rowCount = ws.getRows(); // 行数
		// int columeCount = ws.getColumns(); // 列数
		//
		// System.out.println("====rowCount="+rowCount+"=====");
		//
		//
		// //第一行为字段名，所以导入的Excel总行数大于1才做解析
		// if (rowCount > 1 && columeCount > 0){
		//
		// // i 从1开始，是因为第一行是标题行，不需要读取
		// for (int i = 1; i < rowCount; i++){
		// // 读取Excel单元格的内容
		// // oui = ws.getCell(0, i).getContents().trim(); // 第0列，第i行数据
		// // deviceSerialnumber = ws.getCell(1, i).getContents().trim(); //
		// 第1列，第i行数据
		// // cityName = ws.getCell(2, i).getContents().trim(); // 第2列，第i行数据
		// buyDate = ws.getCell(3, i).getContents().trim(); // 第3列，第i行数据
		// deviceMac = ws.getCell(4, i).getContents().trim(); // 第4列，第i行数据
		//
		// if (ws.getCell(3, i).getType() == CellType.DATE) {
		// DateCell dateCell = (DateCell)ws.getCell(3, i);
		// Date date = dateCell.getDate();
		// buyDate = new DateTimeUtil(date).getYYYY_MM_DD();
		// }else {
		// buyDate = ws.getCell(3, i).getContents().trim();
		// }
		//
		// System.out.println("====buyDate=="+buyDate+"===");
		// System.out.println("====buyDatecellType=="+ws.getCell(3,
		// i).getType()+"===");
		//
		// }
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//

		// ArrayList<String> arrayList = new ArrayList<String>();
		// String sql2 = "22";
		// String
		// sql1="insert into tab_gw_device_init values('59083', '6416F0', '472006416F08903FB', '00', null,'2', '导入预置设备', null , null, 1, '8903FB')";
		// arrayList.add(sql1);
		//
		// System.out.println(arrayList.contains(sql2));

		List<String> list = new ArrayList<String>();
		String aa = "123";
		String bb = "333";
		list.add(aa);
		list.add(bb);
		System.out.println(list.contains("1232"));
		for (String string : list) {
			System.out.println(string);
		}

	}
}
