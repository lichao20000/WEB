<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
  <table class="querytable">

			<TR>
				<th colspan="4">��������</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">ԭʼ����</TD>
				<TD width="80%"><%=request.getParameter("sheetContent") %></TD>
			</TR>
		</table>
</body>
</html>