<!--
@author ��ï��(5159)
@version 1.0
@since 2008-04-01
@category ��ȫ�¼�
 -->
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���յ�������ר��ϵͳ</title>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<link href="../css/menu_list.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/Calendar.js"/>"></SCRIPT>
<script type="text/javascript">
function Query(){
	var time = new Date($.dateToLong($("#start")) + 24 * 3600000);
	var dateValue = time.getYear() + "-" + (time.getMonth() + 1) + "-" + time.getDate();
	var url="<s:url value="/securitygw/SafeReport!getSafeDay.action"><s:param name="deviceid" value="deviceid"/></s:url>&nowDateString=" + dateValue + "&tt=" + new Date()
	$("#chartDay").attr("src",url);

	getTableData("<s:url value="/securitygw/SafeReport!getSafeDayTable.action"/>","#tableDay",dateValue);

	var url="<s:url value="/securitygw/SafeReport!getSafeWeek.action"><s:param name="deviceid" value="deviceid"/></s:url>&nowDateString=" + dateValue + "&tt=" + new Date()
	$("#chartWeek").attr("src",url);
	getTableData("<s:url value="/securitygw/SafeReport!getSafeWeekTable.action"/>","#tableWeek",dateValue);

	var url="<s:url value="/securitygw/SafeReport!getSafeMonth.action"><s:param name="deviceid" value="deviceid"/></s:url>&nowDateString=" + dateValue + "&tt=" + new Date()
	$("#chartMonth").attr("src",url);
	getTableData("<s:url value="/securitygw/SafeReport!getSafeMonthTable.action"/>","#tableMonth",dateValue);

	//addImgClick("<s:property value="deviceid"/>",dateValue);
}
//��ȡajax���ݣ����ص�div��
function getTableData(url,tableId,dateValue){
	$(tableId).html("����������...");
	$.post(
		url,
		{
			deviceid:"<s:property value="deviceid" />",
			nowDateString:dateValue
		},
		function(data){
			$(tableId).html(data);
	});
}

