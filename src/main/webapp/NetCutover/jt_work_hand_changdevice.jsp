<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.netcutover.*,java.util.Map,com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%

//ҵ������
String _type = request.getParameter("type");
//�豸�ͺ�
//String devicetype_id = request.getParameter("devicetype_id");
String gather_id = request.getParameter("gather_id");
String vpiid = request.getParameter("vpiid");
String vciid = request.getParameter("vciid");
// PVC 
String strPVC = "PVC:" + vpiid + "/" + vciid;

// ��������  �Ž�
String ConnectionType = request.getParameter("ConnectionType");

String[] list_LanInterface = request.getParameterValues("LanInterface");
String _LanInterface = "";
if(list_LanInterface != null ){
		for(int i=0;i<list_LanInterface.length;i++){
			if(i == 0){
				_LanInterface = list_LanInterface[i];
			} else {
				_LanInterface+="," + list_LanInterface[i];
			}
		}
	
}
String ServiceList = request.getParameter("ServiceList");

String oui = request.getParameter("old_oui");
String serialnumber = request.getParameter("old_device_serialnumber");

String _username = request.getParameter("username");
String _oui = request.getParameter("new_oui");
String _serialnumber = request.getParameter("new_device_serialnumber");

String tmpSql = "select * from tab_gw_device where oui='" + _oui + "' and device_serialnumber='" 
				+ _serialnumber + "' and device_status =1 ";
// teledb
if (DBUtil.GetDB() == 3) {
	tmpSql = "select device_id, devicetype_id from tab_gw_device where oui='" + _oui + "' and device_serialnumber='"
			+ _serialnumber + "' and device_status =1 ";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(tmpSql);
psql.getSQL();

Map fields = DataSetBean.getRecord(tmpSql);
String device_id = "";
String devicetype_id = "";
if(fields != null){
	device_id = (String)fields.get("device_id");
	devicetype_id	 = (String)fields.get("devicetype_id");
}

%>
<SCRIPT LANGUAGE="JavaScript">
	function goList(page){
		this.location = page;
	}
	function changdevice(){
		document.frm.target="childFrm";
		document.frm.action="jt_changdevice.jsp";
		document.frm.submit();
	}
	function doSend(){
		document.frm.action="jt_Work_send.jsp";
		document.frm.submit();
	}
</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<FORM name="frm" action=""  method="post">
<input type="hidden" name="type" value="<%=_type%>">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											�����豸��Ϣ
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										��ȷ���豸��Ϣ�Ƿ���ȷ��&nbsp;&nbsp;
									</td>

								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
								<TR>
									<TH colspan="4">�û���<%=_username %>���󶨵��豸��Ϣ</TH>
									<input type="hidden" name="username" value="<%=_username %>">
								</TR>							
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										ԭ���豸
									</TD>
									<TD align="left" width="80%" colspan="3">
										<%=oui%> - <%=serialnumber%>
										<input type="hidden" name="old_oui" value="<%=oui%>">
										<input type="hidden" name="old_device_serialnumber" value="<%=serialnumber%>">
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										�������豸
									</TD>
									<TD align="left" width="80%" colspan="3">
										<font color="green"><%=_oui%> - <%=_serialnumber%></font>
										<input type="hidden" name="new_oui" value="<%=_oui%>">
										<input type="hidden" name="new_device_serialnumber" value="<%=_serialnumber%>">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD HEIGHT="20">
							&nbsp;
						</TD>
					</TR>
					<%
						String information = "";
						
						if(fields == null){
							information = "<font color=\"red\">�豸��δ���ӱ�ϵͳ�������Ӻ󽫷��͹����������롾ȷ�ϡ���ȡ���롾���ء���</font>";
						} else {
							information = "ϵͳ���Ѿ����ڴ��豸,�롾���͹�������";
						}
										
					%>
					<TR>
						<TD>
							<font color="green">����ʾ��Ϣ����</font> <%=information%>
						</TD>
					</TR>
					<%
					
						if(fields == null){
					%>
					<TR>
						<TD HEIGHT="20">
							&nbsp;
						</TD>
					</TR>
					<TR>
						<TD align="center">
							<input type="button" name="send" value=" ȷ �� " onclick="changdevice();" class="btn"> &nbsp;&nbsp;&nbsp;
							<input type="button" name="return" value=" �� �� " onclick="goList('jt_Work_handForm.jsp');" class="btn">
						</TD>
					</TR>					
					
					<%		
						} else {				
					%>
					<TR>
						<TD align="right">
							<input type="button" name="send" value="  ���͹��� " onclick="doSend();" class="btn"> &nbsp;&nbsp;&nbsp;
							<input type="hidden" name="vpiid" value="<%=vpiid%>">
							<input type="hidden" name="vciid" value="<%=vciid%>">
							<input type="hidden" name="gather_id" value="<%=gather_id%>">
							<input type="hidden" name="ConnectionType" value="<%=ConnectionType%>">
							<input type="hidden" name="_LanInterface" value="<%=_LanInterface%>">
							<input type="hidden" name="ServiceList" value="<%=ServiceList%>">
							<input type="hidden" name="devicetype_id" value="<%=devicetype_id%>">
							<input type="hidden" name="device_id" value="<%=device_id%>">
						</TD>
					</TR>	
					<%
						}
					%>					
				</TABLE>
			</TD>
		</TR>

		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</form>
<%@ include file="../foot.jsp"%>
