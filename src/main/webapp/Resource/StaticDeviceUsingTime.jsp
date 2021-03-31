<%--
FileName	: DeviceInformStat.jsp
Date		: 2007年12月
Desc		: 设备上报统计.
--%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@ page contentType="text/html;charset=gbk"%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%
//初始化当前时间 
DateTimeUtil dateUtil = new DateTimeUtil();   
String time = dateUtil.getTime();
String today = dateUtil.getYear()+"-"+dateUtil.getMonth()+"-"+dateUtil.getDay();

//设备属地
String city_id = curUser.getCityId();
SelectCityFilter City = new SelectCityFilter(request);
String strCityList = City.getSelfAndNextLayer(false, city_id, "");

%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/code.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--
function checkForm()
{    
    //日期
	var objectS_time = document.all.frm.start_day.value;
	var objectE_time = document.all.frm.end_day.value;	
	var start_day=document.all("start_day").value;
	var end_day=document.all("end_day").value;
	var start_time =document.all.frm.start_time.value;	
	var end_time =document.all.frm.end_time.value;
	var city_id = document.all.frm.city_id.value;
	
	if(""==start_time)
	{
	  alert("请填写开始时间！");
	  return false;
	}
	
	
	if(""==end_time)
	{
	  alert("请填写结束时间！");
	  return false;
	}
	
	if(!IsDate1(objectS_time))
	{
		alert("请输入正确的开始日期！");
		return false;
	}
	
	if(!IsDate1(objectE_time))
	{
		alert("请输入正确的结束日期！");
		return false;
	}	
	if(formatDate(objectS_time,start_time)>formatDate(objectE_time,end_time))
	{
		alert("开始时间不能大于结束时间！");
		return false;
	}
	if(periodMonth(objectS_time,objectE_time)>3)
	{
	 alert("结束时间不能超过开始时间3个月！");
	 return false;
	}
}

function formatDate(strValue,strHMS)
  {    
	var str1=strValue.substring(strValue.indexOf("-")+1,strValue.lastIndexOf("-"));
		str1=(str1.length==1)?"0"+str1:str1; //月
	var str2=strValue.substring(strValue.lastIndexOf("-")+1);
		str2=(str2.length==1)?"0"+str2:str2; //日
	strValue=strValue.substring(0,strValue.indexOf("-")+1)+str1+"-"+str2;	
	if(strHMS.length>0){
		var str3=strHMS.substring(strHMS.indexOf(":")+1,strHMS.lastIndexOf(":"));
		str3=(str3.length==1)?"0"+str3:str3;
		var str4=strHMS.substring(strHMS.lastIndexOf(":")+1);
		str4=(str4.length==1)?"0"+str4:str4;
		var str5=strHMS.substring(0,strHMS.indexOf(":"));
		str5=(str5.length==1)?"0"+str5:str5;
		strValue=strValue+" "+str5+":"+str3+":"+str4;
	}
	return strValue;
  }
  
function periodMonth(beginDate,endDate)
{
  //年
  var beginStr1=beginDate.substring(0,beginDate.indexOf("-"));  
  //月
  var beginStr2=beginDate.substring(beginDate.indexOf("-")+1,beginDate.lastIndexOf("-"));
  
  //年
  var endStr1=endDate.substring(0,endDate.indexOf("-"));  
  //月
  var endStr2=endDate.substring(endDate.indexOf("-")+1,endDate.lastIndexOf("-"));
  
  var months = (endStr1-beginStr1)*12+(endStr2-beginStr2);
  return months;
}

function IsDate1(strValue){
	var v = strValue;
	var tmpStr1,tmpStr2,tmpStr3;
	var pos = v.indexOf("-");
	if(pos == -1){
		alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}

	tmpStr1 = v.substring(0,pos);

	if(tmpStr1.length !=4 || parseInt(tmpStr1)<1){
		alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}

	v = v.substring(pos+1,v.length);
	pos = v.indexOf("-");
	if(pos == -1){
		alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	tmpStr2 = v.substring(0,pos);
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 12 || parseInt(tmpStr2)<1){
		alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}

	tmpStr3=v.substring(pos+1);
	if(tmpStr3.length >2 || parseInt(tmpStr3) > 31 || parseInt(tmpStr3)<1){
		alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	return true;
}

//-->
</SCRIPT>
  
<FORM name="frm" action="StaticDeviceUsingTime_devList.jsp" onsubmit="checkForm()">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							设备资源统计
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		
		<TR>
			<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR><TH colspan="4">企业网关设备按时间统计</TH></TR>
				<tr bgcolor="#FFFFFF">
	               <td align="right" width="15%">
	                    开始时间：
	               </td>
	               <td width="25%">
	                    <input type="text" name="start_day" class=bk value="<%=today%>" size=10 readonly>
	                    <input type="button" value="▼" class=jianbian onClick="showCalendar('day',event)">
	                    <input type="text" name="start_time" class=bk value="00:00:00" size=10>
	               </td>
	               <td align="right" width="20%">
	                    结束时间：
	               </td>
	               <td width="30%">
	                    <input type="text" name="end_day" class=bk value="<%=today%>" size=10 readonly>
	                    <input type="button" value="▼" class=jianbian onClick="showCalendar('day',event)">
	                    <input type="text" name="end_time" class=bk value="<%=time%>" size=10>
	               </td>
	            </tr>
				<TR bgcolor="#FFFFFF">
				<td align="right" width="20%">
	                    属地：
	               </td>
				<TD class="">
					<%=strCityList%>
				</TD>
				<td align="right" width="20%">
	                    设备序列号：
	               </td>
				<TD class="">
					<input type="text" name="device_sn" class="bk">
				</TD>
				</TR>
				<TR bgcolor="#ffffff"><TD colspan="4" align="right" class="green_foot">
					<input type="submit" value=" 查 询 " class="btn"></TD>
				</TR>										
			</TABLE>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR id="devList" style="display:none">
			<TD bgcolor=#999999>
			<DIV id="devListLayer"></DIV>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
				
	</TABLE>
</TD>
</TR>
</TABLE>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
//-->
</SCRIPT>

<%@ include file="../foot.jsp"%>