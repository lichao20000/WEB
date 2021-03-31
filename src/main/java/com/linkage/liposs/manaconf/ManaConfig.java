package com.linkage.liposs.manaconf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.operation.ServeRatioSearch;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.system.UserRes;

/**
 * 配置管理操作类
 * @author maym
 * 2007-04-26
 */
public class ManaConfig
{

	private HttpServletRequest request;
	private static Logger m_logger = LoggerFactory.getLogger(ManaConfig.class);
	
	public ManaConfig(HttpServletRequest request) 
	{
		super();
		this.request = request;
	}
	
	public ManaConfig() 
	{
		super();
		this.request = null;
	}
	
	
	/**
	 * 读取文件
	 * @param fileName
	 * @return
	 */
	public StringBuffer getFile(String fileName)
	{
		StringBuffer sb = new StringBuffer(20000);
		PropertiesFileManager pf = new PropertiesFileManager("config.properties");
		String path = pf.getConfigItem("path");		
		String line = "";
		
		BufferedReader in=null;
		try {
			
			 m_logger.debug(path + fileName);
			 in = new BufferedReader(new FileReader(path + fileName));
			
			 for (int i=1; (line = in.readLine()) != null; i++) {
				sb.append("<tr><TD class=column width='3%' align='right'>");
				sb.append(i);
				sb.append("</TD>");
				sb.append("<TD class=column align='left'>&nbsp;&nbsp;");
				sb.append(line);
				sb.append("</TD></tr>");
			}
		} catch (FileNotFoundException e) {
			m_logger.warn("warning: the file is not exist.");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sb;
	}
	
	/**
	 * 
	 * 比较文件
	 * @param standardFileName
	 * @param compareFileName
	 * @return
	 */
	
	public StringBuffer getCompareFile(String standardFileName, String compareFileName) {
		StringBuffer sb = new StringBuffer(20000);
		
		String lineCompare, lineStandard;
		PropertiesFileManager pf = new PropertiesFileManager("config.properties");
		String path = pf.getConfigItem("path");	
		
		BufferedReader inStandard=null;
		BufferedReader inCompare=null;
		try {
			m_logger.debug(path + standardFileName);
			m_logger.debug(path + compareFileName);
			
			inStandard =
				new BufferedReader(new FileReader(path + standardFileName));
			inCompare =
				new BufferedReader(new FileReader(path + compareFileName));
			
			for (int i=1; (lineCompare = inCompare.readLine()) != null; i++) {
				if((lineStandard = inStandard.readLine()) != null) {
					if(lineCompare.equals(lineStandard)) {
						sb.append("<tr><TD class=column width='3%' align='right'>");
						sb.append(i);
						sb.append("</TD>");
						sb.append("<TD class=column align='left'>&nbsp;&nbsp;");
						sb.append(lineCompare);
						sb.append("</TD></tr>");
					} else {
						sb.append("<tr><TD class=column width='3%' align='right'>");
						sb.append("<font color=\"red\"><B>" + i + "</B></font>");
						sb.append("</TD>");
						sb.append("<TD class=column align='left'>&nbsp;&nbsp;");
						sb.append(lineCompare);
						sb.append("</TD></tr>");
					}
				} else {
					sb.append("<tr><TD class=column width='3%' align='right'>");
					sb.append("<font color=\"red\"><B>" + i + "</B></font>");
					sb.append("</TD>");
					sb.append("<TD class=column align='left'>&nbsp;&nbsp;");
					sb.append(lineCompare);
					sb.append("</TD></tr>");
				}
			}
		} catch (FileNotFoundException e) {
			m_logger.warn("warning: the file is not exist.");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(inStandard!=null){
					inStandard.close();
				}
				if(inCompare!=null){
					inCompare.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sb;	
	}
	/**
	 * 获得匹配关键字的文件查看内容
	 * 
	 * @return
	 */
	public StringBuffer getFile(String fileName,String keyword)
	{
		StringBuffer sb = new StringBuffer(20000);
		String replace = "<span style=\"background-color:#00FF66;font-weight:bold;color:#990000\">"
                       +  keyword + "</span>";
		
        sb = this.getFile(fileName);
        sb = new StringBuffer(sb.toString().replaceAll(keyword, replace));
		return sb;
	}
	/**
	 * 获得关键字匹配的次数
	 * author：maym
	 * date：2007-07-06
	 * @param fileName
	 * @param keyword
	 * @return
	 */
	public int getKeyNumber(String fileName,String keyword)
	{
		String fileContext = this.getFile(fileName, keyword).toString();
		int counter = 0;
        int index = 0;
        
	 	index = fileContext.indexOf(keyword);
	 	
	 	while (index != -1)
	 	{
	 		counter++;
	 		index = index + keyword.length();
		 	index = fileContext.indexOf(keyword, index);
	 	}
		return counter;
	}
	
	/**
	 * 从备份文件目录取得文件列表
	 * 
	 */
	public String[] getAllFileNameList(String gather_id)
	{
		String[] arr = null;
		File directory = null;		
		PropertiesFileManager pf = new PropertiesFileManager("config.properties");
		String path = pf.getConfigItem("path");
		
		path = path + gather_id.trim() + "/";
		
		m_logger.debug(path);
		
		directory = new File(path);

		if (directory.exists() && directory.isDirectory()) {
			arr = directory.list();
			Arrays.sort(arr);
		}
		
		return arr;
	}
	
	/**
	 * 
	 * 获得符合查询条件的配置文件
	 * 
	 */
    public ArrayList getDeviceFileNameList(String device_ip,String gather_id) 
    {
				
		ArrayList list = new ArrayList();
		String[] tem = this.getAllFileNameList(gather_id);
		m_logger.debug("有文件数为:" + tem.length);
		if(tem != null) {
			for (int i=0; i < tem.length; i++) {
				if(tem[i].indexOf(device_ip.trim()) != -1) {
					list.add(gather_id + "/" + tem[i]);
				}				
			}
		}
		
		return list;	
	}
    /**
     * 获得备份文件列表
     * author：maym
     * 2007-07-08
     * @return 文件列表list
     */
    public ArrayList getDeviceFileNameList()
    {
    	ArrayList list = new ArrayList();
    	String device_id = request.getParameter("device_id");
    	String device_ip = request.getParameter("device_ip");
    	String city_id = request.getParameter("city_id");
    	String type = request.getParameter("type");
    	String gather_id = "1";
    	String loopback_ip = null;
    	Map fields = null;
    	String sql = "select gather_id,loopback_ip from tab_gw_device where 1=1";
    	
    	if (city_id.equals("-1") || city_id == null)
    	{
    		HttpSession session = request.getSession();  
        	UserRes curUser = (UserRes) session.getAttribute("curUser");
        	city_id = curUser.getCityId();
    	}
    	
		//如果用户手工输入IP
		if ((type != null) && (type.equals("1")))
		{
			sql += " and city_id in (select city_id from tab_city where city_id='"
				 + city_id + "' or parent_id ='" + city_id + "')" 
				 + " and loopback_ip='" +  device_ip + "'"; 
		}
		else if ((type != null) && (type.equals("2")))
		{
			sql += " and device_id='" + device_id + "'";
		}
//    	m_logger.debug("getDeviceFileNameList():" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		fields = DataSetBean.getRecord(psql.getSQL());
		
		if (fields != null)
		{
			gather_id = (String)fields.get("gather_id");
			loopback_ip = (String)fields.get("loopback_ip");
			list = this.getDeviceFileNameList(loopback_ip, gather_id);
		}
    	return list;
    }
/**
* 通过设备ID获得设备信息
* 
*/
	public Cursor getDeviceByID()
	{
		Cursor cursor = null;
		String device_id = request.getParameter("device_id");
		String strSQL = null;
		strSQL = "select a.device_id,a.backup_type,a.read_comm,a.write_comm,"
			   + " a.tel_logonmod,a.tel_user,a.tel_pwd,a.tel_encmd,a.tel_enpwd,hostprompt,"
		       + " a.ftp_user,a.ftp_pwd,a.ftp_dir,a.ftp_filename,a.if_backup,a.time_type,"
		       + " a.time_model,a.backup_time,a.gather_id,b.vendor_id,b.device_model_id as device_model,b.loopback_ip,b.os_version"
		       + " from tab_backup_device_test a, tab_gw_device b"	  
		       + " where a.device_id ='" + device_id + "'"
		       + " and a.device_id = b.device_id";
		
//		m_logger.debug("根据ID取得备份配置信息："+strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		cursor = DataSetBean.getCursor(psql.getSQL());
		
		return cursor;
	}
	
/**
*  获得采集区域下拉框
* 
*/	
	public String getGatherSelect(String gather_id)
	{
		String result = null;		
		ServeRatioSearch Info = new ServeRatioSearch(request);
		Cursor cursor = Info.GatherID();
		Map fields = cursor.getNext();
		
		result = "<select name=gather_id class=bk onchange=showChild('gather_id')>"
               + "<option value='-1'>==采集区域==</option>";

		while (fields != null)
		{
		   result += "<option value='" + (String) fields.get("gather_id") + "'";
		   
		   if(gather_id != null && gather_id.equals((String) fields.get("gather_id")))
		   {
			   result += " selected='selected'";
		   }
			     
		   result += ">" + (String) fields.get("descr") +  "</option>";


		   fields = cursor.getNext();
		}
		
	    result += "</select>";
		
		return result;
	}
/**
 * 删除已配置设备信息
 * 
 */
	public String delDeviceConfig()
	{
		String result = "设备配置信息已成功删除！";
		String device_id = request.getParameter("device_id");
		String device_ip = request.getParameter("device_ip");
		String gather_id = request.getParameter("gather_id");
		String strSQL = "delete from tab_backup_device_test where device_id=" + device_id;
		
		//检查用户是否有权限操作该设备
		result = checkDomainDevice(device_ip);
		
	    if (result.equals("1"))
	    {
	    	PrepareSQL psql = new PrepareSQL(strSQL);
			int tmp = DataSetBean.executeUpdate(psql.getSQL());
			
			if (tmp == 1)
			{
				result = "设备配置信息删除成功！";
			}
			else
			{
				result = "删除设备配置信息失败，请重新尝试！";
			}
	    }
	    
        
        MCCorbaManager mc = new MCCorbaManager(gather_id);
		mc.chnageConf();	

		return result;
	}
/**
 * 立即备份
 * 
 */
	public String promptBackup()
	{
		String result = null;
		MCCorbaManager corbaManager = null;
		String strSQL = null;
		Cursor cursor = null;
		Map fields = null;
		String gather_id = null;
		String device_ip = null;
		//String command = " ";
		I_Mana_Conf.Telnet_Info tel_info = null;
		I_Mana_Conf.Ftp_Info ftp_info = null;
		
		int backup_type = Integer.parseInt(request.getParameter("backup_type"));
		m_logger.debug("backup type :" + backup_type);
		//String device_id = request.getParameter("device_id");
		String device_id = request.getParameter("devicelist");
		String device_model = request.getParameter("device_model");
		String os_version = request.getParameter("os_version");
		String ftp_filename = request.getParameter("ftp_filename");
		String flag = request.getParameter("flag");//判断是否需要加密
		
		
		if (os_version.equals("-1") || os_version.equals(""))
		{
			os_version = " ";
		}
		
		//获得设备采集区域和设备IP
		strSQL = "select gather_id,loopback_ip from tab_gw_device where device_id='"+device_id+"'";
//		m_logger.debug("get ip gid:"+strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		device_ip = (String)fields.get("loopback_ip");
		gather_id = (String)fields.get("gather_id");
		
		m_logger.debug("##########device_ip:" + device_ip);
		m_logger.debug("##########gather_id:" + gather_id);
		
		if (backup_type == 1)
		{
			//TELNET方式备份
			m_logger.debug("##########TELNET方式进行备份，获得参数...");
			String tel_logonmod = request.getParameter("tel_logonmod");
			String tel_user = request.getParameter("tel_user");
			String tel_pwd = request.getParameter("tel_pwd");
			String tel_encmd = request.getParameter("tel_encmd");
			String tel_enpwd = request.getParameter("tel_enpwd");
			String hostprompt = request.getParameter("hostprompt");
			m_logger.debug("##########TELNET方式进行备份，获得参数完成");
			m_logger.debug("#########加密前的参数#########");
			m_logger.debug("device_ip:" + device_ip);
			m_logger.debug("tel_logonmod:" + tel_logonmod);
			m_logger.debug("tel_user:" + tel_user);
			m_logger.debug("tel_pwd:" + tel_pwd);
			m_logger.debug("tel_enpwd:" + tel_enpwd);
			m_logger.debug("tel_encmd:" + tel_encmd);
			m_logger.debug("hostprompt:" + hostprompt);
			m_logger.debug("ftp_filename:" + ftp_filename);
			m_logger.debug("device_model:" + device_model);
			m_logger.debug("os_version:" + os_version);
			m_logger.debug("##########################################");
			//------------------------------------------
			
		//----------add by maym 070703---------------	
			if (tel_encmd == null || tel_encmd.equals(""))
			{
				tel_encmd = " ";
			}
			
			if (tel_enpwd == null || tel_enpwd.equals(""))
			{
				tel_enpwd = " ";
			}
			/*
			if (hostprompt == null || hostprompt.equals(""))
			{
				hostprompt = "#";
			}*/
		//---------------------------------------------
			if (flag.equals("0"))
			{
			    if (tel_pwd != null)
			    {
				    tel_pwd = Encoder.getBase64(tel_pwd);
			    }
			
			    if (tel_enpwd != null)
			    {
				    tel_enpwd = Encoder.getBase64(tel_enpwd);
			    }
			}
			
			tel_info = new I_Mana_Conf.Telnet_Info(device_ip,
					                               tel_logonmod,
					                               tel_user,
					                               tel_pwd,
					                               tel_encmd,
					                               tel_enpwd,
					                               hostprompt,
					                               ftp_filename,
					                               device_model,
					                               os_version);
			
			m_logger.debug("#########加密后的参数#########");
			m_logger.debug("device_ip:" + device_ip);
			m_logger.debug("tel_logonmod:" + tel_logonmod);
			m_logger.debug("tel_user:" + tel_user);
			m_logger.debug("tel_pwd:" + tel_pwd);
			m_logger.debug("tel_enpwd:" + tel_enpwd);
			m_logger.debug("tel_encmd:" + tel_encmd);
			m_logger.debug("hostprompt:" + hostprompt);
			m_logger.debug("ftp_filename:" + ftp_filename);
			m_logger.debug("device_model:" + device_model);
			m_logger.debug("os_version:" + os_version);
			m_logger.debug("##########################################");
			
			ftp_info = new I_Mana_Conf.Ftp_Info(" ", " ", " ", " ", 0,
					                            " ", " ", " ", " ");
			
		}
		else if (backup_type == 2)
		{
			//FTP方式备份
			String ftp_user = request.getParameter("ftp_user");
			String ftp_pwd = request.getParameter("ftp_pwd");
			String ftp_dir = request.getParameter("ftp_dir");
			
			//------add by maym 070703-------------------------
			if (ftp_dir == null || ftp_dir.equals(""))
			{
				ftp_dir = " ";
			}
			//--------------------------------------------------
			if (flag.equals("0"))
			{	
			    if (ftp_pwd != null)
			    {
				    ftp_pwd = Encoder.getBase64(ftp_pwd);
			    }
			}
			
			ftp_info = new I_Mana_Conf.Ftp_Info(device_model,
					                            os_version,
					                            device_ip,
					                            " ",
					                            0,
					                            ftp_user,
					                            ftp_pwd,
					                            ftp_dir,
					                            ftp_filename);
			
			tel_info = new I_Mana_Conf.Telnet_Info(" ", " ", " ", " "," ",
					                               " ", " ", " ", " ", " ");
		}
		else if (backup_type == 4)
		{
			//TELNET方式备份
			m_logger.debug("##########SHOW方式进行备份，获得参数...");
			String tel_logonmod = request.getParameter("tel_logonmod");
			String tel_user = request.getParameter("tel_user");
			String tel_pwd = request.getParameter("tel_pwd");
			String tel_encmd = request.getParameter("tel_encmd");
			String tel_enpwd = request.getParameter("tel_enpwd");
			String hostprompt = request.getParameter("hostprompt");
			m_logger.debug("##########TELNET方式进行备份，获得参数完成");
			m_logger.debug("#########加密前的参数#########");
			m_logger.debug("device_ip:" + device_ip);
			m_logger.debug("tel_logonmod:" + tel_logonmod);
			m_logger.debug("tel_user:" + tel_user);
			m_logger.debug("tel_pwd:" + tel_pwd);
			m_logger.debug("tel_enpwd:" + tel_enpwd);
			m_logger.debug("tel_encmd:" + tel_encmd);
			m_logger.debug("hostprompt:" + hostprompt);
			m_logger.debug("ftp_filename:" + ftp_filename);
			m_logger.debug("device_model:" + device_model);
			m_logger.debug("os_version:" + os_version);
			m_logger.debug("##########################################");
			//------------------------------------------
			
		//----------add by maym 070703---------------	
			if (tel_encmd == null || tel_encmd.equals(""))
			{
				tel_encmd = " ";
			}
			
			if (tel_enpwd == null || tel_enpwd.equals(""))
			{
				tel_enpwd = " ";
			}
			/*
			if (hostprompt == null || hostprompt.equals(""))
			{
				hostprompt = "#";
			}*/
		//---------------------------------------------
			if (flag.equals("0"))
			{
			    if (tel_pwd != null)
			    {
				    tel_pwd = Encoder.getBase64(tel_pwd);
			    }
			
			    if (tel_enpwd != null)
			    {
				    tel_enpwd = Encoder.getBase64(tel_enpwd);
			    }
			}
			
			tel_info = new I_Mana_Conf.Telnet_Info(device_ip,
					                               tel_logonmod,
					                               tel_user,
					                               tel_pwd,
					                               tel_encmd,
					                               tel_enpwd,
					                               hostprompt,
					                               ftp_filename,
					                               device_model,
					                               os_version);
			
			m_logger.debug("#########加密后的参数#########");
			m_logger.debug("device_ip:" + device_ip);
			m_logger.debug("tel_logonmod:" + tel_logonmod);
			m_logger.debug("tel_user:" + tel_user);
			m_logger.debug("tel_pwd:" + tel_pwd);
			m_logger.debug("tel_enpwd:" + tel_enpwd);
			m_logger.debug("tel_encmd:" + tel_encmd);
			m_logger.debug("hostprompt:" + hostprompt);
			m_logger.debug("ftp_filename:" + ftp_filename);
			m_logger.debug("device_model:" + device_model); 
			m_logger.debug("os_version:" + os_version);
			m_logger.debug("##########################################");
			
			ftp_info = new I_Mana_Conf.Ftp_Info(" ", " ", " ", " ", 0,
					                            " ", " ", " ", " ");
			
		}

		else
		{
			result = "您所选择的备份方式暂不支持，请选择其他方式备份！";
			return result;
		}
		
		//调用CORBA接口进行立即备份
		m_logger.debug("MCCorbaManager(gather_id):" + gather_id);
		corbaManager = new MCCorbaManager(gather_id);
		result = corbaManager.promptBackup(backup_type, tel_info, ftp_info);
		
		if (result.equals("1"))
		{
			result = "设备立即备份成功！";
		}
		else if (result.equals("0"))
		{
			result = "设备立即备份失败，请重新尝试！";
		}
		
		return result;
	}
/**
 * 获得备份日志列表
 * @return  返回日志记录集
 */
	public ArrayList getConfLogList()
	{
		Cursor cursor = null;
		String strSQL = null;
		ArrayList list = new ArrayList();
		String type = request.getParameter("type");
		String str_start = request.getParameter("start");
		String str_end = request.getParameter("end");
		String loopback_ip = request.getParameter("loopback_ip");
		String devicelist = request.getParameter("devicelist");       //设备id
		String gather_id = "1";//1是移动这边的默认采集点，其他项目根据自己需要来修改
		String city_id = request.getParameter("city_id");
		String getGathSql="select gather_id from tab_gw_device where 1=1"; 
		
		//获得用户登陆域
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();  
		
		//如果用户手工输入IP
		if ((type != null) && (type.equals("1")))
		{
			getGathSql += " and city_id='" + city_id + "' and loopback_ip='" +  loopback_ip + "'"; 
		}
		else if ((type != null) && (type.equals("2")))
		{
			getGathSql += " and device_id=" + devicelist;
		}
		PrepareSQL psql = new PrepareSQL(getGathSql);
		HashMap gath = DataSetBean.getRecord(psql.getSQL());
		if(gath!=null)
		{
		    gather_id = (String)gath.get("gather_id");
		}
		long start = 0;
		long end = 0;
		//------------------------ADD BY MAYM--------------------------
		String ssql = "&type=" + type
		            + "&start=" + str_start
		            + "&end=" + str_end
		            + "&loopback_ip=" + loopback_ip
		            + "&devicelist=" + devicelist
		            + "&gather_id=" + gather_id;
		//-------------------------------------------------------------
			
		//时间格式转换
		SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd");
		if ((str_start !=null) && (str_end != null))
		{	
		   try
		   {
			   Date s = Formatter.parse(str_start);
			   Date e = Formatter.parse(str_end);
			
			   start = s.getTime()/1000;
			   end = e.getTime()/1000;
               
		   }
		   catch(ParseException e)
		   {
			   m_logger.error("时间格式转换出现错误!");
		   }
		}

		strSQL = "select a.device_ip,a.backup_time,a.file_name,a.state,a.device_id,"
			   + " b.device_name,b.device_model_id as device_model "
			   + " from tab_backup_log a, tab_gw_device b "
			   + " where a.backup_time >=" + start
			   + " and a.backup_time <=" + end;
		
		//如果用户手工输入IP
		if ((type != null) && (type.equals("1")))
		{
			String result = checkDomainDevice(loopback_ip);
			
			if (result.equals("1"))
			{
			   strSQL +=  " and a.device_ip ='" + loopback_ip +"'"
			          + " and a.device_id = b.device_id";
			}
			else
			{
				list.add(result);
				return list;
			}
		}
		else if ((type != null) && (type.equals("2")))
		{
			strSQL +=  " and a.device_id ='" + devicelist +"'"
			   //+ " and a.gather_id ='" + gather_id + "'"
			   + " and a.device_id = b.device_id";
			   //+ " and b.gather_id = a.gather_id";
		}
		else
		{
			strSQL += " and a.device_id = b.device_id ";
		}
		
	    strSQL += " and a.device_id in (select res_id from tab_gw_res_area where area_id="
			  + areaid + " and res_type=1)"
			  + " order by a.backup_time desc";
		
//		m_logger.debug(strSQL);
//		m_logger.debug(strSQL);
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		
		QueryPage qryp = new QueryPage();
		PrepareSQL psql2 = new PrepareSQL(strSQL);
		qryp.initPage(psql2.getSQL(), offset, pagelen);
		cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		String strBar = null;
		//----------------modify by maym-------------------		
		if (ssql != null && !ssql.equals(""))
		{
			strBar = qryp.getPageBar(ssql);
		}
		else
		{
			strBar = qryp.getPageBar();
		}
        //--------------------------------------------------
		list.add(strBar);
		list.add(cursor);

		return list;
	}
	/**
	 * 根据设备IP，检查用户是否有权限管理该设备
	 * @param device_ip
	 * @return
	 */
    public String checkDomainDevice(String device_ip)
    {
    	HttpSession session = request.getSession();
    	UserRes curUser = (UserRes) session.getAttribute("curUser");
    	long areaid = curUser.getAreaId();
    	String strSQL = "";
    	String result = "1";
    	Cursor cursor = null;
    	Map fields = null;

    	strSQL += "select res_id from tab_res_area where area_id="
    		    + areaid + " and res_type=1"
    		    + " and res_id in (select device_id from tab_gw_device where loopback_ip='"
    		    + device_ip + "')";
    					
//    	m_logger.debug(strSQL);
    	PrepareSQL psql = new PrepareSQL(strSQL);
    	cursor = DataSetBean.getCursor(psql.getSQL());
    	fields = cursor.getNext();
    	if (fields == null)
    	{
    	   result = "您没有权限查看该设备相关备份信息！";
    	}
    	
    	return result;
    }
/**
 * 添加新增的备份设备信息
 * @return 返回操作成功与否的信息
 */	
	public String saveDeviceConfig()
	{
		String result = null;
		String strSQL = null;
		Cursor cursor = null;
		Map fields = null;
	//	String device_id = null;
		String backup_time = null;
		String time_model = "";
		String gather_id = null;
	//	String pre_time = null;
	//	String pre_file = null;
		
		String backup_type = request.getParameter("backup_type");
		String time_type = request.getParameter("time_type");
		String if_backup = request.getParameter("if_backup");
		String device_id = request.getParameter("devicelist");
		String read_comm = request.getParameter("read_comm");
		String write_comm = request.getParameter("write_comm");
		String tel_logonmod = request.getParameter("tel_logonmod");
		String tel_user = request.getParameter("tel_user");
		String tel_pwd = request.getParameter("tel_pwd");
		String tel_encmd = request.getParameter("tel_encmd");
		String tel_enpwd = request.getParameter("tel_enpwd");
		String hostprompt = request.getParameter("hostprompt");
		String ftp_user = request.getParameter("ftp_user");
		String ftp_pwd = request.getParameter("ftp_pwd");
		String ftp_dir = request.getParameter("ftp_dir");
		String ftp_filename = request.getParameter("ftp_filename");
		String action = request.getParameter("act");
		String city_id = request.getParameter("city_id");
		
		if (tel_pwd != null)
		{
			tel_pwd = Encoder.getBase64(tel_pwd);
		}
		
		if (tel_enpwd != null)
		{
			tel_enpwd = Encoder.getBase64(tel_enpwd);
		}
		
		if (ftp_pwd != null)
		{
			ftp_pwd = Encoder.getBase64(ftp_pwd);
		}
				
		if (time_type.equals("1"))
		{
			//定时备份
			String backup_time1 = request.getParameter("backup_time1");
			String backup_time2 = request.getParameter("backup_time2");
			
			String bt1[] = backup_time1.split("-");
			String bt2[] = backup_time2.split(":");
			
			//确保日期格式为YYYYMMDD
		    if (bt1[1].length() == 1)
		    {
		    	bt1[1] = "0" + bt1[1];
		    }
		    
		    if (bt1[2].length() == 1)
		    {
		    	bt1[2] = "0" + bt1[2];
		    }
			
			backup_time = bt1[0]+bt1[1]+bt1[2]+bt2[0]+bt2[1];
			
		}
		else if (time_type.equals("2"))
		{
		    //周期备份	
			time_model = request.getParameter("time_model");
			String hour =  request.getParameter("hour");
			
			if (time_model.equals("1"))
			{
				backup_time = hour;
			}
			else if (time_model.equals("2"))
			{
				String week = request.getParameter("week");
				backup_time = week + "," + hour;
			}
			else if (time_model.equals("3"))
			{
				String month = request.getParameter("month");
				backup_time = month + "," + hour;
			}					
		}
		 
		//获得设备采集区域
		
		strSQL = "select gather_id from tab_gw_device where device_id='"+device_id+"'";
//		m_logger.debug("设备保存获得采集区域：" + strSQL);
		if (city_id != null)
		{
			//strSQL += "	and city_id='"+city_id + "'";
			strSQL += " and city_id in (select city_id from tab_city where city_id='"
	           + city_id + "' or parent_id ='" + city_id + "')" ;

		}

//		m_logger.debug("gather_id:" + strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
		cursor = DataSetBean.getCursor(psql.getSQL());
		fields = cursor.getNext();
		//device_id = (String)fields.get("device_id");
		gather_id = (String)fields.get("gather_id");
//		m_logger.debug("备份设备采集区域："+ gather_id);
		if (backup_type.equals("1"))
		{
			ftp_user = " ";
			ftp_pwd = " ";
			ftp_dir = " ";
			read_comm = " ";
			write_comm = " ";
		}
		else if (backup_type.equals("2"))
		{
			tel_user = " ";
			tel_pwd = " ";
			tel_enpwd = " ";
			tel_encmd = " ";
			hostprompt = " ";
			tel_logonmod = " ";
			read_comm = " ";
			write_comm = " ";
		}
		else if (backup_type.equals("3"))
		{
			tel_user = " ";
			tel_pwd = " ";
			tel_enpwd = " ";
			tel_encmd = " ";
			hostprompt = " ";
			ftp_user = " ";
			ftp_pwd = " ";
			ftp_dir = " ";
			tel_logonmod = " ";
		}
		
		if (action.equals("add"))
		{
		    //检查设备是否已经配置过
		    strSQL = "select device_id from tab_backup_device_test where device_id= '"+device_id
		           +"' and gather_id='"+gather_id + "'";
//		    m_logger.debug("检查设备是否已经添加：" + strSQL);
		    PrepareSQL psql2 = new PrepareSQL(strSQL);
		    cursor = DataSetBean.getCursor(psql2.getSQL());
		    fields = cursor.getNext();
		
		    if (fields != null)
		    {
			  result = "您所配置的设备已经存在！";
			  return result;
		    }
		
		
		   //插如新数据
		    strSQL = "insert into tab_backup_device_test "
			       + "(device_id,backup_type,read_comm,write_comm,"
			       + "tel_logonmod,tel_user,tel_pwd,tel_encmd,tel_enpwd,"
			       + "hostprompt,ftp_user,ftp_pwd,ftp_dir,ftp_filename,"
			       + "if_backup,time_type,time_model,backup_time,gather_id) values('"
			       + device_id +"','"
			       + backup_type + "','"
			       + read_comm + "','"
			       + write_comm + "','"
			       + tel_logonmod + "','"
			       + tel_user + "','"
			       + tel_pwd + "','"
			       + tel_encmd + "','" 
                   + tel_enpwd + "','"
			       + hostprompt + "','"
			       + ftp_user + "','"
			       + ftp_pwd + "','"
			       + ftp_dir + "','"
			       + ftp_filename + "','"
			       + if_backup + "','"
		    	   + time_type +  "','"
		     	   + time_model + "','"
			       + backup_time + "','"
		     	   + gather_id + "')";
		
		}
		else if (action.equals("update"))
		{
			
			strSQL = "update tab_backup_device_test set "
				   + " backup_type='" + backup_type + "',"
				   + " read_comm='" + read_comm + "',"
				   + " write_comm='" + write_comm + "',"
				   + " tel_logonmod='" + tel_logonmod + "',"
				   + " tel_user='" + tel_user + "',"
				   + " tel_pwd='" + tel_pwd + "',"
				   + " tel_encmd='" + tel_encmd + "',"
				   + " tel_enpwd='" + tel_enpwd + "',"
				   + " hostprompt='" + hostprompt + "',"
				   + " ftp_user='" + ftp_user + "',"
				   + " ftp_pwd='" + ftp_pwd + "',"
				   + " ftp_dir='" + ftp_dir + "',"
				   + " ftp_filename='" + ftp_filename + "',"
				   + " if_backup='" + if_backup + "',"
				   + " time_type='" + time_type + "',"
				   + " time_model='" + time_model + "',"
				   + " backup_time='" + backup_time + "',"
				   + " gather_id='" + gather_id + "'"
				   + " where device_id='" + device_id + "'";
		}
		
//        m_logger.debug("配置管理-保存设备信息："+strSQL);
		PrepareSQL psql3 = new PrepareSQL(strSQL);
        
        int row = DataSetBean.executeUpdate(psql3.getSQL());
        if (row > 0)
        {
   	     result = "设备配置信息保存成功！";
        }
        else
        {
   	    result = "设备配置信息保存失败！";
        }
        
        MCCorbaManager mc = new MCCorbaManager(gather_id);
		mc.chnageConf();		
		return result;
	}
	
 /**
  * 获得已配置设备信息列表
  * 
  */	
	public ArrayList getDeviceList()
	{
		Cursor cursor = null;
		String strSQL = null;
		ArrayList list = new ArrayList();
		String backup_type = null;
		String device_model = null;
		String vendor_id = null;
		String loopback_ip = null;
		//add by maym 2007-12-27--------------------------------------------
		String city_id = null;
		String resource_type_id = null;
		//------------------------------------------------------------------
		//--------------------add by maym-----------------
		String ssql = "";                    //该参数用于分页显示数据时确定查询条件
		
		backup_type = request.getParameter("backup_type");
		device_model = request.getParameter("device_model");
		vendor_id = request.getParameter("vendor_id");
		loopback_ip = request.getParameter("loopback_ip");		
		city_id = request.getParameter("city_id");
		resource_type_id = request.getParameter("resource_type_id");
		
		//获得用户登陆域
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();  
		
		
		strSQL = "select a.device_id,a.backup_type,a.if_backup,a.time_type,a.time_model,a.backup_time,b.device_model_id as device_model,b.loopback_ip,b.device_name,a.gather_id "
			   + " from tab_backup_device_test a, tab_gw_device b where a.device_id = b.device_id";
	
		if (backup_type != null && !backup_type.equals("-1"))
		{
			strSQL += " and a.backup_type ='" + backup_type + "'"; 
			//--------------------add by maym-----------------
			ssql += "&backup_type=" + backup_type;
		}
		
		if (loopback_ip != null && !loopback_ip.equals(""))
		{			
			String result = checkDomainDevice(loopback_ip);
			
			if (result.equals("1"))
			{
				strSQL += " and b.loopback_ip ='" + loopback_ip + "'";
				//--------------------add by maym-----------------
				ssql += "&loopback_ip=" + loopback_ip;
			}
			else
			{
				list.add(result);
				return list;
			}
		}
		
		if (vendor_id != null && !vendor_id.equals("-1"))
		{
			strSQL += " and b.vendor_id = '" + vendor_id + "'";
			//--------------------add by maym-----------------
			ssql += "&vendor_id=" +  vendor_id;
		}
		
		if (device_model != null && !device_model.equals("-1"))
		{
			strSQL += " and b.device_model ='" + device_model + "'";
			//--------------------add by maym-----------------
			ssql += "&device_model=" + device_model;		
		}
		
		if ((resource_type_id != null) && !(resource_type_id.equals("-1")))
		{
			strSQL += " and b.resource_type_id=" + resource_type_id;
			ssql += "&resource_type_id=" + resource_type_id;
		}
		
		if ((city_id != null) && !(city_id.equals("-1")))
		{
			strSQL += " and b.city_id in (select city_id from tab_city where city_id='"
		           + city_id + "' or parent_id ='" + city_id + "')" ;
			ssql += "&city_id=" + city_id;
		}
			
		
	    strSQL += " and a.device_id in (select res_id from tab_res_area where area_id="
			  + areaid + " and res_type=1)"
			  + " order by b.device_model";
		
		
		String stroffset = request.getParameter("offset");
		int pagelen = 60;
		int offset;
		
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		QueryPage qryp = new QueryPage();
		qryp.initPage(psql.getSQL(), offset, pagelen);
		cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		String strBar = null;
		
		//----------------modify by maym-------------------		
		if (ssql != null && !ssql.equals(""))
		{
			strBar = qryp.getPageBar(ssql);
		}
		else
		{
			strBar = qryp.getPageBar();
		}
        //--------------------------------------------------

		list.add(strBar);
		list.add(cursor);

		return list;

	}
	
	public void download(HttpServletResponse response,String filename)
	{
		 PropertiesFileManager pf = new PropertiesFileManager("config.properties");
		 String path = pf.getConfigItem("path") + filename;	
		 
	     String filedownload = path;
	     String filedisplay = filename;

	     OutputStream outp = null;
	     FileInputStream in = null;
	     try
	     {
	    	 response.setContentType("application/x-download");
		     filedisplay = URLEncoder.encode(filedisplay,"UTF-8");
		     response.addHeader("Content-Disposition","attachment;filename=" + filedisplay);
	    	 
	    	 outp = response.getOutputStream();
	         in = new FileInputStream(filedownload);

	         byte[] b = new byte[1024];
	         int i = 0;

	         while((i = in.read(b)) > 0)
	         {
	             outp.write(b, 0, i);
	         }
	         outp.flush();
	         
	         if(in != null)
	         {
	             in.close();
	             in = null;
	         }
	         if(outp != null)
	         {
	             outp.close();
	             outp = null;
	         }
	     }
	     catch(Exception e)
	     {
	         m_logger.error("Error!");
	         e.printStackTrace();
	     }finally{
	    	 try {
	    		 if(in!=null){
	    			 in.close();
	    		 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
