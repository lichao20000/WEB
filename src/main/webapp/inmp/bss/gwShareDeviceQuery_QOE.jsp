<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/inmp/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/inmp/WdatePicker.js"></script>

  <%
 	String gw_type = request.getParameter("gw_type");
  if(null == gw_type ||  "".equals(gw_type)){
	  gw_type="1";  
  }
  boolean isMatchSQL = false;
  
long roleId = ((UserRes)session.getAttribute("curUser")).getUser().getRoleId();
String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
//���յ�ʡ���Ĺ���ԱȨ�޲ſ����Զ�sql��ѯ�豸
if("ah_dx".equals(shortName)&&(1==roleId||2==roleId))
{
	isMatchSQL = true;
}
boolean allSelect = "ah_dx".equals(shortName);
%>


<SCRIPT LANGUAGE="JavaScript">
	var servType = null;
	$(function(){
		servType =getUrlParam('servType');
	if("single_on"==servType){
		$("input[@name='gwShare_queryType']").val("1");
		$("#singlePart").show();
		$("#batchPart").hide();
		$("#doButton_a").val("�� ѯ");
		$("#gwShare_thTitle").text("�� �� �� ѯ");
	}else if("single_off"==servType){
		$("input[@name='gwShare_queryType']").val("1");
		$("#batchPart").hide();
		$("#singlePart").show();
		$("#doButton_a").val("�� ѯ");
		$("#gwShare_thTitle").text("�� �� �� ѯ");
	}else if("batch_on"==servType){
		$("input[@name='gwShare_queryType']").val("3");
		$("#singlePart").hide();
		$("#batchPart").show();
		$("#doButton_a").val("�����ļ�");
		$("#gwShare_thTitle").text("�� �� �� ѯ");
	}else if("batch_off"==servType){
		$("input[@name='gwShare_queryType']").val("3");
		$("#singlePart").hide();
		$("#batchPart").show();
		$("#doButton_a").val("�����ļ�");
		$("#gwShare_thTitle").text("�� �� �� ѯ");
	}else{
		$("input[@name='gwShare_queryType']").val("1");
		$("#singlePart").show();
		$("#batchPart").hide();
		$("#doButton_a").val("�� ѯ");
		$("#gwShare_thTitle").text("�� �� �� ѯ");
	}
});
function do_test()
{
	//��ȡ��������ݣ�trimһ��
	//var gwShare_queryParam = document.gwShare_selectForm.gwShare_queryParam.value; 
	  var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		//$.trim($("input[@name='gwShare_queryParam']").val());
		gwShare_queryParam = $.trim(gwShare_queryParam);
	
	var title = document.getElementById("gwShare_thTitle").innerHTML;
	if(title == "�� �� �� ѯ"){
		var gwShare_queryFields = document.getElementsByName("gwShare_queryField");
		if(0 == gwShare_queryParam.length){
			alert("�������ѯ������");
			//document.gwShare_selectForm.gwShare_queryParam.focus();
			$("input[@name='gwShare_queryParam']").focus();
			return false;
		}	
		if(gwShare_queryFields[0].checked){
			if(gwShare_queryParam.length<6&&gwShare_queryParam.length>0){
				alert("�������������6λ�豸���кŽ��в�ѯ��");
				document.gwShare_selectForm.gwShare_queryParam.focus();
				return false;
				}
			}
	}

  if(title == "�� �� �� ѯ"){
			return true;
		}
	return true;
}




