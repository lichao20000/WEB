<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ģ�⹤��ִ�н��</title>
<%@taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript">
function golist(){
	this.location = "/itms/itms/service/simulate/hlj/simulateSheet.jsp";
}
</script>
</head>
<body>
	<table class="listtable"  width="100%">
		<tr>
			<th align="center">BSSģ�⹤��������ʾ��Ϣ</th>
		</tr>
			<tr height="30">
				<td align="center" valign="middle">����ִ�н�� <s:property value="ajax"/></td>
			</tr>
		<tr>
			<td class="foot" align="right">
				<button onclick="golist();">&nbsp;����BSSģ�⹤��&nbsp;</button>
			</td>
		</tr>
	</table>
</body>
</html>