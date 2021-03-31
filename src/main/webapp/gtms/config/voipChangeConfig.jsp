<%--
Author      : �ο���
Date		: 2015-05-25
Desc		: ���DHCP�Ĺر�
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%

 %> 
<html>
<head>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	$("#doButton").attr("disabled",true);
	gwShare_queryChange('3');
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function CheckForm(){
	if($("input[@name='deviceId']").val()==""){
		alert("��ѡ���û���");
		return false;
	}
	return true;
}
var deviceId;
var netusername;
var city_id;
var loid;
var oui;
var device_serialnumber;
var faultListStr;
var userId;
var taskid;


//��ѯ���û�
function deviceResult(returnVal){
	$("#doButton").attr("disabled",false);
	deviceId = "";
	netusername = "";
	city_id = "";
	loid = "";
	oui = "";
	device_serialnumber = "";
	userId = "";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	var totalNum = returnVal[0];
	faultListStr = returnVal[3];
	taskid = returnVal[4];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			//����������deviceId
			deviceId +=  deviceIdArray[i][3]+",";
			netusername +=  deviceIdArray[i][1]+",";
			city_id +=  deviceIdArray[i][2]+",";
			loid +=  deviceIdArray[i][0]+",";
			oui +=  deviceIdArray[i][4]+",";
			device_serialnumber +=  deviceIdArray[i][5]+",";
			userId +=  deviceIdArray[i][6]+",";
		}
		deviceId = deviceId.substring(0, deviceId.lastIndexOf(","));
		netusername = netusername.substring(0, netusername.lastIndexOf(","));
		city_id = city_id.substring(0, city_id.lastIndexOf(","));
		loid = loid.substring(0, loid.lastIndexOf(","));
		oui = oui.substring(0, oui.lastIndexOf(","));
		device_serialnumber = device_serialnumber.substring(0, device_serialnumber.lastIndexOf(","));
		userId = userId.substring(0, userId.lastIndexOf(","));
		
		$("div[@id='selectedDev']").html("<font size=2>����"+totalNum+"���û�</font>");
	}
	else{
		//������ѯ
		deviceId= returnVal[2][0][3] ;
		netusername = returnVal[2][0][1] ;
		city_id = returnVal[2][0][2] ;
		loid = returnVal[2][0][0] ;
		oui = returnVal[2][0][4] ;
		device_serialnumber = returnVal[2][0][5] ;
		userId = returnVal[2][0][6] ;
		if(deviceId==""){
			$("#selectedDev").html("���û������ڻ�δ���նˣ�");
		}
	}
}

function doExecute(){
	var strategy_type  = $("select[@id='strategy_type']").val();
	var username  = $("select[@id='selectMap']").val();
	var url = "<s:url value="/gtms/config/voipChangeConfigAction!doConfigAll.action"/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("���ڲ��������Ե�....");
	
	if(CheckForm()){
		$.post(url,{
            strategy_type : strategy_type,
            deviceIds : deviceId,
            netusernames : netusername,
            city_ids : city_id,
            loids : loid,
            ouis : oui,
            faultList: faultListStr,
            taskid: taskid,
            userIds : userId
         },function(ajax){
				if("1"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("��ִ̨�гɹ�");
				}else {
					$("#resultDIV").html("");
					$("#resultDIV").append("��ִ̨��ʧ��");
				}
				$("div[@id='QueryData']").append(ajax);
				$("div[@id='QueryData']").html("");
				$("button[@name='button']").attr("disabled", true);
          });
	}
} 

</script>
</head>
<%@ include file="../../toolbar.jsp"%>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td HEIGHT=20>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									ҵ��
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">
									<span id="msg" style="color: red"></span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="voipChangeConfigQuery.jsp"%>
					</td>
				</tr>
				<tr>
					<th colspan="4" align="center">
						ҵ��
					</th>
				</tr>
				<tr>
					<td>
						<form name="frm" method="post" action="" onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<table width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<tr>
									<td bgcolor=#999999>
										<table border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev">
														���ѯ�û���
													</div>
												</td>
											</tr>
											<tr>
												<td colspan="6" align="right" CLASS="green_foot">
												<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute()" value=" ִ��" class=btn>
												</td>
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
								<td height="20">
								</td>
								</tr>
								<tr id="trData" style="display: none">
									<td class="colum">
										<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
											���ڲ��������Ե�....
										</div>
									</td>
								</tr>
								<tr>
									<td height="20">
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</td>
	</tr>
</table>
</body>
</html>
<%@ include file="../../foot.jsp"%>

