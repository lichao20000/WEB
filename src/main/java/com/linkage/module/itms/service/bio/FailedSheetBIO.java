package com.linkage.module.itms.service.bio;

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

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.itms.service.dao.FailedSheetOptDao;

public class FailedSheetBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(FailedSheetBIO.class);

	// 回传消息
	private String msg = "";
	// 补录异常工单
	private List<String> reSendFailedSheet = new ArrayList<String>();
	private FailedSheetOptDao dao;
	private int maxPage_splitPage;
	public List<Map<String, String>> queryFailedSheet(String reciveDateStart,
			String reciveDateEnd, String cityId, String username,
			String sheetType, String resultSheet, int curPage_splitPage,
			int num_splitPage) {
		maxPage_splitPage = dao.countFailedSheet(reciveDateStart,
				reciveDateEnd, curPage_splitPage, num_splitPage);
		return dao.queryFailedSheet(reciveDateStart, reciveDateEnd,
				curPage_splitPage, num_splitPage);
	}

	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	/*@Test
	public void test() {
		List<String> dataList1 = getFaileSheetInList("F:/test.txt");
		String s = "";
		for (String s1 : dataList1) {
			s += s1 + "|||";
		}
		System.out.println(s);
	}*/

	/**
	 * 解析文件获取工单列表
	 * 
	 * @param fileName
	 * @param divStr
	 * @return
	 */
	public List<String> getFaileSheetInList(String fileName) {
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
		return dataList;
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
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException, IOException {
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				String strs[] = line.split(":");
				for (String str : strs) {
					if (str.trim().matches(
							"([A-Za-z0-9@#%&\\.\\*]*\\|\\|\\|){1,}[A-Za-z0-9@#%&\\.\\*]*")) {
						logger.warn("str:{}", str);
						list.add(str.trim());
					}
				}
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
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);

			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(temp.trim())) {
						String strs[] = temp.split(":");
						for (String str : strs) {
							if (str.trim()
									.matches(
											"([A-Za-z0-9@#%&\\.\\*]*\\|\\|\\|){1,}[A-Za-z0-9@#%&\\.\\*]*")) {
								//logger.warn("str:", str);
								list.add(str);
							}
						}
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

	public FailedSheetOptDao getDao() {
		return dao;
	}

	public void setDao(FailedSheetOptDao dao) {
		this.dao = dao;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setReSendFailedSheet(List<String> reSendFailedSheet) {
		this.reSendFailedSheet = reSendFailedSheet;
	}

	public String getMsg() {
		return this.msg;
	}

	/**
	 * 挨个重新发送工单
	 * resArr[0]:总数  resArr[1]:超时数  resArr[2]:成功数  resArr[3]:失败数
	 * @param sheetList
	 * @return
	 */
	public int[] reSendSheet(List<String> sheetList) {
		int[] resArr = {0, 0, 0, 0};
		if (null != sheetList) {
			for (String sheet : sheetList) {
				String res = SocketUtil.sendStrMesg(Global.G_ITMS_Sheet_Server,
						Global.G_ITMS_Sheet_Port, sheet + "\n");
				if (null == res) {
					resArr[1]++;
				} else if (!res.startsWith("0|||")) {
					resArr[2]++;
				}else{
					resArr[3]++;
				}
			}
			logger.warn("已发送:");
			resArr[0] = sheetList.size();
		}
		return resArr;
	}

	public List<String> getReSendFailedSheet() {
		return reSendFailedSheet;
	}
}
