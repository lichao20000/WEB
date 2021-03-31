<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
String servTypeList = HGWUserInfoAct.getEgwServTypeList();
String strCityList = DeviceAct.getCityListSelf(false, "", "", request);
String strOfficeList = DeviceAct.getOfficeList1(false,"","");
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function showVal(obj) {
	var serv_type_id = obj.value;
	document.all("childFrm").src = "jt_Work_handForm_list.jsp?serv_type_id=" + serv_type_id + "&refresh=" + new Date().getTime();
}	
function success(flag){
	if(flag=="true"){
		alert("数据保存成功!");
		document.all("frm").reset();
	}else{
		alert("数据保存失败!");
	}
}
//-->
</SCRIPT>
<form action="WorkSheet_BnetSave.jsp" target="childFrm" method="post" name="frm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT="20">
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162">
									<div align="center" class="title_bigwhite">
										Bnet手工工单
									</div>
								</td>
								<td>
									<img src="../images/attention_2.gif" width="15" height="12">
									请输入工单信息
								</td>
							</tr>
						</table>
					</TD>
				</TR>
				<TR>
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4">Bnet手工工单</TH>
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="right" width="20%">
									属地
								</TD>
								<TD align="left" width="30%">
									<%=strCityList%>
								</TD>
								<TD align="right" width="20%">
									局向
								</TD>
								<TD align="left" width="30%">
									<%=strOfficeList%>
								</TD>
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="right" width="20%">
									客户ID
								</TD>
								<TD align="left" width="30%">
									<input name="customer_id" type="text" value="">
								</TD>
								<TD align="right" width="20%">
									客户名称
								</TD>
								<TD align="left" width="30%">
									<input name="customer_name" type="text" value="">
								</TD>
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="right" width="20%">
									OUI
								</TD>
								<TD align="left" width="30%">
									<input name="oui" type="text" value="">
								</TD>
								<TD align="right" width="20%">
									序列号
								</TD>
								<TD align="left" width="30%">
									<input name="device_serialnumber" type="text" value="">
								</TD>
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="right" width="20%">
									设备类型
								</TD>
								<TD align="left" width="30%">
									<select name="device_type">
										<option value="Navigator1-1">Navigator1-1</option>
										<option value="Navigator1-2">Navigator1-2</option>
										<option value="Navigator2-1">Navigator2-1</option>
										<option value="Navigator2-2">Navigator2-2</option>
									</select>
								</TD>
								<TD colspan=2>
								</TD>															
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="right" width="20%">
									业务类型
								</TD>
								<TD align="left" width="30%">
									<%=servTypeList%>
								</TD>
								<TD align="right" width="20%">
									操作类型
								</TD>
								<TD align="left" width="30%">
									<select name="oper_type" class="bk">
										<option value="-1" selected>==请选择==</option>
									</select>
								</TD>
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="right" width="20%">
									业务帐号
								</TD>
								<TD align="left" width="30%">
									<input name="service_account" type="text" value=""> 
								</TD>
								<TD align="right" width="20%">
									业务密码
								</TD>
								<TD align="left" width="30%">
									<input name="service_pwd" type="password" value=""> 
								</TD>
							</TR>
							<TR bgcolor="#ffffff">
								<TD align="center" colspan=4 class=foot>
									<input name="btn" value="保存" type="submit">
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<SCRIPT LANGUAGE="JavaScript">
<!--
	showVal(document.all("some_service"));
//-->
</SCRIPT>
<IFRAME ID="childFrm" name="childFrm" STYLE="display:none"></IFRAME>
<%@ include file="../foot.jsp"%>