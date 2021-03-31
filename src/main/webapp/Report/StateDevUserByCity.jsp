<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<html>
<head>
<title>设备绑定报表统计</title>
<%@ include file="../toolbar.jsp"%>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/progress.js"></SCRIPT>
</head>
<body>
<form name="frm" action="stateDevUser.action" method="post">
<br>
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align=center>
 <tr>
  <td>
  <table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
   <tr>
    <td width="162" align="center" class="title_bigwhite">设备绑定报表统计</td>
    <td>&nbsp;</td>
   </tr>
  </table>
  </td>
 </tr>
 <tr>
  <td>
  	 <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
  	 	<tr>
  	 		<TH colspan="4">设备绑定报表统计</TH>
  	 	</tr>
  	    <tr bgcolor=#ffffff>
  	   	  <td class=column align=right>开始时间</td>
  	   	  <td><input type="text" name="startTime" value="<s:property value="startTime"/>" readonly class=bk>
			<img name="endimg" onclick="new WdatePicker(document.frm.startTime,'%Y-%M-%D',false,'whyGreen')" src="../images/search.gif" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
		  </td>
		  <td class=column align=right>结束时间</td>
		  <td>
			<input type="text" name="endTime" value="<s:property value="endTime"/>" readonly class=bk>
			<img name="endimg" onclick="new WdatePicker(document.frm.endTime,'%Y-%M-%D',false,'whyGreen')" src="../images/search.gif" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
		  </td>
   	    </tr>
<!-- 
   		<tr bgcolor=#ffffff>
    		<td class=column align=right>属 地</td>
   			<td>
   			<select name="city_id" class=bk>
   			<s:iterator value="cityList">
   			<OPTION value='<s:property value="city_id"/>'><s:property value="city_name"/></OPTION>
   			</s:iterator>
   			</select>
   			<s:property value=""/>
   			
   			</td>
    		<td class=column>&nbsp;</td>
   	 		<td>&nbsp;</td>
   		</tr>
-->
   		<tr bgcolor=#ffffff>
    		<td class=column colspan=4 align=right>
    			<input type="button" value=" 统 计 " name="button" onclick="doQuery()" class=jianbian>
    			<input type="hidden" name="gw_type" value='<s:property value="gw_type"/>'>
    			<input type="hidden" name="city_id" value='<s:property value="city_id"/>'>
    		</td>
   		</tr>
  	 </table>
  </td>
 </tr>
 <tr>
  <td height = "20">
  </td>
 </tr>
 <tr>
  <td align="center">
  	 <div id="resultData" ></div>
  	 <div id="progress"></div>
  </td>
 </tr>
 <tr><td>&nbsp;</td></tr>
 <tr id="excel" style="display:none">
  <td align="left">
  	&nbsp;&nbsp;
  	<a href="javascript:toExcel()">
		<img src="../images/excel.gif" border="0" width="16" height="16"></img>
	</a>
     <iframe id="childFrm" style="display:none"></iframe>
  </td>
 </tr>
</TABLE>
</form>
</body>
</html>
<%@ include file="../foot.jsp"%>

<script language="javascript">

function toExcel(){
	var strURL = "stateDevUser!toExcel.action";
	var stime = frm.startTime.value;
	var etime = frm.endTime.value;
	var cityId = frm.city_id.value;
	var gw_type = frm.gw_type.value;
	strURL += "?startTime="+stime+"&endTime="+etime+"&city_id="+cityId+"&gw_type="+gw_type;
	
	document.getElementById("childFrm").src = strURL;
}

function openNewWindow(surl){
	var gw_type = frm.gw_type.value;
	surl = surl + "&gw_type="+gw_type;
	window.open(surl,"","left=20,top=20,height=450,width=700,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function init(mesg){
	//var strHTML = '<s:property value="strResult"/>';
	var strHTML = mesg;
	var resultObj = document.getElementById("resultData");
	var strline = strHTML.split("|||");
	var strOne = "";
	var strtmp = "";
	strtmp = "<table width=\"100%\" align=\"center\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" bgcolor=\"#999999\">";
	strtmp += "<tr><th>属地</th><th>上报上来与用户有绑定关系的设备数量</th><th>CRM工单过来的在用用户数</th><th>ADSL用户数</th><th>LAN用户数</th><th>光纤用户数</th><th>设备占用户的比例</th></tr>";
	var gw_type = frm.gw_type.value;
	var devUrl = "";
	var userUrl = "";
	for(var i = 0; i < strline.length; i++){
		strOne = strline[i].split("|");
		devUrl = "DeviceList4State.jsp?city_id="+strOne[0]+"&startTime="+strTime2Second2(frm.startTime.value)+"&endTime="+strTime2Second2(frm.endTime.value)+"&binddate=binddate";
		userUrl = "UserList4State.jsp?city_id="+strOne[0]+"&startTime="+strTime2Second2(frm.startTime.value)+"&endTime="+strTime2Second2(frm.endTime.value)+"&gw_type="+gw_type;
		strtmp += "<tr>";
		strtmp += "<td class=column ><a href='javascript:queryBycity(\""+strOne[0]+"\")'>"+strOne[1]+"</a></td>";
		strtmp += "<td class=column align='center'><a href='javascript:openNewWindow(\""+devUrl+"\")'>"+strOne[2]+"</a></td>";
		strtmp += "<td class=column align='center'><a href='javascript:openNewWindow(\""+userUrl+"\")'>"+strOne[3]+"</a></td>";
		strtmp += "<td class=column align='center'><a href='javascript:openNewWindow(\""+userUrl+"&accessType=ADSL"+"\")'>"+strOne[4]+"</a></td>";
		strtmp += "<td class=column align='center'><a href='javascript:openNewWindow(\""+userUrl+"&accessType=LAN"+"\")'>"+strOne[5]+"</a></td>";
		strtmp += "<td class=column align='center'><a href='javascript:openNewWindow(\""+userUrl+"&accessType=EPON"+"\")'>"+strOne[6]+"</a></td>";
		strtmp += "<td class=column align='center'>"+strOne[7]+"</td>";
		strtmp += "</tr>";
	}
	strtmp += "</table>";

	resultObj.innerHTML = strtmp;
}

function strTime2Second2(dateStr){
	
	var temp = dateStr.split('-')
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(temp[0]);
    reqReplyDate.setMonth(temp[1]-1);
    reqReplyDate.setDate(temp[2]);
    
    reqReplyDate.setHours(0, 0, 0);
	
	return Math.floor(reqReplyDate.getTime()/1000);
}

//-->
</script>

<script language="javascript">
function queryBycity(cityId){
	startProgress();
	frm.city_id.value = cityId;
	var stime = frm.startTime.value;
	var etime = frm.endTime.value;
	var gw_type = frm.gw_type.value;
	var url = "stateDevUser!getReportByCity.action";
	$.post(url,{
      startTime:stime,
      endTime:etime,
      city_id:cityId,
      gw_type:gw_type
    },function(mesg){
    	//alert(mesg);
    	init(mesg);
    	stopProgress();
    });
    
}

function doQuery(){
	var cityId = frm.city_id.value;
	 $("input[@name='button']").attr("disabled", true);
	queryBycity(cityId);
	 $("input[@name='button']").attr("disabled", false);
	
		$("tr[@id='excel']").css("display","");
}


</script>