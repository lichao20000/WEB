package com.linkage.litms.common.corba.interfacecontrol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import I_Ip_Check.Device_Info;
import I_Ip_Check.Snmp_Password;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-5-22
 * @category com.linkage.litms.common.corba.interfacecontrol
 * 版权：南京联创科技 网管科技部
 *
 */
public class IPcheckInterface extends BaseInterface
{
	private static Logger log = LoggerFactory.getLogger(IPcheckInterface.class);
	
	static protected IPcheckInterface Instance = null;
	private IPcheckInterface()
	  {
	    SetProcessName("IpCheck");
	    InitProcessList();
	  }
	  static public IPcheckInterface GetInstance()
	  {
	    if(Instance == null)
	     {
	       Instance = new IPcheckInterface();
	     }
	     return Instance;
	  }
	  
	  
	  /**
	   * 根据起始IP，终止IP来发现某个采集点上得所有设备信息
	   * @param startIp
	   * @param endIp
	   * @param communitty_List
	   * @param gather_id
	   * @return
	   */
	  public Device_Info[] I_IpBrowser(String startIp,String endIp,Snmp_Password[]communitty_List,String gather_id)
	  {
		  Device_Info[] devicesArray=null;
		  log.debug("begin I_IpBrowser startIp:"+startIp+"  endIp:"+endIp+" communitty_List_size:"+communitty_List.length+"  gather_id:"+gather_id);
		  GatherProcess process = null;
		  process = SearchProcessByGatherID(gather_id);
		  if (process == null)
		  {
			  log.debug("没有找到gather_id("+gather_id+")程序");
			  return new Device_Info[0];
		  }
		  if(process.Manager==null)
		  {
			  process.InitManager();
		  }		  
		  try
		  {
			  log.debug("begin one I_IpBrowser to  Ip_check");
			  devicesArray = ((I_Ip_Check.I_ObjectManager) process.Manager).I_IpBrowser(startIp,endIp,communitty_List);
			  log.debug("end one I_IpBrowser to  Ip_check  "+devicesArray.length);
		  }
		  catch(Exception e)
		  {
			  process.print();
			  e.printStackTrace();
			  process.Manager=null;
			  if(process.Manager==null)
			  {
				  process.InitManager();
			  }	
			  try
			  {
				  log.debug("begin two I_IpBrowser to  Ip_check");
				  devicesArray = ((I_Ip_Check.I_ObjectManager) process.Manager).I_IpBrowser(startIp,endIp,communitty_List);
				  log.debug("end two I_IpBrowser to  Ip_check  "+devicesArray.length);
			  }
			  catch(Exception e1)
			  {
				  e1.printStackTrace();
				  devicesArray = new Device_Info[0];
			  }
		  }	  
	          
		  log.debug("end I_IpBrowser startIp:"+startIp+"  endIp:"+endIp+" communitty_List_size:"+communitty_List.length+"   resulet_size:"+devicesArray.length);
		  
		  return devicesArray;		  
	  }
}
