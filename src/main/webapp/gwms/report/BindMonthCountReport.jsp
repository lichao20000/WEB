<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>�����±���</title>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jsDate/WdatePicker.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/progress.js"/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript">

/*-----------------------------------------------------
//����  :	ҳ���ʼ��
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
$(function(){
	//queryData('<s:property value="cityId"/>',strDate2Second('<s:property value="endData"/>'));
});

/*-----------------------------------------------------
//������:		doQuery
//����  :	��Բ�ѯ��ť�Ĺ���
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function doQuery(){
	$("input[@name='button']").attr("disabled", true);
	queryData($("input[@name='cityId']").val(),strDate2Second($("input[@name='endDataInt']").val()));
	$("input[@name='button']").attr("disabled", false);
}

/*-----------------------------------------------------
//������:		queryData
//����  :	cityId-->����ID�����ơ�00����
			endDataInt-->ʱ��ƫ�Ƶ���
//����  :	��Ծ��������ص������ṩ��ѯ
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function queryData(cityId,endDataInt){
	$("div[@id='resultData']").html("���ڻ�ȡͳ������...");
	startProgress();
	var url = "<s:url value='/gwms/report/bindMonthCount!getReportData.action'/>";
	$.post(url,{
		cityId:cityId,
		endDataInt:endDataInt
	},function(ajax){
	    $("div[@id='resultData']").html("");
		$("div[@id='resultData']").append(ajax);
		stopProgress();
	});	
}

/*-----------------------------------------------------
//������:		queryData
//����  :	cityId-->����ID�����ơ�00����
			endDataInt-->ʱ��ƫ�Ƶ���
//����  :	����excel��ѯ
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function queryDataForExcel(cityId,endDataInt){
	var url = "<s:url value='/gwms/report/bindMonthCount!getReportData.action'/>";
	document.reportForm.action = url+"?reportType=excel&cityId="+cityId+"&endDataInt="+endDataInt;
	document.reportForm.method = "post";
	document.reportForm.submit();
}

/*-----------------------------------------------------
//������:		openUser
//����  :	cityId-->����ID�����ơ�00����
			endDataInt-->ʱ��ƫ�Ƶ���
//����  :	��Բ�ѯ�Ľ�����ʾ�û��б�
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function openUser(cityId,endDataInt,gwType){
	openNewWindow("../../Report/UserList4State.jsp?city_id="+cityId+"&endTime="+endDataInt+"&gw_type="+gwType);
}

/*-----------------------------------------------------
//������:		openDevice
//����  :	cityId-->����ID�����ơ�00����
			endDataInt-->ʱ��ƫ�Ƶ���
//����  :	��Բ�ѯ�Ľ�����ʾ�豸�б�
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function openDevice(cityId,endDataInt,gwType){
	openNewWindow("../..//Report/DeviceList4State.jsp?city_id="+cityId+"&endTime="+endDataInt+"&binddate=binddate");
}

/*-----------------------------------------------------
//������:		openNewWindow
//����  :	surl-->��Ҫ�򿪴��ڵ�url
//����  :	���ݴ��������������
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-13 of By qxq
------------------------------------------------------*/
function openNewWindow(surl){
	window.open(surl,"","left=20,top=20,height=450,width=700,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

/*-----------------------------------------------------
//������:		strDate2Second
//����  :	dateStr-->ʱ���ַ���(2009-02-12)
//����  :	�����ַ���תΪʱ����(1970-01-01 00:00:00)
//����ֵ:		long ��
//˵��  :	
//����  :	Create 2009-9-17 of By qxq
------------------------------------------------------*/
function strDate2Second(dateStr){
	var temp = dateStr.split('-')
	var reqReplyDate = new Date();
	reqReplyDate.setYear(temp[0]);
    reqReplyDate.setMonth(temp[1]-1);
    reqReplyDate.setDate(temp[2]);
	return Math.floor(reqReplyDate.getTime()/1000);
}

</SCRIPT>
	
</head>
<body>
<form name="frm" action="" method="post">
<br>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
 		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">�����±���</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"/></td>
					</tr>
				</table>
			</td>
		</tr>
 		<tr>
  			<td>
  	 			<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
			  	 	<tr>
			  	 		<TH colspan="4">�����±���</TH>
			  	 	</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%" align="right">ͳ��ʱ��</td>
						<td colspan="3" align="left">
							<input type="text" size="10" name="endDataInt" value="<s:property value="endData"/>" readonly class=bk/>
							<img name="endimg" onclick="new WdatePicker(document.frm.endDataInt,'%Y-%M-%D',false,'whyGreen')" 
								src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="ѡ��"/>(YYYY-MM-DD)
							&nbsp;&nbsp; <font color="red">ͳ��ʱ��Ϊ��ֹ��ʾʱ���ǰһ�죡</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column colspan="4" align="right">
							<input type="button" name="button" value=" ͳ  �� " onclick="doQuery()" class=jianbian/>
							<input type="hidden" value="<s:property value="cityId"/>" name="cityId"/>
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
	</TABLE>
</form>
<form name="reportForm"></form>
</body>
</html>
<%@ include file="../foot.jsp"%>
