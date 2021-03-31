<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", -10);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������è��¼����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	
	var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

$(window).resize(function(){
	dyniframesize();
}); 
	function getUrlParam(name) {
 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //����һ������Ŀ�������������ʽ����
 var r = window.location.search.substr(1).match(reg); //ƥ��Ŀ�����
 if (r != null) return unescape(r[2]); return null; //���ز���ֵ
}
$(function(){
	dyniframesize();
	var servType =getUrlParam('servType');
	$("input[@name='servType']").val(servType);
	if("batch_off"==servType){
		$("#title").text("������è��¼����");
		$("#QoeOff").show();
		$("#batchOn").hide();
		$("#singleOn").hide();
		$("#doButton").val("ִ ��");
		$("#doButton").bind("click",BatchQOEOff);
	}
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setImport();
});
		
	
		
var deviceIds = "";

var deviceId ;
function QOEQuery(){
	doExecute("query");
}
function QOETest(){
	doExecute("single_on");
}
function QOEOff(){
	doExecute("single_off");
}
function BatchQOETest(){
	doExecute("batch_on");
}
function BatchQOEOff(){
	doExecute("batch_off");
}


function doExecute(do_type){
	$("#doButton").attr("disabled",true);
	var deviceIds = $("input[@name='deviceIds']").val();
	var singleOn_url = $("input[@name='singleOn_url']").val();
	var batchOn_url = $("input[@name='batchOn_url']").val();
	var batchOn_exeWay = $("select[@name='batchOn_exeWay']").val();
	var exeWay_QoeOff = $("select[@name='exeWay_QoeOff']").val();
	if(CheckForm(do_type))
	{
		$("tr[@id='trData']").show();
		
	  var service_id;
	  if("single_off"==do_type||"batch_off"==do_type){
	  	service_id="2006";
	  }
	  $("input[@name='service_id']").val(service_id);
	  $("input[@name='do_type']").val(do_type);
	  var form = document.getElementById("frm");
	  if("query"==do_type){
	  	form.action = "<s:url value='/gtms/config/qoeFunctionAct!queryQOE.action'/>";
	  }else{
	  	form.action = "<s:url value='/gtms/config/qoeFunctionAct!doConfig.action'/>";
	  }
	  form.submit();
		}else{
			$("#doButton").attr("disabled",false);
		}
	}

function CheckForm(do_type){
	if($("input[@name='deviceIds']").val()==""){
		alert("��ѡ���豸��");
		return false;
	}
	
		if("single_on"==do_type){
			var singleOn_url = $("input[@name='singleOn_url']").val();
			if("" == singleOn_url){
				alert("����д��������URL");
				return false;
			}
		}else if("batch_on"==do_type){
			var batchOn_url = $("input[@name='batchOn_url']").val();
			if("" == batchOn_url){
				alert("����д��������URL");
				return false;
			}
		}
		return true;
}

function deviceResult(returnVal){	
	deviceId="";
	$("#resultDIV").html("");
	
	var totalNum = returnVal[0];
	
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			//����������deviceId
			deviceId +=  deviceIdArray[i][0]+",";
			
		}
		if(totalNum > 20000){
			alert("�豸��������2W̨��Ӱ�쵽��������");
			$("#doButton").attr("disabled",true);
		}else{
			$("#doButton").attr("disabled",false);
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");

	  var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		$("input[@name='deviceIds']").val(deviceId);
		
	}
}


//��ѯ
function doTest()
{
	var url = "<s:url value='/gwms/service/gwBindDevQuery!queryTabBindList.action'/>";
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var is_check = $("select[@name='is_check']").val();
	var rela_dev_type = $("select[@name='rela_dev_type']").val();
	//������ѯ����LOID���߿������
	var loid = $("input[@name='loid']").val();
	var username = $("input[@name='username']").val();
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("gwShare_selectForm");
	form.action = url;
	form.submit();
}

function ToExcel() {
	var mainForm = document.getElementById("gwShare_selectForm");
	mainForm.action = "<s:url value='/gwms/service/gwBindDevQuery!NewDeviceQueryExcel.action' />";
	mainForm.submit();
}

function ToExcel1() {
	var mainForm = document.getElementById("gwShare_selectForm");
	mainForm.action = "<s:url value='/gwms/service/gwBindDevQuery!queryDeviceExcel.action' />";
	mainForm.submit();
}


</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table border="0" align="center" cellpadding="0" cellspacing="1" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" align="center"
										cellpadding="0" cellspacing="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" id="title" class="title_bigwhite" nowrap>
								</td>
								<td nowrap>
									<img src="<s:url value='../images/inmp/attention_2.gif'/>" width="15"
										height="12">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
		<TR bgcolor="#FFFFFF">
			<td colspan="4" id="sharePart">
				<%@ include file="/ids/share/gwShareDeviceQuery_bind.jsp"%>
			</td>
		</TR>
		
	<TR>
		<td colspan="4">
			<!-- չʾ���part -->
			<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
				<TR>
					<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
						name="dataForm" height="0" frameborder="0" scrolling="no"
						width="100%" src=""></iframe></TD>
				</TR>
			</TABLE>
		</td>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
