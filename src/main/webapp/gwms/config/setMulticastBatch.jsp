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
<title>�ն��鲥����</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<%
	String gwType = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_queryChange('3');
	$("input[@name='gwShare_jiadan']").css("display","none");
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
$(function(){
	
});
var gw_type = "<%= gwType%>";
//��ѯ�������
var deviceId="";
var sn="";
var loid="";
var userid="";
function deviceResult(returnVal){
	$("button[@name='subBtn']").attr("disabled", false);
	deviceId = "";
	sn="";
	loid="";
	userid="";
	$("#result1").html("");
	var totalNum = returnVal[0];
	if(returnVal[0] == 0){
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0;i < deviceIdArray.length;i++){
			//����������deviceId
			deviceId +=  deviceIdArray[i][0]+",";
			sn +=  deviceIdArray[i][2]+",";
			loid +=  deviceIdArray[i][13]+",";
			userid += deviceIdArray[i][14]+",";
		}
		var endIndex1 = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex1);
		
		endIndex1 = sn.lastIndexOf(",");
		sn = sn.substring(0,endIndex1);
		
		endIndex1 = loid.lastIndexOf(",");
		loid = loid.substring(0,endIndex1);
		
		endIndex1 = userid.lastIndexOf(",");
		userid = userid.substring(0,endIndex1);
		
		if(totalNum > 5000){
			alert("�豸��������5000̨��Ӱ�쵽��������");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// �ж�δ�����Ե�����
		var url = "<s:url value='/gwms/config/serviceManSheet!queryMulticastNum.action'/>"; 
		$.post(url,{} ,function(ajax){
	          if("goon"!=ajax)
	           {
	           		alert(ajax);
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
	}
}

//ҵ���·�
function doBusiness(){
	if('' == deviceId){
		alert("���Ȳ�ѯ����ѡ���豸");
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/gwms/config/serviceManSheet!setMulticastBatch.action'/>";
	$("button[@name='subBtn']").attr("disabled", true);
	$.post(url,{
		deviceId:deviceId,
		sn:sn,
		loid:loid,
		userId:userid,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='result1']").html("֪ͨ��̨ : ");
		$("button[@name='subBtn']").attr("disabled", false);
		$("div[@id='result1']").append(ajax);
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
						<div align="center" class="title_bigwhite">�����鲥����</div>
						</td>
					</tr>
				</table>
				<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</TD>
			</TR>
			<TR id="bisShowName">
				<TH colspan="4">�����鲥����</TH>
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
					
					<TR align="left" id="doBiz" STYLE="display:">
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" id="subBtn" onclick="doBusiness()">��ʼ�鲥����</button>
							<input type="hidden" name="devId" value="">
						</TD>
					</TR>
					<TR id="resultTR1" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" width="15%" id="resultTD1">ִ�н��</TD>
						<TD colspan="3">
						<DIV id="result1"></DIV>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4"><!-- ��ע���豸��������ߣ�ϵͳ�����豸���ߺ��һʱ�����ҵ���·��� --></TD>
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