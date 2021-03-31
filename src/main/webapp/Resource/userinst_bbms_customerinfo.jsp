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
			desc = "（<font color='red'>此绑定设备厂商OUI为“null”，数据有问题，请联系管理员</font>）";
		}
		//如果用户已绑定了其他设备
		strData = "<TABLE width='98%' border=0 align='center'>"
			+ "<TR bgcolor=#FFFFFF><TD class=column2 style='border:1px solid #999999;'>"
			+ "该账号已绑定了设备"+chkUser.split("\\|")[0] + "-" + chkUser.split("\\|")[1]+"，请重新输入账号或先解绑！"+desc+"</TD></TR></TABLE>";
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
				+ "该账号没有相应的企业信息，请确保输入正确！如有问题，请联系管理员或手工添加！</TD></TR></TABLE>";
			//userInfo = strData;
		} else {
			//strData += "<TABLE width='100%' border=0 align='center'><TR><TD class=column2 colspan='5'><font color=='#0000FF'>目前该账号对应如下企业客户，请确认是否正确，如不正确请联系BSS重新派单！</font></TD></TR><TR><TH nowrap>客户ID</TH>"
			//+ "<TH>客户名称</TH><TH nowrap>联系人</TH><TH nowrap>联系电话</TH><TH nowrap>客户地址</TH></TR>";

			strData += "<table width='98%' border=0 cellspacing=1 cellpadding=2 align='center' bgcolor='#999999'><TR><th colspan='5'>用户对应的企业相关信息</th></TR><TR><TH nowrap>客户ID</TH>"
				+ "<TH>客户名称</TH><TH nowrap>联系人</TH><TH nowrap>联系电话</TH><TH nowrap>客户地址</TH></TR>";
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
			strData += "<font color='blue'>下面是该用户对应的企业信息，请确认是否正确，如有问题，请联系管理员！</font><td bgcolor=#FFFFFF align='right' colspan='5'><input type='submit' name='save_btn' value=' 保 存 '>&nbsp;&nbsp;<input type='hidden' name='customer_id' value='" +customer_id +"'><input type='hidden' name='city_id' value='" +city_id +"'></td>";
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




