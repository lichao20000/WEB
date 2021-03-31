<%--
Author      : ������
Date		: 2018-06-26
Desc		: ��������������
--%>
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/head.jsp"%>

<%
	String gwType = "1";
	String do_type = "2";
%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	do_type = <%=do_type%>
	gwShare_queryChange('3');
	$("#doButton").attr("disabled",true);
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	$("input[@name='starttime']").val("");
	$("input[@name='intervalTime']").val("");
});
function CheckForm(){
	if($("input[@name='deviceId']").val()==""){
		alert("��ѡ���û���");
		return false;
	}
	return true;
}
var deviceId;
var serv_account;
var device_serialnumber;
var city_name;
var starttime;
var intervalTime;

//��ѯ���û�
function deviceResult(returnVal){
	$("#doButton").attr("disabled",false);
	deviceId = "";
	serv_account = "";
	device_serialnumber = "";
	city_name = "";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	var totalNum = returnVal[0];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			//����������deviceId
			deviceId +=  deviceIdArray[i][0]+",";
			serv_account +=  deviceIdArray[i][1]+",";
			device_serialnumber +=  deviceIdArray[i][2]+",";
			city_name +=  deviceIdArray[i][3]+",";
		}
		deviceId = deviceId.substring(0, deviceId.lastIndexOf(","));
		serv_account = serv_account.substring(0, serv_account.lastIndexOf(","));
		device_serialnumber = device_serialnumber.substring(0, device_serialnumber.lastIndexOf(","));
		city_name = city_name.substring(0, city_name.lastIndexOf(","));
		$("div[@id='selectedDev']").html("<font size=2>����"+totalNum+"���û�</font>");
	}
	else{
		//������ѯ
		deviceId= returnVal[2][0][0] ;
		serv_account = returnVal[2][0][1] ;
		device_serialnumber = returnVal[2][0][2] ;
		city_name = returnVal[2][0][3] ;
		if(deviceId==""){
			$("#selectedDev").html("���û�������!");
		}
	}
}
function doExecute(){
	var maxNetNum = $("input[@id='maxNetNum']").val();
	var username  = $("select[@id='selectMap']").val();
	var starttimeinput =  $("input[@name='starttime']").val();
	var intervalTime =  $("input[@name='intervalTime']").val();
	var reg=/^\d+$/;
	if($.trim(starttimeinput)==""){
		alert("��ʼʱ�䲻��Ϊ��!");
		return false;
	}
	if(intervalTime != "" && !reg.test(intervalTime))
	{
		if(0 != intervalTime)
		{
			alert("���ʱ��ֻ��Ϊ����!");
			return false;
		}
	}
	if(parseInt(intervalTime) > 1000)
	{
		alert("���ʱ�䲻���Գ���1000!");
		return false;
	}
	var url = "<s:url value="/gtms/stb/diagnostic/StbBatchRestart!execStbBatchRestart.action"/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("���ڲ��������Ե�....");
	if(CheckForm()){
		$.post(url,{
			intervalTime : intervalTime,
            deviceIds : deviceId,
            starttime : starttimeinput,
            serv_account : serv_account,
            device_serialnumber : device_serialnumber
         },function(ajax){
				$("#resultDIV").html("");
				$("#resultDIV").append(ajax);
				$("div[@id='QueryData']").append(ajax);
				$("div[@id='QueryData']").html("");
				$("button[@name='button']").attr("disabled", true);
          });
	}
} 
</script>
</head>
<%@ include file="/toolbar.jsp"%>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%">
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										������������������</td>
									<td nowrap><img
										src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12"> <span id="msg" style="color: red"></span></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4"><%@ include file="/gtms/stb/share/gwShareDeviceQuery_StbBatchRestart.jsp"%>
						</td>
					</tr>
					<tr>
						<th colspan="4" align="center">��������</th>
					</tr>
					<tr>
						<td>
							<form name="frm" method="post" action="" onsubmit="return CheckForm()">
								<input type="hidden" name="deviceIds" value="" />
								<table width="100%" border=0 cellspacing=0 cellpadding=0
									align="center">
									<tr>
										<td bgcolor=#999999>
											<table border=0 cellspacing=1 cellpadding=2 width="100%"
												id="table_showConfig">
												<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev">
														���ѯ��������Ϣ��
													</div>
												</td>
												</tr>
												<tr bgcolor="#FFFFFF" id="tr_strategybs">
													<td class=column align=center width="15%">��ʼʱ��</td>
													<td width="20%"><input type="text" name="starttime"
													readonly class=bk value="<s:property value="starttime"/>">
													<img name="shortDateimg"
														onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
														src="../../../images/dateButton.png" width="13"
														height="12" border="0" alt="ѡ��"></td>
													<td align="right" width="15%">���ʱ�䣺</td>
													<td width="20%"><input type="text" name="intervalTime"
												        class=bk value="<s:property value="intervalTime"/>">
													</td>
												</tr>
												<tr>
													<td colspan="4" align="right" CLASS="green_foot"><INPUT
														TYPE="button" id="doButton" name="doButton"
														onclick="doExecute()" value=" ִ��" class=btn></td>
												</tr>
												<tr bgcolor="#FFFFFF">
													<td colspan="6" align="left" class="green_foot">
														<div id="resultDIV" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td height="20"></td>
									</tr>
									<tr id="trData" style="display: none">
										<td class="colum">
											<div id="QueryData"
												style="width: 100%; z-index: 1; top: 100px">
												���ڲ��������Ե�....</div>
										</td>
									</tr>
									<tr>
										<td height="20"></td>
									</tr>
								</table>
							</form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td HEIGHT=20>&nbsp; <IFRAME ID=childFrm SRC=""
					STYLE="display: none"></IFRAME> <IFRAME ID=childFrm1 SRC=""
					STYLE="display: none"></IFRAME> <IFRAME ID=childFrm2 SRC=""
					STYLE="display: none"></IFRAME>
			</td>
		</tr>
	</table>
</body>
</html>
<%@ include file="/foot.jsp"%>
