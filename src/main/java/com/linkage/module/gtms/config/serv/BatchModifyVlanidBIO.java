package com.linkage.module.gtms.config.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.dao.BatchModifyVlanIdDAO;
import com.linkage.module.gtms.config.dao.StackRefreshToolsDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
//import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class BatchModifyVlanidBIO {
	
	private BatchModifyVlanIdDAO dao;
	private static Logger logger = LoggerFactory.getLogger(BatchModifyVlanidBIO.class);
	// 回传消息
	private String msg = null;
	
	public String doConfig4JX(UserRes curUser,String filePath,String wanBus,String iptvBus,String voipBus) {
		logger.warn("BatchModifyVlanidBIO.doConfig4JX()");
		
		long time = new DateTimeUtil().getLongTime(); // 入表时间，同时为任务id
		int result = dao.doConfig4JX(curUser.getUser().getId(), curUser.getCityId(), time, filePath,wanBus,iptvBus,voipBus);
		if(result>0){
			logger.warn("更新表成功");
			msg = "1";
		}
		return msg;
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
	public int getImportDataByTXT4JX(String fileName)
			throws FileNotFoundException, IOException {
		logger.warn("getImportDataByTXT4JX:{}", fileName);
		int count = 0;
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				count++;
			}
		}
		in.close();
		in = null;
		
		return count;
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
	 */
	public int getImportDataByXLS4JX(String fileName) throws BiffException, IOException {
		logger.debug("getImportDataByXLS4JX{}", fileName);
		int count = 0;
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);
			int rowCount = ws.getRows();
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())) {
						count++;
					}
				}
			}
		}
		return count;
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
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}
	/**
	 * 获取今天总数
	 */
	public long getTodayCount() { 
		return dao.getTodayCount();		
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BatchModifyVlanIdDAO getDao()
	{
		return dao;
	}

	public void setDao(BatchModifyVlanIdDAO dao)
	{
		this.dao = dao;
	}
}
