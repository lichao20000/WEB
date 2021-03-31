package com.linkage.module.itms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.itms.resource.dao.MemBookDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年12月18日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MemBookBIO
{
	
	private MemBookDAO dao;
	private Logger logger = LoggerFactory.getLogger(MemBookBIO.class);
	
	public List<Map> qryWorkList(String busNo,String vendor_id,String spec_id,String spec_model,String hardware,String software,String workOpts_id
			,String connPerson,String connPhone,String reception_id,String startTime,String endTime, 
			int curPage_splitPage,int num_splitPage){
		return dao.qryWorkList(busNo, vendor_id, spec_id, spec_model,hardware, software, workOpts_id, connPerson, connPhone, reception_id, startTime, endTime, curPage_splitPage, num_splitPage);
	}
	
	public int qryWorkListCount(String busNo,String vendor_id,String spec_id,String spec_model,String hardware,String software,String workOpts_id
			,String connPerson,String connPhone,String reception_id,String startTime,String endTime){
		return dao.qryWorkListCount(busNo, vendor_id, spec_id,spec_model, hardware, software, workOpts_id, connPerson, connPhone, reception_id, startTime, endTime);
	}
	
	public List<HashMap<String,String>> qryWorkExcel(String busNo,String vendor_id,String spec_id,String spec_model,String hardware,String software,String workOpts_id
			,String connPerson,String connPhone,String reception_id,String startTime,String endTime){
		return dao.qryWorkExcel(busNo, vendor_id, spec_id,spec_model, hardware, software, workOpts_id, connPerson, connPhone, reception_id, startTime, endTime);
	}
	
	public String operMemorandum(int type,String busNo,String vendor,String spec,String spec_model,String hardware,String software,String workOpts
			,String workContent,String connPerson,String connPhone,String reception,String startTime,
			String endTime,int status,String remark,String fileName){
		if(type == 1){
			return dao.addMemorandumInfo(busNo, vendor, spec, spec_model, hardware, software, workOpts, workContent, connPerson, connPhone, reception, startTime, endTime, status, remark, fileName);
		}else{
			return dao.updateMemorandum(busNo, vendor, spec, spec_model, hardware, software, workOpts, workContent, connPerson, connPhone, reception, startTime, endTime, status, remark, fileName);
		}
	}
	
	public void downLoad(String fileName,HttpServletResponse response){
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		String path = LipossGlobals.getLipossProperty("work_memory_path");
		if(StringUtil.IsEmpty(path)){
			path = "/temp/";
		}
		String filePath = lipossHome + path + fileName;
		File imageFile = new File(filePath);
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			// path是指欲下载的文件的路径
			// 取得文件名
			String filename = imageFile.getName();
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + imageFile.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}
		catch (IOException e)
		{
			logger.error("download file:[{}], error:", imageFile, e);
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
	public String deletMem(String busNo){
		return dao.deletMem(busNo);
	}
	
	//通过厂商id获取终端类型
	public String getSpecType(String vendor_id){
		List maps =  dao.getSpecType(vendor_id);
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < maps.size(); i++){
			Map map = (Map) maps.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("value_id"));
			bf.append("$");
			bf.append(map.get("value"));
		}
		return bf.toString();
	}
	
	public String getParamMap(int type){
		List maps = dao.getMap(type);
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < maps.size(); i++){
			Map map = (Map) maps.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("value_id"));
			bf.append("$");
			bf.append(map.get("value"));
		}
		return bf.toString();
	}
	
	public String impFileParse(String filename){
		List workMemList = readFileRes(filename);
		if (workMemList.size() < 1){
			return "导入的文件无数据";
		}
		String rows = "";
		String[] row;
		String busNo = "";
		String vendor = "";
		String spec_type = "";
		String spec_model = "";
		String hardware = "";
		String software = "";
		String workOpts = "";
		String connPerson = "";
		String connPhone = "";
		String reception = "";
		String startTime = "";
		String endTime = "";
		String workContent = "";
		int status = 0;
		String remark = "";
		ArrayList<String> batchSql = new ArrayList<String>();
		try
		{
			//舍弃第一条
			workMemList = workMemList.subList(1, workMemList.size());
			int index = 0 ;
			for (Iterator iter = workMemList.iterator(); iter.hasNext();) {
				index++;
				rows = (String) iter.next();
				row = rows.split(",");
				logger.warn("row.length:"+row.length);
				if(row.length != 14){
					logger.error("第{}行数据有误,该行数据无法入库",new Object[]{index});
					continue;
				}
				busNo = StringUtil.getStringValue(System.currentTimeMillis()/1000 + index);
				vendor = row[0];
				if(StringUtil.IsEmpty(vendor)){
					vendor = "vendorIsNULL";
				}
				spec_type = row[1];
				if(StringUtil.IsEmpty(spec_type)){
					spec_type = "specIsNULL";
				}
				spec_model = row[2];
				hardware = row[3];
				software = row[4];
				workOpts = row[5];
				if(StringUtil.IsEmpty(workOpts)){
					workOpts = "workOptsIsNULL";
				}
				connPerson = row[6];
				connPhone = row[7];
				reception = row[8];
				if(StringUtil.IsEmpty(reception)){
					reception = "receptionIsNULL";
				}
				startTime = row[9];
				endTime = row[10];
				DateTimeUtil dtu = new DateTimeUtil(startTime);
				if(StringUtil.IsEmpty(startTime)){
					startTime = "0";
				}else{
					startTime = String.valueOf(dtu.getLongTime());
				}
				if(StringUtil.IsEmpty(endTime)){
					endTime = "0";
				}else{
					dtu = new DateTimeUtil(endTime);
					endTime = String.valueOf(dtu.getLongTime());
				}
				
				workContent = row[11];
				status = StringUtil.getIntegerValue(row[12]);
				remark = row[13];
				
				batchSql.add(dao.addMemorandum(busNo, vendor, spec_type, spec_model, hardware, software, workOpts, workContent, connPerson, connPhone, reception, startTime, endTime, status, remark, ""));
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			logger.error("解析导入的文件失败：{}",e1.getMessage());
		}
		try {
			if (batchSql.size() > 0) {
				int iCodes = DBOperation.executeUpdate(batchSql);
				if (iCodes > 0) {
					 return "批量导入成功";
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量导入工作备忘录失败：{}",e.getMessage());
		}
		return "批量导入失败";
	}
	
	private List readFileRes(String fname) {
		String pathDir = LipossGlobals.getLipossProperty("work_memory_path");
		if(StringUtil.IsEmpty(pathDir)){
			pathDir = "/temp/";
		}
		String tempf = LipossGlobals.getLipossHome() + java.io.File.separator + pathDir;
		String path = tempf + java.io.File.separator + fname;
		logger.warn("readFileRes："+path);
		List list = new ArrayList();
		BufferedReader in =null;
		try {
			in = new BufferedReader(new FileReader(path));
			String line;
			while ((line = in.readLine()) != null) {
				list.add(line);
			}
			in.close();
			in = null;
			java.io.File f = new java.io.File(path);
			f.delete();
			f = null;
		} catch (IOException e) {
			//logger.warn("处理文件出错" + e.getMessage());
		}finally{
			if(in!=null){
				try {
					in.close();
					in=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public MemBookDAO getDao()
	{
		return dao;
	}

	public void setDao(MemBookDAO dao)
	{
		this.dao = dao;
	}
}
