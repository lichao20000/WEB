<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page
	import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@page import="java.util.Map"%>

<%
	String username = request.getParameter("username");
	String user_id = request.getParameter("user_id");
	String gwOptType = request.getParameter("gwOptType");
	String some_service = request.getParameter("some_service");
	String cityId = request.getParameter("cityId");
	String tabName = "";
	String sqlUser = "";
	
	if (gwOptType != null && (gwOptType.equals("21") || gwOptType.equals("22"))) {
		sqlUser = "select a.user_id from tab_egwcustomer a, tab_customerinfo b where a.username = '"
				+ username
				+ "' and (a.user_state = '1' or a.user_state = '2') and a.customer_id=b.customer_id ";
		if (!"00".equals(cityId)) {
			sqlUser += " and b.city_id in ("
					+ StringUtils.weave(CityDAO.getAllNextCityIdsByCityPid(cityId)) + ",'')";
		}

	} else {
		sqlUser = "select user_id from tab_hgwcustomer where username = '" + username
				+ "' and (user_state = '1' or user_state = '2')";

	}

	Cursor cursorTmp = DataSetBean.getCursor(sqlUser);
	Map userMap = cursorTmp.getNext();

	if (userMap != null) {
		if (gwOptType != null && (gwOptType.equals("11") || gwOptType.equals("21"))) {
			out.println("<font color='red'>该用户已存在</font>");
		} else if (user_id.equals(userMap.get("user_id"))) {

		} else {
			out.println("<font color='red'>该用户已存在</font>");
		}
	}
%>

