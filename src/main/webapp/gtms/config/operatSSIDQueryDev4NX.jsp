<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/Js/jquery.js"></script>
<script type="text/javascript" src="/Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_queryChange("1");
});

function gwShare_queryChange(change)
{
	$("input[@name='deviceIds']").val("");
	$("div[@id='QueryData']").html("");
	switch (change)
	{
		case "1":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("1");
			$("input[@name='gwShare_jiadan']").css("display","none");
			$("input[@name='gwShare_import']").css("display","");
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='queryDev']").css("display","");
			$("tr[@id='nextJoin']").css("display","none");
			$("tr[@id='gwShare_tr30']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val("��  ѯ");
			$("input[@name='gwShare_queryButton']").css("display","");
			break;
		case "3":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("3");
			$("input[@name='gwShare_jiadan']").css("display","");
			$("input[@name='gwShare_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='queryDev']").css("display","none");
			$("tr[@id='nextJoin']").css("display","");
			$("tr[@id='gwShare_tr30']").css("display","");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("input[@name='gwShare_queryButton']").css("display","none");
			break;
	}
}

function do_query()
{
	var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
	
	var title = document.getElementById("gwShare_thTitle").innerHTML;
	if(title == "�� �� �� ѯ")
	{
		if(0 == gwShare_queryParam.length){
			alert("�������ѯ������");
			$("input[@name='gwShare_queryParam']").focus();
			return false;
		}
		
		//��ȡѡ�������
		var gwShare_queryFields = document.getElementsByName("gwShare_queryField");
		if(gwShare_queryFields[0].checked)
		{
			if(gwShare_queryParam.length<6 && gwShare_queryParam.length>0){
				alert("�������������6λ�豸���кŽ��в�ѯ��");
				document.gwShare_selectForm.gwShare_queryParam.focus();
				return false;
			}
		}
	}
	
	$("div[@id='QueryData']").html("");
	var url="<s:url value='/gtms/config/operatSSID!queryDeviceList.action'/>"
  				+ "?gwShare_queryResultType="+$("input[@name='gwShare_queryResultType']").val()
   				+ "&gwShare_queryType=" + $("input[@name='gwShare_queryType']").val()
   				+ "&gwShare_queryField=" + $("input[@name='gwShare_queryField'][@checked]").val()
				+ "&gwShare_queryParam=" + gwShare_queryParam;
	var win='dialogWidth=800px;dialogHeight=450px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	var returnVal=window.showModalDialog(url,'',win);    
	if(typeof(returnVal)=='undefined'){
		return;
	}else{
		deviceResult(returnVal);
	}
}

function gwShare_setImport()
{
	$("input[@name='gwShare_import']").css("display","");
}
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="gwShare_selectForm" target="dataForm"
	action="<s:url value='/gtms/config/operatSSID!queryDeviceList.action'/>" >
<input type="hidden" name="gwShare_queryType" value="1" />
<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
 
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr>
					<th colspan="4" id="gwShare_thTitle" STYLE="display:">�� �� �� ѯ</th>
				</tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr11" STYLE="display:">
					<td colspan="4" align="center" width="100%">
						<div>
							<input type="input" class="bk" name="gwShare_queryParam" size="64" maxlength="64"/>
							<br /> 
							<div id="gwShare_msgdiv" STYLE="display:none" > </div>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr12" bgcolor="#FFFFFF" STYLE="display:">
					<td colspan="4" align="center" width="100%">
						<input type="radio" class=jianbian name="gwShare_queryField" value="deviceSn" checked />
							 �豸���к� &nbsp;&nbsp;
						<input type="radio" class=jianbian name="gwShare_queryField" value="username"/>
							 LOID &nbsp;&nbsp;
						<input type="radio" class=jianbian name="gwShare_queryField" value="kdname"/>
							 ����˺�  &nbsp;&nbsp;
					</td>
				</tr>
				<tr id="gwShare_tr30" bgcolor="#FFFFFF"  style="display:none">
					<td align="right" width="15%">��������</td>
					<td colspan="3" width="85%">
						<input type="text" name="task_name" value="" size="64" maxlength="128"/>
					</td>
				</tr>
				<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" frameborder=0 scrolling=no height="20" width="100%" 
								src="<s:url value='/gwms/share/FileUpload.jsp?noDownLoad=yes'/>" >
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="3" CLASS="green_foot">
						1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��<br>
						2���ļ��ĵ�һ��Ϊ�����У������豸���кš������ߡ�����˺š������ߡ�LOID����<br>
						3���ļ�ֻ��һ�С�
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_query()" class=jianbian 
							name="gwShare_queryButton" value=" �� ѯ " />
							&nbsp;&nbsp;
						<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
							onclick="javascript:gwShare_queryChange('1');" name="gwShare_jiadan" value="�򵥲�ѯ" />
							&nbsp;&nbsp;
						<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
							onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="�����ѯ" />
							&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
</form>