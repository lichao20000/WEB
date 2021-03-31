package com.linkage.litms.common.corba.interfacecontrol;

import java.util.HashMap;

import org.omg.CORBA.StringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class FluxManagerInterface extends BaseInterface
{
	private static Logger log = LoggerFactory.getLogger(FluxManagerInterface.class);
	static protected FluxManagerInterface Instance = null;

	private FluxManagerInterface()
	{
		SetProcessName("FluxManager");
		InitProcessList();
	}

	static public FluxManagerInterface GetInstance()
	{
		if (Instance == null)
		{
			Instance = new FluxManagerInterface();
		}
		return Instance;
	}

	public boolean readDevices(String[] IDList)
	{
		log.debug("begin readDevices size:"+IDList.length+"  "+new DateTimeUtil().getLongDate());
		if (IDList.length == 0)
			return false;
		for (int i = 0; i < IDList.length; i++)
		{
			HashMap deviceInfo = getDeviceInfo(IDList[i]);
			if (deviceInfo == null)
				continue;
			GatherProcess process = SearchProcessByObject(deviceInfo);
			if (process == null)
				continue;
			if (process.Manager == null)
			{
				process.InitManager();
			}

			try
			{
				//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
				
				//String[] IpList = new String[1];
				//IpList[0] = (String) deviceInfo.get("loopback_ip");
				String[] IdList = new String[1];
				IdList[0] = IDList[i];
				
				log.debug("begin readDevices to FluxManager device_id:"+IDList[i]+"  "+new DateTimeUtil().getLongDate());
				((FluxControl.FluxDeviceConf) process.Manager).readDevices(IdList);
				log.debug("end readDevices to FluxManager device_id:"+IDList[i]+"  "+new DateTimeUtil().getLongDate());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				
				//重绑
				if (process.Manager == null)
				{
					process.InitManager();
				}

				try
				{
					//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
					
					//String[] IpList = new String[1];
					//IpList[0] = (String) deviceInfo.get("loopback_ip");
					String[] IdList = new String[1];
					IdList[0] = IDList[i];
					
					log.debug("begin readDevices to FluxManager two device_id:"+IDList[i]+"  "+new DateTimeUtil().getLongDate());
					((FluxControl.FluxDeviceConf) process.Manager).readDevices(IdList);
					log.debug("end readDevices to FluxManager two device_id:"+IDList[i]+"  "+new DateTimeUtil().getLongDate());
				}
				catch (Exception e1)
				{
					log.error("readDevices device_id:"+IDList[i]);
					//e.printStackTrace();
					process.Manager = null;
				}
			}
		}
		log.debug("end readDevices size:"+IDList.length+"  "+new DateTimeUtil().getLongDate());
		return true;
	}

	public boolean readPorts(String device_id, String[] plist)
	{
		HashMap deviceInfo = getGWDeviceInfo(device_id);
		if (deviceInfo == null)
			return false;
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null)
			return false;
		if (process.Manager == null)
		{
			process.InitManager();
		}
		//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
		
		//String device_ip = (String) deviceInfo.get("loopback_ip");
		try
		{
			((FluxControl.FluxDeviceConf) process.Manager).readPorts(device_id, plist);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			process.Manager = null;
		}
		return true;
	}

	public FluxControl.FluxData[] getPortData(String device_id,
					StringHolder collecttime)
	{
		log.debug(new DateTimeUtil().getLongDate()+"  begin getPortData from web device_id:"+device_id);
		collecttime.value = "0";
		HashMap deviceInfo = getGWDeviceInfo(device_id);
		if (deviceInfo == null)
			return new FluxControl.FluxData[0];		
		
		GatherProcess process = SearchProcessByObject(deviceInfo);
		if (process == null)
		{			
			return new FluxControl.FluxData[0];
		}
		if (process.Manager == null)
		{
			process.InitManager();
		}
		// 如果还是为null
		if (process.Manager == null)
		{			
			return new FluxControl.FluxData[0];
		}
		
		//String device_ip = (String) deviceInfo.get("loopback_ip");
		
		FluxControl.FluxData[] FluxDataList;
		try
		{
			log.debug(new DateTimeUtil().getLongDate()+"  begin getPortData from Fluxmanager device_id:"+device_id);
			FluxDataList = ((FluxControl.FluxDeviceConf) process.Manager)
							.getPortData(device_id, collecttime);	
			log.debug(new DateTimeUtil().getLongDate()+"  end getPortData from Fluxmanager device_id:"+device_id+"   size:"+FluxDataList.length);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			FluxDataList = new FluxControl.FluxData[0];
			process.Manager = null;
		}
		log.debug(new DateTimeUtil().getLongDate()+"  end getPortData from web device_id:"+device_id+"  "+FluxDataList.length);
		
		return FluxDataList;
	}	

	public FluxControl.FluxPortData[] getPortsFluxData(
					FluxControl.FluxPort[] portlist, StringHolder collecttime)
	{
		collecttime.value = "0";
		FluxControl.FluxPortData[] returnDataList = new FluxControl.FluxPortData[portlist.length];
		for (int i = 0; i < portlist.length; i++)
		{
			String device_id = portlist[i].deviceip;
			HashMap deviceInfo = getGWDeviceInfo(device_id);
			if (deviceInfo == null)
				return new FluxControl.FluxPortData[0];

			GatherProcess process = SearchProcessByObject(deviceInfo);
			if (process == null)
				return new FluxControl.FluxPortData[0];
			if (process.Manager == null)
			{
				process.InitManager();
			}
			try
			{
				FluxControl.FluxPort[] flux_portlist = new FluxControl.FluxPort[1];
				flux_portlist[0] = new FluxControl.FluxPort();
				flux_portlist[0].deviceip = (String)deviceInfo.get("loopback_ip");
				flux_portlist[0].descget = portlist[i].descget;
				flux_portlist[0].wayget = portlist[i].wayget;
				FluxControl.FluxPortData[] FluxPortDataList = ((FluxControl.FluxDeviceConf) process.Manager)
								.getPortsFluxData(flux_portlist, collecttime);	
				returnDataList[i] = new FluxControl.FluxPortData();
                returnDataList[i].deviceip = FluxPortDataList[0].deviceip;
                returnDataList[i].ifindex = FluxPortDataList[0].ifindex;
                returnDataList[i].ifdescr = FluxPortDataList[0].ifdescr;
                returnDataList[i].ifname = FluxPortDataList[0].ifname;
                returnDataList[i].ifnamedefined = FluxPortDataList[0].
                                                  ifnamedefined;
                returnDataList[i].ifportip = FluxPortDataList[0].ifportip;
                returnDataList[i].ifinoctetsbps = FluxPortDataList[0].
                                                  ifinoctetsbps;
                returnDataList[i].ifoutoctetsbps = FluxPortDataList[0].
                        ifoutoctetsbps;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				returnDataList = new FluxControl.FluxPortData[0];
				process.Manager = null;
			}
		}
		return returnDataList;
	}

}
