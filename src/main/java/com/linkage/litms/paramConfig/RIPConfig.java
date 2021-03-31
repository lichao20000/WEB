package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.webtopo.snmpgather.ReadDevicePort;
import com.linkage.litms.webtopo.snmpgather.SnmpDataGather;
import com.linkage.module.gwms.Global;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-6-12
 * @category com.linkage.litms.paramConfig
 * 版权：南京联创科技 网管科技部
 *
 */
public class RIPConfig
{
	private static Logger log = LoggerFactory.getLogger(RIPConfig.class);
		
	/**
	 * 在原有的端口信息基础上加上RIP功能、及版本信息的查看
	 * @return
	 */
	public ArrayList getDeviceInfo(String device_id)
	{
		log.debug("begin RIPConfig_getDeviceInfo  "+new DateTimeUtil().getLongDate());
		ArrayList list = new ArrayList();
		String[] portArray=null;
		String[] tempPortArray=null;
		ArrayList oidList = new ArrayList();
		HashMap ripMap = new HashMap();
		HashMap ripVersionMap = new HashMap();
		ReadDevicePort devicePort = new ReadDevicePort();
		devicePort.setDevice_ID(device_id);
		ArrayList tempList=devicePort.getDeviceInfo();
		//端口信息不为空的情况下，补充RIP信息
		if(null!=tempList&&0!=tempList.size())
		{
			 HashMap dataMap = null;
			//采集RIP功能
			String oid =getOID(device_id,361);			
			if(!"".equals(oid))
			{
				oidList.add(oid);
				SnmpDataGather sdg = new SnmpDataGather();
				sdg.setDevice(device_id);
				sdg.setIndexData("");			    
			    sdg.setOidData(oidList);
			    log.debug("begin get RIP功能 oid:"+oid +"   "+new DateTimeUtil().getLongDate());
			    dataMap = sdg.getDataList();
			    log.debug("end get RIP功能   oid:"+oid +"  result_size:"+dataMap.size()+"   "+new DateTimeUtil().getLongDate());			    
			    if(0!=dataMap.size())
			    {
			    	 Performance.Data[] DataList =(Performance.Data[])dataMap.get(oid);
					 for(int i=0;i<DataList.length;i++)
					 {
					    ripMap.put(DataList[i].index,DataList[i].dataStr);
					 }		
			    }
			   	    
			}
			
			oidList.clear();
			
			//采集RIP版本
			oid =getOID(device_id,362);
			if(!"".equals(oid))
			{
				oidList.add(oid);
				SnmpDataGather sdg = new SnmpDataGather();
				sdg.setDevice(device_id);
				sdg.setIndexData("");			    
			    sdg.setOidData(oidList);
			    log.debug("begin get RIP版本 oid:"+oid +"   "+new DateTimeUtil().getLongDate());
			    dataMap = sdg.getDataList();
			    log.debug("end get RIP版本   oid:"+oid +"  result_size:"+dataMap.size()+"   "+new DateTimeUtil().getLongDate());
			    if(0!=dataMap.size())
			    {
			    	 Performance.Data[] DataList =(Performance.Data[])dataMap.get(oid);
					 for(int i=0;i<DataList.length;i++)
					 {
					    ripVersionMap.put(DataList[i].index,DataList[i].dataStr);
					 }		
			    }
			   	    
			}
			
			//整理端口列表标题
			tempPortArray =(String[])tempList.get(0);
			portArray = new String[tempPortArray.length+2];
			for(int i=0;i<tempPortArray.length;i++)
			{
				portArray[i]=tempPortArray[i];
			}
			portArray[tempPortArray.length]="RIP功能";
			portArray[tempPortArray.length+1]="RIP版本";
			list.add(portArray);
			
			//整理端口信息
			for(int i=1;i<tempList.size();i++)
			{
				tempPortArray=(String[])tempList.get(i);
				portArray = new String[tempPortArray.length+2];
				int j=0;
				for(j=0;j<tempPortArray.length;j++)
				{
					if("".equals(tempPortArray[j])
							||"NODATA".equalsIgnoreCase(tempPortArray[j])
							||"ERROR".equalsIgnoreCase(tempPortArray[j]))
					{
						portArray[j]="";
					}
					else
					{
						portArray[j]=tempPortArray[j];
					}
				}
				
				//tempPortArray最后一列是端口ip
				if(!"".equals(portArray[j-1]))
				{
					if(ripMap.containsKey(portArray[j-1]))
					{
						portArray[j]=(String)ripMap.get(portArray[j-1]);
						if("".equals(portArray[j])
								||"NODATA".equalsIgnoreCase(portArray[j])
								||"ERROR".equalsIgnoreCase(portArray[j]))
						{
							portArray[j]="未配置";
						}
						else
						{
							portArray[j]="配置";
						}
					}
					else
					{
						portArray[j]="未配置";
					}
				}
				else
				{
					portArray[j]="";
				}
				
				//准备RIP版本				
				if(!"".equals(portArray[j-1])
						&&ripVersionMap.containsKey(portArray[j-1]))
				{
					portArray[j+1]=(String)ripVersionMap.get(portArray[j-1]);
					if("".equals(portArray[j+1])
							||"NODATA".equalsIgnoreCase(portArray[j+1])
							||"ERROR".equalsIgnoreCase(portArray[j+1]))
					{
						portArray[j+1]="";
					}						
				}
				else
				{
					portArray[j+1]="";
				}
				
				list.add(portArray);				
			}
		}
		
		//clear
		ripMap=null;
		ripVersionMap =null;
		oidList=null;
		tempPortArray=null;
		portArray =null;
		
		log.debug("end RIPConfig_getDeviceInfo  "+new DateTimeUtil().getLongDate());
		return list;
		
	}
	
	
	/**
	 * 返回这个设备是否配置了认证口令，就是sgw_security表有没有这个设备的认证口令
	 * @return 有认证口令，则返回true；否则返回false
	 */
	public boolean isConfigSnmp(String device_id)
	{
		String sql="select * from sgw_security where device_id='"+device_id+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql="select device_id from sgw_security where device_id='"+device_id+"'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		HashMap record=DataSetBean.getRecord(sql);
		return record==null?false:true;
	}
	
	
	/**
	 * 获取某个设备的某个类型的oid
	 * @param device_id
	 * @param oid_type
	 * @return
	 */
	public String getOID(String device_id,int oid_type)
	{
		String oid="";
		String sql="select b.oid from tab_gw_device a,tab_gw_model_oper_oid b where a.device_model_id=b.device_model and b.oid_type="
			+oid_type+"  and a.device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap record =DataSetBean.getRecord(sql);
		if(null!=record)
		{
			oid = (String)record.get("oid");
		}
		return oid;		
	}
	
	
}
