<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO����ͳ��</title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	query();
});

//�ύ��ѯ
function query(){
	
	var cityId = $("select[@name='cityId']");
	var startTime = $("input[@name='startTime']");
	var endTime = $("input[@name='endTime']");
	queryData(cityId.val(),startTime.val(),endTime.val());
	
}

//�ύ��ѯ
function queryData(cityId,startTime,endTime){
	var url = "bbms/report/evdoReport!getDataReport.action";
	var dataList = $("div[@id='dataList']");
	if("null"!=dataList && null!=dataList ){
		dataList.html("��������ͳ����...");
	}
	$.post(url,{
		cityId:cityId,
		startTime:startTime,
		endTime:endTime
    },function(mesg){
    	dataList.html(mesg);
    });
}

//����excel��ѯ
function queryDataForExcel(cityId,startTime,endTime){
	
	var url = "bbms/report/evdoReport!getDataReport.action";
	url = url + "?cityId="+cityId+"&startTime="+startTime+"&endTime="+endTime;
	url = url+"&isReport=excel"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

//����pdf��ѯ
function queryDataForPdf(cityId,startTime,endTime){
	
	var url = "bbms/report/evdoReport!getDataReport.action";	
	url = url + "?cityId="+cityId+"&startTime="+startTime+"&endTime="+endTime;
	url = url+"&isReport=pdf"
	
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPrint(cityId,startTime,endTime){
	
	var url = "bbms/report/evdoReport!getDataReport.action";	
	url = url + "?cityId="+cityId+"&startTime="+startTime+"&endTime="+endTime;
	url = url+"&isReport=print";
	
	window.open(url,"","left=60,top=60,width=920,height=500,resizable=yes,scrollbars=yes");
	
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
<body onload="">
<s:form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 align="center" width="95%">
	<tr height="18">
		<td colspan="1"  width="15"></td>
	</tr>
	<tr>
		<td >
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									EVDO����ͳ��
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
	<TR>
  		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
				<TR>
					<TD bgcolor="#999999">
						<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
							<tr bgcolor="#FFFFFF" id="query1" style="display:">
								<td align=right>
									ѡ������
									<s:select label="ѡ������" list="cityList" name="cityId" listKey="city_id" listValue="city_name" emptyOption="true"  value="cityId"/>
									&nbsp;��ʼʱ�� 
									<input type="text" name="startTime" size="10" value='<s:property value="startTime" />' readonly class=bk>
									<img name="shortDateimg"
										onclick="new WdatePicker(document.form1.startTime,'%Y-%M-%D',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ��">(YYYY-MM-DD)
									&nbsp;����ʱ�� 
									<input type="text" name="endTime" size="10" value='<s:property value="endTime" />' readonly class=bk>
									<img name="shortDateimg"
										onclick="new WdatePicker(document.form1.endTime,'%Y-%M-%D',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ��">(YYYY-MM-DD)
									<input type="button" name="btn0" value=" �� ѯ " class=jianbian onclick="javascript:query()"></td>
							</tr>
						</TABLE>
					</TD>
				</TR>
				<tr height="18">
					<td width="15"></td>
				</tr>
				<tr height="18">
					<td ><div id="dataList">��������ͳ����...</div></td>
				</tr>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</s:form>
<form name="frm2"></form>
</body>
<br>
<br>
<%@ include file="../foot.jsp"%>