$(function(){
	Query();
	/*
	var deviceId = "<s:property value="deviceid"/>";

	var url = "<s:url value="/securitygw/entSecStat!getSpamMailOrginData.action" />";
	$("#chartDay").click(function(){
		var time = $("#start").val();
		window.open(url + "?deviceId=" + deviceId + "&type=day" + "&starttime=" + time);
	});

	$("#chartWeek").click(function(){
		var time = $("#start").val();
		window.open(url + "?deviceId=" + deviceId + "&type=week" + "&starttime=" + time);
	});

	$("#chartMonth").click(function(){
		var time = $("#start").val();
		window.open(url + "?deviceId=" + deviceId + "&type=month" + "&starttime=" + time);
	});
	*/
	//$("#chartDay").attr("alt","����鿴����");
	//$("#chartWeek").attr("alt","����鿴����");
	//$("#chartMonth").attr("alt","����鿴����");
});
function gopage(){}
function AdvancedQuery(){
	var url="<s:url value="/securitygw/SafeReport!advQuery.action"><s:param name="deviceid" value="deviceid"/></s:url>";
	$.open(url,"740px","400px","200px","200px","true");
}
</script>
<style type="text/css">
<!--
@import url(../css/css_ico.css);
body {
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;
}
a{margin-bottom:-1px;}
.style2 {	font-size: 10pt;
	font-weight: bold;
}
.style1 {
	font-size: 10pt;
	font-weight: bold;
}
-->
</style>
</head>
<body onclick="parent.document_click()">
<table width=98% style="margin-left: 5px;margin-top: 10px;" align=center>
	<tr>
		<td width="50%">
			<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<tr class="tab_title">
					<td colspan="5" class="title_white">��Ϣ</td>
				</tr>
				<tr class="tr_white">
					<td class="tr_yellow">�豸 IP</td>
					<td colspan="4" class="text"><s:property
						value="funInfo.loopback_ip" /></td>
				</tr>
				<tr class="tr_glory">
					<td class="tr_yellow">�豸�ͺ�</td>
					<td colspan="4" class="text"><s:property
						value="funInfo.device_model" /></td>
				</tr>
				<tr class="trOver_blue">
					<td class="tr_yellow">���иſ�</td>
					<td class="text">����</td>
					<td class="text">�����ʼ�</td>
					<td class="text">�����¼�</td>
					<td class="text">����</td>
				</tr>
				<tr class="tr_glory">
					<td class="tr_yellow">����</td>
					<td class="tr_green"><a class="black" href="#"><s:property
						value="funInfo.virustimes" /></a></td>
					<td class="tr_green"><a class="black" href="#"><s:property
						value="funInfo.ashmailtimes" /></a></td>
					<td class="tr_green"><a class="black" href="#"><s:property
						value="funInfo.attacktimes" /></a></td>
					<td class="tr_green"><a class="black" href="#"><s:property
						value="funInfo.filtertimes" /></a></td>
				</tr>
				<tr class="tr_white">
					<td class="tr_yellow">��ǰ״̬</td>
					<td class="" nowrap colspan="4">
					<table width="89" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<s:if test="funInfo.severity>=4">
								<td class="back_red"><a class="black" href="#"
									onclick="gopage()">Major</a></td>
							</s:if>
							<s:elseif test="funInfo.severity==3">
								<td class="back_org"><a class="black" href="#"
									onclick="gopage()">Minor</a></td>
							</s:elseif>
							<s:elseif test="funInfo.severity==2">
								<td class="back_yel"><a class="black" href="#"
									onclick="gopage()">Warning</a></td>
							</s:elseif>
							<s:else>
								<td class="back_green"><a class="black" href="#"
									onclick="gopage()">Normal</a></td>
							</s:else>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="tr_glory">
					<td class="tr_yellow">��ע</td>
					<td class="text" colspan="4"><s:url id="link"
						value="/securitygw/entSecStat!redirect.action">
						<s:param name="deviceId" value="deviceid"></s:param>
						<s:param name="costomerName" value="funInfo.customname"></s:param>
					</s:url> <s:a href="%{link}">
						<s:property value="funInfo.customname" />
					</s:a></td>
				</tr>
			</table>
		</td>
		<td>
			<table width="100%"  border="1" cellpadding="0" cellspacing="0" class="table">

			<s:url value="/securitygw/VirusReport.action" var="VirusReportHref">
				<s:param name="deviceid" value="deviceid" />
				<s:param name="remark" value="remark" />
				<s:param name="nowDateString" value="nowDateString" />
			</s:url>

			<s:url value="/securitygw/TrashMailReport.action" var="TrashMailReportHref">
				<s:param name="deviceid" value="deviceid" />
				<s:param name="remark" value="remark" />
				<s:param name="nowDateString" value="nowDateString" />
			</s:url>

			<s:url value="/securitygw/AttackReport.action" var="AttackReportHref">
				<s:param name="deviceid" value="deviceid" />
				<s:param name="remark" value="remark" />
				<s:param name="nowDateString" value="nowDateString" />
			</s:url>

			<s:url value="/securitygw/FilterReport.action" var="FilterReportHref">
				<s:param name="deviceid" value="deviceid" />
				<s:param name="remark" value="remark" />
				<s:param name="nowDateString" value="nowDateString" />
			</s:url>

			<s:url value="/securitygw/SafeReport.action" var="SafeReportHref">
				<s:param name="deviceid" value="deviceid" />
				<s:param name="remark" value="remark" />
				<s:param name="nowDateString" value="nowDateString" />
			</s:url>

			<s:url value="/securitygw/UserTopReport!virusTop.action" var="virusTopHref">
				<s:param name="device_id" value="deviceid" />
			</s:url>

			<s:url value="/securitygw/UserTopReport!asmailTop.action" var="asmailTopHref">
				<s:param name="device_id" value="deviceid" />
			</s:url>

			<s:url value="/securitygw/UserTopReport!attackTop.action" var="attackTopHref">
				<s:param name="device_id" value="deviceid" />
			</s:url>

			<s:url value="/securitygw/UserTopReport!filterTop.action" var="filterTopHref">
				<s:param name="device_id" value="deviceid" />
			</s:url>

			   <tr class="tab_title">
				<td colspan="3" class="title_white">��ҵ��ȫ�¼�</td>
			  </tr>
			  <tr class="tr_white">
				<td nowrap class="text"><a href="<s:property value="SafeReportHref" />"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">��ȫ�¼�ͳ��</a></td>
				<td nowrap class="text"><a href="<s:property value="virusTopHref"/>"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����û�����</a></td>
				<td nowrap class="text"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0"><a href="<s:url value="/securitygw/entSecStat!getSpamMailOrginData.action"><s:param name="deviceId" value="deviceid"/></s:url>">�����ʼ�����</a></td>
			  </tr>
			  <tr class="tr_glory">
				<td class="text"><a href="<s:property value="VirusReportHref" />"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����¼�ͳ��</a></td>
				<td class="text"><a href="<s:property value="asmailTopHref"/>"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����ʼ��û�����</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0"><a href="<s:url value="/securitygw/entSecStat!getAttackOrginData.action"><s:param name="deviceId" value="deviceid"/></s:url>">��������</a></td>
			  </tr>
			  <tr class="tr_white">
				<td class="text"><a href="<s:property value="TrashMailReportHref" />"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����ʼ�ͳ��</a></td>
				<td class="text"><a href="<s:property value="attackTopHref" />"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����û�����</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0"><a href="<s:url value="/securitygw/entSecStat!getFilterOrginData.action"><s:param name="deviceId" value="deviceid"/></s:url>">��������</a></td>
			  </tr>
			  <tr class="tr_glory">
				<td class="text"><a href="<s:property value="AttackReportHref" />"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����¼�ͳ��</a></td>
				<td class="text"><a href="<s:property value="filterTopHref"/>"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����û�����</a></td>
				<td class="text">&nbsp;</td>
			  </tr>
			  <tr class="tr_white">
				<td class="text"><a href="<s:property value="FilterReportHref" />"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0">�����¼�ͳ��</a></td>
				<td nowrap class="text">������������������</td>
				<td nowrap class="text">&nbsp;</td>
			  </tr>
			  <tr class="tr_glory">
				<td class="text">������������������</td>
				<td class="text"><img src="./images/bullet_main_02.gif" width="17" height="13" border="0"><a href="<s:url value="/securitygw/entSecStat!getVirusOrginData.action"><s:param name="deviceId" value="deviceid"/></s:url>">��������</a></td>
				<td class="text">&nbsp;</td>
			  </tr>
			</table>
		</td>
	</tr>
	<tr class="tab_title">
		<td colspan="2" class="title_white">��ҵ��ȫ�¼�����
			 <INPUT TYPE="text" id="start" class=bk value="<s:property value="nowDateString" />">
			 <INPUT TYPE="button" value="��" class=jianbian onclick="showCalendar('day',event)">
			 <input name="Submit" type="button" onclick="Query()" class="jianbian" value="��ѯ">
			 <input name="Submit" type="button" onclick="AdvancedQuery()" class="jianbian" value="�߼���ѯ">
		</td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
			<div align="center">
			  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><div align="center">�ձ���</div></td>
				  <td width="6%"><a href="#"><img src="./images/button_back.gif" width="15" height="10" border="0">Top</a></td>
				</tr>
			  </table>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center">
				<span class="style2">
					<a href="#" >
						<img border=0 id="chartDay" src="../images/loading.gif" />
					</a>
				</span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center" id="tableDay">
			</div>
		</td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
			<div align="center"><span class="style2"></span>
			  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><div align="center">�ܱ���</div></td>
				  <td width="6%"><a href="#"><img src="./images/button_back.gif" width="15" height="10" border="0">Top</a></td>
				</tr>
			  </table>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center">
				<span class="style2">
					<a href="#" >
						<img border=0 id="chartWeek" src="../images/loading.gif" >
					</a>
				</span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center" id="tableWeek">
			</div>
		</td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
			<div align="center"><span class="style2"></span>
			  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><div align="center">�±���</div></td>
				  <td width="6%"><a href="#"><img src="./images/button_back.gif" width="15" height="10" border="0">Top</a></td>
				</tr>
			  </table>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center">
				<span class="style2">
					<a href="#" >
						<img border=0 id="chartMonth" src="../images/loading.gif"/>
					</a>
				</span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center" id="tableMonth">
			</div>
		</td>
	</tr>
</table>
</html>
