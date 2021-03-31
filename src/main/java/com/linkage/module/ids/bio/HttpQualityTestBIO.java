package com.linkage.module.ids.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.ids.dao.HttpQualityTestDAO;

public class HttpQualityTestBIO {

	private Logger logger = LoggerFactory.getLogger(HttpQualityTestBIO.class);

	private HttpQualityTestDAO dao;
	// 回传消息
	private String msg = null;
	private String importQueryField = "username";
	private Map<String, String> cityMap = new HashMap<String, String>();

	public int getDevListCount(String fileName) {

		return dao.getTaskDevCount(fileName);
	}

	public String addTask(String taskname, String taskid, long accoid,
			String addtime, String url, String levelreport, String filename,
			String fileName, String deviceIds) {
		String fileName_ = fileName.substring(fileName.length() - 3,
				fileName.length());
		List<String> dataList = null;
		try {
			if ("txt".equals(fileName_)) {
				dataList = getImportDataByTXT(fileName);
			} else {
				dataList = getImportDataByXLS(fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return null;
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		int allDevs = dao.queryDevsByDate();
		int nowDevs = 0;
		List<Map> devList = null;
		// 根据设备序列号获取设备信息
		if (importQueryField.equals("username")) {
			devList = dao.getTaskDevListByLOID(fileName);
		} else {
			devList = dao.getTaskDevListBySN(fileName);
		}
		if(devList.size()==0){
			return "没有匹配到数据";
		}
		logger.warn("devList:{}", devList.size());
		nowDevs = devList.size();
		File f = new File(getFilePath() + fileName);
		if ((allDevs + nowDevs) > 50000) {
			f.delete();
			return "一天内最大定制总数不得超过5W，今天已定制" + allDevs + "条";
		}
		// 获取sqlList
		ArrayList<String> sqlList = sqlList(devList, taskname, taskid);
		// 批量插入设备信息
		dao.insertTaskDev(sqlList);

		int num = dao.addTask(taskname, taskid, accoid, addtime, url,
				levelreport, filename, nowDevs);
		File ff = new File(getFilePath() + fileName);
		ff.delete();
		
		return "true";

	}

	/**
	 * 查询设备（带列表）（针对导入查询）
	 * 
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param cityId
	 *            属地过滤
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public List getDeviceList(String fileName) {
		// logger.debug("getDeviceList({},{},{},{})",new
		// Object[]{areaId,queryType,cityId,fileName});
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3,
				fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try {
			if ("txt".equals(fileName_)) {
				dataList = getImportDataByTXT(fileName);
			} else {
				dataList = getImportDataByXLS(fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return null;
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}

		List list = null;
		dao.insertTmp(fileName, dataList, importQueryField);
		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} else {
			if ("username".equals(importQueryField)) {
				list = dao.queryDeviceByImportUsername(dataList, fileName);
			} else {
				list = dao.queryDeviceByImportDevicesn(dataList, fileName);
			}
		}
		if (null == list || list.size() < 1) {
			this.msg = "账号不存在或账号未绑定设备";
		}

		return list;
	}

	private List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if (null != line && "设备序列号".equals(line)) {
			this.importQueryField = "device_serialnumber";
		} else {
			this.importQueryField = "username";
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f = null;
		return list;
	}

	public String checkName(String fileName, String taskname) {
		int fCount = dao.checkFileName(fileName);
		int tCount = dao.checkTaskName(taskname);
		if (fCount > 0) {
			return "测试结果文件名已经存在！";
		}
		if (tCount > 0) {
			return "任务名已经存在！";
		}

		return "true";
	}

	public List<Map> queryTask(String name, String acc_loginname,
			String starttime, String endtime, int curPage_splitPage,
			int num_splitPage) {
		return dao.queryTask(name, acc_loginname, starttime, endtime,
				curPage_splitPage, num_splitPage);
	}

	public int queryTaskCount(String name, String acc_loginname,
			String starttime, String endtime, int curPage_splitPage,
			int num_splitPage) {
		return dao.queryTaskCount(name, acc_loginname, starttime, endtime,
				curPage_splitPage, num_splitPage);
	}

	public Map queryTaskInfo(String taskid) {
		return dao.queryTaskInfo(taskid);
	}

	public List<Map> getTaskDevList(String taskid, int curPage_splitPage,
			int num_splitPage) {
		return dao.getTaskDevList(taskid, curPage_splitPage, num_splitPage);
	}

	public int getTaskCount(String taskid, int curPage_splitPage,
			int num_splitPage) {
		return dao.getTaskDevCount(taskid, curPage_splitPage, num_splitPage);
	}

	public List<Map> queryTaskExcel(String name, String acc_loginname,
			String starttime, String endtime) {
		return dao.queryTaskExcel(name, acc_loginname, starttime, endtime);
	}

	public String countTest(String fileName) {
		// int allDevs = dao.queryDevsByDate();
		int nowDevs = getExcelRow(fileName);
		// logger.warn("HttpQualityTestBIO->allDevs={}",allDevs);
		// logger.warn("HttpQualityTestBIO->nowDevs={}",nowDevs);
		File f = new File(getFilePath() + fileName);
		// if((allDevs+nowDevs)>5){
		// f.delete();
		// return "一天内最大定制总数不得超过5W，今天已上传"+allDevs+"条";
		// }

		if (nowDevs > 2000) {
			f.delete();
			return "解析失败，文件最多只能导入2000行！";
		}
		// try {
		// //从xls中解析出设备序列号
		// List<String> list = getImportDataByXLS(fileName);
		// //批量插入设备序列号到临时表
		// // dao.insertTmpinsertTmp(fileName,dataList,importQueryField);
		// f.delete();
		// } catch (BiffException e) {
		// logger.warn("文件解析失败:{}",fileName);
		// f.delete();
		// } catch (IOException e) {
		// logger.warn("文件解析失败:{}",fileName);
		// f.delete();
		// }
		return "true";
	}

	public String delTask(String taskid) {
		dao.delTask(taskid);
		return "删除成功";
	}

	@SuppressWarnings("unused")
	private List<String> getImportDataByXLS(String fileName)
			throws IOException, BiffException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);

			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();

			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line)) {
					this.importQueryField = "device_serialnumber";
				} else {
					this.importQueryField = "username";
				}
			}

			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())) {
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}

	@SuppressWarnings("unused")
	private ArrayList<String> sqlList(List<Map> list, String name, String taskid) {
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String oui = String.valueOf(list.get(i).get("oui"));
			String username = String.valueOf(list.get(i).get("username"));
			String loid = String.valueOf(list.get(i).get("loid"));
			String device_serialnumber = String.valueOf(list.get(i).get(
					"device_serialnumber"));
			String device_id = String.valueOf(list.get(i).get("device_id"));
			String city_id = String.valueOf(list.get(i).get("city_id"));
			cityMap = CityDAO.getCityIdCityNameMap();
			String city_name = cityMap.get(city_id);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into tab_ids_task_dev(task_name,task_id,device_id,oui,device_serialnumber,loid,STATU,sg_time,city_name,user_name)");
			sql.append(" values('" + name + "','" + taskid + "','" + device_id
					+ "','" + oui + "','" + device_serialnumber + "','"
					+ loid + "',0,null,'" + city_name + "','" + username+"')");
			sqlList.add(sql.toString());
		}
		return sqlList;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 判断上传文件行数是否符合
	 * 
	 * @param fileName
	 * @return
	 */
	public int getExcelRow(String fileName) {
		String msg = "true";
		File f = new File(getFilePath() + fileName);
		int rowCount = 0;
		try {
			Workbook wwb = Workbook.getWorkbook(f);
			;
			Sheet ws = null;
			ws = wwb.getSheet(0);
			rowCount = ws.getRows() - 1;
		} catch (BiffException e) {
			logger.warn("BiffException");
		} catch (IndexOutOfBoundsException e) {
			logger.warn("IndexOutOfBoundsException");
		} catch (IOException e) {
			logger.warn("IOException");
		}

		return rowCount;

	}

	public HttpQualityTestDAO getDao() {
		return dao;
	}

	public void setDao(HttpQualityTestDAO dao) {
		this.dao = dao;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getImportQueryField() {
		return importQueryField;
	}

	public void setImportQueryField(String importQueryField) {
		this.importQueryField = importQueryField;
	}

}
