<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.Random"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%
	String instArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
	String gw_type = request.getParameter("gw_type");
	String telecom = LipossGlobals.getLipossProperty("telecom");
	Random random = new Random();
	int k = random.nextInt();
	int j = Math.abs(k % 100000000);
	if("1".equals(gw_type)){
		if(telecom.equals("CUC")){
			out.print("cuadmin" + j);
		}else{
			out.print("telecomadmin" + j);
		}
		
	}else if("2".equals(gw_type)){
		out.print("sme" + j);
	}else{
		out.print("admin" + j);
	}
%>