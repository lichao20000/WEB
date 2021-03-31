<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%
request.setCharacterEncoding("GBK");

String device_id = request.getParameter("device_id");

String selectPingTaskSql = "select min(a.ping_task_id) ping_task_id,a.device_id,a.response_ip,b.vpn_auto_id from vpnping_task_map a,vpnping_task_link b where a.vpn_task_id = b.vpn_task_id and a.device_id ='"
                   +device_id+"' group by b.vpn_auto_id,a.device_id,a.response_ip";
Cursor cursor = DataSetBean.getCursor(selectPingTaskSql);
Map fields = cursor.getNext();

//获取所有的设备信息
HashMap deviceMap = new HashMap(100);
DeviceAct doc = new DeviceAct();
Cursor deviceInfos = doc.getDevicesAll();
Map fields1 = deviceInfos.getNext();
String device_id_database ="";
String device_name_database ="";
while(null!=fields1)
{
	device_id_database = (String)fields1.get("device_id");
	device_name_database = (String)fields1.get("device_name");
	deviceMap.put(device_id_database,device_name_database);
	fields1 = deviceInfos.getNext();
}

//获取所有的用户信息
HashMap userMap = new HashMap(10);
String userSql = "select vpn_auto_id,username from vpn_auto_customer";
deviceInfos = DataSetBean.getCursor(userSql);
fields1 = deviceInfos.getNext();
String vpn_auto_id1 ="";
String username1 ="";
while(null!=fields1)
{
	vpn_auto_id1 = (String)fields1.get("vpn_auto_id");
	username1 = (String)fields1.get("username");
	userMap.put(vpn_auto_id1,username1);
	fields1 = deviceInfos.getNext();
}
	
%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR> 
<TR><TD>	
	<TABLE width="96%" border=0 cellspacing=0 cellpadding=0 align="center">	 
	  <TR><TD bgcolor=#999999>
	    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
          <TR>
		    <TD bgcolor="#ffffff" colspan=3 align=center><B>链路服务质量</B>
			</TD>
		  </TR>
		  <TR>
		    <TH>用户名称</TH>		    
		    <TH>发包设备名称</TH>
			<TH>收包设备IP</TH>            
		  </TR>
		  <%
		  if(fields!=null)
			{	
			   String device_id_temp = "";
			   String pingtask_id_temp = "";
			   String device_name_temp = "";
			   String response_ip_temp  = "";
			   
				while(fields != null)
				{	
					vpn_auto_id1 =(String)fields.get("vpn_auto_id");
					username1="未知";
					if(userMap.size()>0)
					{
						username1 = (String)userMap.get(vpn_auto_id1);
						if(null==username1)
						{
							username1="未知";
						}
					}
					device_id_temp = (String)fields.get("device_id");
					pingtask_id_temp = (String)fields.get("ping_task_id");
					device_name_temp = (String)deviceMap.get(device_id_temp);
					response_ip_temp = (String)fields.get("response_ip");
					out.println("<TR bgcolor=#ffffff align=center >");
					out.println("<TD class=column>"+username1+"</TD>");
					out.println("<TD class=column><B><a href ='web_deviceRelateLink_detail.jsp?pingtask_id="+pingtask_id_temp+"&device_name="+device_name_temp+"&response_ip="+response_ip_temp+"&username="+username1+"'>"+device_name_temp+"</a></B></TD>");	
					out.println("<TD class=column>"+response_ip_temp+"</TD>");			
					out.println("</TR>");
					fields = cursor.getNext();
				}					
			}
			else
			{
				out.println("<TR bgcolor=#ffffff>");
				out.println("<TD class=column  colspan=3>没有相关的链路</TD>");
				out.println("</TR>");
			}		 
		  %>		   
		</TABLE>
	  </TD></TR>
	  <TR><TD HEIGHT=10>&nbsp;</TD></TR>
	</TABLE>	
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>