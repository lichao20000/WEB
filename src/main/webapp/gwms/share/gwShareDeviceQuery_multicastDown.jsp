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
<script type="text/javascript" src="<s:url value="/Js/commonUtil.js"/>"></script>
<%
	String gw_type = request.getParameter("gw_type");
	if(null == gw_type ||  "".equals(gw_type)){
		gw_type="1";  
	}
%>

<SCRIPT LANGUAGE="JavaScript">

var area = '<%=shortName%>';


function do_query(){
	$("div[@id='QueryData']").html("");
	setTimeout("gwShare_queryDevice()", 2000);
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
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("�����ϴ��ļ���");
        return;
    }

    url = url + "&gwShare_fileName=" + gwShare_fileName;
    //��ҳ���Ǻӱ���ͨ����
	url = url +"&refresh=" + new Date().getTime();
	url=url+"&gw_type="+<%=gw_type %>;
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	if(!has_showModalDialog) return;
	if(typeof(returnVal)=='undefined'){
		returnVal = window.returnValue;
		deviceResult(returnVal);
		//return;
	}else{
		deviceResult(returnVal);
	}
}





var cqSoftCitys = "";



</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="gwShare_selectForm" action="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="1" />
<input type="hidden" name="gwShare_queryResultType" value="radio" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_gaoji_value" value="none" />
<input type="hidden" name="gwShare_import_value" value="none" />
<input type="hidden" name="gwShare_upimport_value" value="none" />
<input type="hidden" name="isBatch" value="<%=isBatch %>" />
<input type="hidden" name="batchType" 	value="0">
<input type="hidden" name="isShowGJ" value="0" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="5" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="5" CLASS="green_foot">
					1����Ҫ������ļ���ʽ����Excel���ı��ļ�����txt��ʽ �� <br>
					2���ļ��ĵ�һ��Ϊ�����У������û��˺š���
					 <br>
					3���ļ�ֻ��һ�С�
					 <br>
					4���ļ�������Ҫ����2000������Ӱ�����ܡ�
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="6" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_query()" class=jianbian
						name="gwShare_queryButton" value=" �����ļ� " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
</form>