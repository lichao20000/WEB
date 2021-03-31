<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.common.util.Encoder" %>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.PmeeInterface" %>
<%@ page import="com.linkage.litms.webtopo.DeviceResourceInfo" %>
<%@ page import="org.omg.CORBA.StringHolder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<title>性能查看</title>



<!-- <body onLoad="MM_preloadImages('../images/button_save2.gif')"> -->
<body>
<% 
  	request.setCharacterEncoding("GBK");
	String strExpressionid = request.getParameter("id");
	String device_id=request.getParameter("device_id");
	String strName = request.getParameter("name");
	String strDescr = request.getParameter("descr");
			
	String str_polltime = request.getParameter("polltime");
		
	//begin modifide by w5221 山东要求性能直接与PMEE通讯	
	PMEE.Data[] pmeeDataTab = null;
	pmeeDataTab = PmeeInterface.GetInstance().GetDataList(strExpressionid,device_id,new StringHolder());
	//end modifide by w5221 山东要求性能直接与PMEE通讯

	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursorDeviceInfo = deviceInfo.getDeviceResource(device_id);
	Map deviceMap = cursorDeviceInfo.getNext();
	String deviceName = (String)deviceMap.get("device_name");
	String loopbackip = (String)deviceMap.get("loopback_ip");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	String today = sdf.format(date);

	//增加自动刷新功能
	//2007-3-12 江苏测试
	if (str_polltime != null) {
		int polltime = Integer.parseInt(str_polltime) * 60;
		out.println("<meta http-equiv=\"refresh\" content=\""+ polltime + "\">");
	}
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF"> <br>
        <br>
        <table width="100%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
          <tr>
            <td align="center" nowrap  class="title_bigwhite"> 性能实时显示</td>
            <td align="right" nowrap><img src="../images/attention_2.gif"  height="12"> 设备:&nbsp;&nbsp;<%= deviceName%>&nbsp;&nbsp;&nbsp;<%= loopbackip%></td>
            <td align="right"  nowrap><a href="#" onclick="JavaScript:window.open('webtopo_PMReport.jsp?expressionid=<%= strExpressionid%>&ip=<%= device_id%>','历史报表显示','scrollbars=yes,width=820,height=500')">历史报表显示</a></td>
            <td align="left" nowrap><div align="center"><a href="webtop_xinnen_tuxin.jsp?device_id=<%= device_id%>&device_name=<%= deviceName %>&loopback_ip=<%= loopbackip%>&expID=<%= strExpressionid %>&expName=<%=strName%>&expDescr=<%=strDescr%>"><img src="../images/chart.gif" width="20" height="19" border="0"> 图形显示</a> </div></td>
            <td align="left" nowrap><div align="center"><a href="javascript:ViewReport()"><img src="../images/chart.gif" width="20" height="19" border="0"> MRTG图</a> </div></td>
          </tr>
        </table>
        <table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr bgcolor="#FFFFFF" class="blue_title">
          	<td nowrap>选择</td>
            <td nowrap>性能</td>
            <td nowrap>性能表达式</td>
            <td nowrap>索引描述</td>
            <td nowrap>索引</td>
            <td nowrap>值</td>
          </tr>
			<%
				if(pmeeDataTab!=null)
				for(int i=0;i<pmeeDataTab.length;i++){
			%>
          <tr bgcolor="#FFFFFF">
          	<td nowrap><input type="checkbox" name="indexid" value="<%=pmeeDataTab[i].index %>"></td>
            <td nowrap><%= strName %></td>
			<td nowrap><%= strDescr %></td>
			<td nowrap><%= Encoder.AsciiToChineseString(pmeeDataTab[i].desc)%></td>
			<td nowrap><%= pmeeDataTab[i].index %></td>
			<%
			//by zhaixf(3412) 在线时长转换成小时
			if(strName != null && strName.contains("在线时长")){
				try{
				float f = Float.parseFloat(pmeeDataTab[i].value);
				long lg = (long)f;
				String t = lg/3600 + "小时";
				t += (lg%3600)/60 + "分";
				t += (lg%3600)%60 + "秒";
				out.print("<td nowrap>" + t + "</td>");
				}catch(NumberFormatException e){
					out.print("<td nowrap>" + pmeeDataTab[i].value + "</td>");
				}
			}else{
				out.print("<td nowrap>" + pmeeDataTab[i].value + "</td>");
			} %>
		  </tr>
		  <% 
		   } 
		  %>
          <tr bgcolor="#FFFFFF" class=text onmouseout="className='trOverblue'">

            <td colspan="9" nowrap class="green_foot" ><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="50%">&nbsp;&nbsp;<img src="../images/attention.gif" width="15" height="12"> 采集时间:<%=today %></td>
                <td width="50%" align="right">	
                  <!--<input name="Submit" type="submit" class="jianbian" value=" 刷 新 ">-->&nbsp;&nbsp;
				  <input name="Submit2" type="submit" class="jianbian" value=" MRTG图 " onClick="javascriprt:ViewReport()">
                </td>
              </tr>
            </table></td>
          </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<script type="text/javascript">
<!--
	function ViewReport(){
		var checkObj = document.all("indexid");
		var indexid = "";
		if(checkObj == null){
			alert("未选择");
			return;
		}
		//当生成数据只有一行时.
		if(typeof(checkObj.length) == "undefined"){ 
			if(checkObj.checked){
				indexid = checkObj.value;
			}
		}else{//当生成的数据多于一行时.
			for(var i=0;i<checkObj.length;i++){
				if(checkObj[i].checked){
					if(indexid == "")
						indexid = checkObj[i].value;
					else
						indexid += "|" + checkObj[i].value;
				}
			}
		}
		//如果indexid为空时,则...
		if(indexid == "")
			alert("请先选择要实例");
		else{
			var url = "./now_device_flux.jsp?device_id=<%=device_id%>";
			url += "&indexid=" + indexid;
			url += "&expID=<%=strExpressionid%>&expName=<%=strName%>";
			url += "&expDescr=<%=strDescr%>";
			url += "&loopback_ip=<%=loopbackip%>&deviceName=<%=deviceName%>";
			window.open(url);
		 }
	}

//增加自动刷新功能 
 //2007-3-12 江苏测试 
<%if(str_polltime != null){%>
parent.closeMsgDlg();
<%}%>
//-->
</script>
<%@ include file="../foot.jsp"%>
