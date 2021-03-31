<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
	String tempdpi = request.getParameter("dpi");
	String isShowGJ = request.getParameter("isShowGJ");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>批量开关wifi</title>
	<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
	<link href="../../css/listview.css" rel="stylesheet" type="text/css">
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<%@ include file="../../toolbar.jsp"%>
	<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<TR>
			<TD>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										批量开关wifi
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<%@ include file="/gwms/share/gwShareDeviceQuery_wifiModifyEnb.jsp"%>
						</td>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td>
							<input type="hidden" name="param" value="" />
							<TABLE id="table_showConfig" width="100%" border=0
								cellspacing=0 cellpadding=0 align="center" class="querytable">
								<tr>
									<th align="center" width="100%">批量开关wifi</th>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td colspan="4"><div id="selectedDev">请查询设备！</div></td>
								</tr>
								<TR>
									<TD bgcolor=#999999>
										 <div id="fatherDiv">
											<div id="paramAddDiv0">
												<table border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td nowrap align="right" class=column width="20%">
															WIFI开关
														</td>
														<td width="70%" colspan="3" >
															 <select name="apEnable">
															   <option value="-1">===请选择===</option>
															   <option value="1">开启</option>
															   <option value="0">关闭</option>
															 </select>
														</td>
													</TR>
												</table>
											</div>
										</div>
										<table border=0 cellspacing=1 cellpadding=2 width="100%">
												<TR bgcolor="#FFFFFF">
													<TD colspan="4" align="right" class="green_foot">
														<button type="button" id="exeButton" name="exeButton"
															onclick="doExecute();" class=btn>
																	&nbsp;执&nbsp;行 &nbsp;
														</button>
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF">
													<TD colspan="4" align="left" class="green_foot"><div id="resultDIV" /></TD>
												</TR>
										</table>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
</body>

<script type="text/javascript">
	var deviceIds = "";
	var param = "";
	var isShowGJ = "<%=isShowGJ%>";
	$(function(){
		gwShare_setImport();
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	});
	
	function deviceResult(returnVal){
		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			if(5000<totalNum){
				alert("单次配置设备数应小于5000台！");
	       		$("#exeButton").attr("disabled",true);
	            return;
			}
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
			deviceIds = "0";
			param=returnVal[1];
		}

		if(5000<returnVal[0]){
			alert("单次配置设备数应小于5000台！");
       		$("#exeButton").attr("disabled",true);
            return;
		}
	}
	
	//执行配置
	function doExecute(){
	   var url = "<s:url value='/gtms/config/batchConfigWifi!doConfigAll.action'/>"; 
	   var apEnable = $("select[name='apEnable']").val();
 
	   if(apEnable == -1){
		   alert("请选择是打开还是关闭");
		   return false;
	   }
	   $.post(
               url,{
               deviceIds : deviceIds,
               apEnable : apEnable
        } ,function(ajax){
               $("#resultDIV").html("");
               $("#exeButton").attr("disabled",false);
               $("#resultDIV").append(ajax);  
       });
	}
</script>
</html>
<%@ include file="../../foot.jsp"%>