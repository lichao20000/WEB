<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.IPcheckInterface"%>
<%@ page import="I_Ip_Check.Device_Info"%>
<%@ page import="com.linkage.litms.common.util.Encoder"%>
<%@ page import="I_Ip_Check.Snmp_Password"%>
<%@ page import="I_Ip_Check.Interface_Info"%>
<%@ page import="bio.hgwip.IpTool"%>
<% 
  String comm = request.getParameter("comm");  
  String startip =  request.getParameter("startip");
  String endip =  request.getParameter("endip");
  String subnet = request.getParameter("segment");
  String inetmask=request.getParameter("inetmask");
  //用户使用网段来发现设备
  if(null!=subnet&&!"".equals(subnet.trim()))
  {
	startip=IpTool.getLowAddr(subnet,Integer.parseInt(inetmask));
	endip=IpTool.getHighAddr(subnet,Integer.parseInt(inetmask));
  }
  String gather_id =  request.getParameter("gather_id");
  String[] commStr=comm.split(",");  
  Snmp_Password[] snmpPassword = new Snmp_Password[commStr.length];
  for(int i=0;i<commStr.length;i++)
  {
	snmpPassword[i] = new Snmp_Password();
	snmpPassword[i].m_strSecurityLevel=commStr[i].split("\\|\\|")[0];	
	if("null".equals(snmpPassword[i].m_strSecurityLevel))
	{
		snmpPassword[i].m_strSecurityLevel="";
		snmpPassword[i].m_strSecUsername="";
		snmpPassword[i].m_strAuthProtocol="";
		snmpPassword[i].m_strAuthPassword="";
		snmpPassword[i].m_strPrivProtocol="";
		snmpPassword[i].m_strPrivPassword="";
		snmpPassword[i].m_strSecModel="";	
	}
	else
	{
		snmpPassword[i].m_strSecUsername=commStr[i].split("\\|\\|")[1];
		if("null".equals(snmpPassword[i].m_strSecUsername))
		{
			snmpPassword[i].m_strSecUsername="";
		}
		snmpPassword[i].m_strAuthProtocol=commStr[i].split("\\|\\|")[2];
		if("null".equals(snmpPassword[i].m_strAuthProtocol))
		{
			snmpPassword[i].m_strAuthProtocol="";
		}
		snmpPassword[i].m_strAuthPassword=commStr[i].split("\\|\\|")[3];
		if("null".equals(snmpPassword[i].m_strAuthPassword))
		{
			snmpPassword[i].m_strAuthPassword="";
		}
		snmpPassword[i].m_strPrivProtocol=commStr[i].split("\\|\\|")[4];
		if("null".equals(snmpPassword[i].m_strPrivProtocol))
		{
			snmpPassword[i].m_strPrivProtocol="";
		}
		snmpPassword[i].m_strPrivPassword=commStr[i].split("\\|\\|")[5];
		if("null".equals(snmpPassword[i].m_strPrivPassword))
		{
			snmpPassword[i].m_strPrivPassword="";
		}
		snmpPassword[i].m_strSecModel="3";
	}
	snmpPassword[i].m_strCommunity=commStr[i].split("\\|\\|")[6]; 
	if("null".equals(snmpPassword[i].m_strCommunity))
	{
		snmpPassword[i].m_strCommunity="";
	}
  }
  
  //for(int i=0;i<snmpPassword.length;i++)
  //{
	  
  //}
  
  Device_Info[] devices= IPcheckInterface.GetInstance().I_IpBrowser(startip,endip,snmpPassword,gather_id); 
