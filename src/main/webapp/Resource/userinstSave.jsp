<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<jsp:useBean id="UserInstAct" scope="request"
	class="com.linkage.litms.resource.UserInstAct" />
<%--
	zhaixf(3412) 2008-05-12
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<%
	String strMsg = "δ֪����";
	String strAction = request.getParameter("strAction");
	int chk = LipossGlobals.getChkInstUser();
	boolean isITMS = LipossGlobals.IsITMS();
	if ("modify".equals(strAction)) {
		strMsg = UserInstAct.userModifyConfig(request);
	}else{
		String username = request.getParameter("username");
		//String gwType = request.getParameter("gwType");
		String tabName = "";
		//if(gwType != null && "2".equals(gwType)){
		if(isITMS){
			tabName = "tab_hgwcustomer";
		}else{
			tabName = "tab_egwcustomer";
		}
		String sql = "select username, oui, device_serialnumber,device_id from " + tabName 
					+ " where username='" + username
					+ "' and user_state in ('1','2')";
		Map map = DataSetBean.getRecord(sql);

		if(map != null && map.size() > 0){
			String oui = String.valueOf(map.get("oui"));
			String device_serialnumber = String.valueOf(map.get("device_serialnumber"));
			if(map.get("device_id") != null && !"".equals(map.get("device_id"))){
				strMsg = "�û���" + username + "�Ѿ������豸��" + oui + "-" + device_serialnumber;
				strMsg += "; ����д�������ϵ����Ա";
			}else{
				strMsg = UserInstAct.saveConfig(request);
			}
		}else{
			if(chk == 1){
				strMsg = "�û��������ڣ���������û�";
			}else{
				//out.println("<SCRIPT LANGUAGE=\"JavaScript\">confirm(\"���û��������ݿ��в����ڣ�ȷ��Ҫ��ӣ�\")</SCRIPT>");
				strMsg = UserInstAct.saveConfig(request);
			}
		}
	}
	
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="80%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH align="center">
									��ʾ��Ϣ
								</TH>
							</TR>
							<TR height="50">
								<TD align=center valign=middle class=column>
									<%=strMsg%>
								</TD>
							</TR>
							<tr>
								<td class=column align="right">
									<input type="button" name="back" value=" �� �� "
										onclick="history.go(-1)">
								</td>
							</tr>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<BR><BR>
<%@ include file="../foot.jsp"%>
