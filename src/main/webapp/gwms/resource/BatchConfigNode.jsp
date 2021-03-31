<%--
Author      : 张四辈
Date		: 2013-6-5
Desc		: 批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">

	/**
	 *将高级查询，导入查询和升级导入查询显示出来
	 **/
	$(function() {
		$("input[@name='gwShare_queryResultType']").val("checkbox");
		gwShare_setGaoji();
		gwShare_setImport();
	});
	var deviceIds = "";
	
	/**
	 *自动调节iframe高度
	 **/
	function iFrameHeight() { 
		var ifm= document.getElementById("iframepage"); 
		var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument; 
		if(ifm != null && subWeb != null) { 
		ifm.height = subWeb.body.scrollHeight; 
			} 
		} 
		
	/**
	 * 验证并提交
	 **/
	function CheckForm() {
		var filename = $("input[@name='gwShare_fileName']").val();
		var nodeIds = $("input[@name='nodeIds']").val();

		if (null == filename || filename == "") {
			if ($("input[@name='deviceIds']").val() == "") {
				alert("请选择设备！");
				return false;
			}
		}
		if (null == nodeIds || nodeIds == "") {
			alert("请上传节点！");
			return false;
		}

		document.frm.action = '<s:url value="/gwms/resource/batchConfigNodeACT!importTask.action"/>';
		document.frm.submit();
	}

	/**
	 * 查询出设备
	 **/
	function deviceResult(returnVal) {
		deviceIds = "";
		if (returnVal[0] == 0) {
			$("div[@id='selectedDev']").html(
					"<font size=2><strong>待操作设备数目:" + returnVal[2].length
							+ "</strong></font>");
			for ( var i = 0; i < returnVal[2].length; i++) {
				deviceIds = deviceIds + returnVal[2][i][0] + ",";
			}
			$("input[@name='deviceIds']").val(deviceIds);
		} else {
			$("div[@id='selectedDev']").html(
					"<font size=2><strong>待操作设备数目:" + returnVal[0]
							+ "</strong></font>");
			$("input[@name='deviceIds']").val("0");
			$("input[@name='param']").val(returnVal[1]);
		}

	}
	/**
	 *查询出设备
	 **/
	function deviceUpResult(returnVal) {
		$("div[@id='selectedDev']").html(
				"<font size=2><strong>待操作设备数目:" + returnVal[0]
						+ "</strong></font>");
		var gw_type = $("input[@name='gw_type']").val();
		$("input[@name='gwShare_fileName']").val(returnVal[1]);
		$("input[@name='gwShare_queryType']").val(returnVal[2]);
	}
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
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
									批量配置采集节点</td>
								<td nowrap><img
									src="<s:url value='/images/attention_2.gif'/>" width="15"
									height="12"></td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4"><%@ include
							file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">配置采集策略</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post">
							<input type="hidden" name="deviceIds" value="" /> <input
								type="hidden" name="gwShare_fileName" value="" /> <input type="hidden"
								name="param" value="" /> <input type="hidden" name="gw_type"
								value="<%=gwType%>" /> <input type="hidden"
								name="gwShare_queryType" value="" />

							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">请查询设备！</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="softUp">
											<tr bgcolor="#FFFFFF">
												<td align="right" width="15%">提交文件</td>
												<td colspan="3" width="85%">
													<div id="importNodename">
															<iframe id="iframepage" name="iframepage" FRAMEBORDER=no SCROLLING=yes src="<s:url value="/gwms/resource/NodeFileUpload.jsp"/>" onLoad="iFrameHeight()" width="100%">
															</iframe>
															<input type="hidden" name=nodeIds value=""/>		
													</div>
												</td>
											</tr>
											<tr>
												<td CLASS="green_foot" align="right">注意事项</td>
												<td colspan="3" CLASS="green_foot">
													1、需要导入的文件格式限于文本文件，即txt格式 。 <br> 2、文件的第一行为标题行，即【采集节点】。
													<br> 3、文件只有一列。 <br> 4、文件行数最多50，以免影响性能。
												</td>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot"><INPUT
													TYPE="button" value=" 执 行 " class=btn
													" onclick="CheckForm()"></TD>
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
	<TR>
		<TD HEIGHT=20>&nbsp; <IFRAME ID=childFrm SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm1 SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm2 SRC=""
				STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
