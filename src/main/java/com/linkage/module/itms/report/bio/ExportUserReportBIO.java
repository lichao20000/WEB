
package com.linkage.module.itms.report.bio;

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

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.itms.report.dao.ExportUserReportDAO;

/**
 * @author hanzezheng (Ailk No.)
 * @version 1.0
 * @since 2015-1-8
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("rawtypes")
public class ExportUserReportBIO
{

	// 查询条件
	private String importQueryField = "";
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ExportUserReportBIO.class);
	// 回传消息
	private String msg = null;
	private ExportUserReportDAO exportUserReportDAO;

	public List getUserList(String fileName,int curPage_splitPage, int num_splitPage)
	{
		if (fileName.length() < 4)
		{
			logger.warn("上传的文件名不正确！", fileName);
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_))
		{
			logger.warn("{}上传的文件格式不正确！",fileName);
			return null;
		}
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",fileName);
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",fileName);
			return null;
		}
		catch(Exception e){
			logger.warn("{}文件解析出错！",fileName);
			return null;
		}
		List<Map> list=null;
		int count=exportUserReportDAO.getTemporaryNameEx(fileName);
		if(count==0){
			exportUserReportDAO.insertTmp(fileName, dataList,importQueryField);
		}
		if(dataList.size()<1){
			Log.warn("文件未解析到合法数据！");
			return null;
		}else{
			if("loid".equals(importQueryField)){
				list = exportUserReportDAO.queryUserByImportLOID(dataList, fileName, curPage_splitPage,  num_splitPage);
			}else if("net_account".equals(importQueryField)){
				list = exportUserReportDAO.queryUserByImportNet_account(dataList, fileName, curPage_splitPage,  num_splitPage);
			}else if("itv_account".equals(importQueryField)){
				list = exportUserReportDAO.queryUserByImportItv_account(dataList, fileName, curPage_splitPage,  num_splitPage);
			}else if("voip_account".equals(importQueryField)){
				list = exportUserReportDAO.queryUserByImportVoip_account(dataList, fileName, curPage_splitPage, num_splitPage);
			}
		}
		if(null==list || list.size()<1){
			Log.warn("未查询到相关记录");
		}
		return list;
	}

	public int getUserCount(String fileName,
			int curPage_splitPage, int num_splitPage){
		int count=0;
		if("loid".equals(importQueryField)){
			count=exportUserReportDAO.queryUserByLoidCount(fileName, curPage_splitPage, num_splitPage);
		}else if("net_account".equals(importQueryField)){
			count=exportUserReportDAO.queryUserByNetCount(fileName, curPage_splitPage, num_splitPage);
		}else if("itv_account".equals(importQueryField)){
			count=exportUserReportDAO.queryUserByItvCount(fileName, curPage_splitPage, num_splitPage);
		}else if("voip_account".equals(importQueryField)){
			count=exportUserReportDAO.queryUserByVoipCount(fileName, curPage_splitPage, num_splitPage);
		}
		return count;
	}
	
	private List<String> getImportDataByXLS(String fileName) throws IOException, BiffException
	{
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);
			
			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			//int columeCount = ws.getColumns();
			
			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
				
				if(null!=line && "用户LOID".equals(line)){
					this.importQueryField = "loid";
				}else if(null!=line && "宽带账号".equals(line)){
					this.importQueryField = "net_account";
				}else if(null!=line && "ITV账号".equals(line)){
					this.importQueryField = "itv_account";
				}else if(null!=line && "语音账号".equals(line)){
					this.importQueryField = "voip_account";
				}
			}
			
			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		return list;
		
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
	private List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException
	{
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件
		 */
		if(null!=line && "用户LOID".equals(line)){
			this.importQueryField = "loid";
		}else if(null!=line && "宽带账号".equals(line)){
			this.importQueryField = "net_account";
		}else if(null!=line && "ITV账号".equals(line)){
			this.importQueryField = "itv_account";
		}else if(null!=line && "语音账号".equals(line)){
			this.importQueryField = "voip_account";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null ) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		return list;
	}

	public List<Map> getUseExcel(String fileName){
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",fileName);
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",fileName);
			return null;
		}
		catch(Exception e){
			logger.warn("{}文件解析出错！",fileName);
			return null;
		}
		return exportUserReportDAO.getUserExcel(fileName, importQueryField);
	}
	
	
	public String getImportQueryField()
	{
		return importQueryField;
	}

	
	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}

	
	public String getMsg()
	{
		return msg;
	}

	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	
	public ExportUserReportDAO getExportUserReportDAO()
	{
		return exportUserReportDAO;
	}

	
	public void setExportUserReportDAO(ExportUserReportDAO exportUserReportDAO)
	{
		this.exportUserReportDAO = exportUserReportDAO;
	}

	

	
}
