
package com.linkage.module.gtms.report.serv;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.report.dao.GBBroadBandMatchDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * JXDX-REQ-ITMS-20190227-WWF-001(ITMS+家庭网关页面匹配终端百兆千兆信息需求)-批注
 *
 */
public class GBBroadBandMatchBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(GBBroadBandMatchBIO.class);
	private GBBroadBandMatchDAO dao;
	

	/** 获取设备信息 */
	public List<Map> devInfo(String gwShare_fileName) throws FileNotFoundException, IOException, BiffException {
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length() - 3, gwShare_fileName.length());
		List<String> fileList = new ArrayList<String>();
		List<Map> dataList = new ArrayList<Map>();
		List<Map> reportList = new ArrayList<Map>();
		Map map = null;
		if ("txt".equals(fileName_)) {
			fileList = getImportDataByTXT(gwShare_fileName);
		} else {
			fileList = getImportDataByXLS(gwShare_fileName);
		}
		for (int i = 0; i < fileList.size(); i++) {
			map = new HashMap();
			String[] oui_sn = fileList.get(i).split("-");
			String oui = "";
			String sn = "";
			if(oui_sn.length>1) {
				oui = oui_sn[0];
				sn = oui_sn[1];
			}
			 //获取设备信息
			dataList = dao.devInfo(oui,sn);
			//如果未查到设备信息，oui-串码保留，其余列为空
			map.put("oui-sn", fileList.get(i));//第一列是 oui-串码
			map.putAll(dataList.get(0));//其余列
			reportList.add(map);
			dataList = null;
			map = null;
		}
		return reportList;
	}
	
	
	/**
	 * 获取文件行数
	 */
	public int getCount(String gwShare_fileName) throws FileNotFoundException, IOException, BiffException {
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length() - 3, gwShare_fileName.length());
		List<String> list = new ArrayList<String>();
		if ("txt".equals(fileName_)) {
			list = getImportDataByTXT(gwShare_fileName);
		} else {
			list = getImportDataByXLS(gwShare_fileName);
		}
		return list.size();
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
	public List<String> getImportDataByTXT(String gwShare_fileName) throws FileNotFoundException, IOException{
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + gwShare_fileName));
		String line = in.readLine();
		
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
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
	 */
	public List<String> getImportDataByXLS(String gwShare_fileName) throws BiffException, IOException {
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + gwShare_fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);
			int rowCount = ws.getRows();
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp.trim())) {
					list.add(temp.trim());
				}
			}
		}
		return list;
	}
	
	
	/** 下载模板*/
	public List<Map> downloadTemplate() {
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("oui-sn", "oui-xxxxxxxx");
		map.put("oui-sn", "oui-xxxxxxxx");
		list.add(map);
		return list;
	}
	
	
	/** 获取文件名yyyyMMddHHmm */
	public String getFileName(String gwShare_fileName) {
		String file_name = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		if(gwShare_fileName!= null || gwShare_fileName!= "") {
			long time = Long.parseLong(gwShare_fileName.substring(0, 13));
			Date date = new Date(time);
			file_name = sdf.format(date);
		}
		return file_name;
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
	

	public GBBroadBandMatchDAO getDao() {
		return dao;
	}

	public void setDao(GBBroadBandMatchDAO dao) {
		this.dao = dao;
	}
}
