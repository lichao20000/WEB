<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.Map"%>

<jsp:useBean id="UserInstAct" scope="request" class="com.linkage.litms.resource.UserInstAct" />
<%
	request.setCharacterEncoding("GBK");
	String username = request.getParameter("username");
	String strData = "";
	String desc = "";
	//String userInfo = "";
	//String customerInfo = "";
	UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
	String cityId = userRes.getCityId();
	
	String chkUser = UserInstAct.checkUser(username, cityId);
	
	if (null != chkUser && !"null".equals(chkUser.split("\\|")[1])) {
		if("null".equals(chkUser.split("\\|")[0])) {
			desc = "��<font color='red'>�˰��豸����OUIΪ��null�������������⣬����ϵ����Ա</font>��";
		}
		//����û��Ѱ��������豸
		strData = "<TABLE width='98%' border=0 align='center'>"
			+ "<TR bgcolor=#FFFFFF><TD class=column2 style='border:1px solid #999999;'>"
			+ "���˺��Ѱ����豸"+chkUser.split("\\|")[0] + "-" + chkUser.split("\\|")[1]+"�������������˺Ż��Ƚ��"+desc+"</TD></TR></TABLE>";
		//userInfo = strData;
	} else {
		Cursor cursor = UserInstAct.getCustomerInfo_bbms(username, cityId);
		Map fields = cursor.getNext();
		String customer_id = null;
		String customer_name = null;
		String customer_address = null;
		String linkphone = null;
		String linkman = null;
		String city_id = null;

		if (fields == null) {
			strData = "<TABLE width='98%' border=0 align='center'>"
				+ "<TR bgcolor=#FFFFFF><TD class=column2 style='border:1px solid #999999;'>"
				+ "���˺�û����Ӧ����ҵ��Ϣ����ȷ��������ȷ���������⣬����ϵ����Ա���ֹ���ӣ�</TD></TR></TABLE>";
			//userInfo = strData;
		} else {
			//strData += "<TABLE width='100%' border=0 align='center'><TR><TD class=column2 colspan='5'><font color=='#0000FF'>Ŀǰ���˺Ŷ�Ӧ������ҵ�ͻ�����ȷ���Ƿ���ȷ���粻��ȷ����ϵBSS�����ɵ���</font></TD></TR><TR><TH nowrap>�ͻ�ID</TH>"
			//+ "<TH>�ͻ�����</TH><TH nowrap>��ϵ��</TH><TH nowrap>��ϵ�绰</TH><TH nowrap>�ͻ���ַ</TH></TR>";

			strData += "<table width='98%' border=0 cellspacing=1 cellpadding=2 align='center' bgcolor='#999999'><TR><th colspan='5'>�û���Ӧ����ҵ�����Ϣ</th></TR><TR><TH nowrap>�ͻ�ID</TH>"
				+ "<TH>�ͻ�����</TH><TH nowrap>��ϵ��</TH><TH nowrap>��ϵ�绰</TH><TH nowrap>�ͻ���ַ</TH></TR>";
			while (fields != null) {
				customer_id = (String) fields.get("customer_id");
				customer_id = customer_id.replaceAll("\\+", "%2B");
				customer_id = customer_id.replaceAll("&", "%26");
				customer_id = customer_id.replaceAll("#", "%23");
				customer_name = (String) fields.get("customer_name");
				linkman = (String) fields.get("linkman");
				linkphone = (String) fields.get("linkphone");
				customer_address = (String) fields.get("customer_address");
				city_id = (String) fields.get("city_id");
				
				strData += "<TR bgcolor=#FFFFFF><TD class=column2 width='10%'>"+customer_id+"</TD>";
				strData += "<TD class=column2 width='20%'>" + customer_name
						+ "</TD>";
				strData += "<TD class=column2 width='15%'>"
						+ linkman + "</TD>";
				strData += "<TD class=column2 width='15%'>"
						+ linkphone
						+ "</TD>";
				strData += "<TD class=column2 width='40%'>"
					+ customer_address
					+ "</TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<font color='blue'>�����Ǹ��û���Ӧ����ҵ��Ϣ����ȷ���Ƿ���ȷ���������⣬����ϵ����Ա��</font><td bgcolor=#FFFFFF align='right' colspan='5'><input type='submit' name='save_btn' value=' �� �� '>&nbsp;&nbsp;<input type='hidden' name='customer_id' value='" +customer_id +"'><input type='hidden' name='city_id' value='" +city_id +"'></td>";
			strData += "</TBALE>";
			
			//customerInfo = strData;
		}
	}
%>
<span ID="child"><%=strData%></span>
<script LANGUAGE="JavaScript">
parent.document.all("customerinfo_str").style.display="";
parent.document.all("customerinfo_table").innerHTML = child.innerHTML;
</script>




