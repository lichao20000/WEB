<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean, com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<jsp:useBean id="UserInstAct" scope="request"
	class="com.linkage.litms.resource.UserInstAct" />
<%
	String strMsg = "δ֪����";
	String bindMsg = "";
	String exeMsg = "";
	String username = "";
	String strAction = request.getParameter("strAction");
	UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
	String cityId = userRes.getCityId();
	
	int chk = LipossGlobals.getChkInstUser();
	if ("modify".equals(strAction)) {
		strMsg = UserInstAct.userModifyConfig(request);
	}else{
		username = request.getParameter("username");
		StringBuffer sb = new StringBuffer();
		sb.append("select a.oui,a.device_serialnumber from tab_egwcustomer a, tab_customerinfo b where username='");
		sb.append(username);
		sb.append("' and a.user_state in ('1','2') and a.customer_id = b.customer_id ");
		if (!"00".equals(cityId)) {
			sb.append("and b.city_id in(");
			sb.append(StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(cityId)));
			sb.append(")");
		}
		
		//String sql = "select username, oui, device_serialnumber from tab_egwcustomer where username='" + username
		//			+ "' and user_state in ('1','2')";
		
		Map map = DataSetBean.getRecord(sb.toString());

		if(map != null && map.size() > 0){
			String oui = (String) map.get("oui");
			String device_serialnumber = (String) map.get("device_serialnumber");
			if(device_serialnumber != null && !"".equals(device_serialnumber)&& !"null".equals(device_serialnumber)){
				strMsg = "�û���" + username + "�Ѿ������豸��" + oui + "-" + device_serialnumber;
				strMsg += "; ����д�������ϵ����Ա";
			}else{
				strMsg = UserInstAct.saveConfig_bbms(request);
			}
		}else{
			if(chk == 1){
				strMsg = "�û��������ڣ���������û�";
			}else{
				//out.println("<SCRIPT LANGUAGE=\"JavaScript\">confirm(\"���û��������ݿ��в����ڣ�ȷ��Ҫ��ӣ�\")</SCRIPT>");
				strMsg = UserInstAct.saveConfig_bbms(request);
			}
		}
	}
	if (strMsg.indexOf("|") != -1) {
		bindMsg = strMsg.split("\\|")[0];
		exeMsg = strMsg.split("\\|")[1];
	} else {
		bindMsg = strMsg;
	}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function pageJump(username) {
		window.location.href= "../NetCutover/WorkSheetSearch_bbms.jsp?username="+username;
	}
//-->
</SCRIPT>
  
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="80%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH align="center" colspan="2">
									�ֳ���װ��ʾ��Ϣ
								</TH>
							</TR>
							<TR height="50">
								<TD align=center valign=middle class=column>
									��״̬��<%=bindMsg%>
								</TD>
							</TR>
							<tr>
								<td class=column colspan="2" align="right">
									<input type="button" name="back" value=" �� �� " onclick="history.go(-1)">
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
