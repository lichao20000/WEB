package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.Log;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-5-22
 * @category com.linkage.litms.webtopo
 * 版权：南京联创科技 网管科技部
 *
 */
public class IpBrowserAct
{
	private static Logger logger = LoggerFactory.getLogger(IpBrowserAct.class);
	//加设备信息
	private String addDeviceSQL="insert into tab_gw_device(device_id,oui,device_serialnumber,device_name,loopback_ip,device_status,gather_id,city_id,devicetype_id,port,cpe_currentupdatetime,gw_type,device_model_id)"+
	" values(?,?,?,?,?,1,?,?,?,0,?,2,?)";
	
	private String addDeviceSecuritySQL="insert into sgw_security(device_id,security_username,security_model,security_level,auth_protocol,auth_passwd,privacy_protocol,privacy_passwd,snmp_version,is_enable,snmp_r_passwd)"+
	" values(?,?,?,?,?,?,?,?,?,1,?)";
	
	//装载系统中的所有域信息
	private String getAreaSQL="select distinct area_id,area_pid from tab_area";
	
	//设备型号
//	private String getDeviceTypeSQL="select oui+device_model+softwareversion device_model,devicetype_id from tab_devicetype_info ";
	private String getDeviceTypeSQL="select oui+device_model+softwareversion device_model,devicetype_id " +
			" from tab_devicetype_info a,gw_device_model where a.device_model_id=b.device_model_id";
	//设备域sql
	private String addDeviceAreaSQL="insert into tab_gw_res_area(res_type,res_id,area_id) values(1,?,?)";
	
	//根据oui+device_serialnumber看是否能查询到设备
	private String selectDeviceSQL="select count(1) num from tab_gw_device where oui=? and device_serialnumber=?";
	
	private PrepareSQL pSQL = null;
	
	public IpBrowserAct()
	{
		pSQL = new PrepareSQL();
	}
	
	/**
	 * 获取设备型号map
	 * @return
	 */
	private HashMap getDeviceTypeMap()
	{ 
		PrepareSQL psql = new PrepareSQL(getDeviceTypeSQL);
		psql.getSQL();
		return DataSetBean.getMap(getDeviceTypeSQL);
	}
	
