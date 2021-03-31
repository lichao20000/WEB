<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>IPTV开启ssid2</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
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
											IPTV开启ssid2
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
									<TABLE id="table_showConfig" width="100%" border=0 style="display:none"
										cellspacing=1 cellpadding=1 align="center" bgcolor=#999999 class="querytable">
										<tr>
											<th  align="center" width="100%">
												&nbsp;查&nbsp;询&nbsp;结&nbsp;果
											</th>
												
										</tr>
										
										<tr bgcolor="#FFFFFF" height="25">
											<td width="50%" colspan="4">
											<div id="selectedDev">请查询设备！</div>
										</tr>
										<tr bgcolor="#FFFFFF">
											<td width="50%" colspan="4">
											 <div id="fatherDiv">
											  </div>
											</td>
										</tr>
											
									</TABLE>
							</td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</body>
<script type="text/javascript">
	var deviceId ="";
	$(function(){
		gwShare_setGaoji();
	});
	function deviceResult(returnVal){
		deviceId ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
        deviceId = 	returnVal[2][0][0] ;	
        var deviceserialnumber = returnVal[2][0][2];  // add by zhangchy 2011-08-23 用于后台业务下发
		var oui = returnVal[2][0][1]; // add by zhangchy 2011-08-23 用于后台业务下发
		$("div[@id='selectedDev']").html("<strong>待操作设备:"+oui+"-"+deviceserialnumber+"</strong>");
		var url = "<s:url value='/gtms/config/wirelessConfigAction!getUserInfo.action'/>"; 
		$.post(url,{
			deviceId : deviceId 
		},function(ajax){
			$("#fatherDiv").html(ajax);
			$("#table_showConfig").attr("display","");
		});	
	}
	function doExecute(){
	
	 var url = "<s:url value='/gtms/config/wirelessConfigAction!doConfig.action'/>"; 
	   $("#doButton").attr("disabled",true);
	   $.post(
	             url,{
	             deviceId : deviceId,
	             gw_type : <%=gwType%>
	         } ,function(ajax){
	                $("#resultDIV").html("");
	                $("#doButton").attr("disabled",false);
	                if("1"==ajax){
	                      $("#resultDIV").append("调用后台成功");
	                 }else{
	                     $("#resultDIV").append("调用后台失败");
	          }
	  });
	}
	 	
	</script>
</html>
<%@ include file="../../foot.jsp"%>