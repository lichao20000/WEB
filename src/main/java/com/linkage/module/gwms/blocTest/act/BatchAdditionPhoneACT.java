
package com.linkage.module.gwms.blocTest.act;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.blocTest.bio.BatchAdditionPhoneBIO;

import action.splitpage.splitPageAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-21
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchAdditionPhoneACT extends splitPageAction implements ServletRequestAware
{
	private static Logger logger = LoggerFactory.getLogger(BatchAdditionPhoneACT.class);
	/**传入的文件名*/
	private String gwShare_fileName;
	private static final long serialVersionUID = 1L;
	
	private BatchAdditionPhoneBIO bio;
	// 回传消息
	private String msg = null;
	private String ajax="";
	
	public String insert() throws IOException
	{
		logger.warn("insert gwShare_fileName==="+gwShare_fileName);
		ArrayList<Map> list = new ArrayList<Map>();
		List<Map<String,String>> listshow=new ArrayList<Map<String,String>>();
		try
		{
			String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
			if("xls".equals(fileName_)){
				list = BatchAdditionPhoneBIO.importToExcel_col(gwShare_fileName);
			}else if("txt".equals(fileName_)){
				if(Global.JXDX.equals(Global.instAreaShortName)){
					listshow=bio.getImportDataByTXT_jxdx(gwShare_fileName );
				}else{
					listshow=bio.getImportDataByTXT(gwShare_fileName );
				}
			}
			
			if(list!=null && list.size()>0)
			{
				ajax=String.valueOf(bio.gettabphoneinfo(list));
			}
			
			if(listshow!=null && listshow.size()>0)
			{
				if(Global.JXDX.equals(Global.instAreaShortName))
				{
					if(listshow.size()>50000){
						logger.warn("文件过大，最多单次导入五万条数据！");
						ajax="-100";
						return "ajax";
					}
					
					ajax=String.valueOf(bio.insertTabmaccompany(listshow));
					logger.warn("入库tab_mac_company数据量："+listshow.size());
				}
				else
				{
					ajax=String.valueOf(bio.gettabmaccompany(listshow));
				}
			}
		}
		catch(FileNotFoundException e){
			logger.warn("{}文件没找到！",gwShare_fileName);
			this.msg = "文件没找到！";
			return null;
		}catch(IOException e){
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		catch (ParseException e)
		{
			logger.warn("{}文件解析出错！",gwShare_fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		return "ajax";
	} 

	
	public String getGwShare_fileName(){
		return gwShare_fileName;
	}
	
	public void setGwShare_fileName(String gwShare_fileName){
		this.gwShare_fileName = gwShare_fileName;
	}

	public BatchAdditionPhoneBIO getBio(){
		return bio;
	}

	public void setBio(BatchAdditionPhoneBIO bio){
		this.bio = bio;
	}

	public String getMsg(){
		return msg;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	@Override
	public void setServletRequest(HttpServletRequest request){
	}
	
	public String getAjax(){
		return ajax;
	}
	
	public void setAjax(String ajax){
		this.ajax = ajax;
	}
	
}
