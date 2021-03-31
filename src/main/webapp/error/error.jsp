<%@ page language="java" contentType="text/html; charset=GBK" isErrorPage="true" pageEncoding="GBK"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setStatus(HttpServletResponse.SC_OK);   
  
      %> 
<html>
<head>
<title>你访问的页面不存在或被删除！</title>

<style type=text/css>
.font14 {
	font-size: 14px
}

.font12 {
	font-size: 12px
}

.font12 a {
	font-size: 12px;
	color: #cc0000;
	text-decoration: none;
}
</style>

</head>
<body>
	<table height="500" cellspacing="0" cellpadding="0" width="500" align="center"  border="0">	
		<tbody>
			<tr>
				<td height=330></td>
			</tr>
			<tr>
				<td valign=top>
					<div class=font14 align=center>
						<strong>你访问的页面<font color=#0099ff>不存在</font>或被<font
							color=#ff0000>删除！<br></font></strong>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
