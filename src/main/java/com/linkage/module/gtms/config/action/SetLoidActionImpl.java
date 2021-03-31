package com.linkage.module.gtms.config.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.SetLoidServ;
import com.linkage.module.gwms.Global;

public class SetLoidActionImpl extends splitPageAction implements SessionAware {
	private static Logger logger = LoggerFactory
			.getLogger(SetLoidActionImpl.class);
	private static final long serialVersionUID = -7717771870335534826L;

	private Map session;

	private String gw_type = "1";

	private String ajax = "";
	// 上传文件的处理开始
	private File upload;// 实际上传文件

	private String fileName; // 上传的文件名

	private String fileType; // 上传的文件名
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;

	private String cityId;

	private long userId = 0l;

	private List resultList = null;

	private String loid = "";

	private String deviceNumber = "";

	private String message = "";

	private String statu = "";

	private SetLoidServ bio = null;

	/** 导出数据 */
	private List<Map> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileNameExcel;

	public String initSetLoid() {
		logger.warn("SetLoidActionImpl->initSetLoid()");
		logger.warn("startTime=" + startTime + "&&endTime=" + endTime);
		this.setTime();
		resultList = bio.queryDeviceDetail(loid, deviceNumber, startTime,
				endTime, statu, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getCount(loid, deviceNumber, startTime,
				endTime, statu, curPage_splitPage, num_splitPage);
		return "list";
	}

	public String initSetLoidExcel() {
		logger.debug("initSetLoidExcel()");
		logger.warn("startTime=" + startTime + "&&endTime=" + endTime);
		this.setTime();
		title = new String[] { "区域", "OUI", "LOID", "设备序列号", "执行日期", "状态" };
		column = new String[] { "city_name", "oui", "loid",
				"device_serialnumber", "update_time", "status" };
		fileName = "回填LOID详细信息";
		data = bio.queryDeviceDetail(loid, deviceNumber, startTime, endTime,
				statu);
		return "excel";
	}

	// 配置Loid
	public String doConfig() throws IOException {
		logger.warn("SetLoidActionImpl->doConfig()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();

		if (!"xls".equals(fileType) && !"txt".equals(fileType)
				&& !"csv".equals(fileType)) {
			this.message = "上传的文件格式不正确！";
			return "isSucc";
		}
		List<Map<String, String>> dataList = null;
		try {
			if ("txt".equals(fileType)) {
				dataList = this.parseTXT(fileName);
			} else {
				dataList = this.parseExcel(fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			this.message = "文件没找到！";
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
			this.message = "文件解析出错！";
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			this.message = "文件解析出错！";
		}

		if (null != dataList && dataList.size() > 0) {
			if (dataList.size() > 5000) {
				message = "导入设备过多，请将数量控制在5000以内";
				return message;
			}
			message = bio.doConfig(cityId, userId, dataList);
		} else {
			message = "文件异常，请检查文件！";
		}
		return "isSucc";
	}

	/**
	 * 解析excel文件
	 * 
	 * @param excelFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public List<Map<String, String>> parseExcel(String fileName)
			throws IOException {

		logger.debug("start to parse file:[{}]", fileName);
		String oui = "";
		String device_serialnumber = "";
		String loid = "";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> tempMap = null;

		String targetDirectory = ServletActionContext.getServletContext()
				.getRealPath("/loid");
		logger.warn("targetDirectory=" + targetDirectory);
		String targetFileName = new Date().getTime() + fileName;
		File target = new File(targetDirectory, targetFileName);
		FileUtils.copyFile(upload, target);
		if (null != target && target.exists()) {
			try {
				// 创建工作表
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
						target));
				// 获得sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 获得sheet的总行数
				int rowCount = sheet.getLastRowNum();
				logger.warn("获得sheet的总行数:" + rowCount);
				// 如果是江苏
				if (Global.JSDX.equals(Global.instAreaShortName)) {
					// 循环解析每一行，第一行不取
					for (int i = 1; i <= rowCount; i++) {
						tempMap = new HashMap<String, String>();
						// 获得行对象
						HSSFRow row = sheet.getRow(i);
						HSSFCell cell = row.getCell(0);
						HSSFCell cellSec = row.getCell(1);
						HSSFCell cellTh = row.getCell(2);
						if (null != cell) {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							device_serialnumber = StringUtil
									.getStringValue(cell
											.getRichStringCellValue());
							tempMap.put("device_serialnumber",
									device_serialnumber);
						}
						if (null != cellSec) {
							cellSec.setCellType(HSSFCell.CELL_TYPE_STRING);
							loid = StringUtil.getStringValue(cellSec
									.getRichStringCellValue());
							tempMap.put("loid", loid);
						}
						logger.warn("tempMap.toString()==>"
								+ tempMap.toString());
						list.add(tempMap);
					}
				} else {
					// 循环解析每一行，第一行不取
					for (int i = 1; i <= rowCount; i++) {
						tempMap = new HashMap<String, String>();
						// 获得行对象
						HSSFRow row = sheet.getRow(i);
						HSSFCell cell = row.getCell(0);
						HSSFCell cellSec = row.getCell(1);
						HSSFCell cellTh = row.getCell(2);
						if (null != cell) {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							oui = StringUtil.getStringValue(cell
									.getRichStringCellValue());
							tempMap.put("oui", oui);
						}
						if (null != cellSec) {
							cellSec.setCellType(HSSFCell.CELL_TYPE_STRING);
							device_serialnumber = StringUtil
									.getStringValue(cellSec
											.getRichStringCellValue());
							tempMap.put("device_serialnumber",
									device_serialnumber);
						}
						if (null != cellTh) {
							cellTh.setCellType(HSSFCell.CELL_TYPE_STRING);
							loid = StringUtil.getStringValue(cellTh
									.getRichStringCellValue());
							tempMap.put("loid", loid);
						}
						logger.warn("tempMap.toString()==>"
								+ tempMap.toString());
						list.add(tempMap);
					}
				}
				// 循环解析每一行，第一行不取
			} catch (Exception e) {
				logger.error("parse File happen Exception = " + e);
			}
		}
		logger.warn("list.size():" + list.size());
		return list;
	}

	public List<Map<String, String>> parseTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.warn("parseTXT{}", fileName);

		logger.warn("start to parse file:[{}]", fileName);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> tempMap = null;

		String targetDirectory = ServletActionContext.getServletContext()
				.getRealPath("/loid");
		logger.warn("targetDirectory=" + targetDirectory);
		String targetFileName = new Date().getTime() + fileName;
		File target = new File(targetDirectory, targetFileName);
		FileUtils.copyFile(upload, target);
		logger.warn("target===lujing-===" + target.getAbsolutePath());
		BufferedReader in = new BufferedReader(new FileReader(target));
		String line = in.readLine();
		Map<String, String> tempMapTxgt = null;
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			tempMapTxgt = new HashMap<String, String>();
			logger.warn("line ==" + line);
			String[] bind = line.split(",");
			tempMapTxgt.put("device_serialnumber", bind[0]);
			tempMapTxgt.put("loid", bind[1]);
			list.add(tempMapTxgt);
			tempMapTxgt = null;
		}
		in.close();
		in = null;
		return list;
	}

	public InputStream getExportExcelStream() {
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String filePath = path + "loid/batchUPtemplate.xls";
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("SetLoidActionImpl==>setTime()" + startTime);
		// 该月第一天
		if (StringUtil.IsEmpty(startTime) && StringUtil.IsEmpty(endTime)) {
			startTime = String.valueOf(new DateTimeUtil(new DateTimeUtil()
					.getFirtDayOfMonth()).getLongTime());
			endTime = String.valueOf(new DateTimeUtil().getLongTime());
		} else {
			if (startTime.length() > 10) {
				startTime = String.valueOf(new DateTimeUtil(startTime)
						.getLongTime());
				endTime = String.valueOf(new DateTimeUtil(endTime)
						.getLongTime());
			}
		}

	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setBio(SetLoidServ bio) {
		this.bio = bio;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String getFileNameExcel() {
		return fileNameExcel;
	}

	public void setFileNameExcel(String fileNameExcel) {
		this.fileNameExcel = fileNameExcel;
	}

	public SetLoidServ getBio() {
		return bio;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