%>
<div id="devicedata">
<table border=0 cellspacing=1 cellpadding=2 width="95%" bgcolor=#999999>
				<tr>
					<th>OUI</th>
					<th>设备序列号</th>
					<th>设备IP</th>					
					<th>设备型号</th>	
					<th>软件版本</th>					
				</tr>
				<%
				  if(null==devices||0==devices.length)
				  {
					  out.println("<tr bgcolor=\"#FFFFFF\"><td align=center colspan=5>没有发现对应得设备</td></tr>");					 
				  }
				  else
				  {
					  String style="bgcolor=\"red\"";
					  String str="";
					  for(int i=0;i<devices.length;i++)
					  {
						if(devices[i].Oui==null||"".equals(devices[i].Oui))
						{
							devices[i].Oui="null";
						}
						
						if(devices[i].m_DeviceSerialNum==null||"".equals(devices[i].m_DeviceSerialNum))
						{
							devices[i].m_DeviceSerialNum="null";
						}

						if(devices[i].m_DeviceIp==null||"".equals(devices[i].m_DeviceIp))
						{
							devices[i].m_DeviceIp="null";
						}

						if(devices[i].m_DeviceModel==null||"".equals(devices[i].m_DeviceModel))
						{
							devices[i].m_DeviceModel="null";
						}

						if(devices[i].SecUsername==null||"".equals(devices[i].SecUsername))
						{
							devices[i].SecUsername="null";
						}

						if(devices[i].SecModel==null||"".equals(devices[i].SecModel))
						{
							devices[i].SecModel="null";
						}
						
						if(devices[i].SecurityLevel==null||"".equals(devices[i].SecurityLevel))
						{
							devices[i].SecurityLevel="null";
						}
						
						if(devices[i].AuthProtocol==null||"".equals(devices[i].AuthProtocol))
						{
							devices[i].AuthProtocol="null";
						}
						
						if(devices[i].AuthPassword==null||"".equals(devices[i].AuthPassword))
						{
							devices[i].AuthPassword="null";
						}
						
						if(devices[i].PrivProtocol==null||"".equals(devices[i].PrivProtocol))
						{
							devices[i].PrivProtocol="null";
						}
						
						if(devices[i].PrivPassword==null||"".equals(devices[i].PrivPassword))
						{
							devices[i].PrivPassword="null";
						}

						if(devices[i].SnmpVersion==null||"".equals(devices[i].SnmpVersion))
						{
							devices[i].SnmpVersion="null";
						}
						
						if(devices[i].Community==null||"".equals(devices[i].Community))
						{
							devices[i].Community="null";
						}
						
						str=devices[i].Oui;
						str+="||"+devices[i].m_DeviceSerialNum;
						str+="||"+devices[i].m_DeviceIp;					
						str+="||"+Encoder.AsciiToChineseString(devices[i].m_DeviceModel);						
						str+="||"+gather_id;
						str+="||"+devices[i].SecUsername;
						str+="||"+devices[i].SecModel;
                        str+="||"+devices[i].SecurityLevel;
                        str+="||"+devices[i].AuthProtocol;
						str+="||"+devices[i].AuthPassword;
						str+="||"+devices[i].PrivProtocol;
						str+="||"+devices[i].PrivPassword;	
						str+="||"+devices[i].SnmpVersion;
						str+="||"+devices[i].Community;
						str+="||"+Encoder.AsciiToChineseString(devices[i].m_DeviceSoftwareRev);
						
						if(!"null".equals(devices[i].Oui)&&!"null".equals(devices[i].m_DeviceSerialNum)
							&&!"null".equals(devices[i].m_DeviceIp)&&!"null".equals(devices[i].m_DeviceSoftwareRev)
							&&!"null".equals(devices[i].m_DeviceModel)
								&&(!"null".equals(devices[i].SecurityLevel)||!"null".equals(devices[i].Community)))
						{
							style="bgcolor=\"#FFFFFF\"";
						}
						else
						{
							style="bgcolor=\"red\"";
						}

						out.println("<tr "+style+">");							
						out.println("<td align=center>"+(("null".equals(devices[i].Oui))?"":devices[i].Oui)+"</td>");						
						out.println("<td align=center>"+(("null".equals(devices[i].m_DeviceSerialNum))?"":devices[i].m_DeviceSerialNum)+"</td>");
						out.println("<td align=center>"+(("null".equals(devices[i].m_DeviceIp))?"":devices[i].m_DeviceIp)+"</td>");			
						out.println("<td align=center>"+(("null".equals(devices[i].m_DeviceModel))?"":Encoder.AsciiToChineseString(devices[i].m_DeviceModel))+"</td>");
						out.println("<td align=center>"+(("null".equals(devices[i].m_DeviceSoftwareRev))?"":Encoder.AsciiToChineseString(devices[i].m_DeviceSoftwareRev))+"</td>");
						out.println("<input type=\"hidden\" name=\"deviceinfo\" value=\""+str+"\">");
						out.println("</tr>");
					  }
					  out.println("<tr bgcolor=\"#FFFFFF\"><td colspan=5 align=right class=green_foot>");
					  out.println("<input type=\"submit\" value=\"生成拓扑\"  class=btn>");
					  out.println("</tr>");
				  }
				%>
				<tr bgcolor="#FFFFFF">
					<td align=left colspan=5>
						<font color=red>说明:</font>
							<br>
								<font color=blue>*</font> 
								红色设备，表示因为一些必要数据采集不到，不能导入拓扑图；其它颜色设备就可以
								<br>							
					</td>
				</tr>
</table>				
</div>
<SCRIPT LANGUAGE="JavaScript">
parent.closeMsgDlg();
parent.data.innerHTML=devicedata.innerHTML;
</script>