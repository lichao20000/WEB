<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>


<SCRIPT LANGUAGE="JavaScript">

// 1 家庭网关  2 企业网关
var flag = '<s:property value="flag" />';

function ToExcel(cityId) {
	var page="<s:url value='/Resource/countDevice!toExcel.action'/>?cityId=" + cityId+"&flag=" + flag;
	document.all("childFrm").src=page;
}

function detail(cityId,isBindDevice){

	var page="<s:url value='/Resource/countDevice!getDetail.action'/>?cityId="+cityId+"&isBindDevice="+isBindDevice+"&flag=" + flag;

	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}


</SCRIPT>




<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备资源统计计</title>
<%--
	/**
	 * 网关设备统计(家庭网关/企业网关)
	 *
	 * @author 段光锐(5250)
	 * @version 1.0
	 * @since 2008-1-16
	 * @category 资源管理
	 */
--%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<style type="text/css">
</style>
</head>
<body>
<form name="speclinefrm" action="<s:url value="/Resource/countDevice.action"/>"  method ="post" >
	<table width="98%" border=0 align="center" cellpadding="0" cellspacing="0">
		<tr><td height=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							设备资源统计
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							对网关设备进行统计
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class ="colum">
				<table class = "listtable">
					<thead>
					<tr>
						<th colspan="4" align="center">
							<s:iterator value="flag" var="fg"  status="status">
								<s:if test="flag == 1">家庭网关设备统计</s:if>
								<s:elseif test="flag == 2">企业网关设备统计</s:elseif>
								<s:else>网关设备统计</s:else>
							</s:iterator>
						</th>
					</tr>
					<tr class="green_title2">
						<th>属地</th>
						<th>所挂设备数</th>
						<th>与用户关联设备数</th>
					</tr>
					</thead>
					<tbody>
					<s:if test="resultList.size()>0">
							<s:iterator value="resultList">
								<tr>
									<td >
										<s:property value="city_name" />
									</td>

									<td >
										<a href="javascript:onclick=detail('<s:property value="city_id"/>','0');">
											<s:property value="devicenum" />
										</a>
									</td>
									<td >
										<a href="javascript:onclick=detail('<s:property value="city_id"/>','1');">
											<s:property value="cusnum" />
										</a>
									</td>
								</tr>
							</s:iterator>
							<TR>
								<TD colspan="4">
									<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
										style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>')">
							</TD>
							</TR>
						</s:if>
						<s:else>
							<TR ><TD colspan="4">系统中没有检索到相关数据！</TD></TR>
						</s:else>

					</tbody>
				</table>
			</td>
		</tr>
		<TR STYLE="display: none">
			<TD colspan="4">
				<iframe id="childFrm" src=""></iframe>
			</TD>
		</TR>
	</table>
</form>
</body>
</html>
<br>
<%@ include file="../foot.jsp"%>
