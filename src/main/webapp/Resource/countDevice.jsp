<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>


<SCRIPT LANGUAGE="JavaScript">

// 1 ��ͥ����  2 ��ҵ����
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
<title>�豸��Դͳ�Ƽ�</title>
<%--
	/**
	 * �����豸ͳ��(��ͥ����/��ҵ����)
	 *
	 * @author �ι���(5250)
	 * @version 1.0
	 * @since 2008-1-16
	 * @category ��Դ����
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
							�豸��Դͳ��
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							�������豸����ͳ��
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
								<s:if test="flag == 1">��ͥ�����豸ͳ��</s:if>
								<s:elseif test="flag == 2">��ҵ�����豸ͳ��</s:elseif>
								<s:else>�����豸ͳ��</s:else>
							</s:iterator>
						</th>
					</tr>
					<tr class="green_title2">
						<th>����</th>
						<th>�����豸��</th>
						<th>���û������豸��</th>
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
									<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
										style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>')">
							</TD>
							</TR>
						</s:if>
						<s:else>
							<TR ><TD colspan="4">ϵͳ��û�м�����������ݣ�</TD></TR>
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