function gwShare_queryField_selected(value){
	$("input[@name='gwShare_queryField_temp']").val(value.value);
}
function do_query(){
	if(!do_test()){
		return;
	}
	document.getElementById("dataForm").contentWindow.document.body.innerText = "";
	setTimeout("gwShare_queryDevice()", 2000);
}
function gwShare_queryDevice(){
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
   if("1"==gwShare_queryType){
    var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
		url = url + "&gwShare_queryField=" + gwShare_queryField;
		url = url + "&gwShare_queryParam=" + gwShare_queryParam;
	}else {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
	}
	url = url +"&refresh=" + new Date().getTime();
	url=url+"&gw_type="+<%=gw_type %>;
	var tips = "";
	var isHaveBind;
	if ("single_on"==servType||"single_off"==servType||""==servType) {
		var gwShare_queryField = $("input[@name='gwShare_queryField_temp']").val();
	  var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
		var isBindUrl = "<s:url value='/gtms/config/qoeFunctionAct!isBind.action'/>";
		$.post(isBindUrl, {
			gwShare_queryField:gwShare_queryField,
			gwShare_queryParam:gwShare_queryParam
    	 },function(ajax){
     		isHaveBind = ajax;
    	 //alert(isHaveBind);
 		if(isHaveBind == 0){
			alert("��ѯ��������ڣ��޷���ͨ");
 			tips = "��ѯ��������ڣ��޷���ͨ";
 			return;
		}else if(isHaveBind == 2){
			alert("�ն�δ���û����޷���ͨ");
 			tips = "�ն�δ���û����޷���ͨ";
 			return;
		}else if(isHaveBind == 3){
			alert("�û�δ���նˣ��޷���ͨ");
 			tips = "�û�δ���նˣ��޷���ͨ";
 			return;
		}else if(isHaveBind == 4){
			alert("�˺Ų�Ψһ��������豸���кŲ�ѯ");
 			tips = "�˺Ų�Ψһ��������豸���кŲ�ѯ";
 			return;
		}else{
			var isQoeUrl = "<s:url value='/gtms/config/qoeFunctionAct!isQoe.action'/>";
			$.post(isQoeUrl, {
				deviceIds : ajax
		     },function(ajax){
		     	if(ajax == 0){
		 			alert("�ն˰汾��֧�ֿ�ͨ����ͨʧ��");
		  			tips = "�ն˰汾��֧�ֿ�ͨ����ͨʧ��";
		  			return;
		 		}else{
		 			var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised'); 
		 	 		if(typeof(returnVal)=='undefined'){
		 	 			return;
		 	 		}else{
		 	 			deviceResult(returnVal);
		 	 		}
		 		}
		     });
		}
	});
}else if("batch_on"==servType||"batch_off"==servType){
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised'); 
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
function gwShare_revalue(){
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	
		$("input[@name='gwShare_queryField']").get(0).checked = true;
		$("input[@name='gwShare_queryParam']").val("");
	
}
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	
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
<form name="gwShare_selectForm" action="<s:url value="/inmp/bss/gwDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="" />
<input type="hidden" name="gwShare_queryResultType" value="radio" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_import_value" value="none" />


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<th colspan="4" id="gwShare_thTitle"></th>
	<tr>
		<td bgcolor=#999999>
			<table id="singlePart" border=0 cellspacing=1 cellpadding=2 width="100%" align="center" display="none">
				<tr bgcolor="#FFFFFF"  >
					<td colspan="4" align="center" width="100%">
						<div>
							<input type="input" class="bk" name="gwShare_queryParam" size="60" maxlength="60"/>
							<!-- <label style="CURSOR:hand" onclick="javascript:gwShare_queryChange('2');">�߼���ѯ</label> -->
							<br /> 
							
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF"  >
					<td colspan="4" align="center" width="100%">
						<input type="radio" name="gwShare_queryField" value="deviceSn" checked onclick="gwShare_queryField_selected(this)"/> �豸���к� &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="username" onclick="gwShare_queryField_selected(this)"/> LOID &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="kdname" onclick="gwShare_queryField_selected(this)"/> ����˺� &nbsp;&nbsp;
					</td>
				</tr>
			</table>
			<table id="batchPart" border=0 cellspacing=1 cellpadding=2 width="100%" align="center" display="none">
				<tr  bgcolor="#FFFFFF" >
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/inmp/bss/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr >
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="3" CLASS="green_foot">
					1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��
					 <br>
					2���ļ��ĵ�һ��Ϊ�����У������û��˺š������豸���кš����ߡ�����˺š���
					 <br>
					3���ļ�ֻ��һ�С�
					 <br>
					4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
					</td>
				</tr>
			</table>
		</td>
		
	</tr>
	<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" id="doButton_a" onclick="javascript:do_query()" class=jianbian 
						name="gwShare_queryButton" value="" />
						<input type="button" class=jianbian onclick="javascript:gwShare_revalue()" 
						name="gwShare_reButto" value=" �� �� " />
					</td>
		</tr>
</TABLE>
</form>