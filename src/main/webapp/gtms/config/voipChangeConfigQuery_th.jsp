<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>

  <%  String gw_type = "1"; %>

<SCRIPT LANGUAGE="JavaScript">

function do_query(){
	gwShare_queryDevice();
}


/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryDevice(){
	
	var	width=500;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gtms/config/voipChangeConfigAction!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	if(""==gwShare_fileName){
		alert("�����ϴ��ļ���");
		return;
	}
	url = url + "&gwShare_fileName=" + gwShare_fileName;
	url = url +"&refresh=" + new Date().getTime();
	url = url +"&gwShare_thpage=1";
	var tips = "";
	var isHaveBind;
	var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	if(typeof(returnVal)=='undefined'){
		return;
	}else{
		deviceResult(returnVal);
	}
}


/*------------------------------------------------------------------------------
//������:		��дreset
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ��3�������ѯ
//����  :	��ҳ���������
//����ֵ:		ҳ������
//˵��  :	
//����  :	Create 2010-4-26 of By qxq
------------------------------------------------------------------------------*/
function gwShare_revalue(){
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
}

/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/

$(function(){
gwShare_queryChange('3');
});

function gwShare_queryChange(change){
	var gwShare_import_value = $("input[@name='gwShare_import_value']").val();
	$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
	$("input[@name='gwShare_queryType']").val("3");
	$("input[@name='gwShare_jiadan']").css("display","");
	$("input[@name='gwShare_import']").css("display","none");
	$("input[@name='gwShare_up_import']").css("display","none");
	$("tr[@id='gwShare_tr31']").css("display","");
	$("tr[@id='gwShare_tr32']").css("display","");
	$("input[@name='gwShare_queryButton']").val("�����ļ�");
}

function downloadCase() {
	var url = "<s:url value='/gtms/config/voipChangeConfigAction!downModleTH.action'/>";
	window.open(url);
}

function gwShare_setUpImport(){
	$("input[@name='gwShare_up_import']").css("display","");
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
<form name="gwShare_selectForm" action="<s:url value="/gtms/config/voipChangeConfigAction!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="5" />
<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
<input type="hidden" name="gwShare_import_value" value="none" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr><th colspan="4" id="gwShare_thTitle">�� �� �� ѯ</th></tr>
				<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="3" CLASS="green_foot">
					1����Ҫ������ļ���ʽ�����°�Excel����xlsx��ʽ .
					<button onclick="downloadCase()" >�������xlsxģ��</button>
					 <br>
					2���ļ�������Ҫ����200�У�����Ӱ�����ܡ�
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian style="CURSOR:hand" style="display: none"
							onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="�����ѯ" />
						<input id="query" type="button" onclick="javascript:do_query()" class=jianbian 
							name="gwShare_queryButton" value=" �� ѯ " />
						<!-- <input id="reset" type="button" class=jianbian onclick="javascript:gwShare_revalue()" 
							name="gwShare_reButto" value=" �� �� " /> -->
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
</form>