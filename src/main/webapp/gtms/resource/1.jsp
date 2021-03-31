<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import ="acMyPackagesw.UpdateCityIdAndStatusForAH"%>
<%
try{
	String param = request.getParameter("parame");
	System.out.println("====param="+param+"========");
	UpdateCityIdAndStatusForAH act = new UpdateCityIdAndStatusForAH();
	String result = act.getDeviceIdAndIp(param);
	System.out.println("====="+result+"========");
}catch (Exception e) {
	e.printStackTrace();
}
%>
<html>
	<head>
	    <title>新增数图配置模板</title>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	</head>
	
	
	<body>
		<TABLE id="addTable">

			<tr>
				<td HEIGHT=20>&nbsp;
					
				</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								数图配置模板管理
							</td>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
			<TR>
				<TD>
					<TABLE class="querytable" width="100%">
						
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</body>
</html>