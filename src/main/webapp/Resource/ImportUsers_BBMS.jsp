<%@ include file="../timelater.jsp" %>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ include file="../head.jsp" %>
<%@ include file="../toolbar.jsp"%>

<% 
	String isSuccess = request.getParameter("isSuccess");
	if ("1".equals(isSuccess)) {
%>
	<script type="text/javascript">
		alert("用户资料上传成功！");
	</script>
<%
	} else if ("0".equals(isSuccess)) {
%>
		<script type="text/javascript">
			alert("用户资料上传失败！");
		</script>
<%		
	}else if ("lackDataErr".equals(isSuccess)) {
%>
		<script type="text/javascript">
			alert("所导入文件中：必填字段不完整，上传失败！");
		</script>
<%	
	}else if ("numErr".equals(isSuccess)) {
%>
		<script type="text/javascript">
			alert("所导入文件中：‘定制终端业务ID’、‘企业类型’ 必须全部为数字，上传失败！");
		</script>
<%		
	}
%>
<% 
String path = "user.xls";
%>
<script language="javascript">
<!--
	function checkForm(){
		var file = document.importUserForm.file.value;
		
		if (file == ''){
			alert('请选择需要导入的.xls文件！');
			return false;
		}
		
		if (file.indexOf('.xls') == -1){
			alert('请选择.xls格式的文件！');
			return false;
		}
		
		document.importUserForm.submit();
	}
//-->
</script>

<table border="0" cellspacing="0" cellpadding="0" width="95%" align="center">
	<tr>
	<td height="20">&nbsp;</td>
	</tr>
	<tr>
	<td>
		<form name="importUserForm" action="<s:url value="/Resource/importUsersBBMS.action"/>" method="POST" enctype="multipart/form-data">
		<table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
			<tr>
				<td>
					<table border="0" cellspacing="0" cellpadding="0" height="30" width="100%" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								用户资源
							</td>
							<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								上传用户文件到系统中。
						</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<tr>
							<th colspan="4">上传企业网关用户</th>
						</tr>
						<tr bgcolor="#ffffff">
							<td align="right" width="10%">请选择文件:</td>
							<td width="35%"><input type="file" name="file"></td>
							<td colspan="2" align="left">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" value=" 上 传 " onclick="checkForm()">
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" bgcolor=#ffffff>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="20">&nbsp;</td>
										<td width="2%">&nbsp;</td>
										<td colspan="2"><font color="red">文件格式：</font></td>
									</tr>
									<tr>
										<td height="15">&nbsp;</td>
										<td width="2%">1、</td>
										<td>文件为.xls格式</td>
									</tr>
									<tr>
										<td height="15">&nbsp;</td>
										<td width="2%" valign="top">2、</td>
										<td>文件中的第一行不入库，各字段为：<br>定制终端ID（必须）、所属区域（必须）、宽带账号（必须）、定制终端业务ID（必须）、终端安装地址（必须）、企业名称（必须）、联系人姓名（必须）、联系人电话（必须）、企业类型（可选）、 企业规模（可选）、企业地址（可选）</td>
									</tr>
									<tr>
										<tr>
										<td height="30">&nbsp;</td>
										<td width="2%"></td>
										<td><a href="<%= path%>">点击下载模板</a></td>
									</tr>
								</table>
							</td>
						</tr>
					</TABLE>
				</td>
			</tr>
		</table>
		</form>
	</td>
	</tr>
</table>
<%@ include file="../foot.jsp"%>












