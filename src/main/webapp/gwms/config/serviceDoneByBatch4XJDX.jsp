<%--
�ն�ҵ�������·�
Version: 1.0.0
Date: 2015-08-05
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<HEAD>
<title>�ն�ҵ�������·�</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<%
	String gwType = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	//gwShare_queryChange('3');
	//$("input[@name='gwShare_jiadan']").css("display","none");
	//$("input[@name='gwShare_queryResultType']").val("checkbox");
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
$(function(){
	/*var starttime = "20:00:00";
	var endtime = "23:00:00";
	starttime = parseInt(starttime.split(":")[0]) * 3600 + parseInt(starttime.split(":")[1]) * 60 + parseInt(starttime.split(":")[2]);
	endtime = parseInt(endtime.split(":")[0]) * 3600 + parseInt(endtime.split(":")[1]) * 60 + parseInt(endtime.split(":")[2]);
	var currenttime = parseInt(new Date().toLocaleTimeString().split(":")[0]) * 3600 + parseInt(new Date().toLocaleTimeString().split(":")[1]) * 60 + parseInt(new Date().toLocaleTimeString().split(":")[2]);
	if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#queryServTypeId,#subBtn,#gwShare_queryButton,#gwShare_reButto").attr("disabled",true);
			$("#msg").html("�˹��ܽ���20:00-23:00ʹ��");
			$("#msg").css({color:"red"}); 
			$("#gwShare_tr31").hide();
		}*/
});
var gw_type = "<%= gwType%>";
//��ѯ�������
var deviceIds;
var has_showModalDialog = !!window.showModalDialog;
function deviceResult(returnVal){
	/*$("button[@name='subBtn']").attr("disabled", false);
	deviceId = "";
	$("#result1").html("");
	var totalNum = returnVal[0];
	if(returnVal[0] == 0){
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0;i < deviceIdArray.length;i++){
			//����������deviceId
			deviceId +=  deviceIdArray[i][0]+",";
		}
		var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		if(totalNum > 1000){
			alert("�豸��������1000̨��Ӱ�쵽��������");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// �ж�δ�����Ե�����
		var url = "<s:url value='/gwms/config/serviceManSheet!queryUndoNum.action'/>"; 
		var maxNum = 30000;
		$.post(url,{} ,function(ajax){
	          var num = parseInt(ajax);
	          if(num > maxNum)
	           {
	           		alert("�����������Ѵﵽ���ޣ������������ã�");
	           		$("#subBtn").attr("disabled",true);
		            return;
	           }
	    });
		$("div[@id='selectedDev']").html("<font size=2>����"+totalNum+"̨�豸</font>");
	}else{ //������ѯ
		deviceId = returnVal[2][0][0];
		if(deviceId == ""){
			$("#selectedDev").html("���û������ڻ�δ���նˣ�");
		}
	}*/
	deviceIds="";
	if(returnVal[0]==0){
		if(returnVal[2].length > 1000){
			alert("�豸��������1000̨��Ӱ�쵽��������");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// �ж�δ�����Ե�����
		/*	var url = "<s:url value='/gwms/config/serviceManSheet!queryUndoNum.action'/>";
		var maxNum = 30000;
		$.post(url,{} ,function(ajax){
			var num = parseInt(ajax);
			if(num > maxNum)
			{
				alert("�����������Ѵﵽ���ޣ������������ã�");
				$("#subBtn").attr("disabled",true);
				return;
			}
		});*/
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		if(returnVal[0] > 1000){
			alert("�豸��������1000̨��Ӱ�쵽��������");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// �ж�δ�����Ե�����
		/*var url = "<s:url value='/gwms/config/serviceManSheet!queryUndoNum.action'/>";
		var maxNum = 30000;
		$.post(url,{} ,function(ajax){
			var num = parseInt(ajax);
			if(num > maxNum)
			{
				alert("�����������Ѵﵽ���ޣ������������ã�");
				$("#subBtn").attr("disabled",true);
				return;
			}
		});*/
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
}

//ҵ���·�
function doBusiness(){
	var batchsql = $("input[@name='deviceIds']").val();
	deviceIds=$("input[@name='deviceIds']").val();
	var servTypeId = $("select[@name='queryServTypeId']").val();
	if('' == deviceIds){
		alert("���Ȳ�ѯ����ѡ���豸");
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/gwms/config/serviceManSheet!serviceDoneByBatchXJDX.action'/>";
	$("button[@name='subBtn']").attr("disabled", true);
	$.post(url,{
		deviceId:deviceIds,
		servTypeId:servTypeId,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='result1']").html("֪ͨ��̨ : ");
		$("button[@name='subBtn']").attr("disabled", false);
		if (ajax == "1"){
			$("div[@id='result1']").append("<FONT COLOR='blue'>�ɹ�!</FONT>");
		} else {
			$("div[@id='result1']").append("<FONT COLOR='red'>ʧ��!</FONT>");
		}
		$("tr[@id='resultTR1']").show();
		//�һ���ť
		$("button[@name='subBtn']").attr("disabled", true);
	});
	
} 

</script>
</HEAD>
<body>
<FORM NAME="frm" METHOD="post" action="">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD colspan="4">
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">����ҵ���·�</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"><span id="msg">����BSSҵ�������·����ն� </span></td>
					</tr>
				</table>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<td colspan="4">
					<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</TR>
			<TR id="bisShowName">
				<TH colspan="4">����ҵ���·�</TH>
			</TR>
			<TR id="bisShowContent">
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr bgcolor="#FFFFFF">
						<td colspan="6">
							<div id="selectedDev">
								���ѯ�豸��
							</div>
						</td>
					</tr>
					<TR id="bizshow" bgcolor="#FFFFFF" STYLE="display:">
						<TD class=column align="right" nowrap width="15%">ҵ������:</TD>
						<TD width="35%" >
							<SELECT name="queryServTypeId" id="queryServTypeId" class="bk">
								<OPTION value="0" selected = "selected" >ȫҵ��</OPTION>
								<OPTION value="10" >���ҵ��</OPTION>
								<OPTION value="11">itvҵ��</OPTION>
								<OPTION value="14">VOIPҵ��</OPTION>
								<ms:inArea areaCode="xj_dx" notInMode="false">
									<OPTION value="15">��������</OPTION>
									<OPTION value="20">���߹���WIFI</OPTION>
									<OPTION value="38">VPNҵ��</OPTION>
									<OPTION value="32">��Դ</OPTION>
									<OPTION value="51">wifi����</OPTION>
								</ms:inArea>
							</SELECT>
						</TD>
						<TD width="15%" class=column align="right">��������:</TD>
						<TD width="35%" id="operationTypeTd">����</TD>
					</TR>
					<TR align="left" id="doBiz" STYLE="display:">
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" id="subBtn" onclick="doBusiness()">ҵ���·�</button>
							<input type="hidden" name="deviceIds" value=""/>
							<input type="hidden" name="param" value=""/>
						</TD>
					</TR>
					<TR id="resultTR1" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" width="15%" id="resultTD1">ִ�н��</TD>
						<TD colspan="3">
						<DIV id="result1"></DIV>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4">��ע���豸��������ߣ�ϵͳ�����豸���ߺ��һʱ�����ҵ���·���</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
<%@ include file="../../foot.jsp"%>