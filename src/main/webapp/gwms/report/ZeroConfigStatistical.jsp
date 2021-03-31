<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������״̬ͳ��</title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	init();
});

function init(){
	
	//queryData("<s:property value="cityId" />",strTime2Second("<s:property value="strStartTime" />"),strTime2Second("<s:property value="strEndTime" />"));
}

function getLastDay(strDay)
{
	//ѡ�����ڵ�ǰһ��
	var intDay1=DateParse(strDay)-86400000;
	var d=new Date(intDay1);
	strDay=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();		
	return strDay;
}

function queryUser(querytype)
{
	
	var startTime= strTime2Second(form1.startTime.value);
	var endTime = strTime2Second(form1.endTime.value);
	var cityId = document.form1.cityId.value;
	
	var d=new Date();
	var today= d.getTime()/1000;
	
	if(startTime>endTime)
	{
		window.alert("��ѯ��ʼʱ�䲻�ܴ��ڲ�ѯ����ʱ��!");
		return;
	}
	
	if(endTime>today)
	{
		window.alert("��ѯ����ʱ�䲻�ܴ���ϵͳ��ǰʱ��!");
		return;
	}
	$("input[@name='btn0']").attr("disabled", true);
	queryData(cityId,startTime,endTime);
	$("input[@name='btn0']").attr("disabled", false);
}

//�ύ��ѯ
function queryData(city_id,startTime,endTime){
	
	var url = "gwms/report/zeroConfigStatistical!getDayReport.action";
	var dataList = document.getElementById("dataList");
	var queryInform = document.getElementById("queryInform");
	
	//���ز�ѯ�����Ϣ modify by zhangcong 2011-06-09
	if("null"!=dataList && null!=dataList ){
		dataList.style.display="none";
	}
	
	//��ʾ��ѯ״̬��Ϣ modify by zhangcong 2011-06-09
	if("null"!=queryInform && null!=queryInform ){
		queryInform.style.display="";
	}
	
	document.getElementById("Bind").style.display="none";
	document.getElementById("noBind").style.display="none";
	
	$.post(url,{
		cityId:city_id,
		startTime:startTime,
		endTime:endTime
    },function(mesg){
    	dataList.innerHTML = mesg;
    });

	//���ز�ѯ��ʾ��Ϣ modify by zhangcong 2011-06-09
	queryInform.style.display="none";
	//��ʾ��� modify by zhangcong 2011-06-09
	dataList.style.display="";
}

//����excel��ѯ
function queryDataForExcel(prvurl,city_id,startTime,endTime){
	
	var url = "gwms/report/zeroConfigStatistical!";
	url = url + prvurl + ".action";
	
	url = url + "?cityId="+city_id+"&startTime="+startTime+"&endTime="+endTime;
	
	url = url+"&isReport=excel"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

//����pdf��ѯ
function queryDataForPdf(prvurl,city_id,startTime,endTime){
	
	var url = "gwms/report/zeroConfigStatistical!";
	url = url + prvurl + ".action";
	
	url = url + "?cityId="+city_id+"&startTime="+startTime+"&endTime="+endTime;
	
	url = url+"&isReport=pdf"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPrint(prvurl,city_id,startTime,endTime){
	
	var url = "gwms/report/zeroConfigStatistical!";
	url = url + prvurl + ".action";
	
	url = url + "?cityId="+city_id+"&startTime="+startTime+"&endTime="+endTime;
	url = url+"&isReport=print";
	
	window.open(url,"","left=60,top=60,width=920,height=500,resizable=yes,scrollbars=yes");
	
}

function queryDataBind(cityId,startTime,endTime){
	
	var url = "gwms/report/zeroConfigStatistical!getBindData.action";
	var bind = document.getElementById("bind");
	bind.style.display="";
	if("null"!=bind && null!=bind ){
		bind.innerHTML = "��������ͳ����...";
	}
	$.post(url,{
		cityId:cityId,
		startTime:startTime,
		endTime:endTime
    },function(mesg){
    	document.getElementById("bind").innerHTML = mesg;
    });
}

function queryDataNoBind(cityId,startTime,endTime){
	
	var url = "gwms/report/zeroConfigStatistical!getNoBindData.action";
	var noBind = document.getElementById("noBind");
	noBind.style.display="";
	if("null"!=noBind && null!=noBind ){
		noBind.innerHTML = "��������ͳ����...";
	}
	$.post(url,{
		cityId:cityId,
		startTime:startTime,
		endTime:endTime
    },function(mesg){
    	document.getElementById("noBind").innerHTML = mesg;
    });
}

//��ʽ��ʱ��
function formatDate(strValue)
{
	var str1=strValue.substring(strValue.indexOf("-")+1,strValue.lastIndexOf("-"));
		str1=(str1.length==1)?"0"+str1:str1;
	var str2=strValue.substring(strValue.lastIndexOf("-")+1);
		str2=(str2.length==1)?"0"+str2:str2;
	strValue=strValue.substring(0,strValue.indexOf("-")+1)+str1+"-"+str2;
	return strValue;
}

//��2004-10-20������ʱ��ת����long��
function DateParse(strValue)
{
	strValue=strValue.substring(5,10)+"-"+strValue.substring(0,4);	
	return Date.parse(strValue);
}

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<body onload="">
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<tr>
		<td >
			<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
				<tr>
					<td>
						<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
							<tr>
								<td>
									<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
										<tr>
											<td width="162" align="center" class="title_bigwhite" nowrap>
												������״̬ͳ��
											</td>
											<td nowrap>
												<img src="../../images/attention_2.gif" width="15" height="12">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
				<tr>
					<td>
						<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#000000">
							<tr bgcolor="#FFFFFF" id="query1" style="display:">
								<td align=right class=column width="14%">��ʼʱ��</td>
								<input type="hidden" name="cityId" value="<s:property value="cityId" />"/>
								<td align=left width="34%">
									<input type="text" name="startTime" value='<s:property value="strStartTime" />' readonly class=bk>
									<img onClick="WdatePicker({el:document.form1.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
															src="../../images/dateButton.png" width="15" height="12"
															border="0" alt="ѡ��" />
								</td>
								<td align=right class=column width="14%">����ʱ��</td>
								<td align=left width="38%">
									<input type="text" name="endTime" value='<s:property value="strEndTime" />' readonly class=bk>
									<img name="shortDateimg"
															onClick="WdatePicker({el:document.form1.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
															src="../../images/dateButton.png" width="15" height="12"
															border="0" alt="ѡ��" />
									&nbsp;&nbsp;
									<input type="button" name="btn0" value=" �� ѯ " class=jianbian onclick="javascript:queryUser()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	<TR>
		<TD HEIGHT=15>&nbsp;</TD>
	</TR>
	<TR>
		<TD ><div id="queryInform" align=center style="display:none">��������ͳ����...</div></TD>
	</TR>
	<TR>
		<TD ><div id="dataList" align=center></div></TD>
	</TR>
	<TR>
		<TD HEIGHT=15>&nbsp;</TD>
	</TR>
	<TR>
		<TD><div id="bind" align=center style="display:none">��������ͳ����...</div></TD>
	</TR>
	<TR>
		<TD HEIGHT=15>&nbsp;</TD>
	</TR>
	<TR>
		<TD><div id="noBind" align=center style="display:none">��������ͳ����...</div></TD>
	</TR>
</TABLE>
</form>
<form name="frm2"></form>
</body>
<br>
<br>
<%@ include file="../foot.jsp"%>
