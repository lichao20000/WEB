
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
			request.setCharacterEncoding("gbk");
			DeviceAct deviceAct = new DeviceAct();
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="updatedevice.jsp" onSubmit="return CheckForm();">
			<input type="hidden" name="prot_type"/>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									中间件管理
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="2" align="center">
													批量接口
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													设备信息数据的初始同步:
												</TD>
												<TD align="left" style="padding-left:20px">
													<div><a href="export_device_info.jsp">导出设备信息数据到excel文件</a></div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD height="25" align="right" width="25%" style="padding-right:40px">
													域信息数据的初始同步:
												</TD>
												<TD align="left" style="padding-left:20px">
													<div><a href="export_area_info.jsp">导出域信息数据到excel文件</a></div>
												</TD>
											</TR>
											<TR>
												<TD colspan="2" height="25" align="right" CLASS="green_foot">
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
</TABLE>
<%@ include file="../foot.jsp"%>