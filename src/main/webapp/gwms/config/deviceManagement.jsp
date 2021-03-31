<%--
Author      : 王森博
Date		: 2009-12-1
Desc		: 
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
	function CheckForm(){
		$("input[@name='deviceIds']").val("");
		if($("input[@name='device_id'][@checked]").val()==undefined){
			alert("请选择设备！");
			return false;
		}
		var acsurl = $("input[@name='acsUrl']").val();
		if(acsurl.indexOf("*")>0){
			alert("请输入正确的ACS URL地址！");
			$("input[@name='acsUrl']").focus();
			return false;
		}
		var deviceIds = "";
		$("input[@name='device_id'][@checked]").each(function(){ 
		   	deviceIds = deviceIds + $(this).val()+",";
		   });
		$("input[@name='deviceIds']").val(deviceIds);
		return true;
	}
	
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="checkbox" />
	<jsp:param name="jsFunctionName" value="" />
	<jsp:param name="div_device_height" value="150" />
	<jsp:param name="listControl" value="100" />
	<jsp:param name="byImport" value="0" />
</jsp:include>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="<s:url value="/gwms/config/devManage.action"/>"
				onsubmit="return CheckForm()">
				<input type="hidden" name="deviceIds" value="" />
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										更改ACS URL
									</td>
									<td nowrap>
										<img src="<s:url value='/images/attention_2.gif'/>" width="15"
											height="12">
										修改设备的ACS URL
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<div id="selectDevice">
								<span>加载中....</span>
							</div>
						</td>
					</TR>
					<TR>
						<TH colspan="4" align="center">
							ACS路径
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ACS URL
												</TD>
												<TD width="85%" colspan="3">
														<input type="text" name="acsUrl" value="http://*:*/ACS-server/ACS" class=bk size="50">													
														<!-- <input type="text" name="acsUrl" value="http://192.168.32.231:6060/ACS-server/ACS" class=bk size="50"
															readonly="readonly"> -->													
													<font color="red"> *</font>
												</TD>
											</TR>

											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">

													<INPUT TYPE="submit" value=" 执 行 " class=btn>

												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
