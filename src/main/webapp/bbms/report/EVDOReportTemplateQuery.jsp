<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<!-- 
EVDOͳ�Ʊ����ѯ����
��Ҫ���ݣ�ͳ��ʱ����������������·��ʹ��ʱ�Ρ�Ƶ�Ρ�����ͳ��
author��zxj 
E-mail��qixq@lianchuang.com 
since ��2009-11-24
-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="titleCN"/></title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/tab.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
/*------------------------------------------------------------------------------
//������:		
//����  :	
//����  :	��ʼ������
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
$(function(){
	var reportName = $("input[@name='reportName']").val();
	if("EVDOTYPE"==reportName){
		$("#td1").css("display","none");
		$("#td3").css("display","none");
		queryData("2");
	}else{
		queryData("1");
	}
});

/*------------------------------------------------------------------------------
//������:		query
//����  :	
//����  :	����ѡ���������ѯ���ݣ�ͬʱ����Щ�����洢����
//����ֵ:		
//˵��  :	�˺���Ϊ�����ѯʱ��������
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
function query(){
	var reportType,countType,queryDate,reportName;
	reportType = $("input[@name='reportType']").val();
	countType = $("select[@name='countType']").val();
	queryDate = $("input[@name='queryDate']").val();
	reportName = $("input[@name='reportName']").val();
	switch (reportType)
	{
		case "1":
		{
			 $("input[@name='dayCountType']").val(countType);
			 $("input[@name='dayQueryDate']").val(queryDate);
			break;
		}
		case "2":
		{
			$("input[@name='weekCountType']").val(countType);
			$("input[@name='weekQueryDate']").val(queryDate);
			break;
		}
		case "3":
		{
			$("input[@name='monthCountType']").val(countType);
			$("input[@name='monthQueryDate']").val(queryDate);
			break;
		}
	}
	queryDataCore(reportName,reportType,countType,strDate2Second(queryDate));
}

/*------------------------------------------------------------------------------
//������:		queryData
//����  :	reportType-->��������(1���ձ���2���ܱ���3���±���)
//����  :	����reportType����������ݣ�Ȼ�����queryDataCore
	   :    �����Ǳ������͵�ת�䣬��Ҫ�����ص����ݸ�ֵ����ز���
//����ֵ:		
//˵��  :	�˺���Ϊ���ѡ�Կ�ʱ��������
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
function queryData(reportType){
	var countType,queryDate,reportName;
	reportName = $("input[@name='reportName']").val();
	$("input[@name='reportType']").val(reportType);
	switch (reportType)
	{
		case "1":
		{
			$("#td1").attr("class","curendtab_bbms");
			$("#td2").attr("class","endtab_bbms");
			$("#td3").attr("class","endtab_bbms");
			countType = $("input[@name='dayCountType']").val();
			queryDate = $("input[@name='dayQueryDate']").val();
			break;
		}
		case "2":
		{
			$("#td1").attr("class","endtab_bbms");
			$("#td2").attr("class","curendtab_bbms");
			$("#td3").attr("class","endtab_bbms");
			countType = $("input[@name='weekCountType']").val();
			queryDate = $("input[@name='weekQueryDate']").val();
			break;
		}
		case "3":
		{
			$("#td1").attr("className","endtab_bbms");
			$("#td2").attr("className","endtab_bbms");
			$("#td3").attr("className","curendtab_bbms");
			countType = $("input[@name='monthCountType']").val();
			queryDate = $("input[@name='monthQueryDate']").val();
			break;
		}
	}
	$("select[@name='countType']").val(countType);
	$("input[@name='queryDate']").val(queryDate);
	queryDataCore(reportName,reportType,countType,strDate2Second(queryDate));
}

/*------------------------------------------------------------------------------
//������:		queryDataCore
//����  :	reportType-->��������(1���ձ���2���ܱ���3���±���)
	   :    countType-->ͳ������(1��������2������ҵ��)
	   :    queryDate-->ͳ��ʱ��(���ʽ)
//����  :	�ύ��������ѯ����
//����ֵ:		
//˵��  :	
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
function queryDataCore(reportName,reportType,countType,queryDate){
	var url = "bbms/report/evdoTemplate!getReportData.action";
	$("#dataList").html("��������ͳ����...");
	$.post(url,{
		reportName:reportName,
		reportType:reportType,
		countType:countType,
		queryDate:queryDate
    },function(mesg){
    	$("#dataList").html(mesg);
    });
}

/*------------------------------------------------------------------------------
//������:		queryDataForExcel
//����  :	reportType-->��������(1���ձ���2���ܱ���3���±���)
	   :    countType-->ͳ������(1��������2������ҵ��)
	   :    queryDate-->ͳ��ʱ��(���ʽ)
//����  :	�ύ������������Ӧ��excel��ʽ
//����ֵ:		excel��ʽ����
//˵��  :	
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
function queryDataForExcel(reportName,reportType,countType,queryDate){
	var url = "bbms/report/evdoTemplate!getReportData.action";
	url = url + "?reportName="+reportName+"&reportType="+reportType+"&countType="+countType+"&queryDate="+queryDate;
	url = url+"&isReport=excel"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

/*------------------------------------------------------------------------------
//������:		strTime2Second
//����  :	dateStr-->ʱ���ַ���(2009-02-12 00:00:00)
//����  :	�����ַ���תΪʱ����(1970-01-01 00:00:00)
//����ֵ:		long ��
//˵��  :	
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
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

/*------------------------------------------------------------------------------
//������:		strDate2Second
//����  :	dateStr-->ʱ���ַ���(2009-02-12)
//����  :	�����ַ���תΪʱ����(1970-01-01 00:00:00)
//����ֵ:		long ��
//˵��  :	
//����  :	Create 2009-11-19 of By qxq
------------------------------------------------------------------------------*/
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
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
  		<TD>
			<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
				<TR>
					<TD>
						<TABLE width="30%" border=0 cellspacing=0 cellpadding=0>
							<TR height=30>
								<TH class="curendtab_bbms" id="td1"><a class="tab_A" href="javascript:queryData('1');">��ͳ��</a></TH>
								<TH class="endtab_bbms" id="td2"><a class="tab_A" href="javascript:queryData('2');">��ͳ��</a></TH>
								<TH class="endtab_bbms" id="td3"><a class="tab_A" href="javascript:queryData('3');">��ͳ��</a></TH>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR>
					<TD bgcolor="#999999">
						<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
	   						<tr bgcolor="#FFFFFF" id="query1" style="display:">
								<td align=right>
									ͳ�Ʒ�ʽ��
									<select name="countType" selected="<s:property value="countType" />">
										<option value="1">������</option>
										<option value="2">����ҵ</option>
									</select>
									&nbsp;
									ͳ��ʱ�䣺
									<input type="text" size="12" name="queryDate" value='<s:property value="queryDateStr" />' readonly class=bk/>
									<img name="shortDateimg" onclick="new WdatePicker(document.form1.queryDate,'%Y-%M-%D',true,'whyGreen')"
										src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="ѡ��">(YYYY-MM-DD)
									<input type="button" name="btn0" value=" �� ѯ " class=jianbian onclick="javascript:query()">
									<input type="hidden" name="reportType" value='<s:property value="reportType" />'/>
									<input type="hidden" name="dayQueryDate" value='<s:property value="queryDateStr" />'/>
									<input type="hidden" name="weekQueryDate" value='<s:property value="queryDateStr" />'/>
									<input type="hidden" name="monthQueryDate" value='<s:property value="queryDateStr" />'/>
									<input type="hidden" name="dayCountType" value='<s:property value="countType"/>'/>
									<input type="hidden" name="weekCountType" value='<s:property value="countType"/>'/>
									<input type="hidden" name="monthCountType" value='<s:property value="countType"/>'/>
									<input type="hidden" name="reportName" value='<s:property value="reportName"/>'/>
								</td>
							</tr>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD bgcolor=#999999><div id="dataList">��������ͳ����...</div></TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<form name="frm2"></form>
</body>
<br>
<br>
<%@ include file="../foot.jsp"%>