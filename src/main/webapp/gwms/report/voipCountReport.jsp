<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<title>语音业务按协议统计报表</title>
<%-- <SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jsDate/WdatePicker.js"/>"></SCRIPT> --%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/progress.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<script type="text/javascript">
	
	function doQuery(){
		$("#button1").attr("disabled", true);
		var gw_type = "<s:property value='gw_type' />";
		
		var cityId=$("select[@name='cityId']").val();
		$("div[@id='resultData']").html("正在获取统计数据...");
		startProgress();
		
		var url = "<s:url value='/gwms/report/voipCountReportACT!queryDataList.action'/>";
		$.post(url,{
			cityId:cityId,
			gw_type:gw_type
		},function(ajax){
		    $("div[@id='resultData']").html("");
			$("div[@id='resultData']").append(ajax);
			stopProgress();
		});	
		
		$("#button1").attr("disabled", false);
	}
	
	//点击数字二级页面
	function getdetailList(gw_type,dataCityId,protocol){
		var cityId=$("select[@name='cityId']").val();
		var page = "<s:url value='/gwms/report/voipCountReportACT!querydetailList.action'/>"
			page+="?gw_type="+gw_type+"&cityId="+cityId+"&protocol="+protocol+"&dataCityId="+dataCityId;
		window.open(page,"","left=20,top=20,width=850,height=700,resizable=no,scrollbars=yes");
	}
	
	// 导出列表数据
	function queryDataForExcel(gw_type,city_id){
		var url = "<s:url value='/gwms/report/voipCountReportACT!queryDataList.action'/>";
		document.reportForm.action = url+"?returnType=excel&gw_type="+gw_type+"&cityId="+city_id;
		document.reportForm.method = "post";
		document.reportForm.submit();
	}
	
</script>
</head>

<body>
<form id="form" name="selectForm" action="" method="post">
<br>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
						<tr>
							<td width="182" align="center" class="title_bigwhite">
							语音业务按协议统计
							</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"/></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">

						<tr>
							<th colspan="6">
							语音业务按协议统计
							</th>
						</tr>

						<tr>
							<td class=column width="10%" align='right'>厂商</td>
							<TD class=column width="35%" ><s:select list="cityList" name="cityId"
									headerKey="" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
						</tr>
						<tr>
							<td colspan="6" align="right" class=foot>
								<button id="button1" onclick="doQuery()">&nbsp;查 询&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height = "20">
				</td>
			</tr>
			<tr>
				<td align="center">
					<div id="resultData" ></div>
					<div id="progress"></div>
				</td>
			</tr>

		</table>
		<br>
	</form>
<form name="reportForm"></form>
</body>
</html>
<%@ include file="../../foot.jsp"%>