package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wsdl.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;

public class ServerConfigServImpl implements ServerConfigServ {
	
	private static Logger logger = LoggerFactory.getLogger(ServerConfigServImpl.class);
	
	//DHCP server配置模版参数
	private String LANHostConfigManagement = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.";
	private String DHCPServerConfigurable = "DHCPServerConfigurable";
	private String DHCPServerEnable ="DHCPServerEnable";
	private String DHCPRelay ="DHCPRelay";
	private String MinAddress  = "MinAddress";
	private String MaxAddress  = "MaxAddress";
	private String ReservedAddresses = "ReservedAddresses";
	private String SubnetMask  = "SubnetMask";
	private String DNSServers = "DNSServers";
	private String DHCPLeaseTime = "DHCPLeaseTime";
	
	//private String DomainName = "DomainName";
	//private String IPRouters = "IPRouters";
	//private String IPInterfaceNumberOfEntries  = "IPInterfaceNumberOfEntries";
	//private String DHCPConditionalPoolNumberOfEntries   = "DHCPConditionalPoolNumberOfEntries";
	
	//维护账户修改
	private String AdminName = "InternetGatewayDevice.X_CU_Function.Web.AdminName";
	private String AdminPassword="InternetGatewayDevice.X_CU_Function.Web.AdminPassword";
	//IGMP
	private String IGMPSnoopingEnable="InternetGatewayDevice.X_CU_Function.IGMP.IGMPSnoopingEnable";
	//ALG
	private String SIPEnable ="InternetGatewayDevice.X_CU_Function.ALG.SIPEnable";
	private String FTPEnable = "InternetGatewayDevice.X_CU_Function.ALG.FTPEnable";
	//WLAN
	private String  Enable="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.Enable";
	//DHCP server config template
	public String doConfigAll(String serverConfig, String isOpenDHCP,
			String isRelay, String minAddress, String maxAddress,
			String reservedAddresses, String subnetMask, String dNSServers,
			String dHCPLeaseTime,String deviceId, String gw_type) {
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		ACSCorba corba = new ACSCorba(gw_type);
		
		objList.add(new ParameValueOBJ(LANHostConfigManagement+DHCPServerConfigurable,serverConfig,"4"));
		objList.add(new ParameValueOBJ(LANHostConfigManagement+DHCPServerEnable,isOpenDHCP,"4"));
		objList.add(new ParameValueOBJ(LANHostConfigManagement+DHCPRelay,isRelay,"4"));
		if(!StringUtil.IsEmpty(minAddress)){
			objList.add(new ParameValueOBJ(LANHostConfigManagement+MinAddress,minAddress,"1"));
		}
		if(StringUtil.IsEmpty(reservedAddresses)){
			objList.add(new ParameValueOBJ(LANHostConfigManagement+MaxAddress,maxAddress,"1"));
		}
		if(StringUtil.IsEmpty(reservedAddresses)){
			objList.add(new ParameValueOBJ(LANHostConfigManagement+ReservedAddresses,reservedAddresses,"1"));
		}
		if(StringUtil.IsEmpty(subnetMask)){
			objList.add(new ParameValueOBJ(LANHostConfigManagement+SubnetMask,subnetMask,"1"));
		}
		if(StringUtil.IsEmpty(dNSServers)){
			objList.add(new ParameValueOBJ(LANHostConfigManagement+DNSServers,dNSServers,"1"));
		}
		if(StringUtil.IsEmpty(dHCPLeaseTime)){
			objList.add(new ParameValueOBJ(LANHostConfigManagement+DHCPLeaseTime,dHCPLeaseTime,"2"));
		}
		int retResult = corba.setValue(deviceId, objList);
		if (retResult != 1)
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(retResult);
			if (null == obj)
			{
				return Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return obj.getFaultDesc();
			}
		}else{
			logger.warn("DHCP Server配置节点下发成功！");
			return "DHC ServerP配置节点下发成功！";
		}
	}
	//maintaince accounts config template
	public String maintainAccConifig(String deviceId, String gw_type,
			String adminName, String adminPassword) {
		ACSCorba corba = new ACSCorba(gw_type);
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(new ParameValueOBJ(AdminName,adminName,"1"));
		objList.add(new ParameValueOBJ(AdminPassword,adminPassword,"1"));
		
		int retResult = corba.setValue(deviceId, objList);
		if (retResult != 1)
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(retResult);
			if (null == obj)
			{
				return Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return obj.getFaultDesc();
			}
		}else{
			logger.warn("维护帐号配置节点下发成功！");
			return "维护帐号配置节点下发成功！";
		}
	}
	//IGMP configuration
	public String igmpConifig(String deviceId, String gw_type,
			String iGMPSnoopingEnable) {
		ACSCorba corba = new ACSCorba(gw_type);
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(new ParameValueOBJ(IGMPSnoopingEnable,iGMPSnoopingEnable,"4"));
		
		int retResult = corba.setValue(deviceId, objList);
		if (retResult != 1)
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(retResult);
			if (null == obj)
			{
				return Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return obj.getFaultDesc();
			}
		}else{
			logger.warn("IGMP配置节点下发成功！");
			return "IGMP配置节点下发成功！";
		}
	}
	//alg config 
	@Override
	public String algConifig(String deviceId, String gw_type, String ftpEnable,
			String sipEnable) {
		ACSCorba corba = new ACSCorba(gw_type);
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		
		objList.add(new ParameValueOBJ(FTPEnable,ftpEnable,"4"));
		objList.add(new ParameValueOBJ(SIPEnable,sipEnable,"4"));
		int retResult = corba.setValue(deviceId, objList);
		if (retResult != 1)
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(retResult);
			if (null == obj)
			{
				return Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return obj.getFaultDesc();
			}
		}else{
			logger.warn("ALG配置节点下发成功！");
			return "ALG配置节点下发成功！";
		}
	}
	//wlan config 
	public String wlanConfig(String deviceId, String gw_type, String wlanEnable) {
		ACSCorba corba = new ACSCorba(gw_type);
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		
		objList.add(new ParameValueOBJ(Enable,wlanEnable,"4"));
		int retResult = corba.setValue(deviceId, objList);
		if (retResult != 1)
		{
			CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(retResult);
			if (null == obj)
			{
				return Global.G_Fault_Map.get(100000).getFaultDesc();
			}
			else
			{
				if (null == obj.getFaultDesc())
				{
					return Global.G_Fault_Map.get(100000).getFaultDesc();
				}
				return obj.getFaultDesc();
			}
		}else{
			logger.warn("WLAN配置节点下发成功！");
			return "WLAN配置节点下发成功！";
		}
	}
	

}
