<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>VOIP相关参数管理</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
var deviceIdArr ="";
$(function(){
	gwShare_setGaoji();
	gwShare_setImport();
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function config(flag){
	if(0==flag){
		$("#longTimer").attr("disabled",true);
		$("#shortTimer").attr("disabled",true);
		$("#longTimer").val("");
		$("#shortTimer").val("");
	}else{
		$("#longTimer").attr("disabled",false);
		$("#shortTimer").attr("disabled",false);
	}
}
function deviceResult(returnVal){

	$("#exeButton").attr("disabled",false);
	
	deviceIdArr = "";
	$("table[@id='table_showConfig']").css("display","");
	$("input[@name='longTimer']").val("");
	$("input[@name='shortTimer']").val("");
	$("#resultDIV").html("");
	
	var totalNum = returnVal[0];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
	}
	
	var deviceIdArray = returnVal[2];
	
	for(var i=0 ;i<deviceIdArray.length;i++){
		//遍历出来的deviceId
		deviceIdArr +=  deviceIdArray[i][0]+",";
	}
	var endIndex = deviceIdArr.lastIndexOf(",");
	deviceIdArr = deviceIdArr.substring(0,endIndex);
	if(totalNum > 100){
		alert("设备数量超过100台，影响到交互性能");
		$("#exeButton").attr("disabled",true);
		return;
	}
	
	// 判断未作策略的数量
	var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryUndoNum.action'/>"; 
	var maxNum = 50000;
	$.post(url,{} ,function(ajax){
          var num = parseInt(ajax);
           if(num > maxNum)
           {
           		alert("今天配置数已达到上限，请明日再配置！");
           		$("#exeButton").attr("disabled",true);
	            return;
           }
    });
}
function doExecute(){
	var configRadio = $("input[@type=radio][name='configRadio'][checked]").val();
	var longTimer = $("input[@name='longTimer']").val();
	var shortTimer = $("input[@name='shortTimer']").val();
	var serviceId  = "1411";
	var gwType = <%=gwType%>;
	var url = "<s:url value='/gtms/config/timerParamConfig!addToStrategy.action'/>"; 
	$("#exeButton").attr("disabled",true);
	if(checkForm(configRadio,longTimer,shortTimer)){
		$.post(url,{
					deviceIdArr : deviceIdArr,	
					configRadio : configRadio,
					longTimer : longTimer,
					shortTimer : shortTimer,
					serviceId : serviceId,
					gwType : gwType
		},function(ajax){
			$("#resultDIV").html("");
			$("#exeButton").attr("disabled",false);
			if("1"==ajax){
				$("#resultDIV").append("后台执行成功");
			}else if ("-4"==ajax){
				$("#resultDIV").append("后台执行失败");
			}
	});
	}
}
function checkForm(configRadio,longTimer,shortTimer){
	//当选择配置的时候才验证
    if("1"==configRadio){
		if(!IsNumber(longTimer,"长定时器")){
			$("#exeButton").attr("disabled",false);
			return false;
		}	
		if(!IsNumber(shortTimer,"短定时器")){
		$("#exeButton").attr("disabled",false);
			return false;
		}
	}
	return true;	
}
</SCRIPT>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									VOIP相关参数管理
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm">
							<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0
								align="center" style="display:none" class="querytable">
								<tr  bgcolor="#FFFFFF">
									<th colspan="4"  align="center">
										 配置 策略
									</th>								
								</tr>
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<td nowrap align="right" class=column width="15%">
													长短定时器
												</td>
												<td id="" width="85%" colspan="3">
													<input  type="radio"  name="configRadio" name="configRadio" onclick="config(0);" value="0">不配置</input>
													&nbsp;&nbsp;&nbsp;
													<input  type="radio"  name="configRadio" onclick="config(1)" value="1" checked="checked">配置</input>
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td nowrap align="right" class=column width="15%">
													长定时器
												</td>
												<td id="" width="35%" >
													<input  type="text" id="longTimer" name="longTimer" value=""/>
												</td>
												<td nowrap align="right" class=column width="15%">
													短定时器
												</td>
												<td id="" width="35%">
													<input  type="text" id= "shortTimer" name="shortTimer" value=""/>
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<button type="button" id="exeButton" name="exeButton"
													 onclick="doExecute();" class=btn> &nbsp;执 &nbsp;行 &nbsp; </button>
												</TD>
											</TR>
											<TR  bgcolor="#FFFFFF">
												<TD colspan="4" align="left" class="green_foot" >
													<div id="resultDIV"/>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
</TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>