	/**
	 * 获取系统内的域map
	 * @return
	 */
	private HashMap getAreaMap()
	{
		PrepareSQL psql = new PrepareSQL(getAreaSQL);
		psql.getSQL();
		return DataSetBean.getMap(getAreaSQL);
	}	
	
	
	/**
	 * 给设备权限控制表中插数据
	 * @param area_id
	 * @param device_id
	 * @param areaMap
	 * @return
	 */
	private ArrayList  addDeviceAreaSQL(long area_id,String device_id,HashMap areaMap)
	{
		int num=0;
		ArrayList list = new ArrayList();
		if(1!=area_id)
		{
			pSQL.setSQL(addDeviceAreaSQL);
			pSQL.setString(1,device_id);
			pSQL.setLong(2,area_id);
			list.add(pSQL.getSQL());
			String pid=(String)areaMap.get(String.valueOf(area_id));
			//父域存在继续给这个设备加权限
			while(null!=areaMap.get(pid)&&!"1".equals(pid))
			{
				area_id =Long.parseLong(pid);
				pSQL.setSQL(addDeviceAreaSQL);
				pSQL.setString(1,device_id);
				pSQL.setLong(2,area_id);
				list.add(pSQL.getSQL());
				pid=(String)areaMap.get(String.valueOf(area_id));			
			}
			
		}
		
		return list;
	}	
	
	
	/**
	 * 返回操作结果，0：成功；-1:数据库操作失败；-2:参数错误,-3:通知后台失败；-4;后台操作失败 ,-5:设备在系统中存在或系统不支持此设备
	 * @param request
	 * @return
	 */
	public int addDevices(HttpServletRequest request)
	{
		String pid = request.getParameter("pid");
		String[] deviceInfos = request.getParameterValues("deviceinfo");
		if(null==deviceInfos||0==deviceInfos.length)
		{
			return -2;
		}		
		//设备入库sql队列
		ArrayList sqlList = new ArrayList();				
		//域map
		HashMap areaMap = getAreaMap();	
		ArrayList addDeviceList= new ArrayList();
		
		HttpSession session =request.getSession();
		UserRes curUser = (UserRes)session.getAttribute("curUser");	
		User user =curUser.getUser();
		WebCorbaInst webCorb = new WebCorbaInst("db");
		String device_id_str=webCorb.getDeviceSerial(curUser.getUser(),deviceInfos.length);
		long device_id=Long.parseLong(device_id_str);
		String oui="";
		String device_serialnumber="";
		//String device_name="";	
		String gather_id="";		
		String device_ip="";
		String device_model="";	
		String softwareversion="";
		String SecUsername="";
		String SecModel="";
		String SecurityLevel="";
		String AuthProtocol="";
		String AuthPassword="";
		String PrivProtocol="";
		String PrivPassword="";
		String device_type_id="";
		String device_model_id="";
		String snmpVersion="3";
		String community="public";
		HashMap map =null;
		
		for(int i=0;i<deviceInfos.length;i++)
		{
			logger.debug("deviceInfos["+i+"]:"+deviceInfos[i]);
			oui=deviceInfos[i].split("\\|\\|")[0];
			device_serialnumber=deviceInfos[i].split("\\|\\|")[1];
			device_ip=deviceInfos[i].split("\\|\\|")[2];
			//device_name=deviceInfos[i].split("\\|\\|")[3];
			device_model=deviceInfos[i].split("\\|\\|")[3];			
			gather_id=deviceInfos[i].split("\\|\\|")[4];
			SecUsername=deviceInfos[i].split("\\|\\|")[5];
			SecModel=deviceInfos[i].split("\\|\\|")[6];
			SecurityLevel=deviceInfos[i].split("\\|\\|")[7];
			AuthProtocol=deviceInfos[i].split("\\|\\|")[8];
			AuthPassword=deviceInfos[i].split("\\|\\|")[9];
			PrivProtocol=deviceInfos[i].split("\\|\\|")[10];
			PrivPassword=deviceInfos[i].split("\\|\\|")[11];
			snmpVersion=deviceInfos[i].split("\\|\\|")[12];
			community=deviceInfos[i].split("\\|\\|")[13];
			softwareversion=deviceInfos[i].split("\\|\\|")[14];
			logger.debug("oui:"+oui);
			logger.debug("device_serialnumber:"+device_serialnumber);
			logger.debug("device_ip:"+device_ip);
			//logger.debug("device_name："+device_name);
			logger.debug("device_model："+device_model);
			logger.debug("gather_id："+gather_id);
			logger.debug("SecUsername："+SecUsername);
			logger.debug("SecModel："+SecModel);
			logger.debug("SecurityLevel："+SecurityLevel);
			logger.debug("AuthProtocol："+AuthProtocol);
			logger.debug("AuthPassword："+AuthPassword);
			logger.debug("PrivProtocol："+PrivProtocol);
			logger.debug("PrivPassword："+PrivPassword);
			logger.debug("community："+community);
			logger.debug("snmpVersion："+snmpVersion);
			logger.debug("softwareversion："+softwareversion);		
			if("null".equalsIgnoreCase(oui)||"null".equalsIgnoreCase(device_serialnumber)
					||"null".equalsIgnoreCase(device_model)||"null".equalsIgnoreCase(device_ip)
					||"null".equalsIgnoreCase(softwareversion)||"null".equalsIgnoreCase(snmpVersion)
					||("null".equalsIgnoreCase(SecurityLevel)&&"null".equalsIgnoreCase(community)))
			{
				Log.writeLog("设备(oui:"+oui+"   serialnumber:"+device_serialnumber
						+"    device_model:"+device_model+"    device_ip:"+device_ip+"    snmpVersion:"+snmpVersion
						+"   SecurityLevel:"+SecurityLevel+"  community:"+community+")必要参数不足","web.log");
				continue;
			}
			//判断设备是否存在，如存在，则不做操作
			pSQL.setSQL(selectDeviceSQL);
			pSQL.setString(1,oui);
			pSQL.setString(2,device_serialnumber);
			map =DataSetBean.getRecord(pSQL.getSQL());			
			if(null!=map&&Long.parseLong((String)map.get("num"))>0)
			{
				Log.writeLog("设备(oui:"+oui+"   serialnumber:"+device_serialnumber+")已存在系统中","web.log");
				continue;
			}			
			
			device_type_id =DeviceAct.getDeviceTypeID(oui,"",device_model,"","",softwareversion);
			device_model_id=DeviceAct.getDeviceModelID(oui,device_model);
			if("-1".equals(device_type_id)||"-1".equals(device_model_id))
			{
				Log.writeLog("设备(oui:"+oui+"   serialnumber:"+device_serialnumber+" softwareversion:"
						+softwareversion+")没有得到对应的device_type_id、device_model_id！ ","web.log");
				continue;
			}
			//增加设备
			pSQL.setSQL(addDeviceSQL);
			pSQL.setString(1,String.valueOf(device_id));
			pSQL.setString(2,oui);
			pSQL.setString(3,device_serialnumber);
			pSQL.setString(4,oui+"-"+device_serialnumber);
			pSQL.setString(5,device_ip);
			pSQL.setString(6,gather_id);
			pSQL.setString(7,user.getCityId());
			pSQL.setStringExt(8,device_type_id,false);
			pSQL.setLong(9,new Date().getTime()/1000);	
			pSQL.setStringExt(10,device_model_id,true);
			sqlList.add(pSQL.getSQL());
			
			//加设备安全认证的SQL
			pSQL.setSQL(addDeviceSecuritySQL);
			pSQL.setString(1,String.valueOf(device_id));
			//v1或v2版本得情况下,v3参数都为null
			if("1".equals(snmpVersion)||"2".equals(snmpVersion))
			{
				pSQL.setStringExt(2,null,false);
				pSQL.setStringExt(3,null,false);
				pSQL.setStringExt(4,null,false);
				pSQL.setStringExt(5,null,false);
				pSQL.setStringExt(6,null,false);
				pSQL.setStringExt(7,null,false);
				pSQL.setStringExt(8,null,false);				
				
			}
			else
			{
				if("null".equalsIgnoreCase(SecUsername))
				{
					pSQL.setStringExt(2,null,false);
				}
				else
				{
					pSQL.setString(2,SecUsername);
				}			
				pSQL.setStringExt(3,"3",false);
				pSQL.setStringExt(4,SecurityLevel,false);
				if("null".equalsIgnoreCase(AuthProtocol))
				{
					pSQL.setStringExt(5,null,false);
				}
				else
				{
					pSQL.setStringExt(5,AuthProtocol,true);
				}
				if("null".equalsIgnoreCase(AuthPassword))
				{
					pSQL.setStringExt(6,null,false);
				}
				else
				{
					pSQL.setStringExt(6,AuthPassword,true);
				}			
				if(null!=PrivProtocol&&!"".equals(PrivProtocol.trim())&&"null".equalsIgnoreCase(PrivProtocol))
				{
					pSQL.setStringExt(7,PrivProtocol,true);				
				}
				else
				{
					pSQL.setStringExt(7,null,false);
				}
				if(null!=PrivPassword&&!"".equals(PrivPassword.trim())&&"null".equalsIgnoreCase(PrivPassword))
				{
					pSQL.setStringExt(8,PrivPassword,true);				
				}
				else
				{
					pSQL.setStringExt(8,null,false);
				}				
			}
			pSQL.setString(9,"v"+snmpVersion);
			if("null".equalsIgnoreCase(community))
			{
				pSQL.setStringExt(10,null,false);
			}
			else
			{
				pSQL.setString(10,community);
			}			
			sqlList.add(pSQL.getSQL());
			sqlList.addAll(addDeviceAreaSQL(user.getAreaId(),String.valueOf(device_id),areaMap));				
			addDeviceList.add(String.valueOf(device_id));
			device_id++;
		}
		
		//clear
		areaMap =null;		
		
		//有增加的设备，才需要操作数据库，并通知MC
		if(addDeviceList.size()>0)
		{			
			int[] resultCode=DataSetBean.doBatch(sqlList);
			if(null!=resultCode)
			{
				Scheduler  scheduler = new Scheduler ();
				String[] device_ids = new String[addDeviceList.size()];
				addDeviceList.toArray(device_ids);
				try
				{
					int length=scheduler.importTopo(String.valueOf(user.getAreaId()),device_ids,pid,0,0);
					//MC执行不成功
					if(length!=device_ids.length)
					{
						return -4;
					}				
				}
				catch(Exception e)
				{
					return -3;
				}			
				
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -5;
		}
		
		return 0;
		
	}
}
