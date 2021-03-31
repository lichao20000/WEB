<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>QOS队列实时显示</title>

</head>

<body >
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<% 
  	request.setCharacterEncoding("GBK");
	String device_id= request.getParameter("device_id");
	
	//通过FluxDataList从数据库和Mastercontrol获得流量信息
	QosDataList fluxData = new QosDataList();
	//HashMap hMap = fluxData.getInterfaceDevicePort(device_id);
	QosQueueData[] fluxDataTab = fluxData.getDeviceQosQueueData(device_id,user.getAccount(),user.getPasswd());
	String [] arr_port = new String[fluxDataTab.length];
	
	//通过DeviceResourceInfo从数据库获得设备信息
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor = deviceInfo.getDeviceResource(device_id);
	
	Map myMap = cursor.getNext();
	//获得当前时间
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	String today = sdf.format(date);
	String deviceName = "";
	String loopbackip = "";
	if(myMap != null){
		deviceName= (String)myMap.get("device_name");
		loopbackip= (String)myMap.get("loopback_ip");
	}
	//DecimalFormat decFormat = new DecimalFormat();
	//decFormat.applyPattern("#,###.0#");
	//FluxUnit fu=FluxUnit.getFluxUnit(session);
	//double unit = fu.getFluxBase();
	//用于javascript函数的参数
	//String tmpParam = "";
%>

<table width="780" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF"> <br>
        <br>
		<td width="100%%">
        <table width="100%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="blue_gargtd">
          <tr>
            <td align="center" nowrap  class="title_bigwhite">QOS队列实时显示</td>
            <td align="right" nowrap>&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> 以下是设备:<%= deviceName %>:&nbsp;&nbsp;<%=loopbackip%> 端口QOS实时显示 </td>
            <td align="center">&nbsp;</td>
			<!--
			<td align="right" nowrap style=""><a href="webtopo_liuliang_historyForm.jsp?device_id=<%= device_id%>">历史报表显示 </a><a href="webtop_liuliang_report.jsp?device_id=<%= device_id %>&device_name=<%= deviceName %>&loopback_ip=<%= loopbackip%>"><img src="../images/chart.gif" width="20" height="19" border="0"> 图形显示</a></td><td align="right" nowrap style=""><a href="webtopo_liuliang_historyForm.jsp?device_id=<%= device_id%>">历史报表显示 </a><a href="webtop_liuliang_report.jsp?device_id=<%= device_id %>&device_name=<%= deviceName %>&loopback_ip=<%= loopbackip%>"><img src="../images/chart.gif" width="20" height="19" border="0"> 图形显示</a></td>
			-->
          </tr>

		</table>
        <table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr bgcolor="#FFFFFF" class="blue_title">
            <td nowrap>索引</td>
			<td nowrap>描述</td>
            <td nowrap>策略方向</td>
			<td nowrap>策略描述</td>
			<td nowrap>策略等级</td>
			<td nowrap>承诺带宽(kbps)</td>
			<td nowrap>最大队列长度</td>
            <td nowrap>当前队列长度</td>
			<td nowrap>丢包包数(个/秒)</td>
			<td nowrap>丢包字节数(Byte/秒)</td>
          </tr>
		  
		  
			<%				
				if(fluxDataTab!=null)
				{
				String inoctets;
				String outoctets;
				for(int i=0;i<fluxDataTab.length;i++){
					/*
					inoctets = fluxDataTab[i].ifinoctetsbps;
					
					if(inoctets.length()>=6 && inoctets.length()<=9) {
						inoctets =StringUtils.formatString((Double.parseDouble(inoctets)/(unit*unit)),2) + "M";
					}
					else if (inoctets.length()>=10) {
						inoctets = StringUtils.formatString((Double.parseDouble(inoctets)/(unit*unit*unit)),2) + "G";
					}
					else {
						inoctets = fluxDataTab[i].ifinoctetsbps;
					}
					
					 outoctets= fluxDataTab[i].ifoutoctetsbps;
					
					if(outoctets.length()>=6 && outoctets.length()<=9) {
						outoctets = StringUtils.formatString((Double.parseDouble(outoctets)/(unit*unit)),2) + "M";
					}
					else if (outoctets.length()>=10) {
						outoctets = StringUtils.formatString((Double.parseDouble(outoctets)/(unit*unit*unit)),2) + "G";
					}
					else {
						outoctets = fluxDataTab[i].ifoutoctetsbps ;
					}
					
					tmpParam = "'" + fluxDataTab[i].index + "','" + fluxDataTab[i].ifdescr + "','" + fluxDataTab[i].ifname + "','" + fluxDataTab[i].ifnamedefined + "','" + fluxDataTab[i].ifportip + "'";
					*/
			%>

		  
          <tr bgcolor="#FFFFFF" >
            <td><%= fluxDataTab[i].index %></td>
			<td><%= fluxDataTab[i].desc %></td>
			<td><%= fluxDataTab[i].policydirection %></td>
			<td><%= Encoder.AsciiToChineseString(fluxDataTab[i].policyinfo) %></td>
			<td><%= fluxDataTab[i].policyvalue %></td>
			<td><%= fluxDataTab[i].qosbandwidth %></td>
			 <td><%= fluxDataTab[i].qosmaxqdepth %></td>
			 <td><%= fluxDataTab[i].qoscurrentqdepth %></td>
			 <td><%= fluxDataTab[i].qosdiscardpktpktpps %></td>
			 <td><%= fluxDataTab[i].qosdiscardbytebps %></td>
		  </tr>
		  <% 
			  arr_port[i] = fluxDataTab[i].index;
		  }
		}

		myMap.clear();
	%>
          <tr bgcolor="#FFFFFF" class=text onmouseout="className='blue_trOut'">
            <td colspan="18" class="blue_foot" ><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="77%">&nbsp;&nbsp;<img src="../images/attention.gif" width="15" height="12"> 采集时间:<%=today %></td>
                <td width="23%" align=right>
                <!-- <input name="Submit" type="submit" class="jianbian" value=" 刷 新 ">-->

				 <input name="Submit2" type="button" class="jianbian" value=" 关 闭 " onClick="javascriprt:window.close()">
                </td>
              </tr>
            </table>
			</td>
          </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<script language="javascript">
	//存在上次点击的行对象
	var _srcObj;
	function ViewReport(ifindex,ifdescr,ifname,ifnamedefined,ifportip){
		if(_srcObj != null){
			_srcObj.className="";
		}
		var obj = event.srcElement.parentElement;
		var tableObj = obj.parentElement;
		
		obj.className="trOver";
		_srcObj = obj;

		page = "../Visualman/now_getPort.jsp?device_id="+<%=device_id%>+"&ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		window.open(page);
	}
</script>
</body>
</html>
