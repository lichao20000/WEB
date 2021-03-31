/**
 *
 */
package com.linkage.module.gtms.stb.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.resource.dao.StbBidChageDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.dao.GwVendorModelVersionDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.bio
 *
 */

@SuppressWarnings("rawtypes")
public class StbBidChageBIO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(StbBidChageBIO.class);

	private GwStbVendorModelVersionDAO vmvStbDao = null;

	public GwStbVendorModelVersionDAO getVmvStbDao()
	{
		return vmvStbDao;
	}


	public void setVmvStbDao(GwStbVendorModelVersionDAO vmvStbDao)
	{
		this.vmvStbDao = vmvStbDao;
	}

	private StbBidChageDAO dao = null;

	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";

	/**
	 * 模拟匹配提示
	 */

	public String getDeviceSn(String cityId, String searchTxt,
			String separator, String gwShare_queryField) {
		StringBuilder sb = new StringBuilder();
		List list = dao.getDeviceSn(cityId, searchTxt,
				gwShare_queryField);
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String key = String.valueOf(map.get("device_sn"));
			if (i == 0) {
				sb.append(key);
			} else {
				sb.append(separator).append(key);
			}
		}
		return sb.toString();
	}

	public int getTmpList() {
		return dao.getTmpList();
	}

	public int[] insertImportDataTmp(List<String> dataList, int type) {
		return dao.insertImportDataTmp(dataList, type);
	}

	public List<Map> NewDeviceQueryInfo(String start_time, String end_time,
			String gw_type, long areaId, String queryType, String queryParam,
			String queryField, String cityId, String vendorId,
			String deviceModelId,String devicetypeId, int curPage_splitPage,
			int num_splitPage,String isNewNeed) {
		logger.debug("NewDeviceQueryInfo()");
		return dao.newDeviceQueryInfo(start_time, end_time, gw_type,
				areaId, queryType, queryParam, queryField, cityId, vendorId,
				deviceModelId,devicetypeId, curPage_splitPage, num_splitPage,isNewNeed);
	}

	public List<Map> newDeviceQueryInfo4Simple(String mac, String username,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.newDeviceQueryInfo4Simple(mac, username, curPage_splitPage,
				num_splitPage);
	}

	public List<Map> NewDeviceQueryExcel(String start_time, String end_time,
			String gw_type, long areaId, String queryType, String queryParam,
			String queryField, String cityId, String vendorId,
			String deviceModelId, String devicetypeId,String isNewNeed) {
		logger.debug("NewDeviceQueryInfo()");
		return dao.NewDeviceQueryExcel(start_time, end_time, gw_type,
				areaId, queryType, queryParam, queryField, cityId, vendorId,
				deviceModelId, deviceModelId,isNewNeed);
	}

	public int countNewDeviceQueryInfo(String start_time, String end_time,
			String gw_type, long areaId, String queryType, String queryParam,
			String queryField, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, int curPage_splitPage,
			int num_splitPage,String isNewNeed) {
		logger.debug("countNewDeviceQueryInfo()");
		return dao.countNewDeviceQueryInfo(start_time, end_time,
				gw_type, areaId, queryType, queryParam, queryField, cityId,
				vendorId, deviceModelId, devicetypeId, curPage_splitPage,
				num_splitPage,isNewNeed);
	}

	public int countNewDeviceQueryInfo4Simple(String mac, String username,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.countNewDeviceQueryInfo4Simple(mac, username,
				curPage_splitPage, num_splitPage);
	}
	/*没调用 注释 2020/11/17
	public String getDeviceSql(String gw_type, String cityId, String fileName) {
		StringBuffer pSQL = new StringBuffer();
		pSQL.append("select a.device_id,a.devicetype_id,a.device_model_id,b.username ");
		pSQL.append("from tab_gw_device a,tab_tmp_accounts b,tab_seniorquery_tmp f ");
		pSQL.append("where a.device_status=1 and a.device_id=b.device_id ");
		if (null != cityId && !"00".equals(cityId)) {
			ArrayList<String> cityArray = CityDAO
					.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if ("username".equals(importQueryField)) {
			pSQL.append(" and b.username=f.username and f.filename='"
					+ fileName + "'");
		}

		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type=" + gw_type);
		}
		return pSQL.toString();
	}*/

	public List getDeviceList1(int curPage_splitPage, int num_splitPage,
			String gw_type, long areaId, String cityId, String fileName) {
		return dao.queryDeviceByImportUsername1(curPage_splitPage,
				num_splitPage, gw_type, areaId, cityId, fileName);
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
	public List getDeviceList(String gw_type, long areaId, String queryType,
			String cityId, String fileName,boolean flag) {
		logger.debug("getDeviceList({},{},{},{})", new Object[] { areaId,
				queryType, cityId, fileName });
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3,
				fileName.length());
		logger.debug("fileName_;{}", fileName_);
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try {
			if(flag){
				if ("txt".equals(fileName_)) {
					dataList = getImportDataByTXT(fileName);
				} else {
					dataList = getImportDataByXLS(fileName);
				}
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！{}", fileName, e);
			this.msg = "文件没找到！";
			return null;
		} catch (IOException e) {
			logger.warn("{}文件解析出错！{}", fileName, e);
			this.msg = "文件解析出错！";
			return null;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！{}", fileName, e);
			this.msg = "文件解析出错！";
			return null;
		}

		List list = null;
		if(flag){
		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} else {
			dao.insertTmp(fileName, dataList, importQueryField);
			if ("username".equals(importQueryField)) {
				list = dao.queryDeviceByImportUsername(gw_type, areaId,
						cityId, dataList, fileName);
			}
		}
		}else{
			list = dao.queryDeviceByImportUsername(gw_type, areaId,
					cityId, dataList, fileName);
		}

		if (null == list || list.size() < 1) {
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	// 数据
	public List getnumber(String gw_type, long areaId, String queryType,
			String cityId, String fileName) {
		logger.debug("getDeviceList({},{},{},{})", new Object[] { areaId,
				queryType, cityId, fileName });
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3,
				fileName.length());
		logger.debug("fileName_;{}", fileName_);
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

		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} else {
			return dataList;
		}

	}

	/**
	 * 查询设备（带列表）（针对导入查询,上传文件有两列参数）
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
	public List getDeviceListForDoubleParam(String gw_type, long areaId,
			String queryType, String cityId, String fileName) {
		logger.debug("getDeviceList({},{},{},{})", new Object[] { areaId,
				queryType, cityId, fileName });
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3,
				fileName.length());
		logger.debug("fileName_;{}", fileName_);
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<List> listAll = null;
		try {
			if ("txt".equals(fileName_)) {
				listAll = getImportDataByTXTForDoubleParam(fileName);
			} else {
				listAll = getImportDataByXLSForDoubleParam(fileName);
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
		List<String> dataList = listAll.get(0);
		List<String> paramList = listAll.get(1);
		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} else {
			dao.insertTmpForDoubleParam(fileName, dataList, paramList,
					importQueryField);
			if ("username".equals(importQueryField)) {
				list = dao.queryDeviceByImportUsernameForDoubleParam(
						gw_type, areaId, cityId, dataList, fileName);
			} else {
				list = dao.queryDeviceByImportDevicesnForDoubleParam(
						gw_type, areaId, cityId, dataList, fileName);
			}
		}

		if (null == list || list.size() < 1) {
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	/**
	 * 查询设备（带列表）（针对导入查询）(无线专线开通)
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
	public List getDeviceList4Wireless(String gw_type, long areaId,
			String queryType, String cityId, String fileName,
			String currencyType) {
		logger.debug("getDeviceList({},{},{},{})", new Object[] { areaId,
				queryType, cityId, fileName });
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
				if ("1".equals(currencyType)) {
					dataList = getImportDataByXLS4CurrencyWireless(fileName);
				} else if ("0".equals(currencyType)) {
					dataList = getImportDataByXLS4Wireless(fileName);
				}

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
		// 无线通用页面校验上传文件参数
		if ("1".equals(currencyType)) {
			// 判断上传文件每行记录是否有空值
			ArrayList<Integer> listInd = new ArrayList<Integer>();

			for (int i = 0; i < dataList.size(); i++) {
				String msgCur = dataList.get(i);
				String msgArr[] = msgCur.split(",");
				for (int j = 0; j < msgArr.length; j++) {
					String s = msgArr[j].trim();
					if (s == null || s.length() <= 0) {
						listInd.add(i);
						break;
					}
				}
			}
			if (null != listInd && listInd.size() != 0) {
				for (int i = listInd.size() - 1; i >= 0; i--) {
					int m = Integer.valueOf(listInd.get(i));
					dataList.remove(m);
					listInd.clear();
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				String msgCur = dataList.get(i);
			}
			// 判断上传文件每行记录长度
			ArrayList<Integer> listIndex = new ArrayList<Integer>();
			for (int i = 0; i < dataList.size(); i++) {
				String msgCur = dataList.get(i);
				String msgArr[] = msgCur.split(",");
				int dataNum = msgArr.length;
				if (dataNum < 6) {
					listIndex.add(i);
				}
			}

			if (null != listIndex && listIndex.size() != 0) {
				for (int i = listIndex.size() - 1; i >= 0; i--) {
					int m = Integer.valueOf(listIndex.get(i));
					dataList.remove(m);
					listIndex.clear();
				}
			}
		}

		List list = null;

		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} else {
			dao.insertTmp4Wireless(fileName, dataList,
					importQueryField, currencyType);
			if ("username".equals(importQueryField)) {
				list = dao.queryDeviceByImportUsername(gw_type, areaId,
						cityId, dataList, fileName);
			} else {
				list = dao.queryDeviceByImportDevicesn(gw_type, areaId,
						cityId, dataList, fileName);
			}
		}
		if (null == list || list.size() < 1) {
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
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
	public List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */

		this.importQueryField = "username";

		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!StringUtil.IsEmpty(line) && !list.contains(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 解析文件（xls格式）
	 *
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 *
	 */
	public List<String> getImportDataByXLS(String fileName)
			throws BiffException, IOException {
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
				} else if (null != line && "宽带账号".equals(line)) {
					this.importQueryField = "kdusername";
				} else {
					this.importQueryField = "username";
				}
			}

			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (!StringUtil.IsEmpty(temp) && !list.contains(temp.trim())) {
					list.add(temp.trim());
				}
			}
		}
		f.delete();
		f = null;
		return list;
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
	public List<List> getImportDataByTXTForDoubleParam(String fileName)
			throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<List> listAll = new ArrayList<List>();
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		String[] split = line.split(",");
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if (null != split[0] && split[0].contains("设备序列号")) {
			this.importQueryField = "device_serialnumber";
		} else {
			this.importQueryField = "username";
		}
		List<String> nodeList = null;
		if (null != split[1] && split[1].contains("配置参数节点值")) {
			nodeList = new ArrayList<String>();
		} else {
			return null;
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			String[] splitMsg = line.split(",");
			if (!"".equals(splitMsg[0].trim())
					&& !"".equals(splitMsg[1].trim()) && null != splitMsg[0]
					&& null != splitMsg[1]) {
				list.add(splitMsg[0].trim());
				nodeList.add(splitMsg[1].trim());
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f.delete();
		f = null;
		listAll.add(list);
		listAll.add(nodeList);
		return listAll;
	}

	/**
	 * 解析文件（xls格式）
	 *
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 *
	 */
	public List<List> getImportDataByXLSForDoubleParam(String fileName)
			throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<List> listAll = new ArrayList<List>();
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		List<String> nodeList = null;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);

			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();

			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				String paramTitle = ws.getCell(1, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line)) {
					this.importQueryField = "device_serialnumber";
				} else {
					this.importQueryField = "username";
				}
				if (null != paramTitle && paramTitle.contains("配置参数节点值")) {
					nodeList = new ArrayList<String>();
				} else {
					return null;
				}
			}

			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				String param = ws.getCell(1, i).getContents();
				if (null != temp && !"".equals(temp.trim()) && null != param
						&& !"".equals(param.trim())) {
					list.add(ws.getCell(0, i).getContents().trim());
					nodeList.add(ws.getCell(1, i).getContents().trim());
				}
			}
		}
		f.delete();
		f = null;
		listAll.add(list);
		listAll.add(nodeList);
		return listAll;
	}

	/**
	 * 解析文件（xls格式）(无线专线开通)
	 *
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 *
	 */
	public List<String> getImportDataByXLS4Wireless(String fileName)
			throws BiffException, IOException {
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
				if (null != line && line.contains("设备序列号")) {
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
						temp += "," + ws.getCell(1, i).getContents().trim()
								+ "," + ws.getCell(2, i).getContents().trim()
								+ "," + ws.getCell(3, i).getContents().trim()
								+ "," + ws.getCell(4, i).getContents().trim();
						list.add(temp);
					}
				}
				temp = null;
			}
		}
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 解析文件（xls格式）(通用无线开通)
	 *
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 *
	 */
	public List<String> getImportDataByXLS4CurrencyWireless(String fileName)
			throws BiffException, IOException {
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
				if (null != line && line.contains("设备序列号")) {
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
						temp += "," + ws.getCell(1, i).getContents().trim()
								+ "," + ws.getCell(2, i).getContents().trim()
								+ "," + ws.getCell(3, i).getContents().trim()
								+ "," + ws.getCell(4, i).getContents().trim()
								+ "," + ws.getCell(5, i).getContents().trim();
						list.add(temp);
					}
				}
				temp = null;
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
	 * 针对高级查询，结果集比较大的话，二次查询
	 *
	 * @@param areaId 登录人所属域
	 * @param param
	 *            查询条件集 例如x|xx|xxx|xxx|xxxx
	 * @return
	 */
	public List getDeviceList(String gw_type, long areaId, String param) {
		logger.debug("GwDeviceQueryBIO=>getDeviceList({})", param);
		if (null == param) {
			return null;
		}
		String[] _param = param.split("\\|");

		String queryType = _param[0].trim();
		String queryField = _param[1].trim();
		String cityId = _param[2].trim();
		String queryParam = _param[3].trim();
		String onlineStatus = _param[4].trim();
		String vendorId = _param[5].trim();
		String deviceModelId = _param[6].trim();
		String devicetypeId = _param[7].trim();
		String bindType = _param[8].trim();
		String deviceSerialnumber = _param[9].trim();

		return getDeviceList(gw_type, areaId, queryType, queryParam,
				queryField, cityId, onlineStatus, vendorId, deviceModelId,
				devicetypeId, bindType, deviceSerialnumber);
	}

	/**
	 * 查询设备（带列表）（针对数据量小于一定量时）
	 *
	 * @param curPage_splitPage
	 *            分页选项当前页数
	 * @param num_splitPage
	 *            分页选项每页显示数
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param queryParam
	 *            简单查询参数
	 * @param queryField
	 *            简单查询条件字段
	 * @param cityId
	 *            高级查询属地过滤
	 * @param onlineStatus
	 *            高级查询是否在线过滤
	 * @param vendorId
	 *            高级查询厂商过滤
	 * @param deviceModelId
	 *            高级查询设备型号过滤
	 * @param devicetypeId
	 *            高级查询软件版本过滤
	 * @param bindType
	 *            高级查询是否绑定过滤
	 * @param deviceSerialnumber
	 *            高级查询设备序列号模糊匹配
	 * @return
	 */
	public List getDeviceList(String gw_type, long areaId, String queryType,
			String queryParam, String queryField, String cityId,
			String onlineStatus, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber) {
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		List list = null;
		if ("1".equals(queryType)) {
			if ("deviceSn".equals(queryField)) {
				list = dao.queryDevice(gw_type, areaId, cityId, null,
						null, null, null, queryParam, null);
			} else if ("deviceIp".equals(queryField)) {
				list = dao.queryDevice(gw_type, areaId, cityId, null,
						null, null, null, null, queryParam);
			} else if ("username".equals(queryField)) {
				list = dao.queryDevice(gw_type, areaId, cityId,
						queryParam);
			} else if ("voipPhoneNum".equals(queryField)) {
				list = dao.queryDevice(gw_type, cityId, queryParam); // 　宁夏，按照VOIP电话号码查询设备
																				// add
																				// by
																				// zhangchy
																				// 2012-02-23
																				// 不分页
			} else if ("kdname".equals(queryField)) {
				list = dao.queryDeviceByKdname(gw_type, cityId,
						queryParam); // 增加宽带账号查询 add by chenjie 2012-4-18 不分页
			} else {
				list = dao.queryDeviceByFieldOr(gw_type, areaId,
						queryParam, cityId);
			}
		} else {
			if (null != onlineStatus && !"".equals(onlineStatus)
					&& !"-1".equals(onlineStatus)) {
				list = dao.queryDeviceByLikeStatus(gw_type, areaId,
						cityId, vendorId, deviceModelId, devicetypeId,
						bindType, deviceSerialnumber, null, onlineStatus);
			} else {
				list = dao.queryDevice(gw_type, areaId, cityId,
						vendorId, deviceModelId, devicetypeId, bindType,
						deviceSerialnumber, null);
			}
		}
		if (null == list || list.size() < 1) {
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	/**
	 * 查询是否有isawifi
	 *
	 * @param gw_type
	 * @param areaId
	 * @param queryType
	 * @param queryParam
	 * @param queryField
	 * @param cityId
	 * @param onlineStatus
	 * @param vendorId
	 * @param deviceModelId
	 * @param devicetypeId
	 * @param bindType
	 * @param deviceSerialnumber
	 * @return
	 */
	public List<Map> queryisawifi(String gw_type, long areaId,
			String queryType, String queryParam, String queryField,
			String cityId, String onlineStatus, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber) {
		List list = null;
		if ("1".equals(queryType)) {
			if ("deviceSn".equals(queryField)) {
				list = dao.isawifiDevice(gw_type, areaId, cityId, null,
						null, null, null, queryParam, null);
			} else if ("deviceIp".equals(queryField)) {
				list = dao.isawifiDevice(gw_type, areaId, cityId, null,
						null, null, null, null, queryParam);
			} else if ("username".equals(queryField)) {
				list = dao.isawifiDevice(gw_type, areaId, cityId,
						queryParam);
			} else if ("voipPhoneNum".equals(queryField)) {
				list = dao.isawifiDevice(gw_type, cityId, queryParam); // 　宁夏，按照VOIP电话号码查询设备
																				// add
																				// by
																				// zhangchy
																				// 2012-02-23
																				// 不分页
			} else if ("kdname".equals(queryField)) {
				list = dao.isawifiDeviceByKdname(gw_type, cityId,
						queryParam); // 增加宽带账号查询 add by chenjie 2012-4-18 不分页
			} else {
				list = dao.isawifiDeviceByFieldOr(gw_type, areaId,
						queryParam, cityId);
			}
		}
		/*
		 * else { list.add("1"); }
		 */
		return list;
	}

	public int queryaccount_number(String gw_type, long areaId,
			String queryType, String queryParam, String queryField,
			String cityId, String onlineStatus, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber) {
		int count = 0;
		if ("1".equals(queryType)) {
			if ("deviceSn".equals(queryField)) {
				count = dao.queryaccount_number(gw_type, areaId,
						cityId, null, null, null, null, queryParam, null);
			} else if ("deviceIp".equals(queryField)) {
				count = dao.queryaccount_number(gw_type, areaId,
						cityId, null, null, null, null, null, queryParam);
			} else if ("username".equals(queryField)) {
				count = dao.account_number(gw_type, areaId, cityId,
						queryParam);
			} else if ("voipPhoneNum".equals(queryField)) {
				count = dao.number(gw_type, cityId, queryParam); // 　宁夏，按照VOIP电话号码查询设备
																			// add
																			// by
																			// zhangchy
																			// 2012-02-23
																			// 不分页
			} else if ("kdname".equals(queryField)) {
				count = dao.Kd(gw_type, cityId, queryParam); // 增加宽带账号查询
																		// add
																		// by
																		// chenjie
																		// 2012-4-18
																		// 不分页
			} else {
				count = dao.ByFieldOr(gw_type, areaId, queryParam,
						cityId);
			}
		}
		return count;
	}

	/**
	 * 自定义SQL查询设备列表
	 *
	 * @param gwShare_matchSQL
	 * @return
	 */
	public List<String> getDeviceListBySQL(String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryBIO=>getDeviceListBySQL");
		List<String> list = new ArrayList<String>();
		try {
			list = dao.queryDeviceBySQL(gwShare_matchSQL);
			if (null == list || list.size() < 1) {
				this.msg = "未查询到相关记录";
			}
		} catch (Exception e) {
			this.msg = "未查询到相关记录";
			e.printStackTrace();
			logger.error("getDeviceListBySQL-->Exception:{}"
					+ new Object[] { e.getMessage() });
			return list;
		}

		return list;
	}

	/**
	 * 高级查询SQL
	 *
	 * @param curPage_splitPage
	 *            分页选项当前页数
	 * @param num_splitPage
	 *            分页选项每页显示数
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param queryParam
	 *            简单查询参数
	 * @param queryField
	 *            简单查询条件字段
	 * @param cityId
	 *            高级查询属地过滤
	 * @param onlineStatus
	 *            高级查询是否在线过滤
	 * @param vendorId
	 *            高级查询厂商过滤
	 * @param deviceModelId
	 *            高级查询设备型号过滤
	 * @param devicetypeId
	 *            高级查询软件版本过滤
	 * @param bindType
	 *            高级查询是否绑定过滤
	 * @param deviceSerialnumber
	 *            高级查询设备序列号模糊匹配
	 * @return
	 */
	public String getDeviceListSQL(String gw_type, long areaId,
			String queryType, String queryParam, String queryField,
			String cityId, String onlineStatus, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber) {
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		String sqlStr = null;
		if (!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)) {
			sqlStr = dao.queryDeviceByLikeStatusSQL(gw_type, areaId,
					cityId, vendorId, deviceModelId, devicetypeId, bindType,
					deviceSerialnumber, null, onlineStatus);
		} else {
			sqlStr = dao.queryDeviceSQL(gw_type, areaId, cityId,
					vendorId, deviceModelId, devicetypeId, bindType,
					deviceSerialnumber, null);
		}
		return sqlStr;
	}

	public String getDeviceListSQL2(String gw_type, long areaId,
			String queryType, String queryParam, String queryField,
			String cityId, String onlineStatus, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber) {
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		String sqlStr = null;
		if (!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)) {
			sqlStr = dao.queryDeviceByLikeStatusSQL2(gw_type, areaId,
					cityId, vendorId, deviceModelId, devicetypeId, bindType,
					deviceSerialnumber, onlineStatus);
		} else {
			sqlStr = dao.queryDeviceSQL2(gw_type, areaId, cityId,
					vendorId, deviceModelId, devicetypeId, bindType,
					deviceSerialnumber);
		}
		return sqlStr;
	}

	/**
	 * 查询设备（带列表）
	 *
	 * @param curPage_splitPage
	 *            分页选项当前页数
	 * @param num_splitPage
	 *            分页选项每页显示数
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param queryParam
	 *            简单查询参数
	 * @param queryField
	 *            简单查询条件字段
	 * @param cityId
	 *            高级查询属地过滤
	 * @param onlineStatus
	 *            高级查询是否在线过滤
	 * @param vendorId
	 *            高级查询厂商过滤
	 * @param deviceModelId
	 *            高级查询设备型号过滤
	 * @param devicetypeId
	 *            高级查询软件版本过滤
	 * @param bindType
	 *            高级查询是否绑定过滤
	 * @param deviceSerialnumber
	 *            高级查询设备序列号模糊匹配
	 * @return
	 */
	public List getDeviceList(String gw_type, int curPage_splitPage,
			int num_splitPage, long areaId, String queryType,
			String queryParam, String queryField, String cityId,
			String onlineStatus, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber) {
		logger.debug("GwDeviceQueryBIO=>getDeviceList");
		List list = null;
		if ("1".equals(queryType)) {
			if ("deviceSn".equals(queryField)) {
				list = dao.queryDevice(gw_type, curPage_splitPage,
						num_splitPage, areaId, cityId, null, null, null, null,
						queryParam, null);
			} else if ("deviceIp".equals(queryField)) {
				list = dao.queryDevice(gw_type, curPage_splitPage,
						num_splitPage, areaId, cityId, null, null, null, null,
						null, queryParam);
			} else if ("username".equals(queryField)) {
				list = dao.queryDevice(gw_type, curPage_splitPage,
						num_splitPage, areaId, cityId, queryParam);
			} else if ("voipPhoneNum".equals(queryField)) {
				list = dao.queryDevice(gw_type, curPage_splitPage,
						num_splitPage, cityId, queryParam); // 宁夏 根据VOIP电话号码查询设备
															// 分页 add by
															// zhangchy
															// 2012-02-23
			} else if ("kdname".equals(queryField)) {
				list = dao.queryDeviceByKdname(gw_type, cityId,
						queryParam, curPage_splitPage, num_splitPage); // 增加宽带账号查询
																		// add
																		// by
																		// chenjie
																		// 2012-4-18
																		// 分页
			} else {
				list = dao.queryDeviceByFieldOr(gw_type,
						curPage_splitPage, num_splitPage, areaId, queryParam,
						cityId);
			}
		} else {
			if (null != onlineStatus && !"".equals(onlineStatus)
					&& !"-1".equals(onlineStatus)) {
				list = dao.queryDeviceByLikeStatus(gw_type,
						curPage_splitPage, num_splitPage, areaId, cityId,
						vendorId, deviceModelId, devicetypeId, bindType,
						deviceSerialnumber, null, onlineStatus);
			} else {
				list = dao.queryDevice(gw_type, curPage_splitPage,
						num_splitPage, areaId, cityId, vendorId, deviceModelId,
						devicetypeId, bindType, deviceSerialnumber, null);
			}
		}
		if (null == list || list.size() < 1) {
			this.msg = "账号不存在或账号未绑定设备";
		}
		return list;
	}

	/**
	 * 自定义SQL查询设备列表
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param gwShare_matchSQL
	 * @return
	 */
	public List<String> getDeviceListBySQL(int curPage_splitPage,
			int num_splitPage, String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryBIO=>getDeviceListBySQL");
		List<String> list = new ArrayList<String>();
		try {
			list = dao.queryDeviceBySQL(curPage_splitPage,
					num_splitPage, gwShare_matchSQL);
			if (null == list || list.size() < 1) {
				this.msg = "未查询到相关记录";
			}
		} catch (Exception e) {
			this.msg = "未查询到相关记录";
			e.printStackTrace();
			logger.error("getDeviceListBySQL-->Exception:{}"
					+ new Object[] { e.getMessage() });
			return list;
		}
		return list;
	}

	/**
	 * 查询设备（带列表）
	 *
	 * @param areaId
	 *            登录人的areaId
	 * @param queryType
	 *            查询类型(1:简单查询；2:高级查询)
	 * @param queryParam
	 *            简单查询参数
	 * @param queryField
	 *            简单查询条件字段
	 * @param cityId
	 *            高级查询属地过滤
	 * @param onlineStatus
	 *            高级查询是否在线过滤
	 * @param vendorId
	 *            高级查询厂商过滤
	 * @param deviceModelId
	 *            高级查询设备型号过滤
	 * @param devicetypeId
	 *            高级查询软件版本过滤
	 * @param bindType
	 *            高级查询是否绑定过滤
	 * @param deviceSerialnumber
	 *            高级查询设备序列号模糊匹配
	 * @return
	 */
	public int getDeviceListCount(String gw_type, long areaId,
			String queryType, String queryParam, String queryField,
			String cityId, String onlineStatus, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber) {
		logger.debug("GwDeviceQueryBIO=>getDeviceListCount");
		int count = 0;
		if ("1".equals(queryType)) {
			if ("deviceSn".equals(queryField)) {
				count = dao.queryDeviceCount(gw_type, areaId, cityId,
						null, null, null, null, queryParam, null);
			} else if ("deviceIp".equals(queryField)) {
				count = dao.queryDeviceCount(gw_type, areaId, cityId,
						null, null, null, null, null, queryParam);
			} else if ("username".equals(queryField)) {
				count = dao.queryDeviceCount(gw_type, areaId, cityId,
						queryParam);
			} else if ("voipPhoneNum".equals(queryField)) {
				count = dao.queryDeviceCount(gw_type, cityId,
						queryParam); // 宁夏 按VOIP电话号码查询设备 add by zhangchy
										// 2012-02-23 统计设备
			} else if ("kdname".equals(queryField)) {
				count = dao.queryDeviceCountByKdname(gw_type, cityId,
						queryParam); // 增加宽带账号查询 add by chenjie 2012-4-18
			} else {
				count = dao.queryDeviceByFieldOrCount(gw_type, areaId,
						queryParam, cityId);
			}
		} else {
			if (null != onlineStatus && !"".equals(onlineStatus)
					&& !"-1".equals(onlineStatus)) {
				count = dao.queryDeviceByLikeStatusCount(gw_type,
						areaId, cityId, vendorId, deviceModelId, devicetypeId,
						bindType, deviceSerialnumber, null, onlineStatus);
			} else {
				count = dao.queryDeviceCount(gw_type, areaId, cityId,
						vendorId, deviceModelId, devicetypeId, bindType,
						deviceSerialnumber, null);
			}
		}
		return count;
	}

	/**
	 * 校验自定义SQL
	 *
	 * @param gwShare_matchSQL
	 * @return
	 */
	public boolean checkMatchSQL(String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryBIO=>checkMatchSQL");
		if (StringUtil.IsEmpty(gwShare_matchSQL)) {
			this.msg = "自定义SQL不合法";
			return false;
		} else if (!gwShare_matchSQL.contains("from")
				|| !gwShare_matchSQL.contains("select")
				|| !gwShare_matchSQL.contains("where")) {
			this.msg = "自定义SQL没有select或from或where等关键字";
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 根据自定义的SQL查询设备
	 *
	 * @param gwShare_matchSQL
	 * @return
	 */
	public int getDeviceListCountBySQL(String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryBIO=>getDeviceListCountBySQL");
		int count = 0;

		try {
			count = dao.queryDeviceCountBySQL(gwShare_matchSQL);
		} catch (Exception e) {
			logger.error("getDeviceListCountBySQL-->Exception:{}"
					+ new Object[] { e.getMessage() });
			return count;
		}

		return count;
	}

	/**
	 * 查询属地
	 *
	 * @return
	 */
	public String getCity(String cityId) {
		logger.debug("GwDeviceQueryBIO=>getCity(cityId:{})", cityId);
		List list = CityDAO.getNextCityListByCityPid(cityId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}

	/**
	 * 查询属地
	 *
	 * @return
	 */
	public String getCityCore(String cityId) {
		logger.debug("GwDeviceQueryBIO=>getCity(cityId:{})", cityId);
		List list = CityDAO.getNextCityListByCityPidCore(cityId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备厂商
	 *
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryBIO=>getVendor()");
		List list = vmvStbDao.getVendor();
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}

	/**
	 * 查询设备型号
	 *
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId) {
		logger.debug("GwDeviceQueryBIO=>getDeviceModel(vendorId:{})", vendorId);
		List list = vmvStbDao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	public String getDevicetype(String deviceModelId, String isBatch) {
		logger.debug("GwDeviceQueryBIO=>getDevicetype(deviceModelId:{})",
				deviceModelId);
		List list = vmvStbDao.getVersionList1(deviceModelId, isBatch);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			if ("1".equals(isBatch)) {
				bf.append(map.get("hardwareversion") + "("
						+ map.get("softwareversion") + ")");
			} else {
				bf.append(map.get("hardwareversion") + "("
						+ map.get("softwareversion") + ")");
			}
		}
		return bf.toString();
	}

	/**
	 * 查询设备版本
	 *
	 * @param deviceModelId
	 * @return
	 */
	public String getDevicetype(String deviceModelId) {
		logger.debug("GwDeviceQueryBIO=>getDevicetype(deviceModelId:{})",
				deviceModelId);
		List list = vmvStbDao.getVersionList1(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("hardwareversion"));
			bf.append("$");
			bf.append(map.get("hardwareversion"));
		}
		return bf.toString();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public StbBidChageDAO getDao()
	{
		return dao;
	}

	public void setDao(StbBidChageDAO dao)
	{
		this.dao = dao;
	}
}
