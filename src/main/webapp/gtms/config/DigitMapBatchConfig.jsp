<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
	String flag = request.getParameter("flag");
 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>语音数图批量修改</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
		
<SCRIPT LANGUAGE="JavaScript">
var deviceId ="";
$(function(){
	gwShare_setGaoji();
	gwShare_setImport();
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function deviceResult(returnVal){
	
	$("#doButton").attr("disabled",false);
	
	deviceId="";
	$("#resultDIV").html("");
	
	var totalNum = returnVal[0];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
	}
		
	var deviceIdArray = returnVal[2];
	for(var i=0 ;i<deviceIdArray.length;i++){
		//遍历出来的deviceId
		deviceId +=  deviceIdArray[i][0]+",";
	}
	var endIndex = deviceId.lastIndexOf(",");
	deviceId = deviceId.substring(0,endIndex);
	if(totalNum > 100){
		alert("设备数量超过100台，影响到交互性能");
		$("#doButton").attr("disabled",true);
		return;
	}
	$("div[@id='selectedDev']").html("<font size=2>"+totalNum+"</font>");
	
	// 判断未作策略的数量
	var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryUndoNum.action'/>"; 
	var maxNum = 50000;
	$.post(url,{} ,function(ajax){
           var num = parseInt(ajax);
           if(num > maxNum)
           {
           		alert("今天配置数已达到上限，请明日再配置！");
           		$("#doButton").attr("disabled",true);
	           return;
           }
    });
}
function downloadCase() {
	var url = "<s:url value='/gtms/config/digitMapConfig!download.action'/>";
		var caseDownload = $('input[name="caseDownload"]:checked').val();
			document.getElementById("frm").action = url;
		document.getElementById("frm").submit();
	}
function doExecute(){
	var map_id = $("select[@id='map_id']").val();
	var url = "<s:url value='/gtms/config/digitMapConfig!doConfigAll.action'/>";
	$("#doButton").attr("disabled",true);
	$.post(url,{
			          flag : <%=flag%>,
              map_id : map_id, 
              device_id : deviceId,
              gw_type: <%=gwType%>
           },function(ajax){
           	$("#resultDIV").html("");
	         	$("#doButton").attr("disabled",false);
				if("1"==ajax){
					$("#resultDIV").append("后台执行成功");
				}else if ("-4"==ajax){
					$("#resultDIV").append("后台执行失败");
				}else{
					$("#resultDIV").append("后台执行异常");
				}
            });
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
											虚拟网语音数图手工配置
										</td>
										<td nowrap>
											<img src="../../images/attention_2.gif" width="15"
												height="12">
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
						<tr bgcolor="#FFFFFF">
							<td>
								<FORM NAME="frm" METHOD="post" ACTION="" >
									<TABLE id="table_showConfig" width="100%" border=0
										cellspacing=0 cellpadding=0 align="center"
										style="" class="querytable">
										<tr>
											<th colspan="2"  align="center">
												虚拟网语音数图手工配置
											</th>
										</tr>
										<TR>
											
											<TD bgcolor=#999999>
												<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td nowrap align="right" class=column width="30%">
															导入设备的数量
														</td>
														<td width="70%">
															<div id="selectedDev"></div>
														</td>
													</TR>
													<TR bgcolor="#FFFFFF" >
														<td  align="right" class=column width="30%">
															数图模板配置
														</td>
														<td  width="70%" >
															<s:select list="digitMapList" name="map_id" id="map_id" listKey="map_id" listValue="map_name" 
															 cssClass="bk"></s:select>
														<span >&nbsp;&nbsp;&nbsp;
															导入查询案例模板下载:
															<input type="radio" width="22%" name="caseDownload" value="1" onclick="downloadCase()">txt模板</input>
														<input type="radio" width="22%" name="caseDownload" value="0" onclick="downloadCase()">xls模板</input>
														</span>
														</td>
														
													</TR>
													<TR bgcolor="#FFFFFF">
														<TD colspan="2" align="right" class="green_foot">
															<button type="button" id="doButton" name="doButton"
																onclick="doExecute();" class=btn disabled="true">
																&nbsp;下&nbsp;发&nbsp;配&nbsp;置 &nbsp;
															</button>
														</TD>
													</TR>
													<TR bgcolor="#FFFFFF">
														<TD colspan="4" align="left" class="green_foot">
															<div id="resultDIV" />
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